import { AuthService } from '../services/AuthService';

/**
 * Classe d'erreur personnalisée pour les erreurs API
 */
export class ApiError extends Error {
  constructor(message, status, statusText) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.statusText = statusText;
  }
}

/**
 * Récupère les en-têtes HTTP avec le token d'authentification
 * @param {Object} additionalHeaders - En-têtes supplémentaires optionnels
 * @returns {Object} En-têtes HTTP avec Authorization si token présent
 */
export function getAuthHeaders(additionalHeaders = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...additionalHeaders,
  };

  const token = AuthService.getToken();
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  return headers;
}

/**
 * Effectue une requête fetch avec authentification
 * @param {string} url - URL de la requête
 * @param {Object} options - Options fetch (method, body, etc.)
 * @returns {Promise<Response>} Réponse de la requête
 * @throws {ApiError} Lance une erreur avec un message approprié selon le code d'erreur
 */
export async function fetchWithAuth(url, options = {}) {
  const authHeaders = getAuthHeaders(options.headers);
  
  const response = await fetch(url, {
    ...options,
    headers: authHeaders,
  });

  // Gérer les erreurs HTTP
  if (!response.ok) {
    let errorMessage;
    
    switch (response.status) {
      case 401:
        errorMessage = 'Vous devez être connecté pour accéder à cette ressource. Veuillez vous connecter.';
        break;
      case 403:
        errorMessage = 'Vous n\'avez pas les droits nécessaires pour effectuer cette action.';
        break;
      case 404:
        errorMessage = 'La ressource demandée n\'existe pas.';
        break;
      case 500:
        errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
        break;
      default:
        errorMessage = `Erreur ${response.status}: ${response.statusText}`;
    }

    throw new ApiError(errorMessage, response.status, response.statusText);
  }

  return response;
}
