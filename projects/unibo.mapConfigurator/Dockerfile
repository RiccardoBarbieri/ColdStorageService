FROM openjdk:11
EXPOSE 8015
VOLUME ["/data"]
ADD ./build/distributions/unibo.mapConfigurator-boot-2.3.tar /
WORKDIR unibo.mapConfigurator-boot-2.3/bin
CMD ["bash", "./unibo.mapConfigurator"]
