# Utiliser l'image officielle de Debian comme base
FROM debian:latest

# Mettre à jour les paquets et installer les dépendances nécessaires
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    wget \
    git \
    && apt-get clean

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers du projet dans le conteneur
COPY . /app

# Compiler les fichiers Java avec l'encodage UTF-8
# Assurez-vous que les fichiers Java sont dans le bon répertoire
RUN javac -encoding UTF-8 *.java

# Exposer le port utilisé par le serveur
EXPOSE 9043

# Commande par défaut pour démarrer le serveur
# Assurez-vous d'utiliser le bon nom de package/classe
CMD ["java", "Server"]

