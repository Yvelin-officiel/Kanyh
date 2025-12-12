<script setup>
import { ref, watch, onMounted } from 'vue';
import { fetchSpecialties } from '../services/SpecialiteService';
import QuestService from '../services/QuestService';

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
    return specialiteIcons[nom] || '⚔️';
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

const handleSubmit = async () => {
    if (formData.value.statut !== 'REJETEE' && selectedSpecialites.value.length === 0) {
        error.value = 'Veuillez sélectionner au moins une spécialité';
        return;
    }

    isSubmitting.value = true;
    error.value = '';

    try {
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
        emit('quest-validated', updateData);
        closeModal();
    } catch (err) {
        console.error('Erreur lors de la validation de la quête:', err);
        error.value = err.message || 'Erreur lors de la validation de la quête';
    } finally {
        isSubmitting.value = false;
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
            class="fixed inset-0 bg-black/50 backdrop-blur-sm z-50 flex items-center justify-center p-4"
            @click.self="closeModal"
        >
            <!-- Modal Container -->
            <transition name="modal-slide">
                <div
                    v-if="isOpen"
                    class="bg-white rounded-2xl shadow-[0_20px_60px_rgba(0,0,0,0.3)] border-2 border-primary/40 p-8 relative max-w-2xl w-full max-h-[90vh] overflow-y-auto"
                    @click.stop
                >
                    <!-- Coins décoratifs -->
                    <div class="absolute top-0 left-0 w-12 h-12 border-l-2 border-t-2 border-primary/60 rounded-tl-2xl animate-corner-glow"></div>
                    <div class="absolute top-0 right-0 w-12 h-12 border-r-2 border-t-2 border-primary/60 rounded-tr-2xl animate-corner-glow"></div>
                    <div class="absolute bottom-0 left-0 w-12 h-12 border-l-2 border-b-2 border-primary/60 rounded-bl-2xl animate-corner-glow"></div>
                    <div class="absolute bottom-0 right-0 w-12 h-12 border-r-2 border-b-2 border-primary/60 rounded-br-2xl animate-corner-glow"></div>

                    <!-- Bouton fermer -->
                    <button
                        @click="closeModal"
                        class="absolute top-4 right-4 z-10 w-10 h-10 flex items-center justify-center rounded-full bg-accent/10 hover:bg-accent/20 text-accent hover:text-accent border border-accent/40 transition-all duration-300 hover:scale-110 hover:rotate-90"
                        aria-label="Fermer"
                    >
                        <span class="text-2xl">✖</span>
                    </button>

                    <!-- Titre -->
                    <div class="text-center mb-6 pr-12">
                        <h2 class="text-3xl font-cinzel text-primary-dark mb-2 flex items-center justify-center gap-3">
                            <span class="text-4xl">📜</span>
                            Valider la Quête
                            <span class="text-4xl">✨</span>
                        </h2>
                        <div class="h-1 bg-gradient-to-r from-transparent via-primary to-transparent mt-3 mx-auto w-3/4 rounded-full"></div>
                    </div>

                    <!-- Formulaire -->
                    <form @submit.prevent="handleSubmit" class="space-y-5">
                        <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
                            <!-- Nom -->
                            <div class="flex flex-col lg:col-span-2">
                                <label for="modal-nom" class="input-label flex items-center gap-2">
                                    <span class="text-xl">📝</span> Nom de la quête
                                </label>
                                <input
                                    id="modal-nom"
                                    v-model="formData.nom"
                                    type="text"
                                    placeholder="Nom de la quête..."
                                    class="input-field"
                                    required
                                />
                            </div>

                            <!-- Description -->
                            <div class="flex flex-col lg:col-span-2">
                                <label for="modal-description" class="input-label flex items-center gap-2">
                                    <span class="text-xl">📖</span> Description
                                </label>
                                <textarea
                                    id="modal-description"
                                    v-model="formData.description"
                                    placeholder="Description de la quête..."
                                    class="input-field min-h-[80px] resize-none"
                                    required
                                ></textarea>
                            </div>

                            <!-- Prime -->
                            <div class="flex flex-col">
                                <label for="modal-prime" class="input-label flex items-center gap-2">
                                    <span class="text-xl">💰</span> Prime (or)
                                </label>
                                <input
                                    id="modal-prime"
                                    v-model.number="formData.prime"
                                    type="number"
                                    placeholder="1000"
                                    class="input-field"
                                    min="0"
                                    required
                                />
                            </div>

                            <!-- Durée estimée -->
                            <div class="flex flex-col">
                                <label for="modal-duree" class="input-label flex items-center gap-2">
                                    <span class="text-xl">⏳</span> Durée estimée (h)
                                </label>
                                <input
                                    id="modal-duree"
                                    v-model.number="formData.dureeEstimee"
                                    type="number"
                                    placeholder="24"
                                    class="input-field"
                                    min="1"
                                    required
                                />
                            </div>

                            <!-- Date de péremption -->
                            <div class="flex flex-col">
                                <label for="modal-date" class="input-label flex items-center gap-2">
                                    <span class="text-xl">📅</span> Date limite
                                </label>
                                <input
                                    id="modal-date"
                                    v-model="formData.datePeremption"
                                    type="date"
                                    class="input-field"
                                    required
                                />
                            </div>

                            <!-- Statut -->
                            <div class="flex flex-col">
                                <label for="modal-statut" class="input-label flex items-center gap-2">
                                    <span class="text-xl">🎯</span> Statut
                                </label>
                                <select
                                    id="modal-statut"
                                    v-model="formData.statut"
                                    class="input-field"
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
                        <div class="flex flex-col">
                            <label class="input-label flex items-center gap-2 mb-3">
                                <span class="text-xl">⚔️</span> Spécialités requises
                            </label>
                            <div class="grid grid-cols-2 md:grid-cols-4 gap-3">
                                <div
                                    v-for="specialite in specialites"
                                    :key="specialite.id"
                                    @click="toggleSpecialite(specialite.id)"
                                    :class="[
                                        'cursor-pointer px-3 py-2 rounded-lg border-2 transition-all duration-300 text-center font-cinzel text-sm',
                                        selectedSpecialites.includes(specialite.id)
                                            ? 'bg-primary/20 border-primary text-primary-dark shadow-md scale-105'
                                            : 'bg-white border-primary/30 text-txt-secondary hover:border-primary/50 hover:bg-primary/5'
                                    ]"
                                >
                                    <span class="text-lg">{{ getSpecialiteIcon(specialite.nom) }}</span>
                                    <span class="block mt-1">{{ specialite.nom }}</span>
                                </div>
                            </div>
                        </div>

                        <!-- Résumé -->
                        <div class="bg-primary/5 rounded-xl p-4 border border-primary/20">
                            <h4 class="font-cinzel text-primary-dark text-sm mb-2 flex items-center gap-2">
                                <span>📋</span> Résumé
                            </h4>
                            <div class="text-sm text-txt-secondary font-inter space-y-1">
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
                            <div v-if="error" class="bg-red-50 border-2 border-accent text-txt-primary px-4 py-3 rounded-xl font-cinzel text-sm shadow-sm">
                                <div class="flex items-center gap-2">
                                    <span class="text-xl">⚠️</span>
                                    <span>{{ error }}</span>
                                </div>
                            </div>
                        </transition>

                        <!-- Boutons -->
                        <div class="flex gap-3 justify-center pt-4 border-t-2 border-primary/20">
                            <button
                                type="button"
                                @click="closeModal"
                                class="px-6 py-3 bg-gradient-to-br from-txt-secondary to-txt-primary text-white rounded-lg font-cinzel text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105"
                            >
                                ❌ Annuler
                            </button>
                            <button
                                type="submit"
                                class="px-6 py-3 bg-gradient-to-br from-secondary to-secondary-dark text-white rounded-lg font-cinzel text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden"
                                :disabled="isSubmitting || (formData.statut !== 'REJETEE' && selectedSpecialites.length === 0)"
                            >
                                <span class="relative z-10 flex items-center gap-2">
                                    {{ isSubmitting ? '⚡ Validation...' : '✅ Valider la Quête' }}
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