package am.example.demo.utils.others;

import static java.lang.Thread.sleep;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLockTest {

  static Map<String, String> map = new HashMap<>();


  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    ReentrantLock lock = new ReentrantLock();

    executor.submit(() -> {
      lock.lock();
      try {
        sleep(5000);
        map.put("foo", "bar");
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    });

    Runnable readTask = () -> {
      lock.lock();
      try {
        sleep(2000);
        System.out.println(map.get("foo"));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    };

    executor.submit(readTask);
    executor.submit(readTask);

    executor.shutdown();


  }
}

