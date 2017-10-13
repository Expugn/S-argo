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
    private String userName = "";
    private String typeData = "";
    private String bannerType = "";
    private int mdRemain = 0;
    private int rcGet = 0;
    private boolean guaranteedScout = false;
    private String characterString = "";


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
            RECORD CRYSTAL = "[Record Crystal] - +<Amount Get> Record Crystals (<Amount Total>)"
            GUARANTEED SCOUT = "[Guaranteed Scout] - <Amount Total> Record Crystals Left"
         */
        if (guaranteedScout)
        {
            builder.withTitle("[Guaranteed Scout] - " + typeData + " Record Crystals Left");
        }
        else if (bannerType.equalsIgnoreCase("Normal"))
        {
            typeData = "Multi Pull";
            builder.withTitle("[" + bannerType + "] - " + typeData);
        }
        else if (bannerType.equalsIgnoreCase("Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v2") ||
                bannerType.equalsIgnoreCase("Birthday Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v3"))
        {
            builder.withTitle("[" + bannerType + "] - Step " + typeData);
        }
        else if (bannerType.equalsIgnoreCase("Record Crystal") || bannerType.equalsIgnoreCase("Record Crystal v2"))
        {
            builder.withTitle("[" + bannerType + "] - +" + rcGet + " Record Crystals (" + typeData + ")");
        }
        else
        {
            builder.withTitle("Single Pull");
        }

        if (!characterString.isEmpty())
        {
            builder.appendField("- Scout Result -", characterString, false);
        }

        builder.withDesc(argoText);
        builder.withAuthorIcon(authorIcon);
        builder.withColor(244, 233, 167);
        builder.withThumbnail(thumbnail);
        builder.withFooterIcon(footerIcon);
        builder.withFooterText(userName + " | " + mdRemain + " " + footerText);
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

    public void setRcGet(int rcGet)
    {
        this.rcGet = rcGet;
    }

    public void setMdRemain(int mdRemain)
    {
        this.mdRemain = mdRemain;
    }

    public void setGuaranteedScout(boolean guaranteedScout)
    {
        this.guaranteedScout = guaranteedScout;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setCharacterString(String characterString)
    {
        this.characterString = characterString;
    }
}
