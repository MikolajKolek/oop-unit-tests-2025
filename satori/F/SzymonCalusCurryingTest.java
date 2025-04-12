import org.junit.Test;

import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class SzymonCalusCurryingTest {
    @Test
    public void apply_should_throw_with_too_little_arguments() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> a.apply());
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> a.apply(1));
    }

    @Test
    public void apply_should_throw_illegal_when_args_is_null() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.apply(null));
    }

//    @Test
//    public void apply_should_work_when_too_many_arguments() {
//        VarIntFunction a = A -> A[0] + A[1];
//        assertEquals(3, a.apply(1, 2, 3));
//    } // could be optional?

    @Test
    public void partial_should_throw_illegal_when_args_is_null() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.applyPartial(null));
        assertThrows(IllegalArgumentException.class, () -> a.applyPartial(1).applyPartial(null));
        assertThrows(IllegalArgumentException.class, () -> a.applyPartial(null).applyPartial(1));
        assertThrows(IllegalArgumentException.class, () -> a.applyPartial(1).applyPartial(null).apply());
    }

    @Test
    public void partial_should_return_original_when_no_args() {
        VarIntFunction a = A -> A[0] + A[1];
        assertEquals(a, a.applyPartial());

        VarIntFunction b = a.applyPartial(1);
        assertEquals(b, b.applyPartial());

        for(int i = 0; i < 1000; i++) {
            b = b.applyPartial();
        }
        assertEquals(b, b.applyPartial());
    }

    @Test
    public void partial_basic_tests() {
        VarIntFunction a = A -> (A[0] * A[1] + A[2]) * A[3];
        VarIntFunction b = B -> {
            int result = 0;
            for(int i = 0; i < 1000; i++) {
                result += B[i];
            }
            return result;
        };
        assertEquals(20, a.applyPartial(1, 2).applyPartial(3).applyPartial(4).apply());
        assertEquals(0,  a.applyPartial(1).applyPartial(2, 3).applyPartial(0).apply());

        int result = 0;
        for(int i = 0; i < 1000; i++) {
            result += i;
            b = b.applyPartial(i);
        }
        assertEquals(result, b.apply());
    }

    @Test
    public void unary_basic_test() {
        VarIntFunction inRange = x -> {
            int num = x[2];
            if(num >= x[0] && num <= x[1]) return num;
            return 0;
        };

        IntUnaryOperator between = inRange.applyPartial(10, 20).asUnary();
        assertArrayEquals(new int[] {0,0,0,0,0,0,0,0,0,0,10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,0,0,0,0,0,0,0,0,0}, IntStream.range(0, 30).map(between).toArray());
    }

    @Test
    public void permuted_args_should_throw_when_null_or_empty() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(null));
        assertThrows(IllegalArgumentException.class, a::withPermutedArgs);
    }

    @Test
    public void permuted_args_should_throw_when_incorrect_permutation() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1, 1));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1, 3));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(2, 3));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(0, 1));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1, 2, 3, 4, 3, 2, 1));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(-1));
    }

    @Test
    public void permuted_args_should_throw_when_applied_to_incorrect_number_of_arguments() {
        VarIntFunction a = A -> A[0] + A[1] + A[2];
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1, 2).applyPartial(1, 2).apply(3));
        assertThrows(IllegalArgumentException.class,
                () -> a.withPermutedArgs(1, 2, 3).applyPartial(1).applyPartial(2).withPermutedArgs(1, 2, 3).apply(3));
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1, 2, 3).apply(1, 2, 3, 4));
    }

    @Test
    public void permuted_args_should_chain_correctly() {
        VarIntFunction a = A -> (A[0] * A[1]) + A[2] + A[3] + (A[4] * A[5]);

        for(int i = 0; i < 6; i++) {
            a = a.withPermutedArgs(3, 4, 1, 5, 2, 6);
        } // identity

        assertEquals(39, a.apply(1, 2, 3, 4, 5, 6));

        a = a.withPermutedArgs(2, 5, 4, 1, 6, 3);

        assertEquals(23, a.apply(1, 2, 3, 4, 5, 6));

        for(int i = 0; i < 3; i++) {
            a = a.withPermutedArgs(3, 6, 1, 2, 4, 5);
        } // (412635)

        assertEquals(36, a.apply(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void arity_should_throw_when_negative() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.withRequiredArity(-1));
    }

    @Test
    public void arity_should_throw_when_arity_is_incorrect() {
        VarIntFunction a = A -> A[0] + A[1];
        assertThrows(IllegalArgumentException.class, () -> a.withRequiredArity(1).apply());
        assertThrows(IllegalArgumentException.class, () -> a.withRequiredArity(1).applyPartial(1).apply(2));
        assertThrows(IllegalArgumentException.class, () -> a.withRequiredArity(1).apply(1, 2, 3));
        assertEquals(3, a.withRequiredArity(2).applyPartial(1).apply(2));
    }
}
