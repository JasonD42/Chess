import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * Write a description of class CheßCore here.
 * 
 * @author Jason Drews
 * @version 5.10.17
 */
public class CheßCore extends JFrame
{
    private static Board board;
    private static Options ops;
    private static Color backColor = new Color(200,200,200);
    private static JFrame theGUI;
    private static JButton compGame;

    public static void main (String[] args)
    {
        theGUI = new JFrame("Cheß");

        theGUI.setSize(800,550);
        theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
        theGUI.setLayout(new BorderLayout());      

        startGame(false);
    }

    public static void startGame(boolean reset)
    {
        Container pane = theGUI.getContentPane();
        if(!reset)
        {
            ops = new Options(Color.WHITE,200,500);
            board = new Board(backColor, 8,8);
            
            pane.add(board, BorderLayout.CENTER);
            pane.add(ops, BorderLayout.EAST);
        }
        else
        {
            pane.remove(board);
            Board board2 = new Board(backColor, 8,8);
            board = board2;
            pane.add(board, BorderLayout.CENTER);
        }
        theGUI.setVisible(true);
    }
    
    public static Board getBoard()
    {
        return board;
    }
    public static Options getOptions()
    {
        return ops;
    }
    public static JButton getButton()
    {
        return compGame;
    }
    public static void setButton(JButton daButton)
    {
        compGame = daButton;
    }
}

