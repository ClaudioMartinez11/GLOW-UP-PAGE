# Glow Up

Página web de reservas para un local de manicura y peinados. Permite seleccionar el tipo de servicio, consultar meses y fechas disponibles, elegir un horario y confirmar una reserva con los datos de la clienta.

El proyecto está dividido en:

- `index.html` y `styles.css`: frontend responsive para PC y celular.
- `backend/`: API Spring Boot para guardar las reservas.
- PostgreSQL/Neon: almacenamiento persistente de las reservas.
- Google Sheets: documento compartido que se actualiza automáticamente con cada reserva.
- WhatsApp: mensaje preparado con los datos de la reserva.

## Servicios

Las agendas y precios se separan en dos servicios:

- **Servicios de uñas**
- **Tratamientos capilares**

Cada servicio mantiene su propia selección de fecha y horario. Las reservas se agregan a pestañas separadas en Google Sheets:

- `Servicios de uñas`
- `Tratamientos capilares`

## Requisitos

### Frontend

- Navegador moderno.
- Five Server o cualquier servidor web local.
- Netlify para publicar la página.

### Backend

- JDK 17 o posterior.
- Maven 3.9 o posterior.
- PostgreSQL 15+ o una base de datos Neon.
- Una cuenta de servicio de Google con acceso de edición a la hoja.

## Ejecutar el frontend localmente

Abrir la carpeta del proyecto con VS Code y ejecutar `index.html` usando Five Server.

Por defecto, el frontend busca el backend en:

```text
http://localhost:8080
```

Para producción se puede definir la URL pública del backend antes del script principal:

```html
<script>
    window.GLOW_UP_API_URL = "https://tu-backend-publicado.example.com";
</script>
```

## Ejecutar el backend

Desde la carpeta `backend`:

```powershell
mvn spring-boot:run
```

También se puede construir el archivo ejecutable:

```powershell
mvn clean package
java -jar target/glow-up-backend-0.0.1-SNAPSHOT.jar
```

La API queda disponible en:

```text
http://localhost:8080
```

## Variables de entorno

Configurar estas variables en el servidor donde se ejecute Spring Boot:

```text
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...
DATABASE_SSLMODE=require
CORS_ORIGINS=https://tu-sitio.netlify.app,http://localhost:5500
GOOGLE_SHEETS_SPREADSHEET_ID=...
GOOGLE_SERVICE_ACCOUNT_JSON={...}
```

`GOOGLE_SERVICE_ACCOUNT_JSON` debe contener el JSON de la cuenta de servicio. No subir ese JSON a GitHub.

## API

### Crear una reserva

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

Al crear la reserva, el backend:

1. La guarda en Neon PostgreSQL.
2. La agrega a la pestaña correspondiente de Google Sheets.
3. Permite que el frontend abra WhatsApp con el mensaje preparado.

### Estado del backend

```http
GET /api/reservations/health
```

### Descargar Excel

```http
GET /api/reservations/export.xlsx
```

Genera un archivo actualizado llamado:

```text
RESERVAS SEGUIMIENTO.xlsx
```

El archivo contiene dos hojas separadas por servicio. Se genera desde la base de datos cada vez que se solicita, por lo que no depende de una copia antigua guardada en el servidor.

## Despliegue

El flujo recomendado es:

```text
GitHub -> Netlify -> Backend Spring Boot -> Neon PostgreSQL
                                      -> Google Sheets
```

- GitHub guarda el código fuente.
- Netlify publica `index.html`, `styles.css` y los recursos del frontend.
- El backend Java debe ejecutarse en un servicio compatible con Spring Boot, como Render, Railway o Fly.io.
- Neon almacena las reservas.
- Google Sheets funciona como documento compartido para que la clienta pueda ver y editar el seguimiento.

## Seguridad

No subir al repositorio:

- Contraseñas de Neon.
- `GOOGLE_SERVICE_ACCOUNT_JSON`.
- Tokens o claves de APIs.
- Archivos `.env`.

Estas exclusiones están contempladas en `.gitignore`.
