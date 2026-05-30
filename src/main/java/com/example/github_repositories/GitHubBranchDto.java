package com.example.github_repositories;

public class GitHubBranchDto {
    public String name;
    public Commit commit;

    public static class Commit {
        public String sha;
    }
}