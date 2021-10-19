import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.*;

public class Board extends JPanel{
	
	private static Tile[][] board = new Tile[24][24];
	private int bombNumber = 99;
	private static boolean go = true;
	
	/*
	 * first all of the tiles are initialized with an id and a state of 0
	 * second the bombs are randomly added to tiles
	 * finally the number on the rest of the tiles are calculated based off the bombs
	 */
	Board() throws InvalidStateException
	{
		setLayout(new GridLayout(24, 24));
		createTiles();
		addBombs();
		addNumbers();
	}
	
	//initializes the tiles with state 0 and an ID and adds to panel
	public void createTiles()
	{
		for(int i = 0; i < board.length; i++)
		{
			for(int j = 0; j < board[0].length; j++)
			{
				board[i][j] = new Tile(0, new ID(i, j));
				board[i][j].setPreferredSize(new Dimension(23, 23));
				add(board[i][j]);
			}
		}
	}
	
	//randomly adds the bombNumber of bombs to the tiles
	public void addBombs() throws InvalidStateException
	{
		int bombsAdded = 0;
		boolean stop = false;
		while(bombsAdded < bombNumber)
		{
			for(int i = 0; i < board.length; i++)
			{
				for(int j = 0; j < board[0].length; j++)
				{
					if(bombsAdded >= bombNumber)
					{
						stop = true;
						break;
					}
					if((int)(Math.random() * 5000) == 0)
					{
						bombsAdded++;
						board[i][j].setState(9);
					}
				}
				if(stop)
					break;
			}
		}
	}
	
	//based off the bombs this methods adds the states to the rest of the tiles
	public void addNumbers() throws InvalidStateException
	{
		for(int x = 0; x < board.length; x++)
		{
			for(int y = 0; y < board[0].length; y++)
			{
				if(!board[x][y].isBomb())
				{
					//top left
					if(x - 1 >= 0 && y - 1 >= 0)
					{
						if(board[x-1][y-1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//up
					if(x - 1 >= 0)
					{
						if(board[x-1][y].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//top right
					if(x - 1 >= 0 && y + 1 <= 23)
					{
						if(board[x-1][y+1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//right
					if(y + 1 <= 23)
					{
						if(board[x][y+1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//bottom right
					if(x + 1 <= 23 && y + 1 <= 23)
					{
						if(board[x+1][y+1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//bottom
					if(x + 1 <= 23)
					{
						if(board[x+1][y].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//bottom left
					if(x + 1 <= 23 && y - 1 >= 0)
					{
						if(board[x+1][y-1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
					//left
					if(y - 1 >= 0)
					{
						if(board[x][y-1].isBomb())
						{
							board[x][y].incrementState();
						}
					}
				}
			}
		}
	}
	
	//clears all of the tiles around a clear tile 
	public static void clearRevealed(ID id)
	{
		int x = id.getX();
		int y = id.getY();
		
		board[x][y].reveal();
		
		//top left
		if(x - 1 >= 0 && y - 1 >= 0)
			board[x-1][y-1].reveal();
		//up
		if(x - 1 >= 0)
			board[x-1][y].reveal();
		//top right
		if(x - 1 >= 0 && y + 1 <= 23)
			board[x-1][y+1].reveal();
		//right
		if(y + 1 <= 23)
			board[x][y+1].reveal();
		//bottom right
		if(x + 1 <= 23 && y + 1 <= 23)
			board[x+1][y+1].reveal();
		//bottom
		if(x + 1 <= 23)
			board[x+1][y].reveal();
		//bottom left
		if(x + 1 <= 23 && y - 1 >= 0)
			board[x+1][y-1].reveal();
		//left
		if(y - 1 >= 0)
			board[x][y-1].reveal();
		
	}
	
	//ends the game when a bomb is hit
	public static void endGame()
	{
		for(int x = 0; x < board.length; x++)
		{
			for(int y = 0; y < board[0].length; y++)
			{
				board[x][y].setFreeze(true);
				board[x][y].reveal();
				board[x][y].setFlag(false);
			}
		}
		if(go)
		{
		JFrame frame = new JFrame();
		frame.setSize(200, 200);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("YOU LOST");
		panel.add(label);
		frame.add(panel);
		
		JButton button = new JButton("Play Again?");
		button.addActionListener((ActionEvent e) -> {
			/*
			JFrame frame1 = new JFrame();
			frame1.setSize(600, 620);
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Board board = null;
			try {
				board = new Board();
			} catch (InvalidStateException e1) {
				System.out.println("OOF");
				e1.printStackTrace();
			}
			frame1.add(board);
			frame1.setTitle("MineSweeper");
			frame1.setVisible(true);
			*/
			try {
				main(null);
			} catch (InvalidStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(button);
		frame.setVisible(true);
		go = false;
		}
	}
	
	//returns true if all of the squares without a bomb are revealed
	public static boolean hasWon()
	{
		for(int x = 0; x < board.length; x++)
		{
			for(int y = 0; y < board[0].length; y++)
			{
				boolean go = true;
				if(board[x][y].getState() == 9)
				{
					go = false;
				}
				if(!(board[x][y].isRevealed()) && go)
				{
					return false;
				}
			}
		}
		if(go)
		{
		JFrame frame = new JFrame();
		frame.setSize(200, 200);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("YOU WON!!!");
		panel.add(label);
		frame.add(panel);
		
		JButton button = new JButton("Play Again?");
		button.addActionListener((ActionEvent e) -> {
			//frame.dispose();
			JFrame frame1 = new JFrame();
			frame1.setSize(600, 620);
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Board board = null;
			try {
				board = new Board();
			} catch (InvalidStateException e1) {
				System.out.println("OOF");
				e1.printStackTrace();
			}
			frame1.add(board);
			frame1.setTitle("MineSweeper");
			frame1.setVisible(true);
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(button);
		frame.setVisible(true);
		go = false;
		}
		return true;
	}
	
	public static void main(String[] args) throws InvalidStateException
	{
		JFrame frame = new JFrame();
		frame.setSize(600, 620);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Board b = new Board();
		frame.add(b);
		frame.setTitle("MineSweeper");
		frame.setVisible(true);
		//System.out.println((int)(Math.random()*2) + 1);
		
		
	}
	
	public String toString()
	{
		String s = "";
		for(int x = 0; x < board.length; x++)
		{
			for(int y = 0; y < board[0].length; y++)
			{
				s = s + "[" + board[x][y].toString() + "]";
			}
			s = s + "\n";
		}
		return s;
	}

	
}
