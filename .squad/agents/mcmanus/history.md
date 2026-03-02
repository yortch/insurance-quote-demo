# McManus — History

## Project Context
- **Project:** Three Rivers Insurance — Insurance Quote Website
- **Frontend:** ReactJS
- **Backend:** Java
- **Infrastructure:** Azure with Terraform
- **CI/CD:** GitHub Actions
- **User:** Jorge Balderas

## Learnings

### CI Workflow Patterns (2025-07-15)
- **Path-based triggers**: Use `paths: ['frontend/**']` / `paths: ['backend/**']` to scope workflows to their directory, avoiding unnecessary CI runs.
- **Frontend caching**: `actions/setup-node@v4` has built-in `cache: 'npm'` support — point `cache-dependency-path` at the lockfile in the subdirectory.
- **Backend caching**: `actions/setup-java@v4` has built-in `cache: 'maven'` support — no manual `.m2` caching needed.
- **Maven flags**: Use `--batch-mode --no-transfer-progress` in CI to suppress interactive prompts and noisy download logs.
- **Test step**: Frontend package.json has no `test` script yet, so the frontend CI omits a test step. Add it when tests are introduced.
- **Timeouts**: 10 min for frontend (Node), 15 min for backend (Maven + checkstyle + spotbugs) are safe defaults.
- **Working directory**: Use `defaults.run.working-directory` at job level to avoid repeating `cd` in every step.
