const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

/**
 * Service pour gérer les équipes
 */
class EquipeService {
  /**
   * Récupère toutes les équipes
   * @returns {Promise<Array>} Liste des équipes
   */
  static async fetchEquipes() {
    try {
      const response = await fetch(`${API_BASE_URL}/equipes`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Erreur dans fetchEquipes:", error);
      throw error;
    }
  }

  /**
   * Récupère une équipe par son ID
   * @param {number} id - L'ID de l'équipe
   * @returns {Promise<Object>} Les détails de l'équipe
   */
  static async fetchEquipeById(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/equipes/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Erreur dans fetchEquipeById:", error);
      throw error;
    }
  }
}

export default EquipeService;
