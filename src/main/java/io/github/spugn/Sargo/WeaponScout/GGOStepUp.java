package io.github.spugn.Sargo.WeaponScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Sargo;

/**
 * GGO STEP UP WEAPON SCOUT
 * <p>
 *     Step Up scouts are scouts that increment "Steps" with every multi
 *     the user does. On certain Steps, scout rates may increase or the
 *     price for the scout is discounted.
 *
 *     As for GGO Step Up scouts, the pattern is similar to Character
 *     Step Up v2 scouts but on Step 1 a Copper rarity or higher
 *     automatic rifle is guaranteed.
 * </p>
 * <p>
 *     STEP CHANGES:<br>
 *     Step 1)<br>
 *          - Multi Scout price is 100 Memory Diamonds.<br>
 *          - Copper rarity or higher Automatic Rifle is guaranteed.<br>
 *     Step 3)<br>
 *          - Multi Scout price is 100 Memory Diamonds.<br>
 *          - Gold rarity weapon rates increase by 1.5x.<br>
 *     Step 5)<br>
 *          - 1 or more Gold rarity weapon is guaranteed.<br>
 *     Step 6)<br>
 *          - Gold rarity weapon rates increase by 2.0x.<br>
 *          - Step 6 repeats.<br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 * @see WeaponScout
 */
public class GGOStepUp extends WeaponScout
{
    public GGOStepUp(Message message, int bannerID, String choice, String discordID)
    {
        super(message, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        USER.addBannerInfo(SELECTED_BANNER.getBannerName() + " Weapons", 1);
        bannerTypeData = 1;
    }

    @Override
    protected void modifyScoutData()
    {
        switch (bannerTypeData)
        {
            case 1:
                multiScoutPrice = 100;
                guaranteeAutomaticRifle = true;
                break;
            case 3:
                multiScoutPrice = 100;
                COPPER = COPPER - ((GOLD * 1.5) - GOLD);
                GOLD = GOLD * 1.5;
                break;
            case 5:
                guaranteeOneGold = true;
                break;
            case 6:
                COPPER = COPPER - ((GOLD * 2.0) - GOLD);
                GOLD = GOLD * 2.0;
            default:
                break;
        }
    }

    @Override
    protected void updateBannerData()
    {
        int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName() + " Weapons");
        currentStep++;
        if (currentStep > 6)
        {
            USER.changeValue(SELECTED_BANNER.getBannerName() + " Weapons", 6);
        }
        else
        {
            USER.changeValue(SELECTED_BANNER.getBannerName() + " Weapons", currentStep);
        }
    }

    @Override
    protected void setupScoutMenu()
    {
        if (!SIMPLE_MESSAGE)
        {
            switch (CHOICE.toLowerCase())
            {
                case "ws":
                case "wsi":
                    sMenu = sMenu.andThen(s -> s.setTitle("[Weapon Scout] - Single Pull"));
                    break;
                case "wm":
                case "wmi":
                    sMenu = sMenu.andThen(s -> s.setTitle("[GGO Step Up Weapons] - Multi Pull"));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[GGO Step Up Weapons] - Unknown"));
                    break;
            }
        }
        else
        {
            switch (CHOICE.toLowerCase())
            {
                case "ws":
                case "wsi":
                    simpleMessage += "**[Weapon Scout] - Single Pull**" + "\n";
                    break;
                case "wm":
                case "wmi":
                    simpleMessage += "**[GGO Step Up Weapons] - Step " + bannerTypeData + "**" + "\n";
                    break;
                default:
                    simpleMessage += "**[GGO Step Up Weapons] - Unknown**" + "\n";
                    break;
            }
        }
    }

    @Override
    protected void run()
    {
        switch (CHOICE.toLowerCase())
        {
            case "ws":
            case "wsi":
                if (userMemoryDiamonds < singleScoutPrice)
                {
                    Sargo.replyToMessage_Warning(TEXT_CHANNEL, "NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.");
                    return;
                }

                if (CHOICE.equalsIgnoreCase("wsi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= singleScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doSinglePull();
                break;
            case "wm":
            case "wmi":
                if (userMemoryDiamonds < multiScoutPrice)
                {
                    Sargo.replyToMessage_Warning(TEXT_CHANNEL, "NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.");
                    return;
                }

                if (CHOICE.equalsIgnoreCase("wmi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= multiScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doMultiPull();
                updateBannerData();
                break;
            default:
                Sargo.replyToMessage_Warning(TEXT_CHANNEL, "UNKNOWN/UNAVAILABLE SCOUT TYPE", "Use '" + CommandManager.getCommandPrefix() + "**scout** " + BANNER_ID + "' and read the footer text for available scout types.");
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}