import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
/**
 * Write a description of class Square here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Square extends JButton
{    
    private JButton space;
    private int row, col;
    private boolean hasPiece;
    private boolean hasWhitePiece;
    private boolean hasDot;
    private boolean isClicked = false;
    private Piece piece;
    private ArrayList<Square> moves;

    public Square(int r, int c)
    {
        space = new JButton();
        row = r;
        col = c;
        hasPiece = false;
        moves = new ArrayList<Square>();
    }
    
    public Square(int r, int c, Piece piece, int color)
    {
        super(piece.getImage(color));
        row = r;
        col = c;
        hasPiece = true;
        this.piece = piece;
        if(color == 1)
            hasWhitePiece = true;
        else
            hasWhitePiece = false;
        moves = new ArrayList<Square>();
    }
    
    public Square(int r, int c, boolean hasDot)
    {
        space = new JButton();
        this.hasDot = hasDot;
        row = r;
        col = c;
        hasPiece = false;
        moves = new ArrayList<Square>();
    }
    
    public void addMove(Square fin)
    {
        moves.add(fin);
    }
    public void removeMoves()
    {
        moves.clear();
    }
    public ArrayList<Square> getMoves()
    {
        return moves;
    }
    
    public Piece getPiece()
    {
        return piece;
    }
    
    public boolean hasKing()
    {
        if(hasPiece())
            return piece.getName().equals("king");
        return false;
    }
    public boolean hasRook()
    {
        if(hasPiece())
            return piece.getName().equals("rook");
        return false;
    }
    public boolean hasQueen()
    {
        if(hasPiece())
            return piece.getName().equals("queen");
        return false;
    }
    public boolean hasBishop()
    {
        if(hasPiece())
            return piece.getName().equals("bishop");
        return false;
    }
    
    public int getRow()
    {
        return row;
    }
    public int getCol()
    {
        return col;
    }
    
    public boolean hasDiffColor(Square fin)
    {
        if((hasWhitePiece && !fin.hasWhite()) || (!hasWhitePiece && (fin.hasWhite() || !fin.hasPiece())))
            return true;
        return false;
    }
    public boolean isRightColor(boolean whiteTurn)
    {
        if(whiteTurn)
            return hasWhitePiece;
        else
            return !hasWhitePiece;
    }
    
    public boolean hasWhite()
    {
        return hasWhitePiece;
    }
    
    public boolean hasPiece()
    {
        return hasPiece;
    }
    
    public void click()
    {
        isClicked = !isClicked;
    }
    
    public boolean isClicked()
    {
        return isClicked;
    }
}
