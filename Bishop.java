import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Write a description of class Bishop here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bishop extends Piece
{
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\bishop_";
    
    BufferedImage img = null;

    /**
     * Constructor for objects of class Bishop
     */
    public Bishop()
    {
        name = "bishop";
        startingSquares = new int[][]{{2,0},{5,0},{2,7},{5,7}};
        
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
        if(rowChange == colChange)
            if(init.hasDiffColor(fin))  
                return true;
        return false;
    }
}
