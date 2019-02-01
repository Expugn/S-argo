package io.github.spugn.Sargo.Objects;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import sx.blah.discord.util.EmbedBuilder;

/**
 * BANNER LIST MENU
 * <p>
 *     Creates the Embed Message used when listing available banners.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 * @see io.github.spugn.Sargo.Functions.BannerInfo
 */
public class BannerListMenu
{
    private EmbedBuilder builder;
    private int bannerCount;
    private String bannerList;
    private int currentPage;
    private int highestPage;

    public BannerListMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName("Banner List (Page " + currentPage + " of " + highestPage + ")");
        builder.withDesc(bannerCount + " banners available.");
        builder.withThumbnail(new GitHubImage("images/System/Scout_Icon.png").getURL());
        builder.withColor(255, 0, 255);

        builder.appendField("- Banners -", bannerList, false);

        builder.withFooterText("'" + CommandManager.getCommandPrefix() + "scout [Banner ID]' for more banner info.  |  '" + CommandManager.getCommandPrefix() + "scout p[Page]' to view another page.");
    }

    public EmbedBuilder get()
    {
        build();
        return builder;
    }

    public void setBannerCount(int bannerCount)
    {
        this.bannerCount = bannerCount;
    }

    public void setBannerList(String bannerList)
    {
        this.bannerList = bannerList;
    }

    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }

    public void setHighestPage(int highestPage)
    {
        this.highestPage = highestPage;
    }
}
