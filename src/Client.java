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

            PrintWriter pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            st = new Thread(new StringSender(pw));
            st.start();
            scan = new Scanner(peerConnectionSocket.getInputStream());
            String fromSocket;
            BigInteger keyE = new BigInteger(scan.nextLine());
            BigInteger keyPQ = new BigInteger(scan.nextLine());
            RsaInstance rsa = new RsaInstance(keyE, keyPQ);
            System.err.println(keyE.toString());
            BigInteger encrypted = rsa.encrypt(unCoded);
            pw.print(unCoded.toString());
            pw.flush();
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
