<script setup lang="ts">
import TheHeaderDropdown from "@/components/TheHeaderDropdown.vue";
import { useCartStore } from "@/stores/cart";
const cartStore = useCartStore()
</script>

<style scoped>
header {
  width: 1200px;
  background: var(--secondary-background-color);
  display: flex;
  flex-direction: column;
  padding: 1em;
  gap: 1em;
}

.text-logo {
  font-family: var(--title-font-family);
  font-size: 566%;
}

.header-dropdown-and-cart-and-search-bar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.bookstore-logo {
  display: flex;
  align-items: flex-end;
}

.search {
  display: flex;
}

.search-btn {
  border-radius: 100%;
  border: none;
  background-color: transparent;
  margin-left: -2rem;
  margin-right: 0.3rem;
}

.login-register-button {
  margin-left: 1rem;
  background-color: var(--primary-color-light);
}

.login-register-button:hover {
  background-color: var(--primary-color);
}

.search-bar {
  padding: 0.5em;
  flex-grow: 1;
  border-radius: 5px;
  border: none;
}

.cart {
  display: flex;
}

.cart p {
  font-size: 110%;
  margin-left: 1rem;
  margin-top: 0.1rem;
}

@media (max-width: 600px) {
  header {
    flex-direction: column;
  }
}
</style>

<template>
  <header class="container">
    <section class="header-dropdown-and-cart-and-search-bar">
      <form class="search" action="category.html">
        <input type="text" class="search-bar" /><br />
        <button class="search-btn"><i class="fa-solid fa-magnifying-glass"></i></button>
      </form>
      <router-link to="/">
        <button class="button login-register-button">login</button>
      </router-link>
      <router-link to="/">
        <button class="button login-register-button">register</button>
      </router-link>
      <router-link :to="{ name: 'cart-view' }">
        <div class="cart">
          <p><i class="fa-solid fa-cart-shopping"></i>&nbsp&nbsp({{ cartStore.cart.numberOfItems }})</p>
        </div>
      </router-link>
      <the-header-dropdown></the-header-dropdown>
    </section>
    <section class="bookstore-logo">
      <router-link to="/">
        <img src="/site-images/bookstore-logo.png" alt="Good Bookstore Logo" width="150" height="auto" />
      </router-link>
      <h1>
        <router-link to="/" class="text-logo">Good Bookstore</router-link>
      </h1>
    </section>
  </header>
</template>
