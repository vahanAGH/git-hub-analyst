package am.example.demo.service;

import am.example.demo.model.ReturnCommitData;
import am.example.demo.model.ReturnCommitterData;
import java.io.IOException;

public interface GitHubCommitsPickerService {

  /**
   * Get list of author of commits (committer) for repository
   *
   * @param repoId repository id
   * @param perPage count of unique committer to return
   * @return ReturnCommitterData
   */
  ReturnCommitterData getCommitAuthors(Long repoId, Integer perPage, Integer page) throws IOException;

  /**
   * Gather commits for repo's default branch, ordinary it is master
   *
   * @return ReturnCommitData
   */
  ReturnCommitData getCommitsForRepoDefaultBranch(Long repoId, Integer perPage, Integer page) throws IOException;

  /**
   * Gather commits using exact url
   *
   * @return ReturnCommitData
   */
  ReturnCommitData getCommitsByExactUrl(String url) throws IOException;
}
