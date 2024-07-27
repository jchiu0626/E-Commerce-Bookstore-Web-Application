<script setup lang="ts">
import TheCategoryNav from "@/components/TheCategoryNav.vue";
import TheCategoryBookList from "@/components/TheCategoryBookList.vue";
import { useRoute } from "vue-router";
import { useBookStore } from "@/stores/book";
import { useCategoryStore } from "@/stores/category";
import { watch } from "vue";
import router from "@/router";

const route = useRoute();
const bookStore = useBookStore();
const categoryStore = useCategoryStore();

watch(
  () => route.params.name,
  (newName) => {
    bookStore.fetchBooks(newName as string).catch(() => router.push("/not-found"));
    categoryStore.currentCategory = newName as string;
  },
  { immediate: true }
);

</script>

<style scoped></style>

<template>
  <div>
    <the-category-nav></the-category-nav>
    <the-category-book-list></the-category-book-list>
  </div>
</template>