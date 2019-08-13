package am.example.demo.config;

import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableCaching
@EnableScheduling
@EnableSwagger2
public class MainConfig {

  private static final Logger logger = LoggerFactory.getLogger(MainConfig.class);

  @Autowired
  private CacheManager cacheManager;

  /**
   * Github client connection manager
   *
   * @param user for authentication / authorization
   * @param pass for authentication / authorization
   * @return Github object
   */
  @Bean
  public Github getConnectedGitHubObject(@Value("${user}") String user, @Value("${pass}") String pass) {
    Github github = new RtGithub(user, pass);
    logger.info(" ---> Connection to GitHub is OK.");
    return github;
  }

  /**
   * Scheduler to evict cache fixedDelay in milli sec Clear cache delay is set to 30 min for now
   */
  @Scheduled(fixedDelay = 1800000)
  public void evictAuthorsCache() {
    cacheManager.getCache("commitAuthorsCache").clear();
    logger.info("==> Cache 'commitAuthorsCache' has been evicted");
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
  }
}
