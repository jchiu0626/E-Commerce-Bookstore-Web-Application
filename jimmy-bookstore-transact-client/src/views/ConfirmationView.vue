<script setup lang="ts">
import ConfirmationTable from "@/components/ConfirmationTable.vue";

import { useCategoryStore } from "@/stores/category";
import { useOrderDetailsStore } from "@/stores/orderDetails";
import { useCartStore } from "@/stores/cart";
import { computed } from "vue";
import { asDollarsAndCents } from "@/main";
const categoryStore = useCategoryStore();
const orderDetailsStore = useOrderDetailsStore();
const orderDetails = orderDetailsStore.orderDetails;
const cartStore = useCartStore();
const orderDate = computed(function () {
  let date = new Date(orderDetails.order.dateCreated);
  return date.toLocaleTimeString();
});

const ccExpDate = computed(function (): Date {
  return new Date(orderDetails.customer.ccExpDate);
});

const ccExpYear = computed(function (): number {
  return ccExpDate.value.getFullYear();
});

const ccExpMonth = computed(function (): string {
  let month = ccExpDate.value.getMonth() + 1;
  let result = "" + month;
  if (month < 10) {
    result = "0" + month;
  }
  return result;
});
</script>

<style scoped>
#confirmationView {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 1em auto;
}

h1 {
  padding-top: 1rem;
  text-align: center;
  font-size: 300%;
  margin-bottom: 1rem;
}

ul {
  text-align: left;
}

ul>li {
  margin: 0.25em;
}
</style>

<template>
  <div v-if="orderDetailsStore.hasOrderDetails()" id="confirmationView">
    <ul>
      <li>Confirmation #: {{ orderDetails.order.confirmationNumber }}</li>
      <li>Time: {{ orderDate }} {{ new Date(orderDetails.order.dateCreated).toLocaleDateString() }}</li>
    </ul>
    <confirmation-table> </confirmation-table>
    <ul>
      <li>{{ orderDetails.customer.customerName }}</li>
      <li>{{ orderDetails.customer.address }}</li>
      <li>{{ orderDetails.customer.email }}</li>
      <li>{{ orderDetails.customer.phone }}</li>
      <li>{{ orderDetails.customer.ccNumber.replace(/.(?=.{4})/g, '*') }}
        ({{ ccExpYear }}/{{ ccExpMonth }})</li>
    </ul>
    <div id="customerInfo"></div>
  </div>
  <div v-else>
    <h1>No order exists</h1>
  </div>
</template>
