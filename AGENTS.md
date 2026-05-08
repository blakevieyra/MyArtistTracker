# AGENTS.md

## Cursor Cloud specific instructions

### Architecture

- **Frontend**: Angular 17 SPA at `ngArtistsTracker/` (port 4200)
- **Backend**: Spring Boot 3.2.2 REST API at `RESTArtistsTracker/` (port 8083)
- **JPA Library**: Shared entities at `JPAArtistsTracker/` (included via Gradle `includeFlat`)
- **Database**: MySQL `artiststrackerdb` on localhost:3306 (user: `artistsuser` / password: `artistsuser`)

### Starting Services

1. **MySQL**: `sudo mysqld --user=mysql --datadir=/var/lib/mysql --pid-file=/var/run/mysqld/mysqld.pid --socket=/var/run/mysqld/mysqld.sock &`
   - Seed data: `sudo mysql -u root < /workspace/DB/artiststracker.sql`
   - Grant: `sudo mysql -u root -e "GRANT ALL PRIVILEGES ON artiststrackerdb.* TO 'artistsuser'@'localhost'; FLUSH PRIVILEGES;"`
   - The `blake` user password in the DB must be a BCrypt hash of `blake` for authentication to work.
2. **Backend**: `cd RESTArtistsTracker && ./gradlew bootRun` (requires MySQL running first)
3. **Frontend**: `cd ngArtistsTracker && npx ng serve --host 0.0.0.0`

### Authentication

- The app uses HTTP Basic Auth backed by a `user` table in MySQL.
- Default user: `blake` / `blake` (password stored BCrypt-encoded).
- The `/register` endpoint creates new users with BCrypt-encoded passwords automatically.
- The `GET /api/artists` endpoint returns only artists linked to the logged-in user via `user_has_artist` join table (favorites system).

### Build Commands

- **Backend build**: `cd RESTArtistsTracker && ./gradlew build -x test`
- **Backend tests**: `cd RESTArtistsTracker && ./gradlew :test` (Spring Boot context load test passes)
- **Frontend build**: `cd ngArtistsTracker && npx ng build`
- **Frontend tests**: `cd ngArtistsTracker && npx ng test` (Karma/Jasmine)

### Known Issues

- `JPAArtistsTracker` standalone JPA tests fail because `persistence.xml` doesn't list the `User` entity class. Use `./gradlew :test` (not `./gradlew test`) from `RESTArtistsTracker/` to skip those.
- No ESLint is configured for the Angular frontend (`ng lint` not available without adding `@angular-eslint/schematics`).
- The `POST /api/artists` endpoint has a NullPointerException when creating new artists (pre-existing bug: `Artist.getUsers()` returns null for new entities).

### Gotchas

- The Gradle multi-project uses `includeFlat` in `RESTArtistsTracker/settings.gradle`, meaning `JPAArtistsTracker` must be a sibling directory (which it is in this repo layout).
- Angular dev config (`environment.development.ts`) points to `http://localhost:8083/`. The production config uses a relative path for deployment.
- MySQL must be started manually in cloud environments (service manager may not work in containers). Use the `mysqld` command above.
