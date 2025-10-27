const API_BASE_URL = 'http://localhost:3000/api'; // À adapter selon votre configuration

class QuestService {
    /**
     * Récupère la liste de toutes les quêtes
     * @returns {Promise<Array>} Liste des quêtes
     */
    static async fetchQuests() {
        try {
            const response = await fetch(`${API_BASE_URL}/quest`);
            if (!response.ok) {
                throw new Error(`Erreur ${response.status}: ${response.statusText}`);
            }
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Erreur lors de la récupération des quêtes:', error);
            throw error;
        }
    }

    /**
     * Récupère une quête par son ID
     * @param {number} id - L'ID de la quête
     * @returns {Promise<Object>} La quête
     */
    static async fetchQuestById(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/quest/${id}`);
            if (!response.ok) {
                throw new Error(`Erreur ${response.status}: ${response.statusText}`);
            }
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Erreur lors de la récupération de la quête:', error);
            throw error;
        }
    }

    /**
     * Met à jour une quête
     * @param {number} id - L'ID de la quête
     * @param {Object} questData - Les données de la quête à mettre à jour
     * @returns {Promise<Object>} La quête mise à jour
     */
    static async updateQuest(id, questData) {
        try {
            const response = await fetch(`${API_BASE_URL}/quest/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(questData),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(
                    errorData.message || `Erreur ${response.status}: ${response.statusText}`
                );
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Erreur lors de la mise à jour de la quête:', error);
            throw error;
        }
    }

    /**
     * Supprime une quête
     * @param {number} id - L'ID de la quête
     * @returns {Promise<void>}
     */
    static async deleteQuest(id) {
        try {
            const response = await fetch(`${API_BASE_URL}/quest/${id}`, {
                method: 'DELETE',
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(
                    errorData.message || `Erreur ${response.status}: ${response.statusText}`
                );
            }
        } catch (error) {
            console.error('Erreur lors de la suppression de la quête:', error);
            throw error;
        }
    }
}

export default QuestService;
