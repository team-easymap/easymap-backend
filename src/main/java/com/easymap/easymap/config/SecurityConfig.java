package com.easymap.easymap.config;


import com.easymap.easymap.entity.User;
import com.easymap.easymap.config.filter.JwtTokenValidatorFilter;
import com.easymap.easymap.provider.JwtProvider;
import com.easymap.easymap.repository.UserRepository;
import com.easymap.easymap.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {


    private final UserRepository userRepository;
    private final UserService userService;
    private final String redirectUrl;
    private final String redirectUrlFailed;
    private final JwtProvider jwtProvider;

    @Value("${front-server.url}")
    private String frontServerUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontServerUrl, "favicon.ico") // React 앱 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Autowired
    public SecurityConfig(UserRepository userRepository, UserService userService, @Value("${jwt.redirect-url}") String redirectUrl, @Value("${jwt.redirect-url-failed}") String redirectUrlFailed, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.redirectUrl = redirectUrl;
        this.redirectUrlFailed = redirectUrlFailed;
        this.jwtProvider = jwtProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/test/**").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(auth -> auth.disable())
                .formLogin(auth -> auth.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService())
                        )
                        .successHandler(new CustomAuthenticationSuccessHandler(userRepository, jwtProvider, redirectUrl))
                        .failureHandler(new CustomAuthenticationFailureHandler(redirectUrlFailed))
                )
                .addFilterBefore(new JwtTokenValidatorFilter(jwtProvider, userRepository), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            // loadUser 람다
            try {
                OAuth2User oauth2User = delegate.loadUser(userRequest);
                // OAuth 정보를 가져온다.
                String oauth = userRequest.getClientRegistration().getClientName();
                String email = null;
                String nickname = null;
                System.out.println("OAUTH:" + oauth);
                if (oauth.equals("Google")) {
                    email = oauth2User.getAttribute("email");
                    nickname = oauth2User.getAttribute("name");
                } else if (oauth.equals("Kakao")) {
                    email = (String) ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email");
                    nickname = (String) ((Map<String, Object>) oauth2User.getAttribute("properties")).get("nickname");
                }

                Random random = new Random();
                String randomNum = "";
                int attempt = 0;
                final int MAX_ATTEMPTS = 1000;

                while (userService.isUserNicknameDuplicated(nickname + randomNum)) {
                    if (attempt >= MAX_ATTEMPTS) {
                        throw new RuntimeException("Unable to generate unique nickname after " + MAX_ATTEMPTS + " attempts.");
                    }
                    randomNum = String.valueOf(random.nextInt(1000)); // 0 ~ 999 사이의 숫자를 추가
                    attempt++;
                }
                nickname += randomNum;

                Optional<User> user = userRepository.findByEmail(email);
                // 해당 이메일로 가입된 사용자가 없는 경우
                if (!user.isPresent()) {
                    log.info("User is not present.");
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setNickname(nickname);
                    newUser.setOauthType(oauth);
                    newUser.setSignupDate(LocalDateTime.now());
                    newUser.setUserRole("ROLE_USER");
                    userRepository.save(newUser);
                }
                // 해당 이메일로 가입된 기존 유저와 같은 oauth로 로그인한 경우
                else if (user.get().getOauthType().equals(oauth)) {
                    log.info("The user is already registered with a same OAuth");
                    // 해당 이메일로 가입이 되었지만 탈퇴한 경우
                    if (user.get().getDeactivationDate() != null) {
                        User oldUser = user.get();
                        // 탈퇴한 유저가 재 가입시
                        userService.recoverData(oldUser);
                        userRepository.save(oldUser);
                    }
                    // 특이사항 없는 유저인 경우 스킵
                }
                // 해당 이메일로 가입된 유저가 있지만 현재 OAuth타입과 다른 경우
                else if (!user.get().getOauthType().equals(oauth)) {
                    log.info("The user is already registered with a different OAuth");
                }


                return oauth2User;
            } catch (Exception e){
                throw new OAuth2AuthenticationException(e.getMessage());
            }
        };
    }
}

class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final String redirectUrl;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository, JwtProvider jwtProvider, String redirectUrl) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        if(email == null) {
            email = (String) ((Map<String, Object>) oauth2User.getAttribute("kakao_account")).get("email");
        }
        User user = userRepository.findUserByEmailAndDeactivationDateIsNull(email).orElseThrow();
        String jwt = jwtProvider.generateToken(user, 60 * 60 * 100L);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //TODO 배포 시 true로 변경해야함 (TLS 적용 시)
        cookie.setSecure(false);
        response.addCookie(cookie);

        response.sendRedirect(redirectUrl);
    }
}

@Slf4j
class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final String redirectUrlFailed;

    public CustomAuthenticationFailureHandler(String redirectUrlFailed) {
        this.redirectUrlFailed = redirectUrlFailed;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2 authentication failed.");

        response.sendRedirect(redirectUrlFailed);
    }
}
