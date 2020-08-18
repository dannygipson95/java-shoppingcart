package com.lambdaschool.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.logout().disable();

        http.authorizeRequests()
                .antMatchers("/",
                        "/h2-console/**",
                        "/swagger-resources/**",
                        "/swagger-resource/**",
                        "/swagger-ui.html",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/createnewuser")
                .permitAll()
                .antMatchers("/users/users",
                        "/users/user/*",
                        "/carts/cart/*",
                        "/products/products",
                        "products/product/*"
                        )
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "users/user")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/user/*")
                .hasAnyRole("ADMIN")
                .antMatchers("/carts/user")
                .authenticated()
                .antMatchers(HttpMethod.POST, "/carts/create/product/*")
                .authenticated()
                .antMatchers(HttpMethod.PUT, "/carts/update/cart/*/product/*")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/carts/delete/cart/*/product/*")
                .hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/products/product")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/products/product/*")
                .hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/product/product/*")
                .hasAnyRole("ADMIN")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
