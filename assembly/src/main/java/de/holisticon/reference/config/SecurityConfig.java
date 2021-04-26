package de.holisticon.reference.config;

import java.util.Arrays;
import java.util.Collections;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@KeycloakConfiguration
@Profile("!test")
@EnableWebSecurity
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  public static final String ROLE_INTERNAL = "internal";
  private final KeycloakClientRequestFactory keycloakClientRequestFactory;

  public SecurityConfig(KeycloakClientRequestFactory keycloakClientRequestFactory) {
    this.keycloakClientRequestFactory = keycloakClientRequestFactory;
    // to use principal and authentication together with @async
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

  }


  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public KeycloakRestTemplate keycloakRestTemplate() {
    return new KeycloakRestTemplate(this.keycloakClientRequestFactory);
  }

  @Bean
  @Override
  @ConditionalOnMissingBean(HttpSessionManager.class)
  protected HttpSessionManager httpSessionManager() {
    return new HttpSessionManager();
  }

  /**
   * Map Keycloak to spring roles.
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
    authorityMapper.setConvertToLowerCase(true);
    authorityMapper.setConvertToUpperCase(false);

    KeycloakAuthenticationProvider keyCloakAuthProvider = keycloakAuthenticationProvider();
    keyCloakAuthProvider.setGrantedAuthoritiesMapper(authorityMapper);
    auth.authenticationProvider(keyCloakAuthProvider);
  }


  /**
   * Use application.properties to configure keycloak.
   */
  @Bean
  public KeycloakConfigResolver keyCloakConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  }


  /**
   * No Session required.
   */
  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }


  /**
   * Configure access constraints
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    // @formatter:off
    http
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()
            .cors()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
            .and()
            .authorizeRequests()
            .antMatchers("/**")
            .authenticated()
            .anyRequest()
            .permitAll();
    // @formatter:on
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    // The authorization header is not allowed usually that's why we
    // explicitly allow it because every request includes the keycloak bearer token
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


  @Bean
  public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(KeycloakAuthenticationProcessingFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }


  @Bean
  public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(KeycloakPreAuthActionsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean<>(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }
}