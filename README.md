# Luca Sardellitti's Work Samples

## Platformer AI
A program which can solve various user created platformer 
levels using a genetic algorithm technique.

### Running the program
Run `platformerEditor.py` inside of the [platformer-ai](platformer-ai) folder.

### Using the program
Once the program opens, a window will open showing the main menu. 
There are various options for using this program.
1. User Play:
	- play through the default level using the WASD keys
1. Quick AI:
	- watch the genetic algorith solve the default level
1. Level Editor:
	- takes you to a level editor where you get to create the layout of a level
	- use the "Save Level" button to keep your layout between runs
	- 3 options for testing the level:
        1. User Play: play through the default level using the WASD keys
        1. AI Learn: watch the process of the AI solving your level
        1. AI Finish: runs the AI without showing the process, gives solution faster
    - Additional Option: type of solve:
        1. Shortest Distance: best player is determined by closest straight line to flag
        1. Shortest Path: best player is determined by shortest path to flag (walls block path)

## Space Game Engine
A 3-D game engine made using OpenGL for Java, with objects 
moving on spherical planets and travelling between them.

### Running the program
There are two executabe files which both run the same game engine found in the [SpaceGameExecutables](SpaceGameExecutables) folder, `spaceGameEngine.jar` and `spaceGameExecutable.exe`.

### Source Code
The source code for this game engine can be found [here](SpaceGame/src/). The main loop running the game is `MainGameLoop` inside of the `gameManager` package.

## Sensor Network Simulations
The [SensorNetworkSimulations](SensorNetworkSimulations) folder contains MATLAB code which was used to simulate and numerically calculate the properties of wireless sensor networks.