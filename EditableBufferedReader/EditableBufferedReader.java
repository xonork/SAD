import java.io.*;

public class EditableBufferedReader extends BufferedReader{

	private Line line;

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
		if (c == Keys.ESC){
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
		while( (x != Keys.EXIT) && (x != Keys.ENTER)){
			x = this.read();
			/*line.addChar((char)x);
			//System.out.print(x+"");
			str+=(char)x;
			line.clearTerminal();
			System.out.print(line.toString());*/
	
			switch(x){
				case Keys.LEFT:
					System.out.print("\033[D");
					line.moveLeft();
					
				break;

				case Keys.RIGHT:
					System.out.print("\033[C");
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

				default:
					line.addChar((char)x);
					str+=(char)x;
			}
					
					
		
			
		}
		
		unsetRaw();
		return str;	
	}

}
