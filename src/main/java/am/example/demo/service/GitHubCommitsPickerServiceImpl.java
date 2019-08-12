package am.example.demo.service;

import am.example.demo.model.CachedCommitterData;
import am.example.demo.model.Commit;
import am.example.demo.model.Committer;
import am.example.demo.model.ReturnCommitData;
import am.example.demo.model.ReturnCommitterData;
import am.example.demo.utils.HttpHeadersHelper;
import am.example.demo.utils.HttpResponseHelper;
import com.jcabi.github.Github;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GitHubCommitsPickerServiceImpl implements GitHubCommitsPickerService {

  private static final Logger logger = LoggerFactory.getLogger(GitHubCommitsPickerServiceImpl.class);
  private static final String SEARCH_COMMITS_PATH = "/repositories/{repoId}/commits";

  @Value("${default.page.size}")
  private int defaultPageSize;

  @Value("${default.repoAndCommitter.page.size}")
  private int defaultRepoAndCommitterPageSize;

  @Value("${github.url}")
  private String githubUrl;

  @Autowired
  private Github github;

  @Autowired
  private GitHubCachedCommitsPickerService gitHubCachedCommitsPickerService;

  @Override
  public ReturnCommitterData getCommitAuthors(Long repoId, Integer perPage, Integer page) throws IOException {
    logger.info("=> Start to gather author of commits for repo: - {}", repoId);

    final int pp = (perPage == null || perPage == 0) ? defaultRepoAndCommitterPageSize : perPage; // 10
    final int p = (page == null || page == 0) ? 1 : page; // 18
    final int toIndex = pp * p; // 180

    final Set<Committer> cachedCommitterSet = new LinkedHashSet<>();
    String url = composeUrlToGatherCommits(repoId, defaultPageSize, 1);
    boolean isShownAdditionalPageLink = false;

    while (true) {
      CachedCommitterData cachedCommitterData = gitHubCachedCommitsPickerService.getCommitAuthorsForRepoDefaultBranch(url);
      // exclude duplicate commit's author
      cachedCommitterSet.addAll(cachedCommitterData.committerS);
      logger.debug(
          "==> Return commits' author from Github repo: " + repoId + ", page:" + p + ", per_page:" + pp + ", count:" + cachedCommitterSet.size());

      if (cachedCommitterData.nextUrl == null) { // stop gathering new commit's author
        break;
      } else if (cachedCommitterSet.size() > toIndex) { // stop gathering new commit's author
        break;
      } else if (cachedCommitterSet.size() == toIndex) {  // stop gathering new commit's author and show additional page link
        isShownAdditionalPageLink = true;
        break;
      } else { // continue with new url
        url = cachedCommitterData.nextUrl;
      }
    }

    return new ReturnCommitterData(cachedCommitterSet, isShownAdditionalPageLink);
  }

  @Override
  public ReturnCommitData getCommitsForRepoDefaultBranch(Long repoId, Integer perPage, Integer page) throws IOException {
    logger.info(" Start to gather commits for repo: - {}", repoId);

    final String commitUrl = composeUrlToGatherCommits(repoId, perPage, page);

    return getCommitsByExactUrl(commitUrl);
  }

  @Override
  public ReturnCommitData getCommitsByExactUrl(String url) throws IOException {
    logger.info(" Start to gather commits by exact url: - {}", url);
    URI uri = URI.create(url);
    final JsonResponse resp = github.entry()
        .uri().set(uri).back()
        .fetch()
        .as(JsonResponse.class);

    HttpResponseHelper.checkResponseHttpStatusCode(resp);
    return HttpResponseHelper.composeReturnCommitData(resp);
  }

  /*
   * Compose request url to Github by adding query string param to main Github commit endpoint
   * @param repoId added as query string param
   * @param perPage added as query string param
   * @param page added as query string param
   * @return url as String
   * Scope friendly for testing reason
   */
  String composeUrlToGatherCommits(Long repoId, Integer perPage, Integer page) {
    if (repoId == null) {
      throw new IllegalArgumentException(" repoId cannot be null ");
    }
    final String sPerPage = (perPage == null || perPage == 0) ? String.valueOf(defaultPageSize) : String.valueOf(perPage);
    final String sPage = (page == null || page == 0) ? "1" : String.valueOf(page);
    final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(githubUrl).path(SEARCH_COMMITS_PATH).queryParam("per_page", sPerPage)
        .queryParam("page", sPage);

    return uriBuilder.buildAndExpand(repoId).toUriString();
  }

  /*
   * Recursively calls till there is next url in Link header to obtain all commits for mentioned url (repo)
   * @param url endpoint to get a call to Github service
   * @param allCommits container to keep commit
   * @throws IOException if there is issue to fetch data
   */
  private void getAllCommitsForRepoDefaultBranch(String url, List<Commit> allCommits) throws IOException {
    logger.info(" Start to gather commits by exact url: - {}", url);
    URI uri = URI.create(url);
    final JsonResponse resp = github.entry()
        .uri().set(uri).back()
        .fetch()
        .as(JsonResponse.class);

    HttpResponseHelper.checkResponseHttpStatusCode(resp);
    allCommits.addAll(HttpResponseHelper.transformCommitData(resp));

    String nextUrl = HttpHeadersHelper.getNextUrl(resp);
    if (StringUtils.hasText(nextUrl)) {
      getAllCommitsForRepoDefaultBranch(nextUrl, allCommits);
    }
  }

  /*
   * Recursively calls till there is next url in Link header and Set size less than required amount of committer.
   * @param url endpoint to get a call to Github service
   * @param committerSet container to keep committer
   * @param requiredAmount required amount of committer
   * @throws IOException if there is issue to fetch data
   */
  private void getCommitAuthorsForRepoDefaultBranch(String url, Set<Committer> committerSet, int requiredAmount) throws IOException {
    URI uri = URI.create(url);
    final JsonResponse resp = github.entry()
        .uri().set(uri).back()
        .fetch()
        .as(JsonResponse.class);

    HttpResponseHelper.checkResponseHttpStatusCode(resp);
    List<Committer> committerList = HttpResponseHelper.transformCommitDataToCommitterList(resp);
    committerSet.addAll(committerList);

    String nextUrl = HttpHeadersHelper.getNextUrl(resp);
    if (committerSet.size() < requiredAmount && nextUrl != null) {
      getCommitAuthorsForRepoDefaultBranch(nextUrl, committerSet, requiredAmount);
    }
  }

}
