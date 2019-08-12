package am.example.demo.model;

import javax.json.JsonObject;


public class GithubPublicRepo {

  public Long id;
  public String nodeId;
  public String name;
  public String fullName;
  public Long ownerId;
  public String ownerLogin;
  public String ownerNodeId;
  public String ownerType;
  public String htmlUrl;
  public String description;
  public String commitsUrl;
  public String defaultBranch;

  public GithubPublicRepo() {
    // Do nothing
  }


  public GithubPublicRepo(Long id, String nodeId, String name, String fullName, Long ownerId, String ownerLogin, String ownerNodeId,
      String ownerType, String htmlUrl, String description, String commitsUrl, String defaultBranch) {
    this.id = id;
    this.nodeId = nodeId;
    this.name = name;
    this.fullName = fullName;
    this.ownerId = ownerId;
    this.ownerLogin = ownerLogin;
    this.ownerNodeId = ownerNodeId;
    this.ownerType = ownerType;
    this.htmlUrl = htmlUrl;
    this.description = description;
    this.commitsUrl = commitsUrl;
    this.defaultBranch = defaultBranch;
  }

  public GithubPublicRepo(JsonObject joRepo) {
    if (joRepo == null) {

      throw new IllegalArgumentException(" JsonObject cannot be null. ");
    } else {

      this.id = joRepo.getJsonNumber("id").longValue();
      this.nodeId = joRepo.getString("node_id");
      this.name = joRepo.getString("name");
      this.fullName = joRepo.getString("full_name");
      JsonObject owner = joRepo.getJsonObject("owner");
      this.ownerId = owner.getJsonNumber("id").longValue();
      this.ownerLogin = owner.getString("login");
      this.ownerNodeId = owner.getString("node_id");
      this.ownerType = owner.getString("type");
      this.htmlUrl = joRepo.getString("html_url");
      this.description = joRepo.getString("description", "");
      this.commitsUrl = joRepo.getString("commits_url");
      this.defaultBranch = joRepo.getString("default_branch");
    }
  }

}
