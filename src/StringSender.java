import java.util.*;
import java.io.*;

public class StringSender implements Runnable {
    private Scanner scan;
    private PrintWriter out;
    private boolean cont = true;

    public StringSender(PrintWriter out) {
	this.out = out;
        scan = new Scanner(System.in);
    }

    public void run() {
	while (cont) {
	    System.out.print("Press Enter to Encrypt integer > ");
            String str = scan.nextLine();
	    out.println(str);
            out.flush();
	}
    }

    public void stop() {
        cont = false;
    }

    public static void main(String[] args) {
	Thread st = new Thread(new StringSender(new PrintWriter(System.out)));
	st.start();
	try {
            st.join();
	} catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
