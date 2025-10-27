// Service pour gérer les aventuriers
// TODO: Remplacer les données de test par de vrais appels API quand le backend sera connecté

const API_BASE_URL = "http://localhost:8000"; // À adapter selon votre configuration

// Données de test
const mockAdventurers = [
  {
    id: 1,
    nom: "Thorin Écu-de-Chêne",
    specialite: "Guerrier",
    niveau_experience: 5,
    taux_journalier_base: 150,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-27",
  },
  {
    id: 2,
    nom: "Gandalf le Gris",
    specialite: "Mage",
    niveau_experience: 10,
    taux_journalier_base: 300,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-27",
  },
  {
    id: 3,
    nom: "Legolas",
    specialite: "Archer",
    niveau_experience: 7,
    taux_journalier_base: 200,
    disponibilite: "en mission",
    date_disponibilite: "2025-11-15",
  },
  {
    id: 4,
    nom: "Gimli",
    specialite: "Guerrier",
    niveau_experience: 6,
    taux_journalier_base: 175,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-27",
  },
  {
    id: 5,
    nom: "Aragorn",
    specialite: "Rôdeur",
    niveau_experience: 8,
    taux_journalier_base: 225,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-27",
  },
  {
    id: 6,
    nom: "Merlin l'Enchanteur",
    specialite: "Mage",
    niveau_experience: 9,
    taux_journalier_base: 275,
    disponibilite: "en repos",
    date_disponibilite: "2025-11-01",
  },
];

/**
 * Récupère la liste des aventuriers
 * @returns {Promise<Array>} Liste des aventuriers
 */
export async function getAdventurers() {
  try {
    // Pour l'instant, on retourne les données de test
    // TODO: Décommenter quand le backend sera prêt
    /*
    const response = await fetch(`${API_BASE_URL}/aventuriers`);
    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des aventuriers');
    }
    return await response.json();
    */

    // Simulation d'un délai réseau
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(mockAdventurers);
      }, 500);
    });
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
    // Pour l'instant, on retourne les données de test
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const adventurer = mockAdventurers.find((adv) => adv.id === id);
        if (adventurer) {
          resolve(adventurer);
        } else {
          reject(new Error("Aventurier non trouvé"));
        }
      }, 300);
    });
  } catch (error) {
    console.error("Erreur dans getAdventurerById:", error);
    throw error;
  }
}
