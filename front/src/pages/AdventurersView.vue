<template>
  <div class="bg-light-bg min-h-screen p-8 font-inter">
    <div class="max-w-7xl mx-auto">
      <!-- En-tête -->
      <header class="mb-8 text-center">
        <h1 class="titre text-secondary mb-2">Liste des Aventuriers</h1>
      </header>

      <!-- Filtres et recherche -->
      <div class="bg-[#f9f5e7] rounded-xl p-6 shadow-lg mb-8 border-2 border-primary">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
          <!-- Recherche par nom -->
          <div class="flex flex-col">
            <label class="font-cinzel text-lg text-txt-primary mb-2">Nom</label>
            <input
              v-model="filters.nom"
              type="text"
              placeholder="Rechercher par nom..."
              class="input-field"
            />
          </div>

          <!-- Recherche par spécialité -->
          <div class="flex flex-col">
            <label class="font-cinzel text-lg text-txt-primary mb-2">Spécialité</label>
            <input
              v-model="filters.specialite"
              type="text"
              placeholder="Guerrier, Mage..."
              class="input-field"
            />
          </div>

          <!-- Recherche par niveau -->
          <div class="flex flex-col">
            <label class="font-cinzel text-lg text-txt-primary mb-2">Niveau minimum</label>
            <input
              v-model.number="filters.niveauMin"
              type="number"
              min="0"
              max="10"
              placeholder="0-10"
              class="input-field"
            />
          </div>

          <!-- Tri -->
          <div class="flex flex-col">
            <label class="font-cinzel text-lg text-txt-primary mb-2">Trier par</label>
            <select v-model="sortBy" class="input-field">
              <option value="">Sans tri</option>
              <option value="experience-desc">Expérience (décroissant)</option>
              <option value="experience-asc">Expérience (croissant)</option>
              <option value="taux-desc">Taux journalier (décroissant)</option>
              <option value="taux-asc">Taux journalier (croissant)</option>
            </select>
          </div>
        </div>

        <!-- Bouton reset -->
        <div class="flex justify-end min-h-[40px]">
          <button v-if="hasActiveFilters" @click="resetFilters" class="btn-secondary">
            Réinitialiser les filtres
          </button>
        </div>
      </div>

      <!-- État de chargement -->
      <div v-if="loading" class="text-center py-12">
        <div class="w-12 h-12 mx-auto border-4 border-light-bg border-t-primary rounded-full animate-spin"></div>
        <p class="mt-4 text-txt-secondary font-cinzel">Chargement des aventuriers...</p>
      </div>

      <!-- Message d'erreur -->
      <div v-else-if="error" class="bg-erreur/10 border-2 border-erreur rounded-xl p-6 text-center">
        <p class="text-erreur font-cinzel text-xl mb-4">❌ {{ error }}</p>
        <button @click="loadAdventurers" class="btn-primary">Réessayer</button>
      </div>

      <!-- Liste des aventuriers -->
      <div v-else>
        <!-- Compteur de résultats -->
        <div class="mb-4 text-txt-secondary font-inter">
          <span class="font-semibold text-primary">{{ filteredAdventurers.length }}</span> 
          aventurier{{ filteredAdventurers.length > 1 ? 's' : '' }} trouvé{{ filteredAdventurers.length > 1 ? 's' : '' }}
        </div>

        <!-- Grille des aventuriers -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="adventurer in filteredAdventurers"
            :key="adventurer.id"
            class="bg-white rounded-xl shadow-lg overflow-hidden transition-all duration-300 hover:-translate-y-1 hover:shadow-xl border-3"
            :class="
              adventurer.disponibilite === 'disponible' ? 'border-success' : 
              adventurer.disponibilite === 'en mission' ? 'border-erreur' : 'border-primary'
            "
          >
            <!-- En-tête de la carte -->
            <div class="p-6 flex flex-col gap-3 bg-gradient-to-br from-secondary to-secondary-dark">
              <h2 class="font-cinzel text-2xl text-light-bg">
                {{ adventurer.nom }}
              </h2>
              
              <span 
                class="self-start px-3 py-2 rounded-full text-sm font-bold uppercase font-cinzel"
                :class="
                  adventurer.disponibilite === 'disponible' 
                    ? 'bg-success text-secondary-dark' 
                    : adventurer.disponibilite === 'en mission' 
                      ? 'bg-erreur text-white' 
                      : 'bg-primary text-dark-bg'
                "
              >
                {{ adventurer.disponibilite }}
              </span>
            </div>

            <!-- Corps de la carte -->
            <div class="p-6 bg-[#F9F5E7]">
              <!-- Spécialité -->
              <div class="flex justify-between items-center mb-4 pb-3 border-b border-primary">
                <span class="font-semibold text-txt-primary font-inter">🎯 Spécialité</span>
                <span class="text-txt-secondary text-right font-inter">
                  {{ adventurer.specialite }}
                </span>
              </div>

              <!-- Expérience -->
              <div class="flex justify-between items-center mb-4 pb-3 border-b border-primary">
                <span class="font-semibold text-txt-primary font-inter">⭐ Expérience</span>
                <div class="text-right">
                  <div class="flex gap-0.5">
                    <span 
                      v-for="i in 10" 
                      :key="i" 
                      class="text-base"
                      :class="i <= adventurer.niveau_experience ? 'text-primary' : 'text-gray-300'"
                    >★</span>
                  </div>
                </div>
              </div>

              <!-- Taux journalier -->
              <div class="flex justify-between items-center mb-4 pb-3 border-b border-primary">
                <span class="font-semibold text-txt-primary font-inter">💰 Taux journalier</span>
                <span class="font-semibold text-right font-inter text-primary">
                  {{ adventurer.taux_journalier_base.toLocaleString() }} PO
                </span>
              </div>

              <!-- Disponibilité -->
              <div class="flex justify-between items-center">
                <span class="font-semibold text-txt-primary font-inter">📅 Disponibilité</span>
                <span class="text-txt-secondary text-right font-inter">{{ formatDate(adventurer.date_disponibilite) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Message si aucun résultat -->
        <div v-if="filteredAdventurers.length === 0" class="text-center py-12">
          <p class="text-txt-secondary font-cinzel text-xl">Aucun aventurier ne correspond à vos critères.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import { getAdventurers } from '../services/adventurersService';

export default {
  name: 'AdventurersView',
  setup() {
    const adventurers = ref([]);
    const loading = ref(false);
    const error = ref(null);
    
    // Filtres
    const filters = ref({
      nom: '',
      specialite: '',
      niveauMin: null
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
          adv.specialite.toLowerCase().includes(filters.value.specialite.toLowerCase())
        );
      }

      // Filtrer par niveau minimum
      if (filters.value.niveauMin !== null && filters.value.niveauMin !== '') {
        result = result.filter(adv => 
          adv.niveau_experience >= filters.value.niveauMin
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
              return b.taux_journalier_base - a.taux_journalier_base;
            case 'taux-asc':
              return a.taux_journalier_base - b.taux_journalier_base;
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
             (filters.value.niveauMin !== null && filters.value.niveauMin !== '') ||
             sortBy.value !== '';
    });

    // Réinitialiser les filtres
    const resetFilters = () => {
      filters.value = {
        nom: '',
        specialite: '',
        niveauMin: null
      };
      sortBy.value = '';
    };

    // Formater la date
    const formatDate = (dateString) => {
      const date = new Date(dateString);
      return date.toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    };

    // Charger les données au montage du composant
    onMounted(() => {
      loadAdventurers();
    });

    return {
      adventurers,
      loading,
      error,
      filters,
      sortBy,
      filteredAdventurers,
      hasActiveFilters,
      loadAdventurers,
      resetFilters,
      formatDate
    };
  }
};
</script>

<style scoped>
/* Tout le style est géré par Tailwind CSS */
</style>
