import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Client {
    public static void main(String[] args) {
	Scanner scan;
        Thread st = null;
	Socket peerConnectionSocket = null;
        if (args.length != 2) {
            System.err.println("Usage: Client <address> <port>");
            return;
        }
       long Encryptee = (long)(Math.random()*100) + 1;
       BigInteger unCoded;
       unCoded = BigInteger.valueOf(Encryptee);
        try {
            peerConnectionSocket = new Socket(args[0], Integer.parseInt(args[1]));

            st = new Thread(new StringSender(new PrintWriter(peerConnectionSocket.getOutputStream())));
            st.start();
            scan = new Scanner(peerConnectionSocket.getInputStream());
            String fromSocket;
            while ((fromSocket = scan.nextLine()) != null) {
                System.out.println(fromSocket);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (st != null) {
                st.stop();
            }
        }
    }
}
