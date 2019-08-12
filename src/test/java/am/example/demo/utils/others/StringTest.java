package am.example.demo.utils.others;

public class StringTest {

  static String u = "https://api.github.com/repositories/33225130/commits?per_page=200&page=5";

  public static void main(String[] args) {
    String[] uArray = u.split("&page=");
    for (int i = 0; i < uArray.length; i++) {
      System.out.println(uArray[i]);
    }
  }

}
