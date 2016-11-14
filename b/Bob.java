import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Bob {
    public static void main(String[] args) {
	Scanner scan;
        int proceed;
	Socket peerConnectionSocket = null;
        if (args.length != 2) {
            System.err.println("Usage: Client <address> <port>");
            return;
        }
        Scanner keyboard = new Scanner(System.in);
        PrintWriter pw = null;
        try {
            peerConnectionSocket = new Socket(args[0], Integer.parseInt(args[1]));
            pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            scan = new Scanner(peerConnectionSocket.getInputStream());

            do {
                doEverythingOnce(pw, scan);
                System.out.println("Proceed? 1-yes, 0-no");
                proceed = Integer.parseInt(keyboard.nextLine());
                pw.println(proceed);
                pw.flush();
            } while (proceed == 1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private static void doEverythingOnce(PrintWriter pw, Scanner scan) {
        BigInteger decrypted;
        BigInteger unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
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
        decrypted = new BigInteger(scan.nextLine());
        if (decrypted.equals(unCoded)) {
            System.out.println("There is a match");
            int keySize = Integer.parseInt(scan.nextLine());
            boolean send = true;
            pw.println(send);
            pw.flush();
            System.out.println("The key size is " + keySize);
            RsaInstance reverseRSA = new RsaInstance(keySize);
            System.out.println("pq = " + reverseRSA.getPQString());
            System.out.println("e = " +  reverseRSA.getEString());
            System.out.println("d = " +  reverseRSA.getDString());
            pw.println(reverseRSA.getEString() + "\n" + reverseRSA.getPQString());
            pw.flush();
            System.out.println("Recieved encryption: ");
            BigInteger confirmationEncrypt = new BigInteger(scan.nextLine());
            System.out.println("Bob's encrypted number: " + confirmationEncrypt.toString());
            BigInteger confirmationDecrypt = reverseRSA.decrypt(confirmationEncrypt);
            System.out.println("decrypting...");
            System.out.println("Alice's secret number: " + confirmationDecrypt.toString());
            pw.println(confirmationDecrypt.toString());
            pw.flush(); 
        }
    }
}
