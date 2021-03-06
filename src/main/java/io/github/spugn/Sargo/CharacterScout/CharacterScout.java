package io.github.spugn.Sargo.CharacterScout;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.CopperCharacter;
import io.github.spugn.Sargo.Objects.SilverCharacter;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.ScoutMasterParser;
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
 * @version 2.0
 * @since v2.0
 * @see Normal
 * @see StepUp
 * @see RecordCrystal
 */
abstract class CharacterScout
{
    /* PACKAGE-PRIVATE VARIABLES */
    TextChannel TEXT_CHANNEL;
    int BANNER_ID;
    String CHOICE;
    List<Banner> BANNERS;
    UserParser USER;
    double COPPER;
    double GOLD;
    double PLATINUM;
    double PLATINUM6;
    List<Integer> GOLD_BANNERS;
    List<Integer> GOLD_BANNERS_V2;
    List<Integer> PLATINUM_BANNERS;
    List<Integer> PLATINUM_BANNERS_V2;
    boolean IMAGE_DISABLED;
    boolean SIMPLE_MESSAGE;
    Random RNG;
    Banner SELECTED_BANNER;
    int bannerTypeData;
    Consumer<EmbedCreateSpec> sMenu;
    String simpleMessage;
    int rcGet;
    boolean guaranteedScout;
    boolean guaranteeOnePlatinum;
    boolean guaranteeGoldPlus;
    boolean guaranteeOnePlatinum6;
    boolean guaranteedScoutNoGold;
    boolean guaranteeSpecificCharacter;
    int userMemoryDiamonds;
    int userRecordCrystals;
    int singleScoutPrice;
    int multiScoutPrice;
    boolean generateImage;
    boolean stopScout;
    boolean randomizeResults;

