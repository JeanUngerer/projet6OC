# projet6OC - Pay My Buddy

Projet de formation Openclassroom

### Contexte
Problème à résoudre : transactions d’argent peu pratiques :
* les banques ont actuellement un processus long et peu pratique de configuration d'un
transfert d'argent ;
* les transferts bancaires demandent trop de données (numéro de compte, code SWIFT,
etc.) ;
* il est difficile de rembourser ou de transférer de l'argent à des amis ou de la famille. Il
est difficile de transférer de l'argent vers des comptes pour des achats.

### Solution :
* développer une application où les utilisateurs pourraient s'enregistrer facilement avec
une adresse e-mail ou un compte de réseaux sociaux;
* les utilisateurs peuvent ajouter des amis à leur réseau pour leur transférer de l'argent;
* passer par une conception simple pour rationaliser la procédure et éviter les soucis.


### Installation

##### Database
* MySql,
* Générée en code first
* Par défaut sur le port 3306, (modifier le port dans les fichiers application.yml et application-test.yml)
* Variables d'environement à setup :
  * DBUSER représente datasource.username
  * DBPASS représente datasource.password
* Les scripts sql de creation des tables ne sont pas nécessaire pour lancer le projet (générées par le code) mais se trouvent sous : paymybuddy/src/main/ressources/sql/DB.sql

##### SpringBoot backend
* à partir de la racine du projet : cd paymybuddy
* mvn install -DDBUSER=<datasource.username> -DDBPASS=<datasource.password>
* par défaut le serveur spring se lance sur le port 8090 (modifier dans application.yml)
* cd target
* java -jar paymybuddy-0.0.1-SNAPSHOT.jar
* A des fins de démonstration la database et réinitialisée à chaque lancement de l'application.

Deux utilisateurs sont générés avec les credentials suivants :
* usernameAdmin/password
* usernameUser/password

Les Admin ont accès à certains endpoint auxquels les autres utilisateur n'ont pas accès.


##### Angular Frontend
* à partir de la racine du projet : cd pay-my-buddy-front
* utilise angular/cli ~15.0.4
* npm install pour installer les package node
* npm start pour lancer le web development server.
* Home @ http://localhost:4200/home par défaut



### Schéma de base de données

![db_diagram](https://github.com/JeanUngerer/projet6OC/blob/front-init/Schemas/DBdiagram.png?raw=true)


### Diagramme de classe
![db_diagram](https://github.com/JeanUngerer/projet6OC/blob/front-init/Schemas/paymybuddy.png?raw=true)