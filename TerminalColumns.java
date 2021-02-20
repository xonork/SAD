import java.io.*;
import java.util.Scanner;

class TerminalColumns{
	public static void main(String[] args){
	
	try{
		//It executes the command tput cols 2 and writes the output in a temporal file
		Runtime.getRuntime().exec(new String[] {"bash", "-c", "tput cols 2 > /tmp/columns.txt"  });    						
	}
	catch (IOException e){ e.printStackTrace(); }

	try{
		//It reads the file and writes its content (which is the number of columns) in the terminal
		File file = new File("/tmp/columns.txt");
		Scanner reader = new Scanner(file);
		String columns = reader.nextLine();
		System.out.print("Columns: "+columns+"\n");

	}
	catch (FileNotFoundException e){ e.printStackTrace(); }
	}

	//We could use this program as a function in the EditableBufferedReader Class, instead of write in
	//the terminal the number of columns it could return that number as an integer.
	
} 
