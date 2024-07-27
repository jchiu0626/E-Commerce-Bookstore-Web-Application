<script setup lang="ts">
import { reactive, ref } from "vue";
import useVuelidate from "@vuelidate/core";
import type { OrderDetails, ServerErrorResponse } from "@/types";
import { helpers, maxLength, minLength, required, email } from "@vuelidate/validators";
import { useCartStore } from "@/stores/cart";
import { useCategoryStore } from "@/stores/category";
import { isCreditCard, isMobilePhone } from "@/validators";
import CheckoutFieldError from "@/components/CheckoutFieldError.vue";
import { asDollarsAndCents } from "@/main";
import router from "@/router";

const defaultServerErrorMessage = "An unexpected error occurred, please try again."
const serverErrorMessage = ref(defaultServerErrorMessage);
const cartStore = useCartStore();
const cart = cartStore.cart;
const categoryStore = useCategoryStore();

const months: string[] = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const form = reactive({
  name: "",
  address: "",
  phone: "",
  email: "",
  ccNumber: "",
  ccExpiryMonth: new Date().getMonth() + 1,
  ccExpiryYear: new Date().getFullYear(),
  checkoutStatus: "",
});

const rules = {
  name: {
    required: helpers.withMessage("Please provide a name.", required),
    minLength: helpers.withMessage(
      "Name must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Name can have at most 45 letters.",
      maxLength(45)
    ),
  },
  address: {
    required: helpers.withMessage("Please provide an address.", required),
    minLength: helpers.withMessage(
      "Address must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Address can have at most 45 letters.",
      maxLength(45)
    ),
  },
  phone: {
    required: helpers.withMessage("Please provide a phone number.", required),
    phone: helpers.withMessage("Please provide a valid phone number",
      (value: string) => isMobilePhone(value))
  },
  email: {
    required: helpers.withMessage("Please provide an email address.", required),
    email: helpers.withMessage("Please provide a valid email address", email)
  },
  ccNumber: {
    required: helpers.withMessage("Please provide a credit card number.", required),
    ccNumber: helpers.withMessage("Please provide a valid credit card number",
      (value: string) => isCreditCard(value))
  },
  ccExpiryMonth: {},
  ccExpiryYear: {}
};
const v$ = useVuelidate(rules, form);

async function submitOrder() {
  console.log("Submit order");
  const isFormCorrect = await v$.value.$validate();
  if (!isFormCorrect) {
    form.checkoutStatus = "ERROR";
  } else {
    try {
      form.checkoutStatus = "PENDING";
      serverErrorMessage.value = defaultServerErrorMessage;

      const placeOrderResponse: OrderDetails | ServerErrorResponse =
        await cartStore
          .placeOrder({
            name: form.name,
            address: form.address,
            phone: form.phone,
            email: form.email,
            ccNumber: form.ccNumber,
            ccExpiryMonth: form.ccExpiryMonth,
            ccExpiryYear: form.ccExpiryYear,
          })

      if ("error" in placeOrderResponse) {
        form.checkoutStatus = "SERVER_ERROR";
        serverErrorMessage.value = (placeOrderResponse as ServerErrorResponse).message
        console.log("Error placing order", placeOrderResponse);
      } else {
        form.checkoutStatus = "OK";
        await router.push({ name: "confirmation-view" });
      }

    } catch (e) {
      form.checkoutStatus = "SERVER_ERROR";
      serverErrorMessage.value = defaultServerErrorMessage;
      console.log("Error placing order", e);
    }
  }
}

/* NOTE: For example yearFrom(0) == <current_year> */
function yearFrom(index: number) {
  return new Date().getFullYear() + index;
}
</script>

<style scoped>
.checkout-page {
  background: var(--neutral-color);
  color: var(--default-text-color);
}

.checkout-page-body {
  display: flex;
  flex-direction: column;
  padding: 1em;
  justify-content: center;
  align-items: center;
}

