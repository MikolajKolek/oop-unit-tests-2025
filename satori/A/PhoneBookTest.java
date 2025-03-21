import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneBookTest {
    @Test
    public void testEmpty() {
        PhoneBook pb = new PhoneBook();
        assertTrue(pb.isEmpty());
        assertFalse(pb.isFull());
        assertEquals(0, pb.size());
        assertEquals(10, pb.capacity());

        PhoneBook empty = new PhoneBook();
        assertFalse(pb.contains(empty));
        assertTrue(pb.supersetOf(empty));
        assertTrue(pb.subsetOf(empty));
        assertEquals(pb, empty);
        assertEquals("{\n}\n", pb.toString());
    }

    @Test
    public void testAdd() {
        PhoneBook pb = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
        pb.add("111-22-33");
        pb.add("0222-333-333");
        assertTrue(pb.isEmpty());

        pb.add("222-333-333");
        pb.add("222444555");
        assertFalse(pb.isEmpty());
        assertEquals(1, pb.size());

        pb.add("123-456-789");
        PhoneBook group = new PhoneBook().add(pb);
        assertEquals(2, group.size());
    }

    @Test
    public void testFormat() {
        PhoneBook g1 = new PhoneBook(PhoneBook.NumberFormat.DIGITS).add("111222333");
        PhoneBook g2 = new PhoneBook(PhoneBook.NumberFormat.HYPHENED).add("111-222-444");
        PhoneBook pb = new PhoneBook(PhoneBook.NumberFormat.DIGITS).add(g1);
        assertTrue(pb.contains("111222333"));
        assertTrue(g2.contains("111-222-444"));
        assertFalse(pb.contains("111222444"));

        pb.add(g2);
        assertFalse(pb.contains("111-222-444"));
        assertTrue(pb.contains("111222444"));

        pb.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertTrue(pb.contains("111-222-444"));
        assertFalse(pb.contains("111222444"));

        assertEquals("""
                {
                  {
                    111-222-333
                  }
                  {
                    111-222-444
                  }
                }""", pb.toString().trim());
    }

    @Test
    public void testCopy() {
        PhoneBook g1 = new PhoneBook().add("111000001").add("111000002").add("111000003");
        PhoneBook g2 = g1.copyBook();
        g2.changeFormat(PhoneBook.NumberFormat.HYPHENED);
        assertEquals(3, g1.size());
        assertEquals(g1, g2);

        g2.add("222-000-001");
        assertEquals(3, g1.size());
        assertEquals(4, g2.size());
        assertNotEquals(g1, g2);
        assertTrue(g1.subsetOf(g2));
    }

    @Test
    public void testSubBooks() {
        PhoneBook g0 = new PhoneBook().add("111000001").add("111000003");
        PhoneBook g1 = new PhoneBook().add("111000001").add("111000002").add("111000003");
        PhoneBook g2 = new PhoneBook().add("222000001").add("222000002").add("222000003");
        PhoneBook pb1 = new PhoneBook(7).add(g1).add(g2);
        PhoneBook pb2 = new PhoneBook().add(g2).add(g1);

        assertNotEquals(pb1.toString(), pb2.toString());
        assertEquals(pb1, pb2);
        assertEquals(6, pb1.size());
        assertEquals("""
                        {
                          {
                            111000001
                            111000002
                            111000003
                          }
                          {
                            222000001
                            222000002
                            222000003
                          }
                        }
                        """,
                pb1.toString());
        assertFalse(pb1.contains(g0));
        assertFalse(pb1.supersetOf(g0));
        assertTrue(pb1.supersetOf(new PhoneBook().add(g1)));
        assertTrue(g1.supersetOf(g0));
        assertFalse(g1.contains(g0));
        assertTrue(pb1.contains("111000001"));
        assertTrue(pb1.contains("222000001"));
        assertFalse(pb1.contains("121000003"));

        PhoneBook revHyph = new PhoneBook(PhoneBook.NumberFormat.HYPHENED);
        revHyph.add("111-000-003").add("111-000-002").add("111-000-001");
        assertTrue(pb1.contains(revHyph));
    }
}
