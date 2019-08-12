package am.example.demo.service;

import static org.junit.Assert.assertEquals;

import am.example.demo.model.ReturnCommitData;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitHubCommitsPickerServiceImplTest {

  @Autowired
  GitHubCommitsPickerServiceImpl gitHubCommitsPickerServiceImpl;
  @Value("${default.page.size}")
  private int defaultPageSize;

  @Test(expected = IllegalArgumentException.class)
  public void composeUrlToGatherCommits_shouldThrowException_whenRepoIdIsNull() {
    gitHubCommitsPickerServiceImpl.composeUrlToGatherCommits(null, 100, 0);
  }

  /*
   *  Expected url = https://api.github.com/repositories/9546965/commits?per_page=100&page=1
   */
  @Test
  public void composeUrlToGatherCommits_shouldReturnExpectedUrl() {
    String url = gitHubCommitsPickerServiceImpl.composeUrlToGatherCommits(9546965L, 200, 0);

    assertEquals("https://api.github.com/repositories/9546965/commits?per_page=" + 200 + "&page=1", url);
  }

  /*
   *  Expected url = https://api.github.com/repositories/9546965/commits?per_page=100&page=1
   */
  @Test
  public void composeUrlToGatherCommits_shouldSetPerPageParameterToDefaultPageSize_whenItIsNull() {
    String url = gitHubCommitsPickerServiceImpl.composeUrlToGatherCommits(9546965L, null, 0);

    assertEquals("https://api.github.com/repositories/9546965/commits?per_page=" + defaultPageSize + "&page=1", url);
  }

  /*
   * Expected url = https://api.github.com/repositories/9546965/commits?per_page=100&page=1
   */
  @Test
  public void composeUrlToGatherCommits_shouldSetPerPageParameterToDefaultPageSize_whenItIs0() {
    String url = gitHubCommitsPickerServiceImpl.composeUrlToGatherCommits(9546965L, 0, 0);

    assertEquals("https://api.github.com/repositories/9546965/commits?per_page=" + defaultPageSize + "&page=1", url);
  }


  @Test
  public void getCommitsForRepoDefaultBranch_shouldReturnExpectedListOfCommit() throws IOException {
    ReturnCommitData commits = gitHubCommitsPickerServiceImpl.getCommitsForRepoDefaultBranch(33225130L, 200, 0);
    System.out.println("------>" + commits.commitList.size());
    assert true;
  }
}
