import java.io.*;
import java.lang.*;
import java.util.*;

public class Line extends Observable{

	private char[] buffer; //= {'a','b','c','d','e','g',' ',' ',' '};
	private int cursPos;
	private int numberOfChars;
	private boolean insert;
	private MyStringBuffer bufferx;

	public Line(){
		cursPos = 0;
		numberOfChars = 0;
		insert = false;
		bufferx = new MyStringBuffer(this.columnCounter());
		
		
	}


	public void moveRight(){
		
		if((cursPos != bufferx.length()) && (cursPos != (bufferx.str.length-1))){
			cursPos++;
			this.notifyConsole();
		}
		
		
		
	}

	public void moveLeft(){
		if(cursPos != 0){
			cursPos--;
			this.notifyConsole();
		}
		
		
		
		
	} 

	public void deleteChar(){

		bufferx.deleteChar(cursPos);
		this.moveLeft();
		this.notifyConsole();
	}

	public void supr(){
		if(cursPos != bufferx.length()){
			bufferx.deleteChar(cursPos+1);
			this.notifyConsole();
		}
		
	}

	public void addChar(char c){

		if(!insert){
			
			bufferx.insert(cursPos, c);
			this.moveRight();
			this.notifyConsole();
			
		}
		else if(insert){
			
			bufferx.setCharAt(cursPos,c);
			this.moveRight();
			this.notifyConsole();		
	 	}	
			
			
			
			
		
		

	}

	public void goHome(){
		cursPos=0;
		//System.out.print("\033[" + 0 + "G");
		this.notifyConsole();
		
		
	}

	public void goEnd(){
		if(bufferx.length() < (bufferx.str.length)){
			cursPos = bufferx.length();
		}
		else
			cursPos = (bufferx.str.length-1);

		this.notifyConsole();
		
		

	}

	public void switchMode(){
		insert = !insert;
	}

	private void notifyConsole(){
		setChanged();
		notifyObservers(new Console.ConsoleArgs(bufferx, cursPos, numberOfChars));
	}


	
	public static int columnCounter(){
		try{
			Process p = Runtime.getRuntime().exec("tput cols");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String cols = reader.readLine();
			return Integer.parseInt(cols);
		} catch (Exception e) {
			System.out.print(e);
		}
		return 0;
			
		
	}

	public String toString(){
		String str = bufferx.toString();
		
		return str;
	}

	

		

	

	

}
