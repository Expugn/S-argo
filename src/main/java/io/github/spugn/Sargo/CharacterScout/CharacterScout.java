package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.ScoutMasterParser;
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
 * CHARACTER SCOUT
 * <p>
 *     This class contains all the data and functions needed to perform a
 *     character scout sans all the scout type exclusive stuff like rate
 *     changes or price changes. Scout type exclusive stuff can be found
 *     in their appropriate classes (Example: {@link Normal}, {@link StepUp},
 *     or {@link RecordCrystal}).
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see Normal
 * @see StepUp
 * @see RecordCrystal
 */
abstract class CharacterScout
{
    /* PACKAGE-PRIVATE VARIABLES */
    IChannel CHANNEL;
    int BANNER_ID;
    String CHOICE;
    List<Banner> BANNERS;
    UserParser USER;
    double COPPER;
    double GOLD;
    double PLATINUM;
    List<Integer> GOLD_BANNERS;
    List<Integer> GOLD_BANNERS_V2;
    boolean IMAGE_DISABLED;
    boolean SIMPLE_MESSAGE;
    Random RNG;
    Banner SELECTED_BANNER;
    int bannerTypeData;
    EmbedBuilder scoutMenu;
    String simpleMessage;
    int rcGet;
    boolean guaranteedScout;
    boolean guaranteeOnePlatinum;
    int userMemoryDiamonds;
    int userRecordCrystals;
    int singleScoutPrice;
    int multiScoutPrice;
    boolean generateImage;
    boolean stopScout;

