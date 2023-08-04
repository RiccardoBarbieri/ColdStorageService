## ------------------------------------------------
## ----- Dockerfile for unibo.mapConfigurator -----
## ------------------------------------------------

FROM openjdk:11
EXPOSE 8015
VOLUME /data
ADD ./build/distributions/unibo.mapConfigurator-boot-1.0.tar /
WORKDIR /unibo.mapConfigurator-boot-1.0/bin
CMD ["bash", "unibo.mapConfigurator"]

# Volume creation
# docker volume create map_volume
# 
# Image building
# docker build -t mapconfigurator:1.0 .
# 
# Inspect volume contents
# docker exec {CONTAINER ID}ls -la /data
