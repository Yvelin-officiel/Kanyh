<template>
    <div class="min-h-screen bg-gradient-to-br from-[#f5f1e8] via-[#faf8f3] to-[#ebe5d9] font-inter">
        <!-- Navbar -->
        <Navbar />

        <!-- Hero Section -->
        <section class="relative overflow-hidden">
            <!-- Image de fond -->
            <div class="absolute inset-0 z-0">
                <img 
                    src="../assets/images/background-capuches-opale.png" 
                    alt="Les Capuches d'Opale" 
                    class="w-full h-full object-cover opacity-40"
                />
                <div class="absolute inset-0 bg-gradient-to-b from-transparent via-[#faf8f3]/50 to-[#faf8f3]"></div>
            </div>

            <!-- Particules dorées -->
            <div class="absolute inset-0 pointer-events-none z-10">
                <div class="sparkle-light" style="top: 15%; left: 10%; animation-delay: 0s;"></div>
                <div class="sparkle-light" style="top: 60%; left: 80%; animation-delay: 2s;"></div>
                <div class="sparkle-light" style="top: 35%; left: 30%; animation-delay: 4s;"></div>
                <div class="sparkle-light" style="top: 75%; left: 60%; animation-delay: 1s;"></div>
                <div class="sparkle-light" style="top: 25%; left: 70%; animation-delay: 3s;"></div>
            </div>

            <!-- Contenu Hero -->
            <div class="relative z-20 max-w-7xl mx-auto px-6 py-32 md:py-48">
                <div class="text-center space-y-8">
                    <!-- Logo principal -->
                    <div class="flex justify-center mb-8 animate-float">
                        <div class="w-32 h-32 md:w-40 md:h-40 rounded-full overflow-hidden border-4 border-primary/60 shadow-[0_0_30px_rgba(197,160,89,0.4)]">
                            <img src="../assets/images/logo-capuches-opale.png" alt="Les Capuches d'Opale" class="w-full h-full object-cover" />
                        </div>
                    </div>

                    <!-- Titre principal -->
                    <h1 class="text-5xl md:text-7xl font-cinzel text-primary-dark drop-shadow-[0_4px_8px_rgba(161,124,59,0.3)] tracking-wide animate-fade-in">
                        Les Capuches d'Opale
                    </h1>

                    <!-- Sous-titre -->
                    <p class="text-xl md:text-2xl font-cinzel text-txt-primary italic max-w-2xl mx-auto animate-fade-in-delay">
                        "Là où les légendes prennent vie et les aventures commencent"
                    </p>

                    <!-- Séparateur décoratif -->
                    <div class="flex justify-center">
                        <div class="h-1 w-64 bg-gradient-to-r from-transparent via-primary to-transparent rounded-full"></div>
                    </div>

                    <!-- Boutons d'action -->
                    <div class="flex flex-col sm:flex-row gap-4 justify-center items-center pt-8 animate-fade-in-delay-2">
                        <router-link 
                            :to="{ name: 'QuestDashboard' }"
                            class="btn-hero px-8 py-4 bg-gradient-to-br from-primary to-primary-dark text-white rounded-xl font-cinzel text-lg shadow-[0_4px_12px_rgba(197,160,89,0.3)] hover:shadow-[0_6px_20px_rgba(197,160,89,0.5)] transition-all duration-300 hover:scale-105"
                        >
                            <span class="flex items-center gap-3">
                                <span class="text-2xl">📜</span>
                                Découvrir les Quêtes
                            </span>
                        </router-link>
                        <router-link 
                            :to="{ name: 'AdventurersView' }"
                            class="btn-hero px-8 py-4 bg-gradient-to-br from-secondary to-secondary-dark text-white rounded-xl font-cinzel text-lg shadow-[0_4px_12px_rgba(45,106,79,0.3)] hover:shadow-[0_6px_20px_rgba(45,106,79,0.5)] transition-all duration-300 hover:scale-105"
                        >
                            <span class="flex items-center gap-3">
                                <span class="text-2xl">🗡️</span>
                                Voir les Aventuriers
                            </span>
                        </router-link>
                    </div>
                </div>
            </div>
        </section>

        <!-- Section Présentation -->
        <section class="max-w-7xl mx-auto px-6 py-20">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <!-- Carte 1 -->
                <div class="feature-card bg-white/95 rounded-2xl shadow-lg border-2 border-primary/30 p-8 hover:shadow-xl hover:scale-105 transition-all duration-300">
                    <div class="text-center space-y-4">
                        <div class="text-6xl mb-4">📋</div>
                        <h3 class="text-2xl font-cinzel text-primary-dark">Quêtes Épiques</h3>
                        <p class="text-txt-secondary font-inter">
                            Découvrez et gérez des quêtes légendaires. Créez, suivez et accomplissez des missions extraordinaires.
                        </p>
                    </div>
                </div>

                <!-- Carte 2 -->
                <div class="feature-card bg-white/95 rounded-2xl shadow-lg border-2 border-secondary/30 p-8 hover:shadow-xl hover:scale-105 transition-all duration-300">
                    <div class="text-center space-y-4">
                        <div class="text-6xl mb-4">⚔️</div>
                        <h3 class="text-2xl font-cinzel text-secondary-dark">Aventuriers</h3>
                        <p class="text-txt-secondary font-inter">
                            Recrutez des héros courageux et formez des équipes de choc pour vos aventures.
                        </p>
                    </div>
                </div>

                <!-- Carte 3 -->
                <div class="feature-card bg-white/95 rounded-2xl shadow-lg border-2 border-primary/30 p-8 hover:shadow-xl hover:scale-105 transition-all duration-300">
                    <div class="text-center space-y-4">
                        <div class="text-6xl mb-4">💰</div>
                        <h3 class="text-2xl font-cinzel text-primary-dark">Récompenses</h3>
                        <p class="text-txt-secondary font-inter">
                            Gagnez de l'or, de l'expérience et des trésors en accomplissant vos quêtes.
                        </p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Footer -->
        <footer class="bg-white/80 backdrop-blur-sm border-t-2 border-primary/30 mt-20">
            <div class="max-w-7xl mx-auto px-6 py-8">
                <div class="text-center space-y-4">
                    <p class="font-cinzel text-txt-secondary italic">
                        "Chaque aventure commence par un premier pas"
                    </p>
                    <p class="text-sm text-txt-secondary font-inter">
                        © 2025 Les Capuches d'Opale. Tous droits réservés.
                    </p>
                </div>
            </div>
        </footer>
    </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';

