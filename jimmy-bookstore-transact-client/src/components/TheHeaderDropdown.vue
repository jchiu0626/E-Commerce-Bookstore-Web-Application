<script setup lang="ts">
import { provide } from "vue";
import type { CategoryItem } from "@/types";
import { apiUrl } from "@/api";
import { useCategoryStore } from "@/stores/category";
const categoryStore = useCategoryStore();

</script>

<style scoped>
.header-dropdown {
  position: relative;
}

.categories-button {
  background-color: var(--primary-color-dark);
}

.categories-button:hover,
.categories-button:active {
  background: var(--primary-color-dark);
  color: var(--default-text-color);
}

.header-dropdown ul {
  background-color: white;
  color: black;
  display: none;
  right: 1rem;
}

.header-dropdown li {
  padding: 0.25em 0.5em;
}


.header-dropdown li:hover {
  background: lightgray;
}

.header-dropdown:hover ul {
  display: block;
  position: absolute;
  z-index: 1;
  cursor: pointer;
  text-align: end;
}
</style>

<template>
  <div class="header-dropdown">
    <button class="button categories-button"><i class="fa-solid fa-bars"></i></button>
    <ul>
      <li v-for="category in categoryStore.categoryList" :key="category.categoryId">
        <router-link :to="{ name: 'category-view', params: { name: category.name } }">
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </div>
</template>
