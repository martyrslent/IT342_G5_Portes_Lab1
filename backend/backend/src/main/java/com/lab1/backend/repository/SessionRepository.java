package com.lab1.backend.repository;

import com.lab1.backend.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<StudySession, Long> {
    // JpaRepository gives you .findAll(), .save(), and .findById() for free!
}