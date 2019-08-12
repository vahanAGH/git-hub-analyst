package am.example.demo.controller;


import am.example.demo.model.ReturnRepoData;
import am.example.demo.service.GitHubReposPickerService;
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
public class GitHubSearchRepoController {

  @Autowired
  GitHubReposPickerService gitHubDataPickerService;

  /**
   * Search repo's info by criteria.
   *
   * @param criteria the search criteria
   * @return the list of repo
   * @throws ResourceNotFoundException the resource not found exception
   */
  @GetMapping("/repos")
  public ResponseEntity<ReturnRepoData> searchPublicRepoByCriteria(@RequestParam String criteria,
      @RequestParam(required = false, name = "per_page") Integer perPage)
      throws ResourceNotFoundException, IOException {

    final ReturnRepoData githubPublicProjectList = gitHubDataPickerService.searchRepo(criteria, perPage);

    return ResponseEntity.ok().body(githubPublicProjectList);
  }


  /**
   * Get repo by passed url
   *
   * @return the list of repo
   */
  @GetMapping("/repos/url")
  public ResponseEntity<ReturnRepoData> getPublicRepoByExactUrl(@RequestParam String url)
      throws ResourceNotFoundException, IOException {

    final ReturnRepoData githubPublicProjectList = gitHubDataPickerService.getRepoByExactUrl(url);

    return ResponseEntity.ok().body(githubPublicProjectList);
  }

}
