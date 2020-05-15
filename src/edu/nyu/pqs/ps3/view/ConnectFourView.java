/**
 * This code is submission of pqs Assignment3 to implement Connect 4 game
 *
 * @author Shubham Divekar (sjd451)
 */
package edu.nyu.pqs.ps3.view;


import edu.nyu.pqs.ps3.model.ConnectFourController;
import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Listner;
import edu.nyu.pqs.ps3.model.Result;
import edu.nyu.pqs.ps3.players.AIDifficulty;
import edu.nyu.pqs.ps3.players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Creates a view for the Human Player to play with
 */
public class ConnectFourView implements Listner {

    private ConnectFourModel model;
    private JFrame frame;
    private JPanel introductionChooseGameType;
    private JPanel gameGrid;
    private JPanel buttonPanel;
    private GridLayout gridLayout;
    private JLabel instructions;
    private JLabel introduction;
    private JLabel[][] slots;
    private JButton[] buttons;
    private JButton startTwoPlayer;
    private JButton startWithComputer;
    private ConnectFourController controller;
    private Player currentPlayer;

    /**
     * Gets model instance, registers as an observer and initializes game
     */
    public ConnectFourView(ConnectFourController controller) {
        this.model = controller.getModel();
        this.controller = controller;
        model.attachListner(this);
        initializeGame();
    }

