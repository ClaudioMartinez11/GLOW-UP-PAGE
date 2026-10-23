# Glow Up

Página web de reservas para un local de manicura y peinados. Permite seleccionar un servicio, consultar fechas y horarios disponibles y confirmar una reserva con los datos de la clienta.

## Características

- Diseño responsive para PC y celular.
- Selección de servicio.
- Selección de fecha y horario.
- Creación de reservas.
- Prevención de reservas duplicadas.
- Almacenamiento persistente en PostgreSQL.
- Sincronización con Google Sheets.
- Exportación de reservas a Excel.
- Integración con WhatsApp.
- API REST desarrollada con Spring Boot.

## Arquitectura

El proyecto está dividido en:

```text
GLOW-UP-PAGE/
├── index.html
├── styles.css
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
├── .gitignore
└── README.md
```

### Tecnologías

**Frontend**
- HTML5
- CSS3
- JavaScript
- Netlify

**Backend**
- Java
- Spring Boot
- Maven
- PostgreSQL

**Servicios externos**
- Neon PostgreSQL
- Google Sheets API
- WhatsApp

## Servicios

La agenda se divide en dos categorías:

- Servicios de uñas
- Tratamientos capilares

Cada categoría mantiene su propia selección de fechas y horarios.

Las reservas pueden sincronizarse con pestañas separadas de Google Sheets:

```text
Servicios de uñas
Tratamientos capilares
```

## Requisitos

### Frontend

- Navegador moderno.
- VS Code + Five Server para desarrollo local.
- Netlify para despliegue.

### Backend

- JDK 17 o superior.
- Maven 3.9 o superior.
- PostgreSQL 15+ o Neon PostgreSQL.
- Cuenta de servicio de Google con acceso a la hoja correspondiente.

## Ejecutar el frontend localmente

Abrir el proyecto con VS Code y ejecutar `index.html` mediante Five Server o cualquier servidor web local.

Por defecto, el frontend utiliza:

```text
http://localhost:8080
```

Para producción se puede configurar la URL pública del backend:

```html
<script>
    window.GLOW_UP_API_URL = "https://tu-backend.example.com";
</script>
```

## Ejecutar el backend

Desde la carpeta `backend`:

```bash
mvn spring-boot:run
```

También se puede generar el archivo ejecutable:

```bash
mvn clean package
java -jar target/glow-up-backend-0.0.1-SNAPSHOT.jar
```

La API estará disponible en:

```text
http://localhost:8080
```

## Variables de entorno

El backend utiliza variables de entorno para evitar almacenar credenciales directamente en el código fuente.

Ejemplo:

```text
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...
DATABASE_SSLMODE=require

CORS_ORIGINS=https://tu-sitio.netlify.app,http://localhost:5500

GOOGLE_SHEETS_SPREADSHEET_ID=...
GOOGLE_SERVICE_ACCOUNT_JSON={...}
```

### Importante

Las siguientes credenciales **no deben subirse a GitHub**:

- Contraseña de PostgreSQL.
- `GOOGLE_SERVICE_ACCOUNT_JSON`.
- Tokens.
- API keys.
- Archivos `.env`.
- Cualquier otra credencial privada.

Estas variables deben configurarse directamente en el servicio donde se ejecute el backend.

## API

### Crear una reserva

Endpoint público utilizado por el formulario:

```http
POST /api/reservations
Content-Type: application/json
```

Ejemplo:

```json
{
  "serviceType": "NAILS",
  "serviceName": "Servicios de uñas",
  "date": "2026-09-14",
  "time": "9:00 am",
  "clientName": "Ana Perez",
  "clientPhone": "1122334455"
}
```

Al crear una reserva, el backend:

1. Valida los datos recibidos.
2. Comprueba la disponibilidad.
3. Guarda la reserva en PostgreSQL.
4. Sincroniza la información con Google Sheets.
5. Permite preparar el mensaje de confirmación mediante WhatsApp.

## Estado del backend

Para comprobar que el backend está funcionando:

```http
GET /api/reservations/health
```

Este endpoint solamente devuelve el estado del servicio y no debe exponer información de clientes.

## Exportación de reservas

```http
GET /api/reservations/export.xlsx
```

Genera un archivo Excel actualizado con las reservas almacenadas en la base de datos.

El archivo contiene información de las reservas y, por lo tanto, **este endpoint debe estar protegido y destinado exclusivamente al administrador**.

El archivo se genera desde PostgreSQL cada vez que se solicita y no depende de una copia antigua almacenada en el servidor.

## Seguridad

La aplicación debe mantener separadas las funciones públicas y administrativas.

### Endpoints públicos

```text
POST /api/reservations
GET  /api/reservations/health
```

Estos endpoints pueden ser utilizados por el sitio web.

### Endpoints administrativos

```text
GET /api/reservations/export.xlsx
```

La exportación contiene información de clientes y debe requerir autenticación/autorización de administrador.

Las reservas y sus datos personales no deben estar disponibles públicamente.

### Protección de datos

Los datos de las clientas deben almacenarse y utilizarse únicamente para las funciones necesarias del sistema de reservas.

Google Sheets debe mantenerse con acceso restringido. No se recomienda publicar una hoja que contenga nombres, teléfonos, fechas u otros datos personales.

## CORS

El backend permite configurar los orígenes autorizados mediante:

```text
CORS_ORIGINS
```

En producción se recomienda permitir únicamente el dominio real del frontend.

Ejemplo:

```text
CORS_ORIGINS=https://tu-sitio.netlify.app
```

Evitar permitir cualquier origen si no es necesario.

## Despliegue

Arquitectura recomendada:

```text
GitHub
   │
   ├── Frontend
   │      ↓
   │   Netlify
   │
   └── Backend
          ↓
      Spring Boot
          │
          ├── PostgreSQL / Neon
          │
          └── Google Sheets
```

El frontend puede publicarse mediante Netlify.

El backend puede ejecutarse en un servicio compatible con Spring Boot.

PostgreSQL/Neon se utiliza como almacenamiento principal de las reservas.

Google Sheets puede utilizarse como herramienta privada de seguimiento administrativo.

## Recomendaciones para producción

Antes de utilizar el sistema con clientes reales se recomienda:

- Utilizar HTTPS.
- Proteger los endpoints administrativos.
- Validar los datos también en el backend.
- Aplicar límites de solicitudes para evitar spam.
- Mantener CORS restringido al dominio del frontend.
- No exponer nombres ni teléfonos mediante endpoints públicos.
- Mantener privada la hoja de Google Sheets.
- Configurar las credenciales mediante variables de entorno.
- Realizar copias de seguridad de la base de datos.
- No almacenar secretos en el repositorio.

## Flujo de una reserva

```text
Cliente
   │
   ↓
Página web
   │
   ↓
POST /api/reservations
   │
   ↓
Backend Spring Boot
   │
   ├── Validación
   │
   ├── Comprobación de disponibilidad
   │
   ├── PostgreSQL
   │
   └── Google Sheets
```

Los datos administrativos y la exportación de reservas deben permanecer protegidos y no formar parte de la información pública del sitio.

## Estado del proyecto

Proyecto en desarrollo para la gestión de reservas de un local de manicura y peinados.

La aplicación está preparada para ejecutarse en un entorno de producción una vez configuradas correctamente las variables de entorno, la autenticación administrativa, las políticas de CORS y las medidas de protección de datos.
