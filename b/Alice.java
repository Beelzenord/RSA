import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Alice {
    public static void main(String[] args) {
	Scanner scan;
	Socket peerConnectionSocket = null;
	int proceed ;
        if (args.length != 2) {
            System.err.println("Usage: Server <port> <keysize>");
            return;
        }
        int sizeOfKey = Integer.parseInt(args[1]);
        PrintWriter pw = null;
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Waiting for connection...");
            peerConnectionSocket = ss.accept();
            pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            scan = new Scanner(peerConnectionSocket.getInputStream());

            do {
                doEverythingOnce(pw, scan, sizeOfKey);
                proceed = Integer.parseInt(scan.nextLine());
                System.out.println("proceeding: "+ proceed);
            } while (proceed == 1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private static void doEverythingOnce(PrintWriter pw, Scanner scan, int sizeOfKey) {
        boolean itReceived = false;
        BigInteger confirmer;
        RsaInstance rsa = new RsaInstance(sizeOfKey);
        System.out.println("My pq = " + rsa.getPQString());
        System.out.println("My e = " + rsa.getEString());
        System.out.println("My d = " + rsa.getDString());
        System.out.println("Sending my key...");
        pw.println(rsa.getEString());
        pw.println(rsa.getPQString());
        pw.flush();

        System.out.println("Reading Bob's encrypted number...");
        BigInteger encryptedNumber = new BigInteger(scan.nextLine());
        System.out.println("Bob's encrypted number = " + encryptedNumber.toString());
        System.out.println("Decrypting Bob's number...");
        BigInteger decryptedNumber = rsa.decrypt(encryptedNumber);
        System.out.println("Bob's secret number = " + decryptedNumber.toString());
        pw.println(decryptedNumber.toString());
        pw.flush();  

        System.out.println("Sending key size...");
        pw.println(sizeOfKey);
        pw.flush();
        itReceived = Boolean.parseBoolean(scan.nextLine());
        if (itReceived) {
            System.out.println("Waiting for Bob's key...");
            BigInteger reverseE = new BigInteger(scan.nextLine());
            BigInteger reversePQ = new BigInteger(scan.nextLine());
            System.out.println("Bob's e = " + reverseE.toString());
            System.out.println("Bob's pq = " + reversePQ.toString());
            RsaInstance rsaReverse = new RsaInstance(reverseE, reversePQ);
            System.out.println("Creating a secret number...");
            confirmer = BigInteger.valueOf((int)(Math.random()*100) + 1);
            System.out.println("My secret number = " + confirmer.intValue());
            System.out.println("Encrypting my secret number...");
            BigInteger reverseEncrypted =  rsaReverse.encrypt(confirmer);
            System.out.println("My encrypted secret number = "
                               + reverseEncrypted.toString());

            System.out.println("Sending encrypted number to Bob...");
            pw.println(reverseEncrypted.toString());
            pw.flush();
            System.out.println("Waiting for Bob to decrypt my secret number...");
            BigInteger keyFinalised = new BigInteger(scan.nextLine());
            if (keyFinalised.equals(confirmer)) {
                System.out.println("Connection is secure!");
            } else {
                System.out.println("Bob could not decrypt. Connection is NOT secure!");
            }
        }
    }
}
