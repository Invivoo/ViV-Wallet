[![CircleCI](https://circleci.com/gh/maurelio1234/ViV-Wallet.svg?style=svg)](https://circleci.com/gh/maurelio1234/ViV-Wallet)

# ViV-Wallet

## Development server (app + api)

Run `mvn clean install` to build ViV-Wallet-app and move it to ViV-Wallet-api resources. Run it in sudo mode the first time to install the version of node used by the frontend-maven-plugin. 
Move to ViV-Wallet-api folder and run `mvn spring-boot:run` for a dev server. Navigate to `http://localhost:8080/`.
