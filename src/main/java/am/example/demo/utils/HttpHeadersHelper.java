package am.example.demo.utils;

import com.jcabi.http.response.JsonResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class HttpHeadersHelper {

  public static final String LINK = "Link";

  private static final String URL_LINK_DELIMITER = ",";
  private static final Logger logger = LoggerFactory.getLogger(HttpHeadersHelper.class);

  /**
   * It helps to transform GitHub API 'Link' header to Map. GitHub API 'Link' header contains urls of next, prev, first, last pages of result. It is
   * helpful for pagination of search result.
   *
   * @param headerLink List<String>
   * @return Map<PageOrder, String>
   */
  public static Map<PageOrder, String> transformLinkHeaderToMap(List<String> headerLink) {
    final Map<PageOrder, String> linkedUrlMap = new HashMap<>();

    if (CollectionUtils.isEmpty(headerLink)) {

      logger.warn(" The Header 'Link' is absent in HTTP response. ");
    } else {

      final String[] firstHeaderWithNameLink = headerLink.get(0).split(URL_LINK_DELIMITER);
      for (int i = 0; i < firstHeaderWithNameLink.length; i++) {

        final int openTagIndex = firstHeaderWithNameLink[i].indexOf('<') + 1;
        final int closeTagIndex = firstHeaderWithNameLink[i].lastIndexOf('>');
        final String url = (openTagIndex == -1 || closeTagIndex == -1) ? null : firstHeaderWithNameLink[i].substring(openTagIndex, closeTagIndex);

        final int startRelValueIdx = firstHeaderWithNameLink[i].indexOf("rel=\"") + "rel=\"".length();
        final int endRelValueIdx = firstHeaderWithNameLink[i].lastIndexOf("\"");
        final PageOrder pageOrder = (startRelValueIdx == -1 || endRelValueIdx == -1) ? null
            : PageOrder.valueOf(firstHeaderWithNameLink[i].substring(startRelValueIdx, endRelValueIdx));

        if (pageOrder == null || url == null) {
          throw new IllegalArgumentException(" Incoming HTTP header 'Link' format is not valid. ");
        } else {
          linkedUrlMap.put(pageOrder, url);
        }
      }

    }
    return linkedUrlMap;
  }

  /**
   * Not used yet, excess method It helps to generate GitHub API 'Link' header map to our app headers to return to web client.
   *
   * @param pageOrderUrlMap Map<PageOrder, Strirng>
   * @return org.springframework.http.HttpHeaders object
   */
  public static HttpHeaders generatePageOrderHttpHeaders(Map<PageOrder, String> pageOrderUrlMap) {
    HttpHeaders httpHeaders = new HttpHeaders();

    if (CollectionUtils.isEmpty(pageOrderUrlMap)) {
      logger.warn(" pageOrderUrlMap parameter is null or empty ");
    } else {
      pageOrderUrlMap.forEach((k, v) -> {
        httpHeaders.add(LINK.concat("-").concat(k.name()), v);
      });
    }
    return httpHeaders;
  }

  /**
   * Return next url if any from <code>Link</code> response header or null if next url is absent.
   *
   * @param resp JsonResponse
   * @return next url as String
   */
  public static String getNextUrl(JsonResponse resp) {
    if (resp == null) {
      throw new IllegalArgumentException(" JsonResponse cannot be null ");
    }
    String nextUrl = null;

    Map<String, List<String>> httpHeaders = resp.headers();
    List<String> link = httpHeaders.get(HttpHeadersHelper.LINK);
    if (!CollectionUtils.isEmpty(link)) {
      Map<PageOrder, String> paginationUrlMap = transformLinkHeaderToMap(link);
      nextUrl = paginationUrlMap.get(PageOrder.next);
    }
    return nextUrl;
  }

  /**
   * Get current page for pagination from url
   *
   * @param url String
   * @return page number
   */
  public static int getPageFromUrl(String url) {
    if (url == null) {
      throw new IllegalArgumentException(" Url cannot be null ");
    }
    String[] urlArray = url.split("&page=");
    if (url.length() > 0) {
      return Integer.valueOf(urlArray[1]).intValue();
    } else {
      return 0;
    }
  }
}
