<template>
  <div class="min-h-screen bg-light-bg font-inter">
    <!-- Navbar -->
    <Navbar />

    <div class="p-8">
      <div class="max-w-md mx-auto mt-12">
        <!-- Decorative Header -->
        <div class="text-center mb-8">
          <h1 class="font-cinzel text-4xl text-primary-dark mb-2">
            ⚔️ Inscription
          </h1>
          <p class="text-gray-600">
            Rejoignez la Guilde des Aventuriers
          </p>
        </div>

        <!-- Registration Form Card -->
        <div class="relative bg-white/95 rounded-lg shadow-lg border-2 border-primary/40 p-8">
          <!-- Decorative Corners -->
          <div class="absolute top-0 left-0 w-8 h-8 border-t-4 border-l-4 border-primary rounded-tl-lg"></div>
          <div class="absolute top-0 right-0 w-8 h-8 border-t-4 border-r-4 border-primary rounded-tr-lg"></div>
          <div class="absolute bottom-0 left-0 w-8 h-8 border-b-4 border-l-4 border-primary rounded-bl-lg"></div>
          <div class="absolute bottom-0 right-0 w-8 h-8 border-b-4 border-r-4 border-primary rounded-br-lg"></div>

          <!-- Success Message -->
          <div v-if="successMessage" class="mb-6 p-4 bg-secondary/20 border border-secondary rounded-lg">
            <p class="text-secondary-dark flex items-center gap-2">
              <span>✓</span>
              <span>{{ successMessage }}</span>
            </p>
          </div>

          <!-- Error Message -->
          <div v-if="errorMessage" class="mb-6 p-4 bg-accent/20 border border-accent rounded-lg">
            <p class="text-accent flex items-center gap-2">
              <span>⚠</span>
              <span>{{ errorMessage }}</span>
            </p>
          </div>

          <form @submit.prevent="handleRegister" class="space-y-6">
            <!-- Username Field -->
            <div>
              <label for="userName" class="block text-sm font-medium text-gray-700 mb-2">
                👤 Nom d'utilisateur
              </label>
              <input
                id="userName"
                v-model="formData.userName"
                type="text"
                required
                placeholder="Choisissez un nom d'utilisateur"
                class="w-full px-4 py-3 border-2 border-primary/30 rounded-lg focus:outline-none focus:border-primary transition-colors bg-white/50"
                :disabled="isLoading"
              />
            </div>

            <!-- Password Field -->
            <div>
              <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                🔒 Mot de passe
              </label>
              <input
                id="password"
                v-model="formData.password"
                type="password"
                required
                placeholder="Choisissez un mot de passe"
                class="w-full px-4 py-3 border-2 border-primary/30 rounded-lg focus:outline-none focus:border-primary transition-colors bg-white/50"
                :disabled="isLoading"
              />
              <div class="mt-2 text-xs text-gray-500 space-y-1">
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.length ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.length ? '✓' : '○' }}</span>
                  10-20 caractères
                </p>
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.uppercase ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.uppercase ? '✓' : '○' }}</span>
                  Une majuscule
                </p>
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.lowercase ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.lowercase ? '✓' : '○' }}</span>
                  Une minuscule
                </p>
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.digit ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.digit ? '✓' : '○' }}</span>
                  Un chiffre
                </p>
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.special ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.special ? '✓' : '○' }}</span>
                  Un caractère spécial (#?!@$%^&*.-)
                </p>
                <p class="flex items-center gap-1">
                  <span :class="passwordValidation.noSpace ? 'text-secondary' : 'text-gray-400'">{{ passwordValidation.noSpace ? '✓' : '○' }}</span>
                  Sans espace
                </p>
              </div>
            </div>

            <!-- Confirm Password Field -->
            <div>
              <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-2">
                🔒 Confirmer le mot de passe
              </label>
              <input
                id="confirmPassword"
                v-model="confirmPassword"
                type="password"
                required
                placeholder="Confirmez votre mot de passe"
                class="w-full px-4 py-3 border-2 border-primary/30 rounded-lg focus:outline-none focus:border-primary transition-colors bg-white/50"
                :disabled="isLoading"
              />
            </div>

            <!-- Submit Button -->
            <button
              type="submit"
              :disabled="isLoading"
              class="w-full bg-primary hover:bg-primary-dark text-white font-cinzel py-3 px-6 rounded-lg transition-all duration-300 shadow-md hover:shadow-xl disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="!isLoading">⚔️ S'inscrire</span>
              <span v-else>⏳ Inscription en cours...</span>
            </button>
          </form>

          <!-- Login Link -->
          <div class="mt-6 text-center">
            <p class="text-gray-600">
              Déjà membre de la guilde ?
              <router-link
                :to="{ name: 'Login' }"
                class="text-primary hover:text-primary-dark font-medium transition-colors"
              >
                Se connecter
              </router-link>
            </p>
          </div>
        </div>

        <!-- Additional Info -->
        <div class="mt-6 text-center text-sm text-gray-500">
          <p>🛡️ En vous inscrivant, vous acceptez de rejoindre les Capuches d'Opale</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import { AuthService } from '../services/AuthService.js';

export default {
  name: 'Register',
  components: {
    Navbar
  },
  setup() {
    const router = useRouter();
    const formData = ref({
      userName: '',
      password: ''
    });
    const confirmPassword = ref('');
    const isLoading = ref(false);
    const errorMessage = ref('');
    const successMessage = ref('');

    // Password validation computed property
    const passwordValidation = computed(() => {
      const pwd = formData.value.password;
      return {
        length: pwd.length >= 10 && pwd.length <= 20,
        uppercase: /[A-Z]/.test(pwd),
        lowercase: /[a-z]/.test(pwd),
        digit: /\d/.test(pwd),
        special: /[#?!@$%^&*.\-]/.test(pwd),
        noSpace: !/\s/.test(pwd)
      };
    });

    const isPasswordValid = computed(() => {
      const validation = passwordValidation.value;
      return validation.length && validation.uppercase && validation.lowercase && 
             validation.digit && validation.special && validation.noSpace;
    });

    const handleRegister = async () => {
      // Reset messages
      errorMessage.value = '';
      successMessage.value = '';

      // Validate passwords match
      if (formData.value.password !== confirmPassword.value) {
        errorMessage.value = 'Les mots de passe ne correspondent pas';
        return;
      }

      // Validate password format
      if (!isPasswordValid.value) {
        errorMessage.value = 'Le mot de passe ne respecte pas les critères requis';
        return;
      }

      isLoading.value = true;

      try {
        await AuthService.register({
          userName: formData.value.userName,
          password: formData.value.password
        });

        successMessage.value = 'Inscription réussie ! Redirection vers la connexion...';

        // Redirect to login after 2 seconds
        setTimeout(() => {
          router.push({ name: 'Login' });
        }, 2000);

      } catch (error) {
        errorMessage.value = error.message || 'Une erreur est survenue lors de l\'inscription';
      } finally {
        isLoading.value = false;
      }
    };

    return {
      formData,
      confirmPassword,
      isLoading,
      errorMessage,
      successMessage,
      passwordValidation,
      isPasswordValid,
      handleRegister
    };
  }
};
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.max-w-md {
  animation: fadeIn 0.5s ease-out;
}
</style>
