[![CircleCI](https://circleci.com/gh/Invivoo/ViV-Wallet.svg?style=svg)](https://circleci.com/gh/Invivoo/ViV-Wallet)

# Le VIV Wallet

Table des matières :

-   [Introduction](#introduction)
-   [Contribuer au projet fil rouge](#contribuer-au-projet-fil-rouge)
-   [À lire avant de commencer](#à-lire-avant-de-commencer)
-   [Let's code](#lets-code)

## Introduction

Grow Together est un programme d'entreprise qui a un double objectif : permettre aux consultants qui souhaitent s&#39;impliquer de développer et de faire valoir leur expertise, et faire évoluer le positionnement d&#39;Invivoo vers une offre à plus forte valeur ajoutée répondant aux besoins de nos clients. Comme son nom l&#39;indique, il s&#39;agit bien de grandir tous ensemble.

Pour cela, le programme repose principalement sur la mise en place d&#39;un parcours d&#39;évolution cadré pour les consultants désireux de développer des compétences tout en s&#39;impliquant dans le développement d&#39;Invivoo. Ce parcours propose différents niveaux d&#39;implication allant de Consultant Senior à Manager puis Manager Senior. S&#39;impliquer dans un de ces rôles est récompensé par une réelle plus-value que ce soit en termes d&#39;évolution de carrière, de responsabilités confiées ou d&#39;incentive financier sous la forme d&#39;une monnaie Virtuel, le VIV, la Virtual Invivoo Value en fonction de la contribution.

Le VIV-Wallet est une application qui s&#39;adresses à différents types d&#39;utilisateurs, ou persona. Il va permettre :

-   à chaque consultant de suivre sa cagnotte de VIV,
-   aux membres du programme de suivre leurs objectifs,
-   aux membres de l&#39;équipe Admin, de convertir ses VIV sur la paie du collaborateur.

## À lire avant de commencer

Quelques guidelines ont été données pour assurer la cohérence du projet ainsi que son modèle de données

-   Dans ce projet, tu pourras retrouver des tickets Java et des tickets Front-End. Retrouve [ici l'intégralité des user stories par persona qui ont permis de les générer](https://invivoo.sharepoint.com/:w:/s/Managersd'Expertise/EeWUsfMrHMdOgpooDoYeWCYBsczKABL8gNalyRRjTDFb_g?e=ebTLgZ).
-   Pour donner de la cohérence au projet, le modèle de données, les apis disponible et la structure du projet Java ont été défini en amont [dans les spécifications techniques](https://invivoo.sharepoint.com/:w:/s/Managersd'Expertise/ES5hwrPj9fdFj1g8w58NLvcBn2_JeJ9HslSdzdUwafGFpQ?e=KSssb3). Si il y a besoin de les faire évoluer, merci de créer une conversation à ce sujet dans [dans le canal du projet](https://teams.microsoft.com/l/channel/19%3a4ad8360f9c4e4016a157bfe3f7ca968a%40thread.skype/VIV-Wallet?groupId=d5a0827f-1103-4e19-89f3-d85e7caeb167&tenantId=6d13640c-ba8f-4480-a2a0-0093bacdb7c1).

## Contribuer au projet fil rouge

Le VIV Wallet est un projet fil rouge dans le cadre du Programme Grow Together. Il est open-source.

**/!\ Attention /!\ en tant que projet Open-Source, il est accessible par tout le monde. Il faut donc apporter un soin particulier à ne pas insérer des informations confidentielles dans le code**

Si tu veux contribuer, voilà la marche à suivre :

-   Lis bien l'intégralité de ce README !
-   Choisis le ticket que tu veux développer dans [la version en cours de développement](https://github.com/Invivoo/ViV-Wallet/projects/1),
-   Indique sur Teams [dans le canal du projet](https://teams.microsoft.com/l/channel/19%3a4ad8360f9c4e4016a157bfe3f7ca968a%40thread.skype/VIV-Wallet?groupId=d5a0827f-1103-4e19-89f3-d85e7caeb167&tenantId=6d13640c-ba8f-4480-a2a0-0093bacdb7c1) le ticket sur lequel tu souhaites t'impliquer en créant une conversation avec le lien du ticket,
-   Attend le go d'un manager d'expertise (il vérifiera que le ticket est faisable et n'est pas déjà traité par quelqu'un d'autre),
-   Déplace le ticket dans In Progress en t'affectant sur le ticket [ici](https://github.com/Invivoo/ViV-Wallet/projects/1). N'hésite pas à demander plus d'info dans la conversation liée au ticket sur Teams.
-   Crée une branche intitulée _feature|hotfix/id-du-ticket_ à partir de la branche develop (exemple pour la feature #12, la branche serait feature/12),
-   Pour chaque ticket, ajoute des Tests en t'inspirant de la spec et des tests existants !
-   Une fois le développement fait, crée une pull request et demande via Teams à un manager d'expertise de la valider dans la conversation liée au ticket sur Teams,
-   _Et Voilà_, le nombre de VIV associé au ticket sera débloqué une fois que l'ensemble de la version sera livrée.

/!\ Attention /!\ dans certains cas, les VIVs ne seront pas crédités :

-   si tu es en intercontrat,
-   si les tickets sont développés pendant tes heures au siège (de 8h à 12h et 14h à 18h)

## Let's code

### Comment créer sa clef SSH pour travailler en local avec son compte GitHub

Normalement tout est décrit [dans l'aide GitHub sur le sujet](https://help.github.com/en/enterprise/2.17/user/authenticating-to-github/connecting-to-github-with-ssh). Vos clés SSH se trouvent dans c:\users\<vous>\.ssh sous Windows ou dans /.ssh sous Unix.

Idéalement, il faudrait d'abord installer [gitbash](https://www.atlassian.com/git/tutorials/git-bash), afin ensuite de créer sa clé SSH.

Suivez [ce guide](https://git-scm.com/book/fr/v2/Git-sur-le-serveur-G%C3%A9n%C3%A9ration-des-cl%C3%A9s-publiques-SSH) ou celui de la page d'aide de GitHub pour créer la clef, puis copier son contenu dans votre compte GitHub pour la déclarer (voir [là](https://help.github.com/en/enterprise/2.17/user/authenticating-to-github/adding-a-new-ssh-key-to-your-github-account)).

Ensuite le git clone la prendra en compte.

### Pour ceux qui veulent travailler en isolation dans un container avec VSCode Remote Development

[VSCode Remote Development](https://code.visualstudio.com/docs/remote/remote-overview) permet de travailler dans un environnement isolé (container docker par exemple). Pas besoin d'installer de jdk ou nodejs sur sa machine, l'environnement standard de dev (défini dans le répertoire .devcontainer) est créé automatiquement lors du premier lancement.

Pour installer l'extension VSCode pour le remote development, suivre la procédure suivante: [https://code.visualstudio.com/docs/remote/containers](https://code.visualstudio.com/docs/remote/containers).

### Pour travailler sur le backend avec le front et le back avec une base in memory

Run `mvn clean install` from the project root to build ViV-Wallet-app and move it to ViV-Wallet-api resources.
Run it in sudo mode the first time to install the version of node used by the frontend-maven-plugin.cd
Move to `ViV-Wallet-api/` folder and run `mvn spring-boot:run` for a dev server.
As we are using spring dev tools, you just have to compile to see your changes. If the hot swap failed, just restart the api.

Then move to the `viv-wallet-app/` folder, run `npm run serve:localbackend` to start the front end.

The app should be running at `localhost:4200`.

### Pour travailler sur le backend avec la base d'uat en mode non authentifié

Connect to invivoo local network using the wifi at the office or a vpn
Run `mvn clean install`
Run `mvn spring-boot:run` with uat and noAuth spring profiles activated
Use node v16, move to the `viv-wallet-app/` folder and run `npm run serve:localbackend`

### Pour travailler sur le backend avec une base en local en mode non authentifié

Launch sqlServer docker
with `docker run --name sql -P -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=/EU22MQwyx' -e 'MSSQL_PID=Express' -p 1433:1433 -d mcr.microsoft.com/mssql/server:2017-CU14-ubuntu`
Ask for a dump of the db and import it with Azure Data Studio
Run `mvn clean install` and create a database named viv_wallet
Run `mvn spring-boot:run` with sqlServer and noAuth spring profiles activated

### Pour travailler sur le frontend

In the `viv-wallet-app/` folder, run `npm run mock-server` to start a mock backend server. Then start the front end by typing `npm run serve`.
