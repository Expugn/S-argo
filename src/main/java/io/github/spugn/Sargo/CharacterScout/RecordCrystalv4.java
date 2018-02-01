package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.WarningMessage;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

/**
 * RECORD CRYSTAL V4 CHARACTER SCOUT
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
 *     Record Crystal v4 has the same mechanic as Record Crystal v3 where with every
 *     record crystal scout you perform you have a chance to win 1-5
 *     record crystals back, except instead of 1-5 record crystals at rates where
 *     it is most likely to get 2 record crystals it uses the same base record crystal
 *     rates.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.3
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
                    scoutMenu.withTitle("**[Guaranteed Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")**" + "\n");
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
                    simpleMessage += "**[Guaranteed Scout] - " + ((bannerTypeData - 10) + circluatedRecordCrystals) + " Record Crystals Left (+" + circluatedRecordCrystals + ")**" + "\n";
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
            case "rc":
            case "rci":
                if (CHOICE.equalsIgnoreCase("rci") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userRecordCrystals = USER.getBannerData(SELECTED_BANNER.getBannerName());
                if (userRecordCrystals < 10)
                {
                    CHANNEL.sendMessage(new WarningMessage("INSUFFICIENT RECORD CRYSTALS", "You need 10 record crystals to do a record crystal scout.").get().build());
                    return;
                }

                circluatedRecordCrystals = getRecordCrystals();
                userRecordCrystals -= 10;
                userRecordCrystals += circluatedRecordCrystals;
                USER.changeValue(SELECTED_BANNER.getBannerName(), userRecordCrystals);
                guaranteedScout = true;
                doSinglePull();
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
