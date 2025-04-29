# Zinder

Zinder est une application de rencontre interactive qui permet aux utilisateurs de découvrir des profils, d'envoyer des likes, de discuter avec leurs matchs et de créer des connexions. Ce projet est une simulation complète d'une application client-serveur avec une interface graphique conviviale.

---

## Fonctionnalités

- **Création de compte** : Les utilisateurs peuvent créer un compte avec une photo de profil, un nom, une description et un mot de passe.
- **Connexion** : Authentification sécurisée pour accéder à l'application.
- **Swipe de profils** : Découverte de profils avec la possibilité de liker ou de passer.
- **Matchs** : Notification lorsqu'un like est réciproque.
- **Chat** : Messagerie instantanée avec les profils correspondants.
- **Serveur centralisé** : Gestion des utilisateurs, des likes et des messages.

---

## Architecture

### Côté Client
- **Interface utilisateur** : Créée avec Swing pour une expérience graphique interactive.
- **Gestion des profils** : Affichage des profils disponibles et gestion des interactions (like, passer).
- **Messagerie** : Envoi et réception de messages en temps réel.

### Côté Serveur
- **Gestion des connexions** : Serveur multi-clients utilisant des threads pour gérer plusieurs utilisateurs simultanément.
- **Stockage des données** : Conservation des profils, des likes et des messages.
- **Notifications** : Envoi des notifications de matchs et des nouveaux messages aux utilisateurs concernés.

---

## Structure du Projet

```plaintext
Zinder/
├──                # Interface de chat pour discuter avec les matchs
├──              # Gestion de la connexion client au serveur
├──       # Gestion des interactions serveur-client
├──           # Interface de connexion utilisateur
├──          # Contrôleur central pour gérer les interactions
├──         # Interface pour créer un nouveau compte
├──      # Interface pour découvrir et liker des profils
├──             # Modèle pour les messages échangés
├──              # Modèle pour les profils utilisateurs
├──              # Serveur central pour gérer les données et les connexions
└──                # Documentation du projet
````

---
# Explication
Commencez par vous connecter à Docker avec :  
``` ssh identifiant@di-docker ```

( cette commande stipule que vous êtes connecté au réseau de l'IUT )

Puis cloner ce référentiel dans docker: 
``` git clone https://github.com/vava408/docker-sae203.git ```

Puis crée une image une images du DockerFile : 
```docker build -t zinder 
```

Puis lancer le docker avec la commande : 
```docker run --name zinder-server -d -p 9043:9043 zinder```

docker run permet de lancer le docker avec l'image que l'on creer précédemment, puis --name, 
permet l'initialisation d'un nom pour notre docker.
```-d``` permet l'éécution en arrière plan, puis ```-p``` permet de choisir le port
( dans notre cas, le port 9043 ) 

Ensuite, assurez-vous que le conteneur est bien en fonctionnement avec la commande :  
```
docker ps
```

Le résultat attendu est :  
```plaintext
002b46c927d3   zinder                "java Server"            57 minutes ago       Up 57 minutes       80/tcp, 0.0.0.0:9043->9043/tcp                   zinder-server
```

Pour finir, lancez le client sur votre machine avec les commandes suivantes :  
```
javac *.java
```
puis :  
```
java client
```

Enfin, arrêtez le conteneur avec la commande suivante (remplacez `b8f8f406b03c` par l'ID affiché par `docker ps`) :  
```
docker stop b8f8f406b03c
```

Si vous souhaitez supprimer le conteneur, utilisez la commande suivante :  
```
docker rm b8f8f406b03c
```
 