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
        calculateE();
        calculateD();
    }

    public RsaInstance(BigInteger e, BigInteger pq) {
        this.e = e;
        this.pq = pq;
    }

    private void calculatePrimes() {
        BigInteger p, q;
        p = new BigInteger(size, 100, rng);
        q = new BigInteger(size, 100, rng);
        pq = p.multiply(q);
        p1q1 = p.subtract(BigInteger.ONE)
            .multiply(q.subtract(BigInteger.ONE));
    }

    public BigInteger encrypt(BigInteger number) {
        return number.modPow(e, pq);
    }

    public BigInteger decrypt(BigInteger number) {
        return number.modPow(d, pq);
    }

    private void calculateE() {
        BigInteger eTemp;
        do {
            eTemp = new BigInteger(size - 1, rng);
        } while (!eTemp.gcd(p1q1).equals(BigInteger.ONE)
                 || eTemp.equals(BigInteger.ZERO));
        e = eTemp;
    }

    private void calculateD() {
        d = e.modInverse(p1q1);
    }

    public String getEString(){
        return e.toString();
    }

    public String getPQString() {
        return pq.toString();
    }

    public String getDString() {
        return d.toString();
    }
}
