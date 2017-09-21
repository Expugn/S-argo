package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;

public class WeaponScoutMenu
{
    private EmbedBuilder builder;

    private String bannerName = "";
    private String pullType = "";
    private String chestText = "*...*";
    private String authorIcon = Images.SCOUT_ICON.getUrl();
    private String thumbnail = Images.CHEST_BROWN.getUrl();
    private String footerIcon = Images.MEMORY_DIAMOND_ICON.getUrl();
    private String footerText = Text.MD_REMAINING.get();
    private String userName = "";
    private int mdRemain = 0;
    private String weaponString = "";


    public WeaponScoutMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName(bannerName);

        if (pullType.equals(Text.MULTI_PULL.get()))
        {
            builder.withTitle("[Weapon Scout] - Multi Pull");
        }
        else
        {
            builder.withTitle("[Weapon Scout] - Single Pull");
        }

        if (!weaponString.isEmpty())
        {
            builder.appendField("- Weapon Result -", weaponString, false);
        }

        builder.withDesc(chestText);
        builder.withAuthorIcon(authorIcon);
        builder.withColor(139, 69, 19);
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

    public void setThumbnail(String url)
    {
        thumbnail = url;
    }

    public void setMdRemain(int mdRemain)
    {
        this.mdRemain = mdRemain;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setWeaponString(String weaponString)
    {
        this.weaponString = weaponString;
    }
}
