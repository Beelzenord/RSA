import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Server {
    public static void main(String[] args) {
	Scanner scan;
        Thread st = null;
	Socket peerConnectionSocket = null;
        if (args.length != 2) {
            System.err.println("Usage: Server <port> <keysize>");
            return;
        }
        RsaInstance rsa = new RsaInstance(Integer.parseInt(args[1]));
        System.out.println("pq = " + rsa.getPQString());
        System.out.println("e = " + rsa.getEString());
        System.out.println("d = " + rsa.getDString());
        PrintWriter pw = null;
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Waiting for connection...");
            peerConnectionSocket = ss.accept();

            pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            st = new Thread(new StringSender(pw));
            //st.start();
            scan = new Scanner(peerConnectionSocket.getInputStream());
            String fromSocket;
            pw.println(rsa.getEString() + "\n" + rsa.getPQString());
            pw.flush();
            System.out.println("Reading encrypted number...");
            BigInteger encryptedNumber = new BigInteger(scan.nextLine());
            System.out.println("Decrypting number...");
            int decryptedNumber = rsa.decrypt(encryptedNumber);
            System.out.println("Secret number = " + decryptedNumber);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (st != null) {
                st.stop();
            }
        }
    }
}
