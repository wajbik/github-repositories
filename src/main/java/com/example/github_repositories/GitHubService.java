package com.example.github_repositories;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubService {

    private final GitHubClient client;

    public GitHubService(GitHubClient client) {
        this.client = client;
    }

    public List<RepositoryResponse> getUserRepositories(String username) {

        List<GitHubRepoDto> repos = client.getUserRepos(username);

        return repos.stream()
                .filter(repo -> !repo.fork)
                .map(repo -> {

                    var branches = client.getBranches(repo.owner.login, repo.name)
                            .stream()
                            .map(b -> new BranchDto(b.name, b.commit.sha))
                            .toList();

                    return new RepositoryResponse(
                            repo.name,
                            repo.owner.login,
                            branches
                    );
                })
                .toList();
    }
}