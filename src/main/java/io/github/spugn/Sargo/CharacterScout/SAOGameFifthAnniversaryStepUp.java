package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.WarningMessage;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

/**
 * SAO GAME 5TH ANNIVERSARY CHARACTER SCOUT
 * <p>
 *     View {@link StepUp}'s JavaDoc for more information
 *     about Step Up scouts in general.<br>
 *
 *     Same deal as StepUpv2, but with some changes to what
 *     happens in each step. This scout type is used for
 *     the banners that include characters with voted skills.
 * </p>
 * <p>
 *     STEP CHANGES:<br>
 *     Step 1)<br>
 *          - Multi Scout price is 55 Memory Diamonds.<br>
 *     Step 3)<br>
 *          - Platinum rarity character rates increase by 1.5x.<br>
 *     Step 5)<br>
 *          - One platinum rarity character is guaranteed.<br>
 *     Step 6)<br>
 *          - Platinum rarity character rates increase by 2.0x<br>
 *          - Step 6 repeats.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see StepUp
 * @see CharacterScout
 */
public class SAOGameFifthAnniversaryStepUp extends CharacterScout
{
    public SAOGameFifthAnniversaryStepUp(IChannel channel, int bannerID, String choice, String discordID)
    {
        super(channel, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        USER.addBannerInfo(SELECTED_BANNER.getBannerName(), 1);
        bannerTypeData = 1;
    }

    @Override
    protected void modifyScoutData()
    {
        switch (bannerTypeData)
        {
            case 1:
                multiScoutPrice = 55;
                break;
            case 3:
                COPPER = COPPER - ((PLATINUM * 1.5) - PLATINUM);
                PLATINUM = PLATINUM * 1.5;
                break;
            case 5:
                guaranteeOnePlatinum = true;
                break;
            case 6:
                COPPER = COPPER - ((PLATINUM * 2.0) - PLATINUM);
                PLATINUM = PLATINUM * 2.0;
            default:
                break;
        }
    }

    @Override
    protected void updateBannerData()
    {
        int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName());
        currentStep++;
        if (currentStep > 6)
        {
            USER.changeValue(SELECTED_BANNER.getBannerName(), 6);
        }
        else
        {
            USER.changeValue(SELECTED_BANNER.getBannerName(), currentStep);
        }
    }

    @Override
    protected Character randGoldCharacter()
    {
        int randIndex = GOLD_BANNERS.get(RNG.nextInt(GOLD_BANNERS.size()));
        Banner randBanner = BANNERS.get(randIndex - 1);
        List<Character> randCharacters = randBanner.getCharacters();
        return randCharacters.get(RNG.nextInt(randCharacters.size()));
    }

    @Override
    protected void setupScoutMenu()
    {
        if (!SIMPLE_MESSAGE)
        {
            switch (CHOICE.toLowerCase())
            {
                case "s":
                case "si":
                    scoutMenu.withTitle("Single Pull");
                    break;
                case "m":
                case "mi":
                    scoutMenu.withTitle("[SAO Game 5th Anniversary Step Up] - Step " + bannerTypeData);
                    break;
                default:
                    scoutMenu.withTitle("[SAO Game 5th Anniversary Step Up] - Unknown");
                    break;
            }
        }
        else
        {
            switch (CHOICE.toLowerCase())
            {
                case "s":
                case "si":
                    simpleMessage += "**Single Pull**" + "\n";
                    break;
                case "m":
                case "mi":
                    simpleMessage += "**[SAO Game 5th Anniversary Step Up] - Step " + bannerTypeData + "**" + "\n";
                    break;
                default:
                    simpleMessage += "**[SAO Game 5th Anniversary Step Up] - Unknown**" + "\n";
                    break;
            }
        }
    }

    @Override
    protected void run()
    {
        switch (CHOICE.toLowerCase())
        {
            case "s":
            case "si":
                if (userMemoryDiamonds < singleScoutPrice)
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.").get().build());
                    return;
                }

                if (CHOICE.equalsIgnoreCase("si") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= singleScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doSinglePull();
                break;
            case "m":
            case "mi":
                if (userMemoryDiamonds < multiScoutPrice)
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.").get().build());
                    return;
                }

                if (CHOICE.equalsIgnoreCase("mi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= multiScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doMultiPull();
                updateBannerData();
                break;
            default:
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN/UNAVAILABLE SCOUT TYPE", "Use '" + CommandManager.getCommandPrefix() + "**scout** " + (BANNER_ID + 1) + "' and read the footer text for available scout types.").get().build());
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
