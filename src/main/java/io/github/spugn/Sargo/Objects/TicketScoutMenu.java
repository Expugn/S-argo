package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;

public class TicketScoutMenu
{
    private EmbedBuilder builder;

    private String bannerName = "";
    private String pullType = "";
    private String authorIcon = Images.SCOUT_ICON.getUrl();
    private String thumbnail = Images.CHEST_BROWN.getUrl();
    private String footerIcon = Images.MEMORY_DIAMOND_ICON.getUrl();
    private String userName = "";
    private int totalScout = 0;
    private String itemString = "";


    public TicketScoutMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName(bannerName);

        if (pullType.equals(Text.MULTI_PULL.get()))
        {
            builder.withTitle("[Ticket Scout] - Multi Pull");
        }
        else
        {
            builder.withTitle("[Ticket Scout] - Single Pull");
        }

        if (!itemString.isEmpty())
        {
            builder.appendField("- Weapon Result -", itemString, false);
        }

        builder.withAuthorIcon(authorIcon);
        builder.withColor(255, 255, 255);
        builder.withThumbnail(thumbnail);
        builder.withFooterIcon(footerIcon);
        builder.withFooterText(userName + " | " + totalScout + " Total Ticket Scouts");
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

    public void setThumbnail(String url)
    {
        thumbnail = url;
    }

    public void setTotalScout(int totalScout)
    {
        this.totalScout = totalScout;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setItemString(String itemString)
    {
        this.itemString = itemString;
    }
}
