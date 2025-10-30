// Service pour gérer les aventuriers

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Données mockées pour les aventuriers
const mockAdventurers = [
  {
    id: 1,
    nom: "Thorin Écu-de-Chêne",
    specialite: "Guerrier",
    niveau_experience: 8,
    taux_journalier_base: 250,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 2,
    nom: "Gandalf le Gris",
    specialite: "Mage",
    niveau_experience: 10,
    taux_journalier_base: 400,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 3,
    nom: "Legolas Feuille-Verte",
    specialite: "Archer",
    niveau_experience: 9,
    taux_journalier_base: 320,
    disponibilite: "en mission",
    date_disponibilite: "2025-11-15",
  },
  {
    id: 4,
    nom: "Gimli fils de Gloïn",
    specialite: "Guerrier",
    niveau_experience: 7,
    taux_journalier_base: 230,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 5,
    nom: "Aragorn Grands-Pas",
    specialite: "Rôdeur",
    niveau_experience: 9,
    taux_journalier_base: 350,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 6,
    nom: "Merlin l'Enchanteur",
    specialite: "Mage",
    niveau_experience: 10,
    taux_journalier_base: 450,
    disponibilite: "en repos",
    date_disponibilite: "2025-11-05",
  },
  {
    id: 7,
    nom: "Lancelot du Lac",
    specialite: "Paladin",
    niveau_experience: 8,
    taux_journalier_base: 300,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 8,
    nom: "Robin des Bois",
    specialite: "Archer",
    niveau_experience: 7,
    taux_journalier_base: 220,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
  {
    id: 9,
    nom: "Merida la Brave",
    specialite: "Archer",
    niveau_experience: 6,
    taux_journalier_base: 180,
    disponibilite: "en mission",
    date_disponibilite: "2025-11-10",
  },
  {
    id: 10,
    nom: "Jeanne d'Arc",
    specialite: "Paladin",
    niveau_experience: 9,
    taux_journalier_base: 380,
    disponibilite: "disponible",
    date_disponibilite: "2025-10-30",
  },
];

// Compteur pour les nouveaux IDs
let nextAdventurerId = 100;

/**
 * Récupère la liste des aventuriers
 * @returns {Promise<Array>} Liste des aventuriers
 */
export async function getAdventurers() {
  try {
    // Tentative d'appel API
    try {
      const response = await fetch(`${API_BASE_URL}/aventuriers`);
      if (response.ok) {
        return await response.json();
      }
    } catch (apiError) {
      console.log("API non disponible, utilisation des données mockées");
    }

    // Retourner les données mockées si l'API échoue
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([...mockAdventurers]);
      }, 300);
    });
  } catch (error) {
    console.error("Erreur dans getAdventurers:", error);
    return [...mockAdventurers];
  }
}

/**
 * Récupère un aventurier par son ID
 * @param {number} id - ID de l'aventurier
 * @returns {Promise<Object>} L'aventurier
 */
export async function getAdventurerById(id) {
  try {
    // Tentative d'appel API
    try {
      const response = await fetch(`${API_BASE_URL}/aventuriers/${id}`);
      if (response.ok) {
        return await response.json();
      }
    } catch (apiError) {
      console.log("API non disponible, recherche dans les données mockées");
    }

    // Rechercher dans les données mockées
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const adventurer = mockAdventurers.find((adv) => adv.id === id);
        if (adventurer) {
          resolve(adventurer);
        } else {
          reject(new Error("Aventurier non trouvé"));
        }
      }, 200);
    });
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

    // Tentative d'appel API
    try {
      const response = await fetch(`${API_BASE_URL}/aventuriers`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(apiData),
      });

      if (response.ok) {
        return await response.json();
      }
    } catch (apiError) {
      console.log("API non disponible, création locale");
    }

    // Simulation de création si l'API échoue
    return new Promise((resolve) => {
      setTimeout(() => {
        const newAdventurer = {
          id: nextAdventurerId++,
          ...apiData,
          niveau_experience: 1,
          disponibilite: "disponible",
          date_disponibilite: new Date().toISOString().split("T")[0],
        };
        mockAdventurers.push(newAdventurer);
        resolve(newAdventurer);
      }, 500);
    });
  } catch (error) {
    console.error("Erreur dans createAdventurer:", error);
    throw error;
  }
}
