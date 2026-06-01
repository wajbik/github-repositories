package com.example.github_repositories;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitHubIntegrationTest {

    private static final WireMockServer wireMockServer =
            new WireMockServer(8089);

    @LocalServerPort
    int port;

    private final RestClient restClient = RestClient.create();

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
    }

    @AfterEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "github.api.url",
                () -> "http://localhost:8089"
        );
    }

    @Test
    void shouldReturnNonForkRepositories() {

        wireMockServer.stubFor(
                get(urlEqualTo("/users/wajbik/repos"))
                        .willReturn(
                                okJson("""
                                [
                                  {
                                    "name":"repo-one",
                                    "fork":false,
                                    "owner":{
                                      "login":"wajbik"
                                    }
                                  },
                                  {
                                    "name":"forked-repo",
                                    "fork":true,
                                    "owner":{
                                      "login":"wajbik"
                                    }
                                  }
                                ]
                                """)
                        )
        );

        wireMockServer.stubFor(
                get(urlEqualTo("/repos/wajbik/repo-one/branches"))
                        .willReturn(
                                okJson("""
                                [
                                  {
                                    "name":"main",
                                    "commit":{
                                      "sha":"abc123"
                                    }
                                  }
                                ]
                                """)
                        )
        );

        String response = restClient.get()
                .uri("http://localhost:" + port + "/repositories/wajbik")
                .retrieve()
                .body(String.class);

        assertThat(response)
                .contains("repo-one");

        assertThat(response)
                .doesNotContain("forked-repo");

        assertThat(response)
                .contains("abc123");
    }

    @Test
    void shouldReturn404WhenUserDoesNotExist() {

        wireMockServer.stubFor(
                get(urlEqualTo("/users/unknown/repos"))
                        .willReturn(
                                aResponse()
                                        .withStatus(404)
                        )
        );

        HttpClientErrorException exception =
                assertThrows(
                        HttpClientErrorException.class,
                        () -> restClient.get()
                                .uri("http://localhost:" + port + "/repositories/unknown")
                                .retrieve()
                                .body(String.class)
                );

        assertThat(exception.getStatusCode().value())
                .isEqualTo(404);

        assertThat(exception.getResponseBodyAsString())
                .contains("\"status\":404");

        assertThat(exception.getResponseBodyAsString())
                .contains("User not found");
    }
}