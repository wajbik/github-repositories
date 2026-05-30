package com.example.github_repositories;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repositories")
public class GitHubController {

    private final GitHubService service;

    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    public List<RepositoryResponse> getRepositories(@PathVariable String username) {
        return service.getUserRepositories(username);
    }
}