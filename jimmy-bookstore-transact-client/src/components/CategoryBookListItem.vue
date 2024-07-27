<script setup lang="ts">
import { defineProps } from "vue";
import type { BookItem } from "@/types";
import { useCartStore } from "@/stores/cart";
import { asDollarsAndCents } from "@/main";
const cartStore = useCartStore()
const props = defineProps<{
  book: BookItem;
}>();
const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  name = name.replace(/:/g, "");
  return `${name}.jpg`;
};
function getImageUrl(imageFileName: string) {
  return new URL(`../assets/book-images/${imageFileName}`, import.meta.url).href
}

</script>

<style scoped>
#book-boxes {
  display: flex;
  flex-wrap: wrap;
  padding: 1em;
  gap: 1em;
}

.category-book-list button {
  border-radius: 5px;
}

.book-image {
  display: flex;
  justify-content: center;
}

.book-image img {
  width: 150px;
  height: 200px;
  object-fit: cover;
  object-position: center;
}

.book-image button {
  background-color: var(--neutral-color);
  padding: 0.4em 1em;
  transform: translateY(-2.25em);
  margin-bottom: -3em;
}

.book-box {
  width: 280px;
  height: 380px;
  display: flex;
  flex-direction: column;
  background-color: var(--card-background-color);
  padding: 1em;
  gap: 0.25em;
}

.book-title {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.book-author {
  display: flex;
  justify-content: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.book-price {
  display: flex;
  justify-content: center;
}

.read-now-button {
  transition: background-color 1s;
  display: flex;
  background-color: var(--neutral-color);
  border: none;
  width: 50%;
  height: 10%;
  justify-content: center;
  align-items: center;
  margin-left: 25%;
  margin-top: -50%;
  margin-bottom: 45%;
}

.read-now-button p {
  line-height: 100%;
}

.empty {
  background-color: transparent;
  border: none;
  width: 50%;
  height: 10%;
  margin-left: 25%;
  margin-top: -50%;
  margin-bottom: 45%;
}

.read-now-button:hover {
  background-color: var(--primary-color);
}

.add-to-cart-button {
  transition: width 1s;
  width: 150px;
  border-radius: 5px;
  border: none;
}

.add-to-cart-button:hover {
  width: 250px;
}

.book-title {
  font-weight: bold;
  text-align: center;
}

.book-author {
  font-style: italic;
}

button {
  background-color: var(--call-to-action-color);
  color: var(--white-text-color)
}
</style>

<template>
  <li class="book-box">
    <div class="book-image">
      <img :src="getImageUrl(bookImageFileName(props.book))" :alt="book.title" />
    </div>
    <button v-if="book.isPublic" class="read-now-button button">
      <router-link to='/'>
        <p>read now</p>
      </router-link>
    </button>
    <p v-else class="empty"></p>
    <div class="book-title">{{ book.title }}</div>
    <div class="book-author">{{ book.author }}</div>
    <div class="book-price">{{ asDollarsAndCents(book.price) }}</div>
    <div style="display: flex; justify-content: center;">
      <button class="add-to-cart-button button" @click="cartStore.addToCart(book)"><i
          class="fa-solid fa-cart-plus"></i>&nbsp&nbspAdd to Cart</button>
    </div>
  </li>
</template>
