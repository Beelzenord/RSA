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
        BigInteger Confirmer;
        RsaInstance rsa = new RsaInstance(sizeOfKey);
        System.out.println("pq = " + rsa.getPQString());
        System.out.println("e = " + rsa.getEString());
        System.out.println("d = " + rsa.getDString());

        pw.println(rsa.getEString() + "\n" + rsa.getPQString());
        pw.flush();
        System.out.println("Reading encrypted number...");

        BigInteger encryptedNumber = new BigInteger(scan.nextLine());
        System.out.println("Encrypted number = " + encryptedNumber.toString());
        System.out.println("Decrypting number...");
        BigInteger decryptedNumber = rsa.decrypt(encryptedNumber);
        System.out.println("Secret number = " + decryptedNumber.toString());

        pw.println(decryptedNumber.toString());
        pw.flush();  
        System.out.println("Decoded is sent...");
        pw.println(sizeOfKey);
        pw.flush();
        itReceived = Boolean.parseBoolean(scan.nextLine());
        if (itReceived) {
            System.out.println("Ready to process reverse keys");
            BigInteger reverseE = new BigInteger(scan.nextLine());
            BigInteger reversePQ = new BigInteger(scan.nextLine());
            RsaInstance rsaReverse = new RsaInstance(reverseE, reversePQ);
            System.out.println("Done so far");
            Confirmer = BigInteger.valueOf((int)(Math.random()*100) + 1);
            System.out.println("Secret number: " + Confirmer.intValue());
            BigInteger reverseEncrypted =  rsaReverse.encrypt(Confirmer);
            System.out.println("Alice's encrypted number :" + reverseEncrypted.toString());

            pw.println(reverseEncrypted.toString());
            pw.flush();
            BigInteger keyFinalised = new BigInteger(scan.nextLine());
            if (keyFinalised.equals(Confirmer)) {
                System.out.println("Secure!");
            }
        }
    }
}
