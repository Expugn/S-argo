package io.github.spugn.Sargo.CharacterScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;

import java.util.List;

/**
 * BIRTHDAY STEP UP CHARACTER SCOUT
 * <p>
 *     View {@link StepUp}'s JavaDoc for more information
 *     about Step Up scouts in general.<br>
 *
 *     Birthday Step Up scouts are usually a collection of
 *     both gold and platinum variants of one character in
 *     celebration for their birthday. This gives players
 *     a chance to grab old characters that they missed at
 *     a discount on Step 1 and a high gold/platinum scout
 *     rate at Step 3.
 * </p>
 * <p>
 *     STEP CHANGES:<br>
 *     Step 1)<br>
 *          - Multi Scout price is 125 Memory Diamonds.<br>
 *     Step 3)<br>
 *          - Gold rarity character rates increase by 2.0x<br>
 *          - Platinum rarity character rates increase by 2.0x.<br>
 *          - Steps reset back to 1.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see StepUp
 * @see CharacterScout
 */
public class BirthdayStepUp extends CharacterScout
{
    public BirthdayStepUp(Message message, int bannerID, String choice, String discordID)
    {
        super(message, bannerID, choice, discordID);
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
        // REMOVE 1.5% FROM PLATINUM (IT'S ORIGINAL VALUE)
        COPPER = COPPER + (PLATINUM - (PLATINUM / 1.5));
        PLATINUM = PLATINUM / 1.5;

        if (CHOICE.equalsIgnoreCase("m") ||
                CHOICE.equalsIgnoreCase("mi"))
        {
            switch (bannerTypeData)
            {
                case 1:
                    multiScoutPrice = 125;
                    break;
                case 3:
                    COPPER = COPPER - (((PLATINUM * 2.0) - PLATINUM) + ((GOLD * 2.0) - GOLD));
                    GOLD = GOLD * 2.0;
                    PLATINUM = PLATINUM * 2.0;
                default:
                    break;
            }
        }
    }

    @Override
    protected void updateBannerData()
    {
        int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName());
        currentStep++;
        if (currentStep > 3)
        {
            USER.changeValue(SELECTED_BANNER.getBannerName(), 1);
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
    protected Character randPlatinumCharacter()
    {
        /* THIS SCOUT TYPE DOES NOT USE THIS FUNCTIONALITY */
        return null;
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
                    sMenu = sMenu.andThen(s -> s.setTitle("Single Pull"));
                    break;
                case "m":
                case "mi":
                    sMenu = sMenu.andThen(s -> s.setTitle("[Birthday Step Up] - Step " + bannerTypeData));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[Birthday Step Up] - Unknown"));
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
                    simpleMessage += "**[Birthday Step Up] - Step " + bannerTypeData + "**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Birthday Step Up] - Unknown**" + "\n";
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
                    print_NotEnoughMemoryDiamonds_Single_Message();
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
                    print_NotEnoughMemoryDiamonds_Multi_Message();
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
                print_UnknownScoutType_sm_Message();
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
