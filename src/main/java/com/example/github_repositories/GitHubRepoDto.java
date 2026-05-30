package com.example.github_repositories;

public class GitHubRepoDto {
    public String name;
    public boolean fork;
    public Owner owner;

    public static class Owner {
        public String login;
    }
}