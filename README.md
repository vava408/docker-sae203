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
Zinner/
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