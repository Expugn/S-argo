package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
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

public class Scout
{
    private IChannel CHANNEL;
    private int BANNER_ID;
    private String CHOICE;
    private String DISCORD_ID;

    private SettingsParser SETTINGS;
    private List<Banner> BANNERS;
    private UserParser USER;

    private List<Double> RECORD_CRYSTAL_RATES;
    private double COPPER;
    private double SILVER;
    private double GOLD;
    private double PLATINUM;
    private List<Integer> GOLD_BANNERS;

    private Random RNG;

    private Banner SELECTED_BANNER;
    private List<Character> BANNER_CHARACTERS;
    private int bannerType;
    private int bannerTypeData;
    private List<Character> goldCharacters;
    private List<Character> platinumCharacters;

    private String imageString;
    private String imageStrings[];
    private int highestRarity;

    private ScoutMenu scoutMenu;
    private String argoText;
    private String argoFace;

    private File tempUserDirectory;

    private List<Character> characters;

    private int rcGet;
    private boolean guaranteeOnePlatinum;
    private boolean guaranteedScout;

    private int userMemoryDiamonds;
    private int userHackingCrystals;
    private int userRecordCrystals;
    private int singleScoutPrice;
    private int multiScoutPrice;

    public Scout(IChannel channel, int bannerID, String choice, String discordID) throws RateLimitException
    {
        CHANNEL = channel;
        BANNER_ID = bannerID - 1;
        CHOICE = choice;
        DISCORD_ID = discordID;

        initFiles();
        initSettings();
        initUser();
        initVariables();
        initMemoryDiamonds();

        if (!(BANNER_ID < BANNERS.size() && BANNER_ID >= 0))
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
            return;
        }

        initBanner();

        if (CHOICE.equalsIgnoreCase("single") || CHOICE.equalsIgnoreCase("s") || CHOICE.equalsIgnoreCase("1"))
        {
            if (userMemoryDiamonds < singleScoutPrice)
            {
                CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.").get().build());
                return;
            }

            userMemoryDiamonds -= singleScoutPrice;
            USER.setMemoryDiamonds(userMemoryDiamonds);

            doSinglePull();
        }
        else if (CHOICE.equalsIgnoreCase("multi") || CHOICE.equalsIgnoreCase("m") || CHOICE.equalsIgnoreCase("11"))
        {
            if (userMemoryDiamonds < multiScoutPrice)
            {
                CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.").get().build());
                return;
            }

            userMemoryDiamonds -= multiScoutPrice;
            USER.setMemoryDiamonds(userMemoryDiamonds);

            doMultiPull();
            updateBannerData();
        }
        else if (CHOICE.equalsIgnoreCase("g") || CHOICE.equalsIgnoreCase("guaranteed") || CHOICE.equalsIgnoreCase("rc"))
        {
            if (bannerType != 2)
            {
                CHANNEL.sendMessage(new WarningMessage("NO RECORD CRYSTAL SCOUT", "This banner does not have a record crystal scout.").get().build());
                return;
            }

            userRecordCrystals = USER.getBannerData(SELECTED_BANNER.getBannerName());

            if (userRecordCrystals < 10)
            {
                CHANNEL.sendMessage(new WarningMessage("INSUFFICIENT RECORD CRYSTALS", "You need 10 record crystals to do a record crystal scout.").get().build());
                return;
            }

            userRecordCrystals -= 10;
            USER.changeValue(SELECTED_BANNER.getBannerName(), userRecordCrystals);

            guaranteedScout = true;

            doSinglePull();
        }

        buildScoutMenu();

        IMessage display = null;
        try
        {
            display = CHANNEL.sendMessage(scoutMenu.get().build());
            CHANNEL.sendFile(new File(tempUserDirectory + "/results.png"));
        }
        catch (FileNotFoundException e)
        {
            CHANNEL.sendMessage(new WarningMessage("IMAGE NOT FOUND", "Unable to display scout result.").get().build());
        }
        catch (RateLimitException e)
        {
            EmbedBuilder rateLimited = new WarningMessage("RATE LIMIT EXCEPTION", "Slow down on the requests!").get();
            display.edit(rateLimited.build());
            deleteTempDirectory();
        }

