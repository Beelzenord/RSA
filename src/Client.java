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
        BigInteger unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
        try {
            peerConnectionSocket = new Socket(args[0], Integer.parseInt(args[1]));

            PrintWriter pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            st = new Thread(new StringSender(pw));
            //st.start();
            scan = new Scanner(peerConnectionSocket.getInputStream());
            System.out.println("Secret number = " + unCoded.intValue());
            System.out.println("Waiting for key...");
            BigInteger keyE = new BigInteger(scan.nextLine());
            BigInteger keyPQ = new BigInteger(scan.nextLine());
            RsaInstance rsa = new RsaInstance(keyE, keyPQ);
            System.out.println("e = " + keyE.toString());
            System.out.println("pq = " + keyPQ.toString());
            BigInteger encrypted = rsa.encrypt(unCoded);
            System.out.println("Encrypted number = " + encrypted.toString());
            pw.print(unCoded.toString());
            pw.flush();
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
