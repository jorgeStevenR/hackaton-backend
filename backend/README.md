# TutorMatch - Backend

Matching de tutorias entre pares. Un estudiante pide ayuda en una materia y TutorMatch compara su
solicitud con todos los tutores, calcula un **score de afinidad (0-100)** con criterios ponderados y
recomienda el mejor tutor explicando el porque.

Spring Boot 3.5 · Java 17 · Maven · H2 (o PostgreSQL/Supabase) · springdoc OpenAPI

## Arquitectura

Arquitectura limpia en cuatro capas. **Regla de dependencia:** las capas externas dependen de las
internas, nunca al reves. El dominio es Java puro (sin Spring, JPA ni Lombok).

```
        ┌──────────────┐     ┌────────────────┐     ┌──────────────────────────────┐
HTTP ──►│     web      │────►│  application   │────►│            domain            │
        │ controllers  │     │   use cases    │     │ model · matching · port      │
        │ DTOs, mapper │     │   services     │     │ (Java puro, sin frameworks)  │
        └──────────────┘     └────────────────┘     └──────────────▲───────────────┘
                                                                   │ implementa
                                                    ┌──────────────┴───────────────┐
                                                    │        infrastructure        │──► BD
                                                    │ JPA adapter · config · CORS  │
                                                    └──────────────────────────────┘

        web ──► application ──► domain ◄── infrastructure
```

| Capa | Paquete | Contenido |
|------|---------|-----------|
| domain | `domain.model` | `Tutor`, `Solicitud`, `ResultadoMatch`, `DetalleCriterio`, `Modalidad`, `NivelExperiencia` |
| | `domain.matching` | Patron **Strategy**: `CriterioAfinidad` + `CriterioHorario`, `CriterioNivel`, `CriterioCalificacion`, `CriterioModalidad`, y `MotorAfinidad` |
| | `domain.port` | `TutorRepositoryPort` (puerto de salida) |
| application | `application.usecase` / `.service` | `RegistrarTutor`, `ListarTutores`, `BuscarTutorIdeal` |
| infrastructure | `infrastructure.persistence` | `TutorEntity`, `TutorPersistenceAdapter` (implementa el puerto), mapper |
| | `infrastructure.config` | Pesos (`matching.pesos`), ensamblado del motor, CORS, OpenAPI |
| web | `web.*` | Controladores, DTOs (records), `WebMapper`, errores `ProblemDetail` |

### Score de afinidad

`score = Σ(puntaje_criterio × peso) × 100`, redondeado a 1 decimal. Pesos en `application.properties`:

| Criterio | Peso | Puntaje (0-1) |
|----------|------|---------------|
| Horario | 0.40 | horarios coincidentes / solicitados |
| Nivel | 0.30 | nivel / 3 |
| Calificacion | 0.20 | calificacion / 5 |
| Modalidad | 0.10 | 1 si es compatible (o el tutor es AMBAS) |

Primero se descartan los tutores que no dominan la materia (sin distinguir mayusculas ni tildes). En
empate gana el de mayor nivel. Agregar un criterio = una clase nueva que implemente `CriterioAfinidad`.

## API

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/tutores` | Lista de tutores |
| GET | `/api/tutores/{id}` | Un tutor (404 si no existe) |
| POST | `/api/tutores` | Registra un tutor (201) |
| POST | `/api/match` | Ranking de tutores para una solicitud |
| GET | `/actuator/health` | Health check |

### Autenticacion (JWT)

| Metodo | Ruta | Body / Header | Respuesta |
|--------|------|---------------|-----------|
| POST | `/api/auth/register` | `{ name, email, password }` | 201 `{ user, token }` |
| POST | `/api/auth/login` | `{ email, password, rememberMe? }` | 200 `{ user, token }` |
| GET | `/api/auth/me` | `Authorization: Bearer <token>` | 200 `{ user }` |
| POST | `/api/auth/logout` | - | 204 (el frontend borra el token) |

`user = { id (string), name, email }`. Contrasenas cifradas con BCrypt; nunca se devuelven. Email en minusculas y unico.
Token de 1 dia, o 30 dias con `rememberMe: true`. Todos los errores traen `{ "message": "..." }`:
401 credenciales invalidas o sesion expirada, 409 email repetido, 400/422 datos invalidos.

Horarios con formato `DIA-HORA`: dias `LUN..VIE`, horas `08`, `10`, `14`, `16`.

```bash
curl -X POST http://localhost:8080/api/match -H "Content-Type: application/json" \
  -d '{"nombreEstudiante":"Pedro","materia":"Calculo","horarios":["LUN-08","MIE-10","VIE-14"],"modalidad":"VIRTUAL"}'
```

Errores en formato ProblemDetail (RFC 7807): 400 validacion, 404 no encontrado, 422 regla de negocio, 500 generico.

**Swagger UI:** http://localhost:8080/swagger-ui.html

## Correr local

Desde la carpeta `backend/`:

```bash
mvn test               # tests del dominio
mvn spring-boot:run    # levanta la app en http://localhost:8080
```

Por defecto usa **H2 en memoria** (se reinicia en cada arranque). Hibernate (JPA) crea la tabla `tutores` a partir de `TutorEntity` y `DatosIniciales` carga 10 tutores si esta vacia.
Consola H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:tutormatch`, usuario `sa`, sin contrasena).

### Con Supabase (datos persistentes)

1. Copia `.env.example` a `.env` y pon `DB_PASSWORD` (`.env` esta en `.gitignore`).
2. Arranca con el perfil `supabase`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=supabase
```

## Docker

```bash
docker build -t tutormatch-backend .
docker run -p 8080:8080 tutormatch-backend
# Con Supabase:
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=supabase --env-file .env tutormatch-backend
```

## Desplegar en Render

1. Sube el repo a GitHub.
2. En Render: **New > Web Service** y conecta el repo.
3. **Root Directory:** `backend` · **Language:** `Docker`.
4. **Environment Variables:**
   - `JWT_SECRET` = texto aleatorio de 32+ caracteres (firma los tokens; **obligatorio en produccion**)
   - `FRONTEND_URL` = (opcional) otras URLs del frontend separadas por comas.
     `http://localhost:5173` y `https://hackaton-beta-seis.vercel.app` ya estan permitidas.
   - (Opcional, para Supabase) `SPRING_PROFILES_ACTIVE` = `supabase` y `DB_PASSWORD` = contrasena
5. **Health Check Path:** `/actuator/health`
6. **Deploy.** Render inyecta `PORT` y la app lo usa automaticamente.
