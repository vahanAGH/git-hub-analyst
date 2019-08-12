package am.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class Committer implements Serializable {

  public Long id;
  public String login;
  public String nodeId;
  public String name;
  public String email;
  /**
   * Date field like "2018-08-31T06:40:25Z"
   */
  public String date;


  public Committer() {
    // Using in case when object with empty fields must be created
  }

  public Committer(Long id, String login, String nodeId, String name, String email, String date) {
    this.id = id;
    this.login = login;
    this.nodeId = nodeId;
    this.name = name;
    this.email = email;
    this.date = date;
  }

  @Override
  public String toString() {
    return "Committer{" +
        "id=" + id +
        ", login='" + login + '\'' +
        ", nodeId='" + nodeId + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", date='" + date + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Committer committer = (Committer) o;
    return id.equals(committer.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
