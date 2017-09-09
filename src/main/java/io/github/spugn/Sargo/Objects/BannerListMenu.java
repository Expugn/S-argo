package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;

/**
 * Gives templates for the BannerList menu.
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
        builder.withAuthorName(Text.BANNER_LIST_TITLE.get() + " (Page " + currentPage + " of " + highestPage + ")");
        builder.withDesc(bannerCount + Text.BANNER_LIST_BANNER_COUNT.get());
        builder.withThumbnail(Images.SCOUT_ICON.getUrl());
        builder.withColor(153, 0, 153);

        builder.appendField(Text.BANNER_LIST_BANNER_HEADER.get(), bannerList, false);

        builder.withFooterText(Text.BANNER_LIST_FOOTER.get());
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
