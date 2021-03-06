package controller;
import model.*;
import view.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;

public class GameBoard
{
    public JFrame mainFrame;
    public JButton reset;
    //added
    private int moves = 0;
    private JLabel score = new JLabel("Moves = " + moves);
    private GridCard gridCard;
    private String currentDifficulty;
    private int timeLeft=60;
    private Timer timer;

    private int currentScore;
    private int pos; 

    private boolean selected=false;
    private boolean play = true;
    private Card c;
    private JButton button1 = new JButton("Placeholder");
    private JButton button2 = new JButton("Placeholder");

    public GameBoard(int size)
    {
        GameBoard p = this;

        mainFrame = new JFrame("Memory Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel backGround = new JPanel();

	    JPanel stackMenu = new JPanel();
        stackMenu.setLayout(new BoxLayout(stackMenu, BoxLayout.Y_AXIS));
        Deck d = new Deck("Easy");

        gridCard = new GridCard(d,p);	


	    //panel for scores
        JPanel scorePanel = new JPanel();
        JLabel time = new JLabel("Time : " + timeLeft);

        ActionListener timerAction = new ActionListener()
        {
            public void actionPerformed(ActionEvent evt) 
            {
                timeLeft-=1;
                time.setText("Time : " + timeLeft);
                if (timeLeft==0)
                {
                    timer.stop();
                    endGame(moves,(60-timeLeft), currentDifficulty, p);
                }
            }
        };
        timer = new Timer(1000,timerAction);
        timer.start();

        JLabel highScore = new JLabel("High Score: " + updateHighScore("Easy"));
        scorePanel.add(score);
        scorePanel.add(highScore);
        scorePanel.add(time);
        stackMenu.add(scorePanel);


	    //panel for difficulties
        JPanel diffPanel = new JPanel();

        String difficultyChoice[] = {"Easy","Medium","Hard"};
        JComboBox difficulty = new JComboBox<>(difficultyChoice);
        currentDifficulty = difficulty.getSelectedItem().toString();
        
        JButton play = new JButton("Play");
        play.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String playDifficulty = difficulty.getSelectedItem().toString();
                Deck newDeck = new Deck(playDifficulty);
			
                backGround.remove(gridCard);
                gridCard = new GridCard(newDeck, p);
                backGround.add(gridCard,0);

			    currentScore = updateHighScore(playDifficulty);
			    highScore.setText("High Score: " + currentScore);

                moves=0;
                score.setText("Moves = " + moves);
                timer.stop();
                timeLeft=60;
                timer.start();

                mainFrame.revalidate();
                mainFrame.pack();

                currentDifficulty = playDifficulty;
            }
        });

	    reset = new JButton("Reset");
        reset.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Deck newDeck = new Deck(currentDifficulty);
                backGround.remove(gridCard);
                gridCard = new GridCard(newDeck, p);
                backGround.add(gridCard,0);

		        currentScore = updateHighScore(currentDifficulty);
                highScore.setText("High Score: " + currentScore);

                moves=0;
                timer.stop();
                timeLeft=60;
                timer.start();
                score.setText("Moves = " + moves);

                mainFrame.revalidate();
                mainFrame.pack();
            }
        });

	    diffPanel.add(difficulty);
	    diffPanel.add(play);
	    diffPanel.add(reset);
	    stackMenu.add(diffPanel);

	
        backGround.add(gridCard);
        backGround.add(stackMenu);
        mainFrame.add(backGround);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public JFrame getFrame()
    {
        return mainFrame;
    }

    public int getMoves()
    {
        return moves;
    }

    public String getDifficulty()
    {
        return currentDifficulty;
    }

    public void updateScore()
    {
        moves++;
        score.setText("Moves = " + moves);
        mainFrame.revalidate();
    }

    public int updateHighScore(String diff)
    {
	    HighScore scoreNumber = new HighScore();
        int pos = scoreNumber.position(diff);
        int HighestScore = scoreNumber.getCurrentScore(pos);
	    return HighestScore;
    }	

    public int stopTime()
    {
        timer.stop();
        return timeLeft;
    }

    public void startAgain()
    {
        mainFrame.setEnabled(true);
        reset.doClick();
        timeLeft = 60;
        timer.start();
    }

    //code that used to be in driver
    public void switchSelected()
    {
        selected = !selected;
    }
    public boolean cardSelected()
    {
        return selected;
    }
    public Card getCard()
    {
        return c;
    }
    public void setCard(Card card)
    {
        c = card;
    }
    public void setButton(JButton inButton)
    {
        button1=inButton;
    }
    public void setSecondButton(JButton inButton)
    {
        button2=inButton;
    }
    public JButton getButton()
    {
        return button1;
    }
    public JButton getSecondButton()
    {
        return button2;
    }
    public void endGame(int time, int moves, String difficulty, GameBoard Proto)
    {
        GameEndDisplay end = new GameEndDisplay(moves, time, difficulty, Proto);
        end.createDisplay();
        end.playAgain();
    }
}
