import org.junit.Test;

import static org.junit.Assert.*;

public class MikolajKolekCurryingTest {
    @Test
    public void req_arity_throws_illegal_argument_exception_when_argument_is_null() {
        VarIntFunction a = A -> A[0];
        assertThrows(IllegalArgumentException.class, () -> a.withRequiredArity(1).applyAsInt(null));
    }

    @Test
    public void permuted_args_throws_illegal_argument_exception_when_argument_is_null() {
        VarIntFunction a = A -> A[0];
        assertThrows(IllegalArgumentException.class, () -> a.withPermutedArgs(1).applyAsInt(null));
    }

    @Test
    public void constant_functions_are_allowed() {
        VarIntFunction a = A -> 12;
        assertEquals(12, a.applyAsInt(new int[0]));
        assertEquals(12, a.apply());
    }

    @Test
    public void zero_argument_apply_partial_returns_original_function() {
        VarIntFunction f_xyz = A -> A[0] * A[0] + 2 * A[1] + A[2];
        assertEquals(10, f_xyz.applyPartial().apply(1, 2, 5));
    }
}
