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
        const errorData = await response.json();
        throw new Error(errorData.message || 'Erreur lors de l\'inscription');
      }

      return await response.text();
    } catch (error) {
      throw new Error(error.message || 'Erreur réseau');
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
        const errorData = await response.json();
        throw new Error(errorData.message || 'Identifiants invalides');
      }

      const data = await response.json();
      
      // Store token in localStorage
      if (data.token) {
        localStorage.setItem('authToken', data.token);
      }

      return data;
    } catch (error) {
      throw new Error(error.message || 'Erreur réseau');
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
