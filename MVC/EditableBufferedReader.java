import java.io.*;

public class EditableBufferedReader extends BufferedReader{

	private Line line;
	private Console con;
	

	public EditableBufferedReader(Reader in){
		super(in);
		line = new Line();
		con = new Console();
		line.addObserver(con);
		
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
				if(Keys.SpecialsList.contains(special))
					return (special);
				else
					return -1;
			}
		}
		
		return c;
		
	}

	public String readLine() throws IOException{
		setRaw();
		int x = 0;
		String str = "";	
		while( (x != Keys.EXIT) && (x != Keys.ENTER)){
			x = this.read();
			
			/*line.addChar((char)x);
			//System.out.print(x+"");
			str+=(char)x;
			line.clearTerminal();
			System.out.print(line.toString());*/
	
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
					if(super.read() == Keys.TILDE)
						line.switchMode();
				break;

				case Keys.BACKSPACE:
					line.deleteChar();
				break;

				case Keys.SUPR:
					if(super.read() == Keys.TILDE)
						line.supr();

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
