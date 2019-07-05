import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Write a description of class Rook here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rook extends Piece
{
    private static boolean canCastle = true;
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\rook_";
    
    BufferedImage img = null;

    /**
     * Constructor for objects of class Rook
     */
    public Rook()
    {
        name = "rook";
        canCastle = true;
        startingSquares = new int[][]{{0,0},{7,0},{0,7},{7,7}};
        
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
        if((rowChange == 0 || colChange == 0))
            if(init.hasDiffColor(fin)) 
            {
                if(check)
                    canCastle = false;
                return true;
            }
        return false;
    }
    
    public boolean hasMoved()
    {
        return !canCastle;
    }
}
