package org.example.service;

import org.example.model.Session;
import org.example.repository.SessionRepository;
import org.example.repository.SessionRepositoryHibernate;

import java.sql.Timestamp;

public class SessionService {

    SessionRepository sessionRepository = new SessionRepositoryHibernate();

    public Session get(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        return session;
    }

    public boolean isValid(Session session){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return session != null && session.getExpiresAt().after(now);

    }
}
