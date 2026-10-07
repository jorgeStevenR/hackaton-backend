package com.hackathon.tutormatch.infrastructure.persistence;

import com.hackathon.tutormatch.domain.model.Modalidad;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_estudiante", nullable = false)
    private String nombreEstudiante;

    @Column(nullable = false)
    private String materia;

    /** Separados por comas: "LUN-08,MIE-10" */
    @Column(nullable = false)
    private String horarios;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Modalidad modalidad;

    @Column(nullable = false)
    private Instant fecha;

    /** Null si ningun tutor quedo asignado. Se guarda el nombre para conservar el historial. */
    @Column(name = "tutor_asignado_id")
    private Long tutorAsignadoId;

    @Column(name = "nombre_tutor_asignado")
    private String nombreTutorAsignado;

    private Double score;

    @Column(length = 1000)
    private String justificacion;

    @Column(name = "candidatos_evaluados", nullable = false)
    private int candidatosEvaluados;
}
