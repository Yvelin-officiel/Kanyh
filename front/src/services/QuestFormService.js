import { fetchSpecialties } from "./SpecialiteService";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL; // À adapter selon votre configuration

// Compteur pour les IDs des nouvelles quêtes
let nextQuestId = 100;

class QuestFormService {
  /**
   * Récupère la liste des spécialités disponibles
   * @returns {Promise<Array>} Liste des spécialités
   */
  static async fetchSpecialties() {
    return await fetchSpecialties();
  }

  /**
   * Crée une nouvelle quête
   * @param {Object} questData - Les données de la quête
   * @returns {Promise<Object>} Les données de la quête créée
   */
  static async createQuest(questData) {
    try {
      // Tentative d'appel API
      try {
        const response = await fetch(`${API_BASE_URL}/quetes`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(questData),
        });

        if (response.ok) {
          const data = await response.json();
          return data;
        }
      } catch (apiError) {
        console.log("API non disponible, création locale de la quête");
      }

      // Simulation de création si l'API échoue
      return new Promise((resolve) => {
        setTimeout(() => {
          const newQuest = {
            id: nextQuestId++,
            ...questData,
            statut: "nouvelle",
            experienceGagnee: Math.floor(questData.prime * 0.3), // 30% de la prime en XP
          };
          resolve(newQuest);
        }, 500);
      });
    } catch (error) {
      console.error("Erreur lors de la création de la quête:", error);
      throw error;
    }
  }
}

export default QuestFormService;
