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
		
		if (c == Keys.ESC){
			int next = super.read();
			if (next == Keys.CSI){
				int special = super.read();
				if((special == Keys.INSERT) || (special == Keys.SUPR)){
					if(super.read() == Keys.TILDE)
						return special;
				}
				else if(Keys.SpecialsList.contains(special))
					return (special);
				else
					return -1;
			}
		}
		
		return c;
		
	}

	public String readLine() throws IOException{
		setRaw();
		Line line = new Line();
		Console con = new Console();
		line.addObserver(con);
		int x;
		String str = "";	
		while( (x = this.read()) != Keys.ENTER){
			
			switch(x){
				case Keys.LEFT:
					line.moveLeft();
					
				break;

				case Keys.RIGHT:
					line.moveRight();
				break;

				case Keys.HOME:
					line.goHome();
				break;

				case Keys.END:
					line.goEnd();
				break;

				case Keys.INSERT:
					line.switchMode();
				break;

				case Keys.BACKSPACE:
					line.deleteChar();
				break;

				case Keys.SUPR:
					line.supr();
				break;

				case -1:
				break;
					

				default:
					line.addChar((char)x);
					
			}
					
					
		
			
		}
		str = line.toString();
		unsetRaw();
		return str;	
	}

}
