package org.vizuina;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mandala on 04-Mar-17.
 */
//@Component
public class AuthenticationTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public final String HEADER_SECURITY_TOKEN = "X-CustomToken";


    public AuthenticationTokenProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new NoOpAuthenticationManager());
        setAuthenticationSuccessHandler(new TokenSimpleUrlAuthenticationSuccessHandler());

    }


//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        Authentication authentication = new KontikiTokenAuthentication();
//        SecurityContextHolder.getContext().setAuthentication(getAuthenticationManager().authenticate(authentication));
//        filterChain.doFilter(servletRequest, servletResponse);
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader(HEADER_SECURITY_TOKEN);
        logger.info("token found:" + token);
        AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
        if (userAuthenticationToken == null)
            throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        return userAuthenticationToken;
    }

    private AbstractAuthenticationToken authUserByToken(String token) {
        if (token == null) return null;

        String username = "kontiki"; //logic to extract username from token
        String role = "ROLE_USER"; //extract role information from token

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));

        User principal = new User(username, "", authorities);
        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());

        return authToken;
    }

    class NoOpAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication)
                throws AuthenticationException {
            // TODO Auto-generated method stub
            return authentication;
        }
    }

    class TokenSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        @Override
        protected String determineTargetUrl(HttpServletRequest request,
                                            HttpServletResponse response) {
            String context = request.getContextPath();
            String fullURL = request.getRequestURI();
            String url = fullURL.substring(fullURL.indexOf(context) + context.length());
            return url;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
                throws IOException, ServletException {
            String url = determineTargetUrl(request, response);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

}
