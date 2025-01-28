package org.lt.project.security.filter;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;



@Component
public class XssFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }

    private static class XssRequestWrapper extends HttpServletRequestWrapper {
        public XssRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = stripXSS(values[i]);
            }
            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            return stripXSS(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return stripXSS(value);
        }

        private String stripXSS(String value) {
            if (value == null) {
                return null;
            }
            value = value.replaceAll("", "");
            // HTML ve JavaScript karakterlerini encode et
            value = Jsoup.clean(value, Safelist.none());
            // SQL injection karakterlerini temizle
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
            value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
            value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
            value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
            return value;
        }
    }
} 