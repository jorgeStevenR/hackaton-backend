package com.hackathon.tutormatch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSolicitudRepository extends JpaRepository<SolicitudEntity, Long> {

    List<SolicitudEntity> findAllByOrderByFechaDescIdDesc();
}
