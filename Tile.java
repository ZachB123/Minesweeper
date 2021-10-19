import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

//is the individual tile for the minesweeper game
public class Tile extends JComponent
{
	/*
	 * state is what is underneath the unrevealed tile
	 * 0 means 0 bombs around tile
	 * 1 -> 8 means n bombs around the tile
	 * 9 means it is a bomb
	 */
	private int state = 0;
	
	//true if the user placed a flag on the tile
	private boolean flag = false;
	
	//default is false turns true if the user clicks on it
	private boolean revealed = false;
	
	//The ID of the tile
	private ID id;
	
	//is turned true when you lose the game so you can not change any of the tiles
	private boolean freeze = false;
	
	public boolean isFlag()
	{
		return flag;
	}
	
	public void setFlag(boolean b)
	{
		flag = b;
		repaint();
	}
	
	//increments the state
	public void incrementState() throws InvalidStateException
	{
		state++;
		if(state == 9)
		{
			throw new InvalidStateException("9 occured");
		}
	}
	
	public void setFreeze(boolean freeze)
	{
		this.freeze = freeze;
	}
	
	
	//sets the ID of the tile
	public void setID(ID id)
	{
		this.id = id;
	}
	
	//sets the state of the tile
	public void setState(int n) throws InvalidStateException
	{
		if(n < 0 || n > 9)
		{
			throw new InvalidStateException("" + n + " is not a valid state");
		}
		state = n;
		
	}
	
	//gets the state of the current tile
	public int getState()
	{
		return state;
	}
	
	//returns true if state is 9 which means its a bomb
	public boolean isBomb()
	{
		if(state == 9)
			return true;
		return false;
	}
	
	//is run when the user clicks on a tile
	public void reveal()
	{
		
		if(!revealed)
			{
			revealed = true;
			repaint();
			if(state == 9)
			{
				Board.endGame();
			}
			if(state == 0)
			{
				Board.clearRevealed(id);
			}
		}
	}
	
	//returns true if the tile has been click on and opened up
	public boolean isRevealed()
	{
		return revealed;
	}
	
	//main drawing methods calls the other drawing methods
	public void paintComponent(Graphics g)
	{
		if(flag)
		{
			paintFlag(g);
		}
		else
		{
			if(!revealed)
			{
				paintUnopened(g);
			}
			else
			{
				if(state <= 8 && state >= 1)
				   paintNumber(g);
				if(state == 9)
					paintBomb(g);
			}
		}
	}
	
	private void paintFlag(Graphics g) {
		g.drawRect(0,  0, 23, 23);
		g.drawLine(19, 13, 19, 23);
		g.setColor(Color.RED);
		g.fillRect(7, 5, 13, 8);
	}

	//paints the black circle indicating the bomb
	private void paintBomb(Graphics g) {
		g.drawRect(0,  0, 23, 22);
		g.fillOval(2, 2, 19, 19);
	}

	//first sets the color based on the number then draws the number on the tile
	public void paintNumber(Graphics g)
	{
		g.drawRect(0,  0, 23, 22);
		
		// 1=light blue 2=green 3=red 4=dark blue 5=maroon 6=aqua/turquoise 7=black 8=light gray
		switch(state)
		{
		case 1: g.setColor(new Color(0,171,235)); break;
		case 2: g.setColor(new Color(17, 102, 0)); break;
		case 3: g.setColor(Color.RED); break;
		case 4: g.setColor(new Color(0,0,200)); break;
		case 5: g.setColor(new Color(128,0,0)); break;
		case 6: g.setColor(new Color(0,230,230)); break;
		case 7: g.setColor(Color.BLACK); break;
		case 8: g.setColor(Color.DARK_GRAY); break;
		}
		
		//drawing the number
		g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		g.drawString(Integer.toString(state), 8, 17);
	}

	//paints a square with a square inside it to signify that it has been unopened
	public void paintUnopened(Graphics g)
	{
		g.drawRect(0,  0, 23, 23);
		g.drawRect(2,  2, 19,  19);
	}
		
	
	//opens up or puts a flag on the tile when clicked on
	public class MouseDetector extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent m) {
        	if(!freeze)
        	{
        		if (m.getButton() == MouseEvent.BUTTON1){
        			if(!flag)
							reveal();	
        		} else if (m.getButton() == MouseEvent.BUTTON3) {
        			if(flag)
        			{
        				flag = false;
        			}
        			else
        			{
        				if(!revealed)
        				{
        					flag = true;
        				}
        			}
        			repaint();
        		} 
        		if(Board.hasWon())
        		{
        			
        		}
        	}
        }
    }
	
	//adds the mouse listener to the Tile when it is made
	Tile()
	{
		MouseDetector m = new MouseDetector();
		addMouseListener(m);
	}
	
	//initialize the tile with a state and an ID
	Tile(int s, ID id)
	{
		this();
		state = s;
		this.id = id;
	}
	
	public String toString()
	{
		return "State: " + state + " Revealed " + isRevealed() + " Flag: " + flag + " ID: " + id.toString(); 
	}
	
	
}
