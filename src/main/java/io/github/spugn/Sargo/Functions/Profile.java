package io.github.spugn.Sargo.Functions;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * PROFILE
 * <p>
 *     Displays a user's statistics from scouting.
 * </p>
 *
 * @author S'pugn
 * @version 2.0
 * @since v1.0
 */
public class Profile
{
    //private static IChannel CHANNEL;

    private String DISCORD_ID;
    private TextChannel CHANNEL;

    //private EmbedBuilder builder;
    //private IUser iUser;
    private String userName;
    private UserParser user;
    private Member member;

    private int goldCount;
    private int platinumCount;
    private int platinum6Count;

    private SortedMap<String, Integer> bannerType;

    /**
     * Generates and displays a user's basic profile.
     *
     * @param message Message that was sent that triggered the bot
     * @param discordID  Discord ID of the user.
     */
    public Profile(Message message, String discordID)
    {
        CHANNEL = (TextChannel) message.getChannel().block();
        DISCORD_ID = discordID;

        if (!(new File("data/Users/USER_" + discordID + ".xml").exists()))
        {
            // USER FILE DOES NOT EXIST.
            Consumer<EmbedCreateSpec> ecsTemplate = s -> {
                Member discordMember = CHANNEL.getGuild().block().getMemberById(Snowflake.of(discordID)).block();
                s.setAuthor(discordMember.getUsername() + "#" + discordMember.getDiscriminator() + "'s Profile", "", discordMember.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.addField("USER FILE DOES NOT EXIST", "There is no data for this user.", false);
            };
            Sargo.sendEmbed(CHANNEL, ecsTemplate);
            return;
        }

        init();
        initBannerInfo();

        DecimalFormat df = new DecimalFormat("0.00");

        String basicInfo = "";
        basicInfo += "**Memory Diamonds**: " + user.getMemoryDiamonds() + "\n";
        basicInfo += "**Hacking Crystals**: " + user.getHackingCrystals() + "\n";
        basicInfo += "**Col Balance**: " + user.getColBalance() + "\n\n";

        basicInfo += "**Total Weapons Pulled**: " + user.getTotalWeaponCount() + "\n";
        basicInfo += "**4★ Weapons**: " + user.getTotalR4WeaponCount() + "\n";
        basicInfo += "**5★ Weapons**: " + user.getTotalR5WeaponCount() + "\n\n";
        basicInfo += "**4★ Exchange Swords**: " + user.getR4ExchangeSwords() + "\n";
        basicInfo += "**Rainbow Essences**: " + user.getRainbowEssence() + "\n";
        basicInfo += "**Upgrade Crystals**: " + user.getUpgradeCrystal() + "\n";
        basicInfo += "**Memory Fragments**: " + user.getMemoryFragment() + "\n\n";

        basicInfo += "**Money Spent**: $" + df.format(user.getMoneySpent()) + "\n";
        basicInfo += "**Total Ticket Scouts**: " + user.getTotalTicketScout() + "\n";

        /* CHARACTER BOX */
        String characterInfo = "";

        int cCTotal = new CopperCharacter().getSize();
        int sCTotal = new SilverCharacter().getSize();

        int userCopper = user.getCopperCount();
        int userSilver = user.getSilverCount();
        int userGold = user.getGoldCount();
        int userPlatinum = user.getPlatinumCount();
        int userPlatinum6 = user.getPlatinum6Count();

        characterInfo += "**[2 ★]** - " + userCopper + "/" + cCTotal + "\n";
        characterInfo += "**[3 ★]** - " + userSilver + "/" + sCTotal + "\n";
        characterInfo += "**[4 ★]** - " + userGold + "/" + goldCount + "\n";
        characterInfo += "**[5 ★]** - " + userPlatinum + "/" + platinumCount + "\n";
        characterInfo += "**[6 ★]** - " + userPlatinum6 + "/" + platinum6Count;

        String completionProgress;
        int totalOwned = userCopper + userSilver + userGold + userPlatinum + userPlatinum6;
        int totalCharacters = cCTotal + sCTotal + goldCount + platinumCount + platinum6Count;

        double dTotalOwned = userCopper + userSilver + userGold + userPlatinum;
        double dTotalCharacters = cCTotal + sCTotal + goldCount + platinumCount + platinum6Count;

        if (totalOwned == totalCharacters)
        {
            completionProgress = "**★ 100% ★**";
        }
        else
        {
            completionProgress = df.format((dTotalOwned / dTotalCharacters) * 100) + "%";
        }
        completionProgress += " (" + totalOwned + "/" + totalCharacters + ")";

        String basicInfo_final = basicInfo;
        String characterInfo_final = characterInfo;
        String completionProgress_final = completionProgress;
        Consumer<EmbedCreateSpec> ecsTemplate = s -> {
            s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
            s.setColor(new Color(255, 86, 91));
            s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

            s.addField("Information", basicInfo_final, false);
            s.addField("Characters", characterInfo_final, false);
            s.addField("Completion", completionProgress_final, false);
        };
        Sargo.sendEmbed(CHANNEL, ecsTemplate);
    }

    /**
     * Constructor that determines if a different profile type should be displayed.
     *
     * @param message Message that was sent that triggered the bot.
     * @param discordID  Discord ID of the user.
     * @param menuOption  Profile data type that should be displayed.
     */
    public Profile(Message message, String discordID, int menuOption)
    {
        CHANNEL = (TextChannel) message.getChannel().block();
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
            Sargo.replyToMessage_Warning(message, "INVALID PROFILE OPTION", "Requested profile type not found.");
        }
    }