    /**
     * Creates JFrame to start the game, with introduction, instructions and buttons to
     * choose game type(1 or 2 player game)
     * When game type is chosen, the controller calls the model to initialize the board
     * and adds players to start the game.
     */
    void initializeGame() {
        frame = new JFrame("Connect Four");
        frame.setLayout(new BorderLayout());
        introduction = new JLabel();
        introduction.setText("\nWelcome to connect 4! First person to get 4 in a row wins\n");
        introduction.setPreferredSize(new Dimension(700, 50));
        introduction.setHorizontalAlignment(SwingConstants.CENTER);

        buttonPanel = new JPanel();
        startTwoPlayer = new JButton("2 player");
        startTwoPlayer.setPreferredSize(new Dimension(300, 50));
        startWithComputer = new JButton("1 player (vs computer)");
        startWithComputer.setPreferredSize(new Dimension(300, 50));
        buttonPanel.add(startTwoPlayer);
        buttonPanel.add(startWithComputer);

        introductionChooseGameType = new JPanel();
        introductionChooseGameType.setLayout(new BorderLayout());
        introductionChooseGameType.add(introduction, BorderLayout.NORTH);
        introductionChooseGameType.add(buttonPanel, BorderLayout.CENTER);
        introductionChooseGameType.setSize(400, 200);

        instructions = new JLabel();
        instructions.setPreferredSize(new Dimension(700, 50));
        instructions.setText("Please select the number of players!!!");
        instructions.setHorizontalAlignment(SwingConstants.CENTER);

        frame.getContentPane().add(introductionChooseGameType, BorderLayout.NORTH);
        frame.getContentPane().add(instructions, BorderLayout.SOUTH);
        frame.setSize(700, 250);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startTwoPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.startGameWithPlayer(getPlayerName(1), getPlayerName(2));
                introductionChooseGameType.setVisible(false);
                gameGrid.setVisible(true);
            }
        });

        startWithComputer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.startGameWithComputer(getPlayerName(1), getDifficulty());
                introductionChooseGameType.setVisible(false);
                gameGrid.setVisible(true);
            }
        });
    }

    /**
     * Prompts the user to enter the Player name through a JOptionPane while starting a game
     *
     * @param playerNum index of the player
     * @return Name of the player
     */
    private String getPlayerName(int playerNum) {
        String playerName = "";
        boolean firstTry = true;
        while (playerName.equals("")) {
            if (firstTry)
                playerName = JOptionPane.showInputDialog("Please enter name for Player " + playerNum);
            else
                playerName = JOptionPane.showInputDialog("Please enter a valid name for Player " + playerNum);
            firstTry = false;
        }

        return playerName;
    }

    /**
     * Prompts user to enter desired difficulty through JOptionPane while starting a game
     *
     * @return difficulty choice between easy hard and medium
     */
    private AIDifficulty getDifficulty() {
        String[] options = new String[]{"EASY", "MEDIUM", "HARD"};
        AIDifficulty difficulty = AIDifficulty.EASY;

        int response = JOptionPane.showOptionDialog(null, "Choose the AI difficulty",
                "AI Power", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (response == 2)
            difficulty = AIDifficulty.HARD;
        else if (response == 1)
            difficulty = AIDifficulty.MEDIUM;

        return difficulty;
    }

    /**
     * Initializes the display of the board. This contains a top row of buttons used for inserting
     * a token into a column and a grid of JLabels that represent the slots of the board.
     * OvalIcons class is used to render the circle that represents a token or slot.
     *
     * @param row    Number of rows in the board
     * @param column Number of columns in the board
     */
    @Override
    public void initBoard(int row, int column) {
        if (frame.isAncestorOf(gameGrid)) {
            frame.getContentPane().remove(gameGrid);
        }
        gridLayout = new GridLayout(row + 1, column); //+1 for the buttons array, to add coin
        gameGrid = new JPanel();
        gameGrid.setLayout(gridLayout);

        buttons = new JButton[column];
        for (int i = 0; i < column; i++) {
            buttons[i] = new JButton("↓");
            buttons[i].setActionCommand("" + i);
            buttons[i].setBackground(Color.lightGray);
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    controller.play(Integer.parseInt(event.getActionCommand()));
                }
            });
            JButton temp = buttons[i];
            /** To animate the button on hover */
            temp.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    temp.setBackground(currentPlayer.getTokenColor());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    temp.setBackground(Color.lightGray);
                }
            });
            gameGrid.add(buttons[i]);
        }

        slots = new JLabel[row][column];
        for (int rows = 0; rows < row; rows++) {
            for (int columns = 0; columns < column; columns++) {
                slots[rows][columns] = new JLabel(new OvalIcons(60, 60, Color.WHITE));
                slots[rows][columns].setHorizontalAlignment(SwingConstants.CENTER);
                gameGrid.add(slots[rows][columns]);
            }
        }
        gameGrid.setBackground(Color.BLUE);
        gameGrid.setVisible(false);
        frame.add(gameGrid, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setSize(700, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Display a message notifying that the game has started
     */
    @Override
    public void gameStart() {
        JOptionPane.showMessageDialog(frame, "Get Ready! Player to make the first 4 in a row wins!");
    }

    /**
     * Displays the name of current user and changes the currentPlayer
     *
     * @param player next Player to play
     */
    @Override
    public void playerTurn(Player player) {
        currentPlayer = player;
        instructions.setText(currentPlayer.getPlayerName() + "'s turn");
    }

    /**
     * If the game is over display a prompt for the user to choose playing a new game.
     * If the player wins then Highlight their tokens and fade otherPlayers tokens
     *
     * @param result result of the game
     * @param player player who played last
     */
    @Override
    public void gameEnd(Result result, Player player) {
        if (result == Result.WIN) {
            highlightWinningPlayer(player);
        } else {
            String message = "Game Draw! \nDo you want to play again ?";
            showResetDialog(message);
        }

    }

    /**
     * Prompts if the players want to play again and if so,
     * Resets for a new game, hiding the game board and showing the instructions
     * else closes the application
     */
    private void showResetDialog(String message) {
        String title = "Game Over";
        int option = JOptionPane.showConfirmDialog(frame,
                message, title,
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            controller.resetGame();
            frame.setSize(700, 250);
            instructions.setText("Please select the number of players!!!");
            introductionChooseGameType.setVisible(true);
            gameGrid.setVisible(false);
        } else
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Turns all the winning player token a bit bigger and fades the tokens of other players
     *
     * @param player
     */
    private void highlightWinningPlayer(Player player) {
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                for (JLabel row[] : slots) {
                    for (JLabel slot : row) {
                        if (((OvalIcons) slot.getIcon()).getIconColor() != Color.WHITE)
                            if (((OvalIcons) slot.getIcon()).getIconColor() == player.getTokenColor())
                                slot.setIcon(new OvalIcons(70, 70, player.getTokenColor()));
                            else
                                slot.setIcon(new OvalIcons(60, 60, Color.darkGray));
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                String message = player.getPlayerName() + " won! \nDo you want to play again ?";
                showResetDialog(message);
            }
        };

        swingWorker.execute();
    }

    /**
     * When the model approves insertion of token, animate the insertion
     */
    @Override
    public void addTokenToPosition(int row, int column, Color color) {
        startFallAnimation(row, column, color);
    }

    private void startFallAnimation(int row, int column, Color color) {
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                int timeForAnimation = 0;
                for (int i = 0; i < row; i++) {
                    JLabel label = slots[i][column];
                    Timer timer = new Timer(100 * i, new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            label.setIcon(new OvalIcons(60, 60, color));
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                    timer = new Timer(200 * i, new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            label.setIcon(new OvalIcons(60, 60, Color.WHITE));
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                    timeForAnimation += 110 * i;

                }

                Timer timer = new Timer(timeForAnimation, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        slots[row][column].setIcon(new OvalIcons(60, 60, color));
                    }
                });
                timer.setRepeats(false);
                timer.start();

                return true;
            }

            @Override
            protected void done() {
                /** This is to re-affirm that the insertion is not overwritten due to un-finished animation */
                Timer timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        slots[row][column].setIcon(new OvalIcons(60, 60, color));
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        };

        swingWorker.execute();
    }

    /**
     * If a column becomes full, disable the insertion button for that column
     */
    @Override
    public void disableAddingTokensTo(int column) {
        buttons[column].setEnabled(false);
    }

    /**
     * This is a helper class to render the circular JLabels used in the board
     */
    private static class OvalIcons implements Icon {

        private int width, height;
        private Color foreground;

        public OvalIcons(int w, int h, Color foreground) {
            width = w;
            height = h;
            this.foreground = foreground;
        }

        /**
         * This overrides the default paint to draw a circle as the Icon
         */
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.BLACK);
            g.drawOval(x, y, width, height);
            g.setColor(foreground);
            g.fillOval(x, y, width - 1, height - 1);

        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public Color getIconColor() {
            return foreground;
        }
    }
}
