import org.junit.Test;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class PublicTest {
    @Test
    public void simpleTest() {
        VarIntFunction f_xyz = A -> A[0] * A[0] + 2 * A[1] + A[2]; // x^2 + 2y + z
        assertEquals(10, f_xyz.apply(1, 2, 5));
        f_xyz.apply(1, 2, 3, 4);

        VarIntFunction f_xy5 = f_xyz.withPermutedArgs(3, 1, 2).applyPartial(5);
        assertEquals(10, f_xy5.apply(1, 2));

        VarIntFunction f_5yz = f_xyz.applyPartial(5);
        assertEquals(29, f_5yz.apply(1, 2));

        assertThrows(ArrayIndexOutOfBoundsException.class, () ->
                f_xyz.apply(1, 2));
        assertThrows(IllegalArgumentException.class, () ->
                f_xyz.withRequiredArity(3).apply(1, 2));
    }

    @Test
    public void testDivisors() {
        // returns 0 if last number is divisible by any previous, and last number otherwise
        VarIntFunction divisors_last = x -> {
            int num = x[x.length - 1];
            for (int i = 0; i < x.length - 1; ++i) {
                if (num % x[i] == 0)
                    return 0;
            }
            return num;
        };
        IntUnaryOperator isOdd = divisors_last.applyPartial(2).asUnary();
        IntUnaryOperator smallSieve = divisors_last
                .applyPartial(2, 3, 5, 7, 11, 13, 17, 19)
                .asUnary();

        assertArrayEquals(
                new int[]{0, 1, 0, 3, 0, 5, 0, 7, 0, 9},
                IntStream.range(0, 10).map(isOdd).toArray()
        );
        assertArrayEquals(
                new int[]{31, 0, 0, 37, 0, 41, 43, 0, 47, 0, 0, 53, 0, 0, 59, 61, 0, 0, 67, 0},
                IntStream.iterate(31, i -> i + 2).limit(20).map(smallSieve).toArray()
        );
    }
}
