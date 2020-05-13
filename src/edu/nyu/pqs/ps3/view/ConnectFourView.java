package edu.nyu.pqs.ps3.view;


import edu.nyu.pqs.ps3.model.ConnectFourController;
import edu.nyu.pqs.ps3.model.ConnectFourModel;
import edu.nyu.pqs.ps3.model.Listner;
import edu.nyu.pqs.ps3.model.Result;
import edu.nyu.pqs.ps3.players.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ConnectFourView implements Listner {

    private ConnectFourModel model;
    private JFrame frame;
    private JPanel introductionChooseGameType;
    private JPanel playerinformation;
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


    /**
     * gets model instance, registers as an observer and initializes game
     */
    public ConnectFourView(ConnectFourController controller){
        this.model = controller.getModel();
        this.controller = controller;
        model.attachListner(this);
        initializeGame();
    }

    /**
     * Creates jframe to start the game, creates instruction label, introduction label, buttons to choose game type(1 or 2 player game)
     * When game type is chosen, model creates the grid, model is initialized, players are added and game starts.
     */
    void initializeGame() {
        frame = new JFrame("Connect Four");
        frame.setLayout(new BorderLayout());
        introduction =new JLabel();
        introduction.setText("\nWelcome to connect 4! First person to get 4 in a row wins\n");
        introduction.setPreferredSize(new Dimension(700, 50));
        introduction.setHorizontalAlignment(SwingConstants.CENTER);

        buttonPanel = new JPanel();
        startTwoPlayer= new JButton("2 player");
        startTwoPlayer.setPreferredSize(new Dimension(300,50));
        //startTwoPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        startWithComputer= new JButton("1 player (vs computer)");
        startWithComputer.setPreferredSize(new Dimension(300,50));
        buttonPanel.add(startTwoPlayer);
        buttonPanel.add(startWithComputer);

        instructions = new JLabel();
        instructions.setPreferredSize(new Dimension(700, 50));
        instructions.setText("Please select the number of players!!!");
        instructions.setHorizontalAlignment(SwingConstants.CENTER);

        introductionChooseGameType = new JPanel();
        introductionChooseGameType.setLayout(new BorderLayout());
        introductionChooseGameType.add(introduction, BorderLayout.NORTH);
        introductionChooseGameType.add(buttonPanel, BorderLayout.CENTER);
        //introductionChooseGameType.add(instructions, BorderLayout.SOUTH);
        introductionChooseGameType.setSize(400, 200);

        frame.getContentPane().add(introductionChooseGameType, BorderLayout.NORTH);
        frame.getContentPane().add(instructions, BorderLayout.SOUTH);
        frame.setSize(700, 250);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startTwoPlayer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String player1 = "";
                while(player1.equals(""))
                    player1 = JOptionPane.showInputDialog("Please enter name for Player 1");
                String player2 = "";
                while(player2.equals(""))
                    player2 = JOptionPane.showInputDialog("Please enter name for Player 2");
                controller.startGameWithPlayer(player1, player2);
                introductionChooseGameType.setVisible(false);
                gameGrid.setVisible(true);
            }
        });

        startWithComputer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String[] options = new String[] {"EASY", "MEDIUM", "HARD"};
                int response = JOptionPane.showOptionDialog(null, "Choose the AI difficulty", "AI Power",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);

                controller.startGameWithComputer(response);
                introductionChooseGameType.setVisible(false);
                gameGrid.setVisible(true);
            }
        });
    }

    @Override
    public void initBoard(int row, int column){
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
                    controller.play(Integer.parseInt(event.getActionCommand()));
                }
            });
            gameGrid.add(buttons[i]);
        }

        slots = new JLabel[row][column];
        for (int rows = 0; rows < row; rows++) {
            for (int columns = 0; columns < column; columns++) {
                slots[rows][columns] = new JLabel(new OvalIcons(60,60, Color.WHITE));
                slots[rows][columns].setHorizontalAlignment(SwingConstants.CENTER);
                //slots[rows][columns].setBorder(new LineBorder(Color.BLACK));
                //slots[rows][columns].setOpaque(true);
                //slots[rows][columns].setBackground(Color.lightGray);
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

    @Override
    public void gameStart() {
        JOptionPane.showMessageDialog(frame, "Get Ready! Player to make the first 4 in a row wins!");
    }

    @Override
    public void playerTurn(Player player) {
        instructions.setText(player.getPlayerName() + "'s turn");
    }

    @Override
    public void gameEnd(Result result, Player player) {
        String title = "Game Over";
        String message = "";
        if(result == Result.WIN) {
            message = player.getPlayerName() + " won! \nDo you want to play again ?";
        }
        else
            message = "Game Draw! \nDo you want to play again ?";
        int option = JOptionPane.showConfirmDialog(frame,
                message, title,
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION)
        {
            controller.resetGame();
            frame.setSize(700, 250);
            instructions.setText("Please select the number of players!!!");
            introductionChooseGameType.setVisible(true);
            gameGrid.setVisible(false);
        }
        else
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void addTokenToPosition(int row, int column, Color color) {
        int timeForAnimation = 0;
        for (int i = 0; i < row; i++) {
            JLabel label = slots[i][column];
            Timer timer = new Timer(100*i ,new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    label.setIcon(new OvalIcons(60,60, color));
                }
            });
            timer.setRepeats(false);
            timer.start();
            timer = new Timer(200*i ,new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    label.setIcon(new OvalIcons(60,60, Color.WHITE));
                }
            });
            timer.setRepeats(false);
            timer.start();
            timeForAnimation += 110*i;

        }

        Timer timer = new Timer(timeForAnimation ,new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                slots[row][column].setIcon(new OvalIcons(60,60, color));
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void disableAddingTokensTo(int column) {
        buttons[column].setEnabled(false);
    }

    private static class OvalIcons implements Icon {

        private int width, height;
        private Color foreground;

        public OvalIcons(int w, int h, Color foreground) {
            width = w;
            height = h;
            this.foreground = foreground;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.BLACK);
            g.drawOval(x,y,width,height);
            g.setColor(foreground);
            g.fillOval(x, y, width-1, height-1);

        }

        public int getIconWidth() { return width; }
        public int getIconHeight() { return height; }
    }
}
