export const environment = {
  production: true,
  authentication: {
      keycloak: {
          config: {
              url: 'http://localhost:9090/auth',
              realm: 'app',
              clientId: 'frontend'
          }
      }
  }
};
