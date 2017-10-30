package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.*;
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

public class WeaponScout
{
    private IChannel CHANNEL;
    private int BANNER_ID;
    private String CHOICE;
    private String DISCORD_ID;

    private SettingsParser SETTINGS;
    private List<Banner> BANNERS;
    private UserParser USER;

    private double COPPER;
    private double SILVER;
    private double GOLD;
    private boolean IMAGE_DISABLED;

    private Random RNG;

    private Banner SELECTED_BANNER;
    private List<Weapon> BANNER_WEAPONS;
    private int bannerType;
    private int bannerTypeData;

    private String imageString;
    private String imageStrings[];

    private WeaponScoutMenu scoutMenu;
    private String chestImage;

    private File tempUserDirectory;

    private List<Weapon> weapons;

    private int userMemoryDiamonds;
    private int userColBalance;
    private int singleScoutPrice;
    private int multiScoutPrice;

    private boolean generateImage;
    private String weaponString;

    private boolean stopScout;

    public WeaponScout(IChannel channel, int bannerID, String choice, String discordID) throws RateLimitException
    {
        CHANNEL = channel;
        BANNER_ID = bannerID - 1;
        CHOICE = choice;
        DISCORD_ID = discordID;
        generateImage = false;
        stopScout = false;

        initFiles();
        initSettings();
        initUser();
        initVariables();
        initMemoryDiamonds();

        if (!(BANNER_ID < BANNERS.size() && BANNER_ID >= 0))
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.commandPrefix + "**scout**' for a list of banners.").get().build());
            return;
        }

        initBanner();

        if (BANNER_WEAPONS.size() <= 0)
        {
            CHANNEL.sendMessage(new WarningMessage("NO WEAPONS AVAILABLE", "This banner does not have a weapon scout.").get().build());
            return;
        }

        if (CHOICE.equalsIgnoreCase("ws") || CHOICE.equalsIgnoreCase("wsi"))
        {
            if (userMemoryDiamonds < singleScoutPrice)
            {
                CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.commandPrefix + "**shop**' to get more Memory Diamonds.").get().build());
                return;
            }

            if (CHOICE.equalsIgnoreCase("wsi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }

            userMemoryDiamonds -= singleScoutPrice;
            USER.setMemoryDiamonds(userMemoryDiamonds);

            doSinglePull();
        }
        else if (CHOICE.equalsIgnoreCase("wm") || CHOICE.equalsIgnoreCase("wmi"))
        {
            if (userMemoryDiamonds < multiScoutPrice)
            {
                CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.commandPrefix + "**shop**' to get more Memory Diamonds.").get().build());
                return;
            }

            if (CHOICE.equalsIgnoreCase("wmi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }

            userMemoryDiamonds -= multiScoutPrice;
            USER.setMemoryDiamonds(userMemoryDiamonds);

            doMultiPull();
            updateBannerData();
        }
        else
        {
            return;
        }

        if (stopScout)
        {
            return;
        }

        buildScoutMenu();

        IMessage display = null;
        try
        {
            display = CHANNEL.sendMessage(scoutMenu.get().build());

            if (generateImage && !IMAGE_DISABLED)
            {
                CHANNEL.sendFile(new File(tempUserDirectory + "/results.png"));
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

            }
            deleteTempDirectory();
            return;
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
        COPPER = (int) ((SETTINGS.getTwoRates() * 100) + (SETTINGS.getFiveRates() * 100));
        SILVER = (int) (SETTINGS.getThreeRates() * 100);
        GOLD = (int) (SETTINGS.getFourRates() * 100);
        IMAGE_DISABLED = SETTINGS.isDisableImages();
    }

    private void initBanner()
    {
        SELECTED_BANNER = BANNERS.get(BANNER_ID);
        BANNER_WEAPONS = SELECTED_BANNER.getWeapons();

        bannerType = Integer.parseInt(SELECTED_BANNER.getBannerWepType());
        bannerTypeData = USER.getBannerData(SELECTED_BANNER.getBannerName() + " Weapons");

        if (USER.isBannerInfoExists(SELECTED_BANNER.getBannerName() + " Weapons") == -1)
        {
            int newBannerInfoValue;

            if (bannerType == 1)
            {
                newBannerInfoValue = 1;
            }
            else
            {
                newBannerInfoValue = 0;
            }

            USER.addBannerInfo(SELECTED_BANNER.getBannerName() + " Weapons", newBannerInfoValue);
            bannerTypeData = newBannerInfoValue;
        }

        if (bannerType == 1)
        {
            switch (bannerTypeData)
            {
                case 1:
                    multiScoutPrice = 100;
                    break;
                case 3:
                    COPPER = COPPER - ((GOLD * 2.0) - GOLD) ;
                    GOLD = GOLD * 2.0;
                    break;
                default:
                    break;
            }
        }
    }

    private void initUser()
    {
        userMemoryDiamonds = USER.getMemoryDiamonds();
        userColBalance = USER.getColBalance();
    }

    private void initVariables()
    {
        RNG = new Random(System.currentTimeMillis());

        scoutMenu = new WeaponScoutMenu();

        imageStrings = new String[11];
        weapons = new ArrayList<>();

        weaponString = "";

        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
    }

    private void initMemoryDiamonds()
    {
        singleScoutPrice = 15;
        multiScoutPrice = 150;
    }


    /* PULLS ======================================================================================================== */


    private void doSinglePull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        weapons.add(getWeapon(scout()));

        generateImageString();
        drawImage(imageString);
    }

    private void doMultiPull()
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
        drawImage(imageStrings);
    }

    private void updateBannerData()
    {
        if (bannerType == 1)
        {
            int currentStep = USER.getBannerData(SELECTED_BANNER.getBannerName() + " Weapons");
            currentStep++;
            if (currentStep > 3)
            {
                USER.changeValue(SELECTED_BANNER.getBannerName() + " Weapons", 1);
            }
            else
            {
                USER.changeValue(SELECTED_BANNER.getBannerName() + " Weapons", currentStep);
            }
        }
    }

    /* IMAGE DRAWING ================================================================================================ */


    private void generateImageString()
    {
        if (weapons.get(0).getRarity().equals("4"))
        {
            USER.addWeapon(weapons.get(0));
        }
        weaponString += weapons.get(0).toString() + "\n";
        imageString = weapons.get(0).getImagePath();
    }

    private void generateImageStrings()
    {
        for (int i = 0 ; i < 11 ; i++)
        {
            if (weapons.get(i).getRarity().equals("4"))
            {
                USER.addWeapon(weapons.get(i));
            }
            weaponString += weapons.get(i).toString() + "\n";
            imageStrings[i] = weapons.get(i).getImagePath();
        }
    }

    public void drawImage(String imageString)
    {
        try
        {
            int x;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background = ImageIO.read(new File("images/Scout Backgrounds/Single_Weapon.png"));
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* CHARACTER IMAGE */
            BufferedImage bi = ImageIO.read(new File(imageString));
            x = (scout_background.getWidth(null) / 2) - (bi.getWidth() / 2);
            g.drawImage(bi, x, y, null);

            /* SAVE */
            if (generateImage && !IMAGE_DISABLED)
            {
                ImageIO.write(result, "png", new File(tempUserDirectory + "/results.png"));
            }
        }
        catch (IOException e)
        {
            if (generateImage && !IMAGE_DISABLED)
            {
                CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
                stopScout = true;
            }
        }
    }

    public void drawImage(String imageStrings[])
    {
        try
        {
            int x = 0;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background = ImageIO.read(new File("images/Scout Backgrounds/Multi_Weapon.png"));
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
            if (generateImage && !IMAGE_DISABLED)
            {
                ImageIO.write(result, "png", new File(tempUserDirectory + "/results.png"));
            }
        }
        catch (IOException e)
        {
            if (generateImage && !IMAGE_DISABLED)
            {
                CHANNEL.sendMessage(new WarningMessage("IO EXCEPTION", "Failed to create scout result image.").get().build());
                stopScout = true;
            }
        }
    }


    /* CHARACTER GENERATION ==========================================================================================*/


    private int scout()
    {
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

    private Weapon getWeapon(int rarity)
    {
        Weapon weapon;
        if (rarity == 2)
        {
            CopperWeapon cW = new CopperWeapon();
            weapon = cW.getWeapon(RNG.nextInt(cW.getSize()));
        }
        else if (rarity == 3)
        {
            SilverWeapon sW = new SilverWeapon();
            weapon = sW.getWeapon(RNG.nextInt(sW.getSize()));
        }
        else
        {
            weapon = BANNER_WEAPONS.get(RNG.nextInt(BANNER_WEAPONS.size()));
        }

        File weaponImage = new File(weapon.getImagePath());
        if (!weaponImage.exists())
        {
            switch (Integer.parseInt(weapon.getRarity()))
            {
                case 2:
                    weapon.setImagePath("images/Weapons/Placeholders/Copper.png");
                    break;
                case 3:
                    weapon.setImagePath("images/Weapons/Placeholders/Silver.png");
                    break;
                case 4:
                    weapon.setImagePath("images/Weapons/Placeholders/Gold.png");
                    break;
                default:
                    weapon.setImagePath("images/Weapons/Placeholders/Gray.png");
                    break;
            }
        }

        giveCol(weapon);
        return weapon;
    }


    /* MENU ========================================================================================================= */


    private void buildScoutMenu()
    {
        /* GET CHEST DATA: CHEST IMAGE */
        chest();

        /* EDIT MENU */
        scoutMenu.setThumbnail(chestImage);
        scoutMenu.setBannerName(SELECTED_BANNER.getBannerName());
        scoutMenu.setMdRemain(userMemoryDiamonds);
        scoutMenu.setUserName(CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator());

        /* EDIT DEPENDING ON TYPE OF PULL */
        if (CHOICE.equalsIgnoreCase("ws") || CHOICE.equalsIgnoreCase("wsi"))
        {
            scoutMenu.setPullType(Text.SINGLE_PULL.get());
        }
        else if (CHOICE.equalsIgnoreCase("wm") || CHOICE.equalsIgnoreCase("wmi"))
        {
            scoutMenu.setPullType(Text.MULTI_PULL.get());
        }

        if (bannerType == 0)
        {
            scoutMenu.setBannerType("Weapon Scout");
        }
        else if (bannerType == 1)
        {
            scoutMenu.setBannerType("Step Up Weapon Scout");
            scoutMenu.setTypeData(bannerTypeData + "");
        }
        else
        {
            scoutMenu.setBannerType("Weapon Scout");
        }

        if (!generateImage)
        {
            scoutMenu.setWeaponString(weaponString);
        }
    }

    private void chest()
    {
        switch (Integer.parseInt(weapons.get(0).getRarity()))
        {
            case 2:
                chestImage = Images.CHEST_BROWN.getUrl();
                break;

            case 3:
                chestImage = Images.CHEST_BLUE.getUrl();
                break;

            case 4:
                chestImage = Images.CHEST_RED.getUrl();
                break;

            default:
                chestImage = Images.CHEST_BROWN.getUrl();
                break;
        }
    }


    /* USER DATA MODIFIERS ========================================================================================== */


    private void giveCol(Weapon w)
    {
        final int COL_MAX = 99999999;
        switch(Integer.parseInt(w.getRarity()))
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
}
