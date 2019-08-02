package io.github.spugn.Sargo.WeaponScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Sargo;

public class StepUpv3 extends WeaponScout
{
    public StepUpv3(Message message, int bannerID, String choice, String discordID)
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
                break;
            case 3:
                multiScoutPrice = 100;
                COPPER = COPPER - ((GOLD * 1.5) - GOLD) ;
                GOLD = GOLD * 1.5;
                break;
            case 5:
                guaranteeOneGold = true;
                guaranteeSpecificWeapon = true;
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
                    sMenu = sMenu.andThen(s -> s.setTitle("[Step Up v3 Weapons] - Step " + bannerTypeData));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[Step Up v3 Weapons] - Unknown"));
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
                    simpleMessage += "**[Step Up v3 Weapons] - Step " + bannerTypeData + "**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Step Up v3 Weapons] - Unknown**" + "\n";
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
