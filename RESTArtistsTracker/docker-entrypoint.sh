#!/bin/sh

# If DB_HOST is set (Render deployment), construct the JDBC URL
if [ -n "$DB_HOST" ]; then
  export DATABASE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_NAME}"
fi

exec java -jar app.war
