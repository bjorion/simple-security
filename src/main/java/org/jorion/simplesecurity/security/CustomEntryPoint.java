package org.jorion.simplesecurity.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Customize the response for a failed authentication
 */
@Slf4
@SuppressWarnings("unused")
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        log.info("Request URI [{}], Authentication Exception [{}]", request.getRequestURI(), authException.getMessage());
        response.addHeader("message", "Luke, I am your father!");
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
