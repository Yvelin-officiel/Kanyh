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
                    class="bg-white rounded-xl md:rounded-2xl shadow-[0_20px_60px_rgba(0,0,0,0.3)] border-2 border-primary/40 p-4 sm:p-5 md:p-6 relative max-w-md sm:max-w-lg md:max-w-xl w-full max-h-[90vh] md:max-h-[85vh] overflow-y-auto"
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
                            <span class="text-2xl sm:text-3xl md:text-4xl">🗡️</span>
                            <span class="hidden sm:inline">Nouvel Aventurier</span>
                            <span class="sm:hidden">Aventurier</span>
                            <span class="text-2xl sm:text-3xl md:text-4xl">🛡️</span>
                        </h2>
                        <div class="h-0.5 sm:h-1 bg-gradient-to-r from-transparent via-primary to-transparent mt-2 sm:mt-3 mx-auto w-3/4 rounded-full"></div>
                    </div>

                    <!-- Formulaire -->
                    <form @submit.prevent="handleSubmit" class="space-y-3 sm:space-y-4 md:space-y-5">
                        <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4">
                            <!-- Nom -->
                            <div class="flex flex-col sm:col-span-2">
                                <label for="modal-nom" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">👤</span> Nom
                                </label>
                                <input
                                    id="modal-nom"
                                    v-model="formData.nom"
                                    type="text"
                                    placeholder="Gandalf le Gris"
                                    class="input-field text-sm sm:text-base"
                                    required
                                />
                            </div>

                            <!-- Spécialité -->
                            <div class="flex flex-col">
                                <label for="modal-specialite" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">⚔️</span> Spécialité
                                </label>
                                <select
                                    id="modal-specialite"
                                    v-model="formData.specialiteId"
                                    class="input-field text-sm sm:text-base"
                                    required
                                >
                                    <option value="" disabled>Choisir</option>
                                    <option 
                                        v-for="specialite in specialites" 
                                        :key="specialite.id" 
                                        :value="specialite.id"
                                    >
                                        {{ specialite.nom }}
                                    </option>
                                </select>
                            </div>

                            <!-- Taux journalier -->
                            <div class="flex flex-col">
                                <label for="modal-taux" class="input-label flex items-center gap-1.5 sm:gap-2 text-sm sm:text-base">
                                    <span class="text-base sm:text-lg md:text-xl">💰</span> <span class="sm:inline">Taux/j</span>
                                </label>
                                <input
                                    id="modal-taux"
                                    v-model.number="formData.tauxJournalierBase"
                                    type="number"
                                    placeholder="100"
                                    class="input-field text-sm sm:text-base"
                                    min="0"
                                    required
                                />
                            </div>
                        </div>

                        <!-- Message d'erreur -->
                        <transition name="fade">
                            <div v-if="error" class="bg-red-50 border-2 border-accent text-txt-primary px-3 py-2 sm:px-4 sm:py-3 rounded-lg sm:rounded-xl font-cinzel text-xs sm:text-sm shadow-sm">
                                <div class="flex items-center gap-1.5 sm:gap-2">
                                    <span class="text-base sm:text-lg md:text-xl">⚠️</span>
                                    <span>{{ error }}</span>
                                </div>
                            </div>
                        </transition>

                        <!-- Boutons -->
                        <div class="flex gap-2 sm:gap-3 justify-center pt-3 sm:pt-4 border-t-2 border-primary/20">
                            <button
                                type="button"
                                @click="closeModal"
                                class="px-4 py-2 sm:px-5 sm:py-2.5 md:px-6 md:py-3 bg-gradient-to-br from-txt-secondary to-txt-primary text-white rounded-lg font-cinzel text-sm sm:text-base md:text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105"
                            >
                                <span class="hidden sm:inline">❌ Annuler</span>
                                <span class="sm:hidden">❌</span>
                            </button>
                            <button
                                type="submit"
                                class="px-4 py-2 sm:px-5 sm:py-2.5 md:px-6 md:py-3 bg-gradient-to-br from-secondary to-secondary-dark text-white rounded-lg font-cinzel text-sm sm:text-base md:text-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed relative overflow-hidden"
                                :disabled="isSubmitting"
                            >
                                <span class="relative z-10 flex items-center gap-1.5 sm:gap-2">
                                    <template v-if="isSubmitting">
                                        <span class="hidden sm:inline">⚡ Création...</span>
                                        <span class="sm:hidden">⚡</span>
                                    </template>
                                    <template v-else>
                                        <span class="hidden sm:inline">✨ Recruter</span>
                                        <span class="sm:hidden">✨</span>
                                    </template>
                                </span>
                                <div class="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent translate-x-[-200%] hover:translate-x-[200%] transition-transform duration-1000"></div>
                            </button>
                        </div>
                    </form>
                </div>
            </transition>
        </div>
    </transition>
</template>

<script>
import { ref, watch, onMounted } from 'vue';
import { createAdventurer } from '../services/adventurersService';
import { fetchSpecialties } from '../services/SpecialiteService';

export default {
    name: 'AddAdventurerModal',
    props: {
        isOpen: {
            type: Boolean,
            required: true
        }
    },
    emits: ['close', 'adventurer-created'],
    setup(props, { emit }) {
        const formData = ref({
            nom: '',
            specialiteId: '',
            tauxJournalierBase: 100,
        });

        const isSubmitting = ref(false);
        const error = ref('');
        const specialites = ref([]);

 
        // Charger les spécialités au montage
        onMounted(async () => {
            try {
                specialites.value = await fetchSpecialties();
            } catch (err) {
                console.error('Erreur lors du chargement des spécialités:', err);
            }
        });

        // Réinitialiser l'erreur quand la modal s'ouvre
        watch(() => props.isOpen, (newValue) => {
            if (newValue) {
                error.value = '';
            }
        });

        const closeModal = () => {
            emit('close');
            resetForm();
        };

        const resetForm = () => {
            formData.value = {
                nom: '',
                specialiteId: '',
                tauxJournalierBase: 100,
            };
            error.value = '';
        };

        const handleSubmit = async () => {
            isSubmitting.value = true;
            error.value = '';

            try {
                const result = await createAdventurer(formData.value);
                emit('adventurer-created', result);
                closeModal();
            } catch (err) {
                console.error('Erreur lors de la création de l\'aventurier:', err);
                error.value = err.message || 'Erreur lors de la création de l\'aventurier';
            } finally {
                isSubmitting.value = false;
            }
        };

        return {
            formData,
            isSubmitting,
            error,
            specialites,
            closeModal,
            handleSubmit
        };
    }
};
</script>

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

/* Scrollbar personnalisée */
.overflow-y-auto::-webkit-scrollbar {
    width: 8px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: rgba(197, 160, 89, 0.1);
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background: rgba(197, 160, 89, 0.5);
    border-radius: 10px;
}

.overflow-y-auto::-webkit-scrollbar-thumb:hover {
    background: rgba(197, 160, 89, 0.7);
}
</style>
