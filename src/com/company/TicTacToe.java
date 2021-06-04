package com.company;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class TicTacToe extends JFrame implements ActionListener {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TicTacToe();
            }
        });
    }

    private MyJButton[][] button;
    public int[][] gameState;
    private JPanel gameBoard = new JPanel();
    private JLabel whoseTurnLabel;
    private JComboBox depthCB;

    int turn, total = 0;
    String player1Name = "Player 1";
    String player2Name = "Computer";
    private String player1Symbol = "X";
    private String player2Symbol = "O";
    int depth;


    public TicTacToe() {
        super("Tic Tac Toe");
        setVisible(true);
        setSize(500, 550);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initBoard();
        startPlay();
        setLocationRelativeTo(null);
    }

    private void initBoard() {

        total = 0;
        setLayout(new BorderLayout());
        gameBoard.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(gameBoard, BorderLayout.CENTER);
        gameBoard.setLayout(new GridLayout(3, 3));
        button = new MyJButton[3][3];
        gameState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                button[i][j] = new MyJButton(i, j, this);
                gameState[i][j] = 0;
                gameBoard.add(button[i][j]);
            }
        }
        whoseTurnLabel = new JLabel();
        whoseTurnLabel.setBorder(new EmptyBorder(0, 20, 0, 20));
        add(whoseTurnLabel, BorderLayout.SOUTH);
        JPanel controls = new JPanel();
        depthCB = new JComboBox();
        depthCB.addActionListener(comb);
        depthCB.addItem(3);
        depthCB.addItem(6);
        depthCB.addItem(9);
        depthCB.addItem(12);
        JLabel d = new JLabel("Depth: ");
        controls.add(d);
        controls.add(depthCB);
        add(controls, BorderLayout.NORTH);


    }

    ActionListener comb = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            depth = (int) depthCB.getSelectedItem();
        }
    };

    private void startPlay() {
        if (turn == 0) {
            player1Symbol = "X";
            player2Symbol = "O";
            whoseTurnLabel.setText(player1Name + "'s Turn" + " (" + player1Symbol + ")");
        } else {
            player2Symbol = "X";
            player1Symbol = "O";
            whoseTurnLabel.setText(player2Name + "'s Turn**" + " (" + player2Symbol + ")");
            computerTurn();


        }
    }

    public void buttonClicked(MyJButton b) {
        total++;
        if (turn == 0) {
            b.setText(player1Symbol);
            gameState[b.row][b.col] = 1;
            b.flag = 1;
            whoseTurnLabel.setText(player2Name + "'s Turn*" + " (" + player2Symbol + ")");
            System.out.println("computers turn" + turn);
            ;
        } else {
            b.setText(player2Symbol);
            gameState[b.row][b.col] = -1;
            b.flag = -1;
            whoseTurnLabel.setText(player1Name + "'s Turn" + " (" + player1Symbol + ")");
        }
        turn = 1 - turn;
    }


    public void gameOver(int response) {
        switch (response) {
            case -1:
                wonMsg(player2Name + " Wins");
                break;
            case 1:
                wonMsg(player1Name + " Wins");
                break;
            default:
                wonMsg("It's a Tie");
                break;
        }
    }

    public void wonMsg(String msg) {
        whoseTurnLabel.setText(msg);
        JOptionPane.showMessageDialog(this, msg);
        if (player1Symbol == "X") {
            turn = 0;
        } else {
            turn = 1;
        }
        restartGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub


    }

    private void restartGame() {
        gameBoard.removeAll();
        remove(whoseTurnLabel);
        initBoard();
        startPlay();
    }

    public void computerTurn() {
        int depth1 = depth, alpha = -1000, beta = 1000;

        ResultSet result = minimax(gameState, depth1, alpha, beta, true);
        System.out.println(result.step.y + "result" + result.step.x + " " + result.value);

        if (gameState[1][1] == 0)
            button[1][1].mouseClicked(null);

       /* else if (result.value==-1000) {
            for (int i = 0; i < gameState.length; i++) {
                for (int j = 0; j < gameState.length; j++) {
                    if (gameState[i][j] == 0)
                        button[i][j].mouseClicked(null);

                }
            }
        }*/
        else {

            button[result.step.x][result.step.y].mouseClicked(null);
        }

        /*for (int i = 0; i < gameState.length; i++) {
            System.out.println("gs1  " + gameState[i][0]);
            System.out.println("gs2   " + gameState[i][1]);
            System.out.println("gs3   " + gameState[i][2]);

        }*/


    }


    private ResultSet minimax(int[][] gameState, int depth, int alpha, int beta,
                              boolean maxplayer) {

        ResultSet result = new ResultSet(evaluate(gameState, depth));
        //System.out.println("value" + result.value);

        if (result.value != 0) {// win/lose
            return result;                     // best minimax value and next move
        }
        if (depth == 0) {
            result.value = 0;
            return result;
        }
        if (maxplayer) {
            result.value = -1000;
            //System.out.println("maxx");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    if (gameState[i][j] == 0) {
                        gameState[i][j] = 1;
                        ResultSet childresult = minimax(gameState, depth - 1, alpha, beta, false);
                        if (childresult.value > result.value) {
                            result.value = childresult.value;
                            result.step.setLocation(i, j);
                        }
                        alpha = Math.max(alpha, result.value);
                        gameState[i][j] = 0;
                        if (alpha >= beta) {
                            break;
                        }
                    }
                }
            }
        } else {
            result.value = 1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (gameState[i][j] == 0) {
                        gameState[i][j] = -1;
                        ResultSet childresult = minimax(gameState, depth - 1, alpha, beta, true);
                        if (childresult.value < result.value) {
                            result.value = childresult.value;
                            result.step.setLocation(i, j);
                        }
                        beta = Math.min(beta, result.value);
                        gameState[i][j] = 0;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public int evaluate(int b[][], int depth) {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
                if (b[row][0] == 1)
                    return 1 + depth;
                else if (b[row][0] == -1)
                    return -1 - depth;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
                if (b[0][col] == 1)
                    return 1 + depth;
                else if (b[0][col] == -1)
                    return -1 - depth;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0] == 1)
                return 1 + depth;
            else if (b[0][0] == -1)
                return -1 - depth;
        }
        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2] == 1)
                return 1 + depth;
            else if (b[0][2] == -1)
                return -1 - depth;
        }

        // Else if none of them have won then return 0
        return 0;
    }


}

class ResultSet {

    public int value;
    public Point step;

    public ResultSet(int value) {
        this.value = value;
        step = new Point();
    }

}

