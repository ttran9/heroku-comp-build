package tran.compbuildbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.services.security.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static tran.compbuildbackend.constants.security.SecurityConstants.HEADER_STRING;
import static tran.compbuildbackend.constants.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(httpServletRequest);

            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // get the user id from our token.
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                // get the application user object
                ApplicationUser applicationUserDetails = customUserDetailsService.loadApplicationUserById(userId);
                /*
                * we don't need to pass credentials because with authentication or require passwords.
                * the last parameter is empty b/c we don't use roles in our application.
                */
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        applicationUserDetails, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJWTFromRequest(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            /*
             * start at 7 because "bearer " has a length of 7.
             * so this grabs the token.
             */
            return bearerToken.substring(7);
        }
        return null;
    }
}
