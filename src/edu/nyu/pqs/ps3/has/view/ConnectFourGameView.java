package edu.nyu.pqs.ps3.has.view;

/**
 * This code is submission of pqs Assignment4 to implement Connect Four
 *
 * @author  Himani Shah (has482)
 */

import edu.nyu.pqs.ps3.has.app.GameListener;
import edu.nyu.pqs.ps3.has.app.ConnectFourController;
import edu.nyu.pqs.ps3.has.app.ConnectFourModel;
import edu.nyu.pqs.ps3.has.app.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * It creates a view for the game.
 */
public class ConnectFourGameView implements GameListener {
    private ConnectFourModel model;
    private JFrame frame;
    private JPanel introductionChooseGameType;
    private JPanel gameGrid;
    private GridLayout gridLayout;
    private JLabel instructions;
    private JLabel introduction;
    private JLabel[][] slots;
    private JButton[] buttons;
    private JButton startTwoPlayer;
    private JButton startWithComputer;
    private ConnectFourController controller;

    /**
     * gets model instance, registers as an observer and initializes game
     */
    public ConnectFourGameView(ConnectFourModel model, ConnectFourController controller){
        this.model = model;
        this.controller = controller;
        model.registerListener(this);
        initializeGame();
    }

    /**
     * Creates jframe to start the game, creates instruction label, introduction label, buttons to choose game type(1 or 2 player game)
     * When game type is chosen, model creates the grid, model is initialized, players are added and game starts.
     */
    private void initializeGame() {
        frame = new JFrame("Connect Four");
        frame.setLayout(new BorderLayout());
        introduction =new JLabel();
        introduction.setText("\nWelcome to connect 4! First person to get 4 in a row wins\n");
        introduction.setPreferredSize(new Dimension(700, 50));

        startTwoPlayer= new JButton("2 player");
        startTwoPlayer.setPreferredSize(new Dimension(350,50));
        startWithComputer= new JButton("1 player (vs computer)");
        startWithComputer.setPreferredSize(new Dimension(350,50));

        instructions = new JLabel();
        instructions.setPreferredSize(new Dimension(700, 50));
        instructions.setText("Please select the number of players!!!");

        introductionChooseGameType = new JPanel();
        introductionChooseGameType.setLayout(new BorderLayout());
        introductionChooseGameType.add(introduction, BorderLayout.NORTH);
        introductionChooseGameType.add(startTwoPlayer, BorderLayout.WEST);
        introductionChooseGameType.add(startWithComputer, BorderLayout.EAST);
        introductionChooseGameType.add(instructions, BorderLayout.SOUTH);
        introductionChooseGameType.setSize(400, 200);

        frame.getContentPane().add(introductionChooseGameType, BorderLayout.NORTH);
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startTwoPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.startTwoPlayerGame();
            }
        });

        startWithComputer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.startOnePlayerGame();
            }
        });
    }

    /**
     * Creates row*column board to play the game, It adds buttons to add disc to the column
     * @param row
     * @param column
     */
    public void makeBoard(int row, int column){
        if(frame.isAncestorOf(gameGrid)) {
            frame.getContentPane().remove(gameGrid);
        }
        gridLayout = new GridLayout(row+1,column); //+1 for the buttons array, to add coin
        gameGrid =new JPanel();
        gameGrid.setLayout(gridLayout);

        buttons = new JButton[column];
        for(int i=0; i< column; i++){
            buttons[i] = new JButton("↓");
            buttons[i].setActionCommand("" + i);
            buttons[i].setBackground(Color.lightGray);
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    controller.makeMove(Integer.parseInt(event.getActionCommand()));
                }
            });
            gameGrid.add(buttons[i]);
        }

        slots = new JLabel[row][column];
        for (int rows = 0; rows < row; rows++) {
            for (int columns = 0; columns < column; columns++) {
                slots[rows][columns] = new JLabel();
                slots[rows][columns].setHorizontalAlignment(SwingConstants.CENTER);
                slots[rows][columns].setBorder(new LineBorder(Color.darkGray));
                slots[rows][columns].setOpaque(true);
                slots[rows][columns].setBackground(Color.lightGray);
                gameGrid.add(slots[rows][columns]);
            }
        }
        frame.add(gameGrid, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Displays that game has started
     */
    public void gameStart() {
        instructions.setText("Game has started.");
    }

    /**
     * Displays next turn
     * @param p player
     */
    public void yourTurn(Player p) {
        instructions.setText( p + "\'s turn");
    }

    /**
     * Displays the winner
     * @param p winning player
     */
    public void youWon(Player p) {
        instructions.setText( p+" won!. \n To play again choose the game type above");
    }

    /**
     * Display draw
     */
    public void draw() {
        instructions.setText("Its a draw. \n To play again choose the game type above");
    }

    /**
     * Disable options(choose game type options)
     */
    public void disableOptions() {
        startTwoPlayer.setEnabled(false);
        startWithComputer.setEnabled(false);
    }

    /**
     * Adds disc of particular color at a partiular position on the board
     * @param coinColor disc color
     * @param column  position to add disc
     * @param row position to add disc
     */
    public void addDiscToSlot(Color coinColor, int column, int row) {
        slots[row][column].setBackground(coinColor);
    }

    /**
     * Disables button using which we add disc at a particular column
     * @param column column position
     */
    public void disableAddDiscButton(int column) {
        buttons[column].setEnabled(false);
    }

    /**
     * enables options(choose game type options)
     */
    public void enableOptions() {
        startTwoPlayer.setEnabled(true);
        startWithComputer.setEnabled(true);
    }

    /**
     * Disables button using which we add disc at all columns
     */
    public void diableAllDiscAddButton() {
        for(int i=0; i< model.getGridNoOfColumns(); i++){
            buttons[i].setEnabled(false);
        }
    }
}
