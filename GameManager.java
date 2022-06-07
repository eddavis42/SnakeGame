/**
 * Edward Davis
 * CS 152L Spring 2022
 * Lab 9 part 2 Assignment: Snake Game GUI.
 * This class does all the main bookkeeping to run a snake game. The "game map"
 * is generated from a text file and is built as a 2D array of characters. New
 * objects for the snake and food are created with randomly generated
 * coordinates, each of which are stored with a Point class,linking them to
 * free spaces in the map.The snake move across the x and y axes, and colliding
 * with different objects produces different outcomes.
 *
 */


import java.util.*;
import java.io.*;
import java.awt.Point;

public class GameManager implements SnakeInterface{
    BufferedReader reader = null;
    ArrayList<String> levelInfo = new ArrayList<>();
    Snake playerSnake;
    food playerFood;
    poisonfood playerPoison;
    char emptyMap[][];
    char gameMap[][];
    int width;
    int height;
    int seed;
    boolean gameOver;
    int playerScore;
    Random r;

    /**
     * Game manager has two constructors, so it can be passed a seed for
     * random number generation, or use it's own seed. Constructor
     * instantiates all the game's variables and objects and creates the map.
     *
     * @param levelFile String containing the name of the text file with the
     *                  dimensions of a map.
     */
    GameManager(String levelFile){
        playerScore = 0;
        gameOver=false;
        seed = (int) Math.random()*100;
        r = new Random();
        r.setSeed(seed);
        getMap(levelFile);
        playerSnake = new Snake(nonCollidingCoords());
        updateMap();
        playerFood = new food();
        updateMap();


    }
    GameManager(String levelFile, int seed){
        gameOver=false;
        this.seed = seed;
        r = new Random();
        r.setSeed(seed);
        getMap(levelFile);
        playerSnake = new Snake(nonCollidingCoords());
        updateMap();
        playerFood = new food();
        updateMap();


    }