    /* PRIVATE VARIABLES */
    private String DISCORD_ID;
    private SettingsParser SETTINGS;
    private boolean IS_RARITY_STARS;
    private double SILVER;
    private List<Double> RECORD_CRYSTAL_RATES;
    private List<Double> CIRCULATING_RECORD_CRYSTAL_RATES;
    private List<Character> BANNER_CHARACTERS;
    private int bannerType;
    private List<Character> goldCharacters;
    private List<Character> platinumCharacters;
    private String imageString;
    private String imageStrings[];
    private int highestRarity;
    private File tempUserDirectory;
    private int userHackingCrystals;
    private String characterString;
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterScout.class);

    /* PROTECTED VARIABLES */
    protected List<Character> characters;

    CharacterScout(IChannel channel, int bannerID, String choice, String discordID)
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
        SETTINGS = new SettingsParser();
        BANNERS = new BannerParser().readConfig("data/Banners.xml");
        USER = new UserParser(DISCORD_ID);

        /* SETTINGS */
        IS_RARITY_STARS = SETTINGS.isRarityStars();
        COPPER = (int) (SETTINGS.getCopperRates() * 100);
        SILVER = (int) (SETTINGS.getSilverRates() * 100);
        GOLD = (int) (SETTINGS.getGoldRates() * 100);
        PLATINUM = (int) (SETTINGS.getPlatinumRates() * 100);
        RECORD_CRYSTAL_RATES = SETTINGS.getRecordCrystalRates();
        CIRCULATING_RECORD_CRYSTAL_RATES = SETTINGS.getCirculatingRecordCrystalRates();
        GOLD_BANNERS = SETTINGS.getGoldBanners();
        GOLD_BANNERS_V2 = SETTINGS.getGoldBannersv2();
        IMAGE_DISABLED = SETTINGS.isDisableImages();
        SIMPLE_MESSAGE = SETTINGS.isSimpleMessage();

        /* USER */
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userHackingCrystals = USER.getHackingCrystals();
        userRecordCrystals = 0;

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
        imageStrings = new String[11];
        characters = new ArrayList<>();
        guaranteeOnePlatinum = false;
        guaranteedScout = false;
        characterString = "";
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        scoutMenu = new EmbedBuilder();
        simpleMessage = "";

        /* MEMORY DIAMOND PRICES */
        singleScoutPrice = 25;
        multiScoutPrice = 250;

        /* BANNER */
        if (BANNER_ID < BANNERS.size() && BANNER_ID >= 0)
        {
            SELECTED_BANNER = BANNERS.get(BANNER_ID);
            BANNER_CHARACTERS = SELECTED_BANNER.getCharacters();
            bannerType = SELECTED_BANNER.getBannerType();
            bannerTypeData = USER.getBannerData(SELECTED_BANNER.getBannerName());
            goldCharacters = new ArrayList<>();
            platinumCharacters = new ArrayList<>();

            if (USER.isBannerInfoExists(SELECTED_BANNER.getBannerName()) == -1)
            {
                initBannerInfo();
            }

            for (Character character : BANNER_CHARACTERS)
            {
                if (character.getRarity() == 4)
                {
                    goldCharacters.add(character);
                }
                else if (character.getRarity() == 5)
                {
                    platinumCharacters.add(character);
                }
            }

            if (platinumCharacters.size() <= 0)
            {
                COPPER += PLATINUM;
                PLATINUM = 0;
            }

            modifyScoutData();
            LOGGER.debug("\n- Scout Data -\n" +
                    "Single Price " + singleScoutPrice + "\n" +
                    "Multi Price: " + multiScoutPrice + "\n" +
                    "COPPER: " + COPPER + "%\n" +
                    "SILVER: " + SILVER + "%\n" +
                    "GOLD: " + GOLD + "%\n" +
                    "PLATINUM: " + PLATINUM + "%");
        }
    }

    /**
     * Generates one character and the scout result image.
     */
    void doSinglePull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        characters.add(getCharacter(scout()));
        highestRarity = characters.get(0).getRarity();
        generateImageString();
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawSingleScout(imageString, true, tempUserDirectory + "/results.png");
    }

    /**
     * Generates eleven characters and the scout result image.
     */
    void doMultiPull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        for (int i = 0 ; i < 11 ; i++)
        {
            characters.add(getCharacter(scout()));
        }
        Character tempCharacter;
        for (int a = 0 ; a < 12 ; a++)
        {
            for (int b = 1 ; b < 11 ; b++)
            {
                if (characters.get(b-1).getRarity() <= characters.get(b).getRarity())
                {
                    tempCharacter = characters.get(b-1);
                    characters.set(b-1, characters.get(b));
                    characters.set(b, tempCharacter);
                }
            }
        }
        highestRarity = characters.get(0).getRarity();

        generateImageStrings();
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawMultiScout(imageStrings, true, tempUserDirectory + "/results.png");
    }

    /**
     * Checks the user's character box for duplicate characters,
     * grant Hacking Crystals, and generates the character images.
     */
    private void generateImageString()
    {
        if (!USER.getCharacterBox().isEmpty())
        {
            for (Character userCharacter : USER.getCharacterBox())
            {
                if (userCharacter.getPrefix().equals(characters.get(0).getPrefix())
                        && userCharacter.getRarity() == characters.get(0).getRarity()
                        && userCharacter.getName().equals(characters.get(0).getName()))
                {
                    giveHackingCrystals(characters.get(0));
                    characterString += "~~" + characters.get(0).toString() + "~~" + "\n";
                    if (generateImage && !IMAGE_DISABLED)
                        new ImageEditor().drawCharacterImage(characters.get(0).getImagePath(), characters.get(0).getRarity(), true, IS_RARITY_STARS,tempUserDirectory + "/temp_" + 0 + ".png");
                    imageString = tempUserDirectory + "/temp_" + 0 + ".png";
                    return;
                }
            }
        }
        USER.addCharacter(characters.get(0));
        characterString += "**" + characters.get(0).toString() + "**\n";
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawCharacterImage(characters.get(0).getImagePath(), characters.get(0).getRarity(), false, IS_RARITY_STARS, tempUserDirectory + "/temp_" + 0 + ".png");
        imageString = tempUserDirectory + "/temp_" + 0 + ".png";
    }

    /**
     * Same function as {@link #generateImageString()} but for eleven characters.
     * @see #generateImageString()
     */
    private void generateImageStrings()
    {
        boolean foundDuplicate = false;

        for (int i = 0 ; i < 11 ; i++)
        {
            if (!USER.getCharacterBox().isEmpty())
            {
                for (Character userCharacter : USER.getCharacterBox())
                {
                    if (userCharacter.getPrefix().equals(characters.get(i).getPrefix())
                            && userCharacter.getRarity() == characters.get(i).getRarity()
                            && userCharacter.getName().equals(characters.get(i).getName()))
                    {
                        foundDuplicate = true;
                        giveHackingCrystals(characters.get(i));

                        characterString += "~~" + characters.get(i).toString() + "~~" + "\n";
                        if (generateImage && !IMAGE_DISABLED)
                            new ImageEditor().drawCharacterImage(characters.get(i).getImagePath(), characters.get(i).getRarity(), true, IS_RARITY_STARS, tempUserDirectory + "/temp_" + i + ".png");
                    }
                }

                if (!foundDuplicate)
                {
                    USER.addCharacter(characters.get(i));
                    characterString += "**" + characters.get(i).toString() + "**\n";
                    if (generateImage && !IMAGE_DISABLED)
                        new ImageEditor().drawCharacterImage(characters.get(i).getImagePath(), characters.get(i).getRarity(), false, IS_RARITY_STARS, tempUserDirectory + "/temp_" + i + ".png");
                }
                foundDuplicate = false;
            }
            else
            {
                USER.addCharacter(characters.get(i));
                characterString += "**" + characters.get(i).toString() + "**\n";
                if (generateImage && !IMAGE_DISABLED)
                    new ImageEditor().drawCharacterImage(characters.get(i).getImagePath(), characters.get(i).getRarity(), false, IS_RARITY_STARS, tempUserDirectory + "/temp_" + i + ".png");
            }
            imageStrings[i] = tempUserDirectory + "/temp_" + i + ".png";
        }
    }

    /**
     * Determines a new character's rarity using RNG.
     * @return  The rarity value of a new character.
     */
    private int scout()
    {
        if (guaranteeOnePlatinum)
        {
            guaranteeOnePlatinum = false;
            return 5;
        }

        if (guaranteedScout)
        {
            if (bannerType == 5 ||
                    bannerType == 8)
            {
                return 5;
            }
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
            return 5;
        }
    }

    /**
     * Determines which character is added to the user's collection
     * depending on the rarity value given.
     *
     * @param rarity  The rarity value of the character.
     * @return  A new {@link Character} from the banner or from
     *          the collection of copper or silver characters.
     */
    private Character getCharacter(int rarity)
    {
        Character character;
        if (rarity == 2)
        {
            CopperCharacter cC = new CopperCharacter();
            character = cC.getCharacter(RNG.nextInt(cC.getSize()));
        }
        else if (rarity == 3)
        {
            SilverCharacter sC = new SilverCharacter();
            character = sC.getCharacter(RNG.nextInt(sC.getSize()));
        }
        else if (rarity == 4)
        {
            if (guaranteedScout)
            {
                double d = RNG.nextDouble();
                if (d < 0.6)
                {
                    character = goldCharacters.get(RNG.nextInt(goldCharacters.size()));
                }
                else
                {
                    character = randGoldCharacter();
                }
            }
            else
            {
                if (goldCharacters.size() > 0)
                {
                    character = goldCharacters.get(RNG.nextInt(goldCharacters.size()));
                }
                else
                {
                    character = randGoldCharacter();
                }
            }
        }
        else
        {
            if (guaranteedScout)
            {
                double d = RNG.nextDouble();
                if (d < 0.6)
                {
                    character = platinumCharacters.get(RNG.nextInt(platinumCharacters.size()));
                }
                else
                {
                    character = randGoldCharacter();
                }
            }
            else
            {
                character = platinumCharacters.get(RNG.nextInt(platinumCharacters.size()));
            }


        }
        return character;
    }

    /**
     * Gives Hacking Crystals to the user based on the character rarity value
     * given.
     *
     * @param c  Duplicate {@link Character} to be converted into Hacking Crystals.
     */
    private void giveHackingCrystals(Character c)
    {
        switch(c.getRarity())
        {
            case 2:
                userHackingCrystals += 1;
                break;
            case 3:
                userHackingCrystals += 2;
                break;
            case 4:
                userHackingCrystals += 50;
                break;
            case 5:
                userHackingCrystals += 100;
                break;
            default:
                userHackingCrystals += 1;
                break;
        }
        USER.setHackingCrystals(userHackingCrystals);
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
     * Used in Record Crystal scouts; determine how many record crystals
     * should be given to the user.
     *
     * @return  The amount of Record Crystals that are given to the user.
     */
    int getRecordCrystals()
    {
        double d = RNG.nextDouble();
        double recordCrystalRate = RECORD_CRYSTAL_RATES.get(0);

        for (int i = 0 ; i < RECORD_CRYSTAL_RATES.size() ; i++)
        {
            recordCrystalRate += RECORD_CRYSTAL_RATES.get(i);
            if (d < recordCrystalRate)
            {
                return i;
            }
        }

        return 0;
    }

    /**
     * Used in Record Crystal v3 scouts; determine how many record crystals (1-5)
     * should be returned to the user.
     *
     * @return  The amount of Record Crystals (1-5) that are returned to the user.
     */
    int getCirculatedRecordCrystals()
    {
        double d = RNG.nextDouble();
        double circulatingRecordCrystalRate = CIRCULATING_RECORD_CRYSTAL_RATES.get(0);

        for (int i = 0 ; i < CIRCULATING_RECORD_CRYSTAL_RATES.size() ; i++)
        {
            circulatingRecordCrystalRate += CIRCULATING_RECORD_CRYSTAL_RATES.get(i);
            if (d < circulatingRecordCrystalRate)
            {
                return i;
            }
        }

        return 0;
    }

    /**
     * Displays the scout results to the channel where the command was
     * requested and save the results to the user file afterwards. If
     * the results fail to display for any reason then the user file
     * is not saved.
     */
    void displayAndSave()
    {
        ScoutMasterParser smp = new ScoutMasterParser();
        if (!SIMPLE_MESSAGE)
        {
            scoutMenu.withAuthorName(SELECTED_BANNER.getBannerName());
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
                scoutMenu.appendField("- Scout Result -", characterString, false);
            scoutMenu.withDesc(smp.getQuote());
            scoutMenu.withAuthorIcon(new GitHubImage("images/System/Scout_Icon.png").getURL());
            scoutMenu.withColor(244, 233, 167);
            scoutMenu.withThumbnail(smp.getImage(highestRarity));
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
            simpleMessage += smp.getQuote() + "\n";
            if (!generateImage || IMAGE_DISABLED)
            {
                simpleMessage += "**- Scout Result -**" + "\n";
                simpleMessage += characterString;
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
     * Determines what the {@link #bannerTypeData} should be when there is
     * no data for the banner in the user's file.
     *
     * @see StepUp#initBannerInfo()
     * @see RecordCrystal#initBannerInfo()
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
     * Advances to the next step in a Step Up scout or grants
     * Record Crystals to the user in a Record Crystal scout.
     *
     * @see StepUp#updateBannerData()
     * @see RecordCrystal#updateBannerData()
     */
    protected abstract void updateBannerData();

    /**
     * Gives a random gold rarity character if there are no
     * available gold characters in the banner or if the user
     * failed to hit the 60% for a banner gold character in
     * a Record Crystal scout.
     *
     * @return  The {@link Character} given to the user.
     * @see StepUp#randGoldCharacter()
     * @see RecordCrystal#randGoldCharacter()
     */
    protected abstract Character randGoldCharacter();

    /**
     * Sets up the title portion of the scout result UI.
     * The title has information such as the scout type,
     * current Step, Record Crystals gained, etc.
     *
     * @see Normal#setupScoutMenu()
     * @see StepUp#setupScoutMenu()
     * @see RecordCrystal#setupScoutMenu()
     */
    protected abstract void setupScoutMenu();

    /**
     * Determines how the flow of scouts will go. Check
     * if the desired scout type is valid, if the user
     * has enough Memory Diamonds, etc in the process.
     *
     * @see Normal#run()
     * @see StepUp#run()
     * @see RecordCrystal#run()
     */
    protected abstract void run();
}
