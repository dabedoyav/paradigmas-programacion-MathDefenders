# MathDefenders: Torre de Cálculo

Proyecto grupal del módulo **Paradigmas de Programación** (Institución Universitaria Politécnico Grancolombiano — Facultad de Ingeniería, Diseño e Innovación).

- **Tutor:** David Seligmann
- **Tecnologías:** Java (compilación con `--release 17`, verificado en JDK 26.0.1), Apache Maven (`3.9.16` + Maven Wrapper), JUnit Jupiter `5.14.4` y Javadoc.
- **Arquitectura:** Programación Orientada a Objetos (POO) con patrón **Modelo–Vista–Controlador (MVC)**. El paquete `com.mathdefenders.modelo` está completamente desacoplado de JavaFX.

---

## Repositorio del proyecto

El código fuente completo y su historial de cambios están disponibles en GitHub:

- **Enlace:** [https://github.com/dabedoyav/paradigmas-programacion-MathDefenders](https://github.com/dabedoyav/paradigmas-programacion-MathDefenders)

Para obtener una copia del repositorio:

```bash
git clone https://github.com/dabedoyav/paradigmas-programacion-MathDefenders.git
```

---

## Estado del proyecto por entregas

- **Entrega 1 (Semana 3):** Propuesta formal, reglas del juego, niveles, puntuación y mockups aprobados.
- **Entrega 2 (Semana 5 — Lista):** Modelo de negocio completo en `com.mathdefenders.modelo`, pruebas unitarias automatizadas en JUnit 5 (71 pruebas en 8 clases) y documentación HTML generada con Javadoc (incluyendo miembros privados).
- **Entrega 3 (Semanas 7 y 8 — Pendiente):** Interfaz gráfica completa en JavaFX, controladores, diagramas UML actualizados y presentación de sustentación.

---

## Reparto de actividades y clases del modelo (Entrega 2)

| Integrante | Clases y componentes asignados |
|---|---|
| **Diego Bedoya Velásquez** | `NivelDificultad`, `EstadoPartida`, `Jugador` + pruebas unitarias y Javadoc |
| **Diego Buitrago González** | `Torre`, `RetoMatematico` + pruebas unitarias y Javadoc |
| **Javier Buitrago Flórez** | `GeneradorNivel`, `NivelBasico`, `NivelIntermedio`, `NivelAvanzado`, `FabricaGeneradores` + pruebas unitarias y Javadoc |
| **Santiago Borda Gutiérrez** | `Partida`, `ResultadoRonda`, `ResumenPartida` + pruebas unitarias y Javadoc |
| **Yeimmy Bernal Tejedor** | `RegistroPuntaje`, `GestorPuntajes`, pruebas unitarias de ranking, configuración Maven (`pom.xml`, `mvnw`) y generación de Javadoc |

---

## Estructura del proyecto

```text
C:\paradigmas de programacion
├── pom.xml
├── mvnw / mvnw.cmd
├── .mvn/wrapper/
├── docs/
│   └── apidocs/                 # Documentación HTML Javadoc generada (index.html)
└── src/
    ├── main/java/com/mathdefenders/modelo/
    │   ├── package-info.java
    │   ├── EstadoPartida.java
    │   ├── FabricaGeneradores.java
    │   ├── GeneradorNivel.java
    │   ├── GestorPuntajes.java
    │   ├── Jugador.java
    │   ├── NivelAvanzado.java
    │   ├── NivelBasico.java
    │   ├── NivelDificultad.java
    │   ├── NivelIntermedio.java
    │   ├── Partida.java
    │   ├── RegistroPuntaje.java
    │   ├── ResultadoRonda.java
    │   ├── ResumenPartida.java
    │   ├── RetoMatematico.java
    │   └── Torre.java
    └── test/java/com/mathdefenders/modelo/
        ├── GeneradoresNivelTest.java
        ├── GestorPuntajesTest.java
        ├── NivelDificultadYJugadorTest.java
        ├── PartidaTest.java
        ├── RegistroPuntajeTest.java
        ├── ResultadoRondaYResumenPartidaTest.java
        ├── RetoMatematicoTest.java
        └── TorreTest.java
```

---

## Comandos de compilación, pruebas y Javadoc

Si tienes Maven instalado en tu equipo puedes usar `mvn`; si no lo tienes instalado, usa el **Maven Wrapper** incluido en el repositorio (`.\mvnw.cmd` en Windows PowerShell o `./mvnw` en Linux/macOS):

### Con Maven Wrapper (recomendado para todo el equipo)
```powershell
# Compilar y ejecutar todas las pruebas unitarias JUnit 5
.\mvnw.cmd clean test

# Generar la documentación HTML de Javadoc en docs/apidocs/index.html
.\mvnw.cmd javadoc:javadoc

# Ejecutar todo en un solo paso
.\mvnw.cmd clean test javadoc:javadoc
```

### Con Maven instalado globalmente
```powershell
mvn clean test
mvn javadoc:javadoc
```

### Ubicación del Javadoc para Entrega 2
Al ejecutar `javadoc:javadoc`, la documentación HTML completa (clases, enumeraciones, atributos, constructores y métodos) queda generada en:
- `docs/apidocs/index.html`
