<script setup lang="ts">
import type { CategoryItem } from "@/types";
import { useCategoryStore } from "@/stores/category";
const categoryStore = useCategoryStore();
const categoryImageFileName = function (category: CategoryItem): string {
  let name = category.name.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  return `${name}.jpg`;
};
</script>

<style scoped>
ul {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 1em;
}

li {
  text-align: center;
  cursor: pointer;
}

li div {
  padding: 0.5em 0;
}
</style>

<template>
  <ul>
    <li v-for="category in categoryStore.categoryList" :key="category.categoryId">
      <router-link :to="'../category/' + category.name">
        <div>{{ category.name }}</div>
        <img :src="'category-images/' + categoryImageFileName(category)" :alt="category.name + ' category'" />
      </router-link>
    </li>
  </ul>
</template>