export default {
    name: 'Home',
    components: {
        Navbar
    }
};
</script>

<style scoped>
/* Animation des particules */
.sparkle-light {
  position: absolute;
  width: 8px;
  height: 8px;
  background: radial-gradient(circle, #C5A059 0%, transparent 60%);
  border-radius: 50%;
  animation: float-light 6s infinite ease-in-out;
  box-shadow: 0 0 8px rgba(197, 160, 89, 0.6), 0 0 15px rgba(197, 160, 89, 0.3);
}

@keyframes float-light {
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 0.6;
  }
  50% {
    transform: translateY(-30px) scale(1.4);
    opacity: 1;
  }
}

/* Animation flottante pour le logo */
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

.animate-float {
  animation: float 4s ease-in-out infinite;
}

/* Animations fade-in */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 1s ease-out;
}

.animate-fade-in-delay {
  animation: fadeIn 1s ease-out 0.3s backwards;
}

.animate-fade-in-delay-2 {
  animation: fadeIn 1s ease-out 0.6s backwards;
}

/* Cartes de fonctionnalités */
.feature-card {
  animation: fadeInUp 0.6s ease-out backwards;
}

.feature-card:nth-child(1) { animation-delay: 0.1s; }
.feature-card:nth-child(2) { animation-delay: 0.2s; }
.feature-card:nth-child(3) { animation-delay: 0.3s; }

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
