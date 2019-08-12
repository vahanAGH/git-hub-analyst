package am.example.demo.service;

import am.example.demo.model.CachedCommitterData;
import am.example.demo.model.Committer;
import am.example.demo.utils.HttpHeadersHelper;
import am.example.demo.utils.HttpResponseHelper;
import com.jcabi.github.Github;
import com.jcabi.http.response.JsonResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GitHubCachedCommitsPickerServiceImpl implements GitHubCachedCommitsPickerService {

  private static final Logger logger = LoggerFactory.getLogger(GitHubCachedCommitsPickerServiceImpl.class);

  @Autowired
  private Github github;

  @Cacheable(cacheNames = "commitAuthorsCache")
  public CachedCommitterData getCommitAuthorsForRepoDefaultBranch(String url) throws IOException {
    logger.debug("===> No cache for url: " + url);
    URI uri = URI.create(url);
    final JsonResponse resp = github.entry()
        .uri().set(uri).back()
        .fetch()
        .as(JsonResponse.class);

    HttpResponseHelper.checkResponseHttpStatusCode(resp);
    List<Committer> committerList = HttpResponseHelper.transformCommitDataToCommitterList(resp);
    String nextUrl = HttpHeadersHelper.getNextUrl(resp);

    return new CachedCommitterData(committerList, nextUrl);
  }

}
