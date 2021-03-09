import java.util.Arrays;
import java.util.List;
public final class Keys {

    private Keys() {
            // restrict instantiation
    }

    	public static final int ESC = 27;
	public static final int CSI = 91;
	public static final int ENTER = 13;
	public static final int UP = 65;
	public static final int LEFT = 68;
	public static final int DOWN = 66;
	public static final int RIGHT = 67;
	public static final int EXIT = 3;
	public static final int HOME = 72;
	public static final int END =70;
	public static final int INSERT = 50;
	public static final int SUPR = 51;
	public static final int TILDE = 126;
	public static final int BACKSPACE = 127;
	private static final Integer[] SPECIALS = new Integer[]{LEFT,RIGHT,HOME,END,INSERT,SUPR};
	public static final List<Integer> SpecialsList = Arrays.asList(SPECIALS);
}
