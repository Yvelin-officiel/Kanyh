<template>
    <nav class="bg-white/90 backdrop-blur-md border-b-2 border-primary/30 shadow-lg sticky top-0 z-40">
        <div class="max-w-7xl mx-auto px-6 py-4">
            <div class="flex items-center justify-between">
                <!-- Logo -->
                <router-link :to="{ name: 'Home' }" class="flex items-center gap-3 group">
                    <div class="w-12 h-12 rounded-full overflow-hidden border-2 border-primary/40 group-hover:border-primary transition-all duration-300 group-hover:scale-110">
                        <img src="../assets/images/logo-capuches-opale.png" alt="Les Capuches d'Opale" class="w-full h-full object-cover" />
                    </div>
                    <span class="text-2xl font-cinzel text-primary-dark group-hover:text-primary transition-colors">
                        Les Capuches d'Opale
                    </span>
                </router-link>

                <!-- Navigation Links -->
                <div class="hidden md:flex items-center gap-6">
                    <router-link 
                        :to="{ name: 'Home' }"
                        class="nav-link font-cinzel text-lg text-txt-primary hover:text-primary transition-colors relative"
                        active-class="text-primary font-bold"
                    >
                        <span class="flex items-center gap-2">
                            <span class="text-xl">🏠</span>
                            Accueil
                        </span>
                    </router-link>
                    <router-link 
                        :to="{ name: 'QuestDashboard' }"
                        class="nav-link font-cinzel text-lg text-txt-primary hover:text-primary transition-colors relative"
                        active-class="text-primary font-bold"
                    >
                        <span class="flex items-center gap-2">
                            <span class="text-xl">📋</span>
                            Quêtes
                        </span>
                    </router-link>
                    <router-link 
                        :to="{ name: 'AdventurersView' }"
                        class="nav-link font-cinzel text-lg text-txt-primary hover:text-primary transition-colors relative"
                        active-class="text-primary font-bold"
                    >
                        <span class="flex items-center gap-2">
                            <span class="text-xl">⚔️</span>
                            Aventuriers
                        </span>
                    </router-link>
                    
                    <!-- Auth Links -->
                    <div class="flex items-center gap-3 ml-4 pl-4 border-l-2 border-primary/30">
                        <template v-if="!isAuthenticated">
                            <router-link 
                                :to="{ name: 'Login' }"
                                class="nav-link font-cinzel text-lg text-txt-primary hover:text-primary transition-colors relative"
                                active-class="text-primary font-bold"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">🗝️</span>
                                    Connexion
                                </span>
                            </router-link>
                            <router-link 
                                :to="{ name: 'Register' }"
                                class="bg-primary hover:bg-primary-dark text-white px-4 py-2 rounded-lg transition-all duration-300 shadow-md hover:shadow-lg font-cinzel"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">⚔️</span>
                                    S'inscrire
                                </span>
                            </router-link>
                        </template>
                        <template v-else>
                            <button
                                @click="handleLogout"
                                class="bg-accent hover:bg-accent/80 text-white px-4 py-2 rounded-lg transition-all duration-300 shadow-md hover:shadow-lg font-cinzel"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">🚪</span>
                                    Déconnexion
                                </span>
                            </button>
                        </template>
                    </div>
                </div>

                <!-- Menu Mobile -->
                <button 
                    @click="toggleMobileMenu" 
                    class="md:hidden p-2 rounded-lg hover:bg-primary/10 transition-colors"
                    aria-label="Toggle menu"
                >
                    <span class="text-3xl">{{ mobileMenuOpen ? '✖️' : '☰' }}</span>
                </button>
            </div>

            <!-- Menu Mobile Déroulant -->
            <transition name="slide-down">
                <div v-if="mobileMenuOpen" class="md:hidden mt-4 pb-4 border-t-2 border-primary/20 pt-4">
                    <div class="flex flex-col gap-3">
                        <router-link 
                            :to="{ name: 'Home' }"
                            @click="closeMobileMenu"
                            class="nav-link-mobile font-cinzel text-lg text-txt-primary hover:text-primary transition-colors p-3 rounded-lg hover:bg-primary/10"
                            active-class="bg-primary/10 text-primary font-bold"
                        >
                            <span class="flex items-center gap-2">
                                <span class="text-xl">🏠</span>
                                Accueil
                            </span>
                        </router-link>
                        <router-link 
                            :to="{ name: 'QuestDashboard' }"
                            @click="closeMobileMenu"
                            class="nav-link-mobile font-cinzel text-lg text-txt-primary hover:text-primary transition-colors p-3 rounded-lg hover:bg-primary/10"
                            active-class="bg-primary/10 text-primary font-bold"
                        >
                            <span class="flex items-center gap-2">
                                <span class="text-xl">📋</span>
                                Quêtes
                            </span>
                        </router-link>
                        <router-link 
                            :to="{ name: 'AdventurersView' }"
                            @click="closeMobileMenu"
                            class="nav-link-mobile font-cinzel text-lg text-txt-primary hover:text-primary transition-colors p-3 rounded-lg hover:bg-primary/10"
                            active-class="bg-primary/10 text-primary font-bold"
                        >
                            <span class="flex items-center gap-2">
                                <span class="text-xl">⚔️</span>
                                Aventuriers
                            </span>
                        </router-link>
                        <router-link 
                            :to="{ name: 'DS' }"
                            @click="closeMobileMenu"
                            class="nav-link-mobile font-cinzel text-lg text-txt-primary hover:text-primary transition-colors p-3 rounded-lg hover:bg-primary/10"
                            active-class="bg-primary/10 text-primary font-bold"
                        >
                            <span class="flex items-center gap-2">
                                <span class="text-xl">🎨</span>
                                Design
                            </span>
                        </router-link>
                        
                        <!-- Divider -->
                        <div class="border-t-2 border-primary/20 my-2"></div>
                        
                        <!-- Auth Links -->
                        <template v-if="!isAuthenticated">
                            <router-link 
                                :to="{ name: 'Login' }"
                                @click="closeMobileMenu"
                                class="nav-link-mobile font-cinzel text-lg text-txt-primary hover:text-primary transition-colors p-3 rounded-lg hover:bg-primary/10"
                                active-class="bg-primary/10 text-primary font-bold"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">🗝️</span>
                                    Connexion
                                </span>
                            </router-link>
                            <router-link 
                                :to="{ name: 'Register' }"
                                @click="closeMobileMenu"
                                class="nav-link-mobile font-cinzel text-lg bg-primary hover:bg-primary-dark text-white transition-colors p-3 rounded-lg shadow-md"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">⚔️</span>
                                    S'inscrire
                                </span>
                            </router-link>
                        </template>
                        <template v-else>
                            <button
                                @click="handleLogout"
                                class="nav-link-mobile font-cinzel text-lg bg-accent hover:bg-accent/80 text-white transition-colors p-3 rounded-lg shadow-md"
                            >
                                <span class="flex items-center gap-2">
                                    <span class="text-xl">🚪</span>
                                    Déconnexion
                                </span>
                            </button>
                        </template>
                    </div>
                </div>
            </transition>
        </div>
    </nav>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { AuthService } from '../services/AuthService.js';

