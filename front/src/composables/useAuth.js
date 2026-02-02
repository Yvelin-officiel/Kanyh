import { ref, computed } from 'vue';
import { AuthService } from '../services/AuthService';

// État global d'authentification partagé entre tous les composants
const isAuthenticated = ref(AuthService.isAuthenticated());
const userRole = ref(AuthService.getUserRole());

export function useAuth() {
  // Mettre à jour l'état d'authentification
  const updateAuthState = () => {
    isAuthenticated.value = AuthService.isAuthenticated();
    userRole.value = AuthService.getUserRole();
  };

  // Login
  const login = async (credentials) => {
    const result = await AuthService.login(credentials);
    updateAuthState();
    return result;
  };

  // Logout
  const logout = () => {
    AuthService.logout();
    updateAuthState();
  };

  // Vérifier si l'utilisateur peut valider des quêtes
  const canValidateQuest = computed(() => {
    return userRole.value === 'ADMIN' || userRole.value === 'ASSISTANT';
  });

  return {
    isAuthenticated: computed(() => isAuthenticated.value),
    userRole: computed(() => userRole.value),
    canValidateQuest,
    login,
    logout,
    updateAuthState,
  };
}
