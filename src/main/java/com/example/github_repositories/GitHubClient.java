package com.example.github_repositories;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.beans.factory.annotation.Value;

@Component
public class GitHubClient {

    private final RestClient restClient;

    private final String githubApiUrl;

    public GitHubClient(
            RestClient restClient,
            @Value("${github.api.url}") String githubApiUrl
    ) {
        this.restClient = restClient;
        this.githubApiUrl = githubApiUrl;
    }

    public List<GitHubRepoDto> getUserRepos(String username) {
        try {
            GitHubRepoDto[] response = restClient.get()
                    .uri(githubApiUrl + "/users/{username}/repos", username)
                    .retrieve()
                    .body(GitHubRepoDto[].class);

            return Arrays.asList(response != null ? response : new GitHubRepoDto[0]);

        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<GitHubBranchDto> getBranches(String owner, String repo) {

        GitHubBranchDto[] response = restClient.get()
                .uri(githubApiUrl + "/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .body(GitHubBranchDto[].class);

        return Arrays.asList(response != null ? response : new GitHubBranchDto[0]);
    }
}