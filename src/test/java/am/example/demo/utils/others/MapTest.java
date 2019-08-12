package am.example.demo.utils.others;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class MapTest {

  @Test
  public void orderOfKeySetShouldCorrespondOrderOfValues() {
    Map<String, String> m = new HashMap<>();
    m.put("2", "2");
    m.put("1", "1");
    m.put("6", "6");
    m.put("8", "8");
    m.put("0", "0");
    m.put("6", "6");
    m.put("4", "4");
    m.put("7", "7");
    m.put("9", "9");
    m.put("3", "3");

    Set<String> k = m.keySet();
    Collection<String> v = m.values();
    Object[] keys = k.toArray();
    Object[] values = v.toArray();

    for (int i = 0; i < keys.length; i++) {
      assertEquals(keys[i], values[i]);
    }
  }
}
