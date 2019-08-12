package am.example.demo.model;

import am.example.demo.utils.PageOrder;
import java.util.List;
import java.util.Map;

public class ReturnRepoData {

  public int totalCount;
  public boolean incompleteResults;
  public Map<PageOrder, String> pagination;
  public List<GithubPublicRepo> githubRepoList;

  public ReturnRepoData(int totalCount, boolean incompleteResults, List<GithubPublicRepo> githubRepoList) {
    this.totalCount = totalCount;
    this.incompleteResults = incompleteResults;
    this.githubRepoList = githubRepoList;
  }

  public ReturnRepoData(int totalCount, boolean incompleteResults, Map<PageOrder, String> pagination,
      List<GithubPublicRepo> githubRepoList) {
    this.totalCount = totalCount;
    this.incompleteResults = incompleteResults;
    this.pagination = pagination;
    this.githubRepoList = githubRepoList;
  }
}
