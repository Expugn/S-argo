package io.github.spugn.Sargo.CharacterScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;

import java.util.List;

/**
 * RECORD CRYSTAL V3 CHARACTER SCOUT
 * <p>
 *     Also known as Circulating Record Crystal scouts.
 *
 *     View {@link RecordCrystal}'s JavaDoc for more information
 *     about Record Crystal scouts in general.<br>
 *
 *     Compared to Record Crystal scouts, Record Crystal v3 scouts
 *     feature a wider collection of gold variant characters to
 *     obtain if you miss the 60% needed to get a banner character.
 *     Record Crystal v3 scouts also have the  same added benefit of
 *     Record Crystal v2 where the first scout be 50% off.
 *     Record Crystal v3 introduces a new mechanic where with every
 *     record crystal scout you perform you have a chance to win 1-5
 *     record crystals back.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.2
 * @see RecordCrystal
 * @see CharacterScout
 */
public class RecordCrystalv3 extends CharacterScout
{
    private int circluatedRecordCrystals;

    public RecordCrystalv3(Message message, int bannerID, String choice, String discordID)
    {
        super(message, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        USER.addBannerInfo(SELECTED_BANNER.getBannerName(), -1);
        bannerTypeData = -1;
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
                case -1:
                    multiScoutPrice = 125;
                    bannerTypeData = 0;
                    USER.changeValue(SELECTED_BANNER.getBannerName(), bannerTypeData);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void updateBannerData()
    {
        rcGet = getRecordCrystals();
        bannerTypeData += rcGet;
        USER.changeValue(SELECTED_BANNER.getBannerName(), bannerTypeData);
    }

    @Override
    protected Character randGoldCharacter()
    {
        int randIndex = GOLD_BANNERS_V2.get(RNG.nextInt(GOLD_BANNERS_V2.size()));
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
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal v3] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")"));
                    break;
                case "rc":
                case "rci":
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")" + "\n"));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal v3] - Unknown"));
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
                    simpleMessage += "**[Record Crystal v3] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")**" + "\n";
                    break;
                case "rc":
                case "rci":
                    simpleMessage += "**[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Record Crystal v3] - Unknown**" + "\n";
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
            case "rc":
            case "rci":
                if (CHOICE.equalsIgnoreCase("rci") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userRecordCrystals = USER.getBannerData(SELECTED_BANNER.getBannerName());
                if (userRecordCrystals < 10)
                {
                    print_NotEnoughRecordCrystals_Message();
                    return;
                }

                circluatedRecordCrystals = getCirculatedRecordCrystals();
                userRecordCrystals -= 10;
                userRecordCrystals += circluatedRecordCrystals;
                USER.changeValue(SELECTED_BANNER.getBannerName(), userRecordCrystals);
                guaranteedScout = true;
                doSinglePull();
                break;
            default:
                print_UnknownScoutType_smrc_Message();
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
