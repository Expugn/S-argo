package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.WarningMessage;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

/**
 * RECORD CRYSTAL CHARACTER SCOUT
 * <p>
 *     With Record Crystal character scouts, users are given 1 - 10 "Record Crystals"
 *     with every multi they do. Once a user has at least 10 Record Crystals,
 *     they can perform a sort of "Guaranteed Scout" where they have a 60% chance
 *     to obtain a gold rarity character from the banner. If they hit the 40% chance,
 *     they will get a gold rarity character from a different banner instead.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see CharacterScout
 */
public class RecordCrystal extends CharacterScout
{
    public RecordCrystal(IChannel channel, int bannerID, String choice, String discordID)
    {
        super(channel, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        USER.addBannerInfo(SELECTED_BANNER.getBannerName(), 0);
        bannerTypeData = 0;
    }

    @Override
    protected void modifyScoutData()
    {
        /* RECORD CRYSTAL SCOUTS DO NOT MODIFY ANY SCOUT DATA. */
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
                    scoutMenu.withTitle("[Record Crystal] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")");
                    break;
                case "rc":
                case "rci":
                    scoutMenu.withTitle("[Record Crystal Scout] - " + (bannerTypeData - 10) + " Record Crystals Left");
                    break;
                default:
                    scoutMenu.withTitle("[Record Crystal] - Unknown");
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
                    simpleMessage += "**[Record Crystal] - +" + rcGet + " Record Crystals (" + bannerTypeData + ")**" + "\n";
                    break;
                case "rc":
                case "rci":
                    simpleMessage += "**[Record Crystal Scout] - " + (bannerTypeData - 10) + " Record Crystals Left**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Record Crystal] - Unknown**" + "\n";
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
                    CHANNEL.sendMessage(new WarningMessage("INSUFFICIENT RECORD CRYSTALS", "You need `10` record crystals to do a record crystal scout.\n\n" +
                            "You currently have `" + userRecordCrystals + "` `" + SELECTED_BANNER.getBannerName() + "` Record Crystals.").get().build());
                    return;
                }

                userRecordCrystals -= 10;
                USER.changeValue(SELECTED_BANNER.getBannerName(), userRecordCrystals);
                guaranteedScout = true;
                doSinglePull();
                break;
            default:
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN/UNAVAILABLE SCOUT TYPE", "Only `single`, `multi`, and `record crystal` scouts are available.").get().build());
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
