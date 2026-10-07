package com.hackathon.tutormatch.web.exception;

import com.hackathon.tutormatch.domain.exception.CredencialesInvalidasException;
import com.hackathon.tutormatch.domain.exception.DomainException;
import com.hackathon.tutormatch.domain.exception.EmailYaRegistradoException;
import com.hackathon.tutormatch.domain.exception.SesionExpiradaException;
import com.hackathon.tutormatch.domain.exception.TutorNoEncontradoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;

/** Respuestas de error en formato ProblemDetail (RFC 7807). Nunca expone stack traces. */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validacion(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of("campo", e.getField(),
                        "mensaje", e.getDefaultMessage() == null ? "Valor invalido" : e.getDefaultMessage()))
                .toList();
        // "message" se muestra tal cual en el frontend: se unen los mensajes de cada campo
        String mensaje = errores.stream().map(e -> e.get("mensaje")).distinct()
                .reduce((a, b) -> a + ". " + b).orElse("Uno o mas campos no son validos");
        ProblemDetail problema = problema(HttpStatus.BAD_REQUEST, "Datos invalidos", mensaje);
        problema.setProperty("errores", errores);
        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail cuerpoIlegible(HttpMessageNotReadableException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Cuerpo invalido",
                "El cuerpo de la petición no es un JSON válido o tiene valores no permitidos");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return problema(HttpStatus.BAD_REQUEST, "Parametro invalido",
                "El parametro '" + ex.getName() + "' tiene un valor no valido");
    }

    @ExceptionHandler(TutorNoEncontradoException.class)
    public ProblemDetail noEncontrado(TutorNoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "Tutor no encontrado", ex.getMessage());
    }

    @ExceptionHandler({CredencialesInvalidasException.class, SesionExpiradaException.class})
    public ProblemDetail noAutorizado(DomainException ex) {
        return problema(HttpStatus.UNAUTHORIZED, "No autorizado", ex.getMessage());
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ProblemDetail conflicto(EmailYaRegistradoException ex) {
        return problema(HttpStatus.CONFLICT, "Conflicto", ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail reglaDeNegocio(DomainException ex) {
        return problema(HttpStatus.UNPROCESSABLE_ENTITY, "Regla de negocio violada", ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail rutaNoEncontrada(NoResourceFoundException ex) {
        return problema(HttpStatus.NOT_FOUND, "Ruta no encontrada", "No existe la ruta /" + ex.getResourcePath());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail metodoNoPermitido(HttpRequestMethodNotSupportedException ex) {
        return problema(HttpStatus.METHOD_NOT_ALLOWED, "Metodo no permitido",
                "El metodo " + ex.getMethod() + " no esta soportado en esta ruta");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail inesperado(Exception ex) {
        log.error("Error inesperado", ex);
        return problema(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno",
                "Ocurrio un error inesperado. Intenta de nuevo mas tarde.");
    }

    private static ProblemDetail problema(HttpStatus status, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalle);
        problema.setTitle(titulo);
        // Contrato con el frontend: todos los errores traen { "message": "..." }
        problema.setProperty("message", detalle);
        return problema;
    }
}
