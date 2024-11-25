package com.qiu.config;


import com.qiu.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author qiu
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {   //不使用WebSecurityConfigurerAdapter

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;



    @Bean
    public PasswordEncoder passwordEncoder() {
        // 这里返回PasswordEncoder实现
        return new BCryptPasswordEncoder(); //用这个进行加密
    }

    //****************


    /**
     * 登录时需要调用AuthenticationManager.authenticate执行一次校验
     * @param config
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    //登录请求放行
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                //配置关闭csrf机制
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //配置请求拦截方式
                //permitAll()随意访问

                        //.authorizeHttpRequests(auth->auth.anyRequest().authenticated());    //**之后删
               .authorizeHttpRequests(auth ->auth.requestMatchers("/user/login").anonymous()//anonymous匿名访问
        //***之后改                // .requestMatchers("/user/userInfo").authenticated()
                        //.requestMatchers("/link/getAllLink").authenticated()
        //***之后改                .requestMatchers("/logout").authenticated()
                        //.requestMatchers("/upload").authenticated() 暂时(改后端或前端代码)
                        .anyRequest().authenticated()); //其他请求不需要身份验证(现在是需要)



        //.authenticated()

        //关闭springsecurity默认的退出登录
        http.logout(logout -> logout.disable());
        //在哪个过滤器之前，传哪个过滤器字节码
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        http.cors(Customizer.withDefaults());   //跨域
        return http.build();


    }
//把token校验锅炉其添加到过滤器链中.....





    //***************


//    @Bean
//    public AuthenticationManager authenticationManager(
//            UserDetailsServiceImpl userDetailsService,
//            PasswordEncoder passwordEncoder
//    ){
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        //认证逻辑的提供者,配置UserDetailsService
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        //认证逻辑的提供者，配置PasswordEncoder
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//        authenticationManager = new ProviderManager(daoAuthenticationProvider);
//        return authenticationManager;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
////        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
////        authenticationManagerBuilder.userDetailsService(userDetailsService);
////        authenticationManager = authenticationManagerBuilder.build();
//        http
//                //禁用csrf保护
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                //授权请求
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                //对于接口登录，允许匿名访问
//                                .requestMatchers("/login").anonymous()//****
//                                //除上面外的所有请求全部不需要认证即可访问
//                                //.anyRequest().authenticated() // 其他所有请求都需要认证
//                                .anyRequest().permitAll())
//                .logout(logout -> logout.disable());
//        //AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//        return http.build();
//
//    }
//
//    //跨域cors配置
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList("*")); // 允许哪些域访问
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许哪些HTTP方法
//        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization")); // 允许哪些HTTP头
//        config.setExposedHeaders(Arrays.asList("header1", "header2")); // 允许哪些HTTP头被暴露给客户端
//        config.setAllowCredentials(true); // 是否允许发送Cookie
//
//        source.registerCorsConfiguration("/**", config); // 对所有路径应用CORS配置
//        return source;
//    }


}
// 假设我们有一个自定义的UserDetailsService和PasswordEncoder*************
//    @Bean
//    public UserDetailsService UserDetailsService() {
//        // 这里返回自定义的UserDetailsService实现
//        return new UserDetailsServiceImpl();
//    }
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;