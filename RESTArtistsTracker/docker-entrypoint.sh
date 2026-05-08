#!/bin/sh

# Render deployment: construct proper JDBC URL and set Spring Boot overrides.
# Render auto-injects DATABASE_URL=postgresql://... (without jdbc: prefix).
# We must use SPRING_DATASOURCE_URL which takes priority over application.properties.

if [ -n "$DB_HOST" ]; then
  # Build from individual parts (set by render.yaml)
  export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_NAME}"
elif [ -n "$DATABASE_URL" ]; then
  # Fallback: prepend jdbc: to Render's auto-injected DATABASE_URL
  case "$DATABASE_URL" in
    jdbc:*) export SPRING_DATASOURCE_URL="$DATABASE_URL" ;;
    *)      export SPRING_DATASOURCE_URL="jdbc:$DATABASE_URL" ;;
  esac
fi

if [ -n "$SPRING_DATASOURCE_URL" ]; then
  export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
  export SPRING_JPA_HIBERNATE_DDL_AUTO="update"
  if [ -n "$DATABASE_USERNAME" ]; then
    export SPRING_DATASOURCE_USERNAME="$DATABASE_USERNAME"
  fi
  if [ -n "$DATABASE_PASSWORD" ]; then
    export SPRING_DATASOURCE_PASSWORD="$DATABASE_PASSWORD"
  fi
fi

exec java -jar app.war
