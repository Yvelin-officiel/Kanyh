<script setup>
import { ref, watch, onMounted } from 'vue';
import { fetchSpecialties } from '../services/SpecialiteService';
import QuestService from '../services/QuestService';
import { getAdventurerById } from '../services/adventurersService';

const props = defineProps({
    isOpen: {
        type: Boolean,
        required: true
    },
    quest: {
        type: Object,
        default: null
    }
});

const emit = defineEmits(['close', 'quest-validated']);

const selectedSpecialites = ref([]);
const isSubmitting = ref(false);
const error = ref('');
const specialites = ref([]);
const generatedTeam = ref(null);
const teamAdventurers = ref([]);
const isGeneratingTeam = ref(false);
const showTeamPreview = ref(false);

const formData = ref({
    nom: '',
    description: '',
    prime: 0,
    dureeEstimee: 24,
    datePeremption: '',
    statut: 'EN_COURS'
});

// Icônes pour chaque spécialité
const specialiteIcons = {
    'Guerrier': '⚔️',
    'Mage': '🔮',
    'Archer': '🏹',
    'Rôdeur': '🌲',
    'Voleur': '🗝️',
    'Paladin': '✨',
    'Druide': '🍃',
    'Barde': '🎵'
};

const getSpecialiteIcon = (nom) => {
    return specialiteIcons[nom];
};

// Charger les spécialités au montage
onMounted(async () => {
    try {
        specialites.value = await fetchSpecialties();
    } catch (err) {
        console.error('Erreur lors du chargement des spécialités:', err);
    }
});

// Toggle une spécialité
const toggleSpecialite = (id) => {
    const index = selectedSpecialites.value.indexOf(id);
    if (index > -1) {
        selectedSpecialites.value.splice(index, 1);
    } else {
        selectedSpecialites.value.push(id);
    }
};

// Réinitialiser quand la modal s'ouvre
watch(() => props.isOpen, (newValue) => {
    if (newValue && props.quest) {
        error.value = '';
        selectedSpecialites.value = props.quest.specialitesRequises || [];
        generatedTeam.value = null;
        teamAdventurers.value = [];
        showTeamPreview.value = false;
        formData.value = {
            nom: props.quest.nom || '',
            description: props.quest.description || '',
            prime: props.quest.prime || 0,
            dureeEstimee: props.quest.dureeEstimee || 24,
            datePeremption: props.quest.datePeremption || '',
            statut: props.quest.statut || 'EN_COURS'
        };
    }
});

const closeModal = () => {
    emit('close');
};

const generateTeamPreview = async () => {
    isGeneratingTeam.value = true;
    error.value = '';

    try {
        // Mettre à jour la quête d'abord
        const updateData = {
            nom: formData.value.nom,
            description: formData.value.description,
            prime: formData.value.prime,
            dureeEstimee: formData.value.dureeEstimee,
            datePeremption: formData.value.datePeremption,
            statut: formData.value.statut,
            specialitesRequisesIds: formData.value.statut === 'REJETEE' ? [] : selectedSpecialites.value.map(id => String(id))
        };

        await QuestService.updateQuest(props.quest.id, updateData);

        // Générer l'équipe
        generatedTeam.value = await QuestService.generateTeam(props.quest.id, 4);

        // Récupérer les détails de chaque aventurier
        const adventurersPromises = generatedTeam.value.aventurierIds.map(id => getAdventurerById(id));
        teamAdventurers.value = await Promise.all(adventurersPromises);

        showTeamPreview.value = true;
    } catch (err) {
        console.error('Erreur lors de la génération de l\'équipe:', err);
        error.value = err.message || 'Erreur lors de la génération de l\'équipe';
    } finally {
        isGeneratingTeam.value = false;
    }
};

