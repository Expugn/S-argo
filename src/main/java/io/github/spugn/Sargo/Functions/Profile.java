package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * PROFILE
 * <p>
 *     Displays a user's statistics from scouting.
 * </p>
 *
 * @author S'pugn
 * @version 1.1
 * @since v1.0
 */
public class Profile
{
    private static IChannel CHANNEL;
    private String DISCORD_ID;

    private EmbedBuilder builder;
    private IUser iUser;
    private String userName;
    private UserParser user;

    private int goldCount;
    private int platinumCount;

    private SortedMap<String, Integer> bannerType;

    /**
     * Generates and displays a user's basic profile.
     *
     * @param channel  Channel where the profile should be displayed
     * @param discordID  Discord ID of the user.
     */
    public Profile(IChannel channel, String discordID)
    {
        CHANNEL = channel;
        DISCORD_ID = discordID;

        if (!(new File("data/Users/USER_" + discordID + ".xml").exists()))
        {
            /* USER FILE DOES NOT EXIST. */
            builder = new EmbedBuilder();
            IUser foundUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
            builder.withAuthorName(foundUser.getName() + "#" + foundUser.getDiscriminator() + "'s Profile");
            builder.withAuthorIcon(foundUser.getAvatarURL());
            builder.withColor(255, 86, 91);
            builder.withThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());
            builder.appendField("USER FILE DOES NOT EXIST", "There is no data for this user.", false);

            CHANNEL.sendMessage(builder.build());
            return;
        }

        init();
        initBannerInfo();

        DecimalFormat df = new DecimalFormat("0.00");

        String basicInfo = "";
        basicInfo += "**Memory Diamonds**: " + user.getMemoryDiamonds() + "\n";
        basicInfo += "**Hacking Crystals**: " + user.getHackingCrystals() + "\n";
        basicInfo += "**Col Balance**: " + user.getColBalance() + "\n\n";

        basicInfo += "**4★ Weapons**: " + user.getTotalWeaponCount() + "\n";
        basicInfo += "**4★ Exchange Swords**: " + user.getR4ExchangeSwords() + "\n";
        basicInfo += "**Rainbow Essences**: " + user.getRainbowEssence() + "\n\n";

        basicInfo += "**Money Spent**: $" + df.format(user.getMoneySpent()) + "\n";
        basicInfo += "**Total Ticket Scouts**: " + user.getTotalTicketScout() + "\n";

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

        String completionProgress;
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

    /**
     * Constructor that determines if a different profile type should be displayed.
     *
     * @param channel  Channel where the data should be displayed.
     * @param discordID  Discord ID of the user.
     * @param menuOption  Profile data type that should be displayed.
     */
    public Profile(IChannel channel, String discordID, int menuOption)
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

