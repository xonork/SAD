import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class Line{

	private char[] buffer; //= {'a','b','c','d','e','g',' ',' ',' '};
	private int cursPos;
	private int numberOfChars;
	private int columns;
	private boolean insert;

	public Line(){
		cursPos = 0;
		numberOfChars = 0;
		insert = false;
		columns = this.columnCounter();
		buffer = new char[columns];
		
		
	}

	public static void main(String[] args){
		Line line = new Line();
		line.fillBuffer();
		line.moveLeft();
		
		line.deleteChar();
		System.out.print(line.toString());

	}

	public void clearTerminal(){
		
		System.out.print("\033[2K");
		for(int i = 0; i < numberOfChars; i++){
			System.out.print("\b");		
		}
		
		
	}


	public void moveRight(){
		
		if((cursPos != numberOfChars) && (cursPos != columns))
			cursPos++
		
		
		
		
	}

	public void moveLeft(){
		if(cursPos != 0)
			cursPos--;
		
		
		
		
	} 

	public void deleteChar(){

		if(cursPos != 0){
			for(int i = cursPos; i < numberOfChars; i++){
				buffer[i-1] = buffer[i];		
			}
			this.moveLeft();
			numberOfChars--;
		}
	}

	public void addChar(char c){

		if(numberOfChars != columns){
			for(int i = numberOfChars-1; i >= cursPos; i--){
				buffer[i+1] = buffer[i];		
			}
			buffer[cursPos] = c;
			numberOfChars++;
			this.moveRight();
			this.clearTerminal();
			System.out.print(this.toString());
			System.out.print("\033[" + (cursPos+1) + "G");
			
			
			
		}
		

	}

	public void goHome(){
		
	}

	public void goEnd(){
	}

	public void switchMode(){
	}

	public void fillBuffer(){
		
		cursPos = 6;
		numberOfChars = 6;
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

	public String toString(){
		String str = "";
		for(int i = 0; i < numberOfChars; i++){
			str+=buffer[i];
		}
		System.out.print("\033[" + cursPos + "D");
		return str;
	}

		

	

	

}
