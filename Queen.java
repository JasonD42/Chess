import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Write a description of class Queen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Queen extends Piece
{
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\queen_";
    
    BufferedImage img = null;

    /**
     * Constructor for objects of class Queen
     */
    public Queen()
    {
        name = "queen";
        startingSquares = new int[][]{{3,0},{3,7}};
        
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
        if((rowChange == 0 || colChange == 0) || (rowChange == colChange))
            if(init.hasDiffColor(fin))  
                return true;
        return false;
    }
}