    /**
     * Constructor that redirects to banner info or a character search.
     *
     * @param message User message
     * @param discordID  Discord ID of the user.
     * @param menuOption  Menu type that should be looked up.
     * @param data  Banner ID or character name.
     */
    public Profile(Message message, String discordID, int menuOption, String data)
    {

        CHANNEL = (TextChannel) message.getChannel().block();
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
            Sargo.replyToMessage_Warning(message, "INVALID PROFILE OPTION", "Requested profile type not found.");
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

        Consumer<EmbedCreateSpec> ecsTemplate;
        if (!bannerInfo.isEmpty())
        {
            String bannerInfo_final = bannerInfo;

            ecsTemplate = s -> {
                s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                s.addField("Banner Data", bannerInfo_final, false);
            };
        }
        else
        {
            ecsTemplate = s -> {
                s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                s.addField("Banner Data", "No available banner data.", false);
            };
        }

        Sargo.sendEmbed(CHANNEL, ecsTemplate);
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
        Consumer<EmbedCreateSpec> ecsTemplate;

        // OPEN BANNERS FILE
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
                // TRY AND FIND CHARACTER IN USER BOX
                for (Character oC : user.getCharacterBox())
                {
                    if (c.getPrefix().equals(oC.getPrefix()) && c.getName().equals(oC.getName()) && (c.getRarity() == oC.getRarity()))
                    {
                        // CHARACTER IS SAME
                        characterFound = true;
                        break;
                    }
                }

                // ADD CHARACTER TO LIST
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


            String obtainedList = "";
            int obtainedCounter = 0;
            if (obtainedCharacters.size() > 0)
            {
                for (Character c : obtainedCharacters)
                {
                    obtainedList += c.toString() + "\n";
                    obtainedCounter++;
                }
            }

            String noHaveList = "";
            int noHaveCounter = 0;
            if (unobtainedCharacters.size() > 0)
            {
                for (Character c : unobtainedCharacters)
                {
                    noHaveList += c.toString() + "\n";
                    noHaveCounter++;
                }
            }

            /* WEAPON STATS */
            boolean weaponFound = false;

            List<Weapon> obtainedWeapons = new ArrayList<>();
            List<Weapon> unobtainedWeapons = new ArrayList<>();

            for (Weapon w : requestedBanner.getWeapons())
            {
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

            String obtainedList_wep = "";
            int obtainedCounter_wep = 0;
            if (obtainedWeapons.size() > 0)
            {
                for (Weapon w : obtainedWeapons)
                {
                    obtainedList_wep += w.toStringWithCount() + "\n";
                    obtainedCounter_wep++;
                }
            }

            String noHaveList_wep = "";
            int noHaveCounter_wep = 0;
            if (unobtainedWeapons.size() > 0)
            {
                for (Weapon w : unobtainedWeapons)
                {
                    noHaveList_wep += w.toString() + "\n";
                    noHaveCounter_wep++;
                }
            }

            int characterCounter_final = characterCounter;
            int obtainedCounter_final = obtainedCounter;
            String obtainedList_final = obtainedList;
            int noHaveCounter_final = noHaveCounter;
            String noHaveList_final = noHaveList;
            int obtainedCounter_wep_final = obtainedCounter_wep;
            String obtainedList_wep_final = obtainedList_wep;
            int noHaveCounter_wep_final = noHaveCounter_wep;
            String noHaveList_wep_final = noHaveList_wep;

            ecsTemplate = s -> {
                s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                s.addField(requestedBanner.getBannerName(), characterCounter_final + " Characters Available", false);
                if (obtainedCharacters.size() > 0)
                    s.addField(obtainedCounter_final + " Characters Obtained", obtainedList_final, false);
                if (unobtainedCharacters.size() > 0)
                    s.addField(noHaveCounter_final + " Characters Missing", noHaveList_final, false);
                if (obtainedWeapons.size() > 0)
                    s.addField(obtainedCounter_wep_final + " Weapons Obtained", obtainedList_wep_final, false);
                if (unobtainedWeapons.size() > 0)
                    s.addField(noHaveCounter_wep_final + " Weapons Missing", noHaveList_wep_final, false);
            };
        }
        else
        {
            Sargo.replyToMessage_Warning(CHANNEL, "UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.");
            return;
        }

        Sargo.sendEmbed(CHANNEL, ecsTemplate);
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

        Consumer<EmbedCreateSpec> ecsTemplate;
        if (!characterList.isEmpty())
        {
            String correctName_final = correctName;
            String characterList_final = characterList;
            int characterCount_final = characterCount;

            ecsTemplate = s -> {
                s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                s.addField("Character Search: " + correctName_final, characterList_final, false);
                s.setFooter(characterCount_final + " " + correctName_final + " found.", "");
            };
        }
        else
        {
            ecsTemplate = s -> {
                s.setAuthor(userName + "'s Profile", "", member.getAvatarUrl());
                s.setColor(new Color(255, 86, 91));
                s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                s.addField("Character Search", "Could not find data for \"" + characterName + "\"", false);
            };
        }

        Sargo.sendEmbed(CHANNEL, ecsTemplate);
    }