    /**
     * Helper method to read the data from the level file, extract the height
     * and width of the map, followed by corner coordinates for additional
     * walls.
     * @param levelFile String containing the name of the text file with the
     *                  numbers indicating wall dimensions.
     */
    public void getMap(String levelFile){
        try {
            reader = new BufferedReader(new FileReader(levelFile));
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                levelInfo.add(newLine);
            }
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        finally {
            try {
                if (reader != null ) { reader.close(); }
            }
            catch (IOException unnamedException){}
        }
        //set the width and height of the map
        Scanner lineScanner = new Scanner(levelInfo.get(0));
        width = lineScanner.nextInt();
        height = lineScanner.nextInt();
        emptyMap = new char[height][width];//outter array is height/y variable
        gameMap = new char[height][width];//inner array is width/x variable
        //add border walls
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                emptyMap[i][j] = EMPTY;
            }
        }

        for(int i=0;i<height;i++){
            emptyMap[i][0] = WALL;
            emptyMap[i][width-1] = WALL;
        }
        for(int j=0;j<width;j++){
            emptyMap[0][j] = WALL;
            emptyMap[height-1][j] = WALL;
        }

        //add walls to the map
        levelInfo.remove(0);
        for(String s:levelInfo){
            Scanner lineScanner2 = new Scanner(s);
            int leftX = lineScanner2.nextInt();
            int leftY = lineScanner2.nextInt();
            int rightX = lineScanner2.nextInt();
            int rightY = lineScanner2.nextInt();
            for(int i=leftY;i<=rightY;i++){
                for(int j=leftX;j<=rightX;j++){
                    emptyMap[i][j]=WALL;
                }
            }
        }
        clearMap();
    }

    /**
     * Helper method takes a point containing a pair of coordinates and uses
     * them as index numbers with the gamemap array to check what value is held
     * at the location indicated by the point.
     * @param coords A point containing a pair of coordinates to check
     * @return The character in those coordinates in the map
     */
    char checkCollision(Point coords){
        int x = coords.x;
        int y = coords.y;
        return gameMap[y][x];
    }

    /**
     * Helper method iterates through the map array and produces a list of
     * points containing the index number of each "empty" entry in the array.
     * Then it produces a random object with the seed determined by the
     * constructor to randomly pick from the list a point which will
     * correspond to an empty position in the game map.
     * @return The point
     */
    Point nonCollidingCoords(){
        Point coords = new Point();
        //iterate through the map-array and make a list of points for every
        // empty location, then randomly select from the list
        LinkedList<Point> emptyPoints = new LinkedList<>();
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if(gameMap[i][j] == EMPTY){
                    emptyPoints.addLast(new Point());
                    emptyPoints.getLast().x = j;
                    emptyPoints.getLast().y = i;
                }
            }
        }

        int newCoords = r.nextInt(emptyPoints.size());
        coords = emptyPoints.get(newCoords);
        return coords;
    }

    /**
     * Food was made into its own class, so it can have a constructor and be
     * respawned as many times as necessary. The food constructor uses the
     * nonCollidingCoords() method to ensure it does not spawn in the same
     * position as the wall or snake. When the moves into a position occupied
     * by the food, a new food objects spawns in another random, empty
     * location, and the snake grows by a position.
     */
    class food{
        Point coords;

        food (){
            coords = nonCollidingCoords();
        }
    }

    /**
     * An extra hazard for variety, which shrinks the snake instead of
     * growing it. This could look similar to the food in the full game with GUI
     */
    class poisonfood{
        Point coords;
        poisonfood (){
            coords = nonCollidingCoords();
        }
    }


    /**
     * Sets the game map equal to it's empty version (containing only walls
     * and empty space, since these do not ever change location) before
     * getting the location points from each existing game object and adding
     * a representative character to the game map array. Doing this after the
     * objects' points are updated allows the game manager to only include
     * their current positions in the map, without needing to worry about
     * changing specific indexes.
     */
    void updateMap(){
        clearMap();
        //populate map with snake's coordinates

        gameMap[playerSnake.head.y][playerSnake.head.x] = SNAKEHEAD;

        for(Point p: playerSnake.body){
            gameMap[p.y][p.x] = SNAKEBODY;
        }
        //populate map with food coordinates
        if(playerFood!=null){
            gameMap[playerFood.coords.y][playerFood.coords.x] = FOOD;
        }
        if(playerPoison!=null){
            gameMap[playerPoison.coords.y][playerPoison.coords.x] = POISONFOOD;
        }

    }

    //call before each snake movement so old position is no longer there
    void clearMap(){
                for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                gameMap[i][j] = emptyMap[i][j];
            }
        }
    }

    /**
     * Overriden toSring method is public so it can be called by the tester
     * @return String representation of the game map array
     */
    @Override
    public String toString(){
        String string = "";
        for(char[] column:gameMap){
            for(char row: column){
                //System.out.print(row);
                string += row;

            }
            //System.out.println("");
            string +="\n";
        }
        return string;
    }


    //call before moving the snake to stop from deleting last tale position
    // during next move() call.
    void grow(){
        playerSnake.grow();
    }

    /**
     * Move method calls the snake's move method to move it, and also checks
     * for collisions to update the game as necessary. If the snake collides
     * with a wall it activates a game over, and it does not move. If the snake
     * collides with food, the food respawns to a new location, and the snake
     * grows. If the snake collides with the poison food, it shrinks in length.
     * Keeping the snake's move method and the game manager's move method
     * separate simplifies the separation of the game's bookkeeping and the
     * snake's internal bookkeeping.
     * @param direction enum specifying the direction of movement
     */
    public void move(Direction direction){
        boolean coastClear = true;
        boolean stationary = false;
        updateMap();
        Point checkPos = new Point();
        switch (direction){
            case NONE:
                updateMap();
                checkPos = nonCollidingCoords();
                stationary = true;
                break;
            case UP:
                checkPos = new Point(playerSnake.head.x, playerSnake.head.y-1);
                break;
            case DOWN:
                checkPos = new Point(playerSnake.head.x, playerSnake.head.y+1);
                break;
            case LEFT:
                checkPos = new Point(playerSnake.head.x-1, playerSnake.head.y);
                break;
            case RIGHT:
                checkPos = new Point(playerSnake.head.x+1, playerSnake.head.y);
                break;
        }
        if(checkCollision(checkPos) == WALL &&stationary==false){
            coastClear = false;
            gameOver();
        }
        if(checkCollision(checkPos) == SNAKEBODY &&stationary==false){
            coastClear = false;
            gameOver();
        }
        if(checkCollision(checkPos) == POISONFOOD &&stationary==false){
            playerPoison = null;
            playerSnake.shrink();
            playerScore--;
        }
        if(checkCollision(checkPos) == FOOD &&stationary==false){
            grow();
            playerScore++;
            playerFood = new food();
            if(playerScore %2==0 && playerPoison == null){
                updateMap();
                playerPoison = new poisonfood();
            }
        }
        if(coastClear &&stationary==false){
            playerSnake.move(direction);
        }
        updateMap();

    }


    //call if snake collides with a wall or itself
    void gameOver(){
        gameOver = true;
    }
}
