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

    public BannerListMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName(Text.BANNER_LIST_TITLE.get());
        builder.withDesc(bannerCount + " banners available.");
        builder.withThumbnail(Images.SCOUT_ICON.getUrl());
        builder.withColor(153, 0, 153);

        builder.appendField("- Banners - ", bannerList, false);

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
}
