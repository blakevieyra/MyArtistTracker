#!/bin/sh

# If DB_HOST is set (Render deployment), construct the proper JDBC URL
# and set SPRING_DATASOURCE_URL which Spring Boot uses as an override
if [ -n "$DB_HOST" ]; then
  export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_NAME}"
  export SPRING_DATASOURCE_USERNAME="${DATABASE_USERNAME}"
  export SPRING_DATASOURCE_PASSWORD="${DATABASE_PASSWORD}"
  export SPRING_DATASOURCE_DRIVER_CLASS_NAME="org.postgresql.Driver"
  export SPRING_JPA_HIBERNATE_DDL_AUTO="update"
  export SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.PostgreSQLDialect"
fi

exec java -jar app.war
