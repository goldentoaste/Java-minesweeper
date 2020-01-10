package game;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

public class MouseInput implements MouseListener {

    Frame game;
    boolean isChording;
 

    public MouseInput(Frame game) {
        this.game = game;
        isChording = false;
     
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    //applying the "cliked effects" to tiles on click
    @Override
    public void mousePressed(MouseEvent e) {

        Button temp = (Button)(e.getSource());

        if (game.gameOn) {
            //chording
            isChording = left(e) && right(e) || mid(e);
            if (isChording) {
                //applying the red "on-click" effect to buttons
                for (int i = Math.max(0, temp.row - 1); i <= Math.min(temp.row + 1, game.buttonArray.length - 1); i++) {
                    for (int j = Math.max(0, temp.col - 1); j <= Math.min(temp.col + 1,
                            game.buttonArray[0].length - 1); j++) {
                        if (!game.buttonArray[i][j].flagged && !game.buttonArray[i][j].isRevealed()) {

                            game.buttonArray[i][j].setIcon(game.CLICKED);
                            game.face.setIcon(game.CLICKED_FACE);
                        }
                    }
                }

            }
            //left click on empty tiles
            else if (left(e) && !temp.isRevealed() && !temp.flagged) {
                temp.setIcon(game.CLICKED);
                game.face.setIcon(game.CLICKED_FACE);
            }
        }

    }


    //process inputs on release
    @Override
    public void mouseReleased(MouseEvent e) {
        Button temp = (Button) (e.getSource());
        
        if (game.gameOn) {
            //chording
            if(isChording){
                game.chord(temp.row, temp.col);

            }
            else if(right(e)){
                temp.flag();
            }
            else if(left(e)){
                game.processClick(temp.row, temp.col);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    boolean left(MouseEvent e) {
        return SwingUtilities.isLeftMouseButton(e);
    }

    boolean right(MouseEvent e) {
        return SwingUtilities.isRightMouseButton(e);
    }

    boolean mid(MouseEvent e) {
        return SwingUtilities.isMiddleMouseButton(e);
    }
    void print(Object objToPrint) {
        System.out.println(objToPrint);
    }
}