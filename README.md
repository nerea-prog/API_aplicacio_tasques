# Projecte Tasks - API REST

Aquest projecte és una API REST desenvolupada amb **Spring Boot** que gestiona una taula de tasques (tasks) per a l'aplicació Android. Permet CRUD complet, pujada de CSV i imatges, i genera logs de cada operació.

## Enllaç al vídeo demostratiu

[TASKBUDDY.mp4](video/TASKBUDDY.mp4)

> El vídeo ha de mostrar l'execució del fitxer `.jar` i la prova de tots els endpoints.

## Endpoints disponibles

### Crear i llegir tasques

- `POST /taskbuddy/api/task` — Crear una nova tasca.
- `GET /taskbuddy/api/task` — Obtenir totes les tasques.
- `GET /taskbuddy/api/task/{task_id}` — Obtenir una tasca per ID.
- `POST /taskbuddy/api/task/upload-csv` — Afegir múltiples tasques a través d'un fitxer CSV.

### Actualitzar tasques

- `PUT /taskbuddy/api/task/{task_id}` — Actualitzar completament una tasca per ID.
- `POST /taskbuddy/api/task/{task_id}/image` — Afegir o actualitzar la imatge d'una tasca per ID.

### Eliminar tasques

- `DELETE /taskbuddy/api/task/{task_id}` — Esborrar una tasca per ID.
- `DELETE /taskbuddy/api/task/all` — Esborrar totes les tasques.

## Logs

- La API genera logs per a totes les funcionalitats dins la carpeta `logs/` en l’arrel del projecte.

