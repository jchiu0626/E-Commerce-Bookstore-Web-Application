<script setup lang="ts">
import { provide } from "vue";
import { RouterView, useRoute } from "vue-router";
import type { CategoryItem } from "@/types";
import TheHeader from "@/components/TheHeader.vue";
import TheFooter from "@/components/TheFooter.vue";
import { useCategoryStore } from "./stores/category";
const route = useRoute();
const categoryStore = useCategoryStore();
categoryStore.fetchCategories();

</script>

<style>
.app {
  display: flex;
  align-items: center;
}
</style>

<template>
  <div id="app">
    <the-header></the-header>
    <router-view :key="route.fullPath"></router-view>
    <the-footer></the-footer>
  </div>
</template>
