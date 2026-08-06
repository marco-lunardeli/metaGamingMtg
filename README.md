# metaGamingMtg - Spring Boot backend (Gradle)

Este branch contém o scaffold inicial do backend em Spring Boot (Gradle), com PostgreSQL via Docker.

Como rodar (requer Docker e Docker Compose):

1. Subir serviços:

   docker-compose up --build

2. Verificar o endpoint de exemplo:

   curl http://localhost:8080/api/hello

Se preferir rodar localmente sem Docker, instale o Gradle e execute:

   gradle bootRun

Variáveis padrão (definidas no docker-compose):
- POSTGRES_USER: postgres
- POSTGRES_PASSWORD: postgres
- POSTGRES_DB: metagaming

Observações:
- O backend escuta na porta 8080.
- O datasource é configurado via variáveis de ambiente (application.yml).
