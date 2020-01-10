package game;

import javax.swing.JButton;
import java.awt.*;

class Button extends JButton {

    private static final long serialVersionUID = 7057789581224567281L;
    public int val;
    public int row;
    public int col;
    public boolean revealed = false;
    public boolean flagged;
    Frame game;

    // picking a color depending its value;
    private Color[] buttonColors = { Color.black, new Color(0, 168, 30), Color.BLUE, Color.magenta, Color.orange,
            Color.PINK, Color.cyan, Color.YELLOW, Color.DARK_GRAY, Color.red };

    public Button(int num, Frame game) {
        val = num;
        this.game = game;
        MouseInput m = new MouseInput(game);
        addMouseListener(m);
    }

    public int getVal() {
        return val;
    }

    public void setVal(int newVal) {
        val = newVal;
    }

    public void setPos(int r, int c) {
        row = r;

        col = c;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void flag() {
        if(!revealed && !flagged && game.minesLeft > 0 && !game.firstClick ){
            flagged = true;
            setIcon(game.FLAGICON);
            game.minesLeft--;
        }
        else if(flagged){
            flagged = false;
            setIcon(game.TILEICON);
            game.minesLeft++;

        }
        game.updateMinesLeft();
    }

    public void reveal() {
        //setting the button's icon/text depending on it's value
        if (val == 9) {
            if (flagged) {
                setIcon(game.FlAGGEDMINE);
            } else {
                setIcon(game.MINEICON);
            }
        } else if (val == 0) {
            setIcon(game.EMPTY);
        } else {
            setText(val + "");
            setIcon(null);
        }

        revealed = true;
        setForeground(buttonColors[val]);
    }

}
