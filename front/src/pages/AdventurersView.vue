<template>
  <div class="min-h-screen bg-gradient-to-br from-[#f5f1e8] via-[#faf8f3] to-[#ebe5d9] font-inter">
    <!-- Navbar -->
    <Navbar :hide="isAddModalOpen" />

    <div class="p-8 relative overflow-hidden">
      <!-- Effet de parchemin en arrière-plan -->
      <div
        class="absolute inset-0 opacity-[0.03] bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAxMCAwIEwgMCAwIDAgMTAiIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzAwMCIgc3Ryb2tlLXdpZHRoPSIxIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2dyaWQpIi8+PC9zdmc+')]">
      </div>

      <!-- Particules dorées flottantes -->
      <div class="absolute inset-0 pointer-events-none">
      <div class="sparkle-light" style="top: 10%; left: 15%; animation-delay: 0s;"></div>
      <div class="sparkle-light" style="top: 70%; left: 85%; animation-delay: 2s;"></div>
      <div class="sparkle-light" style="top: 30%; left: 40%; animation-delay: 4s;"></div>
      <div class="sparkle-light" style="top: 85%; left: 20%; animation-delay: 1s;"></div>
      <div class="sparkle-light" style="top: 20%; left: 75%; animation-delay: 3s;"></div>
    </div>

    <div class="max-w-7xl mx-auto relative z-10">
      <!-- En-tête -->
      <div class="text-center mb-8">
        <h1
          class="text-5xl font-cinzel text-primary-dark mb-2 drop-shadow-[0_2px_4px_rgba(161,124,59,0.3)] tracking-wide">
          🗡️ Registre des Aventuriers
        </h1>
        <p class="text-txt-primary font-cinzel text-lg italic">
          "Recrutez les plus vaillants héros du royaume"
        </p>
      </div>

      <!-- Bouton Création Rapide -->
      <div class="mb-6 flex justify-end">
        <button @click="openAddModal"
          class="px-6 py-3 bg-gradient-to-br from-primary to-primary-dark text-white rounded-xl font-cinzel text-lg shadow-[0_4px_12px_rgba(197,160,89,0.3)] hover:shadow-[0_6px_20px_rgba(197,160,89,0.5)] transition-all duration-300 hover:scale-105 border border-primary-dark relative overflow-hidden">
          <span class="flex items-center gap-2">
            <span class="text-2xl">➕</span>
            Recruter un Aventurier
          </span>
        </button>
      </div>

      <!-- Filtres et recherche -->
      <div class="mb-6 bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg border-2 border-primary/30 p-6">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <!-- Recherche par nom -->
          <div class="flex flex-col">
            <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
              <span>🔍</span> Nom
            </label>
            <input v-model="filters.nom" type="text" placeholder="Rechercher par nom..."
              class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm" />
          </div>

          <!-- Recherche par spécialité -->
          <div class="flex flex-col">
            <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
              <span>⚔️</span> Spécialité
            </label>
            <select v-model="filters.specialite"
              class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm">
              <option value="">Toutes les spécialités</option>
              <option v-for="specialite in specialites" :key="specialite.id" :value="specialite.nom">
                {{ specialite.nom }}
              </option>
            </select>
          </div>

          <!-- Recherche par rang -->
          <div class="flex flex-col">
            <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
              <span>⭐</span> Rang Min.
            </label>
            <select v-model="filters.rangMin"
              class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm">
              <option value="">Tous les rangs</option>
              <option value="0">Piou Piou (> 0 XP)</option>
              <option value="400">Débutant (> 400 XP)</option>
              <option value="1200">Combattant (> 1 200 XP)</option>
              <option value="2400">Mercenaire (> 2 400 XP)</option>
              <option value="4200">Baroudeur (> 4 200 XP)</option>
              <option value="6600">Guerrier (> 6 600 XP)</option>
              <option value="10000">Capitaine (> 10 000 XP)</option>
              <option value="15000">Commandant (> 15 000 XP)</option>
              <option value="22000">Elite (> 22 000 XP)</option>
              <option value="32000">Champion (> 32 000 XP)</option>
              <option value="47000">Tueur de Démons (> 47 000 XP)</option>
              <option value="70000">Fléau des Ténèbres (> 70 000 XP)</option>
              <option value="105000">Pourfendeur (> 105 000 XP)</option>
              <option value="160000">Tueur de Dragons (> 160 000 XP)</option>
              <option value="240000">Titan (> 240 000 XP)</option>
              <option value="360000">Caprice des Dieux (> 360 000 XP)</option>
              <option value="540000">Héros Légendaire (> 540 000 XP)</option>
              <option value="810000">Gardien Cosmique (> 810 000 XP)</option>
              <option value="1215000">Seigneur des Univers (> 1M215 XP)</option>
              <option value="1823000">Divinité (> 1M823 XP)</option>
            </select>
          </div>

          <!-- Tri -->
          <div class="flex flex-col">
            <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
              <span>📊</span> Trier par
            </label>
            <select v-model="sortBy"
              class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm">
              <option value="">Sans tri</option>
              <option value="experience-desc">Expérience (décroissant)</option>
              <option value="experience-asc">Expérience (croissant)</option>
              <option value="taux-desc">Taux journalier (décroissant)</option>
              <option value="taux-asc">Taux journalier (croissant)</option>
            </select>
          </div>
        </div>

        <!-- Bouton réinitialiser -->
        <div v-if="hasActiveFilters" class="mt-4 text-center">
          <button @click="resetFilters"
            class="px-4 py-2 text-sm font-cinzel text-primary hover:text-primary-dark transition-colors underline">
            🔄 Réinitialiser les filtres
          </button>
        </div>
      </div>

      <!-- État de chargement -->
      <div v-if="loading" class="text-center py-12">
        <div class="text-4xl mb-4 animate-pulse">⚡</div>
        <p class="text-txt-primary font-cinzel text-xl">Chargement des aventuriers...</p>
      </div>

      <!-- Message d'erreur -->
      <div v-else-if="error"
        class="bg-red-50 border-2 border-accent text-txt-primary px-6 py-4 rounded-xl font-cinzel text-center shadow-md">
        <span class="text-3xl">⚠️</span>
        <p class="mt-2">{{ error }}</p>
        <button @click="loadAdventurers"
          class="mt-4 px-6 py-2 bg-primary text-white rounded-lg hover:bg-primary-dark transition-colors">
          Réessayer
        </button>
      </div>

      <!-- Message si aucun résultat -->
      <div v-else-if="filteredAdventurers.length === 0" class="text-center py-12">
        <div class="text-6xl mb-4">🗡️</div>
        <p class="text-txt-primary font-cinzel text-2xl mb-2">Aucun aventurier trouvé</p>
        <p class="text-txt-secondary font-cinzel italic">Recrutez votre premier héros !</p>
      </div>

      <!-- Liste des aventuriers -->
      <div v-else>
        <!-- Compteur de résultats -->
        <div class="mb-4 text-center">
          <span class="text-txt-primary font-cinzel">
            <span class="text-2xl font-bold text-primary-dark">{{ filteredAdventurers.length }}</span>
            aventurier{{ filteredAdventurers.length > 1 ? 's' : '' }} trouvé{{ filteredAdventurers.length > 1 ? 's' : ''
            }}
          </span>
        </div>

        <!-- Grille des aventuriers -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="adventurer in filteredAdventurers" :key="adventurer.id"
            class="adventurer-card bg-white/95 rounded-xl shadow-md border-2 border-primary/25 overflow-hidden hover:shadow-xl hover:scale-[1.02] transition-all duration-300 cursor-pointer hover:border-primary/50 flex flex-col">
            <!-- Badge disponibilité -->
            <div class="absolute top-3 right-3 px-3 py-1 rounded-full text-xs font-cinzel font-bold z-10 shadow-md" :class="
                adventurer.disponibilite?.toLowerCase() === 'disponible'
                  ? 'bg-gradient-to-r from-green-500 to-green-600 text-white border-2 border-green-400/50'
                  : adventurer.disponibilite?.toLowerCase() === 'en_mission' || adventurer.disponibilite?.toLowerCase() === 'en mission'
                    ? 'bg-gradient-to-r from-orange-500 to-orange-600 text-white border-2 border-orange-400/50'
                    : 'bg-gradient-to-r from-gray-500 to-gray-600 text-white border-2 border-gray-400/50'
              ">
              {{ formatDisponibilite(adventurer.disponibilite) }}
            </div>

            <!-- En-tête de la carte -->
            <div class="p-6 bg-gradient-to-br from-secondary to-secondary-dark min-h-[120px] flex items-center">
              <h2 class="font-cinzel text-2xl text-light-bg pr-24 line-clamp-2">
                {{ adventurer.nom }}
              </h2>
            </div>

            <!-- Corps de la carte -->
            <div class="p-6 space-y-2 text-sm font-cinzel flex-1">
              <!-- Spécialité -->
              <div class="flex items-center gap-2">
               
                <span class="text-lg text-txt-secondary">{{ adventurer.specialite?.nom }}</span>
              </div>

              <!-- Expérience -->
              <div class="flex items-center gap-2">
                <span class="text-lg">⭐</span>
                <div class="flex flex-col">
                  <span class="text-txt-primary font-semibold">{{ adventurer.niveauExperience?.toLocaleString() || adventurer.niveau_experience?.toLocaleString() }} XP</span>
                  <span class="text-xs font-bold" :class="getRangByExperience(adventurer.niveauExperience || adventurer.niveau_experience).couleur">
                    {{ getRangByExperience(adventurer.niveauExperience || adventurer.niveau_experience).nom }}
                  </span>
                </div>
              </div>

              <!-- Taux journalier -->
              <div class="flex items-center gap-2">
                <span class="text-lg">💰</span>
                <span class="text-primary font-bold">{{ adventurer.tauxJournalierBase.toLocaleString() }} or</span>
              </div>

              <!-- Disponibilité -->
              <div class="flex items-center gap-2">
                <span class="text-lg">📅</span>
                <span class="text-txt-secondary">{{ formatDate(adventurer.dateDisponibilite || adventurer.date_disponibilite) }}</span>
              </div>
            </div>

            <!-- Bouton Détails -->
            <div class="p-4 pt-0">
              <button @click="openHistoryModal(adventurer)"
                class="w-full px-4 py-2 bg-gradient-to-r from-primary/10 to-primary/5 hover:from-primary/20 hover:to-primary/10 border-2 border-primary/30 hover:border-primary/50 rounded-lg font-cinzel text-primary-dark font-semibold transition-all duration-200 hover:scale-[1.02] flex items-center justify-center gap-2">
                <span class="text-lg">📜</span>
                Historique des Quêtes
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modale d'ajout d'aventurier -->
    <AddAdventurerModal :isOpen="isAddModalOpen" @close="closeAddModal" @adventurer-created="handleAdventurerCreated" />
    
    <!-- Modale historique des quêtes -->
    <AdventurerQuestsHistory 
      :isOpen="isHistoryModalOpen" 
      :adventurerId="selectedAdventurerId"
      :adventurerName="selectedAdventurerName"
      @close="closeHistoryModal" />
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { getAdventurers } from '../services/adventurersService';
import { fetchSpecialties } from '../services/SpecialiteService';
import AddAdventurerModal from '../components/AddAdventurerModal.vue';
import AdventurerQuestsHistory from '../components/AdventurerQuestsHistory.vue';
import Navbar from '../components/Navbar.vue';

