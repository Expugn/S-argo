package io.github.spugn.Sargo.Objects;

import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Utilities.GitHubImage;

import java.awt.*;
import java.util.function.Consumer;

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
    Consumer<EmbedCreateSpec> ecsTemplate;
    private int bannerCount;
    private String bannerList;
    private int currentPage;
    private int highestPage;

    public BannerListMenu()
    {
        //builder = new EmbedBuilder();
    }

    private void build()
    {
        ecsTemplate = s -> {
            s.setAuthor("Banner List (Page " + currentPage + " of " + highestPage + ")", "", "");
            s.setDescription(bannerCount + " banners available.");
            s.setThumbnail(new GitHubImage("images/System/Scout_Icon.png").getURL());
            s.setColor(new Color(255, 0, 255));
            s.addField("- Banners -", bannerList, false);
            s.setFooter("'" + CommandManager.getCommandPrefix() + "scout [Banner ID]' for more banner info.  |  '" + CommandManager.getCommandPrefix() + "scout p[Page]' to view another page.", "");
        };
    }

    public Consumer<EmbedCreateSpec> get()
    {
        build();
        return ecsTemplate;
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
