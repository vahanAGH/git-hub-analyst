package am.example.demo.utils.others;

import static org.junit.Assert.assertEquals;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;


public class BasicTextEncryptorTest {

  private static final String STRING_TO_ENCRYPT = "bd4433f19c56d3874fd7a77c80da39b5e1578436";
  private static final String password = "just_for_demo";

  @Test
  public void checkEncryption(){
    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    textEncryptor.setPassword(password);
    String encryptedText = textEncryptor.encrypt(STRING_TO_ENCRYPT);
    System.out.println("EncryptedText: " + encryptedText);
    assertEquals(STRING_TO_ENCRYPT,  decrypt(encryptedText));
  }

  private String decrypt(String text) {
    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    textEncryptor.setPassword(password);
    String decryptedText = textEncryptor.decrypt(text);
    System.out.println("DecryptedText: " + decryptedText);
    return decryptedText;
  }

}
