# Cobalt Forge

Laboratorio para construir una **PKI X.509 local con OpenSSL 3**: una Root CA propia, una CA intermedia y certificados de servidor para servir sitios de prueba con **HTTPS confiable**. El primer stack es Spring Boot (`web-java/`); otros vendrán después.

Pensado para **macOS, Windows y Linux**. Hoy los laboratorios cubren por completo macOS y parcialmente Windows; las secciones de Linux se irán agregando.

> **Solo para aprendizaje.** Las CAs de este laboratorio no deben usarse en producción.

## Requisitos

| | macOS | Windows | Linux |
|---|---|---|---|
| **OpenSSL 3** | `brew install openssl@3` (el `openssl` del sistema es LibreSSL) | Incluido en [Git for Windows](https://git-scm.com/download/win) (Git Bash) | Suele venir instalado; si no, `sudo apt install openssl` o equivalente |
| **Java 21** | [SDKMAN](https://sdkman.io) | ZIP de [Temurin](https://adoptium.net) | [SDKMAN](https://sdkman.io) o el gestor de paquetes |

Comprueba con `openssl version` (debe decir `OpenSSL 3.x`) y `java -version`. Maven no hace falta: el sitio usa el wrapper `mvnw`.

### Cobertura de los laboratorios por sistema

| Laboratorio | macOS | Windows | Linux |
|---|---|---|---|
| 00 · Java 21 | ✔ | ✔ | pendiente |
| 01–04 · CAs, certificado y `.p12` (OpenSSL) | ✔ | pendiente | pendiente |
| 05 · `hosts` | ✔ | ✔ | pendiente |
| 06 · HTTPS en Spring Boot | ✔ | ✔ | pendiente |
| 07 · Confiar en la Root CA | ✔ | ✔ | pendiente |

## Estructura

```text
.
├── cert/      # Salida de todos los comandos de OpenSSL (no se versiona, salvo su README)
├── docs/      # Laboratorios paso a paso, numerados
└── web-java/  # Sitio de prueba "Cobalt Forge": Spring Boot (Java 21) + Pico CSS
    ├── pom.xml, mvnw, mvnw.cmd
    └── src/main/resources/
        ├── templates/index.html
        └── static/  # css/ (pico.min.css + theme.css con la paleta), fonts/, img/
```

## Laboratorios

Se siguen en orden; los comandos de OpenSSL se ejecutan desde la raíz del proyecto y escriben en `cert/`.

0. [Instalar Java 21](docs/00-install-java.md) — macOS (SDKMAN) y Windows (ZIP de Temurin en `%USERPROFILE%\Programs\Java`); levantar el sitio.
1. [Root CA](docs/01-root-ca-x509-openssl-macos.md) — clave RSA 4096 cifrada y certificado raíz autofirmado.
2. [Intermediate CA](docs/02-intermediate-ca-x509-openssl-macos.md) — CSR firmada por la Root CA y cadena de confianza.
3. [Certificado de servidor](docs/03-server-cert-x509-openssl-macos.md) — `lab.cobalt-forge.test`, firmado por la Intermediate CA.
4. [Keystore PKCS#12](docs/04-pkcs12-keystore.md) — clave + certificado + cadena en un `.p12`.
5. [Dominio local](docs/05-hosts-dominio-local.md) — `lab.cobalt-forge.test` → `127.0.0.1` en `hosts`.
6. [HTTPS en Spring Boot](docs/06-https-spring-boot.md) — perfil `https` en el puerto 8443; advertencia de CA desconocida.
7. [Confiar en la Root CA](docs/07-confiar-root-ca.md) — almacén del sistema y candado en el navegador.

## Levantar el sitio web

```bash
cd web-java
./mvnw spring-boot:run          # Windows: .\mvnw.cmd spring-boot:run
```

Abrir <http://localhost:8080>. La página muestra si la conexión llega por HTTP o HTTPS.

Con HTTPS, tras completar los laboratorios 03 a 06 (requiere `application-https.yml` y la contraseña del `.p12`):

```bash
cd web-java
read -s "COBALT_FORGE_P12_PASSWORD?Contraseña del .p12: "; echo
export COBALT_FORGE_P12_PASSWORD
./mvnw spring-boot:run -Dspring-boot.run.profiles=https
```

Abrir <https://lab.cobalt-forge.test:8443>. En Windows, ver el [laboratorio 06](docs/06-https-spring-boot.md).

Los estilos son [Pico CSS](https://picocss.com) más `web-java/src/main/resources/static/css/theme.css`, donde se define la paleta: para cambiar colores, edita sus variables.

## Seguridad

`cert/` contiene claves privadas de CA y el keystore del servidor. `.gitignore` excluye su contenido y cualquier `*.pem`, `*.key`, `*.crt`, `*.csr`, `*.p12`, etc. en el repositorio. La contraseña del `.p12` se pasa por variable de entorno, nunca en `application-https.yml`; verifica con `git status` antes de cada commit.
