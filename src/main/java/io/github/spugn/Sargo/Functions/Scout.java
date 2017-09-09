package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Discord.Discord4J.Message;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

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
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;
    private static int BANNER_ID;
    private static String CHOICE;
    private static List<Banner> BANNERS;

    private static Banner SELECTED_BANNER;
    private static List<Character> BANNER_CHARACTERS;
    private static Random RNG;

    /* RATES */
    private double copper;
    private double silver;
    private double gold;
    private double platinum;

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

    public Scout(IDiscordClient client, IChannel channel, int bannerID, String choice)
    {
        CLIENT = client;
        CHANNEL = channel;
        BANNER_ID = bannerID - 1;
        CHOICE = choice;
        RNG = new Random(System.currentTimeMillis());

        /* READ Banners.xml */
        BannerParser bannersXML = new BannerParser();
        BANNERS = bannersXML.readConfig(Files.BANNER_XML.get());

        getRates();

        PLATINUM_PH = new File(Files.PLATINUM_PLACEHOLDER.get());
        GOLD_PH = new File(Files.GOLD_PLACEHOLDER.get());
        SILVER_PH = new File(Files.SILVER_PLACEHOLDER.get());
        COPPER_PH = new File(Files.COPPER_PLACEHOLDER.get());

        imageStrings = new String[11];
        characters = new ArrayList<>();
        scoutMenu = new ScoutMenu();
        goldCharacters = new ArrayList<>();
        platinumCharacters = new ArrayList<>();

        run();
    }

    private void run()
    {
        /* CHECK IF REQUESTED BANNER IS AVAILABLE */
        if (BANNER_ID < BANNERS.size())
        {
            /* GET BANNER AND CHARACTER DATA. ALSO ADJUST RATES IF NEEDED */
            setupBanner();

            if (CHOICE.equalsIgnoreCase("single") || CHOICE.equalsIgnoreCase("s") || CHOICE.equalsIgnoreCase("1"))
            {
                /* PULL AND DRAW SCOUT RESULT IMAGE */
                singlePull();
                drawImage(imageString);
            }
            else if (CHOICE.equalsIgnoreCase("multi") || CHOICE.equalsIgnoreCase("m") || CHOICE.equalsIgnoreCase("11"))
            {
                /* PULL AND DRAW SCOUT RESULT IMAGE */
                multiPull();
                drawImage(imageStrings);
            }
            else
            {
                new Message(CLIENT, CHANNEL, Text.SCOUT_UNKNOWN_TYPE.get(), true, 255, 0, 0);
                return;
            }

            /* BUILD SCOUT MENU, AND DISPLAY RESULTS */
            buildScoutMenu();
            displayResults();
        }
        else
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_UNKNOWN_BANNER.get(), true, 255, 0, 0);
            return;
        }
    }

    private void singlePull()
    {
        /* GET CHARACTER */
        characters.add(getCharacter(scout()));

        /* FIXME - DEBUG MESSAGE */
        //System.out.println(characters.get(0).toString());

        /* SAVE IMAGE STRING AND RARITY OF CHARACTER */
        imageString = characters.get(0).getImagePath();
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
        for(int i = 0 ; i < 11 ; i++)
        {
            /* FIXME - DEBUG MESSAGE */
            //System.out.println(characters.get(i).toString());

            imageStrings[i] = characters.get(i).getImagePath();
        }

        /* SAVE HIGHEST RARITY CHARACTER */
        highestRarity = Integer.parseInt(characters.get(0).getRarity());
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
            character = goldCharacters.get(RNG.nextInt(goldCharacters.size()));
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

            /* SAVE IMAGE AS "images/latest_result.png" */
            ImageIO.write(result, "png", new File(Files.SCOUT_RESULT.get()));
        }
        catch (IOException e)
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_IMAGE_GEN_ERROR.get(), true, 255, 0, 0);
            e.printStackTrace();
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

            /* SAVE IMAGE AS "images/latest_result.png" */
            ImageIO.write(result, "png", new File(Files.SCOUT_RESULT.get()));
        }
        catch (IOException e)
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_IMAGE_GEN_ERROR.get(), true, 255, 0, 0);
            e.printStackTrace();
        }
    }

    private int scout()
    {
        double d;
        d = RNG.nextDouble();

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

    private void getRates()
    {
        SettingsParser settings = new SettingsParser();

        copper = settings.getTwoRates();
        silver = settings.getThreeRates();
        gold = settings.getFourRates();
        platinum = settings.getFiveRates();
    }

    private void setupBanner()
    {
        /* GET BANNER DATA AND CHARACTERS */
        SELECTED_BANNER = BANNERS.get(BANNER_ID);
        BANNER_CHARACTERS = SELECTED_BANNER.getCharacters();

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
    }

    private void buildScoutMenu()
    {
        /* GET ARGO DATA: HER FACE AND TEXT */
        argo();

        /* EDIT MENU */
        scoutMenu.setArgoText(argoText);
        scoutMenu.setThumbnail(argoFace);
        scoutMenu.setBannerName(SELECTED_BANNER.getBannerName());

        /* EDIT DEPENDING ON TYPE OF PULL */
        if (CHOICE.equalsIgnoreCase("single") || CHOICE.equalsIgnoreCase("s") || CHOICE.equalsIgnoreCase("1"))
        {
            scoutMenu.setPullType(Text.SINGLE_PULL.get());
        }
        else if (CHOICE.equalsIgnoreCase("multi") || CHOICE.equalsIgnoreCase("m") || CHOICE.equalsIgnoreCase("11"))
        {
            scoutMenu.setPullType(Text.MULTI_PULL.get());
        }
    }

    private void displayResults()
    {
        try
        {
            /* SEND SCOUT MENU AND SCOUT RESULT IMAGE*/
            CHANNEL.sendMessage(scoutMenu.get().build());
            CHANNEL.sendFile(new File(Files.SCOUT_RESULT.get()));

            /* DELETE SCOUT RESULT IMAGE */
            new File(Files.SCOUT_RESULT.get()).delete();
        }
        catch (FileNotFoundException e)
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_MISSING_RESULT_IMAGE_ERROR.get(), true, 255, 0, 0);
        }

    }

    public List<Character> getCharacters()
    {
        return characters;
    }
}
