const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
import { fetchWithAuth } from '../utils/api';

/**
 * Récupère la liste des spécialités depuis l'API
 * @returns {Promise<Array>} Liste des spécialités avec format { id, nom }
 */
export async function fetchSpecialties() {
  const response = await fetchWithAuth(`${API_BASE_URL}/specialite`);
  const data = await response.json();
  return data;
}

export default {
  fetchSpecialties,
};
