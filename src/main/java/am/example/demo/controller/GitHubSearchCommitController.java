package am.example.demo.controller;

import am.example.demo.model.ReturnCommitData;
import am.example.demo.model.ReturnCommitterData;
import am.example.demo.service.GitHubCommitsPickerService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
public class GitHubSearchCommitController {

  @Autowired
  GitHubCommitsPickerService gitHubCommitsPickerService;

  /**
   * Get commits for repo
   *
   * @param perPage pagination, count to return per request
   * @return List of commits for master branch of mentioned repo
   */
  @GetMapping("/commits")
  public ResponseEntity<ReturnCommitData> getCommitsForRepo(@RequestParam("repo_id") Long repoId,
      @RequestParam(required = false, name = "per_page") Integer perPage, @RequestParam(required = false, name = "page") Integer page)
      throws ResourceNotFoundException, IOException {

    final ReturnCommitData returnCommitData = gitHubCommitsPickerService.getCommitsForRepoDefaultBranch(repoId, perPage, page);

    return ResponseEntity.ok().body(returnCommitData);
  }

  /**
   * Get commits by passed url
   *
   * @return the list of commit
   */
  @GetMapping("/commits/url")
  public ResponseEntity<ReturnCommitData> getPublicRepoByExactUrl(@RequestParam String url)
      throws ResourceNotFoundException, IOException {

    final ReturnCommitData returnCommitData = gitHubCommitsPickerService.getCommitsByExactUrl(url);

    return ResponseEntity.ok().body(returnCommitData);
  }

  /**
   * Get commit's author for repo
   *
   * @param perPage pagination, count to return per request
   * @param page page number to return per request
   * @return List of committer for master branch of mentioned repo
   */
  @GetMapping("/commits/author")
  public ResponseEntity<ReturnCommitterData> getCommitsAuthorForRepo(@RequestParam("repo_id") Long repoId,
      @RequestParam(required = false, name = "per_page") Integer perPage, @RequestParam(required = false, name = "page") Integer page)
      throws ResourceNotFoundException, IOException {

    final ReturnCommitterData returnCommitterData = gitHubCommitsPickerService.getCommitAuthors(repoId, perPage, page);

    return ResponseEntity.ok().body(returnCommitterData);
  }

}
