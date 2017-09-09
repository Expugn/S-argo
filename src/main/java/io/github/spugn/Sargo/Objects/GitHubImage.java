package io.github.spugn.Sargo.Objects;

public class GitHubImage
{
    private String filePath;
    private final String GITHUB_IMAGE = "https://raw.githubusercontent.com/Expugn/S-argo/master/";

    public GitHubImage(String filePath)
    {
        this.filePath = filePath;
    }

    public String getURL()
    {
        /* REPLACE ALL SPACES WITH '%20' AND RETURN */
        return (GITHUB_IMAGE + filePath).replaceAll(" ", "%20");
    }
}
