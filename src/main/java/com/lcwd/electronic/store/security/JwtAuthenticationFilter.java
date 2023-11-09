package com.lcwd.electronic.store.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lcwd.electronic.store.services.impl.CustomUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

	@Autowired
	private CustomUserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		//System.out.println(requestTokenHeader);
		String username = null;
		String jwttoken = null;
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwttoken = requestTokenHeader.substring(7);
			try {
				
				username = jwtTokenHelper.getUserNameFromToken(jwttoken);
				
			}catch(IllegalArgumentException ex) {
				logger.info("Illegal Argument while fetching the username !!");
			}catch(ExpiredJwtException ex) {
				logger.info("Given jwt token expired");
			}catch (MalformedJwtException e) {
				logger.warn("some change is done in token !! Invalid token :{}",e.getMessage());
				//throw new BadApiRequest("some change is done in token !! Invalid token");
			}catch (Exception e) {
				e.printStackTrace();
			}
        }else {
        	logger.info("Invaid Token , Not start with bearer string");
        	//throw new BadApiRequest("Invaid Token , Not start with bearer string");
        }
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenHelper.validateToken(jwttoken, userDetails)) {
            	//Token is validated
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
		else {
			logger.info("Token is not valid");
			//throw new BadApiRequest("Token is not valid");
        }
        filterChain.doFilter(request, response);
		
	}
	
	
}
