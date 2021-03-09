import java.util.*;

public class Console implements Observer {

    public void update(Observable o, Object arg) {
        ConsoleArgs args = (ConsoleArgs) arg;
        this.clearTerminal(args.numberOfChars);
        System.out.print(this.toString(args.numberOfChars, args.buffer));
        System.out.print("\033[" + (args.cursPos + 1) + "G");



    }

    static class ConsoleArgs {
        char[] buffer;
        int cursPos;
        int numberOfChars;

        ConsoleArgs(char[] buffer, int cursPos, int numberOfChars) {
            this.buffer = buffer;
            this.cursPos = cursPos;
            this.numberOfChars = numberOfChars;


        }
    }

    private void clearTerminal(int numberOfChars) {

        System.out.print("\033[2K");
        for (int i = 0; i < numberOfChars; i++) {
            System.out.print("\b");
        }


    }

    private String toString(int numberOfChars, char[] buffer) {
        String str = "";
        for (int i = 0; i < numberOfChars; i++) {
            str += buffer[i];
        }
        //System.out.print("\033[" + cursPos + "D");
        return str;
    }

}
