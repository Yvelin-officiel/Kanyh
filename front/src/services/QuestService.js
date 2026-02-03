const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
import { fetchWithAuth } from "../utils/api";

class QuestService {
  /**
   * Récupère la liste de toutes les quêtes
   * @returns {Promise<Array>} Liste des quêtes
   */
  static async fetchQuests() {
    const response = await fetchWithAuth(`${API_BASE_URL}/quetes`);
    const data = await response.json();
    return data;
  }

  /**
   * Récupère une quête par son ID
   * @param {number} id - L'ID de la quête
   * @returns {Promise<Object>} La quête
   */
  static async fetchQuestById(id) {
    const response = await fetchWithAuth(`${API_BASE_URL}/quetes/${id}`);
    const data = await response.json();
    return data;
  }

  static async fetchAdventurerQuestsHistory(adventurerId) {
    const response = await fetchWithAuth(
      `${API_BASE_URL}/quetes/historique/${adventurerId}`,
    );
    const data = await response.json();
    return data;
  }

  /**
   * Met à jour une quête
   * @param {number} id - L'ID de la quête
   * @param {Object} questData - Les données de la quête à mettre à jour
   * @returns {Promise<Object>} La quête mise à jour
   */
  static async updateQuest(id, questData) {
    const response = await fetchWithAuth(`${API_BASE_URL}/quetes/${id}`, {
      method: "PUT",
      body: JSON.stringify(questData),
    });

    const data = await response.json();
    return data;
  }

  /**
   * Supprime une quête
   * @param {number} id - L'ID de la quête
   * @returns {Promise<void>}
   */
  static async deleteQuest(id) {
    await fetchWithAuth(`${API_BASE_URL}/quetes/${id}`, {
      method: "DELETE",
    });
  }

  /**
   * Génère une équipe pour une quête
   * @param {number} id - L'ID de la quête
   * @param {number} nombreParticipants - Nombre d'aventuriers souhaités
   * @returns {Promise<Object>} L'équipe générée (TeamSuggestionDTO)
   */
  static async generateTeam(id, nombreParticipants = 4) {
    const response = await fetchWithAuth(
      `${API_BASE_URL}/quetes/${id}/generate-team`,
      {
        method: "POST",
      },
    );

    const data = await response.json();
    return data;
  }
  /**
   * Valide l'équipe générée et lance la quête
   * @param {number} id - L'ID de la quête
   * @param {Array<number>} aventurierIds - Liste des IDs des aventuriers
   * @returns {Promise<Object>} La quête mise à jour
   */
  static async validateTeam(id, aventurierIds) {
    const response = await fetchWithAuth(
      `${API_BASE_URL}/quetes/${id}/validate-team`,
      {
        method: "POST",
        body: JSON.stringify(aventurierIds),
      },
    );

    const data = await response.json();
    return data;
  }
}

export default QuestService;
