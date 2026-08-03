package com.crm.auth.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    private static class SessionDetails {
        private final String token;
        private long lastActivity;

        public SessionDetails(String token, long lastActivity) {
            this.token = token;
            this.lastActivity = lastActivity;
        }

        public String getToken() {
            return token;
        }

        public long getLastActivity() {
            return lastActivity;
        }

        public void setLastActivity(long lastActivity) {
            this.lastActivity = lastActivity;
        }
    }

    private final Map<String, SessionDetails> activeSessions = new ConcurrentHashMap<>();
    private final long timeoutMs;

    public SessionManager(@Value("${app.session.timeout-ms:900000}") long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public void registerSession(String username, String token) {
        activeSessions.put(username, new SessionDetails(token, System.currentTimeMillis()));
    }

    public boolean isUserLoggedIn(String username) {
        SessionDetails session = activeSessions.get(username);
        if (session == null) {
            return false;
        }
        boolean isExpired = (System.currentTimeMillis() - session.getLastActivity()) >= timeoutMs;
        if (isExpired) {
            activeSessions.remove(username);
            return false;
        }
        return true;
    }

    public boolean isValidSession(String username, String token) {
        SessionDetails session = activeSessions.get(username);
        if (session == null) {
            registerSession(username, token);
            return true;
        }
        
        if (session.getToken().equals(token)) {
            boolean isExpired = (System.currentTimeMillis() - session.getLastActivity()) >= timeoutMs;
            if (isExpired) {
                activeSessions.remove(username);
                return false;
            }
            return true;
        }
        
        return false;
    }

    public void updateActivity(String username, String token) {
        SessionDetails session = activeSessions.get(username);
        if (session != null && session.getToken().equals(token)) {
            session.setLastActivity(System.currentTimeMillis());
        }
    }

    public void invalidateSession(String username) {
        if (username != null) {
            activeSessions.remove(username);
        }
    }
}
