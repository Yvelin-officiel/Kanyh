<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center p-4">
        <!-- Overlay -->
        <div class="absolute inset-0 bg-black/60 backdrop-blur-sm" @click="closeModal"></div>

        <!-- Modal -->
        <div class="relative bg-white rounded-2xl shadow-2xl max-w-3xl w-full max-h-[85vh] overflow-hidden border-4 border-primary/30">
          <!-- Header -->
          <div class="bg-gradient-to-br from-secondary to-secondary-dark p-6 border-b-4 border-primary/30">
            <div class="flex items-center justify-between">
              <div>
                <h2 class="text-3xl font-cinzel text-light-bg mb-1">📜 Historique des Quêtes</h2>
                <p class="text-light-bg/80 font-cinzel">{{ adventurerName }}</p>
              </div>
              <button @click="closeModal"
                class="text-light-bg hover:text-accent transition-colors text-3xl leading-none">
                ×
              </button>
            </div>
          </div>

          <!-- Content -->
          <div class="p-6 overflow-y-auto max-h-[calc(85vh-140px)]">
            <!-- État de chargement -->
            <div v-if="loading" class="text-center py-12">
              <div class="text-4xl mb-4 animate-pulse">⚡</div>
              <p class="text-txt-primary font-cinzel text-lg">Chargement de l'historique...</p>
            </div>

            <!-- Message d'erreur -->
            <div v-else-if="error" class="bg-red-50 border-2 border-accent text-txt-primary px-6 py-4 rounded-xl font-cinzel text-center">
              <span class="text-3xl">⚠️</span>
              <p class="mt-2">{{ error }}</p>
            </div>

            <!-- Aucune quête -->
            <div v-else-if="quests.length === 0" class="text-center py-12">
              <div class="text-6xl mb-4">🗺️</div>
              <p class="text-txt-primary font-cinzel text-xl mb-2">Aucune quête réalisée</p>
              <p class="text-txt-secondary font-cinzel italic">Cet aventurier n'a pas encore participé à une quête</p>
            </div>

            <!-- Liste des quêtes -->
            <div v-else class="space-y-4">
              <div v-for="quest in quests" :key="quest.id"
                class="bg-white border-2 border-primary/25 rounded-xl p-5 hover:border-primary/50 hover:shadow-md transition-all">
                <!-- Nom de la quête -->
                <div class="flex items-start justify-between mb-3">
                  <h3 class="font-cinzel text-xl text-primary-dark font-bold flex items-center gap-2">
                    <span class="text-2xl">⚔️</span>
                    {{ quest.nom }}
                  </h3>
                  <span class="px-3 py-1 rounded-full text-xs font-cinzel font-bold"
                    :class="getStatutClass(quest.statut)">
                    {{ formatStatut(quest.statut) }}
                  </span>
                </div>

                <!-- Expérience gagnée -->
                <div class="flex items-center gap-2 mb-3">
                  <span class="text-lg">⭐</span>
                  <span class="text-txt-primary font-cinzel font-semibold">+{{ quest.experienceGagnee?.toLocaleString() || 0 }} XP</span>
                </div>

                <!-- Équipe -->
                <div v-if="quest.equipeDetails" class="bg-secondary/10 rounded-lg p-4 border-l-4 border-primary">
                  <p class="text-sm font-cinzel text-primary-dark font-bold mb-3">
                    👥 {{ quest.equipeDetails.nom }}
                  </p>
                  <div v-if="quest.equipeDetails.membres && quest.equipeDetails.membres.length > 0" class="space-y-2">
                    <p class="text-xs text-txt-secondary font-cinzel mb-2">Membres de l'équipe :</p>
                    <div class="flex flex-wrap gap-2">
                      <div v-for="membre in quest.equipeDetails.membres" :key="membre.id"
                        class="flex items-center gap-2 bg-white px-3 py-1.5 rounded-lg border border-primary/20">
                        <span class="text-txt-primary font-cinzel text-sm">{{ membre.nom }}</span>
                        <span class="text-xs font-cinzel font-semibold px-2 py-0.5 rounded-full text-white"
                          :style="{ backgroundColor: getRangColor(membre.niveauExperience) }">
                          {{ getRangByExperience(membre.niveauExperience) }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Informations supplémentaires -->
                <div class="mt-3 flex flex-wrap gap-4 text-xs text-txt-secondary font-cinzel">
                  <div class="flex items-center gap-1">
                    <span>💰</span>
                    <span>{{ quest.prime?.toLocaleString() || 0 }} or</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <span>⏱️</span>
                    <span>{{ quest.dureeEstimee }}h estimées</span>
                  </div>
                  <div v-if="quest.dateCreation" class="flex items-center gap-1">
                    <span>📅</span>
                    <span>Créée le {{ formatDate(quest.dateCreation) }}</span>
                  </div>
                </div>
              </div>

              <!-- Statistiques totales -->
              <div class="mt-6 bg-gradient-to-br from-primary/10 to-primary/5 rounded-xl p-5 border-2 border-primary/30">
                <h4 class="font-cinzel text-lg text-primary-dark font-bold mb-3 flex items-center gap-2">
                  <span class="text-2xl">📊</span>
                  Statistiques Totales
                </h4>
                <div class="grid grid-cols-2 gap-4">
                  <div class="text-center">
                    <p class="text-2xl font-bold text-primary-dark">{{ quests.length }}</p>
                    <p class="text-sm text-txt-secondary font-cinzel">Quête{{ quests.length > 1 ? 's' : '' }}</p>
                  </div>
                  <div class="text-center">
                    <p class="text-2xl font-bold text-green-600">{{ totalExperience.toLocaleString() }}</p>
                    <p class="text-sm text-txt-secondary font-cinzel">XP Total</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script>
import { ref, watch, computed } from 'vue';
import QuestService from '../services/QuestService';
import EquipeService from '../services/EquipeService';
import { getAdventurerById } from '../services/adventurersService';

export default {
  name: 'AdventurerQuestsHistory',
  props: {
    isOpen: {
      type: Boolean,
      required: true
    },
    adventurerId: {
      type: Number,
      default: null
    },
    adventurerName: {
      type: String,
      default: ''
    }
  },
  emits: ['close'],
  setup(props, { emit }) {
    const quests = ref([]);
    const loading = ref(false);
    const error = ref(null);

    // Calculer les statistiques totales
    const totalExperience = computed(() => {
      return quests.value.reduce((sum, quest) => sum + (quest.experienceGagnee || 0), 0);
    });

    const totalReward = computed(() => {
      return quests.value.reduce((sum, quest) => sum + (quest.prime || 0), 0);
    });

    // Obtenir le rang en fonction de l'expérience
    const getRangByExperience = (exp) => {
      if (exp >= 1823000) return 'Divinité';
      if (exp >= 460000) return 'Avatar';
      if (exp >= 266000) return 'Éternel';
      if (exp >= 160000) return 'Transcendant';
      if (exp >= 100000) return 'Immortel';
      if (exp >= 66600) return 'Demi-dieu';
      if (exp >= 46600) return 'Maître légendaire';
      if (exp >= 33000) return 'Légende vivante';
      if (exp >= 23000) return 'Héros';
      if (exp >= 16600) return 'Champion';
      if (exp >= 13000) return 'Maître d\'armes';
      if (exp >= 10000) return 'Capitaine';
      if (exp >= 8200) return 'Vétéran';
      if (exp >= 6600) return 'Guerrier';
      if (exp >= 5200) return 'Chasseur de primes';
      if (exp >= 3600) return 'Explorateur';
      if (exp >= 2200) return 'Mercenaire';
      if (exp >= 1000) return 'Aventurier débutant';
      if (exp >= 330) return 'Pious Bras';
      return 'Piou Piou';
    };

    // Obtenir la couleur du rang
    const getRangColor = (exp) => {
      if (exp >= 1823000) return '#7C2D12';
      if (exp >= 460000) return '#991B1B';
      if (exp >= 266000) return '#DC2626';
      if (exp >= 160000) return '#DC2626';
      if (exp >= 100000) return '#EF4444';
      if (exp >= 66600) return '#EF4444';
      if (exp >= 46600) return '#F59E0B';
      if (exp >= 33000) return '#F59E0B';
      if (exp >= 23000) return '#F59E0B';
      if (exp >= 16600) return '#8B5CF6';
      if (exp >= 13000) return '#8B5CF6';
      if (exp >= 10000) return '#8B5CF6';
      if (exp >= 8200) return '#3B82F6';
      if (exp >= 6600) return '#3B82F6';
      if (exp >= 5200) return '#3B82F6';
      if (exp >= 3600) return '#10B981';
      if (exp >= 2200) return '#10B981';
      if (exp >= 1000) return '#10B981';
      if (exp >= 330) return '#9CA3AF';
      return '#9CA3AF';
    };

    // Charger l'historique des quêtes
    const loadQuestsHistory = async () => {
      if (!props.adventurerId) return;

      loading.value = true;
      error.value = null;
      try {
        const questsData = await QuestService.fetchAdventurerQuestsHistory(props.adventurerId);
        
        // Charger les détails de l'équipe pour chaque quête
        const questsWithTeamDetails = await Promise.all(
          questsData.map(async (quest) => {
            if (quest.equipeId) {
              try {
                // Récupérer les détails de l'équipe (contient les participations)
                const equipeDetails = await EquipeService.fetchEquipeById(quest.equipeId);
                
                // Pour chaque participation, récupérer les détails de l'aventurier
                const membresAvecDetails = await Promise.all(
                  (equipeDetails.participations || []).map(async (participation) => {
                    try {
                      const aventurier = await getAdventurerById(participation.aventurierId);
                      return {
                        id: aventurier.id,
                        nom: aventurier.nom,
                        niveauExperience: aventurier.niveauExperience
                      };
                    } catch (err) {
                      console.error(`Erreur lors du chargement de l'aventurier ${participation.aventurierId}:`, err);
                      return {
                        id: participation.aventurierId,
                        nom: participation.aventurierNom,
                        niveauExperience: 0
                      };
                    }
                  })
                );
                
                return {
                  ...quest,
                  equipeDetails: {
                    nom: equipeDetails.nom,
                    membres: membresAvecDetails
                  }
                };
              } catch (err) {
                console.error(`Erreur lors du chargement de l'équipe ${quest.equipeId}:`, err);
                return quest;
              }
            }
            return quest;
          })
        );
        
        quests.value = questsWithTeamDetails;
      } catch (err) {
        error.value = err.message || 'Erreur lors du chargement de l\'historique';
        console.error('Erreur:', err);
      } finally {
        loading.value = false;
      }
    };

    // Charger les données quand la modale s'ouvre
    watch(() => props.isOpen, (newValue) => {
      if (newValue && props.adventurerId) {
        loadQuestsHistory();
      } else {
        quests.value = [];
        error.value = null;
      }
    });

    // Fermer la modale
    const closeModal = () => {
      emit('close');
    };

    // Formater la date
    const formatDate = (dateString) => {
      if (!dateString) return 'N/A';
      const date = new Date(dateString + 'T00:00:00');
      if (isNaN(date.getTime())) return 'Date invalide';
      return date.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      });
    };

    // Formater le statut
    const formatStatut = (statut) => {
      const map = {
        'NOUVELLE': 'Nouvelle',
        'EN_COURS': 'En cours',
        'TERMINEE': 'Terminée',
        'ANNULEE': 'Annulée',
        'ECHOUEE': 'Échouée'
      };
      return map[statut] || statut;
    };

    // Classe CSS du statut
    const getStatutClass = (statut) => {
      const classes = {
        'NOUVELLE': 'bg-blue-100 text-blue-700',
        'EN_COURS': 'bg-yellow-100 text-yellow-700',
        'TERMINEE': 'bg-green-100 text-green-700',
        'ANNULEE': 'bg-gray-100 text-gray-700',
        'ECHOUEE': 'bg-red-100 text-red-700'
      };
      return classes[statut] || 'bg-gray-100 text-gray-700';
    };

    return {
      quests,
      loading,
      error,
      totalExperience,
      totalReward,
      closeModal,
      formatDate,
      formatStatut,
      getStatutClass,
      getRangByExperience,
      getRangColor
    };
  }
};
</script>

<style scoped>
/* Animations de la modale */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-active .relative,
.modal-leave-active .relative {
  transition: transform 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .relative,
.modal-leave-to .relative {
  transform: scale(0.9);
}
</style>