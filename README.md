# Le VIV Wallet

Table des matières :

- Introduction
- Workflow de travail
- À lire avant de commencer
- C&#39;est parti !

Introduction

Grow Together est un programme d&#39;entreprise qui a un double objectif : permettre aux consultants qui souhaitent s&#39;impliquer de développer et de faire valoir leur expertise, et faire évoluer le positionnement d&#39;Invivoo vers une offre à plus forte valeur ajoutée répondant aux besoins de nos clients. Comme son nom l&#39;indique, il s&#39;agit bien de grandir tous ensemble.

Pour cela, le programme repose principalement sur la mise en place d&#39;un parcours d&#39;évolution cadré pour les consultants désireux de développer des compétences tout en s&#39;impliquant dans le développement d&#39;Invivoo. Ce parcours propose différents niveaux d&#39;implication allant de Consultant Senior à Partner en passant par Manager ou Manager Senior. S&#39;impliquer dans un de ces rôles est récompensé par une réelle plus-value que ce soit en termes d&#39;évolution de carrière, de responsabilités confiées ou d&#39;incentive financier sous la forme d&#39;une monnaie Virtuel, le VIV, la Virtual Invivoo Value en fonction de la contribution.

Le tableau des VIV, disponible [ici](https://www.invivoo.com/grow-together/), permet de connaitre le nombre de VIV correspondant à notre

Le VIV-Wallet est une application qui s&#39;adresses à différents types d&#39;utilisateurs, ou persona Il va permettre :

- à chaque consultant de suivre sa cagnotte de VIV
- à chaque membre des équipes RH, Recrutement, Sales et Communication, d&#39;attribuer ces VIV
- aux membres de l&#39;équipe Admin, de convertir ses VIV sur la paie du collaborateur.

Un projet fil rouge

Le VIV Wallet est un projet fil rouge dans le cadre du Programme Grow Together. Il est open-source.

Voici le workflow à suivre pour pouvoir y contribuer :

- S&#39;affecter sur un des ticket avec l&#39;acteur du manager de l&#39;expertise Java [https://github.com/orgs/Invivoo/projects](https://github.com/orgs/Invivoo/projects). Ne pas hésiter à demander plus d&#39;info dans la Team dédiée au projet fil rouge.
- Cloner le projet
- Tirer une branche viv/feature|hotfix/Id-du-ticket
- Un fois le développement fait, faire une pull request et demander via Teams à un consultant senior de la valider en mettant le lien

## À lire avant de commencer

- [Spécifications - User Stories by persona](https://invivoo.sharepoint.com/:w:/s/Managersd&#39;Expertise/EeWUsfMrHMdOgpooDoYeWCYBsczKABL8gNalyRRjTDFb_g?e=K6owx1)
- [Spécifications - Java](https://invivoo.sharepoint.com/:w:/s/Managersd&#39;Expertise/ES5hwrPj9fdFj1g8w58NLvcBn2_JeJ9HslSdzdUwafGFpQ?e=gLidmS) :
  - Domain Model
  - Api
  - Architecture Decisions (packaging, Database, API, Security, CI, Versioning, Projets)
- Spécifications - Front (TBD)

Tickets by persona

Chaque ticket ci-dessous est composé de la manière suivante :

-
  - _User Story_
  - Test à implémenter
  - Pistes d&#39;implémentation
  
## Development server (app + api)

Run `mvn clean install` to build ViV-Wallet-app and move it to ViV-Wallet-api resources. Run it in sudo mode the first time to install the version of node used by the frontend-maven-plugin. 
Move to ViV-Wallet-api folder and run `mvn spring-boot:run` for a dev server. Navigate to `http://localhost:8080/`.
