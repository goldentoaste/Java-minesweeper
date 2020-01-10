package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;


import javax.imageio.ImageIO;
import javax.swing.*;


import java.util.*;

//Frame class containing all the graphical and logical conmponents 

class Frame {

    // fields to be initialized: frame, panel containing all the buttons, grid for
    // handling of the generation of gameboard

    private JFrame frame;
    private JPanel panel;
    private JPanel scoreJPanel;
    JButton face;
    private javax.swing.Timer timer;
    Font font;
    // private JOptionPane optionPane;

    private JLabel minesLeftJLabel;
    private JLabel timeLabel;

    protected Button[][] buttonArray;
    private int buttonSize;
    protected boolean gameOn;
    protected boolean firstClick = true;
    public int tilesLeft;
    private int rowsNum;
    private int colsNum;
    private int minesNum;
    int minesLeft;
    private long startingTime = 0;
    private String currentMode;
    private double currentTime;
    private ArrayList<Score> highScoresLists;

    // images of tile, mine, & flag
    protected ImageIcon TILEICON;
    protected ImageIcon EMPTY;
    protected ImageIcon MINEICON;
    protected ImageIcon FLAGICON;
    protected ImageIcon FlAGGEDMINE;
    protected ImageIcon CLICKED;
    protected ImageIcon CLICKED_FACE;
    protected ImageIcon NORMAL_FACE;
    protected ImageIcon DEAD_FACE;
    protected ImageIcon WON;

    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "ca"));
        new Frame(50, 9, 9, 2);

    }

    public Frame(int size, int rows, int cols, int mines) {

        //======================ini components======================//
        panel = new JPanel();
        frame = new JFrame("GAME!");
        panel = new JPanel();

        scoreJPanel = new JPanel();
        timer = new javax.swing.Timer(50, null);
        minesLeftJLabel = new JLabel();
        timeLabel = new JLabel();
        face = new JButton();
        currentMode = "e";
        MenuBar menuBar = new MenuBar();
        Menu difficultyMenu = new Menu("Difficulty");
        MenuItem expertItem = new MenuItem("Expert");
        MenuItem normalItem = new MenuItem("Normal");
        MenuItem easyItem = new MenuItem("Easy");
        MenuItem customItem = new MenuItem("Custom");

        Menu gameOptionsMenu = new Menu("GameOptions");
        MenuItem resetItem = new MenuItem("Reset Game");
        MenuItem changeSizeItem = new MenuItem("Change Button Size");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem highScoreItem = new MenuItem("High Score Table");

        // ====================Importing Images =====================//
        initImages(size);
        // ======================Action listenners======================//

        // changes difficalty by setting the board parameter and starting a new game
        ActionListener difficultyAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object temp = e.getSource();

                if (temp == expertItem) {
                    initializeGame(buttonSize, 16, 30, 99);
                    currentMode = "h";
                }

                if (temp == normalItem) {
                    initializeGame(buttonSize, 16, 16, 40);
                    currentMode = "m";
                }

                if (temp == easyItem) {
                    initializeGame(buttonSize, 8, 8, 10);

                    currentMode = "e";
                }
            }
        };
        // starting a new game but keeping the currently game settings
        ActionListener resetAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                initializeGame(buttonSize, rowsNum, colsNum, minesNum);
            }
        };

        // closes the application
        ActionListener exitAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        // updates the currently score board(changes the number of mines left, and the
        // timer)
        ActionListener updateTimeAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentTime = (System.currentTimeMillis() - startingTime) / 1000.0;
                timeLabel.setText("Time: " + currentTime);

            }
        };
        // pop up an about window
        ActionListener aboutAction = new ActionListener() {
            // TO DO: Update about message!
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Java Minesweeper clone by Ray Gong. (C) 2019", "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };
        // pop up a window containing instruction for the game
        ActionListener helpAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        " The board presented contains number(top left) of hidden mines.\n Left click a tile to reveal it.\n"
                                + "Clicking on a mine ends the game; but if tile does not contain a mine, it will show the number of mines adjacent to it.\n "
                                + "Right click to flag a tile if you think it contains a mine.\n Flag all mines or clear all tiles that doesnt contain a mine to win.",
                        "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // allows the user to input a new button size, that is a integer between 20 and
        // 60.
        ActionListener buttonSizeAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // the text field for the user to type in values
                JTextField s = new JTextField();
                Object[] inputField = { "New Button Size:", s };

                int input = JOptionPane.showConfirmDialog(null, inputField, "Change Button Size",
                        JOptionPane.OK_CANCEL_OPTION);

                boolean validInput = false;

                if (input == JOptionPane.OK_OPTION) {
                    // if the user inputs a value and click "ok", then validate the input. If the
                    // input is invalid, then pop another window asking for a input again
                    do {
                        // using regular expression to check if the value inputted is an integer between
                        // 20 and 60
                        if (s.getText().matches("[2-5][0-9]|60")) {
                            int inputSize = Integer.parseInt(s.getText());
                            initImages(inputSize);
                            initializeGame(inputSize, rowsNum, colsNum, minesNum);
                            validInput = true;
                        } else {
                            // setting a default value of 50 if the user previously inputted an invalid
                            // input
                            s.setText("50");
                            inputField[0] = "Input must be an integer between 20 & 60, try again:";
                            input = JOptionPane.showConfirmDialog(null, inputField, "Change Button Size",
                                    JOptionPane.OK_CANCEL_OPTION);

                            if (input == JOptionPane.CANCEL_OPTION) {
                                break;

                            }
                        }
                    } while (!validInput);
                }

            }
        };

        // create a custom game, given the dimensions and number of mines
        ActionListener customGameAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                currentMode = "x";

                JTextField r = new JTextField();
                JTextField c = new JTextField();
                JTextField n = new JTextField();

                Object[] inputFields = { "Input new dimension & number of mines:", "Rows:", r, "Columns", c, "Mines:",
                        n };

                int input = JOptionPane.showConfirmDialog(null, inputFields, "Custom Game",
                        JOptionPane.OK_CANCEL_OPTION);
                boolean legalInput = false;

                if (input == JOptionPane.OK_OPTION) {
                    do {
                        // using isValidSize to ensure that the input dimention is a resonable
                        // size(100x50 max), and number of mines is between 0 and rows*columns - 1. Also
                        // if all three inputs are integers
                        if (isValidSize(r.getText(), c.getText(), n.getText())) {
                            initializeGame(buttonSize, Integer.parseInt(r.getText()), Integer.parseInt(c.getText()),
                                    Integer.parseInt(n.getText()));
                            legalInput = true;
                        } else {
                            // if the first input is not valid, pop up another window asking for inputs
                            // again, but set the values to the same as medium difficalty
                            inputFields[0] = "Invalid input. Restriction for input:\n 5 < rows < 50,  5 < columns < 100, mines < (rows * columns -1)";
                            r.setText("16");
                            c.setText("16");
                            n.setText("40");

                            input = JOptionPane.showConfirmDialog(null, inputFields, "Custom Game",
                                    JOptionPane.OK_CANCEL_OPTION);
                            if (input == JOptionPane.CANCEL_OPTION) {
                                break;
                            }
                        }
                    } while (!legalInput);
                }

            }
        };

      
        ActionListener highScoreAction = new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHighScores();
                showHighScore();
            }
        };

        // ======================Menu Bar======================//
        expertItem.addActionListener(difficultyAction);
        normalItem.addActionListener(difficultyAction);
        easyItem.addActionListener(difficultyAction);
        customItem.addActionListener(customGameAction);
        difficultyMenu.add(expertItem);
        difficultyMenu.add(normalItem);
        difficultyMenu.add(easyItem);
        difficultyMenu.add(customItem);

        resetItem.addActionListener(resetAction);
        exitItem.addActionListener(exitAction);
        aboutItem.addActionListener(aboutAction);
        helpItem.addActionListener(helpAction);
        changeSizeItem.addActionListener(buttonSizeAction);
        highScoreItem.addActionListener(highScoreAction);
        gameOptionsMenu.add(resetItem);
        gameOptionsMenu.add(changeSizeItem);
        gameOptionsMenu.add(highScoreItem);
        gameOptionsMenu.add(aboutItem);
        gameOptionsMenu.add(helpItem);
        gameOptionsMenu.add(exitItem);


        menuBar.add(difficultyMenu);
        menuBar.add(gameOptionsMenu);

        // ======================panels======================//
        panel.setBackground(new Color(252, 252, 252));

        timer.addActionListener(updateTimeAction);

        minesLeftJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scoreJPanel.setLayout(new GridLayout(1, 3));
        scoreJPanel.setBackground(new Color(252, 252, 252));
        scoreJPanel.add(minesLeftJLabel);
        scoreJPanel.add(face);
        scoreJPanel.add(timeLabel);

        face.setMargin(new Insets(0, 0, 0, 0));
        face.setBorderPainted(false);
        face.setFocusPainted(false);
        face.setContentAreaFilled(false);
        face.addActionListener(resetAction);

        // ===========================================Frame===================================================//
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("MineSweeper!");
        frame.setResizable(false);
        frame.setMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.NORTH, scoreJPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);

        initializeGame(size, rows, cols, mines);

        frame.pack();
        frame.setVisible(true);

    }

    public void initImages(int size) {

        try {

            // the composite functions are used to load the image and scaling it in one line
            TILEICON = new ImageIcon(ImageIO.read(this.getClass().getResource("images/tile.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            MINEICON = new ImageIcon(ImageIO.read(this.getClass().getResource("images/mine.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            FLAGICON = new ImageIcon(ImageIO.read(this.getClass().getResource("images/flag.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            EMPTY = new ImageIcon(ImageIO.read(this.getClass().getResource("images/empty.png")).getScaledInstance(size,
                    size, Image.SCALE_SMOOTH));

            FlAGGEDMINE = new ImageIcon(ImageIO.read(this.getClass().getResource("images/flaggedMine.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            CLICKED = new ImageIcon(ImageIO.read(this.getClass().getResource("images/clicked.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            CLICKED_FACE = new ImageIcon(ImageIO.read(this.getClass().getResource("images/clicking.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            NORMAL_FACE = new ImageIcon(ImageIO.read(this.getClass().getResource("images/gameOn.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            DEAD_FACE = new ImageIcon(ImageIO.read(this.getClass().getResource("images/lost.png"))
                    .getScaledInstance(size, size, Image.SCALE_SMOOTH));

            WON = new ImageIcon(ImageIO.read(this.getClass().getResource("images/won.png")).getScaledInstance(size,
                    size, Image.SCALE_SMOOTH));

        } catch (Exception e) {

        }

    }

    public void inputHighScore() {
        String name = JOptionPane.showInputDialog(null, "Your Name:");
            name += " " + currentTime + " " + currentMode + "\n";
        try{
            File path = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            File highScore = new File(path.getParentFile().getAbsolutePath() +"/highScore.txt");
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(highScore, true));
           
            bw.write(name);
            bw.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void showHighScore() {
        JPanel beginnerPanel = new JPanel();
        JPanel intermidiatePanel = new JPanel();
        JPanel expertPanel = new JPanel();
        

        BoxLayout lo1 = new BoxLayout(beginnerPanel, BoxLayout.Y_AXIS);
        BoxLayout lo2 = new BoxLayout(intermidiatePanel, BoxLayout.Y_AXIS);
        BoxLayout lo3 = new BoxLayout(expertPanel, BoxLayout.Y_AXIS);

        beginnerPanel.setLayout(lo1);
        
        intermidiatePanel.setLayout(lo2);
        expertPanel.setLayout(lo3);
        beginnerPanel.add(new JLabel("Beginner       "));
        intermidiatePanel.add(new JLabel("Intermidiate       "));
        expertPanel.add(new JLabel("Expert"));
        
        while (highScoresLists.size() > 0) {
            Score lowest = highScoresLists.get(0);
            for (int i = 0; i < highScoresLists.size(); i++) {
                if (highScoresLists.get(i).time < lowest.time) {
                    lowest = highScoresLists.get(i);
                }
            }
            switch (lowest.mode) {
            case "h":
                if (expertPanel.getComponentCount() < 10)
                    expertPanel.add(new JLabel(lowest.name + "   " + lowest.time));
                highScoresLists.remove(lowest);
                break;
            case "m":
                if (intermidiatePanel.getComponentCount() < 10)
                    intermidiatePanel.add(new JLabel(lowest.name + "   " + lowest.time));
                highScoresLists.remove(lowest);
                break;

            case "e":
                if (beginnerPanel.getComponentCount() < 10)
                    beginnerPanel.add(new JLabel(lowest.name + "   " + lowest.time));
                highScoresLists.remove(lowest);
                break;
            
            default:
                break;
            }
        }

        while(beginnerPanel.getComponentCount() < 10){
            beginnerPanel.add(new JLabel("..."));
        }
        while(intermidiatePanel.getComponentCount() < 10){
            intermidiatePanel.add(new JLabel("..."));
        }
        while(expertPanel.getComponentCount() < 10){
            expertPanel.add(new JLabel("..."));
        }

        //JPanel[] panelsToDisplay = {beginnerPanel, intermidiatePanel, expertPanel};
       
        JPanel panelsToDisplay = new JPanel(new FlowLayout());
        
      
    
        panelsToDisplay.add(beginnerPanel);
        panelsToDisplay.add(intermidiatePanel);
        panelsToDisplay.add(expertPanel);
        
        JOptionPane.showMessageDialog(null, panelsToDisplay, "HighScores", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadHighScores() {
       
        try {
            BufferedReader br;
            File path = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
            File highScore = new File(path.getParentFile().getAbsolutePath() +"/highScore.txt");
            if(!highScore.exists()){
                highScore.createNewFile();
            }
            br = new BufferedReader(new FileReader(highScore));

            String line = br.readLine();
            while (line != null) {

                String[] parameters = line.split(" ");
                highScoresLists.add(new Score(parameters[0], parameters[1], parameters[2]));
               
                line = br.readLine();

            }
            br.close();

        } catch (Exception e) {

        }
    }

    public void initializeGame(int size, int rows, int cols, int mines) {

        // resetting variables
        gameOn = true;
        tilesLeft = rows * cols - mines;
        minesLeft = mines;
        firstClick = true;
        buttonSize = size;
        rowsNum = rows;
        colsNum = cols;
        minesNum = mines;
        this.buttonSize = size;
       

        // ======================High Score======================//
        highScoresLists = new ArrayList<Score>();
        // making UI scalable
        font = new Font("Segoe UI", Font.PLAIN, (int) (buttonSize / 2));
        timeLabel.setFont(font);
        minesLeftJLabel.setFont(font);
        panel.setLayout(new GridLayout(rows, cols));
        panel.setPreferredSize(new Dimension(cols * buttonSize, rows * buttonSize));
        scoreJPanel.setPreferredSize(new Dimension(cols * buttonSize, buttonSize));
        frame.setSize(new Dimension(cols * buttonSize, rows * buttonSize + buttonSize));
        // centering frame
        frame.setLocation(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - frame.getSize().getWidth() / 2),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2
                        - (frame.getSize().getHeight() + 50) / 2));
        // ============================================================================//
        panel.removeAll();
        frame.pack();
        timer.stop();

        face.setIcon(NORMAL_FACE);
        minesLeftJLabel.setText(minesLeft + " remains");
        timeLabel.setText("Time :0.000");
        initButtons(rows, cols, minesNum);

       

    }

    public void initButtons(int r, int c, int num) {
        buttonArray = new Button[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                Button temp = new Button(0, this);
                temp.setPos(i, j);
                temp.setFont(new Font("Segoe UI", Font.PLAIN, (int) (buttonSize * 1.6 / 2)));
                temp.setMargin(new Insets(0, 0, 0, 0));
                temp.setBorderPainted(false);
                temp.setFocusPainted(false);
                temp.setContentAreaFilled(false);
                buttonArray[i][j] = temp;
            }
        }
        initGrid(r, c, num);

    }

    public void initGrid(int r, int c, int num) {
        Random rng = new Random();

        while (num > 0) {
            int randR = rng.nextInt(r);
            int randC = rng.nextInt(c);
            if (buttonArray[randR][randC].getVal() != 9) {
                buttonArray[randR][randC].setVal(9);
                num--;
            }
        }
        fillNum();
    }

    private int numOfNeighbours(int r, int c, int target) {
        int total = 0;
        // Math.max&min is used to prevent the forloops from going out of bound.
        for (int i = Math.max(0, r - 1); i <= Math.min(r + 1, buttonArray.length - 1); i++) {
            for (int j = Math.max(0, c - 1); j <= Math.min(c + 1, buttonArray[0].length - 1); j++) {
                if (buttonArray[i][j].getVal() == target) {
                    total++;
                }
            }
        }
        return total;
    }

    // update the board so that each point has the val of number of bombs around
    public void fillNum() {
        for (int i = 0; i < buttonArray.length; i++) {
            for (int j = 0; j < buttonArray[0].length; j++) {
                buttonArray[i][j].revealed = false;
                buttonArray[i][j].setIcon(TILEICON);
                buttonArray[i][j].setText(null);
                if (buttonArray[i][j].getVal() != 9) {
                    buttonArray[i][j].setVal(numOfNeighbours(i, j, 9));
                }

                panel.add(buttonArray[i][j]);
            }
        }

    }

    public void updateMinesLeft() {
        minesLeftJLabel.setText(minesLeft + " remains");
    }

    // middle/double click behaviour
    public void chord(int row, int col) {
        int flagged = 0;
        // counts how many tiles are flagged in adj tiles
        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, buttonArray.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, buttonArray[0].length - 1); j++) {
                if (buttonArray[i][j].flagged) {
                    flagged++;
                }
            }
        }
        // if number of flagged tiles is the same as the value of the tile that is click
        // on, then reveal all nonflagged tiles
        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, buttonArray.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, buttonArray[0].length - 1); j++) {
                if (!buttonArray[i][j].flagged && !buttonArray[i][j].isRevealed()) {
                    if (flagged == buttonArray[row][col].val && buttonArray[row][col].val != 0
                            && !buttonArray[row][col].flagged && buttonArray[row][col].isRevealed()) {
                        processClick(i, j);
                    } else {
                        buttonArray[i][j].setIcon(TILEICON);
                        face.setIcon(NORMAL_FACE);

                    }
                }
            }
        }

    }

    public void processClick(int row, int col) {

        if (firstClick && buttonArray[row][col].flagged == false) {
            firstClick = false;
            startingTime = System.currentTimeMillis();
            timer.start();
            // counts how many mines is around the 3x3 grid on first click, and then remove
            // them
            int total = 0;
            for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, buttonArray.length - 1); i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, buttonArray[0].length - 1); j++) {
                    if (buttonArray[i][j].getVal() == 9) {
                        total++;
                        buttonArray[i][j].setVal(0);
                        buttonArray[i][j].flagged = false;
                    }
                }
            }
            // places mines back into the grid, but advoiding the initial 3x3 grid
            for (int r = 0; r < buttonArray.length; r++) {
                for (int c = 0; c < buttonArray[0].length; c++) {

                    boolean validPos = true;
                    if (total < 1) {
                        break;
                    }
                    if (r + 1 == row || r - 1 == row || r == row) {
                        if (c + 1 == col || c - 1 == col || c == col) {
                            validPos = false;
                        }
                    }
                    if (buttonArray[r][c].val != 9 && validPos) {
                        buttonArray[r][c].val = 9;
                        total--;
                    }

                }
            }
            // if the previous fill did not compelete putting back mines, then try to fill
            // again, but only advoid the firstclick position itself
            for (int r = 0; r < buttonArray.length; r++) {
                for (int c = 0; c < buttonArray[0].length; c++) {
                    if (total < 1) {
                        break;
                    }
                    if (buttonArray[r][c].val != 9 && !(r == row && c == col)) {
                        buttonArray[r][c].val = 9;
                        total--;

                    }

                }
            }
            fillNum();

        }

        if (gameOn) {

            Button temp = buttonArray[row][col];
            boolean toBeCleared = !temp.revealed;
            if (!temp.flagged) {
                temp.reveal();
                face.setIcon(NORMAL_FACE);

                if (temp.val == 9) {
                    // reveals all mines and ends the game
                    for (int i = 0; i < buttonArray.length; i++) {
                        for (int j = 0; j < buttonArray[0].length; j++) {
                            if (buttonArray[i][j].val == 9 && !buttonArray[i][j].revealed) {
                                buttonArray[i][j].reveal();
                            }
                        }
                    }
                    minesLeft = 0;
                    timer.stop();
                    face.setIcon(DEAD_FACE);
                    gameOn = false;
                }
                // if an empty tile is clicked on, then clear all the adj tiles recursively
                else if (temp.val == 0 && toBeCleared) {
                    temp.setIcon(EMPTY);
                    for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, buttonArray.length - 1); i++) {
                        for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, buttonArray[0].length - 1); j++) {
                            if (!buttonArray[i][j].revealed && !(row == i && col == j)) {
                                processClick(i, j);
                            }
                        }
                    }
                }

            }

            // counts how many tiles are not reveal and are not mines
            int r = 0;
            for (int i = 0; i < buttonArray.length; i++) {
                for (int j = 0; j < buttonArray[0].length; j++) {
                    if (!buttonArray[i][j].revealed && buttonArray[i][j].val != 9) {
                        r++;
                    }
                }
            }
            // if all non mine tiles has been clear, then ends the game
            if (r < 1 && gameOn) {
                for (int i = 0; i < buttonArray.length; i++) {
                    for (int j = 0; j < buttonArray[0].length; j++) {
                        if (buttonArray[i][j].val == 9) {
                            buttonArray[i][j].setIcon(FlAGGEDMINE);
                        }

                    }
                }
                minesLeft = 0;
                minesLeftJLabel.setText(minesLeft + " remains");
                timer.stop();
                face.setIcon(WON);
                gameOn = false;

                inputHighScore();
                loadHighScores();
                showHighScore();
            }
        }

    }

    // check
    private boolean isValidSize(String r, String c, String n) {

        if (r == null || c == null || n == null) {
            return false;
        }

        boolean temp = false;
        // check if r and c are both integers, and 5 < r < 50 and 5 < c < 100 with regex
        boolean validDimension = r.matches("[5-9]|[1-4][0-9]|50") && c.matches("[0-9]|[1-9][0-9]|100");

        if (validDimension) {
            // check if n's text is a integer with regex
            if (n.matches("\\d+")) {
                // if number of mines < rows * columns -1
                if (Integer.parseInt(n) <= (Integer.parseInt(r) * Integer.parseInt(c) - 1)) {
                    temp = true;
                }
            }
        }
        return temp && validDimension;
    }

    void print(Object objToPrint) {
        System.out.println(objToPrint);
    }
}