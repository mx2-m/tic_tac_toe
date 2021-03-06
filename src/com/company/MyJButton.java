package com.company;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyJButton extends JButton implements MouseListener {


    private TicTacToe game;
    int row, col, flag;

    public MyJButton(int row, int col, TicTacToe game) {
        this.row = row; this.col = col; this.game = game;
        flag = 0;
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        if (this.isEnabled()) {
            game.buttonClicked(this);
            this.setEnabled(false);
            int response = game.evaluate(game.gameState,0);
            if (response != 0) {
                game.gameOver(response);
            }
            if (game.total == 9) {
                game.gameOver(0);
            }
            if (game.turn == 1) {
                game.computerTurn();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }



}
