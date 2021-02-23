import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class Line{

	char[] buffer;
	int cursPos;
	int numberOfChars;
	int columns;

	public Line(){
		cursPos = 0;
		numberOfChars = 0;
		columns = this.columnCounter();
		buffer = new char[columns];
	}

	public void clearTerminal(){
		
		System.out.print("\033[2K");
		for(int i = 0; i < numberOfChars; i++){
			System.out.print("\b");		
		}
	}


	public void moveRight(){
	}

	public void moveLeft(){
	} 

	public void deleteChar(){
	}

	public void addChar(char c){
	
		System.out.print(c);
		numberOfChars++;

	}

	public void goHome(){
	}

	public void goEnd(){
	}

	public void switchMode(int mode){
	}

	public static int columnCounter(){
		String columns = null;
		try{
		//It executes the command tput cols 2 and writes the output in a temporal file
		Runtime.getRuntime().exec(new String[] {"bash", "-c", "tput cols 2 > /tmp/columns.txt"  });    						
		}
		catch (IOException e){ e.printStackTrace(); }

		try{
			File file = new File("/tmp/columns.txt");
			Scanner reader = new Scanner(file);
			columns = reader.nextLine();
			

		}
		catch (FileNotFoundException e){ e.printStackTrace(); }
		return Integer.parseInt(columns);
		
	}

		

	

	

}
