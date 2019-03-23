FROM openjdk:11 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/d/Java/Progetti/Piggus/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM openjdk:11
ENV ARTIFACT_NAME=piggus.jar
ENV APP_HOME=/usr/app/piggus/
WORKDIR $APP_HOME
COPY ./build/libs/$ARTIFACT_NAME $APP_HOME/$ARTIFACT_NAME
EXPOSE 8080
ENTRYPOINT ["java","-jar","piggus.jar"]
#
#FROM openjdk:11
#ENV ARTIFACT_NAME=piggus.jar
#ENV APP_HOME=/usr/app/piggus/
#WORKDIR $APP_HOME
#COPY ./build/libs/$ARTIFACT_NAME $ARTIFACT_NAME
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","piggus.jar"]