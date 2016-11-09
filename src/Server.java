import java.io.*;
import java.net.*;
import java.util.*;

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
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Waiting for connection...");
            peerConnectionSocket = ss.accept();

            PrintWriter pw = new PrintWriter(peerConnectionSocket.getOutputStream());
            st = new Thread(new StringSender(pw));
            st.start();
            scan = new Scanner(peerConnectionSocket.getInputStream());
            String fromSocket;
            pw.println(rsa.getEncKey());
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
