package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.text.DecimalFormat;
import java.util.*;

public class Profile
{
    private static IChannel CHANNEL;
    private static String DISCORD_ID;

    private EmbedBuilder builder;
    private IUser iUser;
    private String userName;
    private UserParser user;

    private int goldCount;
    private int platinumCount;

    private SortedMap<String, Integer> bannerType;

    public Profile(IChannel channel, String discordID)
    {
        CHANNEL = channel;
        DISCORD_ID = discordID;

        init();
        initBannerInfo();

        DecimalFormat df = new DecimalFormat("#.00");

        String basicInfo = "";
        basicInfo += "**Memory Diamonds**: " + user.getMemoryDiamonds() + "\n";
        basicInfo += "**Hacking Crystals**: " + user.getHackingCrystals() + "\n";
        basicInfo += "**Money Spent**: $" + df.format(user.getMoneySpent());

        builder.appendField("Information", basicInfo, false);

        /* CHARACTER BOX */
        String characterInfo = "";

        int cCTotal = new CopperCharacter().getSize();
        int sCTotal = new SilverCharacter().getSize();

        int userCopper = user.getCopperCount();
        int userSilver = user.getSilverCount();
        int userGold = user.getGoldCount();
        int userPlatinum = user.getPlatinumCount();

        characterInfo += "**[2 ★]** - " + userCopper + "/" + cCTotal + "\n";
        characterInfo += "**[3 ★]** - " + userSilver + "/" + sCTotal + "\n";
        characterInfo += "**[4 ★]** - " + userGold + "/" + goldCount + "\n";
        characterInfo += "**[5 ★]** - " + userPlatinum + "/" + platinumCount;

        builder.appendField("Characters", characterInfo, false);

        String completionProgress = "";
        int totalOwned = userCopper + userSilver + userGold + userPlatinum;
        int totalCharacters = cCTotal + sCTotal + goldCount + platinumCount;

        double dTotalOwned = userCopper + userSilver + userGold + userPlatinum;
        double dTotalCharacters = cCTotal + sCTotal + goldCount + platinumCount;

        if (totalOwned == totalCharacters)
        {
            completionProgress = "**★ 100% ★**";
        }
        else
        {
            completionProgress = df.format((dTotalOwned / dTotalCharacters) * 100) + "%";
        }
        completionProgress += " (" + totalOwned + "/" + totalCharacters + ")";

        builder.appendField("Completion", completionProgress, false);

        CHANNEL.sendMessage(builder.build());
    }

    public Profile (IChannel channel, String discordID, int menuOption)
    {
        CHANNEL = channel;
        DISCORD_ID = discordID;

        init();
        initBannerInfo();

        /* 'profile data' */
        if (menuOption == 1)
        {
            bannerDataMenu();
        }
        else
        {
            new WarningMessage("INVALID PROFILE OPTION", "Requested profile type not found.");
        }
    }

    public Profile (IChannel channel, String discordID, int menuOption, String data)
    {
        CHANNEL = channel;
        DISCORD_ID = discordID;

        init();
        initBannerInfo();

        /* 'profile info <bannerID>' */
        if (menuOption == 2)
        {
            bannerInfoMenu(data);
        }
        /* 'profile search <character name>' */
        else if (menuOption == 3)
        {
            System.out.println("data " + data);
            bannerSearchMenu(data);
        }
        else
        {
            new WarningMessage("INVALID PROFILE OPTION", "Requested profile type not found.");
        }
    }

    private void bannerDataMenu()
    {
        /* BANNER INFO */
        String bannerInfo = "";

        for(final Map.Entry<String, Integer> e : bannerType.entrySet())
        {
            if (e.getValue() == 1 || e.getValue() == 3)
            {
                /* IF USER IS ON A STEP LARGER THAN 1 */
                if (user.getBannerData(e.getKey()) > 1)
                {
                    bannerInfo += "***" + e.getKey() + "***:\n\t";
                    bannerInfo += "Step " + user.getBannerData(e.getKey()) + "\n";
                }
            }
            else if (e.getValue() == 2)
            {
                /* IF USER HAS MORE THAN 0 RECORD CRYSTALS */
                if (user.getBannerData(e.getKey()) > 0)
                {
                    bannerInfo += "***" + e.getKey() + "***:\n\t";
                    bannerInfo += "" + user.getBannerData(e.getKey()) + " Record Crystals\n";
                }
            }
        }

        if (!bannerInfo.isEmpty())
        {
            builder.appendField("Banner Data", bannerInfo, false);
        }
        else
        {
            builder.appendField("Banner Data", "No available banner data.", false);
        }

        CHANNEL.sendMessage(builder.build());
    }

