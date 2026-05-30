package com.example.github_repositories;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClient {

    private final RestClient restClient;

    private static final String BASE_URL = "https://api.github.com";

    public GitHubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<GitHubRepoDto> getUserRepos(String username) {
        try {
            GitHubRepoDto[] response = restClient.get()
                    .uri(BASE_URL + "/users/{username}/repos", username)
                    .retrieve()
                    .body(GitHubRepoDto[].class);

            return Arrays.asList(response != null ? response : new GitHubRepoDto[0]);

        } catch (Exception e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<GitHubBranchDto> getBranches(String owner, String repo) {

        GitHubBranchDto[] response = restClient.get()
                .uri(BASE_URL + "/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .body(GitHubBranchDto[].class);

        return Arrays.asList(response != null ? response : new GitHubBranchDto[0]);
    }
}