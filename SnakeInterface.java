
/**
 * Edward Davis
 * CS 152L Spring 2022
 * Lab 9 part 2 Assignment: Snake Game GUI.
 * Shared interface allows all the classes to access the direction enum
 *  which is used for moves
 *
 */

 interface SnakeInterface {
    char WALL = 'X';
    char EMPTY = '.';
    char SNAKEHEAD = 'S';
    char SNAKEBODY = 's';
    char FOOD = 'f';
    char POISONFOOD = 'p';
    int windowScaler = 20;

     enum Direction {
        UP,DOWN,LEFT,RIGHT,NONE
    }

   public default void move(Direction direction){

   }

}
