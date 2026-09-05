# Mercenaires vs Zombies

Un jeu 2D plateforme/stratégie où une équipe de mercenaires défend le village de **Valombre**
contre une vague inarrêtable de zombies, réveillés par une malédiction dans l'ancien cimetière des
Cendres.

Tout le code est prêt. Il ne reste qu'à le compiler en fichier `.apk` installable — gratuitement,
via GitHub, sans rien installer sur ton ordinateur.

---

## Étape 1 — Créer un compte GitHub (si tu n'en as pas)

1. Va sur https://github.com/signup
2. Crée un compte gratuit avec ton adresse email.

## Étape 2 — Créer un nouveau dépôt (repository)

1. Une fois connecté, clique sur le bouton **"+"** en haut à droite, puis **"New repository"**.
2. Nom du dépôt : `mercenaires-vs-zombies` (ou ce que tu veux).
3. Laisse-le en **Public** ou **Private**, peu importe.
4. Ne coche aucune case (pas de README, pas de .gitignore) — le projet en a déjà.
5. Clique sur **"Create repository"**.

## Étape 3 — Envoyer le projet sur GitHub

Sur la page qui s'affiche après la création, GitHub te propose plusieurs méthodes. La plus simple
sans utiliser de terminal :

1. Installe **GitHub Desktop** (gratuit) : https://desktop.github.com
2. Ouvre GitHub Desktop, connecte-toi avec ton compte GitHub.
3. Menu **File > Add local repository**, sélectionne le dossier du projet (celui-ci).
4. Si GitHub Desktop te dit que ce n'est pas encore un dépôt Git, clique sur **"create a
   repository"** dans ce même dossier.
5. Clique sur **"Publish repository"** en haut, choisis le dépôt créé à l'étape 2 (ou publie
   directement depuis ici), puis **Publish**.

## Étape 4 — Laisser GitHub compiler l'APK

1. Va sur la page de ton dépôt sur github.com.
2. Clique sur l'onglet **"Actions"** en haut.
3. Tu devrais voir un workflow **"Build APK"** en cours d'exécution (un rond orange qui tourne).
   S'il n'a pas démarré tout seul, clique dessus puis **"Run workflow"**.
4. Attends 3 à 6 minutes. Le rond devient une coche verte ✅ quand c'est terminé.
5. Clique sur l'exécution terminée, puis tout en bas sur l'artifact
   (**"mercenaires-vs-zombies-apk"**) dans la section *Artifacts* : ça télécharge un fichier `.zip`
   contenant `app-debug.apk`.

## Étape 5 — Installer l'APK sur le téléphone

1. Transfère le fichier `app-debug.apk` sur ton téléphone (par email, Google Drive, câble USB...).
2. Ouvre le fichier `.apk` depuis le gestionnaire de fichiers du téléphone.
3. Si le téléphone bloque l'installation : va dans **Réglages > Applications > Autorisations
   spéciales > Installer des applications inconnues**, et autorise l'application que tu utilises
   pour ouvrir le fichier (ex : Fichiers, Chrome...).
4. Reviens sur le fichier `.apk` et installe. L'icône de l'application apparaît sur l'écran
   d'accueil.

---

## Ce que fait l'application (v0.6)

- Un écran d'histoire au lancement : le contexte de Valombre et de sa malédiction, avec un bouton
  "Rassembler l'équipe" pour continuer. Un lien "Revoir l'histoire" sur l'écran de sélection permet
  d'y revenir à tout moment.
- Un écran de sélection d'équipe avant la partie : tu choisis lequel des 6 mercenaires tu joues
  directement (bouton "Jouable"), et tu coches ceux qui t'accompagnent au combat (les autres restent
  à la caserne). La fiche de chaque personnage (rôle, PV, dégâts ou soin, portée, et une courte bio)
  s'affiche sur sa carte. En dessous, une carte narrative par boss ("Menaces liées à la malédiction")
  présente son rôle, ses PV/dégâts, sa capacité spéciale éventuelle et sa phrase de contexte — de
  quoi savoir à quoi s'attendre avant même de lancer la partie.
