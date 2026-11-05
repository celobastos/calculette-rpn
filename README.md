# Calculette RPN - mini projet de Genie Logiciel

Application Java/JavaFX d'une calculatrice en notation polonaise inverse (RPN), organisee selon le patron MVC.

## Prerequis

- JDK 17 ou plus recent
- Maven 3.9 ou plus recent

## Executer l'application

```bash
mvn clean javafx:run
```

## Executer les tests

```bash
mvn test
```

## Tester le modele dans Eclipse

Executer `ModelDemo` avec **Run As > Java Application**, puis saisir un nombre ou une commande dans la console.

```text
4
push
7
+
exit
```

Les commandes disponibles sont `push`, `pop`, `drop`, `swap`, `clear`, `dropall`, `+`, `-`, `*` et `/`.

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