const handleSubmit = async () => {
    if (!showTeamPreview.value) {
        // Première étape : générer l'équipe
        await generateTeamPreview();
    } else {
        // Deuxième étape : valider l'équipe et lancer la quête
        isSubmitting.value = true;
        error.value = '';

        try {
            await QuestService.validateTeam(
                props.quest.id,
                generatedTeam.value.aventurierIds
            );
            emit('quest-validated');
            closeModal();
        } catch (err) {
            console.error('Erreur lors de la validation de l\'équipe:', err);
            error.value = err.message || 'Erreur lors de la validation de l\'équipe';
        } finally {
            isSubmitting.value = false;
        }
    }
};

const statusOptions = [
    { value: 'NOUVELLE', label: 'Nouvelle' },
    { value: 'EN_COURS', label: 'En cours' },
    { value: 'TERMINEE', label: 'Terminée' },
    { value: 'REJETEE', label: 'Rejetée' }
];
</script>

<template>
    <!-- Overlay -->
    <transition name="modal-fade">
        <div
            v-if="isOpen"
            class="fixed inset-0 bg-black/70 backdrop-blur-md z-[9999] flex items-center justify-center p-4"
            @click.self="closeModal"
        >
            <!-- Modal Container -->
            <transition name="modal-slide">
                <div
                    v-if="isOpen"
                    class="bg-white rounded-xl md:rounded-2xl shadow-[0_20px_60px_rgba(0,0,0,0.3)] border-2 border-primary/40 p-4 sm:p-5 md:p-6 relative max-w-md sm:max-w-lg md:max-w-2xl w-full max-h-[90vh] md:max-h-[85vh] overflow-y-auto"
                    @click.stop
                >
                    <!-- Coins décoratifs -->
                    <div class="hidden sm:block absolute top-0 left-0 w-8 h-8 md:w-10 md:h-10 border-l-2 border-t-2 border-primary/60 rounded-tl-xl md:rounded-tl-2xl animate-corner-glow"></div>
                    <div class="hidden sm:block absolute top-0 right-0 w-8 h-8 md:w-10 md:h-10 border-r-2 border-t-2 border-primary/60 rounded-tr-xl md:rounded-tr-2xl animate-corner-glow"></div>
                    <div class="hidden sm:block absolute bottom-0 left-0 w-8 h-8 md:w-10 md:h-10 border-l-2 border-b-2 border-primary/60 rounded-bl-xl md:rounded-bl-2xl animate-corner-glow"></div>
                    <div class="hidden sm:block absolute bottom-0 right-0 w-8 h-8 md:w-10 md:h-10 border-r-2 border-b-2 border-primary/60 rounded-br-xl md:rounded-br-2xl animate-corner-glow"></div>

                    <!-- Bouton fermer -->
                    <button
                        @click="closeModal"
                        class="absolute top-2 right-2 sm:top-3 sm:right-3 md:top-4 md:right-4 z-10 w-8 h-8 sm:w-9 sm:h-9 md:w-10 md:h-10 flex items-center justify-center rounded-full bg-accent/10 hover:bg-accent/20 text-accent hover:text-accent border border-accent/40 transition-all duration-300 hover:scale-110 hover:rotate-90"
                        aria-label="Fermer"
                    >
                        <span class="text-lg sm:text-xl md:text-2xl">✖</span>
                    </button>

                    <!-- Titre -->
                    <div class="text-center mb-4 sm:mb-5 md:mb-6 pr-8 sm:pr-10 md:pr-12">
                        <h2 class="text-xl sm:text-2xl md:text-3xl font-cinzel text-primary-dark mb-1 sm:mb-2 flex items-center justify-center gap-2 sm:gap-3">
                            <span class="text-2xl sm:text-3xl md:text-4xl">📜</span>
                            <span class="hidden sm:inline">Valider la Quête</span>
                            <span class="sm:hidden">Valider</span>
                            <span class="text-2xl sm:text-3xl md:text-4xl">✨</span>
                        </h2>
                        <div class="h-0.5 sm:h-1 bg-gradient-to-r from-transparent via-primary to-transparent mt-2 sm:mt-3 mx-auto w-3/4 rounded-full"></div>
                    </div>

                    <!-- Formulaire -->
                    <form @submit.prevent="handleSubmit" class="space-y-3 sm:space-y-4 md:space-y-5">
                        <!-- Affichage de l'équipe générée -->
                        <div v-if="showTeamPreview" class="bg-gradient-to-br from-primary/10 to-secondary/10 rounded-lg sm:rounded-xl p-4 sm:p-5 border-2 border-primary/30 shadow-lg">
                            <h3 class="font-cinzel text-primary-dark text-base sm:text-lg md:text-xl mb-3 sm:mb-4 flex items-center gap-2">
                                <span class="text-xl sm:text-2xl">👥</span> Équipe Générée
                            </h3>
                            
                            <div class="space-y-2 sm:space-y-3 mb-3 sm:mb-4">
                                <div v-for="adventurer in teamAdventurers" :key="adventurer.id" 
                                     class="bg-white rounded-lg p-3 sm:p-4 border border-primary/20 shadow-sm hover:shadow-md transition-all">
                                    <div class="flex items-center justify-between">
                                        <div class="flex items-center gap-2 sm:gap-3">
                                            <span class="text-2xl sm:text-3xl">{{ getSpecialiteIcon(adventurer.specialite?.nom) }}</span>
                                            <div>
                                                <p class="font-cinzel text-primary-dark text-sm sm:text-base font-semibold">{{ adventurer.nom }}</p>
                                                <p class="text-xs sm:text-sm text-txt-secondary">{{ adventurer.specialite?.nom }}</p>
                                            </div>
                                        </div>
                                        <div class="text-right">
                                            <p class="text-xs sm:text-sm text-txt-secondary">Taux journalier</p>
                                            <p class="font-cinzel text-secondary-dark text-sm sm:text-base font-semibold">{{ adventurer.tauxJournalierBase }} 💰</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="grid grid-cols-2 gap-2 sm:gap-3 bg-white/50 rounded-lg p-3 sm:p-4 border border-primary/20">
                                <div class="text-center">
                                    <p class="text-xs sm:text-sm text-txt-secondary mb-1">Nombre d'aventuriers</p>
                                    <p class="font-cinzel text-primary-dark text-lg sm:text-xl font-bold">{{ generatedTeam.nombreAventuriers }}</p>
                                </div>
                                <div class="text-center">
                                    <p class="text-xs sm:text-sm text-txt-secondary mb-1">Coût estimé</p>
                                    <p class="font-cinzel text-secondary-dark text-lg sm:text-xl font-bold">{{ generatedTeam.coutTotalEstime?.toFixed(2) }} 💰</p>
                                </div>
                                <div class="text-center col-span-2">
                                    <p class="text-xs sm:text-sm text-txt-secondary mb-1">Score moyen</p>
                                    <p class="font-cinzel text-primary-dark text-lg sm:text-xl font-bold">{{ generatedTeam.scoreMoyen?.toFixed(1) }} ⭐</p>
                                </div>
                            </div>
                        </div>

                        <div v-show="!showTeamPreview" class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                            <!-- Nom -->
                            <div class="flex flex-col sm:col-span-2">
                                <label for="modal-nom" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">📝</span> Nom de la quête
                                </label>
                                <input
                                    id="modal-nom"
                                    v-model="formData.nom"
                                    type="text"
                                    placeholder="Nom de la quête..."
                                    class="input-field text-sm sm:text-base"
                                    required
                                />
                            </div>

                            <!-- Description -->
                            <div class="flex flex-col sm:col-span-2">
                                <label for="modal-description" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">📖</span> Description
                                </label>
                                <textarea
                                    id="modal-description"
                                    v-model="formData.description"
                                    placeholder="Description de la quête..."
                                    class="input-field min-h-[60px] sm:min-h-[80px] resize-none text-sm sm:text-base"
                                    required
                                ></textarea>
                            </div>

                            <!-- Prime -->
                            <div class="flex flex-col">
                                <label for="modal-prime" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">💰</span> Prime (or)
                                </label>
                                <input
                                    id="modal-prime"
                                    v-model.number="formData.prime"
                                    type="number"
                                    placeholder="1000"
                                    class="input-field text-sm sm:text-base"
                                    min="0"
                                    step="0.1"
                                    required
                                />
                            </div>

                            <!-- Durée estimée -->
                            <div class="flex flex-col">
                                <label for="modal-duree" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">⏳</span> Durée (h)
                                </label>
                                <input
                                    id="modal-duree"
                                    v-model.number="formData.dureeEstimee"
                                    type="number"
                                    placeholder="24"
                                    class="input-field text-sm sm:text-base"
                                    min="1"
                                    required
                                />
                            </div>

                            <!-- Date de péremption -->
                            <div class="flex flex-col">
                                <label for="modal-date" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">📅</span> Date limite
                                </label>
                                <input
                                    id="modal-date"
                                    v-model="formData.datePeremption"
                                    type="date"
                                    class="input-field text-sm sm:text-base"
                                    required
                                />
                            </div>

                            <!-- Statut -->
                            <div class="flex flex-col">
                                <label for="modal-statut" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">🎯</span> Statut
                                </label>
                                <select
                                    id="modal-statut"
                                    v-model="formData.statut"
                                    class="input-field text-sm sm:text-base"
                                    required
                                >
                                    <option
                                        v-for="status in statusOptions"
                                        :key="status.value"
                                        :value="status.value"
                                    >
                                        {{ status.label }}
                                    </option>
                                </select>
                            </div>
                        </div>

                        <!-- Spécialités requises -->
                        <div v-show="!showTeamPreview" class="flex flex-col">
                            <label class="input-label flex items-center gap-1.5 sm:gap-2 mb-2 sm:mb-3 text-sm sm:text-base">
                                <span class="text-base sm:text-lg md:text-xl">⚔️</span> Spécialités requises
                            </label>
                            <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2 sm:gap-3">
                                <div
                                    v-for="specialite in specialites"
                                    :key="specialite.id"
                                    @click="toggleSpecialite(specialite.id)"
                                    :class="[
                                        'cursor-pointer px-2 sm:px-3 py-1.5 sm:py-2 rounded-lg border-2 transition-all duration-300 text-center font-cinzel text-xs sm:text-sm',
                                        selectedSpecialites.includes(specialite.id)
                                            ? 'bg-primary/20 border-primary text-primary-dark shadow-md scale-105'
                                            : 'bg-white border-primary/30 text-txt-secondary hover:border-primary/50 hover:bg-primary/5'
                                    ]"
                                >
                                    <span class="text-base sm:text-lg">{{ getSpecialiteIcon(specialite.nom) }}</span>
                                    <span class="block mt-0.5 sm:mt-1">{{ specialite.nom }}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Résumé -->
                        <div v-show="!showTeamPreview" class="bg-primary/5 rounded-lg sm:rounded-xl p-3 sm:p-4 border border-primary/20">
                            <h4 class="font-cinzel text-primary-dark text-xs sm:text-sm mb-1.5 sm:mb-2 flex items-center gap-1.5 sm:gap-2">
                                <span>📋</span> Résumé
                            </h4>
                            <div class="text-xs sm:text-sm text-txt-secondary font-inter space-y-0.5 sm:space-y-1">
                                <p><span class="font-semibold">Spécialités :</span>
                                    {{ selectedSpecialites.length > 0
                                        ? selectedSpecialites.map(id => specialites.find(s => s.id === id)?.nom).join(', ')
                                        : 'Aucune sélectionnée'
                                    }}
                                </p>
                                <p><span class="font-semibold">Statut :</span> {{ statusOptions.find(s => s.value === formData.statut)?.label }}</p>
                            </div>
                        </div>

                        <!-- Message d'erreur -->
                        <transition name="fade">
                            <div v-if="error" class="bg-red-50 border-2 border-accent text-txt-primary px-3 sm:px-4 py-2 sm:py-3 rounded-lg sm:rounded-xl font-cinzel text-xs sm:text-sm shadow-sm">
                                <div class="flex items-center gap-1.5 sm:gap-2">
                                    <span class="text-base sm:text-xl">⚠️</span>
                                    <span>{{ error }}</span>
                                </div>
                            </div>
                        </transition>

                        <!-- Boutons -->
                        <div class="flex flex-col sm:flex-row gap-2 sm:gap-3 justify-center pt-3 sm:pt-4 border-t-2 border-primary/20">
                            <button
                                v-if="showTeamPreview"
                                type="button"
                                @click="showTeamPreview = false"
                                class="px-4 sm:px-6 py-2 sm:py-3 bg-gradient-to-br from-txt-secondary to-txt-primary text-white rounded-lg font-cinzel text-sm sm:text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105"
                            >
                                ⬅️ Modifier
                            </button>
                            <button
                                v-else
                                type="button"
                                @click="closeModal"
                                class="px-4 sm:px-6 py-2 sm:py-3 bg-gradient-to-br from-txt-secondary to-txt-primary text-white rounded-lg font-cinzel text-sm sm:text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105"
                            >
                                ❌ Annuler
                            </button>
                            <button
                                type="submit"
                                class="px-4 sm:px-6 py-2 sm:py-3 bg-gradient-to-br from-secondary to-secondary-dark text-white rounded-lg font-cinzel text-sm sm:text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden"
                                :disabled="isGeneratingTeam || isSubmitting"
                            >
                                <span class="relative z-10 flex items-center justify-center gap-1.5 sm:gap-2">
                                    <span v-if="isGeneratingTeam">⚡ Génération...</span>
                                    <span v-else-if="showTeamPreview">✅ Valider l'équipe</span>
                                    <span v-else>🎯 Générer l'équipe</span>
                                </span>
                            </button>
                        </div>
                    </form>
                </div>
            </transition>
        </div>
    </transition>
