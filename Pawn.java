import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Write a description of class Pawn here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pawn extends Piece
{
    private boolean hasMoved;
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\pawn_";

    private static boolean enpAllowed = false;
    private static boolean justEnpassanted = false;
    private static int enp1,enp2;
    private static boolean justPromoted = false;

    BufferedImage img = null;
    /**
     * Constructor for objects of class Pawn
     */
    public Pawn()
    {
        name = "pawn";
        enpAllowed = false;
        justEnpassanted = false;
        justPromoted = false;
        startingSquares = new int[16][2];

        try {
            img = ImageIO.read(new File(imgPath + "white.fw.png"));
        } catch (IOException e) {
        }
        white_pic = new ImageIcon(img);
        try {
            img = ImageIO.read(new File(imgPath + "black.fw.png"));
        } catch (IOException e) {
        }
        black_pic = new ImageIcon(img);
    }

    public boolean isLegal(Square init, Square fin, boolean check)
    {
        int rowChange = init.getRow() - fin.getRow();
        int colChange = init.getCol() - fin.getCol();
        if(init.hasWhite())
        {          
            if(((colChange == 0 && ((rowChange == 1) || (rowChange == 2 && !hasMoved)) && !fin.hasPiece())) || ((rowChange == 1 && Math.abs(colChange) == 1) && (isAttacking(init,fin) || isEnPassanting(init))))
            {
                if(check)
                {
                    hasMoved = true;
                    if(isEnPassanting(init) )
                    {
                        justEnpassanted = true;
                    }
                    if(rowChange == 2)
                    {
                        enpAllowed = true;
                        enp1 = fin.getCol()+1;
                        enp2 = fin.getCol()-1;
                    }
                    else
                        enpAllowed = false;
                    if(fin.getRow() == 0)
                        justPromoted = true;
                }
                return true;
            }
        }
        else
        {
            if(((colChange == 0 && ((rowChange == -1) || (rowChange == -2 && !hasMoved)) && !fin.hasPiece())) || ((rowChange == -1 && Math.abs(colChange) == 1) && (isAttacking(init,fin) || isEnPassanting(init))))
            {
                if(check)
                {
                    hasMoved = true;
                    if(isEnPassanting(init))
                    {
                        justEnpassanted = true;
                    }
                    if(rowChange == -2)
                    {
                        enpAllowed = true;
                        enp1 = fin.getCol()+1;
                        enp2 = fin.getCol()-1;
                    }
                    else
                        enpAllowed = false;
                    if(fin.getRow() == 7)
                        justPromoted = true;
                }
                return true;
            }
        }

        return false;
    }

    private static boolean isAttacking(Square one, Square two)
    {
        if(one.hasWhite())
        {
            if(two.hasPiece() && !two.hasWhite())
                return true;
        }
        else
        {
            if(two.hasPiece() && two.hasWhite())
                return true;
        }
        return false;
    }

    private static boolean isEnPassanting(Square one)
    {
        if(one.hasWhite())
        {
            if((enpAllowed && (one.getCol() == enp1 || one.getCol() == enp2)) && one.getRow() == 3)
                return true;
        }
        else
        {
            if((enpAllowed && (one.getCol() == enp1 || one.getCol() == enp2)) && one.getRow() == 4)
                return true;
        }
        return false;
    }

    public static boolean justEnpassanted()
    {
        return justEnpassanted;
    }

    public static void setEnpassanted()
    {
        justEnpassanted = false;
    }

    public static boolean justPromoted()
    {
        return justPromoted;
    }

    public static void setPromoted()
    {
        justPromoted = false;
    }
}
