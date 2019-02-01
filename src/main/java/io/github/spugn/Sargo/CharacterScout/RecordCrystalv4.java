package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

/**
 * RECORD CRYSTAL V4 CHARACTER SCOUT
 * <p>
 *     It's literally the same thing as RecordCrystalv3.
 *
 *     Only difference is that platinum rates have increased
 *     by 1.5x.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.7
 * @see RecordCrystal
 * @see CharacterScout
 */
public class RecordCrystalv4 extends CharacterScout
{
    private int circluatedRecordCrystals;

    public RecordCrystalv4(IChannel channel, int bannerID, String choice, String discordID)
    {
        super(channel, bannerID, choice, discordID);
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
                    scoutMenu.withTitle("Single Pull");
                    break;
                case "m":
                case "mi":
                    scoutMenu.withTitle("[Record Crystal v4] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")");
                    break;
                case "rc":
                case "rci":
                    scoutMenu.withTitle("[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")" + "\n");
                    break;
                default:
                    scoutMenu.withTitle("[Record Crystal v4] - Unknown");
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
                    simpleMessage += "**[Record Crystal v4] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")**" + "\n";
                    break;
                case "rc":
                case "rci":
                    simpleMessage += "**[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Record Crystal v4] - Unknown**" + "\n";
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
