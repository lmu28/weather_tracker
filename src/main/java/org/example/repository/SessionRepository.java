package org.example.repository;

import org.example.model.Session;

import java.util.Optional;

public interface SessionRepository {

     Optional<Session> findById(String id);
}
