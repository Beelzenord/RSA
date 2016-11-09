import java.util.*;
import java.math.*;

public class RsaInstance {
    int size;
    BigInteger p;
    BigInteger q;
    BigInteger e;
    BigInteger d;
    Random rng;

    public RsaInstance(int size) {
        rng = new Random();
        this.size = size;
        calculatePrimes();
    }

    private void calculatePrimes() {
        p = new BigInteger(size, 100, rng);
        q = new BigInteger(size, 100, rng);
    }
}
