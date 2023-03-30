package com.example.demo.application.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Configuration
@EnableWebSecurity(debug = true)
public class Oauth2ClientExample {

    final static String keyvalue="asdfsdfsagdgfdg34234234sa444t2t24gf24g242g42g4gdfsafd";
    private final Resttem resttem;

    public Oauth2ClientExample(Resttem resttem) {
        this.resttem = resttem;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
//                .addFilterAfter(new OncePerRequestFilter() {
//                    @Override
//                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//                        try {
//                            //verifyToken
//                            Map<String, Object> claims = Jwts.parserBuilder()
//                                    .setSigningKey(keyvalue.getBytes())
//                                    .build()
//                                    .parseClaimsJws(request.getHeader("Authorization")).getBody();
//                            String username = (String) claims.get("userEmail");
//                            //set authentication
//                            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + claims.get("roles")));
//                            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                            SecurityContextHolder.getContext().setAuthentication(authentication);
//                            filterChain.doFilter(request, response);
//                        }catch (Exception e){
//                            throw e;
//                        }
//                    }
//                }, OAuth2LoginAuthenticationFilter.class)
                .authorizeRequests().anyRequest().hasRole("USER")
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(clientRegistrationRepository())
                        .tokenEndpoint()
                        .accessTokenResponseClient(accessTokenResponseClient())
                        .and()
                        .successHandler((request, response, authentication) -> {
                            String userEmail = (String) ((OAuth2User) authentication.getPrincipal()).getAttributes().get("email");
                            //claims
                            Map<String, String> claims = new HashMap<>();
                            claims.put("userEmail", userEmail);
                            claims.put("roles", "USER");
                            //expire time
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, 100);
                            //secret key
                            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(Encoders.BASE64.encode(keyvalue.getBytes(StandardCharsets.UTF_8))));
                            //build jwt
                            String accessToken = Jwts.builder()
                                    .setClaims(claims)
                                    .setSubject(userEmail)
                                    .setIssuedAt(Calendar.getInstance().getTime())
                                    .setExpiration(calendar.getTime())
                                    .signWith(key)
                                    .compact();
                            System.out.println(accessToken);
                        }));

        return httpSecurity.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration
                .withRegistrationId("google")
                .clientId("686443762178-0gqta3eseri3r7gejdf242prqtrm69l3.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/google")
                .scope("email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("testclient")
                .build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();
//        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
//        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());

//        RestTemplate restTemplate2 = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        RestTemplate restTemplate = resttem.restTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);

        return accessTokenResponseClient;
    }
}
