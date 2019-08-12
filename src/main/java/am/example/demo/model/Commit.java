package am.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class Commit implements Serializable {

  public String sha;
  public String nodeId;
  public String url;
  public Committer committer;
  /**
   * Date field like "2018-08-31T06:40:25Z"
   */
  public String date;

  public Commit(String sha, String nodeId, String url, Committer committer) {
    this.sha = sha;
    this.nodeId = nodeId;
    this.url = url;
    this.committer = committer;
    this.date = committer.date;
  }

  @Override
  public String toString() {
    return "Commit{" +
        "sha='" + sha + '\'' +
        ", nodeId='" + nodeId + '\'' +
        ", url='" + url + '\'' +
        ", committer=" + committer +
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
    Commit commit = (Commit) o;
    return sha.equals(commit.sha) &&
        nodeId.equals(commit.nodeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sha, nodeId);
  }
}
