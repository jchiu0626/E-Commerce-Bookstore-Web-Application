<script setup lang="ts">
import { defineProps } from "vue";
import type { BaseValidation } from "@vuelidate/core";

const props = defineProps<{
  fieldName: BaseValidation;
}>();
</script>

<style scoped>
form>.error {
  color: red;
  text-align: right;
  margin-bottom: 1rem;
  margin-top: -0.5rem;
}
</style>

<template>
  <template v-if="fieldName.$error">
    <span class="error" v-for="error of fieldName.$errors" :key="error.$uid">{{ error.$message }}</span>
  </template>
</template>
