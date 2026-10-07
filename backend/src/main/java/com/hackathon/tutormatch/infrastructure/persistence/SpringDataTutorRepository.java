package com.hackathon.tutormatch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTutorRepository extends JpaRepository<TutorEntity, Long> {
}
