package clov3r.oneit_server.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Parameter;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Parameter[] parameters = handlerMethod.getMethod().getParameters();

            boolean authParameterFound = false;
            for (Parameter parameter : parameters) {
                if (parameter.isAnnotationPresent(Auth.class) && parameter.getType().equals(Long.class)) {
                    authParameterFound = true;
                    break;
                }
            }

            if (!authParameterFound) {
                return true;
            }

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "HttpServletRequest request");
                return false;
            }
            token = token.substring(7); // "Bearer " 제거
            if (!jwtTokenProvider.validateToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return false;
            }

            request.setAttribute("userId", jwtTokenProvider.getUserIdFromToken(token));
        }
        return true;
    }
}
