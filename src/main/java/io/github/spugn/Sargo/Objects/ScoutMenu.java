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
    private String footerIcon = Images.MEMORY_DIAMOND_ICON.getUrl();
    private String footerText = Text.MD_REMAINING.get();
    private String typeData = "";
    private String bannerType = "";


    public ScoutMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName(bannerName);

        /*
            NORMAL MULTI = "[Normal] - Multi Pull"
            STEP UP MULTI = "[Step Up] - Step <Step #>"
            RECORD CRYSTAL = "[Record Crystal] - <Amount> Record Crystals"
         */
        if (bannerType.equalsIgnoreCase("Normal"))
        {
            typeData = "Multi Pull";
            builder.withTitle("[" + bannerType + "] - " + typeData);
        }
        else if (bannerType.equalsIgnoreCase("Step Up") || bannerType.equalsIgnoreCase("Step Up v2"))
        {
            builder.withTitle("[" + bannerType + "] - Step " + typeData);
        }
        else if (bannerType.equalsIgnoreCase("Record Crystal"))
        {
            builder.withTitle("[" + bannerType + "] - " + typeData + " Record Crystals");
        }
        else
        {
            builder.withTitle("Single Pull");
        }

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

    public void setThumbnail(String url)
    {
        thumbnail = url;
    }

    public void setTypeData(String typeData)
    {
        this.typeData = typeData;
    }

    public void setBannerType(String bannerType)
    {
        this.bannerType = bannerType;
    }
}
