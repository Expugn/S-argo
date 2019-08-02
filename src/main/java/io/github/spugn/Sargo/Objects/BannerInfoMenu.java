package io.github.spugn.Sargo.Objects;

import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Managers.CommandManager;

import java.awt.*;
import java.util.function.Consumer;

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
    Consumer<EmbedCreateSpec> ecsTemplate;
    private String bannerType;
    private int bannerWepType;
    private String bannerName;
    private int characterAmount;
    private String characterList;
    private int weaponAmount;
    private String weaponList;
    private String ratesList;
    private String stepOneRatesList;
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
        //builder = new EmbedBuilder();
    }

    private void build()
    {
        ecsTemplate = s -> {
            boolean unknownScoutType = false;
            if (bannerWepType == 1)
            {
                s.setAuthor("[" + bannerType + " / Weapon Step Up] " + bannerName, "", "");
            }
            else if (bannerWepType == 2)
            {
                s.setAuthor("[" + bannerType + " / GGO Step Up] " + bannerName, "", "");
            }
            else if (bannerWepType == 3)
            {
                s.setAuthor("[" + bannerType + " / Weapon Step Up v2] " + bannerName, "", "");
            }
            else if (bannerWepType == 4)
            {
                s.setAuthor("[" + bannerType + " / Weapon Step Up v3] " + bannerName, "", "");
            }
            else
            {
                s.setAuthor("[" + bannerType + "] " + bannerName, "", "");
            }

            s.setDescription(characterAmount + " characters available.");
            s.setColor(new Color(0, 153, 153));
            s.setThumbnail(imageURL);

            s.addField("- Characters -", characterList, (weaponAmount > 0));

            if (weaponAmount > 0)
            {
                s.addField("- Weapons -", weaponList, true);
            }

            s.addField("- Pull Rates -", ratesList, true);

            if (bannerType.equalsIgnoreCase("Step Up"))
            {
                s.addField("- Step 3 Pull Rates -", stepThreeRatesList, true);
                s.addField("- Step 5 Pull Rates -", stepFiveRatesList, true);
            }
            else if (bannerType.equalsIgnoreCase("Step Up v2") ||
                    bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up") ||
                    bannerType.equalsIgnoreCase("Step Up v4") ||
                    bannerType.equalsIgnoreCase("Step Up v5") ||
                    bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v2") ||
                    bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v3") ||
                    bannerType.equalsIgnoreCase("Step Up v7") ||
                    bannerType.equalsIgnoreCase("Step Up v8") ||
                    bannerType.equalsIgnoreCase("Step Up v9"))
            {
                if (bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v3"))
                {
                    s.addField("- Step 1 Pull Rates -", stepOneRatesList, true);
                }
                s.addField("- Step 3 Pull Rates -", stepThreeRatesList, true);
                s.addField("- Step 5 Pull Rates -", stepFiveRatesList, true);
                s.addField("- Step 6 Pull Rates -", stepSixRatesList, true);
            }
            else if (bannerType.equalsIgnoreCase("Birthday Step Up"))
            {
                s.addField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            }
            else if (bannerType.equalsIgnoreCase("Record Crystal") ||
                    bannerType.equalsIgnoreCase("Record Crystal v2") ||
                    bannerType.equalsIgnoreCase("Record Crystal v3") ||
                    bannerType.equalsIgnoreCase("Record Crystal v4") ||
                    bannerType.equalsIgnoreCase("Record Crystal v5") ||
                    bannerType.equalsIgnoreCase("Record Crystal v6"))
            {
                s.addField("- Record Crystal Rates -", recordCrystalRatesList, true);

                if (bannerType.equalsIgnoreCase("Record Crystal v3") ||
                        bannerType.equalsIgnoreCase("Record Crystal v4") ||
                        bannerType.equalsIgnoreCase("Record Crystal v6"))
                {
                    s.addField("- Circulating Record Crystal Rates -", circulatingRecordCrystalRatesList, true);
                }
            }
            else if (bannerType.equalsIgnoreCase("Step Up v3") ||
                    bannerType.equalsIgnoreCase("Step Up v6"))
            {
                s.addField("- Step 3 Pull Rates -", stepThreeRatesList, true);
            }

            if (bannerWepType == 1)
            {
                s.addField("- Step 3 Weapon Pull Rates -", stepThreeWeaponRatesList, true);
            }
            else if (bannerWepType == 2 ||
                    bannerWepType == 3 ||
                    bannerWepType == 4)
            {
                s.addField("- Step 3 Weapon Pull Rates -", stepThreeWeaponRatesList, true);
                s.addField("- Step 5 Weapon Pull Rates -", stepFiveWeaponRatesList, true);
                s.addField("- Step 6 Weapon Pull Rates -", stepSixWeaponRatesList, true);
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
                    bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v2") ||
                    bannerType.equalsIgnoreCase("Step Up v6") ||
                    bannerType.equalsIgnoreCase("SAO Game 5th Anniversary Step Up v3") ||
                    bannerType.equalsIgnoreCase("Step Up v7") ||
                    bannerType.equalsIgnoreCase("Step Up v8") ||
                    bannerType.equalsIgnoreCase("Step Up v9"))
            {
                if (weaponAmount > 0)
                {
                    footer += " | ws/wsi | m/mi | wm/wmi]` to scout.";
                }
                else
                {
                    footer += " | m/mi]` to scout.";
                }
            }
            else if (bannerType.equalsIgnoreCase("Record Crystal") ||
                    bannerType.equalsIgnoreCase("Record Crystal v2") ||
                    bannerType.equalsIgnoreCase("Record Crystal v3") ||
                    bannerType.equalsIgnoreCase("Record Crystal v4") ||
                    bannerType.equalsIgnoreCase("Record Crystal v5") ||
                    bannerType.equalsIgnoreCase("Record Crystal v6"))
            {
                if (weaponAmount > 0)
                {
                    footer += " | ws/wsi | m/mi | wm/wmi | rc/rci]` to scout.";
                }
                else
                {
                    footer += " | m/mi | rc/rci]` to scout.";
                }
            }
            else if (bannerType.equalsIgnoreCase("Memorial Scout") ||
                    bannerType.equalsIgnoreCase("Event"))
            {
                footer += "]` to scout.";
            }
            else
            {
                footer = "Unknown Scout Type!";
                unknownScoutType = true;
            }

            if (unknownScoutType)
            {
                s.setFooter(footer, "");
            }
            else
            {
                s.setFooter("Use `" + CommandManager.getCommandPrefix() + "scout " + (bannerID + 1) + " " + footer, "");
            }
        };
    }

    public Consumer<EmbedCreateSpec> get()
    {
        build();
        return ecsTemplate;
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

    public void setStepOneRatesList(String stepOneRatesList)
    {
        this.stepOneRatesList = stepOneRatesList;
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
