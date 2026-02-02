// Service pour gérer les aventuriers

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
import { fetchWithAuth } from '../utils/api';

/**
 * Récupère la liste des aventuriers
 * @returns {Promise<Array>} Liste des aventuriers
 */
export async function getAdventurers() {
  const response = await fetchWithAuth(`${API_BASE_URL}/aventuriers`);
  return await response.json();
}

/**
 * Récupère un aventurier par son ID
 * @param {number} id - ID de l'aventurier
 * @returns {Promise<Object>} L'aventurier
 */
export async function getAdventurerById(id) {
  const response = await fetchWithAuth(`${API_BASE_URL}/aventuriers/${id}`);
  return await response.json();
}

/**
 * Crée un nouvel aventurier
 * @param {Object} adventurerData - Données de l'aventurier à créer (nom, specialite, tauxJournalierBase)
 * @returns {Promise<Object>} L'aventurier créé
 */
export async function createAdventurer(adventurerData) {
  // Préparer les données pour l'API (uniquement les 3 champs requis)
  const apiData = {
    nom: adventurerData.nom,
    specialiteId: adventurerData.specialiteId,
    tauxJournalierBase: adventurerData.tauxJournalierBase,
  };

  const response = await fetchWithAuth(`${API_BASE_URL}/aventuriers`, {
    method: "POST",
    body: JSON.stringify(apiData),
  });

  return await response.json();
}
