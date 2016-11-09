import java.util.*;
import java.math.*;

public class RsaInstance {
    int size;
    BigInteger p;
    BigInteger q;
    BigInteger e;
    BigInteger d;
    BigInteger pq;
    Random rng;

    public RsaInstance(int size) {
        if (size < 3) {
            throw new IllegalArgumentException("Size is too small");
        }
        rng = new Random();
        this.size = size;
        calculatePrimes();
        pq = p.multiply(q);
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
        e = new BigInteger(size-1, 100, rng);
    }
    
    private void calculateD() {
        BigInteger p1q1 = p.subtract(BigInteger.ONE)
            .multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(p1q1);
    }

    public String getEncKey(){
        return "" + e.toString() + "\n" + pq.toString() + "\n";
    }
}
