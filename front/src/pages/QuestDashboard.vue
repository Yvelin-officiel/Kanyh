<template>
    <div class="min-h-screen bg-gradient-to-br from-[#f5f1e8] via-[#faf8f3] to-[#ebe5d9] font-inter">
        <!-- Navbar -->
        <Navbar :hide="showModal" />

        <div class="p-8 relative overflow-hidden">
            <!-- Effet de parchemin en arrière-plan -->
            <div class="absolute inset-0 opacity-[0.03] bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAxMCAwIEwgMCAwIDAgMTAiIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzAwMCIgc3Ryb2tlLXdpZHRoPSIxIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2dyaWQpIi8+PC9zdmc+')]"></div>
        
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
                <h1 class="text-5xl font-cinzel text-primary-dark mb-2 drop-shadow-[0_2px_4px_rgba(161,124,59,0.3)] tracking-wide">
                    📋 Registre des Quêtes
                </h1>
                <p class="text-txt-primary font-cinzel text-lg italic">
                    "Gérez vos aventures avec sagesse"
                </p>
            </div>

            <!-- Bouton Création Rapide -->
            <div class="mb-6 flex justify-end">
                <button
                    @click="openModal"
                    class="px-6 py-3 bg-gradient-to-br from-primary to-primary-dark text-white rounded-xl font-cinzel text-lg shadow-[0_4px_12px_rgba(197,160,89,0.3)] hover:shadow-[0_6px_20px_rgba(197,160,89,0.5)] transition-all duration-300 hover:scale-105 border border-primary-dark relative overflow-hidden"
                >
                    <span class="flex items-center gap-2">
                        <span class="text-2xl">➕</span>
                        Créer une Quête
                    </span>
                </button>
            </div>

            <!-- Modal de Création Rapide -->
            <QuickQuestModal
                :is-open="showModal"
                @close="closeModal"
                @quest-created="onQuestCreated"
            />

            <!-- Modal de Validation de Quête -->
            <ValidateQuestModal
                :is-open="showValidateModal"
                :quest="selectedQuest"
                @close="closeValidateModal"
                @quest-validated="onQuestValidated"
            />

            <!-- Filtres et Tri -->
            <div class="mb-6 bg-white/80 backdrop-blur-sm rounded-2xl shadow-lg border-2 border-primary/30 p-6 relative z-40">
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                    <!-- Recherche -->
                    <div class="flex flex-col">
                        <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
                            <span>🔍</span> Recherche
                        </label>
                        <input
                            v-model="filters.search"
                            type="text"
                            placeholder="Nom de quête..."
                            class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm"
                        />
                    </div>

                    <!-- Statut -->
                    <div class="flex flex-col relative z-50">
                        <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
                            <span>🎯</span> Statut
                        </label>
                        <div class="relative z-50">
                            <!-- Bouton dropdown -->
                            <button
                                @click="statusDropdownOpen = !statusDropdownOpen"
                                type="button"
                                class="w-full px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm text-left flex items-center justify-between"
                            >
                                <span class="text-sm text-txt-primary">
                                    {{ filters.statuts.length === 0 ? 'Aucun statut' : `${filters.statuts.length} sélectionné${filters.statuts.length > 1 ? 's' : ''}` }}
                                </span>
                                <span :class="['transition-transform duration-200', statusDropdownOpen ? 'rotate-180' : '']">▼</span>
                            </button>

                            <!-- Liste déroulante -->
                            <transition name="dropdown">
                                <div
                                    v-if="statusDropdownOpen"
                                    class="absolute z-[100] w-full mt-1 bg-white border-2 border-primary/40 rounded-lg shadow-lg p-2 space-y-1"
                                >
                                    <div
                                        v-for="statut in statusOptions"
                                        :key="statut.value"
                                        @click="toggleStatus(statut.value)"
                                        :class="[
                                            'flex items-center gap-2 cursor-pointer px-3 py-2 rounded-md transition-all duration-200',
                                            filters.statuts.includes(statut.value)
                                                ? statut.activeClass
                                                : 'hover:bg-gray-50'
                                        ]"
                                    >
                                        <div :class="[
                                            'w-5 h-5 rounded border-2 flex items-center justify-center transition-all duration-200',
                                            filters.statuts.includes(statut.value)
                                                ? statut.checkboxClass
                                                : 'border-gray-300 bg-white'
                                        ]">
                                            <span v-if="filters.statuts.includes(statut.value)" class="text-white text-xs font-bold">✓</span>
                                        </div>
                                        <span :class="[
                                            'text-sm font-cinzel transition-colors',
                                            filters.statuts.includes(statut.value)
                                                ? 'font-bold ' + statut.textClass
                                                : 'text-txt-secondary'
                                        ]">
                                            {{ statut.label }}
                                        </span>
                                    </div>
                                </div>
                            </transition>
                        </div>
                    </div>

                    <!-- Prime Min -->
                    <div class="flex flex-col">
                        <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
                            <span>💰</span> Prime Min.
                        </label>
                        <input
                            v-model.number="filters.primeMin"
                            type="number"
                            placeholder="0"
                            class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm"
                        />
                    </div>

                    <!-- Tri -->
                    <div class="flex flex-col">
                        <label class="text-sm font-cinzel text-txt-primary mb-2 flex items-center gap-2">
                            <span>📊</span> Trier par
                        </label>
                        <select
                            v-model="sortBy"
                            class="px-3 py-2 bg-white border-2 border-primary/40 rounded-lg font-cinzel focus:outline-none focus:ring-2 focus:ring-primary shadow-sm"
                        >
                            <option value="date_desc">Date (récent)</option>
                            <option value="date_asc">Date (ancien)</option>
                            <option value="prime_desc">Prime (haute)</option>
                            <option value="prime_asc">Prime (basse)</option>
                            <option value="duree_asc">Durée (courte)</option>
                            <option value="duree_desc">Durée (longue)</option>
                            <option value="echeance_asc">Échéance (proche)</option>
                        </select>
                    </div>
                </div>

                <!-- Bouton réinitialiser -->
                <div class="mt-4 text-center">
                    <button
                        @click="resetFilters"
                        class="px-4 py-2 text-sm font-cinzel text-primary hover:text-primary-dark transition-colors underline"
                    >
                        🔄 Réinitialiser les filtres
                    </button>
                </div>
            </div>

            <!-- Statistiques -->
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
                <div class="stat-card bg-white/90 border-2 border-primary/30 shadow-md">
                    <div class="text-3xl mb-2">📋</div>
                    <div class="text-2xl font-cinzel text-primary-dark font-bold">{{ filteredQuests.length }}</div>
                    <div class="text-sm font-cinzel text-txt-secondary">Quêtes</div>
                </div>
                <div class="stat-card bg-white/90 border-2 border-secondary/30 shadow-md">
                    <div class="text-3xl mb-2">⚔️</div>
                    <div class="text-2xl font-cinzel text-secondary-dark font-bold">{{ getStatCount('EN_COURS') }}</div>
                    <div class="text-sm font-cinzel text-txt-secondary">En cours</div>
                </div>
                <div class="stat-card bg-white/90 border-2 border-success/30 shadow-md">
                    <div class="text-3xl mb-2">✅</div>
                    <div class="text-2xl font-cinzel text-success font-bold">{{ getStatCount('TERMINEE') }}</div>
                    <div class="text-sm font-cinzel text-txt-secondary">Terminées</div>
                </div>
                <div class="stat-card bg-white/90 border-2 border-primary/30 shadow-md">
                    <div class="text-3xl mb-2">💰</div>
                    <div class="text-2xl font-cinzel text-primary-dark font-bold">{{ getTotalPrime() }}</div>
                    <div class="text-sm font-cinzel text-txt-secondary">Or Total</div>
                </div>
            </div>

            <!-- Message de chargement -->
            <div v-if="isLoading" class="text-center py-12">
                <div class="text-4xl mb-4 animate-pulse">⚡</div>
                <p class="text-txt-primary font-cinzel text-xl">Chargement des quêtes...</p>
            </div>

            <!-- Message d'erreur -->
            <div v-else-if="error" class="bg-red-50 border-2 border-accent text-txt-primary px-6 py-4 rounded-xl font-cinzel text-center shadow-md">
                <span class="text-3xl">⚠️</span>
                <p class="mt-2">{{ error }}</p>
            </div>

            <!-- Liste des quêtes -->
            <div v-else-if="filteredQuests.length === 0" class="text-center py-12">
                <div class="text-6xl mb-4">🗺️</div>
                <p class="text-txt-primary font-cinzel text-2xl mb-2">Aucune quête trouvée</p>
                <p class="text-txt-secondary font-cinzel italic">Créez votre première aventure !</p>
            </div>

            <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div
                    v-for="quest in filteredQuests"
                    :key="quest.id"
                    class="quest-card bg-white/95 rounded-xl shadow-md border-2 border-primary/25 p-6 hover:shadow-xl hover:scale-[1.02] transition-all duration-300 relative overflow-hidden cursor-pointer hover:border-primary/50"
                >
                    <!-- Badge statut -->
                    <div :class="getStatusClass(quest.statut)" class="absolute top-3 right-3 px-3 py-1 rounded-full text-xs font-cinzel font-bold">
                        {{ formatStatus(quest.statut) }}
                    </div>

                    <!-- Contenu -->
                    <h3 class="text-xl font-cinzel text-txt-primary font-bold mb-3 pr-20">{{ quest.nom }}</h3>
                    <p class="text-sm text-txt-secondary font-inter mb-4 line-clamp-3">{{ quest.description }}</p>

                    <!-- Infos -->
                    <div class="space-y-2 text-sm font-cinzel">
                        <div class="flex items-center gap-2">
                            <span class="text-lg">💰</span>
                            <span class="text-primary font-bold">{{ quest.prime }} or</span>
                        </div>
                        <div class="flex items-center gap-2">
                            <span class="text-lg">⏳</span>
                            <span class="text-txt-secondary">{{ quest.dureeEstimee }}h estimées</span>
                        </div>
                        <div class="flex items-center gap-2">
                            <span class="text-lg">📅</span>
                            <span :class="isExpiringSoon(quest.datePeremption) ? 'text-accent font-bold' : 'text-txt-secondary'">
                                {{ formatDate(quest.datePeremption) }}
                            </span>
                        </div>
                        <div v-if="quest.experienceGagnee" class="flex items-center gap-2">
                            <span class="text-lg">✨</span>
                            <span class="text-secondary">{{ quest.experienceGagnee }} XP</span>
                        </div>
                    </div>

                    <!-- Badge échéance proche -->
                    <div v-if="isExpiringSoon(quest.datePeremption) && quest.statut !== 'TERMINEE'" class="absolute bottom-12 right-3">
                        <span class="text-2xl animate-pulse">⚠️</span>
                    </div>

                    <!-- Bouton de validation -->
                    <button
                        @click.stop="openValidateModal(quest)"
                        class="absolute bottom-3 right-3 px-3 py-1.5 bg-gradient-to-br from-secondary to-secondary-dark text-white rounded-lg font-cinzel text-xs shadow-[0_2px_8px_rgba(45,106,79,0.3)] hover:shadow-[0_4px_12px_rgba(45,106,79,0.5)] transition-all duration-300 hover:scale-105 border border-secondary-dark flex items-center gap-1.5"
                    >
                        <span>👁️</span>
                        <span>A valider</span>
                    </button>
                </div>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import QuestService from '../services/QuestService';
