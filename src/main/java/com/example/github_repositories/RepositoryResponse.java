package com.example.github_repositories;

import java.util.List;

public class RepositoryResponse {
    public String repositoryName;
    public String ownerLogin;
    public List<BranchDto> branches;

    public RepositoryResponse(String repositoryName, String ownerLogin, List<BranchDto> branches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;
        this.branches = branches;
    }
}