- Le village de Valombre (à droite de l'écran) avec une barre de vie, à défendre.
- Une horde de zombies inarrêtable : elle spawn en continu depuis la gauche, et l'intervalle entre
  deux zombies rétrécit avec le temps (ça devient plus dur plus on survit).
- Deux types de zombies pour l'instant : le "marcheur" (lent, robuste) et le "coureur" (rapide,
  fragile) — de plus en plus de coureurs apparaissent avec le temps.
- Des boss liés à la malédiction, qui surgissent périodiquement (le premier après 45 secondes, puis
  environ toutes les 100 secondes) avec une bannière d'annonce et une barre de vie dédiée à l'écran.
  Ils alternent et deviennent plus coriaces à chaque apparition :
  - **Le Fossoyeur Maudit** : un colosse lent mais très résistant, qui frappe fort au corps-à-corps.
  - **Le Nécromancien des Cendres** : la source de la malédiction elle-même. Il attaque à distance
    avec une explosion de magie noire (dégâts de zone) et relève régulièrement des zombies autour de
    lui pour renforcer la horde.
- Six types de mercenaires disponibles au recrutement ; celui que tu joues directement se déplace,
  saute et attaque au corps-à-corps ou à distance selon son type, les autres se battent
  automatiquement à tes côtés :
  - **Tank** : très résistant, attire l'attention des zombies à distance (aggro) pour protéger le
    reste de l'équipe.
  - **Archer** : dégâts à distance rapides et légers (flèches).
  - **Mage** : dégâts à distance plus lourds mais plus lents (boules de feu).
  - **Artilleur** : tir d'artillerie à très longue portée qui explose et blesse tous les zombies
    proches de l'impact (dégâts de zone).
  - **Soigneur** : n'attaque pas les zombies, mais soigne en continu l'allié le plus blessé à
    portée.
- Contrôles à l'écran : ◀ ▶ pour se déplacer, boutons "Saut" et "Attaque" à droite.
- Partie sans fin : le but est de survivre le plus longtemps possible et d'éliminer un maximum de
  zombies. Quand la vie du village tombe à zéro, c'est game over avec deux boutons : "Recommencer"
  (même équipe) et "Changer d'équipe" (retour à l'écran de sélection).
- Pour l'instant l'art est en formes géométriques colorées (pas de sprites dessinés) — à remplacer
  plus tard par de vrais visuels si tu veux.

## Comment le jeu est construit (pour la suite)

Le code est organisé pour qu'ajouter du contenu plus tard soit simple :

- `game/CharacterType.kt` : la fiche de stats de chaque type de mercenaire (vie, dégâts, portée,
  vitesse, corps-à-corps ou à distance...). Ajouter un archer, un tank, un soigneur, etc. revient à
  ajouter une entrée dans ce fichier — c'est le point d'extension prévu pour arriver aux "dizaines
  de personnages" évoqués au départ.
- `game/ZombieType.kt` : pareil, côté zombies (variantes de vitesse/vie/dégâts).
- `game/BossType.kt` : pareil pour les boss (stats, capacité d'invocation, phrase d'annonce et de
  contexte). Ajouter un nouveau boss revient à ajouter une entrée ici.
- `game/Lore.kt` : le contexte narratif (nom du village, texte de l'écran d'histoire) — à enrichir
  au fur et à mesure (autres villages, chapitres...).
- `game/entities/` : le comportement (déplacement, attaque, rendu) de chaque type d'entité
  (mercenaire, zombie, village, projectile).
- `game/GameEngine.kt` : la boucle de simulation (vagues, IA, collisions) et le rendu.

## Modifier l'application plus tard

Pour toute modification (nouveaux personnages, contexte narratif, nouvelles mécaniques, plusieurs
mercenaires jouables, multijoueur...), dis-le : le code sera modifié, et il suffira de renvoyer les
fichiers modifiés sur GitHub (GitHub Desktop détecte les changements et propose de les publier en
un clic) pour qu'un nouvel APK soit généré automatiquement.
