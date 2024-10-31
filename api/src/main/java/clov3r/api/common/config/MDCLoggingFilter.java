package clov3r.api.common.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCLoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    final UUID uuid = UUID.randomUUID();
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    StringBuilder params = new StringBuilder();
    request.getParameterNames().asIterator().forEachRemaining(name -> {
      params.append(name + "=" + request.getParameter(name) + "\n");
    });

    MDC.put("env", System.getProperty("spring.profiles.active"));
    MDC.put("uri", request.getRequestURI());
    MDC.put("method", request.getMethod());
    MDC.put("params", params.toString());
    MDC.put("auth", request.getHeader("Authorization"));
    MDC.put("request_id", uuid.toString());

    filterChain.doFilter(servletRequest, servletResponse);
    MDC.clear();
  }


}
