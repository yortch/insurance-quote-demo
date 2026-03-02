# Fenster — History

## Project Context
- **Project:** Three Rivers Insurance — Insurance Quote Website
- **Frontend:** ReactJS
- **Backend:** Java
- **Infrastructure:** Azure with Terraform
- **CI/CD:** GitHub Actions
- **User:** Jorge Balderas

## Learnings
- Backend lives at `backend/` with Spring Boot 3.4.1, Java 17, Maven build
- Package root: `com.threeriverinsurance` with sub-packages: controller, service, model, repository, config
- Health endpoint: `GET /api/health` → `{"status":"UP"}`
- Test profile uses H2 in-memory DB; production targets PostgreSQL
- Checkstyle config at `backend/checkstyle.xml` (custom Google-ish rules); run `mvn checkstyle:check`
- SpotBugs configured in pom.xml; run `mvn spotbugs:check`
- Build + test: `mvn clean verify` from `backend/`
- `application.yml` uses Spring multi-document format for profile-specific config (test profile section separated by `---`)
- Checkstyle `FileTabCharacter` must be a module, not a property on `Checker`
- **Never use hardcoded default credentials in `application.yml`** — use `${ENV_VAR}` without defaults for `DB_PASSWORD` and `DB_USERNAME`. GitGuardian flags default credentials as leaked secrets, blocking CI.
