<script setup lang="ts"></script>

<style scoped>
footer {
  width: 1200px;
  background: var(--secondary-background-color);
  display: flex;
  justify-content: space-between;
  gap: 1em;
}

.copyright {
  display: flex;
  margin: 1em;
}

.links {
  display: flex;
  margin: 1em;
  text-decoration: underline;
}

.social-media-icons {
  display: flex;
  margin-top: 1em;
}
</style>

<template>
  <footer class="container">
    <section class="copyright">
      <p><i class="fa-regular fa-copyright"></i> 2023 Good Bookstore</p>
    </section>
    <section class="links">
      <router-link to="/" style="margin-right: 1rem;margin-left: 1rem;">About</router-link>
      <p style="margin-top: -0.2rem;"> | </p>
      <router-link to="/" style="margin-right: 1rem;margin-left: 1rem;">Directions</router-link>
      <p style="margin-top: -0.2rem;"> | </p>
      <router-link to="/" style="margin-right: 1rem;margin-left: 1rem;">Contact</router-link>
    </section>
    <section class="social-media-icons">
      <p>Follow Us</p>
      <router-link to="/" style="font-size: 120%; margin-left: 0.5rem;"><i class="fa-brands fa-facebook"></i></router-link>
      <router-link to="/" style="font-size: 120%; margin-left: 0.5rem;"><i class="fa-brands fa-twitter"></i></router-link>
    </section>
  </footer>
</template>
