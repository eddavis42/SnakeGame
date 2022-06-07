/**
 * Edward Davis
 * CS 152L Spring 2022
 * Lab 9 part 2 Assignment: Snake Game GUI.
 * This class creates a panel which animates the game and manages player
 * controls. It takes a GameManager object for the game's bookkeeping, paints
 * a panel to correspond to the game's map. It runs a timer which moves
 * continuously changes the snake's position, repainting the map with each
 * iteration. It also adds a keylistener to get input from the player to
 * change the direction of the snake.
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.Timer;


class gamePanel extends JPanel implements ActionListener,SnakeInterface {
    LinkedList<Point> gameList;
    GameManager game;
    SnakeInterface.Direction currentDirection;
    Timer timer;

    gamePanel(GameManager game) {
        currentDirection = Direction.NONE;
        this.game = game;
        gameList = new LinkedList<>();
        fillGameList();
        setPreferredSize(new Dimension((game.width)*windowScaler,
                (game.height)*windowScaler));

        timer = new Timer(150, this);
        timer.setInitialDelay(0);
        timer.start();

        addKeyListener ( new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch( keyCode ) {
                    case KeyEvent.VK_UP:
                        if(currentDirection!=Direction.DOWN){
                            currentDirection = Direction.UP;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(currentDirection!=Direction.UP){
                            currentDirection = Direction.DOWN;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if(currentDirection!=Direction.RIGHT){
                            currentDirection = Direction.LEFT;
                        }
                        break;
                    case KeyEvent.VK_RIGHT :
                        if(currentDirection!=Direction.LEFT){
                            currentDirection = Direction.RIGHT;
                        }
                        break;
                }

            }

        });

        setFocusable(true);
        requestFocusInWindow();

    }



    void fillGameList(){
        gameList.clear();
        for (int i = 0; i < game.height; i++) {
            for (int j = 0; j < game.width; j++) {
                gameList.add(new Point(j, i));
            }
        }
    }

    /**
     * Allows the GUI to pause the game
     */
    public void pause(){
        timer.stop();
    }

    /**
     * Allows the GUI to resume the game after pausing
     */
    public void resume(){
        timer.restart();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.PINK);
        for(Point p:gameList){
            switch (game.gameMap[p.y][p.x]){
                case EMPTY:
                    g.setColor(Color.WHITE);
                    break;
                case WALL:
                    g.setColor(Color.BLACK);
                    break;
                case SNAKEBODY:
                    g.setColor(Color.GREEN);
                    break;
                case SNAKEHEAD:
                    g.setColor(new Color(43, 105, 13));
                    break;
                case FOOD:
                    g.setColor(Color.YELLOW);
                    break;
                case POISONFOOD:
                    g.setColor(Color.RED);
                    break;
            }
            g.fillRect((p.x)*windowScaler,(p.y)*windowScaler, windowScaler,windowScaler);
        }

    }

    /**
     * Runs when called by timer
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(game.gameOver){
            timer.stop();
            currentDirection=Direction.NONE;
            JOptionPane.showMessageDialog(this,
                    "Game Over! Your score was: " + game.playerScore + " " +
                            "points.",
                    "Game Over", 1);
        }


        if(currentDirection!=null){
            game.move(currentDirection);
            fillGameList();
            repaint();
            requestFocus();
        }

    }
}