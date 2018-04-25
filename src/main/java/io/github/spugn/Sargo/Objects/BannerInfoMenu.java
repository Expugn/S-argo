package io.github.spugn.Sargo.Objects;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.util.EmbedBuilder;

/**
 * BANNER INFO MENU
 * <p>
 *     Creates the Embed Message used when displaying data about a
 *     certain banner.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 * @see io.github.spugn.Sargo.Functions.BannerInfo
 */
public class BannerInfoMenu
{
    private EmbedBuilder builder;
    private String bannerType;
    private int bannerWepType;
    private String bannerName;
    private int characterAmount;
    private String characterList;
    private int weaponAmount;
    private String weaponList;
    private String ratesList;
    private String stepThreeRatesList;
    private String stepFiveRatesList;
    private String stepSixRatesList;
    private String stepThreeWeaponRatesList;
    private String stepFiveWeaponRatesList;
    private String stepSixWeaponRatesList;
    private String recordCrystalRatesList;
    private String circulatingRecordCrystalRatesList;
    private int bannerID;
    private String imageURL;

    public BannerInfoMenu()
    {
        builder = new EmbedBuilder();
    }

    private void build()
    {
        boolean unknownScoutType = false;
        if (bannerWepType == 1)
        {
            builder.withAuthorName("[" + bannerType + " / Weapon Step Up] " + bannerName);
        }
        else if (bannerWepType == 2)
        {
            builder.withAuthorName("[" + bannerType + " / GGO Step Up] " + bannerName);
        }
        else
        {
            builder.withAuthorName("[" + bannerType + "] " + bannerName);
        }

        builder.withDesc(characterAmount + " characters available.");
        builder.withColor(0, 153, 153);
        builder.withThumbnail(imageURL);

        builder.appendField("- Characters -", characterList, (weaponAmount > 0));

        if (weaponAmount > 0)
        {
            builder.appendField("- Weapons -", weaponList, true);
        }

        builder.appendField("- Pull Rates -", ratesList, true);

        if (bannerType.equalsIgnoreCase("Step Up"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            builder.appendField("- Step 5 Pull Rates -", stepFiveRatesList, true);
        }
        else if (bannerType.equalsIgnoreCase("Step Up v2") ||
                bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v4") ||
                bannerType.equalsIgnoreCase("Step Up v5") ||
                bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v2"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            builder.appendField("- Step 5 Pull Rates -", stepFiveRatesList, true);
            builder.appendField("- Step 6 Pull Rates -", stepSixRatesList, true);
        }
        else if (bannerType.equalsIgnoreCase("Birthday Step Up"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
        }
        else if (bannerType.equalsIgnoreCase("Record Crystal") ||
                bannerType.equalsIgnoreCase("Record Crystal v2") ||
                bannerType.equalsIgnoreCase("Record Crystal v3") ||
                bannerType.equalsIgnoreCase("Record Crystal v4"))
        {
            builder.appendField("- Record Crystal Rates -", recordCrystalRatesList, true);

            if (bannerType.equalsIgnoreCase("Record Crystal v3") ||
                    bannerType.equalsIgnoreCase("Record Crystal v4"))
            {
                builder.appendField("- Circulating Record Crystal Rates -", circulatingRecordCrystalRatesList, true);
            }
        }
        else if (bannerType.equalsIgnoreCase("Step Up v3"))
        {
            builder.appendField("- Step 3 Pull Rates -", stepThreeRatesList, true);
        }

        if (bannerWepType == 1)
        {
            builder.appendField("- Step 3 Weapon Pull Rates -", stepThreeWeaponRatesList, true);
        }
        else if (bannerWepType == 2)
        {
            builder.appendField("- Step 3 Weapon Pull Rates -", stepThreeWeaponRatesList, true);
            builder.appendField("- Step 5 Weapon Pull Rates -", stepFiveWeaponRatesList, true);
            builder.appendField("- Step 6 Weapon Pull Rates -", stepSixWeaponRatesList, true);
        }

        String footer = "[s/si";

        if (bannerType.equalsIgnoreCase("Normal") ||
                bannerType.equalsIgnoreCase("Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v2") ||
                bannerType.equalsIgnoreCase("Birthday Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v3") ||
                bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up") ||
                bannerType.equalsIgnoreCase("Step Up v4") ||
                bannerType.equalsIgnoreCase("Step Up v5") ||
                bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v2"))
        {
            if (weaponAmount > 0)
            {
                footer += " | ws/wsi | m/mi | wm/wmi]' to scout.";
            }
            else
            {
                footer += " | m/mi]' to scout.";
            }
        }
        else if (bannerType.equalsIgnoreCase("Record Crystal") ||
                bannerType.equalsIgnoreCase("Record Crystal v2") ||
                bannerType.equalsIgnoreCase("Record Crystal v3") ||
                bannerType.equalsIgnoreCase("Record Crystal v4"))
        {
            if (weaponAmount > 0)
            {
                footer += " | ws/wsi | m/mi | wm/wmi | rc/rci]' to scout.";
            }
            else
            {
                footer += " | m/mi | rc/rci]' to scout.";
            }
        }
        else if (bannerType.equalsIgnoreCase("Memorial Scout") ||
                bannerType.equalsIgnoreCase("Event"))
        {
            footer += "]' to scout.";
        }
        else
        {
            footer = "Unknown Scout Type!";
            unknownScoutType = true;
        }

        if (unknownScoutType)
        {
            builder.withFooterText(footer);
        }
        else
        {
            builder.withFooterText("Use '" + CommandManager.getCommandPrefix() + "scout " + (bannerID + 1) + " " + footer);
        }

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

    public void setBannerWepType(int bannerWepType)
    {
        this.bannerWepType = bannerWepType;
    }

    public void setBannerName(String bannerName)
    {
        this.bannerName = bannerName;
    }

    public void setCharacterAmount(int characterAmount)
    {
        this.characterAmount = characterAmount;
    }

    public void setWeaponAmount(int weaponAmount)
    {
        this.weaponAmount = weaponAmount;
    }

    public void setCharacterList(String characterList)
    {
        this.characterList = characterList;
    }

    public void setWeaponList(String weaponList)
    {
        this.weaponList = weaponList;
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

    public void setStepThreeWeaponRatesList(String stepThreeWeaponRatesList)
    {
        this.stepThreeWeaponRatesList = stepThreeWeaponRatesList;
    }

    public void setStepFiveWeaponRatesList(String stepFiveWeaponRatesList)
    {
        this.stepFiveWeaponRatesList = stepFiveWeaponRatesList;
    }

    public void setStepSixWeaponRatesList(String stepSixWeaponRatesList)
    {
        this.stepSixWeaponRatesList = stepSixWeaponRatesList;
    }

    public void setRecordCrystalRatesList(String recordCrystalRatesList)
    {
        this.recordCrystalRatesList = recordCrystalRatesList;
    }

    public void setCirculatingRecordCrystalRatesList(String circulatingRecordCrystalRatesList)
    {
        this.circulatingRecordCrystalRatesList = circulatingRecordCrystalRatesList;
    }
}
