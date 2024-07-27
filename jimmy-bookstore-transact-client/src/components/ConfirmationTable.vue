<script setup lang="ts">
// import { getImageUrl } from "@/main";
import { useCartStore } from "@/stores/cart";
import { asDollarsAndCents } from "@/main";

import { useOrderDetailsStore } from "@/stores/orderDetails";
import type { BookItem, OrderDetails } from "@/types";

const cartStore = useCartStore();
const orderDetailsStore = useOrderDetailsStore();
const orderDetails: OrderDetails = orderDetailsStore.orderDetails;

const subtotal = orderDetails.lineItems.reduce(
  (sum, item, index) => sum + item.quantity * orderDetails.books[index].price,
  0
);
// A helper function - optional to use
const bookAt = function (orderDetails: OrderDetails, index: number): BookItem {
  return orderDetails.books[index];
};
</script>



<style scoped>
table {
  border: 1px black solid;
  width: fit-content;
  margin-top: 1em;
  margin-bottom: 1em;
}

td {
  display: table-cell;
  padding: 0.5em 0.5em;
  background-color: white;
  vertical-align: middle;
}

td:nth-child(1) {
  text-align: left;
}

td:nth-child(2) {
  text-align: left;
}

td:nth-child(3) {
  text-align: left;
}

td:nth-child(4) {
  text-align: left;
}

ul {
  text-align: left;
}

ul>li {
  margin: 0.25em;
}
</style>


<template>
  <table>
    <tr>
      <td>Book Name</td>
      <td>Quantity</td>
      <td>Price</td>
      <td>Total Price</td>
    </tr>
    <tr v-for="(item, index) in orderDetails.lineItems" :key="item.bookId">
      <td>
        {{ orderDetails.books[index].title }}
      </td>
      <td>{{ item.quantity }}</td>
      <td>{{ asDollarsAndCents(orderDetails.books[index].price) }}</td>
      <td>{{ asDollarsAndCents(orderDetails.books[index].price * item.quantity) }}</td>
    </tr>
  </table>
  <ul>
    <li>Subtotal: <strong>{{ asDollarsAndCents(subtotal) }}</strong><br></li>
    <li>Subcharge: <strong>5.00</strong><br></li>
    <li>Total: <strong>{{ asDollarsAndCents(subtotal + 500) }}</strong></li>
  </ul>
</template>