.checkout-page-body-cart-empty {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.checkout-page-body-cart-empty h1 {
  padding: 1rem;
  font-size: 300%;
}

.checkout-page-body-cart-empty button {
  padding: 1rem;
}

form {
  display: flex;
  flex-direction: column;
}

form>div {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0.5em;
}

form>div>input,
form>div>select {
  margin-left: 0.5em;
}

form>.error {
  color: red;
  text-align: right;
  margin-bottom: 1rem;
  margin-top: -0.5rem;
}

.secondary-button {
  font-family: 'Inter', sans-serif;
  width: 200px;
  height: 50px;
  color: var(--call-to-action-color);
  background-color: transparent;
  border: 2px solid var(--call-to-action-color);
  font-size: 120%;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  white-space: nowrap;
  margin-bottom: 10px;
}

.checkoutStatusBox {
  margin: 1em;
  padding: 1em;
  text-align: center;
}

.checkout-details {
  text-align: right;
  margin-top: 0.5rem;
  margin-left: 4rem;
}

.call-to-action-button {
  width: 15rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: var(--call-to-action-color);
  color: var(--white-text-color);
  font-family: 'Inter', sans-serif;
  font-size: 24px;
  border-radius: 10px;
  padding-top: 7.5px;
  padding-bottom: 7.5px;
  padding-right: 10px;
  padding-left: 10px;
  margin-left: 7rem;
}

.call-to-action-button:hover,
.call-to-action-button:active {
  border-radius: 10px;
}
</style>

<template>
  <div class="checkout-page">
    <section class="checkout-page-body" v-if="cart.empty">
      <div class="checkout-page-body-cart-empty">
        <h1>Add an item to your cart to checkout.</h1>
        <router-link :to="'/category/' + categoryStore.currentCategory">
          <button class="button secondary-button">Continue Shoping</button>
        </router-link>
      </div>
    </section>
    <section class="checkout-page-body" v-if="!cart.empty">
      <form @submit.prevent="submitOrder">
        <div>
          <label for="name">Name</label>
          <input type="text" size="20" id="name" name="name" v-model.lazy="v$.name.$model" />
        </div>
        <template v-if="v$.name.$error">
          <span class="error" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</span>
        </template>

        <div>
          <label for="address">Address</label>
          <input type="text" size="20" id="address" name="address" v-model.lazy="v$.address.$model" />
        </div>
        <checkout-field-error :field-name="v$.address" />

        <div>
          <label for="phone">Phone</label>
          <input class="textField" type="text" size="20" id="phone" name="phone" v-model.lazy="v$.phone.$model" />
        </div>
        <checkout-field-error :field-name="v$.phone" />

        <div>
          <label for="email">Email</label>
          <input type="text" size="20" id="email" name="email" v-model.lazy="v$.email.$model" />
        </div>
        <checkout-field-error :field-name="v$.email" />

        <div>
          <label for="ccNumber">Credit card</label>
          <input type="text" size="20" id="ccNumber" name="ccNumber" v-model.lazy="v$.ccNumber.$model" />
        </div>
        <checkout-field-error :field-name="v$.ccNumber" />

        <div class="month-and-year">
          <label>Exp Date</label>
          <select v-model="v$.ccExpiryMonth.$model">
            <option v-for="(month, index) in months" :key="index" :value="index + 1">
              {{ month }} ({{ index + 1 }})
            </option>
          </select>
          &nbsp&nbsp
          <select v-model="v$.ccExpiryYear.$model">
            <option v-for="(year, index) in 15" :key="index">
              {{ yearFrom(index) }}
            </option>
          </select>
        </div>
        <input type="submit" name="submit" class="button call-to-action-button"
          :disabled="form.checkoutStatus === 'PENDING'" value="Complete Purchase" />
      </form>

      <section class="checkout-details">
        Your credit card will be charged <strong>{{ asDollarsAndCents(cartStore.cart.total) }}</strong>
        <br> ( {{ asDollarsAndCents(cartStore.cart.subtotal) }} +
        {{ asDollarsAndCents(cartStore.cart.surcharge) }} shipping)

      </section>
      <section v-show="form.checkoutStatus !== ''" class="checkoutStatusBox">
        <div v-if="form.checkoutStatus === 'ERROR'">
          Error: Please fix the problems above and try again.
        </div>

        <div v-else-if="form.checkoutStatus === 'PENDING'">Processing...</div>

        <div v-else-if="form.checkoutStatus === 'OK'">Order placed...</div>

        <div v-else>{{ serverErrorMessage }}</div>
      </section>
    </section>
  </div>
</template>