    /**
     * Initializes the EmbedMessage and variables.
     */
    private void init()
    {
        member = CHANNEL.getGuild().block().getMemberById(Snowflake.of(DISCORD_ID)).block();
        user = new UserParser(DISCORD_ID);
        userName = member.getUsername() + "#" + member.getDiscriminator();

        goldCount = 0;
        platinumCount = 0;
        platinum6Count = 0;
        bannerType = new TreeMap<>();
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
        List<Banner> banners = BannerParser.getBanners();

        List<String> allGoldCharacters = new ArrayList<>();
        List<String> allPlatinumCharacters = new ArrayList<>();
        List<String> allPlatinum6Characters = new ArrayList<>();

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
                else if (c.getRarity() == 6 &&
                        !(allPlatinum6Characters.contains(c.getPrefix() + c.getName())))
                {
                    platinum6Count++;
                    allPlatinum6Characters.add(c.getPrefix() + c.getName());
                }
            }

            /* CHECK IF BANNER IS NOT NORMAL */
            if (!(b.getBannerType() == 0))
            {
                bannerType.put(b.getBannerName(), b.getBannerType());
            }
        }

        purgeDeletedCharacters(allGoldCharacters,
                                allPlatinumCharacters,
                                allPlatinum6Characters);
    }

    /**
     * Deletes any gold/platinum character that no longer exists in the
     * Banners.xml file that the user has in their character box.
     *
     * @param gold  List of all gold characters in the banners file
     * @param plat  List of all platinum characters in the banners file
     */
    private void purgeDeletedCharacters(List<String> gold, List<String> plat, List<String> plat6)
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
            else if (c.getRarity() == 6)
            {
                if (plat6.contains(c.getPrefix() + c.getName()))
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
