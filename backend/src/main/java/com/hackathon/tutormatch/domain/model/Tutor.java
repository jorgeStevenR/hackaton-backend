package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class Tutor {

    private final Long id;
    private final String nombre;
    private final Set<String> materias;
    private final Set<String> horarios;
    private final NivelExperiencia nivel;
    private final double calificacion;
    private final Modalidad modalidad;

    public Tutor(Long id, String nombre, Collection<String> materias, Collection<String> horarios,
                 NivelExperiencia nivel, double calificacion, Modalidad modalidad) {
        if (Textos.estaVacio(nombre)) {
            throw new DatosInvalidosException("El nombre del tutor es obligatorio");
        }
        if (calificacion < 1.0 || calificacion > 5.0) {
            throw new DatosInvalidosException("La calificacion debe estar entre 1 y 5");
        }
        if (nivel == null) {
            throw new DatosInvalidosException("El nivel de experiencia es obligatorio");
        }
        if (modalidad == null) {
            throw new DatosInvalidosException("La modalidad es obligatoria");
        }
        Set<String> materiasLimpias = Textos.limpiar(materias);
        if (materiasLimpias.isEmpty()) {
            throw new DatosInvalidosException("El tutor debe dominar al menos una materia");
        }
        this.id = id;
        this.nombre = nombre.trim();
        this.materias = materiasLimpias;
        this.horarios = Textos.horariosValidos(horarios);
        this.nivel = nivel;
        this.calificacion = calificacion;
        this.modalidad = modalidad;
    }

    /** Compara sin distinguir mayusculas ni tildes: "calculo" domina "Cálculo". */
    public boolean dominaMateria(String materia) {
        return materiaEquivalente(materia).isPresent();
    }

    /** Devuelve la materia tal como la registro el tutor, si la domina. */
    public Optional<String> materiaEquivalente(String materia) {
        String buscada = Textos.normalizar(materia);
        if (buscada.isEmpty()) {
            return Optional.empty();
        }
        return materias.stream()
                .filter(m -> Textos.normalizar(m).equals(buscada))
                .findFirst();
    }

    /** Horarios del tutor que coinciden con los pedidos, en el orden de la solicitud. */
    public Set<String> horariosEnComun(Set<String> solicitados) {
        Set<String> comunes = new LinkedHashSet<>();
        for (String horario : solicitados) {
            if (horarios.contains(horario)) {
                comunes.add(horario);
            }
        }
        return Collections.unmodifiableSet(comunes);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<String> getMaterias() {
        return materias;
    }

    public Set<String> getHorarios() {
        return horarios;
    }

    public NivelExperiencia getNivel() {
        return nivel;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }
}
