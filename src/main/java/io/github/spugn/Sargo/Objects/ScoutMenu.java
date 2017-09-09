package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.Image;

/**
 * Gives templates for the scout menu.
 */
public class ScoutMenu
{
    private EmbedBuilder builder;

    private String bannerName = "";
    private String pullType = "";
    private String argoText = Text.ARGO_1.get();
    private String authorIcon = Images.SCOUT_ICON.getUrl();
    private String thumbnail = Images.ARGO_SMILE.getUrl();
    private String footerIcon = Images.MEMORY_DIAMOND.getUrl();
    private String footerText = Text.MD_REMAINING.get();


    public ScoutMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName(bannerName + " - " + pullType);
        builder.withDesc(argoText);
        builder.withAuthorIcon(authorIcon);
        builder.withColor(244, 233, 167);
        builder.withThumbnail(thumbnail);
        builder.withFooterIcon(footerIcon);
        builder.withFooterText("<Amount> " + footerText);
    }

    public EmbedBuilder get()
    {
        build();
        return builder;
    }

    public void setBannerName(String s)
    {
        bannerName = s;
    }

    public void setPullType(String s)
    {
        pullType = s;
    }

    public void setArgoText(String s)
    {
        argoText = s;
    }

    public void setAuthorIcon(String url)
    {
        authorIcon = url;
    }

    public void setThumbnail(String url)
    {
        thumbnail = url;
    }

    public void setFooterIcon(String url)
    {
        footerIcon = url;
    }

    public void setFooterText(String s)
    {
        footerText = s;
    }
}
