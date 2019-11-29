package am.example.demo.service;

import am.example.demo.model.CachedCommitterData;
import java.io.IOException;

public interface GitHubCachedCommitsPickerService {

  /**
   * Get commit authors from Github by url
   * @param url
   *
   * @return CachedCommitterData
   * @throws IOException if fetch data is failed
   */
  CachedCommitterData getCommitAuthorsForRepoDefaultBranch(String url) throws IOException;

}
