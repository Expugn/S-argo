package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;

public class BannerInfoMenu
{
    private EmbedBuilder builder;
    private String bannerType;
    private String bannerName;
    private int characterAmount;
    private String characterList;
    private String ratesList;
    private String stepThreeRatesList;
    private String stepFiveRatesList;
    private String stepSixRatesList;
    private int bannerID;
    private String imageURL;

    public BannerInfoMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        builder.withAuthorName("[" + bannerType + "] " + bannerName);
        builder.withDesc(characterAmount + Text.BANNER_INFO_CHARACTER_COUNT.get());
        builder.withColor(0, 153, 153);
        builder.withThumbnail(imageURL);

        builder.appendField(Text.BANNER_INFO_CHARACTER_HEADER.get(), characterList, false);

        builder.appendField(Text.BANNER_INFO_RATES_HEADER.get(), ratesList, true);

        if (bannerType.equalsIgnoreCase("Step Up"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            builder.appendField("- Step 5 Pull Rates -", stepFiveRatesList, true);
        }
        else if (bannerType.equalsIgnoreCase("Step Up v2"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            builder.appendField("- Step 5 Pull Rates -", stepFiveRatesList, true);
            builder.appendField("- Step 6 Pull Rates -", stepSixRatesList, true);
        }

        builder.withFooterText(Text.BANNER_INFO_FOOTER_1.get() + " " + (bannerID + 1) + " " + Text.BANNER_INFO_FOOTER_2.get());
    }

    public EmbedBuilder get()
    {
        build();
        return builder;
    }

    public void setBannerType(String bannerType)
    {
        this.bannerType = bannerType;
    }

    public void setBannerName(String bannerName)
    {
        this.bannerName = bannerName;
    }

    public void setCharacterAmount(int characterAmount)
    {
        this.characterAmount = characterAmount;
    }

    public void setCharacterList(String characterList)
    {
        this.characterList = characterList;
    }

    public void setRatesList(String ratesList)
    {
        this.ratesList = ratesList;
    }

    public void setBannerID(int bannerID)
    {
        this.bannerID = bannerID;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public void setStepThreeRatesList(String stepThreeRatesList)
    {
        this.stepThreeRatesList = stepThreeRatesList;
    }

    public void setStepFiveRatesList(String stepFiveRatesList)
    {
        this.stepFiveRatesList = stepFiveRatesList;
    }

    public void setStepSixRatesList(String stepSixRatesList)
    {
        this.stepSixRatesList = stepSixRatesList;
    }
}
