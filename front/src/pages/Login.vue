<template>
  <div class="min-h-screen bg-light-bg font-inter">
    <!-- Navbar -->
    <Navbar />

    <div class="p-8">
      <div class="max-w-md mx-auto mt-12">
        <!-- Decorative Header -->
        <div class="text-center mb-8">
          <h1 class="font-cinzel text-4xl text-primary-dark mb-2">
            🗝️ Connexion
          </h1>
          <p class="text-gray-600">
            Accédez à votre compte aventurier
          </p>
        </div>

        <!-- Login Form Card -->
        <div class="relative bg-white/95 rounded-lg shadow-lg border-2 border-primary/40 p-8">
          <!-- Decorative Corners -->
          <div class="absolute top-0 left-0 w-8 h-8 border-t-4 border-l-4 border-primary rounded-tl-lg"></div>
          <div class="absolute top-0 right-0 w-8 h-8 border-t-4 border-r-4 border-primary rounded-tr-lg"></div>
          <div class="absolute bottom-0 left-0 w-8 h-8 border-b-4 border-l-4 border-primary rounded-bl-lg"></div>
          <div class="absolute bottom-0 right-0 w-8 h-8 border-b-4 border-r-4 border-primary rounded-br-lg"></div>

          <!-- Error Message -->
          <transition name="error-slide">
            <div v-if="errorMessage" class="mb-6 p-4 bg-accent/20 border-2 border-accent rounded-lg animate-shake">
              <div class="flex items-start gap-3">
                <span class="text-2xl flex-shrink-0">⚠️</span>
                <div class="flex-1">
                  <p class="text-accent font-medium mb-1">Erreur de connexion</p>
                  <p class="text-accent/90 text-sm">{{ errorMessage }}</p>
                </div>
                <button 
                  @click="errorMessage = ''"
                  class="text-accent hover:text-accent/70 transition-colors flex-shrink-0"
                  aria-label="Fermer"
                >
                  <span class="text-xl">✕</span>
                </button>
              </div>
            </div>
          </transition>

          <form @submit.prevent="handleLogin" class="space-y-6">
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
                placeholder="Entrez votre nom d'utilisateur"
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
                placeholder="Entrez votre mot de passe"
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
              <span v-if="!isLoading">🗝️ Se connecter</span>
              <span v-else>⏳ Connexion en cours...</span>
            </button>
          </form>

          <!-- Register Link -->
          <div class="mt-6 text-center">
            <p class="text-gray-600">
              Nouveau dans la guilde ?
              <router-link
                :to="{ name: 'Register' }"
                class="text-primary hover:text-primary-dark font-medium transition-colors"
              >
                S'inscrire
              </router-link>
            </p>
          </div>
        </div>

        <!-- Additional Info -->
        <div class="mt-6 text-center text-sm text-gray-500">
          <p>🛡️ Vos identifiants sont protégés par la magie des Capuches d'Opale</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import Navbar from '../components/Navbar.vue';
import { AuthService } from '../services/AuthService.js';

export default {
  name: 'Login',
  components: {
    Navbar
  },
  setup() {
    const router = useRouter();
    const formData = ref({
      userName: '',
      password: ''
    });
    const isLoading = ref(false);
    const errorMessage = ref('');

    const handleLogin = async () => {
      // Reset error message
      errorMessage.value = '';
      
      // Validation côté client
      if (!formData.value.userName.trim()) {
        errorMessage.value = 'Le nom d\'utilisateur est requis.';
        return;
      }
      
      if (!formData.value.password) {
        errorMessage.value = 'Le mot de passe est requis.';
        return;
      }
      
      if (formData.value.password.length < 6) {
        errorMessage.value = 'Le mot de passe doit contenir au moins 6 caractères.';
        return;
      }
      
      isLoading.value = true;

      try {
        const response = await AuthService.login({
          userName: formData.value.userName.trim(),
          password: formData.value.password
        });

        // Login successful - redirect to dashboard or home
        if (response.token) {
          // Redirection avec un court délai pour permettre à l'utilisateur de voir le succès
          setTimeout(() => {
            router.push({ name: 'Home' });
          }, 300);
        }

      } catch (error) {
        console.error('Erreur de connexion:', error);
        
        // Afficher le message d'erreur avec plus de contexte
        if (error.type === 'NETWORK_ERROR') {
          errorMessage.value = '🚫 ' + error.message;
        } else if (error.status === 401) {
          errorMessage.value = '🔒 ' + error.message;
        } else {
          errorMessage.value = error.message || 'Une erreur inattendue s\'est produite. Veuillez réessayer.';
        }
      } finally {
        isLoading.value = false;
      }
    };

    return {
      formData,
      isLoading,
      errorMessage,
      handleLogin
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

@keyframes shake {
  0%, 100% { transform: translateX(0); }
  10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
  20%, 40%, 60%, 80% { transform: translateX(5px); }
}

.animate-shake {
  animation: shake 0.5s ease-in-out;
}

.max-w-md {
  animation: fadeIn 0.5s ease-out;
}

/* Animations pour les messages d'erreur */
.error-slide-enter-active {
  animation: slideDown 0.3s ease-out;
}

.error-slide-leave-active {
  animation: slideDown 0.3s ease-in reverse;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
    max-height: 0;
  }
  to {
    opacity: 1;
    transform: translateY(0);
    max-height: 200px;
  }
}
</style>
