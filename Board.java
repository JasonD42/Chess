import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board extends JPanel implements ActionListener
{
    private static Square[][] squares;
    private char[] rows = {'a','b','c','d','e','f','g','h'};
    private int[] cols = {1,2,3,4,5,6,7,8};
    private static int wob = 1;
    private static Color[] bw = new Color[2];
    private int clickX, clickY;
    private static boolean isWhiteTurn;
    private static boolean wCastleKing,bCastleKing,wCastleQueen,bCastleQueen;
    private static boolean check;
    private static Square checkSource;
    private static Square tempSquI, tempSquF, tempSqu;
    private static ArrayList<Square> squaresWRP, squaresWOP; //with right/other pieces
    private static ArrayList<Square> allMoves;
    private static ArrayList<Square> moves;
    private static Random gen;
    private static int rand, rand2;

    public Board(Color backColor, int x, int y)
    {
        setBackground(backColor);
        setLayout(new GridLayout(x,y));

        squares = new Square[x][y];
        //bw[0] = new Color(100,100,100);
        bw[1] = new Color(255,255,240);
        bw[0] = new Color(100,80,115);

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if (i < 2)
                    makePiece(i,j,2);
                else if (i > 5)
                    makePiece(i,j,1);
                else
                    squares[i][j] = new Square(i,j);
                JButton square = squares[i][j];

                square.setBackground(bw[wob]);
                square.addActionListener(this);
                square.setActionCommand("" + i + j);
                this.add(square);
                swapColor();
            }
            swapColor();
        }
        isWhiteTurn = true;
        wCastleKing = true; wCastleQueen = true;
        bCastleKing = true; bCastleQueen = true;
        check = false;
        checkSource = null;
        Options.changeTurn(isWhiteTurn);
        Options.setError("");
        Options.gameOver("newgame",true);
        Options.justMoved(true);
        //Options.setTimer(0);
        King.setKingPos(squares[7][4],isWhiteTurn);
        King.setKingPos(squares[0][4],!isWhiteTurn);
        gen = new Random();

        squaresWRP = new ArrayList<Square>();
        squaresWOP = new ArrayList<Square>();
        allMoves = new ArrayList<Square>();
        populateMoves(isWhiteTurn);
    }

    private static void swapColor()
    {
        if (wob == 0)
            wob = 1;
        else
            wob = 0;
    }

    public void populateMoves(boolean turn)
    {
        squaresWRP.clear();
        squaresWOP.clear();
        allMoves.clear();
        //Find all squares with pieces
        for(Square[] row : squares)
            for(Square squ : row)
            {
                if(squ.hasPiece() && squ.isRightColor(turn))
                    squaresWRP.add(squ);
                if(squ.hasPiece() && !squ.isRightColor(turn))
                    squaresWOP.add(squ);
                squ.removeMoves();
            }

        if(squaresWRP.size() == 1 && squaresWOP.size() == 1)
        {
            Options.gameOver("draw",isWhiteTurn);
        }

        Square king;            
        if(turn)
            king = King.getWhiteKing();
        else
            king = King.getBlackKing();
        for(Square squ1 : squaresWOP)
        {
            if(moveIsLegal(squ1,king,squ1.getPiece(),false)) //Searches for check
            {
                checkSource = squ1;
                check = true;
            }
        }
        //Find all legal moves for each of those squares
        for(Square[] row : squares)
            for(Square squ2 : row)
            {
                for(Square squ1 : squaresWRP)
                {
                    if(moveIsLegal(squ1,squ2,squ1.getPiece(),false) && willNotBeInCheck(squ1,squ2,turn) && stopsCheck(squ1,squ2,king,turn))
                    {
                        squ1.addMove(squ2);
                        //System.out.println(squ1.getRow() + "," + squ1.getCol() + "  " + squ2.getRow() + "," + squ2.getCol());
                        allMoves.add(squ1);
                        allMoves.add(squ2);

                    }
                }
            }
        if(allMoves.isEmpty())
        {
            if(check)
                Options.gameOver("checkmate",isWhiteTurn);
            else
                Options.gameOver("stalemate",isWhiteTurn);
        }
        //System.out.println();
        resetConditions();
    }    

    public void actionPerformed(ActionEvent e)
    {       
        int x, y, r2, c2;
        Square squ;
        boolean compMove;
        if(allMoves.isEmpty())
            return;
        if(e.getSource() == CheßCore.getButton())
        {
            int count = 0;
            if(e.getID() == 0)
            {
                while(count == 0)
                {
                    rand = gen.nextInt(squaresWRP.size());
                    moves = squaresWRP.get(rand).getMoves();
                    if(moves.size() != 0)
                    {
                        count = moves.size();
                        rand2 = gen.nextInt(moves.size());
                        tempSqu = squaresWRP.get(rand);
                    }
                }
            }
            else
                tempSqu = moves.get(rand2);
            x = tempSqu.getRow();
            y = tempSqu.getCol();
            compMove = true;
        }
        else    
        {
            String act = e.getActionCommand();
            x = Integer.parseInt(act.substring(0,1));
            y = Integer.parseInt(act.substring(1,2));
            compMove = false;
        }
        squ = squares[x][y];
        r2 = squ.getRow();
        c2 = squ.getCol();

        checkConditions();
        if(!boardClicked() && (squ.hasPiece() && isWhiteTurn(squ)))
        {
            squ.setBackground(new Color(0, 172, 230));
            squ.click();
            clickX = r2;
            clickY = c2;
            if(!compMove)
            {
                for(Square squ2: squ.getMoves())
                {
                    squ2.setBackground(new Color(128, 223, 255));                   
                }
            }
        }
        else
        {
            if(squ.isClicked())
            {
                squ.setBackground(bw[squColor(x,y)]);
                squ.click();  
                if(!compMove)
                    for(Square squ2: squ.getMoves())
                    {
                        squ2.setBackground(bw[squColor(squ2.getRow(),squ2.getCol())]);                   
                    }
            }
            else if(boardClicked() && moveInList(squares[clickX][clickY],squ))
            {
                for(Square squ1: squares[clickX][clickY].getMoves())
                {
                    squ1.setBackground(bw[squColor(squ1.getRow(),squ1.getCol())]);                   
                }

                moveIsLegal(squares[clickX][clickY],squ,squares[clickX][clickY].getPiece(),true);
                int color;
                if(squares[clickX][clickY].hasWhite())
                    color = 1;
                else
                    color = 2;
                tempSquF = squares[r2][c2];

                squares[r2][c2] = new Square(r2,c2,squares[clickX][clickY].getPiece(),color);
                Square square = squares[r2][c2];
                square.setBackground(bw[squColor(r2,c2)]);
                square.addActionListener(this);
                square.setActionCommand("" + r2 + c2);               
                this.add(square,(c2 + (r2*8)));
                this.remove((c2+1) + (r2*8));

                int r1 = clickX;
                int c1 = clickY;
                tempSquI = squares[r1][c1];
                squares[r1][c1] = new Square(r1,c1);
                Square square2 = squares[r1][c1];
                square2.setBackground(bw[squColor(r1,c1)]);
                square2.addActionListener(this);
                square2.setActionCommand("" + r1 + c1);                
                this.add(square2,(c1 + (r1*8)));
                this.remove((c1+1) + (r1*8));

                if(square.hasKing())
                    King.setKingPos(square,isWhiteTurn);
                if(King.justCastled())
                {
                    System.out.println("Castled!");
                    int mod;
                    if(c2 == 6)
                    {
                        c2 = 7;
                        mod = -2;
                    }
                    else
                    { 
                        c2 = 0;
                        mod = 3;
                    }
                    squares[r2][c2+mod] = new Square(r2,c2+mod,squares[r2][c2].getPiece(),color);
                    square = squares[r2][c2+mod];
                    square.setBackground(bw[squColor(r2,c2+mod)]);
                    square.addActionListener(this);
                    square.setActionCommand("" + r2 + (c2+mod));               
                    this.add(square,((c2+mod) + (r2*8)));
                    this.remove((c2+mod+1) + (r2*8));
                    squares[r2][c2] = new Square(r2,c2);
                    square2 = squares[r2][c2];
                    square2.setBackground(bw[squColor(r2,c2)]);
                    square2.addActionListener(this);
                    square2.setActionCommand("" + r2 + c2);                
                    this.add(square2,(c2 + (r2*8)));
                    this.remove((c2+1) + (r2*8));  

                    King.setCastled();
                }
                else if(Pawn.justEnpassanted())
                {
                    int mod;
                    if(square.hasWhite())
                        mod = 1;
                    else
                        mod = -1;

                    squares[r2+mod][c2] = new Square(r2+mod,c2);
                    square = squares[r2+mod][c2];
                    square.setBackground(bw[squColor(r2+mod,c2)]);
                    square.addActionListener(this);
                    square.setActionCommand("" + (r2+mod) + (c2));               
                    this.add(square,((c2) + (r2+mod)*8));
                    this.remove((c2+1) + (r2+mod)*8);

                    Pawn.setEnpassanted();
                }
                else if(Pawn.justPromoted())
                {                       
                    squares[r2][c2] = new Square(r2,c2,new Queen(),color);
                    square = squares[r2][c2];
                    square.setBackground(bw[squColor(r2,c2)]);
                    square.addActionListener(this);
                    square.setActionCommand("" + (r2) + (c2));               
                    this.add(square,((c2) + (r2)*8));
                    this.remove((c2+1) + (r2)*8);

                    Pawn.setPromoted();
                }                                
                //revalidate();
                //repaint();
                isWhiteTurn = !isWhiteTurn;
                check = false;
                checkSource = null;
                populateMoves(isWhiteTurn);
                Options.changeTurn(isWhiteTurn);
                Options.setError("");
                Options.justMoved(false);

            }
            else
            {
                Options.setError("Illegal Move");
            }
        }

        revalidate();
        repaint();

        if(e.getID() == 0)
            actionPerformed(new ActionEvent(CheßCore.getButton(), 1, "Keep_Plain"));
        //else if(e.getID() == 1)
        //    CheßCore.getOptions().actionPerformed(new ActionEvent(CheßCore.getButton(),0,"COMPUTER"));
    }

    private static boolean moveInList(Square one, Square two)
    {
        int oneR = one.getRow(), oneC = one.getCol();
        int twoR = two.getRow(), twoC = two.getCol();
        Square init;
        for(int i = 0; i < allMoves.size(); i = i+2)
        {
            init = allMoves.get(i);
            if(oneR == init.getRow() && oneC == init.getCol())
            {
                Square fin = allMoves.get(i+1);
                if(twoR == fin.getRow() && twoC == fin.getCol())
                    return true;
            }
        }
        return false;
    }

    private static boolean moveIsLegal(Square init, Square fin, Piece piece, boolean check)
    {
        return ((piece.isLegal(init,fin,check)) && (noBlock(init,fin) || piece.getName().equals("knight")));
    }

    private static boolean stillInCheck(Piece pieceMoved)
    {
        Square king;        
        if(isWhiteTurn)
        {
            king = King.getWhiteKing();
            if(squares[checkSource.getRow()][checkSource.getCol()].hasWhite())
                return false;
        }
        else
        {
            king = King.getBlackKing();
            if(!squares[checkSource.getRow()][checkSource.getCol()].hasWhite())
                return false;
        }

        return (moveIsLegal(checkSource, king, checkSource.getPiece(), false));
    }

    private static void checkConditions()
    {
        if(!squares[7][4].hasKing() || !squares[7][7].hasRook())
            wCastleKing = false;
        if(!squares[7][4].hasKing() || !squares[7][0].hasRook())
            wCastleQueen = false;
        if(!squares[0][4].hasKing() || !squares[0][7].hasRook())
            bCastleKing = false;
        if(!squares[0][4].hasKing() || !squares[0][0].hasRook())
            bCastleQueen = false;
    }
    
    public static boolean isInCheck()
    {
        return check;
    }

    public static boolean canCastle(Square fin)
    {
        if(fin.getCol() == 6 && fin.getRow() == 7)
            return wCastleKing;
        else if(fin.getCol() == 2 && fin.getRow() == 7)
            return wCastleQueen;
        else if(fin.getCol() == 6 && fin.getRow() == 0)
            return bCastleKing;
        else if(fin.getCol() == 2 && fin.getRow() == 0)
            return bCastleQueen;
        return false;
    }

    private static void resetConditions()
    {
        King.setCastled();
        Pawn.setEnpassanted();
        Pawn.setPromoted();
    }

    private boolean boardClicked()
    {
        for(Square[] row : squares)
        {
            for(Square squ: row)
            {
                if(squ.isClicked())
                {
                    return true;
                }
            }            
        }
        return false;
    }

    private static Square otherKing(boolean isWhite)
    {
        if(isWhite)
            return King.getWhiteKing();
        else
            return King.getBlackKing();
    }

    public static boolean noBlock(Square init, Square fin)
    {
        int rowChange = Math.abs(init.getRow() - fin.getRow());
        int colChange = Math.abs(init.getCol() - fin.getCol());
        if(rowChange == 0 && colChange != 0)
        {
            for(int i = Math.min(init.getCol(),fin.getCol())+1 ; i < Math.max(init.getCol(),fin.getCol()) ; i++)
            {
                if(squares[init.getRow()][i].hasPiece())
                    return false;
            }
            return true;
        }
        else if(colChange == 0 && rowChange != 0)
        {
            for(int i = Math.min(init.getRow(),fin.getRow())+1 ; i < Math.max(init.getRow(),fin.getRow()) ; i++)
            {
                if(squares[i][init.getCol()].hasPiece())
                    return false;
            }
            return true;
        }
        else if(colChange == rowChange) 
        {
            return bishop(init,fin);
        }
        return true;
    }

    private static boolean bishop(Square init,Square fin)
    {              
        int temp = 1, mod = -1;
        int rMin = Math.min(init.getRow(),fin.getRow());
        int rMax = Math.max(init.getRow(),fin.getRow());
        int cMin = Math.min(init.getCol(),fin.getCol());
        int cMax = Math.max(init.getCol(),fin.getCol());

        boolean goingDown = (init.getRow() - fin.getRow() < 0);
        boolean goingRight = (init.getCol() - fin.getCol() < 0);
        if(goingDown && goingRight)
        {
            for(int i = rMin+1 ; i < rMax ; i++)
            {
                if(squares[rMin+temp][cMin+temp].hasPiece())
                    return false;
                temp++;
            }
        }
        else if(goingDown && !goingRight)
        {
            for(int i = rMin+1 ; i < rMax ; i++)
            {
                if(squares[rMin+temp][cMax-temp].hasPiece())
                    return false;
                temp++;
            }
        }
        else if(!goingDown && goingRight)
        {
            for(int i = rMax-1 ; i > rMin ; i--)
            {
                if(squares[rMax-temp][cMin+temp].hasPiece())
                    return false;
                temp++;
            }
        }
        else //going up and left
        {
            for(int i = rMax-1 ; i > rMin ; i--)
            {
                if(squares[rMax-temp][cMax-temp].hasPiece())
                    return false;
                temp++;
            }
        }
        return true;
    }

    private static boolean stopsCheck(Square init, Square fin, Square king, boolean isWhiteTurn)
    {
        boolean ok = false;
        if(checkSource == null || (fin.getRow() == checkSource.getRow() && fin.getCol() == checkSource.getCol()))
            return true;
        else
        {
            Square tempSqu1, tempSqu2;
            tempSqu2 = fin;
            tempSqu1 = init;
            int tempCol = 2;
            if(isWhiteTurn)
                tempCol = 1;
            squares[fin.getRow()][fin.getCol()] = new Square(fin.getRow(),fin.getCol(),init.getPiece(),tempCol);
            squares[init.getRow()][init.getCol()] = new Square(init.getRow(),init.getCol());
            
            if(tempSqu1.hasKing())
                ok = !moveIsLegal(checkSource,squares[fin.getRow()][fin.getCol()],checkSource.getPiece(),false);
            else if(!moveIsLegal(checkSource,king,checkSource.getPiece(),false))
                ok = true;
            squares[fin.getRow()][fin.getCol()] = tempSqu2;
            squares[init.getRow()][init.getCol()] = tempSqu1;
            return ok;
        }
    }

    private static boolean willNotBeInCheck(Square init, Square fin, boolean isWhiteTurn)
    {
        Square king, tempSqu1, tempSqu2;
        if(checkSource != null || init.hasKing())
        {
            if(init.hasKing())
            {
                tempSqu2 = fin;
                tempSqu1 = init;
                int tempCol = 2;
                if(isWhiteTurn)
                    tempCol = 1;
                squares[fin.getRow()][fin.getCol()] = new Square(fin.getRow(),fin.getCol(),init.getPiece(),tempCol);
                squares[init.getRow()][init.getCol()] = new Square(init.getRow(),init.getCol());
                for(Square squ1 : squaresWOP)
                {
                    if(squ1.getRow() != squares[fin.getRow()][fin.getCol()].getRow() || squ1.getCol() != squares[fin.getRow()][fin.getCol()].getCol())
                        if(moveIsLegal(squ1,squares[fin.getRow()][fin.getCol()],squ1.getPiece(),false))
                        {
                            squares[fin.getRow()][fin.getCol()] = tempSqu2; squares[init.getRow()][init.getCol()] = tempSqu1;
                            return false;
                        }
                }
                squares[fin.getRow()][fin.getCol()] = tempSqu2; squares[init.getRow()][init.getCol()] = tempSqu1;
            }
            return true;
        }
        if(isWhiteTurn)
            king = King.getWhiteKing();
        else
            king = King.getBlackKing();

        if((init.getCol() == king.getCol() && fin.getCol() != init.getCol()))           
            return checkVertCheck(init,king,isWhiteTurn);
        else if((init.getRow() == king.getRow() && fin.getRow() != init.getRow()))
            return checkHorizCheck(init,king,isWhiteTurn);
        else if((king.getCol()-king.getRow()) == (init.getCol()-init.getRow()))
            return checkNegDiagCheck(init,king,isWhiteTurn);
        else if((king.getCol()+king.getRow()) == (init.getCol()+init.getRow()))
            return checkPosDiagCheck(init,king,isWhiteTurn);

        return true;
    }   

    private static boolean checkNegDiagCheck(Square init, Square king, boolean isWhiteTurn)
    {
        Square temp;
        int count = 1;
        int row = king.getRow();
        int col = king.getCol();
        while((row-count) >= 0 && (col-count) >= 0)
        {
            temp = squares[row-count][col-count];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasBishop() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
            count++;
        }
        count = 1;
        while((row+count) <= 7 && (col+count) <= 7)
        {
            temp = squares[row+count][col+count];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasBishop() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
            count++;
        }

        return true;
    }

    private static boolean checkPosDiagCheck(Square init, Square king, boolean isWhiteTurn)
    {
        Square temp;
        int count = 1;
        int row = king.getRow();
        int col = king.getCol();
        while((row-count) >= 0 && (col+count) <= 7)
        {
            temp = squares[row-count][col+count];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasBishop() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
            count++;
        }
        count = 1;
        while((row+count) <= 7 && (col-count) >= 0)
        {
            temp = squares[row+count][col-count];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasBishop() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
            count++;
        }

        return true;
    }

    private static boolean checkVertCheck(Square init, Square king, boolean isWhiteTurn)
    {
        Square temp;
        int col = king.getCol();
        for(int i = king.getRow(); i < 7; i++)
        {
            temp = squares[i+1][col];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasRook() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
        }
        for(int i = king.getRow(); i > 0; i--)
        {
            temp = squares[i-1][col];
            if(temp.getRow() != init.getRow() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasRook() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
        }
        return true;
    }

    private static boolean checkHorizCheck(Square init, Square king, boolean isWhiteTurn)
    {
        Square temp;
        int row = king.getRow();
        for(int i = king.getCol(); i < 7; i++)
        {
            temp = squares[row][i+1];
            if(temp.getCol() != init.getCol() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasRook() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
        }
        for(int i = king.getRow(); i > 0; i--)
        {
            temp = squares[row][i-1];
            if(temp.getCol() != init.getCol() && temp.hasPiece())
            {
                if(king.hasDiffColor(temp) && (temp.hasRook() || temp.hasQueen()))
                    return false;
                else
                    break;
            }
        }
        return true;
    }

    public Square getSquare(int i, int j)
    {
        return squares[i][j];
    }

    private static boolean isWhiteTurn(Square squ)
    {
        if((squ.hasWhite() && isWhiteTurn) || (!squ.hasWhite() && !isWhiteTurn))
            return true;
        return false;
    }

    private static void makePiece(int i, int j, int col)
    {
        squares[i][j] = new Square(i,j,getPieceType(i,j),col);
    }

    public static Piece getPieceType(int i, int j)
    {
        if (i == 0 || i == 7)
        {
            if (j == 0 || j == 7)
                return new Rook();
            else if (j == 1 || j == 6)
                return new Knight();
            else if (j == 2 || j == 5)
                return new Bishop();
            else if (j == 3)
                return new Queen();
            else if (j == 4)
                return new King();
        }       
        else
            return new Pawn();
        return null;
    }

    private static int squColor(int i, int j)
    {
        if(((i%2 == 0) && (j%2 == 0)) || (i%2 == 1) && (j%2 == 1))
            return 1;
        return 0;
    }

}

