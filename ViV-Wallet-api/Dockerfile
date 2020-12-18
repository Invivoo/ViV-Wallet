FROM openjdk:11-jre-stretch

WORKDIR /

ADD target/ViV-Wallet-api-0.0.1-SNAPSHOT.jar ViV-Wallet-api-0.0.1-SNAPSHOT.jar

ENV X4BAuthPublicKeyUrl="" \
    SqlServerUrl="" \
    SqlServerUsername="" \
    SqlServerPassword=""

EXPOSE 8080

CMD java -jar ViV-Wallet-api-0.0.1-SNAPSHOT.jar --x4b.auth.publicKeyUrl=$X4BAuthPublicKeyUrl --spring.datasource.url=$SqlServerUrl  --spring.datasource.username=$SqlServerUsername  --spring.datasource.password=$SqlServerPassword