import QuickQuestModal from '../components/QuickQuestModal.vue';
import ValidateQuestModal from '../components/ValidateQuestModal.vue';
import Navbar from '../components/Navbar.vue';

export default {
    name: 'QuestDashboard',
    components: {
        QuickQuestModal,
        ValidateQuestModal,
        Navbar
    },
    setup() {
        const quests = ref([]);
        const isLoading = ref(false);
        const error = ref('');
        const showModal = ref(false);
        const showValidateModal = ref(false);
        const selectedQuest = ref(null);
        const statusDropdownOpen = ref(false);

        // Filtres
        const filters = ref({
            search: '',
            statuts: ['NOUVELLE', 'EN_COURS'],
            primeMin: null,
        });

        const sortBy = ref('date_desc');

        // Options de statut avec styles
        const statusOptions = [
            {
                value: 'NOUVELLE',
                label: 'Nouvelle',
                activeClass: 'bg-primary/10 border-primary/30',
                checkboxClass: 'border-primary bg-primary',
                textClass: 'text-primary-dark'
            },
            {
                value: 'EN_COURS',
                label: 'En cours',
                activeClass: 'bg-secondary/10 border-secondary/30',
                checkboxClass: 'border-secondary bg-secondary',
                textClass: 'text-secondary-dark'
            },
            {
                value: 'TERMINEE',
                label: 'Terminée',
                activeClass: 'bg-success/10 border-success/30',
                checkboxClass: 'border-success bg-success',
                textClass: 'text-success'
            },
            {
                value: 'REJETEE',
                label: 'Rejetée',
                activeClass: 'bg-accent/10 border-accent/30',
                checkboxClass: 'border-accent bg-accent',
                textClass: 'text-accent'
            }
        ];

        // Toggle statut
        const toggleStatus = (status) => {
            const index = filters.value.statuts.indexOf(status);
            if (index > -1) {
                filters.value.statuts.splice(index, 1);
            } else {
                filters.value.statuts.push(status);
            }
        };

        // Charger les quêtes
        const loadQuests = async () => {
            isLoading.value = true;
            error.value = '';
            try {
                quests.value = await QuestService.fetchQuests();
            } catch (err) {
                console.error('Erreur lors du chargement des quêtes:', err);
                error.value = 'Erreur lors du chargement des quêtes';
            } finally {
                isLoading.value = false;
            }
        };

        // Quêtes filtrées
        const filteredQuests = computed(() => {
            let filtered = [...quests.value];

            // Recherche
            if (filters.value.search) {
                const search = filters.value.search.toLowerCase();
                filtered = filtered.filter(q => 
                    q.nom.toLowerCase().includes(search) ||
                    q.description.toLowerCase().includes(search)
                );
            }

            // Statut
            if (filters.value.statuts.length > 0) {
                filtered = filtered.filter(q => filters.value.statuts.includes(q.statut));
            }

            // Prime min
            if (filters.value.primeMin !== null && filters.value.primeMin > 0) {
                filtered = filtered.filter(q => q.prime >= filters.value.primeMin);
            }

            // Tri
            filtered.sort((a, b) => {
                switch (sortBy.value) {
                    case 'date_desc':
                        return new Date(b.id) - new Date(a.id);
                    case 'date_asc':
                        return new Date(a.id) - new Date(b.id);
                    case 'prime_desc':
                        return b.prime - a.prime;
                    case 'prime_asc':
                        return a.prime - b.prime;
                    case 'duree_desc':
                        return b.dureeEstimee - a.dureeEstimee;
                    case 'duree_asc':
                        return a.dureeEstimee - b.dureeEstimee;
                    case 'echeance_asc':
                        return new Date(a.datePeremption) - new Date(b.datePeremption);
                    default:
                        return 0;
                }
            });

            return filtered;
        });

        // Réinitialiser les filtres
        const resetFilters = () => {
            filters.value = {
                search: '',
                statuts: ['NOUVELLE', 'EN_COURS'],
                primeMin: null,
            };
            sortBy.value = 'date_desc';
        };

        // Gestion de la modal
        const openModal = () => {
            showModal.value = true;
        };

        const closeModal = () => {
            showModal.value = false;
        };

        const onQuestCreated = async () => {
            // Recharger les quêtes après création
            await loadQuests();
        };

        // Gestion du modal de validation
        const openValidateModal = (quest) => {
            selectedQuest.value = quest;
            showValidateModal.value = true;
        };

        const closeValidateModal = () => {
            showValidateModal.value = false;
            selectedQuest.value = null;
        };

        const onQuestValidated = async () => {
            // Recharger les quêtes après validation
            await loadQuests();
        };

        // Formater le statut
        const formatStatus = (status) => {
            const statusMap = {
                'NOUVELLE': 'Nouvelle',
                'EN_COURS': 'En cours',
                'TERMINEE': 'Terminée',
                'REJETEE': 'Rejetée'
            };
            return statusMap[status] || status;
        };

        // Classe CSS du statut
        const getStatusClass = (status) => {
            const classes = {
                'NOUVELLE': 'bg-primary text-dark-bg',
                'EN_COURS': 'bg-secondary text-light-bg',
                'TERMINEE': 'bg-success text-dark-bg',
                'REJETEE': 'bg-accent text-light-bg'
            };
            return classes[status] || 'bg-txt-secondary text-light-bg';
        };

        // Formater la date
        const formatDate = (dateString) => {
            const date = new Date(dateString);
            return date.toLocaleDateString('fr-FR', { 
                day: '2-digit', 
                month: 'short', 
                year: 'numeric' 
            });
        };

        // Vérifier si échéance proche (moins de 3 jours)
        const isExpiringSoon = (dateString) => {
            const date = new Date(dateString);
            const today = new Date();
            const diffTime = date - today;
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            return diffDays <= 3 && diffDays >= 0;
        };

        // Statistiques
        const getStatCount = (status) => {
            return quests.value.filter(q => q.statut === status).length;
        };

        const getTotalPrime = () => {
            return quests.value.reduce((sum, q) => sum + q.prime, 0);
        };

        onMounted(() => {
            loadQuests();
        });

        return {
            quests,
            filteredQuests,
            isLoading,
            error,
            filters,
            sortBy,
            showModal,
            showValidateModal,
            selectedQuest,
            statusDropdownOpen,
            statusOptions,
            toggleStatus,
            resetFilters,
            openModal,
            closeModal,
            onQuestCreated,
            openValidateModal,
            closeValidateModal,
            onQuestValidated,
            formatStatus,
            getStatusClass,
            formatDate,
            isExpiringSoon,
            getStatCount,
            getTotalPrime,
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
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 0.5;
  }
  50% {
    transform: translateY(-30px) scale(1.3);
    opacity: 0.8;
  }
}

/* Carte de statut */
.stat-card {
  padding: 1.5rem;
  border-radius: 1rem;
  text-align: center;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

/* Carte de quête */
.quest-card {
  animation: fadeInUp 0.5s ease-out backwards;
}

.quest-card:nth-child(1) { animation-delay: 0.05s; }
.quest-card:nth-child(2) { animation-delay: 0.1s; }
.quest-card:nth-child(3) { animation-delay: 0.15s; }
.quest-card:nth-child(4) { animation-delay: 0.2s; }
.quest-card:nth-child(5) { animation-delay: 0.25s; }
.quest-card:nth-child(6) { animation-delay: 0.3s; }

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

/* Line clamp pour description */
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Animation dropdown */
.dropdown-enter-active {
  animation: dropdown-in 0.2s ease-out;
}

.dropdown-leave-active {
  animation: dropdown-out 0.15s ease-in;
}

@keyframes dropdown-in {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes dropdown-out {
  from {
    opacity: 1;
    transform: translateY(0);
  }
  to {
    opacity: 0;
    transform: translateY(-10px);
  }
}
</style>