        USER.saveData();
        deleteTempDirectory();
    }

    private void initFiles()
    {
        /* OPEN SETTINGS FILE */
        SETTINGS = new SettingsParser();

        /* OPEN BANNERS FILE */
        BannerParser bannersXML = new BannerParser();
        BANNERS = bannersXML.readConfig(Files.BANNER_XML.get());

        /* OPEN USER FILE */
        USER = new UserParser(DISCORD_ID);
    }

    private void initSettings()
    {
        COPPER = (int) (SETTINGS.getTwoRates() * 100);
        SILVER = (int) (SETTINGS.getThreeRates() * 100);
        GOLD = (int) (SETTINGS.getFourRates() * 100);
        PLATINUM = (int) (SETTINGS.getFiveRates() * 100);
        RECORD_CRYSTAL_RATES = SETTINGS.getRecordCrystalRates();
        GOLD_BANNERS = SETTINGS.getGoldBanners();
    }

    private void initBanner()
    {
        SELECTED_BANNER = BANNERS.get(BANNER_ID);
        BANNER_CHARACTERS = SELECTED_BANNER.getCharacters();
        bannerType = Integer.parseInt(SELECTED_BANNER.getBannerType());
        bannerTypeData = USER.getBannerData(SELECTED_BANNER.getBannerName());
        goldCharacters = new ArrayList<>();
        platinumCharacters = new ArrayList<>();

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

        if (platinumCharacters.size() <= 0)
        {
            COPPER += PLATINUM;
            PLATINUM = 0;
        }

        if (bannerType == 1)
        {
            switch (bannerTypeData)
            {
                case 1:
                    multiScoutPrice = 200;
                    break;
                case 3:
                    multiScoutPrice = 200;
                    COPPER = COPPER - ((GOLD * 1.5) - GOLD) ;
                    GOLD = GOLD * 1.5;
                    break;
                case 5:
                    COPPER = COPPER - ((GOLD * 2.0) - GOLD);
                    GOLD = GOLD * 2.0;
                    break;
                default:
                    break;
            }
        }
        else if (bannerType == 3)
        {
            switch (bannerTypeData)
            {
                case 1:
                    multiScoutPrice = 200;
                    break;
                case 3:
                    multiScoutPrice = 200;
                    COPPER = COPPER - ((PLATINUM * 1.5) - PLATINUM);
                    PLATINUM = PLATINUM * 1.5;
                    break;
                case 5:
                    guaranteeOnePlatinum = true;
                    break;
                case 6:
                    COPPER = COPPER - ((PLATINUM * 2.0) - PLATINUM);
                    PLATINUM = PLATINUM * 2.0;
                default:
                    break;
            }
        }
    }

    private void initUser()
    {
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userHackingCrystals = USER.getHackingCrystals();
        userRecordCrystals = 0;
    }

    private void initVariables()
    {
        RNG = new Random(System.currentTimeMillis());

        scoutMenu = new ScoutMenu();

        imageStrings = new String[11];
        characters = new ArrayList<>();

        guaranteeOnePlatinum = false;
        guaranteedScout = false;
    }

    private void initMemoryDiamonds()
    {
        singleScoutPrice = 25;
        multiScoutPrice = 250;
    }


    /* PULLS ======================================================================================================== */


    private void doSinglePull()
    {
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        tempUserDirectory.mkdir();

        characters.add(getCharacter(scout()));
        highestRarity = Integer.parseInt(characters.get(0).getRarity());

        generateImageString();
        drawImage(imageString);
    }

    private void doMultiPull()
    {
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        tempUserDirectory.mkdir();

        for (int i = 0 ; i < 11 ; i++)
        {
            characters.add(getCharacter(scout()));
        }
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
        highestRarity = Integer.parseInt(characters.get(0).getRarity());

        generateImageStrings();
        drawImage(imageStrings);
    }

    private void updateBannerData()
    {
        if (bannerType == 1)
        {
            int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName());
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
        else if (bannerType == 2)
        {
            rcGet = getRecordCrystals();
            bannerTypeData += rcGet;
            USER.changeValue(SELECTED_BANNER.getBannerName(), bannerTypeData);
        }
        else if (bannerType == 3)
        {
            int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName());
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

    /* IMAGE DRAWING ================================================================================================ */


    private void generateImageString()
    {
        if (!USER.getCharacterBox().isEmpty())
        {
            for (Character userCharacter : USER.getCharacterBox())
            {
                if (userCharacter.getPrefix().equals(characters.get(0).getPrefix()))
                {
                    giveHackingCrystals(characters.get(0));
                    try
                    {
                        /* CHARACTER IMAGE */
                        Image characterImage = ImageIO.read(new File(characters.get(0).getImagePath()));
                        BufferedImage result = new BufferedImage(characterImage.getWidth(null), characterImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                        Graphics g = result.getGraphics();
                        g.drawImage(characterImage, 0, 0, null);

                        /* SHADE CHARACTER IMAGE */
                        BufferedImage bi = ImageIO.read(new File("images/Miscellaneous/Owned_Character_Shade.png"));
                        g.drawImage(bi, 0, 0, null);

                        /* SAVE */
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
        }
        USER.addCharacter(characters.get(0));
        imageString = characters.get(0).getImagePath();
    }

    private void generateImageStrings()
    {
        boolean foundDuplicate = false;

        for (int i = 0 ; i < 11 ; i++)
        {
            if (!USER.getCharacterBox().isEmpty())
            {
                for (Character userCharacter : USER.getCharacterBox())
                {
                    if (userCharacter.getPrefix().equals(characters.get(i).getPrefix()) && userCharacter.getName().equals(characters.get(i).getName()))
                    {
                        foundDuplicate = true;
                        giveHackingCrystals(characters.get(i));

                        try
                        {
                            /* CHARACTER IMAGE */
                            Image scout_background = ImageIO.read(new File(characters.get(i).getImagePath()));
                            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            Graphics g = result.getGraphics();
                            g.drawImage(scout_background, 0, 0, null);

                            /* SHADE CHARACTER IMAGE */
                            BufferedImage bi = ImageIO.read(new File("images/Miscellaneous/Owned_Character_Shade.png"));
                            g.drawImage(bi, 0, 0, null);

                            /* SAVE */
                            ImageIO.write(result, "png", new File(tempUserDirectory + "/temp_" + i + ".png"));
                            imageStrings[i] = tempUserDirectory + "/temp_" + i + ".png";
                        }
                        catch (IOException e)
                        {
                            CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
                        }
                    }
                }

                if (!foundDuplicate)
                {
                    USER.addCharacter(characters.get(i));
                    imageStrings[i] = characters.get(i).getImagePath();
                }

                foundDuplicate = false;
            }
            else
            {
                USER.addCharacter(characters.get(i));
                imageStrings[i] = characters.get(i).getImagePath();
            }
        }
    }

    public void drawImage(String imageString)
    {
        try
        {
            int x;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background = ImageIO.read(new File(Files.SINGLE_SCOUT_BACKGROUND.get()));
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* CHARACTER IMAGE */
            BufferedImage bi = ImageIO.read(new File(imageString));
            x = (scout_background.getWidth(null) / 2) - (bi.getWidth() / 2);
            g.drawImage(bi, x, y, null);

            /* SAVE */
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
            int x = 0;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background = ImageIO.read(new File(Files.MULTI_SCOUT_BACKGROUND.get()));
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* CHARACTER IMAGES */
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

            /* SAVE */
            ImageIO.write(result, "png", new File(tempUserDirectory + "/results.png"));
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
        }
    }


    /* CHARACTER GENERATION ==========================================================================================*/


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
            character = platinumCharacters.get(RNG.nextInt(platinumCharacters.size()));
        }

        File characterImage = new File(character.getImagePath());
        if (!characterImage.exists())
        {
            switch (Integer.parseInt(character.getRarity()))
            {
                case 2:
                    character.setImagePath(Files.COPPER_PLACEHOLDER.get());
                    break;
                case 3:
                    character.setImagePath(Files.SILVER_PLACEHOLDER.get());
                    break;
                case 4:
                    character.setImagePath(Files.GOLD_PLACEHOLDER.get());
                    break;
                case 5:
                    character.setImagePath(Files.PLATINUM_PLACEHOLDER.get());
                    break;
                default:
                    character.setImagePath(Files.GRAY_PLACEHOLDER.get());
                    break;
            }
        }
        System.out.println(character.toString());
        return character;
    }

    private Character randGoldCharacter()
    {
        int randIndex = GOLD_BANNERS.get(RNG.nextInt(GOLD_BANNERS.size()));
        Banner randBanner = BANNERS.get(randIndex - 1);
        List<Character> randCharacters = randBanner.getCharacters();

        return randCharacters.get(RNG.nextInt(randCharacters.size()));
    }


    /* MENU ========================================================================================================= */


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
        else if (CHOICE.equalsIgnoreCase("g") || CHOICE.equalsIgnoreCase("guaranteed") || CHOICE.equalsIgnoreCase("rc"))
        {
            scoutMenu.setGuaranteedScout(true);
            scoutMenu.setTypeData(String.valueOf(userRecordCrystals));
        }
    }

    private void argo()
    {
        double aText = RNG.nextDouble();
        double aFace = RNG.nextDouble();

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



        switch (highestRarity)
        {
            case 2:
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


    /* USER DATA MODIFIERS ========================================================================================== */


    private void giveHackingCrystals(Character c)
    {
        switch(Integer.parseInt(c.getRarity()))
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


    /* MISC ========================================================================================================= */


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

    private int getRecordCrystals()
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
}
