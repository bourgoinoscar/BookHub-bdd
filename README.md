Lien Swagger : http://localhost:8080/swagger-ui/index.html#/


# BookHub - API Backend

BookHub est une application Spring Boot de gestion de bibliothèque et de catalogue de livres. L'API permet de gérer le cycle de vie complet des emprunts, des réservations de livres, des avis d'utilisateurs ainsi que le contrôle d'accès basé sur des rôles spécifiques (**LECTEUR**, **BIBLIOTHECAIRE**, **ADMIN**).

## Fonctionnalités principales

### Gestion du Catalogue (Livres)
*   **Accès public :** Consultation de la liste complète, recherche multicritères (par titre, auteur, catégorie, ISBN) et affichage des détails d'un livre.
*   **Gestion (Bibliothécaire) :** Ajout, modification et suppression d'ouvrages.

### Emprunts & Retours
*   **Lecteurs :** Effectuer un emprunt (gestion automatique des stocks et de la date de retour prévu) et consulter son historique personnel.
*   **Bibliothécaires :** Valider le retour effectif d'un livre (remise en stock automatique), lister les emprunts en retard et suivre l'activité globale.

### Réservations
*   **Lecteurs :** Suivre l'état de ses propres réservations.
*   **Bibliothécaires :** Créer une réservation pour un utilisateur, annuler ou valider une réservation, et gérer la file d'attente d'un ouvrage spécifique.

### Avis & Notes (Revues)
*   **Lecteurs :** Publier ou modifier un avis (note + commentaire) sur un livre.
*   **Public :** Consulter la liste des avis globale ou filtrée par livre.
*   **Modération (Bibliothécaire / Admin) :** Suppression d'un avis inapproprié.

### Sécurité & Utilisateurs
*   Authentification via des **Tokens JWT** (JSON Web Token).
*   Inscription publique (rôle par défaut : `LECTEUR`).
*   Gestion des profils (L'utilisateur modifie le sien, l'**ADMIN** a les pleins pouvoirs).
*   Gestion complète des rôles applicatifs exclusive à l'**ADMIN**.

---

## Stack Technique

*   **Framework principal :** Spring Boot 3.2.5
*   **Langage :** Java 21
*   **Sécurité :** Spring Security & JJWT (JSON Web Token)
*   **Accès aux données :** Spring Data JPA
*   **Bases de données supportées :**
    *   Microsoft SQL Server (Runtime)
    *   H2 Database (Base en mémoire pour les tests/développement)
*   **Documentation :** Springdoc OpenAPI (Swagger UI) v2.5.0
*   **Gestionnaire de dépendances :** Maven

---

## Prérequis

Pour exécuter ce projet localement, vous devez avoir installé :
*   **Java 21** ou supérieur
*   **Maven 3.8+**
*   (Optionnel) Une instance de **MS SQL Server** si vous n'utilisez pas la base H2 par défaut.

---

## Installation et Démarrage

Cloner le projet
git clone https://github//votre-compte/bookhub-backend.git
cd bookhub-backend


Compiler et lancer l'application :
./mvnw spring-boot:run

Documentation de l'API & Endpoints
Une fois l'application démarrée, la documentation interactive Swagger UI est accessible pour tester l'ensemble des routes (y compris l'envoi du Token JWT en Header) :

http://localhost:8080/swagger-ui/index.html