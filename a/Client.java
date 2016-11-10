import java.io.*;
import java.net.*;
import java.util.*;
import java.math.*;

public class Client {
    public static void main(String[] args) {
	Scanner scan;
        int  proceed;
        Thread st = null;
	Socket peerConnectionSocket = null;
        Scanner keyBoard = new Scanner(System.in);
        if (args.length != 2) {
            System.err.println("Usage: Client <address> <port>");
            return;
        }
        BigInteger unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
        PrintWriter pw = null;
        try {
            peerConnectionSocket = new Socket(args[0], Integer.parseInt(args[1]));

            pw = new PrintWriter(peerConnectionSocket.getOutputStream());
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
            pw.println(encrypted.toString());
            pw.flush();
	    /*   while(repeatChoice) {
		System.out.println("repeat process?");
                int choice = keyBoard.nextInt();
                if(choice == 1) { 
                  unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
                  System.out.println("Secret number = " + unCoded.intValue());
                  System.out.println("Waiting for key...");
                  encrypted = rsa.encrypt(unCoded);
                System.out.println("Encrypted number = " + encrypted.toString());     
                pw.println(encrypted.toString());
                }
                if(choice == 2)break;
		}*/
            //  pw.flush();
            System.out.println("Proceed? 1-yes, 0-no");
            proceed = Integer.parseInt(keyBoard.nextLine());
            pw.println(proceed);
            while(proceed==1){
                  unCoded = BigInteger.valueOf((int)(Math.random()*100) + 1);
		  //  System.out.println("e = " + keyE.toString());
		  // System.out.println("pq = " + keyPQ.toString());
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
	    // pw.flush();
           
            String fromSocket;
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
