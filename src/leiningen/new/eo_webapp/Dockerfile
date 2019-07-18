FROM clojure AS build-env

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app
RUN mv "$(lein uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar

FROM java:8-alpine
MAINTAINER Wes Richardet <wes@tetriscodes.com>

COPY --from=build-env /usr/src/app/app-standalone.jar /{{name}}/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/{{name}}/app.jar"]
