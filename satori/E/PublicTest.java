import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestPublic {

    @Test
    public void test1() throws GenericFunctionsException {
        Function<String, String> f = Functions.constant("Ala");
        assertEquals("Ala", f.compute(Collections.<String> emptyList()));
        assertEquals((Double) 123.0, Functions.constant(123.0).compute(
                Collections.<Double> emptyList()));
        try {
            System.out.println(Functions.constant("Ala").compute(
                    Arrays.asList("Ala")));
            fail();
        } catch (GenericFunctionsException e) {
            System.out.println("Incorrect number of arguments");
        }
    }

    @Test
    public void test2() throws GenericFunctionsException {
        Function<Integer, Integer> f = Functions.proj(3, 0);
        assertEquals(1, (long) f.compute(Arrays.asList(1, 2, 3)));
        assertEquals("ma", Functions.<String, String> proj(2, 1).compute(
                Arrays.asList("Ala", "ma")));
        try {
            System.out.println(Functions.<String, String> proj(2, 1).compute(
                    Arrays.asList("Ala")));
            fail();
        } catch (GenericFunctionsException e) {
            System.out.println("Too few arguments");
        }
    }

    // ----------------------------------------------------------------------
    public static class StrRvs implements Function<String, String> {
        @Override
        public int arity() { return 1;     }
        @Override
        public String compute(List<? extends String> args) throws GenericFunctionsException {
            if (args == null || args.size() != arity()) throw new GenericFunctionsException();
            return new StringBuilder(args.get(0)).reverse().toString();
        }
    }

    public static class StrConcat implements Function<String, String> {
        @Override
        public int arity() { return 2; }
        @Override
        public String compute(List<? extends String> args) throws GenericFunctionsException {
            if (args == null || args.size() != arity()) throw new GenericFunctionsException();
            return args.get(0).toString() + args.get(1);
        }
    }
    // ----------------------------------------------------------------------

    @Test
    public void test3() throws GenericFunctionsException {
        Function<String, String> f = new StrRvs();
        List<String> ala = Arrays.asList("Ala");
        assertEquals("alA",f.compute(ala));
        f = Functions.compose(f, Arrays.asList(f));
        assertEquals("Ala",f.compute(ala));
        f = Functions.compose(new StrRvs(), Arrays.asList(f));
        assertEquals("alA",f.compute(ala));
    }

    @Test
    public void test4() throws GenericFunctionsException {
        Function<String, String> f = Functions.compose(new StrRvs(),
                Arrays.asList(new StrRvs()));
        f = Functions.compose(new StrConcat(), Arrays.asList(f, new StrRvs()));
        assertEquals("AlaalA",f.compute(Arrays.asList("Ala")));
        f = Functions
                .compose(new StrConcat(), Arrays.asList(new StrConcat(), new StrConcat()));
        assertEquals("****",f.compute(Arrays.asList("*","*")));
    }

    @Test
    public void test5() throws GenericFunctionsException {
        Function<String, String> f = Functions.compose(new StrRvs(),
                Arrays.asList(new StrRvs()));
        f = Functions.compose(new StrConcat(), Arrays.asList(f, new StrRvs()));
        assertEquals("AlaalA",f.compute(Arrays.asList("Ala")));
        assertEquals("ma",Functions.proj(3, 1).compute(
                Arrays.asList("Ala", "ma", "kota")));
        f = Functions.compose(
                new StrConcat(),
                Arrays.asList(Functions.<String, String> proj(2, 0),
                        Functions.<String, String> proj(2, 1)));
        assertEquals("AlaMa",f.compute(Arrays.asList("Ala", "Ma")));
        f = Functions.compose(f, Arrays.asList(
                Functions.compose(f, Arrays.asList(
                        Functions.<String, String> proj(4, 0),
                        Functions.<String, String> proj(4, 1))),
                Functions.compose(f, Arrays.asList(
                        Functions.<String, String> proj(4, 2),
                        Functions.<String, String> proj(4, 3)))));
        assertEquals("AlaMaKota?",f.compute(Arrays.asList("Ala", "Ma", "Kota", "?")));
        f = Functions.compose(
                f,
                Arrays.asList(Functions.<String, String> proj(1, 0),
                        Functions.<String, String> proj(1, 0),
                        Functions.<String, String> proj(1, 0),
                        Functions.<String, String> proj(1, 0)));
        assertEquals("++++",f.compute(Arrays.asList("+")));
    }
}
