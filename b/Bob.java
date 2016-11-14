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
        System.out.println("Waiting for Alice's key...");
        BigInteger keyE = new BigInteger(scan.nextLine());
        BigInteger keyPQ = new BigInteger(scan.nextLine());
        System.out.println("Alice's e = " + keyE.toString());
        System.out.println("Alice's pq = " + keyPQ.toString());
        RsaInstance rsa = new RsaInstance(keyE, keyPQ);
        System.out.println("Creating a secret number...");
        BigInteger unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
        System.out.println("My secret number = " + unCoded.intValue());
        System.out.println("Encrypting my secret number...");
        BigInteger encrypted = rsa.encrypt(unCoded);
        System.out.println("My encrypted secret number = " + encrypted.toString());
        System.out.println("Sending encrypted number to Alice...");
        pw.println(encrypted.toString());
        pw.flush();
        System.out.println("Waiting for Alice to decrypt my secret number...");
        decrypted = new BigInteger(scan.nextLine());
        if (!decrypted.equals(unCoded)) {
            System.out.println("Alice could not decrypt. Connection is NOT secure!");
            return;
        }
        System.out.println("Connection is secure!");
        int keySize = Integer.parseInt(scan.nextLine());
        boolean send = true;
        pw.println(send);
        pw.flush();
        System.out.println("The key size is " + keySize);
        RsaInstance reverseRSA = new RsaInstance(keySize);
        System.out.println("My pq = " + reverseRSA.getPQString());
        System.out.println("My e = " +  reverseRSA.getEString());
        System.out.println("My d = " +  reverseRSA.getDString());
        System.out.println("Sending my key...");
        pw.println(reverseRSA.getEString());
        pw.println(reverseRSA.getPQString());
        pw.flush();
        System.out.println("Reading Alice's encrypted number...");
        BigInteger confirmationEncrypt = new BigInteger(scan.nextLine());
        System.out.println("Alice's encrypted number = " + confirmationEncrypt.toString());
        BigInteger confirmationDecrypt = reverseRSA.decrypt(confirmationEncrypt);
        System.out.println("Decrypting Alice's number...");
        System.out.println("Alice's secret number = " + confirmationDecrypt.toString());
        pw.println(confirmationDecrypt.toString());
        pw.flush(); 
    }
}
