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
                            <span class="text-4xl">⚔️</span>
                            Nouvelle Quête
                            <span class="text-4xl">⚔️</span>
                        </h2>
                        <div class="h-1 bg-gradient-to-r from-transparent via-primary to-transparent mt-3 mx-auto w-3/4 rounded-full"></div>
                    </div>

                    <!-- Formulaire -->
                    <form @submit.prevent="handleSubmit" class="space-y-5">
                        <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
                            <!-- Nom -->
                            <div class="flex flex-col lg:col-span-2">
                                <label for="modal-nom" class="input-label flex items-center gap-2">
                                    <span class="text-xl">📜</span> Titre de la Quête
                                </label>
                                <input
                                    id="modal-nom"
                                    v-model="formData.nom"
                                    type="text"
                                    placeholder="La Quête du Graal Perdu..."
                                    class="input-field"
                                    required
                                />
                            </div>

                            <!-- Description -->
                            <div class="flex flex-col lg:col-span-2">
                                <label for="modal-description" class="input-label flex items-center gap-2">
                                    <span class="text-xl">✒️</span> Description
                                </label>
                                <textarea
                                    id="modal-description"
                                    v-model="formData.description"
                                    placeholder="Racontez l'histoire de cette quête épique..."
                                    class="input-field min-h-[100px]"
                                    rows="4"
                                    required
                                ></textarea>
                            </div>

                            <!-- Prime -->
                            <div class="flex flex-col">
                                <label for="modal-prime" class="input-label flex items-center gap-2">
                                    <span class="text-xl">💰</span> Prime (Or)
                                </label>
                                <input
                                    id="modal-prime"
                                    v-model.number="formData.prime"
                                    type="number"
                                    step="0.1"
                                    placeholder="1000.50"
                                    class="input-field"
                                    min="0"
                                    required
                                />
                            </div>

                            <!-- Durée en jours -->
                            <div class="flex flex-col">
                                <label for="modal-duree" class="input-label flex items-center gap-2">
                                    <span class="text-xl">⏳</span> Durée (jours)
                                </label>
                                <input
                                    id="modal-duree"
                                    v-model.number="formData.duree_jours"
                                    type="number"
                                    placeholder="7"
                                    class="input-field"
                                    min="1"
                                    required
                                />
                            </div>

                            <!-- Date Limite -->
                            <div class="flex flex-col lg:col-span-2">
                                <label for="modal-date" class="input-label flex items-center gap-2">
                                    <span class="text-xl">📅</span> Date Limite
                                </label>
                                <input
                                    id="modal-date"
                                    v-model="formData.date_peremption"
                                    type="date"
                                    class="input-field"
                                    required
                                />
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
                                :disabled="isSubmitting"
                            >
                                <span class="relative z-10 flex items-center gap-2">
                                    {{ isSubmitting ? '⚡ Création...' : '✨ Créer la Quête' }}
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
import { ref, watch } from 'vue';
import QuestFormService from '../services/QuestFormService';

export default {
    name: 'QuickQuestModal',
    props: {
        isOpen: {
            type: Boolean,
            required: true
        }
    },
    emits: ['close', 'quest-created'],
    setup(props, { emit }) {
        const formData = ref({
            nom: '',
            description: '',
            prime: 0,
            duree_jours: 1,
            date_peremption: '',
        });

        const isSubmitting = ref(false);
        const error = ref('');

        // Initialiser la date par défaut quand la modal s'ouvre
        watch(() => props.isOpen, (newValue) => {
            if (newValue) {
                const today = new Date();
                const nextWeek = new Date(today.setDate(today.getDate() + 7));
                formData.value.date_peremption = nextWeek.toISOString().split('T')[0];
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
                description: '',
                prime: 0,
                duree_jours: 1,
                date_peremption: '',
            };
            error.value = '';
        };

        const handleSubmit = async () => {
            isSubmitting.value = true;
            error.value = '';

            try {
                const questData = {
                    nom: formData.value.nom,
                    description: formData.value.description,
                    prime: formData.value.prime,
                    duree_estimee: formData.value.duree_jours * 24, // Convertir jours en heures
                    date_peremption: formData.value.date_peremption,
                };

                const result = await QuestFormService.createQuest(questData);
                emit('quest-created', result);
                closeModal();
            } catch (err) {
                console.error('Erreur lors de la création de la quête:', err);
                error.value = err.message || 'Erreur lors de la création de la quête';
            } finally {
                isSubmitting.value = false;
            }
        };

        return {
            formData,
            isSubmitting,
            error,
            closeModal,
            handleSubmit,
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
