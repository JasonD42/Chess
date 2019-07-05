import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * Write a description of class Knight here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Knight extends Piece
{
    String imgPath = imageDrive + "\\Cheß\\Piece_pics\\knight_";
    
    BufferedImage img = null;

    /**
     * Constructor for objects of class Knight
     */
    public Knight()
    {
       name = "knight";
        startingSquares = new int[][]{{1,0},{6,0},{1,7},{6,7}};
       
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
        if((rowChange == 2 && colChange == 1) || (rowChange == 1 && colChange == 2))
            if(init.hasDiffColor(fin))  
                return true;
        return false;
    }
}