    /**
     * Constructor that redirects to banner info or a character search.
     *
     * @param channel  Channel where the data should be displayed
     * @param discordID  Discord ID of the user.
     * @param menuOption  Menu type that should be looked up.
     * @param data  Banner ID or character name.
     */
    public Profile(IChannel channel, String discordID, int menuOption, String data)
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
            bannerSearchMenu(data);
        }
        else
        {
            new WarningMessage("INVALID PROFILE OPTION", "Requested profile type not found.");
        }
    }

    /**
     * Displays the amount of record crystals or what step a user is on in
     * banners that have that type of data.
     */
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

    /**
     * Displays if the user is missing characters or weapons and if they do happen
     * to have the characters or weapons it will tell them that they obtained them
     * and for weapons it will tell them how much they have obtained of that weapon.
     *
     * @param bannerIDString  The banner ID in string form.
     */
    private void bannerInfoMenu(String bannerIDString)
    {
        int bannerID = Integer.parseInt(bannerIDString) - 1;

        /* OPEN BANNERS FILE */
        //BannerParser bannersXML = new BannerParser();
        //List<Banner> banners = bannersXML.getBanners();
        List<Banner> banners = BannerParser.getBanners();

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
                    if (c.getPrefix().equals(oC.getPrefix()) && c.getName().equals(oC.getName()) && (c.getRarity() == oC.getRarity()))
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

            /* WEAPON STATS */
            boolean weaponFound = false;
            int weaponCounter = 0;

            List<Weapon> obtainedWeapons = new ArrayList<>();
            List<Weapon> unobtainedWeapons = new ArrayList<>();

            for (Weapon w : requestedBanner.getWeapons())
            {
                weaponCounter++;
                /* TRY AND FIND WEAPON IN USER BOX */
                for (Weapon oW : user.getWeaponBox())
                {
                    if (w.getName().equals(oW.getName()))
                    {
                        /* WEAPON IS SAME */
                        w.setCount(oW.getCount());
                        weaponFound = true;
                        break;
                    }
                }

                /* ADD WEAPON TO LIST */
                if (!weaponFound)
                {
                    unobtainedWeapons.add(w);
                }
                else
                {
                    obtainedWeapons.add(w);
                }

                weaponFound = false;
            }

            if (obtainedWeapons.size() > 0)
            {
                String obtainedList = "";
                int obtainedCounter = 0;
                for (Weapon w : obtainedWeapons)
                {
                    obtainedList += w.toStringWithCount() + "\n";
                    obtainedCounter++;
                }
                builder.appendField(obtainedCounter + " Weapons Obtained", obtainedList, false);
            }

            if (unobtainedWeapons.size() > 0)
            {
                String noHaveList = "";
                int noHaveCounter = 0;
                for (Weapon w : unobtainedWeapons)
                {
                    noHaveList += w.toString() + "\n";
                    noHaveCounter++;
                }
                builder.appendField(noHaveCounter + " Weapons Missing", noHaveList, false);
            }
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.").get().build());
            return;
        }

        CHANNEL.sendMessage(builder.build());
    }

    /**
     * Searches for characters with the given name and displays a list
     * of all those characters that the user has.
     *
     * @param characterName  Character to be looked up.
     */
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

    /**
     * Initializes the EmbedMessage and variables.
     */
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
        builder.withThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());
    }

    /**
     * Initializes the banner file and constructs a list of all gold/platinum characters
     * in the banner file.
     *
     * This will also delete any characters that the user has that no longer exists in
     * the banners.xml file.
     */
    private void initBannerInfo()
    {
        /* OPEN BANNERS FILE */
        //BannerParser bannersXML = new BannerParser();
        //List<Banner> banners = bannersXML.getBanners();
        List<Banner> banners = BannerParser.getBanners();

        List<String> allGoldCharacters = new ArrayList<>();
        List<String> allPlatinumCharacters = new ArrayList<>();

        for (Banner b : banners)
        {
            /* GET CHARACTERS */
            for (Character c : b.getCharacters())
            {
                if (c.getRarity() == 4 &&
                        !(allGoldCharacters.contains(c.getPrefix() + c.getName())))
                {
                    goldCount++;
                    allGoldCharacters.add(c.getPrefix() + c.getName());
                }
                else if (c.getRarity() == 5 &&
                        !(allPlatinumCharacters.contains(c.getPrefix() + c.getName())))
                {
                    platinumCount++;
                    allPlatinumCharacters.add(c.getPrefix() + c.getName());
                }
            }

            /* CHECK IF BANNER IS NOT NORMAL */
            if (!(b.getBannerType() == 0))
            {
                bannerType.put(b.getBannerName(), b.getBannerType());
            }
        }

        purgeDeletedCharacters(allGoldCharacters, allPlatinumCharacters);
    }

    /**
     * Deletes any gold/platinum character that no longer exists in the
     * Banners.xml file that the user has in their character box.
     *
     * @param gold  List of all gold characters in the banners file
     * @param plat  List of all platinum characters in the banners file
     */
    private void purgeDeletedCharacters(List<String> gold, List<String> plat)
    {
        boolean unsavedChanges = false;
        List<Character> newUserCharacterBox = new ArrayList<>();
        for (Character c : user.getCharacterBox())
        {
            if (c.getRarity() == 4)
            {
                if (gold.contains(c.getPrefix() + c.getName()))
                    newUserCharacterBox.add(c);
                else
                    if (!unsavedChanges)
                        unsavedChanges = true;

            }
            else if (c.getRarity() == 5)
            {
                if (plat.contains(c.getPrefix() + c.getName()))
                    newUserCharacterBox.add(c);
                else
                    if (!unsavedChanges)
                        unsavedChanges = true;
            }
            else
            {
                newUserCharacterBox.add(c);
            }
        }

        if (unsavedChanges)
        {
            user.setCharacterBox(newUserCharacterBox);
            user.saveData();
            user = new UserParser(DISCORD_ID);
        }
    }
}
