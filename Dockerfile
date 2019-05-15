##################
### Base Image ###
##################
FROM maven:latest AS build

##############
### Labels ###
##############
LABEL maintainer="Felix Klauke <info@felix-klauke.de>"

COPY . .

#######################
### Run maven goals ###
#######################
RUN mvn clean package

############################
### Base Image - Runtime ###
############################
FROM openjdk:11 AS runtime

WORKDIR /opt/app

#####################
### Copy artifact ###
#####################
COPY --from=build server/target/scorpia-server.jar /opt/app/scorpia-server.jar

###################
### Environemnt ###
###################
ENV REVERSE_PROXY_TYPE=TCP
ENV REVERSE_PROXY_HOST=0.0.0.0
ENV REVERSE_PROXY_PORT=8085

ENTRYPOINT [ 'java', '-jar', 'scorpia-server.jar' ]
