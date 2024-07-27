import { defineStore } from 'pinia'
import type { CategoryItem } from '@/types'
import { apiUrl } from '@/api'
import { ref } from 'vue';

export const useCategoryStore = defineStore("category", {
    state: () => ({
        categoryList: [] as CategoryItem[],
        currentCategory: ref("Textbook")
    }),
    actions: {
        async fetchCategories() {
            const response = await fetch(`${apiUrl}/categories/`);
            const data = await response.json();
            this.categoryList = data as CategoryItem[];
        }
    },
    // getters
});