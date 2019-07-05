import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Write a description of class CheßCore here.
 * 
 * @author Jason Drews
 * @version 5.10.17
 */
public class Options extends JPanel implements ActionListener
{
    private static JButton newGame;
    private static JButton compGame;
    private static JButton stop;
    private static JLabel gameOver;
    private static JLabel whosTurn;
    private static JLabel errorMessage;
    private static JLabel moves;
    private static double moveCount = 0;
    private static Timer timer;
    private static int x; //Timer delay (in ms)

    public Options(Color background, int width, int height)
    {
        setBackground(background);
        setPreferredSize(new Dimension(width,height));
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        this.add(Box.createRigidArea(new Dimension(50,25)));

        newGame = new JButton("New Game");
        newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame.setBackground(Color.WHITE);
        newGame.setActionCommand("NEWGAME");
        newGame.addActionListener(this);
        this.add(newGame);

        this.add(Box.createRigidArea(new Dimension(50,25)));

        compGame = CheßCore.getButton();
        compGame = new JButton("CPU vs. CPU");
        compGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        compGame.setBackground(Color.WHITE);
        compGame.setActionCommand("COMPUTER");
        compGame.addActionListener(this);
        CheßCore.setButton(compGame);
        this.add(compGame);

        this.add(Box.createRigidArea(new Dimension(50,5)));

        stop = new JButton("Stop");
        stop.setAlignmentX(Component.CENTER_ALIGNMENT);
        stop.setBackground(Color.WHITE);
        stop.setActionCommand("STOP");
        stop.addActionListener(this);
        this.add(stop);

        /*addTime = CheßCore.getButton();
        addTime = new JButton("+");
        addTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTime.setBackground(Color.WHITE);
        addTime.setActionCommand("TIME+");
        addTime.addActionListener(this);
        this.add(addTime);

        removeTime = CheßCore.getButton();
        removeTime = new JButton("-");
        removeTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeTime.setBackground(Color.WHITE);
        removeTime.setActionCommand("TIME-");
        removeTime.addActionListener(this);
        this.add(removeTime);*/

        this.add(Box.createRigidArea(new Dimension(50,30)));

        whosTurn = new JLabel("White to Play");
        whosTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(whosTurn);

        this.add(Box.createRigidArea(new Dimension(50,100)));

        errorMessage = new JLabel();
        errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(errorMessage);

        this.add(Box.createRigidArea(new Dimension(50,10)));

        gameOver = new JLabel();
        gameOver.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(gameOver);

        this.add(Box.createRigidArea(new Dimension(50,50)));

        moves = new JLabel("Moves Each: " + (int)moveCount);
        moves.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(moves);

        timer = new Timer();
        x = 200;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //g.setColor(Color.BLACK);
        //g.drawString("White's turn",750,250);
    }

    public void actionPerformed(ActionEvent e)
    {
        String act = e.getActionCommand();
        if (act.equals("NEWGAME"))
        {
            timer.cancel();
            CheßCore.startGame(true);            
        }

        if(act.equals("STOP"))
        {
            timer.cancel();
        }

        if(act.equals("COMPUTER"))
        {
            setTimer(x);
            timer = new Timer();
            if(moveCount == 0.0 || e.getID() == 0)
            {                                             
                timer.scheduleAtFixedRate(new TimerTask() 
                    {
                        @Override
                        public void run() {
                            //System.out.println(x);
                            CheßCore.getBoard().actionPerformed(new ActionEvent(compGame,0,"Play"));
                        }
                    }, x, x);                                
            }
            else
                setError("Error: Start new game first.");
        }

        if(act.equals("TIME+"))
        {
            setTimer(x+100);
        }
        if(act.equals("TIME-"))
        {
            setTimer(x-100);
        }
        repaint();
        revalidate();
    }

    public static void setTimer(int delay)
    {
        x = delay;
    }

    public static void changeTurn(boolean isWhiteTurn)
    {
        if(isWhiteTurn)
            whosTurn.setText("White to Play");
        else
            whosTurn.setText("Black to Play");
    }

    public static void setError(String message)
    {
        errorMessage.setText(message);
    }

    public static void justMoved(boolean reset)
    {
        if(reset)
            moveCount = 0;
        else
            moveCount += 0.5; //Gets called twice for each move, two players so 4 * .25 = 1 move each
        moves.setText("Moves Each: " + (int)moveCount);
    }

    public static void gameOver(String result, boolean isWhite)
    {
        if(result.equals("newgame"))
            gameOver.setText("");
        else
        {
            timer.cancel();
            if(result.equals("stalemate"))
                gameOver.setText("Stalemate. It's a draw.");
            else if(result.equals("draw"))
                gameOver.setText("Draw. Insufficient material.");
            if(result.equals("checkmate"))
                if(isWhite)
                    gameOver.setText("Checkmate. Black wins!");
                else
                    gameOver.setText("Checkmate. White wins!");
        }
    }

    public static String askForPromotion()
    {
        return null;
    }
}