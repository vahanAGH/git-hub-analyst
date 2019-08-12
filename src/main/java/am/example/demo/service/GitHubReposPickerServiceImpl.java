package am.example.demo.service;

import am.example.demo.model.GithubPublicRepo;
import am.example.demo.model.ReturnRepoData;
import am.example.demo.utils.HttpHeadersHelper;
import am.example.demo.utils.PageOrder;
import com.jcabi.github.Github;
import com.jcabi.http.RequestURI;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubReposPickerServiceImpl implements GitHubReposPickerService {

  private static final Logger logger = LoggerFactory.getLogger(GitHubReposPickerServiceImpl.class);
  private static final String SEARCH_PATH = "/search/repositories";

  @Autowired
  private Github github;

  @Override
  public ReturnRepoData searchRepo(String repoSearchCriteria, Integer perPage) throws IOException {
    logger.info(" Start search by criteria: - {}", repoSearchCriteria);

    final RequestURI requestURI = github.entry().uri().path(SEARCH_PATH);

    final Map<String, String> queryParams = new HashMap<>();
    queryParams.put("q", repoSearchCriteria);
    if (perPage != null) {
      queryParams.put("per_page", String.valueOf(perPage));
    }

    final JsonResponse resp = requestURI.queryParams(queryParams).back().fetch().as(JsonResponse.class);

    return composeReturnSearchData(resp);
  }

  @Override
  public ReturnRepoData getRepoByExactUrl(String url) throws IOException {
    logger.info(" Start search by exact url: - {}", url);
    URI uri = URI.create(url);
    final JsonResponse resp = github.entry()
        .uri().set(uri).back()
        .fetch()
        .as(JsonResponse.class);

    return composeReturnSearchData(resp);
  }

  private ReturnRepoData composeReturnSearchData(JsonResponse resp) {
    final JsonObject respAsJsonObject = resp.json().readObject();

    JsonNumber totalCountAsJsonNumber = respAsJsonObject.getJsonNumber("total_count");
    int totalCount = totalCountAsJsonNumber == null ? 0 : totalCountAsJsonNumber.intValue();

    boolean incompleteResults = respAsJsonObject.getBoolean("incomplete_results", false);

    final List<JsonObject> items = respAsJsonObject
        .getJsonArray("items")
        .getValuesAs(JsonObject.class);

    final List<GithubPublicRepo> githubPublicRepoList = new LinkedList<>();
    for (final JsonObject item : items) {
      final GithubPublicRepo gpp = new GithubPublicRepo(item);
      githubPublicRepoList.add(gpp);
    }

    List<String> linkHeader = resp.headers().get(HttpHeadersHelper.LINK);
    if (linkHeader != null) {

      final Map<PageOrder, String> paginationUrls = HttpHeadersHelper.transformLinkHeaderToMap(linkHeader);
      return new ReturnRepoData(totalCount, incompleteResults, paginationUrls, githubPublicRepoList);
    } else {

      return new ReturnRepoData(totalCount, incompleteResults, githubPublicRepoList);
    }
  }


}
