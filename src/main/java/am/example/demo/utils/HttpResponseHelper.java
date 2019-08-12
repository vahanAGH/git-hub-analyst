package am.example.demo.utils;

import am.example.demo.model.Commit;
import am.example.demo.model.Committer;
import am.example.demo.model.ReturnCommitData;
import com.jcabi.http.response.JsonResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.json.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpResponseHelper {

  /**
   * Get List of committer from commits data representing as json response
   *
   * @param resp JsonResponse
   * @return List of Committer
   */
  public static List<Committer> transformCommitDataToCommitterList(JsonResponse resp) {
    List<Committer> committerList = resp.json().readArray().stream().map(commitInfo -> {
      JsonObject commitInfoAsJsonObject = commitInfo.asJsonObject();
      if (!commitInfoAsJsonObject.isEmpty()) {

        return composeCommitter(commitInfoAsJsonObject);
      } else {

        return null;
      }
    }).filter(out -> out != null && out.id != null).collect(Collectors.toList());

    return committerList;
  }

  /**
   * Compose committer from response JsonObject
   *
   * @param commitInfoAsJsonObject JsonObject
   * @return Committer or null if author of commit is null
   */
  public static Committer composeCommitter(JsonObject commitInfoAsJsonObject) {
    Committer committer = null;

    if (!commitInfoAsJsonObject.isNull("author")) {
      committer = new Committer();
      JsonObject committerAsJsonObject = commitInfoAsJsonObject.getJsonObject("author");
      committer.id = committerAsJsonObject.getJsonNumber("id").longValue();
      committer.login = committerAsJsonObject.getString("login");
      committer.nodeId = committerAsJsonObject.getString("node_id");

      if (!commitInfoAsJsonObject.isNull("commit")) {
        JsonObject embeddedCommitAsJsonObject = commitInfoAsJsonObject.getJsonObject("commit");

        if (!embeddedCommitAsJsonObject.isNull("author")) {
          JsonObject embeddedCommitterAsJsonObject = embeddedCommitAsJsonObject.getJsonObject("author");
          committer.name = embeddedCommitterAsJsonObject.getString("name");
          committer.email = embeddedCommitterAsJsonObject.getString("email");
          committer.date = embeddedCommitterAsJsonObject.getString("date");
        }
      }
    }

    return committer;
  }

  /**
   * Check response status codes here This method can throws exception to get know Rest container that something going wrong.
   *
   * @param resp JsonResponse
   * @throws IllegalArgumentException, ResponseStatusException
   */
  public static void checkResponseHttpStatusCode(JsonResponse resp) {
    if (resp == null) {
      throw new IllegalArgumentException(" JsonResponse cannot be null ");

    } else if (resp.status() == 409) {
      String errorMsg = resp.json().readObject().getString("message", "Unexpected error ...");
      throw new ResponseStatusException(HttpStatus.CONFLICT, errorMsg, null);

    } else if (resp.status() != 200) {
      throw new IllegalStateException(" Response status code is " + resp.status() + " but should be 200");

    } else {
      //DO nothing
    }

  }

  /**
   * Transform JsonResponse commit data to List of commit
   *
   * @param resp JsonResponse
   * @return List of Commit
   */
  public static List<Commit> transformCommitData(JsonResponse resp) {
    List<Commit> commits = resp.json().readArray().stream().map(commitInfo -> {
      JsonObject commitInfoAsJsonObject = commitInfo.asJsonObject();
      if (!commitInfoAsJsonObject.isEmpty()) {
        Committer committer = composeCommitter(commitInfoAsJsonObject);

        if (committer != null) {

          return new Commit(commitInfoAsJsonObject.getString("sha"), commitInfoAsJsonObject.getString("node_id"),
              commitInfoAsJsonObject.getString("url"), committer);
        }
      }
      return null;
    }).filter(out -> out != null).sorted((o1, o2) -> o2.date.compareTo(o1.date)).collect(Collectors.toList());

    return commits;
  }

  /**
   * Compose ReturnCommitData from JsonResponse
   *
   * @param resp JsonResponse
   * @return ReturnCommitData
   */
  public static ReturnCommitData composeReturnCommitData(JsonResponse resp) {
    List<Commit> commitList = transformCommitData(resp);

    List<String> linkHeader = resp.headers().get(HttpHeadersHelper.LINK);
    if (linkHeader != null) {

      final Map<PageOrder, String> paginationUrls = HttpHeadersHelper.transformLinkHeaderToMap(linkHeader);
      return new ReturnCommitData(commitList, paginationUrls);
    } else {

      return new ReturnCommitData(commitList);
    }
  }

}