    /* PRIVATE VARIABLES */
    private String DISCORD_ID;
    private boolean IS_RARITY_STARS;
    private double SILVER;
    private List<Double> RECORD_CRYSTAL_RATES;
    private List<Double> CIRCULATING_RECORD_CRYSTAL_RATES;
    private List<Character> BANNER_CHARACTERS;
    private int bannerType;
    private List<Character> goldCharacters;
    private List<Character> platinumCharacters;
    private List<Character> platinum6Characters;
    private String imageString;
    private String imageStrings[];
    private int highestRarity;
    private File tempUserDirectory;
    private int userHackingCrystals;
    private String characterString;
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterScout.class);

    /* PROTECTED VARIABLES */
    protected List<Character> characters;

    CharacterScout(Message message, int bannerID, String choice, String discordID)
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
        BANNERS = BannerParser.getBanners();
        USER = new UserParser(DISCORD_ID);

        IS_RARITY_STARS = ScoutSettingsParser.isRarityStars();
        COPPER = (int) (ScoutSettingsParser.getCopperRate() * 100);
        SILVER = (int) (ScoutSettingsParser.getSilverRate() * 100);
        GOLD = (int) (ScoutSettingsParser.getGoldRate() * 100);
        PLATINUM = (int) (ScoutSettingsParser.getPlatinumRate() * 100);
        PLATINUM6 = (int) (ScoutSettingsParser.getPlatinum6Rate() * 100);
        RECORD_CRYSTAL_RATES = ScoutSettingsParser.getRecordCrystalRates();
        CIRCULATING_RECORD_CRYSTAL_RATES = ScoutSettingsParser.getCirculatingRecordCrystalRates();
        GOLD_BANNERS = BannerParser.getGoldBanners();
        GOLD_BANNERS_V2 = BannerParser.getGoldBannersv2();
        PLATINUM_BANNERS = BannerParser.getPlatinumBanners();
        PLATINUM_BANNERS_V2 = BannerParser.getPlatinumBannersv2();
        IMAGE_DISABLED = ScoutSettingsParser.isDisableImages();
        SIMPLE_MESSAGE = ScoutSettingsParser.isSimpleMessage();

        /* USER */
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userHackingCrystals = USER.getHackingCrystals();
        userRecordCrystals = 0;

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
        imageStrings = new String[11];
        characters = new ArrayList<>();
        guaranteeOnePlatinum6 = false;
        guaranteeOnePlatinum = false;
        guaranteeGoldPlus = false;
        guaranteedScout = false;
        guaranteedScoutNoGold = false;
        guaranteeSpecificCharacter = false;
        randomizeResults = false;
        characterString = "";
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        //scoutMenu = new EmbedBuilder();
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
            platinum6Characters = new ArrayList<>();

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
                else if (character.getRarity() == 6)
                {
                    platinum6Characters.add(character);
                }
            }

            // IF THERE ARE NO PLATINUM AND PLATINUM6 CHARACTERS... ADD BOTH %S BACK INTO COPPER
            if (platinumCharacters.size() <= 0 && platinum6Characters.size() <= 0)
            {
                COPPER += PLATINUM6;
                PLATINUM6 = 0;

                COPPER += PLATINUM;
                PLATINUM = 0;
            }
            // IF THERE ARE PLATINUM CHARACTERS BUT NO PLATINUM6, ADD PLATINUM6 % BACK INTO COPPER
            else if (platinumCharacters.size() > 0 && platinum6Characters.size() <= 0)
            {
                COPPER += PLATINUM6;
                PLATINUM6 = 0;
            }

            // PERFORM ANY SCOUT RATE CHANGES THAT ARE REQUIRED FROM THE BANNER TYPE.
            modifyScoutData();

            // DISPLAY FINAL SCOUT PRICES AND RATES ONTO LOGGER.
            LOGGER.debug("\n- Scout Data -\n" +
                    "Single Price " + singleScoutPrice + "\n" +
                    "Multi Price: " + multiScoutPrice + "\n" +
                    "COPPER: " + COPPER + "%\n" +
                    "SILVER: " + SILVER + "%\n" +
                    "GOLD: " + GOLD + "%\n" +
                    "PLATINUM: " + PLATINUM + "%\n" +
                    "PLATINUM6: " + PLATINUM6 + "%");
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
            // REPLACE HIGHEST RARITY IF CHARACTER RARITY IS HIGHER.
            highestRarity = (characters.get(i).getRarity() > highestRarity) ? characters.get(i).getRarity() : highestRarity;
        }

        if (!randomizeResults)
        {
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
        }

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
        // IF BANNER TYPE IS EVENT, RETURN EITHER A GOLD, PLATINUM, OR PLATINUM6 CHARACTER.
        if (bannerType == 9)
        {
            if (goldCharacters.size() > 0)
            {
                return 4;
            }
            else if (platinumCharacters.size() > 0)
            {
                return 5;
            }
            else
            {
                return 6;
            }
        }

        if (guaranteeOnePlatinum6)
        {
            guaranteeOnePlatinum6 = false;
            return 6;
        }

        if (guaranteeOnePlatinum)
        {
            guaranteeOnePlatinum = false;
            return 5;
        }

        if (guaranteeGoldPlus)
        {
            guaranteeGoldPlus = false;
            double x = RNG.nextDouble();
            if (x < 0.03)
            {
                return 5;
            }
            else
            {
                return 4;
            }
        }

        if (guaranteedScout)
        {
            // RECORD CRYSTAL BANNERS V5+
            if (bannerType == 18 ||
                    bannerType == 20)
            {
                return 6;
            }
            // RECORD CRYSTAL BANNERS V2+
            if (bannerType == 5 ||
                    bannerType == 8 ||
                    bannerType == 11)
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
        else if (d < COPPER + SILVER + GOLD + PLATINUM)
        {
            return 5;
        }
        else if (d < COPPER + SILVER + GOLD + PLATINUM + PLATINUM6)
        {
            return 6;
        }
        else
        {
            LOGGER.debug("Unaccounted for scenario has occurred. `d > COPPER + SILVER + GOLD + PLATINUM + PLATINUM6`. Returning `6`.");
            return 6;
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
        else if (rarity == 5)
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
                if (platinumCharacters.size() > 0)
                {
                    character = platinumCharacters.get(RNG.nextInt(platinumCharacters.size()));
                }
                else
                {
                    character = randPlatinumCharacter();
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
                    character = platinum6Characters.get(RNG.nextInt(platinum6Characters.size()));
                }
                else
                {
                    // RECORD CRYSTAL V6 - GOLD CHARACTERS ARE NOT OBTAINABLE
                    if (guaranteedScoutNoGold)
                    {
                        character = randPlatinumCharacter();
                    }
                    else
                    {
                        if (new Random().nextBoolean())
                        {
                            character = randPlatinumCharacter();
                        }
                        else
                        {
                            character = randGoldCharacter();
                        }
                    }
                }
            }
            else
            {
                // STEP UP V9 - STEP 5: FIRST CHARACTER IN BANNER IS GUARANTEED.
                if (bannerType == 21 && guaranteeSpecificCharacter)
                {
                    character = platinum6Characters.get(0);
                    guaranteeSpecificCharacter = false;
                }
                else
                {
                    // STEP UP V9 - FIRST CHARACTER HAS 1/2 OF THE PLATINUM6 RATES, EVERYONE ELSE SHARES THE OTHER HALF.
                    if (bannerType == 21)
                    {
                        double d = RNG.nextDouble();
                        if (d < 0.5)
                        {
                            character = platinum6Characters.get(0);
                        }
                        else
                        {
                            // CHECK IF THERE IS MORE THAN ONE PLATINUM6 CHARACTER
                            if (platinum6Characters.size() > 1)
                            {
                                // GET A CHARACTER FROM INDEX 1 -> WHATEVER AMOUNT OF PLATINUM6 CHARACTERS ARE IN THE BANNER
                                character = platinum6Characters.get(RNG.nextInt(platinum6Characters.size() - 1) + 1);
                            }
                            else
                            {
                                // LESS THAN ONE PLATINUM 6 CHARACTER... JUST DO BUSINESS AS USUAL
                                character = platinum6Characters.get(RNG.nextInt(platinum6Characters.size()));
                            }
                        }
                    }
                    else
                    {
                        // GET ANY PLATINUM6 CHARACTER FROM THE BANNER. (FAIR RATES)
                        character = platinum6Characters.get(RNG.nextInt(platinum6Characters.size()));
                    }

                }
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
        // IF THE BANNER TYPE IS NOT EVENT
        if (bannerType != 9)
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
                case 6:
                    userHackingCrystals += 100;
                    break;
                default:
                    userHackingCrystals += 1;
                    break;
            }
            USER.setHackingCrystals(userHackingCrystals);
        }
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
        Member member = TEXT_CHANNEL.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(DISCORD_ID))).block();
        String username = member.getUsername() + "#" + member.getDiscriminator();
        if (!SIMPLE_MESSAGE)
        {
            sMenu = s -> s.setColor(new Color(244, 233, 167));

            setupScoutMenu();
            sMenu = sMenu.andThen(s -> {
                if (!generateImage || IMAGE_DISABLED)
                    s.addField("- Scout Result -", characterString, false);
                s.setDescription(smp.getQuote());
                s.setAuthor(SELECTED_BANNER.getBannerName(), "", new GitHubImage("images/System/Scout_Icon.png").getURL());

                // IF SELECTED BANNER IS OLDER THAN STEP UP V7 AND NOT EVENT...
                // DISPLAY SAO ARGO ONLY, OTHERWISE USE BOTH SAO AND ALO ARGOS
                if (SELECTED_BANNER.getBannerType() < 17 && SELECTED_BANNER.getBannerType() != 9)
                {
                    s.setThumbnail(smp.getImage(highestRarity, true));
                }
                else
                {
                    s.setThumbnail(smp.getImage(highestRarity));
                }
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
            simpleMessage += smp.getQuote() + "\n";
            if (!generateImage || IMAGE_DISABLED)
            {
                simpleMessage += "**- Scout Result -**" + "\n";
                simpleMessage += characterString;
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

    void print_NotEnoughMemoryDiamonds_Single_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "NOT ENOUGH MEMORY DIAMONDS",
                "You need **" + singleScoutPrice + "** Memory Diamonds to scout.\nUse `" + CommandManager.getCommandPrefix() + "shop` to get more Memory Diamonds (**it's free!**).",
                SELECTED_BANNER.getBannerName());
    }

    void print_NotEnoughMemoryDiamonds_Multi_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "NOT ENOUGH MEMORY DIAMONDS",
                "You need **" + multiScoutPrice + "** Memory Diamonds to scout.\nUse `" + CommandManager.getCommandPrefix() + "shop` to get more Memory Diamonds (**it's free!**).",
                SELECTED_BANNER.getBannerName());
    }

    void print_NotEnoughRecordCrystals_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "INSUFFICIENT RECORD CRYSTALS",
                "You need `10` record crystals to do a record crystal scout.\n\n" +
                        "You currently have `" + (userRecordCrystals >= 0 ? userRecordCrystals : "0") + "` `" + SELECTED_BANNER.getBannerName() + "` Record Crystals.",
                SELECTED_BANNER.getBannerName());
    }

    void print_UnknownScoutType_s_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "UNKNOWN/UNAVAILABLE SCOUT TYPE",
                "Only `single` scouts are available.\n\n" +
                        "**SINGLE SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " s`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " si`",
                SELECTED_BANNER.getBannerName());
    }

    void print_UnknownScoutType_sm_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "UNKNOWN/UNAVAILABLE SCOUT TYPE",
                "Only `single` and `multi` scouts are available.\n\n" +
                        "**SINGLE SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " s`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " si`\n\n" +

                        "**MULTI SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " m`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " mi`",
                SELECTED_BANNER.getBannerName());
    }

    void print_UnknownScoutType_smrc_Message()
    {
        Sargo.replyToMessage_Warning(TEXT_CHANNEL, "UNKNOWN/UNAVAILABLE SCOUT TYPE",
                "Only `single`, `multi`, and `record crystal` scouts are available.\n\n" +
                        "**SINGLE SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " s`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " si`\n\n" +

                        "**MULTI SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " m`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " mi`\n\n" +

                        "**RECORD CRYSTAL SCOUTS**\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " rc`\n" +
                        "`" + CommandManager.getCommandPrefix() + "scout " + (BANNER_ID + 1) + " rci`",
                SELECTED_BANNER.getBannerName());
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
     * Gives a random platinum rarity character if there are no
     * available platinum characters in the banner.
     *
     * @return  The {@link Character} given to the user.
     */
    protected abstract Character randPlatinumCharacter();

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