    private void bannerInfoMenu(String bannerIDString)
    {
        int bannerID = Integer.parseInt(bannerIDString) - 1;

        /* OPEN BANNERS FILE */
        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig(Files.BANNER_XML.get());

        if (bannerID < banners.size() && bannerID >= 0)
        {
            Banner requestedBanner = banners.get(bannerID);
            boolean characterFound = false;
            int characterCounter = 0;

            List<Character> obtainedCharacters = new ArrayList<>();
            List<Character> unobtainedCharacters = new ArrayList<>();

            for (Character c : requestedBanner.getCharacters())
            {
                characterCounter++;
                /* TRY AND FIND CHARACTER IN USER BOX */
                for (Character oC : user.getCharacterBox())
                {
                    if (c.getPrefix().equals(oC.getPrefix()) && c.getName().equals(oC.getName()))
                    {
                        /* CHARACTER IS SAME */
                        characterFound = true;
                        break;
                    }
                }

                /* ADD CHARACTER TO LIST */
                if (!characterFound)
                {
                    unobtainedCharacters.add(c);
                }
                else
                {
                    obtainedCharacters.add(c);
                }

                characterFound = false;
            }

            builder.appendField(requestedBanner.getBannerName(), characterCounter + " Characters Available", false);

            if (obtainedCharacters.size() > 0)
            {
                String obtainedList = "";
                int obtainedCounter = 0;
                for (Character c : obtainedCharacters)
                {
                    obtainedList += c.toString() + "\n";
                    obtainedCounter++;
                }
                builder.appendField(obtainedCounter + " Characters Obtained", obtainedList, false);
            }

            if (unobtainedCharacters.size() > 0)
            {
                String noHaveList = "";
                int noHaveCounter = 0;
                for (Character c : unobtainedCharacters)
                {
                    noHaveList += c.toString() + "\n";
                    noHaveCounter++;
                }
                builder.appendField(noHaveCounter + " Characters Missing", noHaveList, false);
            }
    }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
            return;
        }

        CHANNEL.sendMessage(builder.build());
    }

    private void bannerSearchMenu(String characterName)
    {
        String characterList = "";
        int characterCount = 0;
        String correctName = "";
        boolean fetchCorrectName = true;

        for (Character c : user.getCharacterBox())
        {
            if (c.getName().equalsIgnoreCase(characterName))
            {
                characterList += c.toStringNoName() + "\n";
                characterCount++;

                if (fetchCorrectName)
                {
                    fetchCorrectName = false;
                    correctName = c.getName();
                }
            }
        }

        if (!characterList.isEmpty())
        {
            builder.appendField("Character Search: " + correctName, characterList, false);
            builder.withFooterText(characterCount + " " + correctName + " found.");
        }
        else
        {
            builder.appendField("Character Search", "Could not find data for \"" + characterName + "\"", false);
        }

        CHANNEL.sendMessage(builder.build());
    }

    private void init()
    {
        builder = new EmbedBuilder();
        iUser = CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID));
        user = new UserParser(DISCORD_ID);
        userName = iUser.getName() + "#" + iUser.getDiscriminator();
        goldCount = 0;
        platinumCount = 0;
        bannerType = new TreeMap<>();

        builder.withAuthorName(userName + "'s Profile");
        builder.withAuthorIcon(iUser.getAvatarURL());
        builder.withColor(255, 86, 91);
        builder.withThumbnail(Images.PROFILE_ICON.getUrl());
    }

    private void initBannerInfo()
    {
        /* OPEN BANNERS FILE */
        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig(Files.BANNER_XML.get());
        List<String> allCharacterPrefixes = new ArrayList<>();

        for (Banner b : banners)
        {
            /* GET CHARACTERS */
            for (Character c : b.getCharacters())
            {
                if (c.getRarity().equals("4") && !(allCharacterPrefixes.contains(c.getPrefix())))
                {
                    goldCount++;
                    allCharacterPrefixes.add(c.getPrefix());
                }
                else if (c.getRarity().equals("5") && !(allCharacterPrefixes.contains(c.getPrefix())))
                {
                    platinumCount++;
                    allCharacterPrefixes.add(c.getPrefix());
                }
            }

            /* CHECK IF BANNER IS NOT NORMAL */
            if (!b.getBannerType().equals("0"))
            {
                bannerType.put(b.getBannerName(), Integer.parseInt(b.getBannerType()));
            }
        }
    }
}
