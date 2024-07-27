<script setup lang="ts">
import { useCartStore } from "@/stores/cart";
import type { BookItem } from "@/types";
import { asDollarsAndCents } from "@/main";
const cartStore = useCartStore();

const bookImageFileName = function (book: BookItem): string {
    let name = book.title.toLowerCase();
    name = name.replace(/ /g, "-");
    name = name.replace(/'/g, "");
    name = name.replace(/:/g, "");
    return `${name}.jpg`;
};
const updateCart = function (book: BookItem, quantity: number) {
    cartStore.cart.update(book, quantity);
};
function getImageUrl(imageFileName: string) {
    return new URL(`../assets/book-images/${imageFileName}`, import.meta.url).href
}
</script>

<style scoped>
.cart-table {
    display: grid;
    grid-template-columns: max-content minmax(10em, 20em) repeat(3, max-content);
    row-gap: 1em;
    width: fit-content;
    margin: 0 auto;
    background-color: aliceblue;
}

ul {
    display: contents;
}

ul>li {
    display: contents;
}

.table-heading {
    background-color: #1e52e0;
    color: white;
}

.table-heading>* {
    background-color: #1e52e0;
    padding: 0.5em;
}

.heading-book {
    grid-column: 1 / 3;
}

.heading-price {
    grid-column: 3 / 5;
    text-align: center;
}

.heading-subtotal {
    text-align: right;
    grid-column: -2 / -1;
}

.cart-book-image {
    height: 100px;
    width: 75px;
    margin-right: 1rem;
    margin-left: 1rem;
}

img {
    display: block;
    width: 100px;
    height: auto;
}

.square {
    width: 100px;
    height: 100px;
}

.wide-rect {
    width: 125px;
    height: 100px;
}

.cart-book-price {
    padding-left: 1em;
    text-align: right;
}

.cart-book-quantity {
    padding-left: 1em;
    padding-right: 1em;
    margin-top: -0.17rem;
}

.cart-book-subtotal {
    text-align: right;
    padding-left: 1em;
    padding-right: 1em;
}

/* Row separators in the table */

.line-sep {
    display: block;
    height: 2px;
    background-color: rgb(0, 0, 0);
    grid-column: 1 / -1;
}

/* Increment/decrement buttons */

.icon-button {
    border: none;
    cursor: pointer;
    background-color: aliceblue;
}

.inc-button {
    font-size: 1.125rem;
    color: var(--primary-color);
    margin-right: 0.25em;
}

.inc-button:hover {
    color: var(--primary-color-dark);
}

.dec-button {
    font-size: 1.125rem;
    color: #ccc;
}

.dec-button:hover {
    color: #aaa;
}

/* Chevron buttons */

.dec-arrow-button,
.inc-arrow-button {
    font-size: 1rem;
    color: var(--primary-color);
}

.dec-arrow-button:hover,
.inc-arrow-button:hover {
    color: var(--primary-color-dark);
}

input[type="number"] {
    width: 4em;
}

select {
    background-color: var(--primary-color);
    color: white;
    border: 2px solid var(--primary-color-dark);
    border-radius: 3px;
}
</style>

<template>
    <div class="cart-table">
        <ul>
            <li class="table-heading" v-if="!cartStore.cart.empty">
                <div class="heading-book">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspBook</div>
                <div class="heading-price">Price&nbsp&nbsp&nbsp&nbsp&nbsp&nbspQuantity</div>
                <div class="heading-subtotal">Amount</div>
            </li>

            <template v-for="item in cartStore.cart.items" :key="item.book.bookId">
                <li>
                    <div class="cart-book-image">
                        <img :src="getImageUrl(bookImageFileName(item.book))" :alt="item.book.title" />
                    </div>
                    <div class="cart-book-title">{{ item.book.title }}</div>
                    <div class="cart-book-price">
                        {{ asDollarsAndCents(item.book.price) }}
                    </div>
                    <div class="cart-book-quantity">
                        <span class="quantity">{{ item.quantity }}</span>&nbsp;
                        <button class="icon-button inc-button">
                            <i class="fas fa-plus-circle" @click="updateCart(item.book, item.quantity + 1)"></i>
                        </button>
                        <button class="icon-button dec-button">
                            <i class="fas fa-minus-circle" @click="updateCart(item.book, item.quantity - 1)"></i>
                        </button>
                    </div>
                    <div class="cart-book-subtotal">{{ asDollarsAndCents(item.book.price * item.quantity) }}</div>
                </li>
                <li class="line-sep"></li>
            </template>
        </ul>
    </div>
</template>
