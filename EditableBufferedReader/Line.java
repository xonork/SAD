import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class Line {

    private char[] buffer; //= {'a','b','c','d','e','g',' ',' ',' '};
    private int cursPos;
    private int numberOfChars;
    private int columns;
    private boolean insert;

    public Line() {
        cursPos = 0;
        numberOfChars = 0;
        insert = false;
        columns = this.columnCounter();
        buffer = new char[columns];


    }

    public static void main(String[] args) {
        Line line = new Line();
        line.moveLeft();

        line.deleteChar();
        System.out.print(line.toString());

    }

    public void clearTerminal() {

        System.out.print("\033[2K");
        for (int i = 0; i < numberOfChars; i++) {
            System.out.print("\b");
        }


    }


    public void moveRight() {

        if ((cursPos != numberOfChars) && (cursPos != columns - 1)) {
            cursPos++;
            System.out.print("\033[C");
        }



    }

    public void moveLeft() {
        if (cursPos != 0) {
            cursPos--;
            System.out.print("\033[D");
        }




    }

    public void deleteChar() {

        if (cursPos != 0) {
            for (int i = cursPos; i < numberOfChars; i++) {
                buffer[i - 1] = buffer[i];
            }
            this.moveLeft();
            numberOfChars--;
            this.clearTerminal();
            System.out.print(this.toString());
            System.out.print("\033[" + (cursPos + 1) + "G");
        }
    }

    public void supr() {
        if (cursPos != numberOfChars) {
            for (int i = cursPos + 1; i < numberOfChars; i++) {
                buffer[i - 1] = buffer[i];
            }
            numberOfChars--;
            this.clearTerminal();
            System.out.print(this.toString());
            System.out.print("\033[" + (cursPos + 1) + "G");
        }

    }

    public void addChar(char c) {

        if ((numberOfChars != columns) && !insert) {

            for (int i = numberOfChars - 1; i >= cursPos; i--) {
                buffer[i + 1] = buffer[i];
            }
            numberOfChars++;
            buffer[cursPos] = c;
            this.moveRight();
            this.clearTerminal();
            System.out.print(this.toString());
            System.out.print("\033[" + (cursPos + 1) + "G");

        } else if (insert) {
            if ((cursPos >= numberOfChars) && (numberOfChars != columns)) {
                numberOfChars++;
            }
            buffer[cursPos] = c;
            this.moveRight();
            this.clearTerminal();
            System.out.print(this.toString());
            System.out.print("\033[" + (cursPos + 1) + "G");
        }







    }

    public void goHome() {
        cursPos = 0;
        System.out.print("\033[" + 0 + "G");


    }

    public void goEnd() {
        if (numberOfChars < columns) {
            cursPos = numberOfChars;
        } else
            cursPos = columns - 1;

        System.out.print("\033[" + (cursPos + 1) + "G");



    }

    public void switchMode() {
        insert = !insert;
    }


    public static int columnCounter() {
        try {
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

    public String toString() {
        String str = "";
        for (int i = 0; i < numberOfChars; i++) {
            str += buffer[i];
        }
        //System.out.print("\033[" + cursPos + "D");
        return str;
    }







}
