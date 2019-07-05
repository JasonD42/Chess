import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Creating King Pieces
 */
public class King extends Piece
{
    private static boolean canCastle = true;
    private static boolean justCastled = false;
    private static Square kingPosW;
    private static Square kingPosB;
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\king_";
    BufferedImage img = null;

    /**
     * Constructor for objects of class King
     */
    public King()
    {
        name = "king";
        canCastle = true;
        justCastled = false;
        startingSquares = new int[][]{{4,0},{4,7}};

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
        int rowChange = Math.abs(init.getRow() - fin.getRow());
        int colChange = Math.abs(init.getCol() - fin.getCol());
        if((rowChange <= 1 && colChange <= 1) || ((colChange == 2 && rowChange == 0) && Board.canCastle(fin)) && !Board.isInCheck())
        {
            if(colChange == 2 && check)
                justCastled = true;
            if(init.hasDiffColor(fin))  
            {
                canCastle = false;
                return true;
            }
        }
        return false;
    }

    public boolean canCastle()
    {
        return canCastle;
    }

    public static boolean justCastled()
    {
        return justCastled;
    }
    public static void setCastled()
    {
        justCastled = false;
    }
    
    public static void setKingPos(Square fin, boolean isWhite)
    {
        if(isWhite)
            kingPosW = fin;
        else
            kingPosB = fin;
    }
    public static Square getWhiteKing()
    {
        return kingPosW;
    }
    public static Square getBlackKing()
    {
        return kingPosB;
    }
}
