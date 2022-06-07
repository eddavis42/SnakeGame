/**
 * Edward Davis
 * CS 152L Spring 2022
 * Lab 9 part 2 Assignment: Snake Game GUI.
 * This class generates the GUI to run a snake game, allowing a user to
 * actually play the game. It does so by constructing a GameManager object to
 * manage the bookkeeping, and a GamePanel to animate the game and manage
 * inputs, and adds the GamePanel to a frame along with other buttons to
 * interact with the game.
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class SnakeGUI extends JFrame implements ActionListener, SnakeInterface{
    GameManager game;
    static int newGames=0;
    static gamePanel gamePanel;
    static JLabel Tracker;
    Timer timer;
    boolean paused;


    SnakeGUI(String levelFile){
        super("Snake!");
        paused = false;
        setDefaultCloseOperation ( JFrame . EXIT_ON_CLOSE );
        game = new GameManager(levelFile);
        JPanel menuPanel = new JPanel ();
        JButton newGameButton = new JButton("New Game");
        Tracker = new JLabel("Your score is: "+game.playerScore);
        JButton startPauseButton = new JButton("Pause");
        ActionListener startPause = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!paused){
                    paused=true;
                    gamePanel.pause();
                    startPauseButton.setText("Resume");
                }
                else if(paused){
                    paused = false;
                    gamePanel.resume();
                    startPauseButton.setText("Pause");
                }
                pack ();
                setVisible(true);
            }
        };
        startPauseButton.addActionListener(startPause);

        ActionListener newGame = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGames++;
                gamePanel.timer.stop();
                game = null;
                gamePanel=null;
                game = new GameManager(levelFile,newGames);
                gamePanel = new gamePanel(game);
                paused=false;
                startPauseButton.setText("Pause");
                add(gamePanel,BorderLayout.PAGE_END);
                pack ();
                setVisible(true);
            }
        };
        newGameButton.addActionListener(newGame);

        menuPanel.add(startPauseButton,BorderLayout.CENTER);
        menuPanel.add (Tracker,BorderLayout.WEST);
        menuPanel.add(newGameButton,BorderLayout.EAST);
        add(menuPanel,BorderLayout.PAGE_START);

        gamePanel = new gamePanel(game);
        add(gamePanel,BorderLayout.PAGE_END);

        pack ();
        setVisible(true);

        timer = new Timer(150, this);
        timer.setInitialDelay(0);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Tracker.setText("Your score is: "+game.playerScore);
    }

    public static void main(String[] args) {

        if(args.length>0){
            SnakeGUI gameGUI = new SnakeGUI(args[0]);
        }
        else{
            SnakeGUI gameGUI = new SnakeGUI("maze-cross.txt");
        }

    }


}
