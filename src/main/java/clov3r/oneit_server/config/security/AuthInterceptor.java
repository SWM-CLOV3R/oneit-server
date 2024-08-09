package clov3r.oneit_server.config.security;

import clov3r.oneit_server.exception.AuthException;
import clov3r.oneit_server.response.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        if (handler instanceof HandlerMethod) {
            System.out.println("Request URL: " + request.getRequestURL());
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Parameter[] parameters = handlerMethod.getMethod().getParameters();

            boolean authParameterFound = false;
            boolean authRequired = false;
            for (Parameter parameter : parameters) {
                System.out.println("parameter = " + parameter);
                if (parameter.isAnnotationPresent(Auth.class) && parameter.getType().equals(Long.class)) {
                    authParameterFound = true;
                    Annotation[] auth = parameter.getAnnotations();
                    for (Annotation annotation : auth) {
                        if (annotation instanceof Auth authAnnotation) {
                            if (authAnnotation.required()) {
                                authRequired = true;
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            if (!authParameterFound) {
                return true;
            } else if (!authRequired) {
                return true;
            }

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                throw new AuthException(BaseResponseStatus.NO_AUTHORIZATION_HEADER);
            }
            token = token.substring(7); // "Bearer " 제거
            if (!jwtTokenProvider.validateToken(token)) {
                throw new AuthException(BaseResponseStatus.INVALID_TOKEN);
            }

            request.setAttribute("userIdx", jwtTokenProvider.getUserIdFromToken(token));
        }
        return true;
    }
}
