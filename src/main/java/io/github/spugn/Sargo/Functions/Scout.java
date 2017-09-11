package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import io.github.spugn.sdevkit.Discord.Discord4J.Message;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RateLimitException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;

public class Scout
{
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;
    private static int BANNER_ID;
    private static String CHOICE;
    private static List<Banner> BANNERS;
    private static String DISCORD_ID;

    private static Banner SELECTED_BANNER;
    private static List<Character> BANNER_CHARACTERS;
    private static Random RNG;

    private static UserParser USER;

    private List<Integer> goldBanners;

    /* RATES */
    private double copper;
    private double silver;
    private double gold;
    private double platinum;

    private int bannerType;

    /* bannerTypeData: INFORMATION ON WHAT STEP THE USER IS ON / HOW MANY RECORD CRYSTALS THEY HAVE */
    private int bannerTypeData;

    /* PLACEHOLDERS */
    private static File PLATINUM_PH;
    private static File GOLD_PH;
    private static File SILVER_PH;
    private static File COPPER_PH;

    /* SCOUTED DATA */
    private String imageString;
    private String imageStrings[];
    private String argoText;
    private String argoFace;
    private ScoutMenu scoutMenu;
    private int highestRarity;
    private List<Character> characters;
    private List<Character> goldCharacters;
    private List<Character> platinumCharacters;
    private List<Double> recordCrystalRates;
    private int rcGet;
    private boolean guaranteeOnePlatinum;
    private boolean guaranteedScout;
    private File tempUserDirectory;

    private int userMemoryDiamonds;
    private int singleScoutPrice;
    private int multiScoutPrice;

    public Scout(IDiscordClient client, IChannel channel, int bannerID, String choice, String discordID)
    {
        CLIENT = client;
        CHANNEL = channel;
        BANNER_ID = bannerID - 1;
        CHOICE = choice;
        RNG = new Random(System.currentTimeMillis());
        DISCORD_ID = discordID;

        init();
        run();
    }

    private void init()
    {
        /* OPEN SETTINGS FILE */
        SettingsParser settings = new SettingsParser();

        /* GET RATES */
        copper = (int) (settings.getTwoRates() * 100);
        silver = (int) (settings.getThreeRates() * 100);
        gold = (int) (settings.getFourRates() * 100);
        platinum = (int) (settings.getFiveRates() * 100);

        /* GET GOLD BANNERS*/
        goldBanners = settings.getGoldBanners();

        /* GET RECORD CRYSTAL RATES */
        recordCrystalRates = settings.getRecordCrystalRates();

        /* OPEN BANNERS FILE */
        BannerParser bannersXML = new BannerParser();
        BANNERS = bannersXML.readConfig(Files.BANNER_XML.get());

        /* OPEN USER DATA FILE */
        USER = new UserParser(DISCORD_ID);

        PLATINUM_PH = new File(Files.PLATINUM_PLACEHOLDER.get());
        GOLD_PH = new File(Files.GOLD_PLACEHOLDER.get());
        SILVER_PH = new File(Files.SILVER_PLACEHOLDER.get());
        COPPER_PH = new File(Files.COPPER_PLACEHOLDER.get());

        imageStrings = new String[11];
        characters = new ArrayList<>();
        scoutMenu = new ScoutMenu();
        goldCharacters = new ArrayList<>();
        platinumCharacters = new ArrayList<>();

        guaranteeOnePlatinum = false;
        guaranteedScout = false;

        userMemoryDiamonds = USER.getMemoryDiamonds();
        singleScoutPrice = 25;
        multiScoutPrice = 250;
    }