export default {
    name: 'Navbar',
    setup() {
        const router = useRouter();
        const route = useRoute();
        const mobileMenuOpen = ref(false);
        const isAuthenticated = ref(AuthService.isAuthenticated());

        // Watch for route changes to update authentication status
        watch(() => route.path, () => {
            isAuthenticated.value = AuthService.isAuthenticated();
        });

        // Check authentication status on mount
        onMounted(() => {
            isAuthenticated.value = AuthService.isAuthenticated();
        });

        const toggleMobileMenu = () => {
            mobileMenuOpen.value = !mobileMenuOpen.value;
        };

        const closeMobileMenu = () => {
            mobileMenuOpen.value = false;
        };

        const handleLogout = () => {
            AuthService.logout();
            isAuthenticated.value = false;
            closeMobileMenu();
            router.push({ name: 'Home' });
        };

        return {
            mobileMenuOpen,
            isAuthenticated,
            toggleMobileMenu,
            closeMobileMenu,
            handleLogout,
        };
    }
};
</script>

<style scoped>
/* Effet hover sur les liens de navigation */
.nav-link::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(to right, #C5A059, #A17C3B);
  transition: width 0.3s ease;
}

.nav-link:hover::after {
  width: 100%;
}

/* Animation slide-down pour menu mobile */
.slide-down-enter-active {
  animation: slideDown 0.3s ease-out;
}

.slide-down-leave-active {
  animation: slideDown 0.3s ease-in reverse;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
