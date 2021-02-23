import java.io.*;

public class EditableBufferedReader extends BufferedReader{

	public EditableBufferedReader(Reader in){
		super(in);
		
	}

	public static void setRaw(){
		try{
			Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo </dev/tty"});	
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	public static void unsetRaw(){
		try{
			Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty -raw echo </dev/tty"});
		}
		catch (IOException e) { e.printStackTrace(); }
	}

	public int read() throws IOException{
		int c = super.read();
		//System.out.println(String.valueOf(c));
		System.out.print((char)c);
		return c;
	}

	public  String readLine() throws IOException{
		setRaw();
		int x = 0;
		String str = "";	
		while( x != 3 ){
			x = this.read();
			//System.out.print(x+"");
			str+=(char)x;
			
		}
		unsetRaw();
		return str;	
	}

}
