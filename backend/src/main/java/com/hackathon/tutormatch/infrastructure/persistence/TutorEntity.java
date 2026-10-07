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

@Entity
@Table(name = "tutores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    /** Separadas por comas: "Calculo,Fisica" */
    @Column(nullable = false)
    private String materias;

    /** Separados por comas: "LUN-08,MIE-10" */
    @Column(nullable = false)
    private String horarios;

    @Column(nullable = false)
    private int nivel;

    @Column(nullable = false)
    private double calificacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Modalidad modalidad;
}