export default {
  name: 'AdventurersView',
  components: {
    AddAdventurerModal,
    AdventurerQuestsHistory,
    Navbar
  },
  setup() {
    const adventurers = ref([]);
    const specialites = ref([]);
    const loading = ref(false);
    const error = ref(null);
    const isAddModalOpen = ref(false);
    const isHistoryModalOpen = ref(false);
    const selectedAdventurerId = ref(null);
    const selectedAdventurerName = ref('');

    // Filtres
    const filters = ref({
      nom: '',
      specialite: '',
      rangMin: ''
    });

    // Tri
    const sortBy = ref('');

    // Charger les aventuriers
    const loadAdventurers = async () => {
      loading.value = true;
      error.value = null;
      try {
        adventurers.value = await getAdventurers();
      } catch (err) {
        error.value = err.message || 'Erreur lors du chargement des aventuriers';
        console.error('Erreur:', err);
      } finally {
        loading.value = false;
      }
    };

    // Charger les spécialités
    const loadSpecialities = async () => {
      try {
        specialites.value = await fetchSpecialties();
      } catch (err) {
        console.error('Erreur lors du chargement des spécialités:', err);
      }
    };

    // Filtrer et trier les aventuriers
    const filteredAdventurers = computed(() => {
      let result = [...adventurers.value];

      // Filtrer par nom
      if (filters.value.nom) {
        result = result.filter(adv =>
          adv.nom.toLowerCase().includes(filters.value.nom.toLowerCase())
        );
      }

      // Filtrer par spécialité
      if (filters.value.specialite) {
        result = result.filter(adv =>
          (adv.specialite?.nom || adv.specialite) === filters.value.specialite
        );
      }

      // Filtrer par rang minimum (basé sur l'expérience)
      if (filters.value.rangMin !== null && filters.value.rangMin !== '') {
        const expMin = parseInt(filters.value.rangMin);
        result = result.filter(adv =>
          (adv.niveauExperience || adv.niveau_experience || 0) >= expMin
        );
      }

      // Trier
      if (sortBy.value) {
        result.sort((a, b) => {
          switch (sortBy.value) {
            case 'experience-desc':
              return b.niveau_experience - a.niveau_experience;
            case 'experience-asc':
              return a.niveau_experience - b.niveau_experience;
            case 'taux-desc':
              return b.tauxJournalierBase - a.tauxJournalierBase;
            case 'taux-asc':
              return a.tauxJournalierBase - b.tauxJournalierBase;
            default:
              return 0;
          }
        });
      }

      return result;
    });

    // Vérifier si des filtres sont actifs
    const hasActiveFilters = computed(() => {
      return filters.value.nom !== '' ||
        filters.value.specialite !== '' ||
        filters.value.rangMin !== '' ||
        sortBy.value !== '';
    });

    // Réinitialiser les filtres
    const resetFilters = () => {
      filters.value = {
        nom: '',
        specialite: '',
        rangMin: ''
      };
      sortBy.value = '';
    };

    // Formater la date
    const formatDate = (dateString) => {
      if (!dateString) return 'N/A';
      // Ajouter T00:00:00 pour un parsing correct en temps local
      const date = new Date(dateString + 'T00:00:00');
      if (isNaN(date.getTime())) return 'Date invalide';
      return date.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      });
    };

    // Obtenir le rang selon l'expérience
    const getRangByExperience = (exp) => {
      if (exp >= 1823000) return { nom: 'Divinité', couleur: 'text-purple-600' };
      if (exp >= 1215000) return { nom: 'Seigneur des Univers', couleur: 'text-purple-500' };
      if (exp >= 810000) return { nom: 'Gardien Cosmique', couleur: 'text-indigo-600' };
      if (exp >= 540000) return { nom: 'Héros Légendaire', couleur: 'text-indigo-500' };
      if (exp >= 360000) return { nom: 'Caprice des Dieux', couleur: 'text-blue-600' };
      if (exp >= 240000) return { nom: 'Titan', couleur: 'text-blue-500' };
      if (exp >= 160000) return { nom: 'Tueur de Dragons', couleur: 'text-cyan-600' };
      if (exp >= 105000) return { nom: 'Pourfendeur', couleur: 'text-cyan-500' };
      if (exp >= 70000) return { nom: 'Fléau des Ténèbres', couleur: 'text-teal-600' };
      if (exp >= 47000) return { nom: 'Tueur de Démons', couleur: 'text-teal-500' };
      if (exp >= 32000) return { nom: 'Champion', couleur: 'text-green-600' };
      if (exp >= 22000) return { nom: 'Elite', couleur: 'text-green-500' };
      if (exp >= 15000) return { nom: 'Commandant', couleur: 'text-lime-600' };
      if (exp >= 10000) return { nom: 'Capitaine', couleur: 'text-lime-500' };
      if (exp >= 6600) return { nom: 'Guerrier', couleur: 'text-yellow-600' };
      if (exp >= 4200) return { nom: 'Baroudeur', couleur: 'text-yellow-500' };
      if (exp >= 2400) return { nom: 'Mercenaire', couleur: 'text-orange-600' };
      if (exp >= 1200) return { nom: 'Combattant', couleur: 'text-orange-500' };
      if (exp >= 400) return { nom: 'Débutant', couleur: 'text-amber-600' };
      return { nom: 'Piou Piou', couleur: 'text-gray-500' };
    };

    // Formater la disponibilité
    const formatDisponibilite = (disponibilite) => {
      if (!disponibilite) return 'Indisponible';
      const dispLower = disponibilite.toLowerCase();
      const map = {
        'disponible': 'Disponible',
        'en_mission': 'En mission',
        'en mission': 'En mission',
        'en repos': 'En repos'
      };
      return map[dispLower] || disponibilite;
    };

    // Gestion de la modale
    const openAddModal = () => {
      isAddModalOpen.value = true;
    };

    const closeAddModal = () => {
      isAddModalOpen.value = false;
    };

    const handleAdventurerCreated = (newAdventurer) => {
      // Ajouter le nouvel aventurier à la liste
      adventurers.value.push(newAdventurer);
      // Afficher un message de succès (optionnel)
      console.log('Aventurier créé avec succès:', newAdventurer);
    };

    // Gestion de la modale d'historique
    const openHistoryModal = (adventurer) => {
      selectedAdventurerId.value = adventurer.id;
      selectedAdventurerName.value = adventurer.nom;
      isHistoryModalOpen.value = true;
    };

    const closeHistoryModal = () => {
      isHistoryModalOpen.value = false;
      selectedAdventurerId.value = null;
      selectedAdventurerName.value = '';
    };

    // Charger les données au montage du composant
    onMounted(() => {
      loadAdventurers();
      loadSpecialities();
    });

    return {
      adventurers,
      specialites,
      loading,
      error,
      filters,
      sortBy,
      filteredAdventurers,
      hasActiveFilters,
      isAddModalOpen,
      isHistoryModalOpen,
      selectedAdventurerId,
      selectedAdventurerName,
      loadAdventurers,
      resetFilters,
      formatDate,
      formatDisponibilite,
      getRangByExperience,
      openAddModal,
      closeAddModal,
      handleAdventurerCreated,
      openHistoryModal,
      closeHistoryModal
    };
  }
};
</script>



