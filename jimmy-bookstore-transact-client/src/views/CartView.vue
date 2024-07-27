<script setup lang="ts">
import CartTable from "@/components/CartTable.vue";
import { useCartStore } from "@/stores/cart";
import { useCategoryStore } from "@/stores/category";
import { asDollarsAndCents } from "@/main";
const cartStore = useCartStore();
const categoryStore = useCategoryStore();
const clearCart = function () {
    cartStore.cart.clear();
};
</script>
<style scoped>
.cart-page {
    background-color: var(--neutral-color);
}

h1 {
    padding-top: 1rem;
    text-align: center;
    font-size: 300%;
}

.cart-status {
    text-align: center;
    margin-bottom: 1rem;
    font-size: 1.5rem;
}

.total {
    text-align: center;
    font-size: 200%;
    margin: 1rem;
}

.primary-button {
    font-family: 'Inter', sans-serif;
    width: 200px;
    height: 50px;
    color: var(--white-text-color);
    background-color: var(--call-to-action-color);
    text-align: center;
    font-size: 115%;
    display: flex;
    justify-content: center;
    align-items: center;
    white-space: nowrap;
    margin-bottom: 10px;
}

.secondary-and-tertiary-buttons-div {
    display: grid;
    justify-items: center;
    align-items: center;
    margin-top: 10px;
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

.tertiary-button {
    font-family: 'Inter', sans-serif;
    width: 100px;
    height: 40px;
    color: var(--call-to-action-color);
    background-color: transparent;
    border: 2px solid var(--call-to-action-color);
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    white-space: nowrap;
    margin-bottom: 10px;
}
</style>

<template>
    <div class="cart-page">
        <h1>Cart Page</h1>
        <div class="cart-status">
            <p v-if="cartStore.cart.numberOfItems > 1">Your shopping cart contains {{ cartStore.cart.numberOfItems }} items.
            </p>
            <p v-if="cartStore.cart.numberOfItems == 1">Your shopping cart contains {{ cartStore.cart.numberOfItems }} item.
            </p>
            <p v-if="cartStore.cart.empty">Your shopping cart is empty.</p>
        </div>
        <cart-table></cart-table>
        <div v-if="!cartStore.cart.empty" class="total">
            <p>Subtotal: {{ asDollarsAndCents(cartStore.cart.subtotal) }}</p>
            <p>Surcharge: {{ asDollarsAndCents(cartStore.cart.surcharge) }}</p>
            <p>Total: {{ asDollarsAndCents(cartStore.cart.total) }}</p>
        </div>
        <div class="secondary-and-tertiary-buttons-div">
            <div v-if="!cartStore.cart.empty">
                <router-link to="/checkout">
                    <button class="button primary-button">Proceed to Checkout</button>
                </router-link>
            </div>
            <router-link :to="'/category/' + categoryStore.currentCategory">
                <button class="button secondary-button">Continue Shoping</button>
            </router-link>

            <button class="button tertiary-button " v-if="!cartStore.cart.empty" @click="clearCart()">Clear
                Cart</button>
        </div>
    </div>
</template>