</template>

<style scoped>
/* Animations pour la modal */
.modal-fade-enter-active,
.modal-fade-leave-active {
    transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
    opacity: 0;
}

.modal-slide-enter-active {
    transition: all 0.3s ease-out;
}

.modal-slide-leave-active {
    transition: all 0.25s ease-in;
}

.modal-slide-enter-from {
    transform: scale(0.9) translateY(-20px);
    opacity: 0;
}

.modal-slide-leave-to {
    transform: scale(0.9) translateY(-20px);
    opacity: 0;
}

/* Animation des coins */
@keyframes corner-glow {
    0%, 100% {
        opacity: 0.5;
    }
    50% {
        opacity: 1;
        filter: drop-shadow(0 0 6px rgba(197, 160, 89, 0.5));
    }
}

.animate-corner-glow {
    animation: corner-glow 2s ease-in-out infinite;
}

/* Transition fade */
.fade-enter-active,
.fade-leave-active {
    transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
    transform: translateY(-10px);
}

/* Style du slider */
input[type="range"]::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: linear-gradient(135deg, #C5A059, #A17C3B);
    cursor: pointer;
    box-shadow: 0 2px 6px rgba(197, 160, 89, 0.4);
}

input[type="range"]::-moz-range-thumb {
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background: linear-gradient(135deg, #C5A059, #A17C3B);
    cursor: pointer;
    box-shadow: 0 2px 6px rgba(197, 160, 89, 0.4);
    border: none;
}

/* Scrollbar personnalisée */
.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: rgba(197, 160, 89, 0.1);
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: rgba(197, 160, 89, 0.4);
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: rgba(197, 160, 89, 0.6);
}
</style>