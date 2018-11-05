# RaceTrack
Simple single player car game, which is based on: https://en.wikipedia.org/wiki/Racetrack_(game) 
The player controls the speed of the car with the numbers 1 to 9, and can close the game by pressing 0. If 5 is pressed, the speed wont change. The speed is given in (x, y) coordinates and is controlled as follows:
1: (-1, -1), 2: (0, -1), 3: (1, -1), 4: (-1, 0), 5: (0, 0), 6: (1, 0), 7: (-1, 1), 8: (0, 1), 9: (1, 1)

The program will create image files on your computer, but should delete these either at the startup of the program or the end of the program. 

The game has two different tracks, a square and rectangular track. The user picks one of these tracks along with the size of the track. 

The game uses a hand drawn car "playerCar.png". 

The file RaceTrack.java uses StdDraw.java which is made by Robert Sedgewick and Kevin Wayne. 

Additional features that could be implemented: 
- Playing against a bot
- Multiplayer 
- Different colors 
- More detailed map
- Much more!
