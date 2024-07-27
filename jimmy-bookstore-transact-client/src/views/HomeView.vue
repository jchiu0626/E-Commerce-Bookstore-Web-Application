<script setup lang="ts">
import TheHomeCategoryList from "@/components/TheHomeCategoryList.vue";
</script>

<style scoped>
.home-page {
  width: 1200px;
  height: 439px;
  display: flex;
  flex-grow: 1;
  justify-content: space-between;
  background-color: var(--neutral-color);
}

h1 {
  font-family: var(--default-font-family);
}

.book-pile-image {
  right: 10000em;
}

.home-text {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  color: var(--default-text-color);
}

.home-text h1 {
  font-weight: bold;
  font-size: 2.5em;
}

.home-text p {
  font-size: 1.75em;
  text-align: end;
}

.home-content {
  display: flex;
  flex-direction: column;
}

.call-to-action-button-div {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.call-to-action-button {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  background-color: var(--call-to-action-color);
  color: var(--white-text-color);
  font-family: 'Inter', sans-serif;
  font-size: 24px;
  border-radius: 10px;
  padding-top: 7.5px;
  padding-bottom: 7.5px;
  padding-right: 10px;
  padding-left: 10px;
}

.call-to-action-button:hover,
.call-to-action-button:active {
  border-radius: 10px;
}

.category-image-items {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin-left: 7em;
  margin-right: 1em;
  gap: 1em;
  width: 100%;
}

.category-image-items a {
  text-align: center;
  cursor: pointer;
}

.category-image-items div {
  padding: 0.5em;
  color: black;
}

.category-image-items img {
  width: 150px;
  height: 200px;
  object-fit: cover;
  object-position: center;
}
</style>
<template>
  <div class="home-page">
    <section class="flow-content dark-background">
      <img class="dark-background book-pile-image" src="/site-images/books1.png" alt="Book stall image" width="400"
        height="auto" />
    </section>
    <section class="home-content container dark-background">
      <div class="home-text">
        <h1>Welcome to Good Bookstore!</h1>
        <p>Find your next favorite read among<br>our wide selection of books!</p>
      </div>
      <div class="call-to-action-button-div" style="pointer-events: none;">
        <router-link to="/category/Textbook">
          <button class="button call-to-action-button" style="pointer-events: auto;" width="220">
            SHOP FOR BOOKS!!
          </button>
        </router-link>
      </div>
      <the-home-category-list></the-home-category-list>
    </section>
  </div>
</template>
