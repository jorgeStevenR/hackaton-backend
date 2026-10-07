package com.hackathon.tutormatch.web.mapper;

import com.hackathon.tutormatch.domain.model.DetalleCriterio;
import com.hackathon.tutormatch.domain.model.NivelExperiencia;
import com.hackathon.tutormatch.domain.model.ResultadoMatch;
import com.hackathon.tutormatch.domain.model.Sesion;
import com.hackathon.tutormatch.domain.model.Solicitud;
import com.hackathon.tutormatch.domain.model.SolicitudRegistrada;
import com.hackathon.tutormatch.domain.model.Tutor;
import com.hackathon.tutormatch.domain.model.Usuario;
import com.hackathon.tutormatch.web.dto.AuthResponse;
import com.hackathon.tutormatch.web.dto.DetalleCriterioResponse;
import com.hackathon.tutormatch.web.dto.MatchResponse;
import com.hackathon.tutormatch.web.dto.SolicitudRequest;
import com.hackathon.tutormatch.web.dto.SolicitudResponse;
import com.hackathon.tutormatch.web.dto.TutorRequest;
import com.hackathon.tutormatch.web.dto.TutorResponse;
import com.hackathon.tutormatch.web.dto.UsuarioResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/** Traduce entre los DTO HTTP y el modelo de dominio. */
@Component
public class WebMapper {

    private static final String SEPARADOR = ",";

    public Tutor aTutor(TutorRequest request) {
        return new Tutor(
                null,
                request.nombre(),
                separar(request.materias()),
                separar(request.horarios()),
                NivelExperiencia.desdeValor(request.nivel()),
                request.calificacion(),
                request.modalidad());
    }

    public TutorResponse aTutorResponse(Tutor tutor) {
        return new TutorResponse(
                tutor.getId(),
                tutor.getNombre(),
                String.join(SEPARADOR, tutor.getMaterias()),
                String.join(SEPARADOR, tutor.getHorarios()),
                tutor.getNivel().valor(),
                tutor.getCalificacion(),
                tutor.getModalidad());
    }

    public Solicitud aSolicitud(SolicitudRequest request) {
        return new Solicitud(
                request.nombreEstudiante(),
                request.materia(),
                new LinkedHashSet<>(request.horarios()),
                request.modalidad());
    }

    public MatchResponse aMatchResponse(ResultadoMatch resultado) {
        return new MatchResponse(
                resultado.tutor().getId(),
                resultado.tutor().getNombre(),
                resultado.score(),
                resultado.justificacion(),
                List.copyOf(resultado.horariosCoincidentes()),
                resultado.recomendado(),
                resultado.disponible(),
                resultado.desglose().stream().map(this::aDetalleResponse).toList());
    }

    public SolicitudResponse aSolicitudResponse(SolicitudRegistrada registro) {
        Solicitud s = registro.solicitud();
        return new SolicitudResponse(
                registro.id(),
                s.nombreEstudiante(),
                s.materia(),
                List.copyOf(s.horarios()),
                s.modalidad(),
                registro.fecha(),
                registro.asignada(),
                registro.tutorAsignadoId(),
                registro.nombreTutorAsignado(),
                registro.score(),
                registro.justificacion(),
                registro.candidatosEvaluados());
    }

    public UsuarioResponse aUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(String.valueOf(usuario.getId()), usuario.getNombre(), usuario.getEmail());
    }

    public AuthResponse aAuthResponse(Sesion sesion) {
        return new AuthResponse(aUsuarioResponse(sesion.usuario()), sesion.token());
    }

    private DetalleCriterioResponse aDetalleResponse(DetalleCriterio detalle) {
        return new DetalleCriterioResponse(
                detalle.nombreCriterio(),
                detalle.peso(),
                detalle.puntaje(),
                detalle.aporte(),
                detalle.explicacion());
    }

    private static List<String> separar(String valor) {
        return Arrays.stream(valor.split(SEPARADOR)).map(String::trim).toList();
    }
}
