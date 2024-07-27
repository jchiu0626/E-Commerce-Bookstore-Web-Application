package business.order;

import api.ApiException;
import business.book.Book;
import business.book.BookDao;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.CustomerDao;
import business.customer.CustomerForm;
import business.customer.Customer;
import business.JdbcUtils;
import business.BookstoreDbException;

import java.time.DateTimeException;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;
	private LineItemDao lineItemDao;
	private CustomerDao customerDao;
	private OrderDao orderDao;

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	public void setLineItemDao(LineItemDao lineItemDao) {
		this.lineItemDao = lineItemDao;
	}
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}


	@Override
	public OrderDetails getOrderDetails(long orderId) {
		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.getCustomerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.getBookId()))
				.collect(Collectors.toList());
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);

		try (Connection connection = JdbcUtils.getConnection()) {

			Date date = getDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());

			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					date, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}
	}

	private Date getDate(String monthString, String yearString) {
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(yearString), Integer.parseInt(monthString));
		Date date = Date.from(yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}

	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId,
						item.getBookId(), item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}

	private int generateConfirmationNumber() {
		Random random = new Random();
		return random.nextInt(999999999);
	}

	private void validateCustomer(CustomerForm customerForm) {

		String name = customerForm.getName();

		if (name == null || name.equals("") || name.length() > 45 || name.length() < 4) {
			throw new ApiException.ValidationFailure("name","Invalid name field");
		}

		String address = customerForm.getAddress();

		if (address == null || address.equals("") || address.length() > 45 || address.length() < 4) {
			throw new ApiException.ValidationFailure("address","Invalid address field");
		}

		String phone = customerForm.getPhone();

		if (phone == null || phone.equals("") || phone.replaceAll("\\D", "").length() != 10){
			throw new ApiException.ValidationFailure("phone","Invalid phone field");
		}

		String email = customerForm.getEmail();
		if (email != null) {
			int firstIndex = email.indexOf("@");
			int lastIndex = email.lastIndexOf("@");
			if (firstIndex != lastIndex) {
				throw new ApiException.ValidationFailure("email","Invalid email field");
			}
		}
		if (email == null || email.equals("") || email.contains(" ") || !email.contains("@") || email.endsWith(".")){
			throw new ApiException.ValidationFailure("email","Invalid email field");
		}

		String ccNumber = customerForm.getCcNumber();

		if (ccNumber == null || ccNumber.equals("") || ccNumber.replaceAll("[-\\s]+","").length() > 16
				|| ccNumber.replaceAll("[-\\s]+","").length() < 14){
			throw new ApiException.ValidationFailure("ccNumber","Invalid ccNumber field");
		}

		// TODO: Validation checks for address, phone, email, ccNumber

		if (expiryDateIsInvalid(customerForm.getCcExpiryMonth(), customerForm.getCcExpiryYear())) {
			throw new ApiException.ValidationFailure("Please enter a valid expiration date.");
		}
	}

	private boolean expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {
		if (ccExpiryMonth == null || ccExpiryYear == null || ccExpiryMonth == "" || ccExpiryYear == ""
				|| Integer.parseInt(ccExpiryMonth) > 12) {
			return true;
		}

		// TODO: return true when the provided month/year is before the current month/yeaR
		// HINT: Use Integer.parseInt and the YearMonth class
		int intMonth = Integer.parseInt(ccExpiryMonth);
		int intYear = Integer.parseInt(ccExpiryYear);

		YearMonth currentYearMonth = YearMonth.now();
		int currentYear = currentYearMonth.getYear();
		int currentMonth = currentYearMonth.getMonthValue();
		if (intYear < currentYear){
			return true;
		} else if (intYear == currentYear && intMonth < currentMonth) {
			return true;
		}
		return false;
	}

	private void validateCart(ShoppingCart cart) {

		if (cart.getItems().size() <= 0) {
			throw new ApiException.ValidationFailure("Cart is empty.");
		}

		cart.getItems().forEach(item-> {
			if (item.getQuantity() < 0 || item.getQuantity() > 99) {
				throw new ApiException.ValidationFailure("Invalid quantity");
			}
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			// TODO: complete the required validations
			if (item.getBookForm().getPrice() != databaseBook.getPrice()){
				throw new ApiException.ValidationFailure("Price doesn't match");
			}
			if (item.getBookForm().getCategoryId() != databaseBook.getCategoryId()){
				throw new ApiException.ValidationFailure("Category doesn't match");
			}
		});
	}

}
