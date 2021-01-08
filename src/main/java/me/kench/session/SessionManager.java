package me.kench.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private final Map<UUID, PlayerSession> sessions;

    public SessionManager() {
        sessions = new HashMap<>();
    }

    public Map<UUID, PlayerSession> getSessions() {
        return Collections.unmodifiableMap(sessions);
    }

    public void addSession(UUID uniqueId) {
        sessions.put(
                uniqueId,
                new PlayerSession(uniqueId)
        );
    }

    public PlayerSession getSession(UUID uniqueId) {
        return sessions.get(uniqueId);
    }

    public void removeSession(UUID uniqueId) {
        sessions.remove(uniqueId);
    }
}
