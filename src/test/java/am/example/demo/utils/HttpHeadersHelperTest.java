package am.example.demo.utils;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class HttpHeadersHelperTest {

  /*
   * Expected format of HTTP header "Link":
   * <https://api.github.com/repositories?since=369>; rel="next",
   * <https://api.github.com/repositories{?since}>; rel="first",
   * <https://api.github.com/repositories?since=157>; rel="prev",
   * <https://api.github.com/repositories?since=900>; rel="last"
   */
  @Test
  public void getLinkHeaderAsMap_shouldFitExpectedResult() {
    final List<String> linkHeader = new ArrayList<>();
    linkHeader.add("<https://api.github.com/repositories{?since}>; rel=\"first\", <https://api.github.com/repositories?since=369>; rel=\"next\"," +
        "<https://api.github.com/repositories?since=157>; rel=\"prev\", <https://api.github.com/repositories?since=900>; rel=\"last\"");

    Map<PageOrder, String> result = HttpHeadersHelper.transformLinkHeaderToMap(linkHeader);

    assertEquals("https://api.github.com/repositories{?since}", result.get(PageOrder.first));
    assertEquals("https://api.github.com/repositories?since=369", result.get(PageOrder.next));
    assertEquals("https://api.github.com/repositories?since=157", result.get(PageOrder.prev));
    assertEquals("https://api.github.com/repositories?since=900", result.get(PageOrder.last));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getLinkHeaderAsMap_shouldThrowIllegalArgumentException_whenPageOrderIsFailed() {
    final List<String> linkHeader = new ArrayList<>();
    linkHeader.add("<https://api.github.com/repositories{?since}>; rel=\"furst\"");

    Map<PageOrder, String> result = HttpHeadersHelper.transformLinkHeaderToMap(linkHeader);

    assertEquals("https://api.github.com/repositories{?since}", result.get(PageOrder.first));
  }

  @Test
  public void getLinkHeaderAsMap_shouldReturnEmptyMap_whenInputParameterIsNull() {
    Map<PageOrder, String> result = HttpHeadersHelper.transformLinkHeaderToMap(null);
    assertEquals(0, result.size());
  }

  /*
   * Expected format of HTTP header "Link":
   *  <https://api.github.com/search/repositories?q=java&page=1>; rel="prev",
   *  <https://api.github.com/search/repositories?q=java&page=3>; rel="next",
   *  <https://api.github.com/search/repositories?q=java&page=34>; rel="last",
   *  <https://api.github.com/search/repositories?q=java&page=1>; rel="first"
   */
  @Test(expected = IllegalArgumentException.class)
  public void getLinkHeaderAsMap_shouldThrowIllegalArgumentException_whenInputParameterFormatIsNotValid() {
    final List<String> linkHeader = new ArrayList<>();
    linkHeader.add("https://api.github.com/repositories{?since}>; rel=\"first\", <https://api.github.com/repositories?since=3; rel=\"next\"," +
        "<https://api.github.com/repositories?since=1>; rel=\"prev\", <https://api.github.com/repositories?since=34>; rel=\"last\"");

    Map<PageOrder, String> result = HttpHeadersHelper.transformLinkHeaderToMap(linkHeader);

    assertEquals("https://api.github.com/repositories{?since}", result.get(PageOrder.first));
    assertEquals("https://api.github.com/repositories?since=3", result.get(PageOrder.next));
    assertEquals("https://api.github.com/repositories?since=1", result.get(PageOrder.prev));
    assertEquals("https://api.github.com/repositories?since=34", result.get(PageOrder.last));
  }


  @Test
  public void testURIQueryString() throws URISyntaxException {
    String url = "http://localhost/test?url=https://api.github.com/search/repositories?q=tetris&page=34";
    URI uri = new URI(url);
    String q = uri.getQuery();
    assertEquals("url=https://api.github.com/search/repositories?q=tetris&page=34", q);
  }
}
