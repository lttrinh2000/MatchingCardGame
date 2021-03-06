package view;
import model.*;
import controller.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GameEndDisplay
{
    public static int frameSize = 600;
    private JFrame exitFrame;

    int userTime;
    int userMoves;
    String difficulty;

    HighScore HighScore = new HighScore();
    String userName;
    int prevHigh;
    String prevName;
    GameBoard Proto;

    boolean playAgainChoice;

    public GameEndDisplay(int timeTaken, int numberMoves, String difficulty, GameBoard inProto)
    {
        this.userTime = timeTaken;
        this.userMoves = numberMoves;
	    this.difficulty = difficulty;
        prevName = HighScore.getPrevName(HighScore.position(difficulty));
        prevHigh = HighScore.getCurrentScore(HighScore.position(difficulty));
        this.Proto = inProto;
    }

    public void createDisplay()
    {
	Proto.mainFrame.setEnabled(false);

        // top level container for end-of-game page
        exitFrame = new JFrame("Memory");
        exitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //outer container
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
    
        // panel for game info
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
        // display user moves and time
        JLabel moves = new JLabel("Moves: " + userMoves);
        statsPanel.add(moves);
        JLabel time = new JLabel("Time taken: " + userTime + " seconds");
        statsPanel.add(time);
        
        // panel for prev high score and new high score info
        JPanel scoringPanel = new JPanel();
        scoringPanel.setLayout(new BoxLayout(scoringPanel, BoxLayout.PAGE_AXIS));
        //previous scoring info
        JLabel displayHighscore = new JLabel("Previous High Score: " + prevHigh + " by user " + prevName);
	statsPanel.add(displayHighscore);
	statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
	container.add(statsPanel);

	if (userTime == 0 | userTime == 60)
        {
            JLabel endTime = new JLabel("End of time.");
            scoringPanel.add(endTime);
        }

        // if user got a high score, ask for name
        else if (userMoves < prevHigh)
        {
            JLabel newHigh = new JLabel("You got a new high score!");
	    statsPanel.add(newHigh);
	    Font font1 = new Font("SansSerif", Font.BOLD, 20);
            JTextField nameTextBox = new JTextField();
	    nameTextBox.setSize(200,20);
	    nameTextBox.setFont(font1);
	    nameTextBox.setHorizontalAlignment(JTextField.CENTER);
            scoringPanel.add(nameTextBox);

            JButton submitButton = new JButton("OK");
	    submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            scoringPanel.add(submitButton);

            submitButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    userName = nameTextBox.getText();
                    nameTextBox.setEnabled(false);
                    submitButton.setEnabled(false);

                    HighScore.saveHighScore(userName, String.valueOf(userMoves), HighScore.position(difficulty));
                }
            });
            exitFrame.pack();
        }
        else if (userMoves == prevHigh)
        {
            JLabel newHigh = new JLabel("You almost got a new high score!");
            statsPanel.add(newHigh);
        }
        else
        {
            JLabel newHigh = new JLabel("No high score.");
            statsPanel.add(newHigh);
        }
        container.add(statsPanel);
	container.add(scoringPanel);

        JPanel playAgainPanel = new JPanel(new FlowLayout());
        JLabel playAgainQuestion = new JLabel("Would you like to play again?");
        JButton yesButton = new JButton("Play Again!");
        JButton noButton = new JButton("No.");
        playAgainPanel.add(playAgainQuestion);
        playAgainPanel.add(yesButton);
        playAgainPanel.add(noButton);
        yesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                playAgainChoice = true;
                Proto.startAgain();
                exitFrame.dispose();
            }
        });
        noButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                playAgainChoice = false;
                Proto.mainFrame.dispose();
                exitFrame.dispose();
            }
        });
        container.add(playAgainPanel);

        // pack and toggle visibility on
        exitFrame.add(container);
        exitFrame.pack();
        exitFrame.setVisible(true);
    }
    

    public boolean playAgain()
    {
        return playAgainChoice;
    }

}
