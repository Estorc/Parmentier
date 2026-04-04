# Parmentier - Français
Parmentier est une implémentation du jeu japonais "Hashi"

## Contexte
Ce jeu a été crée pendant la troisième année à l'université de l'équipe de Parmentier, en tant que devoir.
L'objectif était de créer un un jeu en Java et utilisant javaFx et devait posséder la caratéristique d'avoir des aides à la résolution.

## Caractéristiques
- Didacticiel
- Aides à la résolution intégrées
- Support de plusieurs utilisateurs et leurs données
- Mode sombre
- Expériences sonores et graphiques

## Construire depuis la source
Si vous voulez construire le jeu depuis la source, il sera nécessaire d'installer Java et Gradle.

NOTE : Si vous avez Minecraft d'installé, vous avez probalement déjà Java d'installé, il n'est pas nécessaire de réinstaller une version plus récente

Une fois que c'est installé, il faut juste écrire dans un terminal situé à la racine de ce projet
```sh
./gradlew run
```

Il est aussi possible de créer un .jar contenant tout le jeu et ses dépendances (afin d'avoir une version "portable")
```sh
./gradlew assembleShadowDist
``` 
Ensuite dans `app/build/distributions` il y aura un "app-shadow.zip". Une fois ce .zip décompressé, le dossier "lib" contiendra le .jar voulu.

NOTE : Si le terme de "portable" est entre guillement, c'est parce qu'à l'opposé de la phylosophie de son langage d'implémentation, JavaFX  n'est pas fait pour être executable partout. Il sera portable uniquement si vous l'utilisez sur le même système d'exploitation.

# Parmentier - English 
Parmentier is an implementation of the japanese game named 'Hashi'.

## Context
This game has been created during the Parmentier team's third year of computer science at University, as an assignment.
The goal was to create that game in Java and using javaFX, and required to feature hints.
## Features

- Tutorial
- Built-in hints to complete the grid
- Multiple users and data support
- Dark Mode
- Sound and graphical feedback

## Build from source
If you want to build it from source, you'll need first to install Java and Gradle.

NOTE : If you have Minecraft installed, you probably already have Java installed, and it is not necessairy to reinstall a newer version.

Once those are installed, you only need to write in a terminal at the root of this project
```sh
./gradlew run
```

and Voilà!

It is also possible to create a .jar file containing the game and its dependencies to have a "portable" version.
```sh
./gradlew assembleShadowDist
```

In the `app/build/distribution` folder, you should see a "app-shadow.zip" file. Once this file decompressed, you may find the final .jar in the "lib" folder. 

NOTE : if the "portable" word is between quotes it's because javaFX, unlike the philosphy of its programming language, is not meant to run everywhere. It will be portable only if you use it on the same operatin system.

# Licensing
Copyright (C) 2026 Estorc

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, see
<https://www.gnu.org/licenses/>.
