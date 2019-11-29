package am.example.demo.service;

import am.example.demo.model.ReturnRepoData;
import java.io.IOException;

/**
 * GitHub API communicator to search public repos
 */
public interface GitHubReposPickerService {

  /**
   * Gather github public repos by search criteria
   * @param repoSearchCriteria
   * @param perPage
   *
   * @return ReturnRepoData object
   */
  ReturnRepoData searchRepo(String repoSearchCriteria, Integer perPage) throws IOException;

  /**
   * Get repos exact by passed url
   * @param url
   *
   * @return ReturnRepoData object
   */
  ReturnRepoData getRepoByExactUrl(String url) throws IOException;
}
