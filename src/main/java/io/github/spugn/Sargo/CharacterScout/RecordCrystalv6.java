package io.github.spugn.Sargo.CharacterScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;

import java.util.List;

/**
 * RECORD CRYSTAL V4 CHARACTER SCOUT
 * <p>
 *     It's literally the same thing as RecordCrystalv4.
 *
 *     Only difference is that you cannot get any gold
 *     characters in the record crystal scout.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v3.3
 * @see RecordCrystal
 * @see CharacterScout
 */
public class RecordCrystalv6 extends CharacterScout
{
    private int circluatedRecordCrystals;

    public RecordCrystalv6(Message message, int bannerID, String choice, String discordID)
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
        if (CHOICE.equalsIgnoreCase("m") ||
                CHOICE.equalsIgnoreCase("mi"))
        {
            randomizeResults = true;

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
        /* GET A RANDOM PLATINUM VARIANT CHARACTER, IF THERE IS A PLATINUM
           VARIANT OF THAT CHARACTER IN THE BANNER THEN GET A NEW CHARACTER. */
        Character c = null;
        boolean charInBanner = true;
        boolean characterExcluded = true;
        int randIndex;
        Banner randBanner;
        List<Character> randCharacters;
        boolean sameName;
        boolean samePrefix;

        while(charInBanner)
        {
            while (characterExcluded)
            {
                randIndex = PLATINUM_BANNERS_V2.get(RNG.nextInt(PLATINUM_BANNERS_V2.size()));
                randBanner = BANNERS.get(randIndex - 1);
                randCharacters = randBanner.getCharacters();
                c = randCharacters.get(RNG.nextInt(randCharacters.size()));

                for (String excludedCharacter : BannerParser.getExcludedCharacters())
                {
                    String[] parsedECS = BannerParser.parseExcludeCharacterString(excludedCharacter);

                    // COMPARE BANNER ID, PREFIX, NAME, AND RARITY
                    if (parsedECS[0].equals(Integer.toString(randIndex)) &&
                            parsedECS[1].equals(c.getPrefix()) &&
                            parsedECS[2].equals(c.getName()) &&
                            parsedECS[3].equals(Integer.toString(c.getRarity())))
                    {
                        // CHARACTER IS IN THE EXCLUDED LIST, CHOOSE ANOTHER CHARACTER
                        characterExcluded = true;
                        break;
                    }
                    else
                    {
                        // CHARACTER IS NOT IN THE EXCLUDED LIST. PROCEED.
                        characterExcluded = false;
                    }
                }
            }

            // CHECK IF THE CHARACTER DRAWN IS A CHARACTER THAT HAS THE SAME NAME/PREFIX OF A CHARACTER IN THE BANNER.
            for (Character bc : SELECTED_BANNER.getCharacters())
            {
                sameName = c.getName().equalsIgnoreCase(bc.getName());
                samePrefix = c.getPrefix().equalsIgnoreCase(bc.getPrefix());

                if (!(sameName && samePrefix))
                {
                    charInBanner = false;
                }
                else
                {
                    charInBanner = true;
                    break;
                }
            }
        }

        return c;
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
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal v6] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")"));
                    break;
                case "rc":
                case "rci":
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")" + "\n"));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[Record Crystal v6] - Unknown"));
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
                    simpleMessage += "**[Record Crystal v6] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")**" + "\n";
                    break;
                case "rc":
                case "rci":
                    simpleMessage += "**[Record Crystal Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Record Crystal v6] - Unknown**" + "\n";
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
                guaranteedScoutNoGold = true;
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