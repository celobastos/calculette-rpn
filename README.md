# Calculette RPN - mini projet de Genie Logiciel

Application Java/JavaFX d'une calculatrice en notation polonaise inverse (RPN), organisee selon le patron MVC.

## Prerequis

- JDK 17 ou plus recent
- Eclipse IDE avec le support Maven, inclus dans les distributions recentes d'Eclipse
- Maven 3.9 ou plus recent uniquement pour une execution depuis un terminal

Il n'est pas necessaire de telecharger ou de configurer manuellement le SDK JavaFX. Les bibliotheques JavaFX, JUnit et les plugins necessaires sont declares dans le fichier `pom.xml` et sont telecharges automatiquement par Maven lors de la premiere execution. Une connexion Internet est donc necessaire uniquement pour ce premier telechargement.

## Importer et executer dans Eclipse

1. Ouvrir Eclipse.
2. Selectionner **File > Import... > Maven > Existing Maven Projects**.
3. Selectionner le dossier contenant ce projet et son fichier `pom.xml`.
4. Cliquer sur **Finish** et attendre la fin du telechargement des dependances Maven.
5. Faire un clic droit sur le projet puis selectionner **Run As > Maven build...**.
6. Saisir `clean javafx:run` dans le champ **Goals**, puis cliquer sur **Run**.

Pour les executions suivantes, la configuration creee apparait directement dans **Run As > Maven build**.

Si Eclipse n'actualise pas automatiquement les dependances, faire un clic droit sur le projet puis selectionner **Maven > Update Project... > OK**.

## Executer l'application graphique depuis un terminal

Ouvrir un terminal dans le dossier contenant le fichier `pom.xml`, puis executer :

```bash
mvn clean javafx:run
```

Maven telecharge automatiquement JavaFX et demarre l'application graphique. Il faut executer cette commande depuis le dossier du projet, et non depuis le dossier personnel de l'utilisateur.

## Executer le modele sans interface graphique

Le modele peut egalement etre compile et execute directement dans un terminal, sans demarrer JavaFX :

```bash
mvn compile
java -cp target/classes fr.imtminesales.calculette.ModelDemo
```

Il est alors possible de saisir des nombres et des commandes directement dans la console :

```text
4
push
7
+
exit
```

Les commandes disponibles sont :

- pile : `push`, `pop`, `drop`, `swap`, `clear` et `dropall` ;
- arithmetique : `+`, `-`, `*`, `/` et `^` (ou `pow`) ;
- scientifiques : `sin`, `cos`, `tan`, `square`, `sqrt`, `inverse`, `ln`, `log` et `exp` ;
- constantes : `pi` et `e` ;
- assistance : `help` et `exit`.

Les fonctions trigonometriques utilisent des angles en degres. Les fonctions unaires s'appliquent a l'accumulateur s'il contient une valeur, ou au sommet de la pile si l'accumulateur est vide. Pour une puissance, empiler la base, saisir l'exposant, puis utiliser `^`.

Cette execution en console a ete ajoutee pour demontrer concretement l'independance des couches de l'architecture MVC. Le modele ne depend ni de JavaFX, ni de la vue, ni du controleur : il peut donc fonctionner seul. De la meme maniere, le controleur depend de contrats (`CalculatorModelInterface` et `CalculatorView`) et non des implementations graphiques. Il peut ainsi etre teste avec une vue simplifiee sans lancer JavaFX.

## Executer les tests unitaires

```bash
mvn test
```

Les tests unitaires verifient separement le fonctionnement du modele, du controleur et des operations utilisees par l'interface. Les tests du controleur emploient une implementation de test de `CalculatorView` a la place de la vue JavaFX. Ils confirment ainsi que :

- le modele execute correctement les operations RPN et scientifiques sans interface graphique ;
- le controleur communique avec le modele et le contrat de la vue sans dependre de JavaFX ;
- la vue reste responsable de l'affichage et transmet les actions, tandis que les calculs restent dans le modele.

Cette separation permet de verifier le fonctionnement des trois parties de l'architecture MVC sans melanger leurs responsabilites.

## Tester le modele dans Eclipse

Executer `ModelDemo` avec **Run As > Java Application**, puis saisir un nombre ou une commande dans la console.

## Utilisation

1. Saisir un nombre avec le clavier de l'application ou directement dans l'accumulateur.
2. Appuyer sur **PUSH** pour placer ce nombre au sommet de la pile.
3. Saisir et empiler les autres operandes.
4. Appuyer sur `+`, `-`, `x` ou `/`.

Exemple pour `3 * (2 + 5 * (4 + 7))` :

```text
3 PUSH 2 PUSH 5 PUSH 4 PUSH 7 PUSH + x + x
```

Resultat : `171`.

Les autres commandes sont :

- **POP** : depile le sommet vers l'accumulateur ;
- **DROP** : supprime le sommet de la pile ;
- **SWAP** : echange les deux elements du sommet ;
- **CLEAR** : vide l'accumulateur ;
- **DROP ALL** : vide toute la pile ;
- **+/-** : change le signe de la saisie courante.

Les nombres peuvent etre saisis avec un point ou une virgule comme separateur decimal.

## Extensions

- **FONCTIONS SCIENTIFIQUES** : calcule `sin`, `cos`, `tan`, `x²`, `√x`, `1/x`, `ln`, `log`, `eˣ` et `xʸ`. Les angles sont exprimes en degres.
- **CONSTANTES** : place `π` ou `e` dans l'accumulateur.
- **⌫** : efface le dernier caractere de l'accumulateur.

## Ameliorations ajoutees apres la presentation

La version presentee initialement contenait principalement les operations arithmetiques RPN et les commandes de pile. Apres la presentation, nous avons pu ajouter deux ameliorations supplementaires :

- les operations unaires et scientifiques `sin`, `cos`, `tan`, `x²`, `√x`, `1/x`, `ln`, `log` et `eˣ`, ainsi que les constantes `π` et `e` ;
- le bouton **⌫**, qui permet d'effacer le dernier caractere saisi sans vider completement l'accumulateur.

Ces ajouts utilisent la meme architecture MVC que les fonctions initiales. Les calculs restent realises dans le modele et sont declenches depuis l'interface par le controleur.

## Architecture

```text
Vue JavaFX -> Controleur -> Modele RPN
     ^                         |
     +------ notifications ----+
```

- `model` ne depend ni de JavaFX ni de la vue ;
- `controller` traduit les actions utilisateur en operations du modele ;
- `view` construit l'interface JavaFX et n'effectue aucun calcul ;
- le modele publie un instantane immutable apres chaque modification ;
- les erreurs ne modifient pas l'etat du modele.
