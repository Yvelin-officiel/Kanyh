// Service pour gérer les aventuriers
// TODO: Remplacer les données de test par de vrais appels API quand le backend sera connecté

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

/**
 * Récupère la liste des aventuriers
 * @returns {Promise<Array>} Liste des aventuriers
 */
export async function getAdventurers() {
  try {
    const response = await fetch(`${API_BASE_URL}/aventuriers`);
    if (!response.ok) {
      throw new Error("Erreur lors de la récupération des aventuriers");
    }
    return await response.json();
  } catch (error) {
    console.error("Erreur dans getAdventurers:", error);
    throw error;
  }
}

/**
 * Récupère un aventurier par son ID
 * @param {number} id - ID de l'aventurier
 * @returns {Promise<Object>} L'aventurier
 */
export async function getAdventurerById(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/aventuriers/${id}`);
    if (!response.ok) {
      throw new Error("Aventurier non trouvé");
    }
    return await response.json();
  } catch (error) {
    console.error("Erreur dans getAdventurerById:", error);
    throw error;
  }
}

/**
 * Crée un nouvel aventurier
 * @param {Object} adventurerData - Données de l'aventurier à créer (nom, specialite, taux_journalier_base)
 * @returns {Promise<Object>} L'aventurier créé
 */
export async function createAdventurer(adventurerData) {
  try {
    // Préparer les données pour l'API (uniquement les 3 champs requis)
    const apiData = {
      nom: adventurerData.nom,
      specialite: adventurerData.specialite,
      taux_journalier_base: adventurerData.taux_journalier_base,
    };

    const response = await fetch(`${API_BASE_URL}/aventuriers`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(apiData),
    });

    if (!response.ok) {
      throw new Error("Erreur lors de la création de l'aventurier");
    }

    return await response.json();
  } catch (error) {
    console.error("Erreur dans createAdventurer:", error);
    throw error;
  }
}
