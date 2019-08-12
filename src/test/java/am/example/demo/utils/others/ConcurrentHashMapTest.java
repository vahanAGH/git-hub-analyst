package am.example.demo.utils.others;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHashMapTest {

  static ConcurrentHashMap<Long, ConcurrentHashMap<Long, Object>> concurrentHashMap = new ConcurrentHashMap<>();


  public static void main(String[] args) {
    Runnable taskOne = () -> {
      ConcurrentHashMap<Long, Object> oneMap = new ConcurrentHashMap<>();
      for (int i = 0; i < 1; i++) {
        oneMap.put(Long.valueOf(1), "One");
      }
      concurrentHashMap.put(Long.valueOf(1), oneMap);

      if (!"One".equals(concurrentHashMap.get(Long.valueOf(1)).get(Long.valueOf(1)))) {
        System.out.println("taskOne is not One");
      }
    };

    Runnable taskTwo = () -> {
      ConcurrentHashMap<Long, Object> twoMap = new ConcurrentHashMap<>();
      for (int i = 0; i < 1; i++) {
        twoMap.put(Long.valueOf(1), "Two");
      }

      concurrentHashMap.put(Long.valueOf(1), twoMap);

      if (!"Two".equals(concurrentHashMap.get(Long.valueOf(1)).get(Long.valueOf(1)))) {
        System.out.println("taskTwo is not Two");
      }
    };

    ExecutorService service = Executors.newFixedThreadPool(1000);

    for (int i = 0; i < 100000; i++) {
      //   Thread t1 = new Thread(taskOne);
      //   Thread t2 = new Thread(taskTwo);
      if (i % 2 == 0) {
        service.submit(taskOne);
        //     t1.start();
      } else {
        //     t2.start();
        service.submit(taskTwo);
      }
    }
    service.shutdown();
  }
}

