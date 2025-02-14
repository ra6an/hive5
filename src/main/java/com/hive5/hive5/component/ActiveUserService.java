package com.hive5.hive5.component;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ActiveUserService {
    private final Map<UUID, Set<String>> activeUsers = new ConcurrentHashMap<>();

    public void addUser(UUID userId, String sessionId) {
        activeUsers.computeIfAbsent(userId, k -> new HashSet<>()).add(sessionId);
        System.out.println("Dodan novi user: " + userId.toString() + " --- i njegov sessionId je: " + activeUsers.get(userId));
    }

    public void removeUser(String sessionId) {
        activeUsers.forEach((userId, sessions) -> {
            Iterator<String> iterator = sessions.iterator();
            while (iterator.hasNext()) {
                String currentSessionId = iterator.next();
                if (currentSessionId.equals(sessionId)) {
                    iterator.remove(); // Sigurno uklanjanje iz liste
                    if (sessions.isEmpty()) {
                        activeUsers.remove(userId);
                    }
                    break;
                }
            }
        });
    }

    public boolean isUserOnline(UUID userId) {
        return activeUsers.containsKey(userId);
    }

    public Set<String> getUserSessionId(UUID userId) {
        return activeUsers.getOrDefault(userId, Collections.emptySet());
    }
}