<style scoped>
/* Animation des particules dorées en mode clair */
.sparkle-light {
  position: absolute;
  width: 6px;
  height: 6px;
  background: radial-gradient(circle, #C5A059 0%, transparent 60%);
  border-radius: 50%;
  animation: float-light 6s infinite ease-in-out;
  box-shadow: 0 0 8px rgba(197, 160, 89, 0.4), 0 0 15px rgba(197, 160, 89, 0.2);
}

@keyframes float-light {

  0%,
  100% {
    transform: translateY(0) scale(1);
    opacity: 0.5;
  }

  50% {
    transform: translateY(-30px) scale(1.3);
    opacity: 0.8;
  }
}

/* Carte d'aventurier */
.adventurer-card {
  animation: fadeInUp 0.5s ease-out backwards;
  position: relative;
}

.adventurer-card:nth-child(1) {
  animation-delay: 0.05s;
}

.adventurer-card:nth-child(2) {
  animation-delay: 0.1s;
}

.adventurer-card:nth-child(3) {
  animation-delay: 0.15s;
}

.adventurer-card:nth-child(4) {
  animation-delay: 0.2s;
}

.adventurer-card:nth-child(5) {
  animation-delay: 0.25s;
}

.adventurer-card:nth-child(6) {
  animation-delay: 0.3s;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Line clamp pour nom */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
