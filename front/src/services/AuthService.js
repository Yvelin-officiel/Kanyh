const API_BASE_URL = 'http://localhost:8080';

export const AuthService = {
  /**
   * Register a new user
   * @param {Object} userData - User registration data
   * @param {string} userData.userName - Username
   * @param {string} userData.password - Password
   * @returns {Promise<string>} Success message
   */
  async register(userData) {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
      });

      if (!response.ok) {
        let errorMessage = 'Erreur lors de l\'inscription';
        
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (parseError) {
          // Si le parsing JSON échoue, utiliser un message basé sur le code HTTP
          switch (response.status) {
            case 400:
              errorMessage = 'Données invalides. Vérifiez vos informations.';
              break;
            case 409:
              errorMessage = 'Ce nom d\'utilisateur est déjà utilisé.';
              break;
            case 500:
              errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
              break;
            default:
              errorMessage = `Erreur ${response.status}: Impossible de créer le compte.`;
          }
        }
        
        const error = new Error(errorMessage);
        error.status = response.status;
        throw error;
      }

      return await response.text();
    } catch (error) {
      if (error instanceof TypeError) {
        const networkError = new Error('Impossible de contacter le serveur. Vérifiez votre connexion internet.');
        networkError.type = 'NETWORK_ERROR';
        throw networkError;
      }
      throw error;
    }
  },

  /**
   * Login user
   * @param {Object} credentials - User credentials
   * @param {string} credentials.userName - Username
   * @param {string} credentials.password - Password
   * @returns {Promise<Object>} Response with token
   */
  async login(credentials) {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        let errorMessage = 'Identifiants invalides';
        
        try {
          const errorData = await response.json();
          errorMessage = errorData.message || errorMessage;
        } catch (parseError) {
          // Si le parsing JSON échoue, utiliser un message basé sur le code HTTP
          switch (response.status) {
            case 400:
              errorMessage = 'Nom d\'utilisateur ou mot de passe incorrect.';
              break;
            case 401:
              errorMessage = 'Nom d\'utilisateur ou mot de passe incorrect.';
              break;
            case 403:
              errorMessage = 'Accès refusé. Votre compte est peut-être désactivé.';
              break;
            case 429:
              errorMessage = 'Trop de tentatives. Veuillez réessayer dans quelques minutes.';
              break;
            case 500:
              errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
              break;
            default:
              errorMessage = `Erreur ${response.status}: Impossible de se connecter.`;
          }
        }
        
        const error = new Error(errorMessage);
        error.status = response.status;
        throw error;
      }

      const data = await response.json();
      
      // Store token in localStorage
      if (data.token) {
        localStorage.setItem('authToken', data.token);
      } else {
        throw new Error('Token non reçu du serveur.');
      }

      return data;
    } catch (error) {
      if (error instanceof TypeError) {
        const networkError = new Error('Impossible de contacter le serveur. Vérifiez votre connexion internet.');
        networkError.type = 'NETWORK_ERROR';
        throw networkError;
      }
      throw error;
    }
  },

  /**
   * Logout user
   */
  logout() {
    localStorage.removeItem('authToken');
  },

  /**
   * Get stored token
   * @returns {string|null} JWT token
   */
  getToken() {
    return localStorage.getItem('authToken');
  },

  /**
   * Check if user is authenticated
   * @returns {boolean}
   */
  isAuthenticated() {
    return !!this.getToken();
  }
};
