FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY JPAArtistsTracker /app/JPAArtistsTracker
COPY RESTArtistsTracker /app/RESTArtistsTracker

WORKDIR /app/RESTArtistsTracker
RUN chmod +x gradlew && ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/RESTArtistsTracker/build/libs/*.war app.war
COPY RESTArtistsTracker/docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh
EXPOSE 8083
ENTRYPOINT ["/app/docker-entrypoint.sh"]
