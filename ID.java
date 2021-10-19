//ID of the location of tiles in the grid
public class ID {
	
	//row
	private int x; 
	//column
	private int y;
	
	ID(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public String toString()
	{
		return x + ", " + y;
	}
	
}
