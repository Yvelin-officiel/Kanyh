const API_BASE_URL = import.meta.env.VITE_API_BASE_URL; // À adapter selon votre configuration

class QuestFormService {
  /**
   * Récupère la liste des spécialités disponibles
   * @returns {Promise<Array>} Liste des spécialités
   */
  static async fetchSpecialties() {
    try {
      const response = await fetch(`${API_BASE_URL}/specialite`);
      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Erreur lors de la récupération des spécialités:", error);
      throw error;
    }
  }

  /**
   * Crée une nouvelle quête
   * @param {Object} questData - Les données de la quête
   * @returns {Promise<Object>} Les données de la quête créée
   */
  static async createQuest(questData) {
    try {
      const response = await fetch(`${API_BASE_URL}/quest`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(questData),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(
          errorData.message ||
            `Erreur ${response.status}: ${response.statusText}`
        );
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Erreur lors de la création de la quête:", error);
      throw error;
    }
  }
}

export default QuestFormService;
