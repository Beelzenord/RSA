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
        BigInteger[] primes = calculatePrimes();
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Waiting for connection...");
            peerConnectionSocket = ss.accept();

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

    private static BigInteger[] calculatePrimes() {
        BigInteger p = new BigInteger("5");
        BigInteger q = new BigInteger("2");
        return new BigInteger[] {p, q};
    }
}
