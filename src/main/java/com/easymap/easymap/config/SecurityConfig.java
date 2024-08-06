package com.easymap.easymap.config;


import com.easymap.easymap.entity.User;
import com.easymap.easymap.config.filter.JwtTokenValidatorFilter;
import com.easymap.easymap.provider.JwtProvider;
import com.easymap.easymap.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {


    private final UserRepository userRepository;
    private final String redirectUrl;
    private final JwtProvider jwtProvider;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // React 앱 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Autowired
    public SecurityConfig(UserRepository userRepository, @Value("${jwt.redirect-url}") String redirectUrl, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.redirectUrl = redirectUrl;
        this.jwtProvider = jwtProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
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
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oAuth2User.getAttribute("email");
                            User user = userRepository.findByEmail(email).orElseThrow();
                            String jwt = jwtProvider.generateToken(user, 60*60*100L);

                            // JWT를 쿠키에 추가
                            Cookie cookie = new Cookie("jwt", jwt);
                            cookie.setPath("/");
                            cookie.setHttpOnly(true);
                            //TODO 배포 시 true로 변경해야함 (TLS 적용 시)
                            cookie.setSecure(false);
                            response.addCookie(cookie);

                            // 리다이렉션
                            response.sendRedirect(redirectUrl);
                        })
                )
                .addFilterBefore(new JwtTokenValidatorFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return userRequest -> {
            // loadUser 람다
            OAuth2User oauth2User = delegate.loadUser(userRequest);

            // 사용자 정보를 가져온다.
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String oauth = userRequest.getClientRegistration().getClientName();

            // 사용자 정보를 DB에 저장하거나 업데이트 한다.
            Optional<User> user = userRepository.findByEmail(email);
            if(!user.isPresent()) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setNickname(name);
                newUser.setOauthType(oauth);
                newUser.setUserRole("ROLE_USER");
                userRepository.save(newUser);
            }
            return oauth2User;
        };
    }



}
