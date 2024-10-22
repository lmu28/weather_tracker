package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Session;
import org.example.model.User;
import org.example.repository.SessionRepository;
import org.example.repository.SessionRepositoryHibernate;

import javax.swing.*;
import java.rmi.server.UID;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@RequiredArgsConstructor
public class SessionService {

    public static final int HOURS = 24;
    private final SessionRepository sessionRepository;



    public Session get(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        return session;
    }

    public boolean isValid(Session session){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return session != null && session.getExpiresAt().after(now);

    }

    public Session save(User user) {
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(Duration.ofHours(HOURS)));
        String uuid = UUID.randomUUID().toString();
        Session session = new Session(uuid, user,expiresAt);
        sessionRepository.save(session);
        return session;
    }

    public boolean deleteById(String id){
        return sessionRepository.deleteById(id) > 0;
    }


}
