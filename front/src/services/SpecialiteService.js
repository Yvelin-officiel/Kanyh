const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Données mockées pour les spécialités (fallback)
const mockSpecialties = [
  { id: 1, nom: "Guerrier" },
  { id: 2, nom: "Mage" },
  { id: 3, nom: "Archer" },
  { id: 4, nom: "Rôdeur" },
  { id: 5, nom: "Voleur" },
  { id: 6, nom: "Paladin" },
  { id: 7, nom: "Druide" },
  { id: 8, nom: "Barde" },
];

/**
 * Récupère la liste des spécialités depuis l'API
 * @returns {Promise<Array>} Liste des spécialités avec format { id, nom }
 */
export async function fetchSpecialties() {
  try {
    const response = await fetch(`${API_BASE_URL}/specialite`);

    if (!response.ok) {
      throw new Error(`Erreur HTTP: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.warn(
      "API non disponible, utilisation des spécialités mockées:",
      error.message
    );

    // Retourner les données mockées avec un délai pour simuler un appel réseau
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([...mockSpecialties]);
      }, 300);
    });
  }
}

export default {
  fetchSpecialties,
};
