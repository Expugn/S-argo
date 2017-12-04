package io.github.spugn.Sargo.Utilities;

import io.github.spugn.Sargo.XMLParsers.SettingsParser;

public class GitHubImage
{
    private String filePath;
    private final String GITHUB_IMAGE;

    public GitHubImage(String filePath)
    {
        GITHUB_IMAGE = new SettingsParser().getGitHubRepoURL();
        this.filePath = filePath;
    }

    public String getURL()
    {
        String url = GITHUB_IMAGE + filePath;

        /* REPLACE ALL SPACES WITH %20 */
        url = url.replaceAll(" ", "%20");

        /* REPLACE ALL ★ WITH %E2%98%85 */
        url = url.replaceAll("★", "%E2%98%85");

        return url;
    }
}
