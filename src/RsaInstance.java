import java.math.*;

public class RsaInstance {
    int size;

    public RsaInstance(int size) {
        this.size = size;
        BigInteger[] primes = calculatePrimes();
    }

    private BigInteger[] calculatePrimes() {
        BigInteger p = new BigInteger("5");
        BigInteger q = new BigInteger("2");
        return new BigInteger[] {p, q};
    }
}
