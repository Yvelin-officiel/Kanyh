# Kanyh - Backend API

API REST pour la gestion des quêtes et aventuriers dans l'univers Kanyh.

## Table des matières

- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Lancement](#lancement)
- [Architecture](#architecture)
- [API Endpoints - Quêtes](#api-endpoints---quêtes)
- [Tests](#tests)
- [Documentation API](#documentation-api)

---

## Technologies

- **Java 25 LTS**
- **Spring Boot 3.5.6**
- **Spring Data JPA** avec Hibernate 6.6.29
- **H2 Database 2.4.240** (base de données en mémoire/fichier pour le développement)
- **Maven** - Gestion des dépendances
- **Lombok 1.18.42** - Réduction du code boilerplate
- **SpringDoc OpenAPI 2.8.13** - Documentation Swagger
- **JUnit 5.12.2** - Tests unitaires
- **Mockito 5.17.0** - Framework de mocking
- **AssertJ 3.27.4** - Assertions fluides pour les tests

---

## Prérequis

- **Java 25** ou supérieur
- **Maven 3.8+**
- **Git**

---

## Installation

Clonez le repository et naviguez vers le dossier backend :

```bash
git clone <repository-url>
cd Kanyh/back
```

Installez les dépendances Maven :

```bash
mvn clean install
```

---

## Configuration

### Profils disponibles

Le projet utilise des profils Spring pour différents environnements :

- **dev** (par défaut) : Base de données H2 sur fichier

### Configuration du profil `dev`

Fichier : `src/main/resources/application-dev.properties`

```properties
spring.datasource.url=jdbc:h2:file:./data/kanyhdb;AUTO_SERVER=TRUE
spring.datasource.username=root
spring.datasource.password=root
```

La base de données H2 est stockée dans le dossier `./data/kanyhdb`.

### Schéma de base de données

Le schéma est automatiquement créé/mis à jour au démarrage grâce à :

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## Lancement

### Démarrer l'application

```bash
mvn spring-boot:run
```

L'application sera accessible sur : **http://localhost:8080**

### Accéder à Swagger UI

Une fois l'application démarrée, accédez à la documentation interactive :

**http://localhost:8080/swagger-ui/index.html**

> Note : La racine `/` redirige automatiquement vers Swagger UI.

---

## Architecture

Le projet suit une architecture en couches (Layered Architecture) :

```
com.data.kanyh
├── controller/          # Contrôleurs REST
├── service/             # Logique métier
├── repository/          # Accès aux données (JPA)
├── mapper/              # Conversions Entity ↔ DTO
├── model/               # Entités JPA
├── dto/                 # Data Transfer Objects
└── exception/           # Exceptions personnalisées
```

### Flux de données

```
Client → Controller → Service → Repository → Database
                ↓         ↓
              Mapper    Mapper
                ↓         ↓
              DTO    ←  Entity
```

---

## API Endpoints - Quêtes

### Base URL : `/quetes`

| Méthode | Endpoint | Description | Corps de la requête |
|---------|----------|-------------|---------------------|
| `GET` | `/quetes` | Récupère toutes les quêtes | - |
| `GET` | `/quetes/{id}` | Récupère une quête par son ID | - |
| `POST` | `/quetes` | Crée une nouvelle quête | `QueteInputDTO` |
| `PUT` | `/quetes/{id}` | Met à jour une quête existante | `QueteInputDTO` |
| `DELETE` | `/quetes/{id}` | Supprime une quête | - |

---

### Modèles de données

#### QueteInputDTO (Requête)

```json
{
  "nom": "Sauver la princesse",
  "description": "Une quête héroïque",
  "prime": 1000.0,
  "dureeEstimee": 5,
  "datePeremption": "2025-12-31",
  "statut": "NOUVELLE"
}
```

**Validations :**
- `nom` : Obligatoire, non vide
- `description` : Obligatoire, non vide
- `prime` : Obligatoire
- `dureeEstimee` : Obligatoire (en jours)
- `datePeremption` : Obligatoire (format ISO 8601 : `YYYY-MM-DD`)
- `statut` : Obligatoire (ignoré lors de la création, toujours défini à `NOUVELLE`)

#### QueteDTO (Réponse)

```json
{
  "id": 1,
  "nom": "Sauver la princesse",
  "description": "Une quête héroïque",
  "prime": 1000.0,
  "dureeEstimee": 5,
  "datePeremption": "2025-12-31",
  "experienceGagnee": 500,
  "statut": "NOUVELLE",
  "commanditaireId": 10,
  "equipeId": 20
}
```

#### Statuts de quête

```java
public enum StatutQuete {
    NOUVELLE,      // Quête nouvellement créée
    EN_COURS,      // Quête en cours d'exécution
    TERMINEE,      // Quête terminée avec succès
    REJETEE        // Quête rejetée ou échouée
}
```

---

### Exemples d'utilisation

#### 1. Créer une nouvelle quête

**Requête :**

```bash
POST /quetes
Content-Type: application/json

{
  "nom": "Vaincre le dragon",
  "description": "Un dragon menace le village",
  "prime": 5000.0,
  "dureeEstimee": 10,
  "datePeremption": "2026-06-30",
  "statut": "EN_COURS"
}
```

**Réponse :**

```json
{
  "id": 1,
  "nom": "Vaincre le dragon",
  "description": "Un dragon menace le village",
  "prime": 5000.0,
  "dureeEstimee": 10,
  "datePeremption": "2026-06-30",
  "experienceGagnee": null,
  "statut": "NOUVELLE",
  "commanditaireId": null,
  "equipeId": null
}
```

> **Note :** Le statut est automatiquement défini à `NOUVELLE` lors de la création, peu importe la valeur fournie.

#### 2. Récupérer toutes les quêtes

**Requête :**

```bash
GET /quetes
```

**Réponse :**

```json
[
  {
    "id": 1,
    "nom": "Vaincre le dragon",
    "statut": "NOUVELLE",
    ...
  },
  {
    "id": 2,
    "nom": "Trouver le trésor perdu",
    "statut": "EN_COURS",
    ...
  }
]
```

#### 3. Récupérer une quête par ID

**Requête :**

```bash
GET /quetes/1
```

**Réponse :**

```json
{
  "id": 1,
  "nom": "Vaincre le dragon",
  "description": "Un dragon menace le village",
  "prime": 5000.0,
  "dureeEstimee": 10,
  "datePeremption": "2026-06-30",
  "experienceGagnee": null,
  "statut": "NOUVELLE",
  "commanditaireId": null,
  "equipeId": null
}
```

**Erreur si ID inexistant :**

```json
{
  "message": "Quête non trouvée"
}
```

#### 4. Mettre à jour une quête (partiel)

La mise à jour est **partielle** : seuls les champs non-null sont modifiés.

**Requête :**

```bash
PUT /quetes/1
Content-Type: application/json

{
  "prime": 7500.0,
  "dureeEstimee": 15
}
```

**Réponse :**

```json
{
  "id": 1,
  "nom": "Vaincre le dragon",
  "description": "Un dragon menace le village",
  "prime": 7500.0,
  "dureeEstimee": 15,
  "datePeremption": "2026-06-30",
  "experienceGagnee": null,
  "statut": "NOUVELLE",
  "commanditaireId": null,
  "equipeId": null
}
```

> Les champs `nom`, `description` et `datePeremption` restent inchangés.

#### 5. Supprimer une quête

**Requête :**

```bash
DELETE /quetes/1
```

**Réponse :**

```
HTTP 204 No Content
```

**Erreur si ID inexistant :**

```json
{
  "message": "Quête non trouvée"
}
```

---

## Gestion des erreurs

### Codes de statut HTTP

| Code | Description | Cas d'usage |
|------|-------------|-------------|
| `200 OK` | Succès | GET, PUT réussi |
| `204 No Content` | Succès sans corps | DELETE réussi |
| `400 Bad Request` | Validation échouée | Champs manquants/invalides |
| `404 Not Found` | Ressource introuvable | ID inexistant |
| `500 Internal Server Error` | Erreur serveur | Erreur interne |

### Exemple d'erreur de validation

**Requête :**

```bash
POST /quetes
Content-Type: application/json

{
  "nom": "",
  "prime": null
}
```

**Réponse :**

```json
{
  "errors": {
    "nom": "ne doit pas être vide",
    "description": "ne doit pas être vide",
    "prime": "ne doit pas être nul",
    "dureeEstimee": "ne doit pas être nul",
    "datePeremption": "ne doit pas être nul"
  }
}
```

---

## Tests

Le projet inclut des tests unitaires complets pour garantir la qualité du code.

### Structure des tests

```
src/test/java/com/data/kanyh/
├── mapper/
│   └── QueteMapperTest.java      # 9 tests - Conversions DTO ↔ Entity
└── service/
    └── QueteServiceTest.java     # 12 tests - Logique métier
```

### Exécuter tous les tests

```bash
mvn test
```

### Exécuter un test spécifique

```bash
# Tester uniquement QueteMapper
mvn test -Dtest=QueteMapperTest

# Tester uniquement QueteService
mvn test -Dtest=QueteServiceTest
```

### Couverture des tests

#### QueteMapperTest (9 tests)

- ✅ Conversion Entity → DTO (tous les champs)
- ✅ Conversion enum StatutQuete → String
- ✅ Gestion des champs null optionnels
- ✅ Conversion InputDTO → Entity
- ✅ Statut toujours défini à NOUVELLE
- ✅ Mise à jour partielle (champs non-null uniquement)
- ✅ Aucune modification si tous les champs null
- ✅ Mise à jour sélective de plusieurs champs

#### QueteServiceTest (12 tests)

**getAllQuetes() :**
- ✅ Retourne toutes les quêtes
- ✅ Retourne une liste vide si aucune quête

**getQueteById() :**
- ✅ Retourne la quête correspondante
- ✅ Lance NotFoundException si ID inexistant

**save() :**
- ✅ Crée et sauvegarde une nouvelle quête
- ✅ Définit le statut à NOUVELLE automatiquement

**updateQuete() :**
- ✅ Met à jour une quête existante
- ✅ Lance NotFoundException si ID inexistant
- ✅ Permet une mise à jour partielle

**deleteQuete() :**
- ✅ Supprime une quête existante
- ✅ Lance NotFoundException si ID inexistant
- ✅ Vérifie l'existence avant de supprimer

---

## Documentation du code

### Classes principales

#### QueteService

Service de gestion des quêtes avec opérations CRUD complètes.

**Méthodes :**

- `getAllQuetes()` : Récupère toutes les quêtes disponibles
- `getQueteById(Long id)` : Récupère une quête par son identifiant
- `save(QueteInputDTO input)` : Crée et sauvegarde une nouvelle quête (statut NOUVELLE automatique)
- `updateQuete(Long id, QueteInputDTO input)` : Met à jour une quête existante (mise à jour partielle)
- `deleteQuete(Long id)` : Supprime une quête existante (avec vérification d'existence)

**Exceptions :**
- `NotFoundException` : Lancée lorsqu'une quête avec l'ID spécifié n'existe pas

#### QueteMapper

Mapper pour les conversions entre entités Quete et DTOs.

**Méthodes :**

- `toDTO(Quete quete)` : Convertit une entité Quete en QueteDTO (tous les champs mappés)
- `toEntity(QueteInputDTO input)` : Convertit un QueteInputDTO en entité Quete (statut NOUVELLE forcé)
- `updateEntityFromDTO(QueteInputDTO dto, Quete entity)` : Met à jour une entité existante (seuls les champs non-null sont appliqués)

---

## Documentation API

### Swagger UI

Accédez à la documentation interactive complète via Swagger UI :

**http://localhost:8080/swagger-ui/index.html**

Swagger UI permet de :
- Visualiser tous les endpoints disponibles
- Tester les API directement depuis le navigateur
- Voir les modèles de données et validations
- Consulter les codes de réponse HTTP

---

## Structure de la base de données

### Table : `quetes`

| Colonne | Type | Description | Contraintes |
|---------|------|-------------|-------------|
| `id` | BIGINT | Identifiant unique | PRIMARY KEY, AUTO_INCREMENT |
| `nom` | VARCHAR | Nom de la quête | NOT NULL |
| `description` | VARCHAR | Description détaillée | NOT NULL |
| `prime` | DOUBLE | Récompense en or | NOT NULL |
| `duree_estimee` | INTEGER | Durée estimée en jours | NOT NULL |
| `date_peremption` | DATE | Date limite | NOT NULL |
| `experience_gagnee` | INTEGER | Points d'expérience | NULLABLE |
| `statut` | VARCHAR | Statut actuel | NOT NULL (enum) |
| `commanditaire_id` | BIGINT | ID du commanditaire | NULLABLE |
| `equipe_id` | BIGINT | ID de l'équipe assignée | NULLABLE |

---

## Commandes utiles

### Build du projet

```bash
mvn clean package
```

### Lancer l'application en mode dev

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Générer le JAR exécutable

```bash
mvn clean package
java -jar target/kanyh-0.0.1-SNAPSHOT.jar
```

### Nettoyer les fichiers compilés

```bash
mvn clean
```

---

## Conventions de code

- **Lombok** : Utilisation de `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor` pour réduire le boilerplate
- **Javadoc** : Toutes les méthodes publiques des services et mappers sont documentées
- **Validation** : Les DTOs d'entrée utilisent les annotations Jakarta Validation (`@NotNull`, `@NotBlank`)
- **Naming** : Conventions Java standard (camelCase pour les variables, PascalCase pour les classes)

---

## Contribution

1. Créez une branche pour votre fonctionnalité : `git checkout -b feat/ma-fonctionnalite`
2. Commitez vos changements : `git commit -m "feat: ajout de ma fonctionnalité"`
3. Poussez vers la branche : `git push origin feat/ma-fonctionnalite`
4. Créez une Pull Request

### Convention de commits

Utilisez les préfixes suivants :
- `feat:` - Nouvelle fonctionnalité
- `fix:` - Correction de bug
- `docs:` - Documentation
- `test:` - Ajout/modification de tests
- `refactor:` - Refactoring du code

---

## Support

Pour toute question ou problème :
- Consultez la documentation Swagger : http://localhost:8080/swagger-ui/index.html
- Ouvrez une issue sur le repository GitHub

---

## Licence

[À définir]

---

**Kanyh Team - 2025**
