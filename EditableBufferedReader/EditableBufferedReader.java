import java.io.*;

public class EditableBufferedReader extends BufferedReader{

	Line line;

	public EditableBufferedReader(Reader in){
		super(in);
		line = new Line();
		
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
		//System.out.print((char)c);
		if (c == Keys.SC){
			int next = super.read();
			if (next == Keys.CSI){
				int special = super.read();
				return special;
			}
		}
		
		return c;
		
	}

	public String readLine() throws IOException{
		setRaw();
		int x = 0;
		String str = "";	
		while( x != 3 ){
			x = this.read();
			line.addChar((char)x);
			//System.out.print(x+"");
			str+=(char)x;
			line.clearTerminal();
			System.out.print(line.toString());
		}
		
		unsetRaw();
		return str;	
	}

}
