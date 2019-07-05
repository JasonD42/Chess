import javax.swing.*;
import java.awt.*;
/**
 * Abstract Class to Create Chess Pieces
 * 
 * @author Jason Drews
 * @version 05.02.17
 */
public abstract class Piece
{
    protected int[][] startingSquares;
    protected boolean isWhite;
    protected String name;
    protected Square loc;
    protected ImageIcon white_pic, black_pic;
    protected final String imageDrive = "C:\\Users\\JayD_\\Desktop\\Cheß_Old";

    public ImageIcon getImage(int col)
    {
        if (col == 1)
            return white_pic;
        else
            return black_pic;
    }

    public void moveTo(Square spot)
    {

    }
    
    public void allMoves()
    {
        
    }

    public String getPieceType(int i, int j)
    {
        String temp = "";
        if (i == 0 || i == 7)
        {
            if (j == 0 || j == 7)
                temp = "rook";
            else if (j == 1 || j == 6)
                temp = "knight";
            else if (j == 2 || j == 5)
                temp = "bishop";
            else if (j == 3)
                temp = "queen";
            else if (j == 4)
                temp = "king";
            else
                temp = null;
        }       
        else
            temp = "pawn";

        if (i < 2)
            return temp + "_black";
        else
            return temp + "_white";
    }

    public abstract boolean isLegal(Square init, Square fin, boolean check);
    
    public String getName()
    {
        return name;
    }
}
