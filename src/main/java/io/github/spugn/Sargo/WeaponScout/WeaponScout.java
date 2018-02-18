package io.github.spugn.Sargo.WeaponScout;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RateLimitException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WEAPON SCOUT
 * <p>
 *     This class contains all the data and functions needed to perform a
 *     weapon scout sans all the scout type exclusive stuff like rate changes
 *     or price changes. Scout type exclusive stuff can be found in their
 *     appropriate classes (Example: {@link Normal} or {@link StepUp}).
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see Normal
 * @see StepUp
 */
abstract class WeaponScout
{
    /* PACKAGE-PRIVATE VARIABLES */
    IChannel CHANNEL;
    int BANNER_ID;
    String CHOICE;
    UserParser USER;
    double COPPER;
    double GOLD;
    boolean IMAGE_DISABLED;
    boolean SIMPLE_MESSAGE;
    Banner SELECTED_BANNER;
    int bannerTypeData;
    EmbedBuilder scoutMenu;
    String simpleMessage;
    boolean guaranteeAutomaticRifle;
    boolean guaranteeOneGold;
    int userMemoryDiamonds;
    int singleScoutPrice;
    int multiScoutPrice;
    boolean generateImage;
    boolean stopScout;

    /* PRIVATE VARIABLES */
    private String DISCORD_ID;
    private boolean IS_RARITY_STARS;
    private double SILVER;
    private List<Weapon> BANNER_WEAPONS;
    private Random RNG;
    private String imageString;
    private String imageStrings[];
    private File tempUserDirectory;
    private int userColBalance;
    private String weaponString;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeaponScout.class);

    /* PROTECTED VARIABLES */
    protected List<Weapon> weapons;

    WeaponScout(IChannel channel, int bannerID, String choice, String discordID)
    {
        CHANNEL = channel;
        BANNER_ID = bannerID - 1;
        CHOICE = choice;
        DISCORD_ID = discordID;
        generateImage = false;
        stopScout = false;
        init();
    }

    /**
     * Initializes all the variables needed for scouts.
     */
    private void init()
    {
        /* FILES */
        SettingsParser SETTINGS = new SettingsParser();
        List<Banner> BANNERS = new BannerParser().getBanners();
        USER = new UserParser(DISCORD_ID);

        /* SETTINGS */
        IS_RARITY_STARS = SETTINGS.isRarityStars();
        COPPER = (int) ((SETTINGS.getCopperRates() * 100) + (SETTINGS.getPlatinumRates() * 100));
        SILVER = (int) (SETTINGS.getSilverRates() * 100);
        GOLD = (int) (SETTINGS.getGoldRates() * 100);
        IMAGE_DISABLED = SETTINGS.isDisableImages();
        SIMPLE_MESSAGE = SETTINGS.isSimpleMessage();

        /* USER */
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userColBalance = USER.getColBalance();

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
        imageStrings = new String[11];
        weapons = new ArrayList<>();
        weaponString = "";
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        scoutMenu = new EmbedBuilder();
        simpleMessage = "";
        guaranteeAutomaticRifle = false;
        guaranteeOneGold = false;

        /* MEMORY DIAMOND PRICES */
        singleScoutPrice = 15;
        multiScoutPrice = 150;

        /* BANNER */
        if (BANNER_ID < BANNERS.size() && BANNER_ID >= 0)
        {
            SELECTED_BANNER = BANNERS.get(BANNER_ID);
            BANNER_WEAPONS = SELECTED_BANNER.getWeapons();

            bannerTypeData = USER.getBannerData(SELECTED_BANNER.getBannerName() + " Weapons");

            if (USER.isBannerInfoExists(SELECTED_BANNER.getBannerName() + " Weapons") == -1)
            {
                initBannerInfo();
            }

            modifyScoutData();
            LOGGER.debug("\n- Scout Data -\n" +
                    "Single Price " + singleScoutPrice + "\n" +
                    "Multi Price: " + multiScoutPrice + "\n" +
                    "COPPER: " + COPPER + "%\n" +
                    "SILVER: " + SILVER + "%\n" +
                    "GOLD: " + GOLD + "%");
        }
    }

    /**
     * Generates one weapon and the scout result image.
     */
    void doSinglePull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        weapons.add(getWeapon(scout()));

        generateImageString();
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawSingleScout(imageString, false, tempUserDirectory + "/results.png");
    }

    /**
     * Generates eleven weapons and the scout result image.
     */
    void doMultiPull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        for (int i = 0 ; i < 11 ; i++)
        {
            weapons.add(getWeapon(scout()));
        }

        generateImageStrings();
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawMultiScout(imageStrings, false, tempUserDirectory + "/results.png");
    }

    /**
     * Saves gold+ rarity weapons to the user's weapon box and
     * generates the weapon images.
     */
    private void generateImageString()
    {
        if (weapons.get(0).getRarity() == 4)
        {
            USER.addWeapon(weapons.get(0));
        }
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawWeaponImage(weapons.get(0).getImagePath(), weapons.get(0).getRarity(), IS_RARITY_STARS, tempUserDirectory + "/temp_" + 0 + ".png");
        weaponString += weapons.get(0).toString() + "\n";
        imageString = tempUserDirectory + "/temp_" + 0 + ".png";
    }

    /**
     * Same function as {@link #generateImageString()} but for eleven weapons.
     * @see #generateImageString()
     */
    private void generateImageStrings()
    {
        for (int i = 0 ; i < 11 ; i++)
        {
            if (weapons.get(i).getRarity() == 4)
            {
                USER.addWeapon(weapons.get(i));
            }
            if (generateImage && !IMAGE_DISABLED)
                new ImageEditor().drawWeaponImage(weapons.get(i).getImagePath(), weapons.get(i).getRarity(), IS_RARITY_STARS, tempUserDirectory + "/temp_" + i + ".png");
            weaponString += weapons.get(i).toString() + "\n";
            imageStrings[i] = tempUserDirectory + "/temp_" + i + ".png";
        }
    }

    /**
     * Determines a new weapon's rarity using RNG.
     * @return  The rarity value of a new weapon.
     */
    private int scout()
    {
        if (guaranteeOneGold)
        {
            guaranteeOneGold = false;
            return 4;
        }

        double d;
        d = RNG.nextDouble() * 100;

        if (d < COPPER)
        {
            return 2;
        }
        else if (d < COPPER + SILVER)
        {
            return 3;
        }
        else if (d < COPPER + SILVER + GOLD)
        {
            return 4;
        }
        else
        {
            return 2;
        }
    }

    /**
     * Determines which weapon is added to the user's collection
     * depending on the rarity value given.
     *
     * @param rarity  The rarity value of the character.
     * @return  A new {@link Weapon} from the banner or from
     *          the collection of copper or silver weapons.
     */
    private Weapon getWeapon(int rarity)
    {
        Weapon weapon;
        if (rarity == 2)
        {
            if (guaranteeAutomaticRifle)
            {
                Weapon copperAR = new Weapon();
                String copperARName = "Eliza";
                copperAR.setName(copperARName);
                copperAR.setRarity(2);
                copperAR.setImagePath("images/Weapons/" + copperARName.replaceAll(" ", "_") + ".png");
                weapon = copperAR;
                guaranteeAutomaticRifle = false;
            }
            else
            {
                CopperWeapon cW = new CopperWeapon();
                weapon = cW.getWeapon(RNG.nextInt(cW.getSize()));
            }
        }
        else if (rarity == 3)
        {
            if (guaranteeAutomaticRifle)
            {
                Weapon silverAR = new Weapon();
                String silverARName = "Rebecca";
                silverAR.setName(silverARName);
                silverAR.setRarity(3);
                silverAR.setImagePath("images/Weapons/" + silverARName.replaceAll(" ", "_") + ".png");
                weapon = silverAR;
                guaranteeAutomaticRifle = false;
            }
            else
            {
                SilverWeapon sW = new SilverWeapon();
                weapon = sW.getWeapon(RNG.nextInt(sW.getSize()));
            }
        }
        else
        {
            weapon = BANNER_WEAPONS.get(RNG.nextInt(BANNER_WEAPONS.size()));
        }

        giveCol(weapon);
        return weapon;
    }

    /**
     * Gives Col to the user based on the rarity of the weapon given.
     *
     * @param w  Copper or Silver {@link Weapon} to be converted into Col.
     */
    private void giveCol(Weapon w)
    {
        final int COL_MAX = 99999999;
        switch(w.getRarity())
        {
            case 2:
                userColBalance += 1600;
                break;
            case 3:
                userColBalance += 7500;
                break;
            default:
                break;
        }

        if (userColBalance > COL_MAX)
        {
            userColBalance = COL_MAX;
        }

        USER.setColBalance(userColBalance);
    }

    /**
     * Displays the scout results to the channel where the command was
     * requested and save the results to the user file afterwards. If
     * the results fail to display for any reason then the user file
     * is not saved.
     */
    void displayAndSave()
    {
        if (!SIMPLE_MESSAGE)
        {
            scoutMenu.withAuthorName(SELECTED_BANNER.getBannerName());
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
                scoutMenu.appendField("- Weapon Result -", weaponString, false);
            scoutMenu.withAuthorIcon(new GitHubImage("images/System/Scout_Icon.png").getURL());
            scoutMenu.withColor(139, 69, 19);
            if (weapons.get(0).getRarity() == 2)
                scoutMenu.withThumbnail(new GitHubImage("images/System/Brown_Chest.png").getURL());
            else if (weapons.get(0).getRarity() == 3)
                scoutMenu.withThumbnail(new GitHubImage("images/System/Blue_Chest.png").getURL());
            else if (weapons.get(0).getRarity() == 4)
                scoutMenu.withThumbnail(new GitHubImage("images/System/Red_Chest.png").getURL());
            scoutMenu.withFooterIcon(new GitHubImage("images/System/Memory_Diamond_Icon.png").getURL());
            scoutMenu.withFooterText((CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator()) + " | " + USER.getMemoryDiamonds() + " Memory Diamonds Left");

            LOGGER.debug("Displaying Scout Result...");
            IMessage display = null;
            try
            {
                if (generateImage && !IMAGE_DISABLED)
                    display = CHANNEL.sendFile(scoutMenu.build(), new File(tempUserDirectory + "/results.png"));
                else
                    display = CHANNEL.sendMessage(scoutMenu.build());
            }
            catch (FileNotFoundException e)
            {
                CHANNEL.sendMessage(new WarningMessage("FAILED TO GENERATE IMAGE", "Unable to display scout result.").get().build());
                display.delete();
                deleteTempDirectory();
                return;
            }
            catch (RateLimitException e)
            {
                EmbedBuilder rateLimited = new WarningMessage("RATE LIMIT EXCEPTION", "Slow down on the requests!").get();
                try
                {
                    display.edit(rateLimited.build());
                }
                catch (NullPointerException a)
                {
                    // DO SOMETHING
                }
                deleteTempDirectory();
                return;
            }
        }
        else
        {
            simpleMessage += "**" + SELECTED_BANNER.getBannerName() + "**" + "\n";
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
            {
                simpleMessage += "**- Weapon Result -**" + "\n";
                simpleMessage += weaponString;
            }
            simpleMessage += (CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator()) + " | " + USER.getMemoryDiamonds() + " Memory Diamonds Left";

            LOGGER.debug("Displaying Scout Result...");
            IMessage display = null;
            try
            {
                if (generateImage && !IMAGE_DISABLED)
                {
                    display = CHANNEL.sendFile(simpleMessage, new File(tempUserDirectory + "/results.png"));
                }
                else
                {
                    display = CHANNEL.sendMessage(simpleMessage);
                }

            }
            catch (FileNotFoundException e)
            {
                CHANNEL.sendMessage(new WarningMessage("FAILED TO GENERATE IMAGE", "Unable to display scout result.").get().build());
                display.delete();
                deleteTempDirectory();
                return;
            }
            catch (RateLimitException e)
            {
                EmbedBuilder rateLimited = new WarningMessage("RATE LIMIT EXCEPTION", "Slow down on the requests!").get();
                try
                {
                    display.edit(rateLimited.build());
                }
                catch (NullPointerException a)
                {
                    // DO SOMETHING
                }
                deleteTempDirectory();
                return;
            }
        }

        LOGGER.debug("Saving User Data...");
        USER.saveData();
        deleteTempDirectory();
    }

    /**
     * Delete the temp directory that had all the generated images for the scout.
     */
    private void deleteTempDirectory()
    {
        if (tempUserDirectory.exists())
        {
            String[] entries = tempUserDirectory.list();
            for (String s : entries)
            {
                File currentFile = new File(tempUserDirectory.getPath(), s);
                currentFile.delete();
            }
            tempUserDirectory.delete();
        }
    }

    /**
     * Determines what the {@link #bannerTypeData} should be when there is
     * no data for the banner in the user's file.
     *
     * @see StepUp#initBannerInfo()
     */
    protected abstract void initBannerInfo();

    /**
     * Modifies the rates or prices for scouts.
     * Modifications are usually determined by what Step
     * the user is on.
     *
     * @see StepUp#modifyScoutData()
     */
    protected abstract void modifyScoutData();

    /**
     * Advances to the next step in a Step Up scout.
     *
     * @see StepUp#updateBannerData()
     */
    protected abstract void updateBannerData();

    /**
     * Sets up the title portion of the scout result UI.
     * The title has information such as the scout type,
     * current Step, etc.
     *
     * @see Normal#setupScoutMenu()
     * @see StepUp#setupScoutMenu()
     */
    protected abstract void setupScoutMenu();

    /**
     * Determines how the flow of scouts will go. Check
     * if the desired scout type is valid, if the user
     * has enough Memory Diamonds, etc in the process.
     *
     * @see Normal#run()
     * @see StepUp#run()
     */
    protected abstract void run();
}
