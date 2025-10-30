const API_BASE_URL = import.meta.env.VITE_API_BASE_URL; // À adapter selon votre configuration

// Données mockées pour les quêtes
const mockQuests = [
  {
    id: 1,
    nom: "La Quête du Graal Perdu",
    description:
      "Retrouvez le légendaire Graal caché dans les montagnes du Nord. Une aventure périlleuse qui nécessitera courage et détermination.",
    prime: 5000,
    duree_estimee: 168, // 7 jours en heures
    date_peremption: "2025-12-31",
    statut: "nouvelle",
    experience_gagnee: 1500,
  },
  {
    id: 2,
    nom: "Défense du Village d'Ombrelune",
    description:
      "Les gobelins attaquent le paisible village d'Ombrelune. Les villageois ont besoin d'aide pour repousser les assaillants.",
    prime: 1200,
    duree_estimee: 48, // 2 jours
    date_peremption: "2025-11-05",
    statut: "en_cours",
    experience_gagnee: 800,
  },
  {
    id: 3,
    nom: "Escorte de la Caravane Royale",
    description:
      "Escortez une caravane marchande à travers les terres dangereuses jusqu'à la capitale. Protection assurée contre les bandits.",
    prime: 3500,
    duree_estimee: 120, // 5 jours
    date_peremption: "2025-11-15",
    statut: "nouvelle",
    experience_gagnee: 1000,
  },
  {
    id: 4,
    nom: "Le Dragon des Pics Gelés",
    description:
      "Un dragon terrorise les villages de montagne. Les habitants offrent une récompense généreuse pour qui saura les débarrasser de cette menace.",
    prime: 10000,
    duree_estimee: 240, // 10 jours
    date_peremption: "2025-12-20",
    statut: "nouvelle",
    experience_gagnee: 3000,
  },
  {
    id: 5,
    nom: "Enquête sur les Disparitions",
    description:
      "Des habitants disparaissent mystérieusement dans la Forêt Sombre. Menez l'enquête et découvrez la vérité.",
    prime: 2000,
    duree_estimee: 72, // 3 jours
    date_peremption: "2025-11-08",
    statut: "en_cours",
    experience_gagnee: 1200,
  },
  {
    id: 6,
    nom: "Collecte d'Herbes Rares",
    description:
      "L'alchimiste du royaume a besoin d'herbes rares qui ne poussent que dans les Marais Maudits. Mission de collecte urgente.",
    prime: 800,
    duree_estimee: 24,
    date_peremption: "2025-11-02",
    statut: "nouvelle",
    experience_gagnee: 400,
  },
  {
    id: 7,
    nom: "Récupération de l'Épée Légendaire",
    description:
      "L'épée ancestrale du roi a été volée par des bandits. Elle doit être récupérée avant le couronnement.",
    prime: 4500,
    duree_estimee: 96, // 4 jours
    date_peremption: "2025-11-12",
    statut: "terminee",
    experience_gagnee: 1800,
  },
  {
    id: 8,
    nom: "Purification du Puits Empoisonné",
    description:
      "Le puits principal du village a été empoisonné. Trouvez l'antidote et purifiez l'eau.",
    prime: 1500,
    duree_estimee: 48,
    date_peremption: "2025-11-01",
    statut: "nouvelle",
    experience_gagnee: 600,
  },
];

class QuestService {
  /**
   * Récupère la liste de toutes les quêtes
   * @returns {Promise<Array>} Liste des quêtes
   */
  static async fetchQuests() {
    try {
      // Tentative d'appel API
      try {
        const response = await fetch(`${API_BASE_URL}/quetes`);
        if (response.ok) {
          const data = await response.json();
          return data;
        }
      } catch (apiError) {
        console.log("API non disponible, utilisation des données mockées");
      }

      // Retourner les données mockées si l'API échoue
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve([...mockQuests]);
        }, 300);
      });
    } catch (error) {
      console.error("Erreur lors de la récupération des quêtes:", error);
      // En cas d'erreur, retourner les données mockées
      return [...mockQuests];
    }
  }

  /**
   * Récupère une quête par son ID
   * @param {number} id - L'ID de la quête
   * @returns {Promise<Object>} La quête
   */
  static async fetchQuestById(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/quetes/${id}`);
      if (!response.ok) {
        throw new Error(`Erreur ${response.status}: ${response.statusText}`);
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Erreur lors de la récupération de la quête:", error);
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
      const response = await fetch(`${API_BASE_URL}/quetes/${id}`, {
        method: "PUT",
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
      console.error("Erreur lors de la mise à jour de la quête:", error);
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
      const response = await fetch(`${API_BASE_URL}/quetes/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(
          errorData.message ||
            `Erreur ${response.status}: ${response.statusText}`
        );
      }
    } catch (error) {
      console.error("Erreur lors de la suppression de la quête:", error);
      throw error;
    }
  }
}

export default QuestService;