    private void run()
    {
        /* CHECK IF REQUESTED BANNER IS AVAILABLE */
        if (BANNER_ID < BANNERS.size() && BANNER_ID >= 0)
        {
            /* MAKE TEMPORARY IMAGE FOLDER */
            tempUserDirectory = new File("images/temp_" + DISCORD_ID);
            tempUserDirectory.mkdir();

            /* GET BANNER AND CHARACTER DATA. ALSO ADJUST RATES IF NEEDED */
            if (CHOICE.equalsIgnoreCase("g") || CHOICE.equalsIgnoreCase("guaranteed") || CHOICE.equalsIgnoreCase("rc"))
            {
                guaranteedScout = true;
            }
            setupBanner();

            /* FIXME - MESSAGE THAT READS SCOUT RATES */
            //CHANNEL.sendMessage(new WarningMessage("RATES", "2 - " + copper + "\n3 - " + silver + "\n4 - " + gold + "\n5 - " + platinum).get().build());

            if (CHOICE.equalsIgnoreCase("single") || CHOICE.equalsIgnoreCase("s") || CHOICE.equalsIgnoreCase("1"))
            {
                if (userMemoryDiamonds >= singleScoutPrice)
                {
                    /* REMOVE MEMORY DIAMONDS FROM USER ACCOUNT */
                    userMemoryDiamonds -= singleScoutPrice;
                    USER.setMemoryDiamonds(userMemoryDiamonds);

                    /* PULL AND DRAW SCOUT RESULT IMAGE */
                    singlePull();
                    drawImage(imageString);
                }
                else
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.").get().build());
                    deleteTempDirectory();
                    return;
                }
            }
            else if (CHOICE.equalsIgnoreCase("multi") || CHOICE.equalsIgnoreCase("m") || CHOICE.equalsIgnoreCase("11"))
            {
                if (userMemoryDiamonds >= multiScoutPrice)
                {
                    /* REMOVE MEMORY DIAMONDS FROM USER ACCOUNT */
                    userMemoryDiamonds -= multiScoutPrice;
                    USER.setMemoryDiamonds(userMemoryDiamonds);

                    /* PULL AND DRAW SCOUT RESULT IMAGE */
                    multiPull();
                    drawImage(imageStrings);

                    /* INCREMENT STEP IF DOING STEP UP*/
                    if (bannerType == 1 || bannerType == 3)
                    {
                        int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName());

                        /* STEP UP V1 - INCREMENT STEP, RESET IF NEXT STEP IS GREATER THAN 5 */
                        if (bannerType == 1)
                        {
                            currentStep++;
                            if (currentStep > 5)
                            {
                                USER.changeValue(SELECTED_BANNER.getBannerName(), 1);
                            }
                            else
                            {
                                USER.changeValue(SELECTED_BANNER.getBannerName(), currentStep);
                            }
                        }

                        /* STEP UP V3 - INCREMENT STEP, KEEP STEP AT 6 IF GREATER THAN 6 */
                        if (bannerType == 3)
                        {
                            currentStep++;
                            if (currentStep > 6)
                            {
                                USER.changeValue(SELECTED_BANNER.getBannerName(), 6);
                            }
                            else
                            {
                                USER.changeValue(SELECTED_BANNER.getBannerName(), currentStep);
                            }
                        }
                    }
                }
                else
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.").get().build());
                    deleteTempDirectory();
                    return;
                }
            }
            else if (CHOICE.equalsIgnoreCase("g") || CHOICE.equalsIgnoreCase("guaranteed") || CHOICE.equalsIgnoreCase("rc"))
            {
                if (bannerType == 2)
                {
                    int userRC = USER.getBannerData(SELECTED_BANNER.getBannerName());

                    if (userRC >= 10)
                    {
                        /* SUBTRACT 10 RECORD CRYSTAL FROM USER */
                        userRC -= 10;
                        USER.changeValue(SELECTED_BANNER.getBannerName(), userRC);

                        /* SCOUT */
                        singlePull();
                        drawImage(imageString);
                        buildScoutMenu();
                        scoutMenu.setGuaranteedScout(true);
                        scoutMenu.setTypeData(String.valueOf(userRC));

                        displayResults();

                        /* SAVE USER DATA */
                        USER.saveData();

                        deleteTempDirectory();
                        return;
                    }
                    else
                    {
                        CHANNEL.sendMessage(new WarningMessage("INSUFFICIENT RECORD CRYSTALS", "You need 10 record crystals to do a guaranteed scout.").get().build());
                        deleteTempDirectory();
                        return;
                    }
                }
                else
                {
                    CHANNEL.sendMessage(new WarningMessage("NO GUARANTEED SCOUT", "This banner does not have a guaranteed scout.").get().build());
                    deleteTempDirectory();
                    return;
                }
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN SCOUT TYPE", "Use 'scout [Banner ID] [s|m|g]'.").get().build());
                deleteTempDirectory();
                return;
            }

            /* BUILD SCOUT MENU, AND DISPLAY RESULTS */
            buildScoutMenu();
            displayResults();

            /* SAVE USER DATA */
            USER.saveData();

            deleteTempDirectory();
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
        }
    }

    private void deleteTempDirectory()
    {
        String[] entries = tempUserDirectory.list();
        for (String s : entries)
        {
            File currentFile = new File(tempUserDirectory.getPath(), s);
            currentFile.delete();
        }
        tempUserDirectory.delete();
    }

    private void singlePull()
    {
        /* GET CHARACTER */
        characters.add(getCharacter(scout()));

        /* SAVE IMAGE STRING AND RARITY OF CHARACTER */
        generateImageString();
        highestRarity = Integer.parseInt(characters.get(0).getRarity());
    }

    private void multiPull()
    {
        /* GENERATE CHARACTERS */
        for (int i = 0 ; i < 11 ; i++)
        {
            characters.add(getCharacter(scout()));
        }

        /* BUBBLE SORT CHARACTER ARRAY */
        Character tempCharacter;

        for (int a = 0 ; a < 12 ; a++)
        {
            for (int b = 1 ; b < 11 ; b++)
            {
                if (Integer.parseInt(characters.get(b-1).getRarity()) <= Integer.parseInt(characters.get(b).getRarity()))
                {
                    tempCharacter = characters.get(b-1);
                    characters.set(b-1, characters.get(b));
                    characters.set(b, tempCharacter);
                }
            }
        }

        /* GENERATE IMAGE STRINGS */
        generateImageStrings();

        /* SAVE HIGHEST RARITY CHARACTER */
        highestRarity = Integer.parseInt(characters.get(0).getRarity());
    }

    private void generateImageString()
    {
        /* CHARACTER BOX IS NOT EMPTY */
        if (!USER.getCharacterBox().isEmpty())
        {
            /* ITERATE THROUGH CHARACTERS, CHECK IF DUPLICATE EXISTS */
            for (Character userCharacter : USER.getCharacterBox())
            {
                /* CHARACTER IS A DUPLICATE */
                if (userCharacter.getPrefix().equals(characters.get(0).getPrefix()))
                {
                    giveHackingCrystals(characters.get(0));
                    try
                    {
                        /* GET BACKGROUND AND CREATE NEW IMAGE */
                        Image characterImage = ImageIO.read(new File(characters.get(0).getImagePath()));
                        BufferedImage result = new BufferedImage(characterImage.getWidth(null), characterImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

                        /* DRAW BACKGROUND ON NEW IMAGE */
                        Graphics g = result.getGraphics();
                        g.drawImage(characterImage, 0, 0, null);

                        /* READ IMAGE STRING */
                        BufferedImage bi = ImageIO.read(new File("images/Miscellaneous/Owned_Character_Shade.png"));

                        /* DRAW SHADE OVER CHARACTER IMAGE */
                        g.drawImage(bi, 0, 0, null);

                        /* WRITE TO TEMPORARY DIRECTORY AND SAVE FILE PATH */
                        ImageIO.write(result, "png", new File(tempUserDirectory + "/temp_" + 0 + ".png"));
                        imageString = tempUserDirectory + "/temp_" + 0 + ".png";
                        return;
                    }
                    catch (IOException e)
                    {
                        CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
                    }
                }
            }
            /* NO DUPLICATES FOUND */
            USER.addCharacter(characters.get(0));
            imageString = characters.get(0).getImagePath();
        }
        else
        {
            /* CHARACTER BOX IS EMPTY, NO DUPLICATES EXIST. */
            USER.addCharacter(characters.get(0));
            imageString = characters.get(0).getImagePath();
        }

    }

    private void generateImageStrings()
    {
        boolean foundDuplicate = false;

        /* GO THROUGH SCOUTED CHARACTERS */
        for (int i = 0 ; i < 11 ; i++)
        {
            /* CHARACTER BOX IS NOT EMPTY */
            if (!USER.getCharacterBox().isEmpty())
            {
                /* ITERATE THROUGH CHARACTERS, CHECK IF DUPLICATE EXISTS */
                for (Character userCharacter : USER.getCharacterBox())
                {
                    /* CHARACTER IS A DUPLICATE */
                    if (userCharacter.getPrefix().equals(characters.get(i).getPrefix()) && userCharacter.getName().equals(characters.get(i).getName()))
                    {
                        foundDuplicate = true;
                        giveHackingCrystals(characters.get(i));
                        try
                        {
                            /* GET BACKGROUND AND CREATE NEW IMAGE */
                            Image scout_background = ImageIO.read(new File(characters.get(i).getImagePath()));
                            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);

                            /* DRAW BACKGROUND ON NEW IMAGE */
                            Graphics g = result.getGraphics();
                            g.drawImage(scout_background, 0, 0, null);

                            /* READ IMAGE STRING */
                            BufferedImage bi = ImageIO.read(new File("images/Miscellaneous/Owned_Character_Shade.png"));
                            g.drawImage(bi, 0, 0, null);

                            /* WRITE TO TEMPORARY DIRECTORY AND SAVE FILE PATH */
                            ImageIO.write(result, "png", new File(tempUserDirectory + "/temp_" + i + ".png"));
                            imageStrings[i] = tempUserDirectory + "/temp_" + i + ".png";
                        }
                        catch (IOException e)
                        {
                            CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
                        }
                    }
                }
                /* DUPLICATE CHARACTER NOT FOUND */
                if (!foundDuplicate)
                {
                    USER.addCharacter(characters.get(i));
                    imageStrings[i] = characters.get(i).getImagePath();
                }
                foundDuplicate = false;
            }
            else
            {
                /* CHARACTER BOX IS EMPTY, NO DUPLICATES EXIST. */
                USER.addCharacter(characters.get(i));
                imageStrings[i] = characters.get(i).getImagePath();
            }
        }
    }

    private void giveHackingCrystals(Character c)
    {
        /* GET USER HACKING CRYSTALS */
        int userHC = USER.getHackingCrystals();

        /* GRANT HACKING CRYSTALS BASED ON CHARACTER RARITY */
        switch(Integer.parseInt(c.getRarity()))
        {
            case 2:
                userHC += 1;
                break;
            case 3:
                userHC += 2;
                break;
            case 4:
                userHC += 50;
                break;
            case 5:
                userHC += 100;
                break;
            default:
                userHC += 1;
                break;
        }

        /* SAVE HACKING CRYSTAL COUNT */
        USER.setHackingCrystals(userHC);
    }

    private Character getCharacter(int rarity)
    {
        Character character;

        /* GET CHARACTER DETERMINED BY RARITY */
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
                /* 60% CHANCE TO GET A BANNER GOLD CHARACTER FROM GUARANTEED */
                if (d < 0.6)
                {
                    character = goldCharacters.get(RNG.nextInt(goldCharacters.size()));
                }
                /* 40% CHANCE TO GET A OFF-BANNER GOLD CHARACTER */
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
            character = platinumCharacters.get(RNG.nextInt(platinumCharacters.size()));
        }

        /* CHECK IF CHARACTER IMAGE FILE EXISTS */
        /* IF NOT, REPLACE WITH PLACEHOLDER IMAGE */
        File characterImage = new File(character.getImagePath());

        if (!characterImage.exists())
        {
            switch (Integer.parseInt(character.getRarity()))
            {
                case 2:
                    character.setImagePath(COPPER_PH.toString());
                    break;
                case 3:
                    character.setImagePath(SILVER_PH.toString());
                    break;
                case 4:
                    character.setImagePath(GOLD_PH.toString());
                    break;
                case 5:
                    character.setImagePath(PLATINUM_PH.toString());
                    break;
                default:
                    character.setImagePath(COPPER_PH.toString());
                    break;
            }
        }

        return character;
    }

    private void argo()
    {
        double aText = RNG.nextDouble();
        double aFace = RNG.nextDouble();

        /* DETERMINE THE TEXT THAT ARGO WILL SAY */
        if (aText < 0.25)
        {
            argoText = Text.ARGO_1.get();
        }
        else if (aText < 0.5)
        {
            argoText = Text.ARGO_2.get();
        }
        else if (aText < 0.75)
        {
            argoText = Text.ARGO_3.get();
        }
        else
        {
            argoText = Text.ARGO_4.get();
        }

        /* DETERMINE THE TYPE OF FACE ARGO WILL HAVE */
        switch (highestRarity)
        {
            case 2:
                /* 75% FOR SMILE, 25% FOR GRIN */
                if (aFace < 0.75)
                {
                    argoFace = Images.ARGO_SMILE.getUrl();
                }
                else
                {
                    argoFace = Images.ARGO_GRIN.getUrl();
                }
                break;

            case 3:
                /* 50% FOR SMILE, 50% FOR GRIN*/
                if (aFace < 0.5)
                {
                    argoFace = Images.ARGO_SMILE.getUrl();
                }
                else
                {
                    argoFace = Images.ARGO_GRIN.getUrl();
                }
                break;

            case 4:
                /* 10% FOR SMILE, 25% FOR GRIN, 65% FOR SMUG */
                if (aFace < 0.1)
                {
                    argoFace = Images.ARGO_SMILE.getUrl();
                }
                else if (aFace < 0.35)
                {
                    argoFace = Images.ARGO_GRIN.getUrl();
                }
                else
                {
                    argoFace = Images.ARGO_SMUG.getUrl();
                }
                break;

            case 5:
                /* 10% FOR SMILE, 10% FOR GRIN, 20% FOR SMUG, 60% FOR FLOWERS */
                if (aFace < 0.1)
                {
                    argoFace = Images.ARGO_SMILE.getUrl();
                }
                else if (aFace < 0.2)
                {
                    argoFace = Images.ARGO_GRIN.getUrl();
                }
                else if (aFace < 0.4)
                {
                    argoFace = Images.ARGO_SMUG.getUrl();
                }
                else
                {
                    argoFace = Images.ARGO_FLOWERS.getUrl();
                }
                break;

            default:
                argoFace = Images.ARGO_SMILE.getUrl();
                break;
        }
    }

    public void drawImage(String imageString)
    {
        try
        {
            /* STARTING POSITION */
            int x;
            int y = 95;

            /* GET BACKGROUND AND CREATE NEW IMAGE */
            Image scout_background = ImageIO.read(new File(Files.SINGLE_SCOUT_BACKGROUND.get()));
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            /* DRAW BACKGROUND ON NEW IMAGE */
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* READ IMAGE STRING */
            BufferedImage bi = ImageIO.read(new File(imageString));

            /* CALCULATE CENTER */
            x = (scout_background.getWidth(null) / 2) - (bi.getWidth() / 2);

            /* DRAW CHARACTER ON NEW IMAGE */
            g.drawImage(bi, x, y, null);

            /* SAVE IMAGE AS "temp_<discordID>/results.png" */
            ImageIO.write(result, "png", new File(tempUserDirectory + "/results.png"));
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
        }
    }

    public void drawImage(String imageStrings[])
    {
        try
        {
            /* STARTING POSITION */
            int x = 0;
            int y = 95;

            /* GET BACKGROUND AND CREATE NEW IMAGE */
            Image scout_background = ImageIO.read(new File(Files.MULTI_SCOUT_BACKGROUND.get()));
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            /* DRAW BACKGROUND ON NEW IMAGE */
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* ITERATE THROUGH CHARACTER IMAGES AND PASTE ON NEW IMAGE */
            for (int i = 0 ; i < imageStrings.length ; i++)
            {
                BufferedImage bi = ImageIO.read(new File(imageStrings[i]));
                g.drawImage(bi, x, y, null);
                x += bi.getWidth();

                /* RESET POSITION IF NEAR OUT OF BOUNDS */
                if (x >= result.getWidth())
                {
                    x = 0;
                    y += bi.getHeight();
                }
            }

            /* SAVE IMAGE AS "temp_<discordID>/results.png" */
            ImageIO.write(result, "png", new File(tempUserDirectory + "/results.png"));
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
        }
    }

    private int scout()
    {
        if (guaranteeOnePlatinum)
        {
           guaranteeOnePlatinum = false;
           return 5;
        }

        if (guaranteedScout)
        {
            return 4;
        }

        double d;
        d = RNG.nextDouble() * 100;

        /* TWO STAR (COPPER) CHARACTER */
        if (d < copper)
        {
            return 2;
        }
        /* THREE STAR (SILVER) CHARACTER */
        else if (d < copper + silver)
        {
            return 3;
        }
        /* FOUR STAR (GOLD) CHARACTER */
        else if (d < copper + silver + gold)
        {
            return 4;
        }
        /* FIVE STAR (PLATINUM) CHARACTER */
        else
        {
            return 5;
        }
    }

    private void setupBanner()
    {
        /* GET BANNER DATA AND CHARACTERS */
        SELECTED_BANNER = BANNERS.get(BANNER_ID);
        BANNER_CHARACTERS = SELECTED_BANNER.getCharacters();
        bannerType = Integer.parseInt(SELECTED_BANNER.getBannerType());
        bannerTypeData = USER.getBannerData(SELECTED_BANNER.getBannerName());

        /* SORT GOLD AND PLATINUM CHARACTERS AND STORE THEM IN THEIR OWN ARRAYS */
        for (Character character : BANNER_CHARACTERS)
        {
            if (Integer.parseInt(character.getRarity()) == 4)
            {
                goldCharacters.add(character);
            }
            else if (Integer.parseInt(character.getRarity()) == 5)
            {
                platinumCharacters.add(character);
            }
        }

        /* NO PLATINUM CHARACTER, ADJUST RATES */
        if (platinumCharacters.size() <= 0)
        {
            copper += platinum;
            platinum = 0;
        }

        /* MODIFY RATES ACCORDING TO BANNER TYPE */
        {
            /* STEP UP V1 */
            if (bannerType == 1)
            {
                switch (bannerTypeData)
                {
                    case 1:
                        /* MULTI SCOUT PRICE BECOMES 200 */
                        multiScoutPrice = 200;
                        break;
                    case 3:
                        /* MULTI SCOUT PRICE BECOMES 200 AND GOLD SCOUT RATES ARE INCREASED 1.5X */
                        multiScoutPrice = 200;
                        copper = copper - ((gold * 1.5) - gold) ;
                        gold = gold * 1.5;
                        break;
                    case 5:
                        /* GOLD SCOUT RATES ARE INCREASED 2.0X */
                        copper = copper - ((gold * 2.0) - gold);
                        gold = gold * 2.0;
                        break;
                    default:
                        break;
                }
            }
            /* RECORD CRYSTAL */
            else if (bannerType == 2)
            {
                /* GIVE RECORD CRYSTALS IF NOT DOING GUARANTEED SCOUT */
                if (!guaranteedScout)
                {
                    rcGet = getRecordCrystals();
                    bannerTypeData += rcGet;
                    USER.changeValue(SELECTED_BANNER.getBannerName(), bannerTypeData);
                }
            }
            /* STEP UP V2 */
            else if (bannerType == 3)
            {
                switch (bannerTypeData)
                {
                    case 1:
                        /* MULTI SCOUT PRICE BECOMES 200 */
                        multiScoutPrice = 200;
                        break;
                    case 3:
                        /* MULTI SCOUT PRICE BECOMES 200 AND PLATINUM SCOUT RATES ARE INCREASED 1.5X */
                        multiScoutPrice = 200;
                        copper = copper - ((platinum * 1.5) - platinum);
                        platinum = platinum * 1.5;
                        break;
                    case 5:
                        /* ONE PLATINUM CHARACTER IS GUARANTEED */
                        guaranteeOnePlatinum = true;
                        break;
                    case 6:
                        /* PLATINUM SCOUT RATES ARE INCREASED BY 2.0X */
                        copper = copper - ((platinum * 2.0) - platinum);
                        platinum = platinum * 2.0;

                    default:
                        break;
                }
            }
        }
    }

    private void buildScoutMenu()
    {
        /* GET ARGO DATA: HER FACE AND TEXT */
        argo();

        /* EDIT MENU */
        scoutMenu.setArgoText(argoText);
        scoutMenu.setThumbnail(argoFace);
        scoutMenu.setBannerName(SELECTED_BANNER.getBannerName());
        scoutMenu.setMdRemain(userMemoryDiamonds);
        scoutMenu.setUserName(CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator());

        /* EDIT DEPENDING ON TYPE OF PULL */
        if (CHOICE.equalsIgnoreCase("single") || CHOICE.equalsIgnoreCase("s") || CHOICE.equalsIgnoreCase("1"))
        {
            scoutMenu.setPullType(Text.SINGLE_PULL.get());
        }
        else if (CHOICE.equalsIgnoreCase("multi") || CHOICE.equalsIgnoreCase("m") || CHOICE.equalsIgnoreCase("11"))
        {
            scoutMenu.setPullType(Text.MULTI_PULL.get());
            scoutMenu.setBannerType(SELECTED_BANNER.bannerTypeToString());
            scoutMenu.setTypeData(String.valueOf(bannerTypeData));

            if (bannerType == 2)
            {
                scoutMenu.setRcGet(rcGet);
            }
        }
    }

    private void displayResults()
    {
        try
        {
            /* SEND SCOUT MENU AND SCOUT RESULT IMAGE*/
            CHANNEL.sendMessage(scoutMenu.get().build());
            CHANNEL.sendFile(new File(tempUserDirectory + "/results.png"));
        }
        catch (FileNotFoundException e)
        {
            CHANNEL.sendMessage(new WarningMessage("IMAGE NOT FOUND", "Unable to display scout result.").get().build());
        }
        catch (RateLimitException e)
        {
            CHANNEL.sendMessage(new WarningMessage("RATE LIMIT EXCEPTION", "Slow down with the messages!").get().build());
        }

    }

    public List<Character> getCharacters()
    {
        return characters;
    }

    private Character randGoldCharacter()
    {
        int randIndex = goldBanners.get(RNG.nextInt(goldBanners.size()));
        Banner randBanner = BANNERS.get(randIndex - 1);
        List<Character> randCharacters = randBanner.getCharacters();

        return randCharacters.get(RNG.nextInt(randCharacters.size()));
    }

    private int getRecordCrystals()
    {
        double d = RNG.nextDouble();
        double recordCrystalRate = recordCrystalRates.get(0);

        for (int i = 0 ; i < recordCrystalRates.size() ; i++)
        {
            recordCrystalRate += recordCrystalRates.get(i);
            if (d < recordCrystalRate)
            {
                return i;
            }
        }

        return 0;
    }
}
