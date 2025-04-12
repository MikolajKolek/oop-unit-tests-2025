/**
* PO 2024/25, Problem F - Rozwijanie Funkcji
*
* @author YOUR NAME
*/

import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

@FunctionalInterface
interface VarIntFunction extends ToIntFunction<int[]> {
    default int apply(int... args) {
        return 0;
    }

    default VarIntFunction applyPartial(int... partialArgs) {
        return null;
    }

    /**
     * @return this function as a unary function (int -> int)
     */
    default IntUnaryOperator asUnary() {
        return null;
    }

    /**
     * @param argsOrder permutation of (1, 2, ..., n)
     * @return function which first permutes the arguments as given by argsOrder, and then applies original function
     */
    default VarIntFunction withPermutedArgs(int... argsOrder) {
        return null;
    }

    /**
     * @param reqArity non-negative integer representing required number of arguments
     * @return function which throws if applied to the number of arguments different than reqArity
     */
    default VarIntFunction withRequiredArity(int reqArity) {
        return null;
    }
}
