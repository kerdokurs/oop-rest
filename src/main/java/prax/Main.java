package prax;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
  private static final String URL = "https://api.github.com/users/";

  public static void main(String[] args) throws IOException, InterruptedException {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    final HttpResponse<InputStream> response = getDataByUsername("kerdokurs");
    final Data data = mapper.readValue(response.body(), Data.class);

    final Logger logger = LogManager.getLogger();
    logger.info("JSON: {}", data::toString);
  }

  private static HttpResponse<InputStream> getDataByUsername(final String username) throws IOException, InterruptedException {
    final HttpClient client = HttpClient.newHttpClient();

    final HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(URL + username))
      .header("User-Agent", "Mozilla/5.0")
      .GET()
      .build();

    return client.send(request, HttpResponse.BodyHandlers.ofInputStream());
  }

  private static class Data {
    @Getter
    public final String login;
    @Getter
    public final String url;
    @Getter
    public final String publicRepos;
    @Getter
    public final String createdAt;

    public Data(@JsonProperty("login") final String login, @JsonProperty("url") final String url, @JsonProperty("public_repos") final String publicRepos, @JsonProperty("created_at") final String createdAt) {
      this.login = login;
      this.url = url;
      this.publicRepos = publicRepos;
      this.createdAt = createdAt;
    }

    @Override
    public String toString() {
      return "Data{\n" +
        "  login='" + login + "',\n" +
        "  url='" + url + "',\n" +
        "  publicRepos='" + publicRepos + "',\n" +
        "  createdAt='" + createdAt + "'\n" +
        '}';
    }
  }
}
