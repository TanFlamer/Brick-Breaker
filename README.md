## **Brick_Destroy_Improved**

This README talks about the changes made to the original brick breaker game by Filippo Ranza. The changes are either
maintenance or extensions of the original game and the reasons for each change will be made clear.

## **MAINTENANCE**

### **_Improved the brick and crack generation code_**

The original code for brick generation was very unoptimised and redundant. So, the code for has been rewritten to be
more efficient. Additionally, some code for crack generation were completely unused in the code most notably "InMiddle"
and "jumps" and were removed.

### **_Absorbed single brick type level into chessboard levels_**

A single type level is basically just a chessboard level but where both type of bricks are the same. So, to simplify the
code, the single type level method is reworked to just call the chessboard level method with both brick types being
the same.

### **_Absorbed all brick types into a single class_**

If you think about it, a clay brick and cement brick are basically just a steel brick but with 100% damage probability.
Similarly, a cement brick is just a crackable clay or steel brick. So, all brick types have been absorbed into a single
class to simplify the code.

### **_Remade the game with MVC design pattern_**

The code for the main game has been remade using the MVC design pattern. The Model is GameBoard which holds all the
information about every aspect of the game including the entities such as ball, player and bricks. The Controller
is GameBoardController which modifies the game data from GameBoard according to predefined functions. The View is 
GameBoardRenderer which renders the game data of GameBoard which has been modified by GameBoardController onto
the game screen.

## **EXTENSIONS**

### **_Added a new brick type_**

Concrete bricks have been added to the game which combines the properties of Cement and Steel bricks. The Concrete brick
is crackable, can take 2 hits and only has a 40% damage probability.

### **_Added new background to the HomeMenu and game_**

New backgrounds have been added to the HomeMenu and games to make the game look more exciting.

### **_Added new counters in the game_**

In addition to the brick count and ball count, the game now has counters for total score and time, level score and time
and God Mode time. The words have been enlarged and font added to improve the look.

### **_Added new options to the HomeMenu_**

New options such as an InfoScreen which explains the mechanics of the game, a PopUpMenu which shows a random screenshot
of a level when clicked each time, a top 10 HighScore list for each of the levels and the total highscores and a custom 
console has been added to the Home Menu.

### **_Added new options to the Debug Console_**

The options to go to previous level and reset ball and player position have been added to the Debug Console.

### **_Added a new Power Up_**

A new power up has been added to the game which activates God Mode to the ball for 10 seconds. In God Mode, ball-brick
deflection is disabled. So, the ball will not deflect after hitting a brick and will continue moving forward until the
brick is destroyed. Additionally, the ball will bounce off the bottom border and not be lost. A power up is spawned
every minute.

### **_Added audio to the game_**

Audio such as BGM for all menus and levels and sound effects for ball-brick collision has been added to the game.

### **_Added a HighScore PopUp for every level_**

A highscore popup showing all scores and times of the players will be shown after every level and after the game ends.
You can enter and save your name here.

### **_Added a 5th level to the game_**

A 5th level has been added to the game. It is a simple chessboard level with Steel and Concrete bricks. The simplicity
of the level will be explained below.

### **_Added a custom console to the game_**

This is the biggest addition to the game. With this custom console, every aspect of the game such as rows of bricks,
bricks in a row, brick types, level types and number of balls can be customised. There are 3 level types, Default which 
shows the default game level, Ordered, which arranges bricks in an ordered and cyclic manner and random which arranges 
bricks in a random order. Additionally, the level of control can be changed with the level types. With true random, all 
aspects of the game is randomised, even rows of bricks and bricks in a row. While for True Ordered, every aspect of the 
level can be manipulated. So, it is because of this that I made a simple default design for the 5th level.

### **_Added free movement to the game_**

From the custom console, there is an option to toggle free movement for the level. This allows the player to move
anywhere on the screen. Additionally, the ball speed is also able to be modified using the directional arrow keys
without using the debug console. This breaks the balance of the game, so the use of this mode invalidates the
player from recording his score and time for that level and the total scores and time for that game.

### **_Added new game orientation_**

From the custom console, the player is also able to choose the game orientation. Bottom is the default option. If the
player choose top, the game is flipped upside down. The player is now on top and the brick is at the bottom.
