FROM openjdk:11-jre-stretch

WORKDIR /

ADD target/ViV-Wallet-api-0.0.1-SNAPSHOT.jar ViV-Wallet-api-0.0.1-SNAPSHOT.jar

ENV X4BAuthPublicKeyUrl="" \
    LynxApiUrl="" \
    ApiKey="" \
    SqlServerUrl="SQL_SERVER_URL" \
    SqlServerUsername="SQL_SERVER_USERNAME" \
    SqlServerPassword="SQL_SERVER_PASSWORD"

EXPOSE 8080

CMD java -jar ViV-Wallet-api-0.0.1-SNAPSHOT.jar --x4b.auth.publicKeyUrl=$X4BAuthPublicKeyUrl --lynx.vivApiUrl=$LynxApiUrl --api.key=$ApiKey --spring.datasource.url=$SqlServerUrl  --spring.datasource.username=$SqlServerUsername  --spring.datasource.password=$SqlServerPassword --spring.profiles.active=sqlServer