package io.github.spugn.Sargo.WeaponScout;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.CopperWeapon;
import io.github.spugn.Sargo.Objects.SilverWeapon;
import io.github.spugn.Sargo.Objects.Weapon;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.ScoutSettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

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
    TextChannel TEXT_CHANNEL;
    int BANNER_ID;
    String CHOICE;
    UserParser USER;
    double COPPER;
    double GOLD;
    boolean IMAGE_DISABLED;
    boolean SIMPLE_MESSAGE;
    Banner SELECTED_BANNER;
    int bannerTypeData;
    Consumer<EmbedCreateSpec> sMenu;
    String simpleMessage;
    boolean guaranteeAutomaticRifle;
    boolean guaranteeOneGold;
    boolean guaranteeSpecificWeapon;
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
    private int bannerType;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeaponScout.class);

    /* PROTECTED VARIABLES */
    protected List<Weapon> weapons;

    WeaponScout(Message message, int bannerID, String choice, String discordID)
    {
        TEXT_CHANNEL = (TextChannel) message.getChannel().block();
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
        List<Banner> BANNERS = BannerParser.getBanners();
        USER = new UserParser(DISCORD_ID);

        /* SETTINGS */
        IS_RARITY_STARS = ScoutSettingsParser.isRarityStars();
        COPPER = (int) ((ScoutSettingsParser.getCopperRate() * 100) + (ScoutSettingsParser.getPlatinumRate() * 100));
        SILVER = (int) (ScoutSettingsParser.getSilverRate() * 100);
        GOLD = (int) (ScoutSettingsParser.getGoldRate() * 100);
        IMAGE_DISABLED = ScoutSettingsParser.isDisableImages();
        SIMPLE_MESSAGE = ScoutSettingsParser.isSimpleMessage();

        /* USER */
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userColBalance = USER.getColBalance();

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
        imageStrings = new String[11];
        weapons = new ArrayList<>();
        weaponString = "";
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        simpleMessage = "";
        guaranteeAutomaticRifle = false;
        guaranteeOneGold = false;
        guaranteeSpecificWeapon = false;

        /* MEMORY DIAMOND PRICES */
        singleScoutPrice = 15;
        multiScoutPrice = 150;

        /* BANNER */
        if (BANNER_ID < BANNERS.size() && BANNER_ID >= 0)
        {
            SELECTED_BANNER = BANNERS.get(BANNER_ID);
            BANNER_WEAPONS = SELECTED_BANNER.getWeapons();
            bannerType = SELECTED_BANNER.getBannerWepType();

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
            // WEAPON STEP UP V3 - STEP 5: FIRST WEAPON IN BANNER IS GUARANTEED
            if (bannerType == 4 && guaranteeSpecificWeapon)
            {
                weapon = BANNER_WEAPONS.get(0);
                guaranteeSpecificWeapon = false;
            }
            else
            {
                // WEAPON STEP UP V3 - FIRST WEAPON HAS 1/2 OF THE GOLD WEAPON RATES, EVERYONE ELSE SHARES THE OTHER HALF.
                if (bannerType == 4)
                {
                    double d = RNG.nextDouble();
                    if (d < 0.5)
                    {
                        weapon = BANNER_WEAPONS.get(0);
                    }
                    else
                    {
                        // CHECK IF THERE IS MORE THAN ONE GOLD WEAPON
                        if (BANNER_WEAPONS.size() > 1)
                        {
                            // GET A WEAPON FROM INDEX 1 -> WHATEVER AMOUNT ARE IN THE BANNER
                            weapon = BANNER_WEAPONS.get(RNG.nextInt(BANNER_WEAPONS.size() - 1) + 1);
                        }
                        else
                        {
                            // LESS THAN ONE GOLD WEAPON?... BUSINESS AS USUAL
                            weapon = BANNER_WEAPONS.get(RNG.nextInt(BANNER_WEAPONS.size()));
                        }
                    }
                }
                else
                {
                    weapon = BANNER_WEAPONS.get(RNG.nextInt(BANNER_WEAPONS.size()));
                }
            }
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
        final int COL_MAX = 999999999;
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
        Member member = TEXT_CHANNEL.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(DISCORD_ID))).block();
        String username = member.getUsername() + "#" + member.getDiscriminator();
        if (!SIMPLE_MESSAGE)
        {
            sMenu = s -> {
                s.setColor(new Color(139, 69, 19));
                s.setAuthor(SELECTED_BANNER.getBannerName(), "", "");
            };

            setupScoutMenu();
            sMenu = sMenu.andThen(s -> {
                if (!generateImage || IMAGE_DISABLED)
                    s.addField("- Weapon Result -", weaponString, false);
                s.setAuthor(SELECTED_BANNER.getBannerName(), "", new GitHubImage("images/System/Scout_Icon.png").getURL());
                if (weapons.get(0).getRarity() == 2)
                    s.setThumbnail(new GitHubImage("images/System/Brown_Chest.png").getURL());
                else if (weapons.get(0).getRarity() == 3)
                    s.setThumbnail(new GitHubImage("images/System/Blue_Chest.png").getURL());
                else if (weapons.get(0).getRarity() == 4)
                    s.setThumbnail(new GitHubImage("images/System/Red_Chest.png").getURL());
                s.setFooter((username) + " | " + USER.getMemoryDiamonds() + " Memory Diamonds Left", new GitHubImage("images/System/Memory_Diamond_Icon.png").getURL());
            });

            LOGGER.debug("Displaying Scout Result...");
            if (generateImage && !IMAGE_DISABLED)
                Sargo.sendEmbed(TEXT_CHANNEL, sMenu, new File(tempUserDirectory + "/results.png"));
            else
                Sargo.sendEmbed(TEXT_CHANNEL, sMenu);
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
            simpleMessage += (username) + " | " + USER.getMemoryDiamonds() + " Memory Diamonds Left";

            LOGGER.debug("Displaying Scout Result...");
            if (generateImage && !IMAGE_DISABLED)
            {
                Sargo.replyToMessage(TEXT_CHANNEL, simpleMessage, new File(tempUserDirectory + "/results.png"));
            }
            else
            {
                Sargo.replyToMessage(TEXT_CHANNEL, simpleMessage);
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
