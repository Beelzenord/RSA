import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Bob {
    public static void main(String[] args) {
	Scanner scan;
        int proceed;
	Socket peerConnectionSocket = null;
        Scanner keyBoard = new Scanner(System.in);
        if (args.length != 2) {
            System.err.println("Usage: Client <address> <port>");
            return;
        }
        BigInteger unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
        BigInteger Decrypted;
        PrintWriter pw = null;
        try {
            peerConnectionSocket = new Socket(args[0], Integer.parseInt(args[1]));

            pw = new PrintWriter(peerConnectionSocket.getOutputStream());
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
            pw.println(encrypted.toString());
            pw.flush();
            Decrypted = new BigInteger(scan.nextLine());
            if (!Decrypted.equals(unCoded)) {
                System.out.println("Secret number doesn't match!");
                return;
            }
            int keySize = Integer.parseInt(scan.nextLine());
            System.out.println("The key size is " + keySize);
            RsaInstance reverseRSA = new RsaInstance(keySize);
            System.out.println("pq = " + reverseRSA.getPQString());
            System.out.println("e = " +  reverseRSA.getEString());
            System.out.println("d = " +  reverseRSA.getDString());

            System.out.println("Proceed? 1-yes, 0-no");
            proceed = Integer.parseInt(keyBoard.nextLine());
            pw.println(proceed);
            while (proceed == 1) {
                unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
                encrypted = rsa.encrypt(unCoded);
                System.out.println("Encrypted number = " + encrypted.toString());
                pw.println(encrypted.toString());                 
                pw.flush();
                System.out.println("Secret number = " + unCoded.intValue());
                System.out.println("Waiting for key...");

                System.out.println("Proceed? 1-yes, 0-no");
                proceed=Integer.parseInt(keyBoard.nextLine());
                pw.println(proceed);
                pw.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
