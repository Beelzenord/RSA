import java.util.*;
import java.math.*;

public class RsaInstance {
    private int size;
    private BigInteger p;
    private BigInteger q;
    private BigInteger e;
    private BigInteger d;
    private BigInteger pq;
    private Random rng;
    private BigInteger p1q1;
    
    public RsaInstance(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Size is too small");
        }
        rng = new Random();
        this.size = size;
        calculatePrimes();
        pq = p.multiply(q);
        p1q1 = p.subtract(BigInteger.ONE)
            .multiply(q.subtract(BigInteger.ONE));
        calculateE();
        calculateD();
    }

    public RsaInstance(BigInteger e, BigInteger pq) {
        this.e = e;
        this.pq = pq;
    }

    private void calculatePrimes() {
        p = new BigInteger(size, 100, rng);
        q = new BigInteger(size, 100, rng);
    }

    public BigInteger encrypt(BigInteger number) {
       return number.pow(e.intValue()).mod(pq);
    }

     private void calculateE() {
        BigInteger eTemp = new BigInteger(20, rng);
        
        e = eTemp.gcd(pq);
        //boolean e;
        while(e.intValue()<=1){
        eTemp = new BigInteger(20, rng);
        e = eTemp.gcd(pq);
        }
    }

    private void calculateD() {
        d = e.modInverse(p1q1);
    }

    public String getEncKey(){
        return "" + e.toString() + "\n" + pq.toString() + "\n";
    }
}
