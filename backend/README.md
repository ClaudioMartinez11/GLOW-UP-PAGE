# Glow Up Backend

Backend inicial para reservas de manicura y tratamientos capilares.

## Requisitos

- Java 17+
- Maven 3.9+
- PostgreSQL 15+ o Neon
- Una hoja de Google Sheets compartida con la cuenta de servicio

## Ejecutar

Configurar `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` y, si corresponde, `CORS_ORIGINS`, y ejecutar:

```powershell
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Para Neon, usar la URL PostgreSQL provista por Neon y mantener `DATABASE_SSLMODE=require`.

## Google Sheets

Crear una hoja con dos pestañas exactamente llamadas `Servicios de uñas` y `Tratamientos capilares`. Compartirla como editora con el correo de la cuenta de servicio y configurar:

- `GOOGLE_SHEETS_SPREADSHEET_ID`: el ID que aparece entre `/d/` y `/edit` en la URL de Google Sheets.
- `GOOGLE_SERVICE_ACCOUNT_JSON`: el contenido JSON de la cuenta de servicio.

Cada reserva guardada en Neon se agrega automáticamente a la pestaña correspondiente. La clienta puede trabajar sobre esa misma hoja compartida y ver los nuevos turnos sin descargar copias.

Para Netlify, definir una variable antes del script de la página:

```html
<script>window.GLOW_UP_API_URL = "https://tu-backend.example.com";</script>
```

Netlify aloja el frontend; Neon aloja la base de datos. El backend Spring Boot debe ejecutarse en un servicio Java compatible, como Render, Railway, Fly.io o similar.

## Endpoints

- `POST /api/reservations`: crea una reserva.
- `GET /api/reservations/export.xlsx`: genera y descarga `RESERVAS SEGUIMIENTO.xlsx` actualizado desde Neon, con dos hojas separadas por servicio.

El Excel no se guarda como un archivo fijo en el servidor. Cada descarga lo vuelve a generar desde la base de datos, por lo que incluye las reservas realizadas hasta ese momento y evita problemas de almacenamiento temporal en el hosting.
