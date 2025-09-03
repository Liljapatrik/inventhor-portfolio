import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'https://kc.xn--flisbonn-64a.no/',
  realm: 'inventhor',
  clientId: 'inventhor-app',
});

export default keycloak;