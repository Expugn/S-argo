package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
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

public class TicketScout
{
    private IChannel CHANNEL;
    private String CHOICE;
    private String DISCORD_ID;

    private SettingsParser SETTINGS;
    private UserParser USER;

    private boolean IMAGE_DISABLED;

    private Random RNG;

    private String imageString;
    private String imageStrings[];

    private TicketScoutMenu scoutMenu;
    private String chestImage;

    private List<Item> items;

    private List<Item> attributeItemsList;
    private List<Item> medallionsAndKeyList;

    private File tempUserDirectory;

    private int userColBalance;
    private int userTotalScouts;

    private boolean generateImage;
    private String itemString;
    private boolean isPlusTicket;

    private boolean stopScout;

    public TicketScout(IChannel channel, String choice, String discordID) throws RateLimitException
    {
        CHANNEL = channel;
        CHOICE = choice;
        DISCORD_ID = discordID;
        generateImage = false;
        stopScout = false;

        initFiles();
        initSettings();
        initUser();
        initVariables();

        if (CHOICE.equalsIgnoreCase("nts") || CHOICE.equalsIgnoreCase("ntsi"))
        {
            if (CHOICE.equalsIgnoreCase("ntsi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }

            initItems();
            doSinglePull();
        }
        else if (CHOICE.equalsIgnoreCase("ntm") || CHOICE.equalsIgnoreCase("ntmi"))
        {
            if (CHOICE.equalsIgnoreCase("ntmi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }

            initItems();
            doMultiPull();
        }
        else if (CHOICE.equalsIgnoreCase("pts") || CHOICE.equalsIgnoreCase("ptsi"))
        {
            if (CHOICE.equalsIgnoreCase("ptsi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }
            isPlusTicket = true;

            initItems();
            doSinglePull();
        }
        else if (CHOICE.equalsIgnoreCase("ptm") || CHOICE.equalsIgnoreCase("ptmi"))
        {
            if (CHOICE.equalsIgnoreCase("ptmi") && !IMAGE_DISABLED)
            {
                generateImage = true;
            }
            isPlusTicket = true;

            initItems();
            doMultiPull();
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

        USER.upgradeExchangeSwords();
        USER.saveData();
        deleteTempDirectory();
    }

    private void initFiles()
    {
        /* OPEN SETTINGS FILE */
        SETTINGS = new SettingsParser();

        /* OPEN USER FILE */
        USER = new UserParser(DISCORD_ID);
    }

    private void initSettings()
    {
        IMAGE_DISABLED = SETTINGS.isDisableImages();
    }

    private void initUser()
    {
        userColBalance = USER.getColBalance();
        userTotalScouts = USER.getTotalTicketScout();
    }

    private void initVariables()
    {
        RNG = new Random(System.currentTimeMillis());

        scoutMenu = new TicketScoutMenu();

        imageStrings = new String[11];
        items = new ArrayList<>();
        itemString = "";

        tempUserDirectory = new File("images/temp_" + DISCORD_ID);

        attributeItemsList = new ArrayList<>();
        medallionsAndKeyList = new ArrayList<>();
    }

    private void initItems()
    {
        if (!isPlusTicket)
        {
            attributeItemsList.add(new Item("HP Shard", 50, 2500));
            attributeItemsList.add(new Item("MP Shard", 50, 2500));
            attributeItemsList.add(new Item("Attack Shard", 50, 2500));
            attributeItemsList.add(new Item("Defense Shard", 50, 2500));
            attributeItemsList.add(new Item("Critical Shard", 50, 2500));
            attributeItemsList.add(new Item("Skill Shard", 50, 2500));
            attributeItemsList.add(new Item("HP Crystal", 50, 12500));
            attributeItemsList.add(new Item("MP Crystal", 50, 12500));
            attributeItemsList.add(new Item("Attack Crystal", 50, 12500));
            attributeItemsList.add(new Item("Defense Crystal", 50, 12500));
            attributeItemsList.add(new Item("Critical Crystal", 50, 12500));
            attributeItemsList.add(new Item("Skill Crystal", 50, 12500));
            attributeItemsList.add(new Item("Holy HP Crystal", 10, 10000));
            attributeItemsList.add(new Item("Holy MP Crystal", 10, 10000));
            attributeItemsList.add(new Item("Holy Attack Crystal", 10, 10000));
            attributeItemsList.add(new Item("Holy Defense Crystal", 10, 10000));
            attributeItemsList.add(new Item("Holy Critical Crystal", 10, 10000));
            attributeItemsList.add(new Item("Holy Skill Crystal", 10, 10000));
            attributeItemsList.add(new Item("EXP X-Potion", 3, 3000));
            attributeItemsList.add(new Item("EXP Hi-Potion", 5, 1250));
            attributeItemsList.add(new Item("EXP Potion", 10, 500));

            medallionsAndKeyList.add(new Item("Lv. 80 Decryption Key", 1, 0));
            medallionsAndKeyList.add(new Item("Void Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Fire Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Water Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Wind Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Holy Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Dark Medallion", 1, 2500));
            medallionsAndKeyList.add(new Item("Earth Medallion", 1, 2500));
        }
        else
        {
            medallionsAndKeyList.add(new Item("Void Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Fire Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Water Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Wind Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Holy Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Dark Medallion", 3, 7500));
            medallionsAndKeyList.add(new Item("Earth Medallion", 3, 7500));
        }
    }


    /* PULLS ======================================================================================================== */


    private void doSinglePull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        userTotalScouts += 1;
        USER.setTotalTicketScout(userTotalScouts);

        items.add(getItem(scout()));

        generateImageString();
        drawImage(imageString);
    }

    private void doMultiPull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        userTotalScouts += 11;
        USER.setTotalTicketScout(userTotalScouts);

        for (int i = 0 ; i < 11 ; i++)
        {
            items.add(getItem(scout()));
        }

        generateImageStrings();
        drawImage(imageStrings);
    }

    /* IMAGE DRAWING ================================================================================================ */


    private void generateImageString()
    {
        if (items.get(0).getValue() < 0)
        {
            if (items.get(0).getName().equals("Exchange Sword R2"))
                USER.setR2ExchangeSwords(USER.getR2ExchangeSwords() + items.get(0).getQuantity());
            if (items.get(0).getName().equals("Exchange Sword R3"))
                USER.setR3ExchangeSwords(USER.getR3ExchangeSwords() + items.get(0).getQuantity());
            if (items.get(0).getName().equals("Exchange Sword R4"))
                USER.setR4ExchangeSwords(USER.getR4ExchangeSwords() + items.get(0).getQuantity());
            if (items.get(0).getName().equals("Rainbow Essence"))
                USER.setRainbowEssence(USER.getRainbowEssence() + items.get(0).getQuantity());
        }
        itemString += items.get(0).toString() + "\n";
        imageString = items.get(0).getImagePath();
    }

    private void generateImageStrings()
    {
        for (int i = 0 ; i < 11 ; i++)
        {
            if (items.get(i).getValue() < 0)
            {
                if (items.get(i).getName().equals("Exchange Sword R2"))
                    USER.setR2ExchangeSwords(USER.getR2ExchangeSwords() + items.get(i).getQuantity());
                if (items.get(i).getName().equals("Exchange Sword R3"))
                    USER.setR3ExchangeSwords(USER.getR3ExchangeSwords() + items.get(i).getQuantity());
                if (items.get(i).getName().equals("Exchange Sword R4"))
                    USER.setR4ExchangeSwords(USER.getR4ExchangeSwords() + items.get(i).getQuantity());
                if (items.get(i).getName().equals("Rainbow Essence"))
                    USER.setRainbowEssence(USER.getRainbowEssence() + items.get(i).getQuantity());
            }
            itemString += items.get(i).toString() + "\n";
            imageStrings[i] = items.get(i).getImagePath();
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

        if (!isPlusTicket)
        {
            double exchangeSword = 5;
            double attributeItems = 63;
            double medallionsAndKey = 16;
            double stardust = 12;
            double rainbowEssence = 1;

            /* EXCHANGE SWORD R2 */
            if (d < exchangeSword)
            {
                return 1;
            }
            /* SHARD / CRYSTAL / HOLY CRYSTAL / EXP POTION */
            else if (d < exchangeSword + attributeItems)
            {
                return 2;
            }
            /* MEDALLIONS AND LV. 80 KEY */
            else if (d < exchangeSword + attributeItems + medallionsAndKey)
            {
                return 3;
            }
            /* STARDUST */
            else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust)
            {
                return 4;
            }
            /* RAINBOW ESSENCE */
            else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust + rainbowEssence)
            {
                return 5;
            }
            /* COL */
            else
            {
                return 6;
            }
        }
        else
        {
            double exchangeSwordR3 = 8;
            double exchangeSwordR4 = 1;
            double medallions = 63;
            double stardust = 24;

            /* EXCHANGE SWORD R3 */
            if (d < exchangeSwordR3)
            {
                return 1;
            }
            /* EXCHANGE SWORD R4 */
            else if (d < exchangeSwordR3 + exchangeSwordR4)
            {
                return 2;
            }
            /* MEDALLIONS */
            else if (d < exchangeSwordR3 + exchangeSwordR4 + medallions)
            {
                return 3;
            }
            /* STARDUST */
            else if (d < exchangeSwordR3 + exchangeSwordR4 + medallions + stardust)
            {
                return 4;
            }
            /* RAINBOW ESSENCE */
            else
            {
                return 5;
            }
        }
    }

    private Item getItem(int value)
    {
        Item item;
        if (!isPlusTicket)
        {
            /* EXCHANGE SWORD R2 */
            if (value == 1)
            {
                item = new Item("Exchange Sword R2", 1, -1);
            }
            /* SHARD / CRYSTAL / HOLY CRYSTAL / EXP POTION */
            else if (value == 2)
            {
                item = attributeItemsList.get(RNG.nextInt(attributeItemsList.size()));
            }
            /* MEDALLIONS AND LV. 80 KEY */
            else if (value == 3)
            {
                item = medallionsAndKeyList.get(RNG.nextInt(medallionsAndKeyList.size()));
            }
            /* STARDUST */
            else if (value == 4)
            {
                item = new Item("Stardust (Medium)", 10, 500);
            }
            /* RAINBOW ESSENCE */
            else if (value == 5)
            {
                item = new Item("Rainbow Essence", 1, -1);
            }
            /* COL */
            else
            {
                item = new Item("Col", 50000, 50000);
            }
        }
        else
        {
            /* EXCHANGE SWORD R3 */
            if (value == 1)
            {
                item = new Item("Exchange Sword R3", 1, -1);
            }
            /* EXCHANGE SWORD R4 */
            else if (value == 2)
            {
                item = new Item("Exchange Sword R4", 1, -1);
            }
            /* MEDALLIONS  */
            else if (value == 3)
            {
                item = medallionsAndKeyList.get(RNG.nextInt(medallionsAndKeyList.size()));
            }
            /* STARDUST */
            else if (value == 4)
            {
                item = new Item("Stardust (Medium)", 20, 1000);
            }
            /* RAINBOW ESSENCE */
            else
            {
                item = new Item("Rainbow Essence", 1, -1);
            }
        }

        File itemImage = new File(item.getImagePath());
        if (!itemImage.exists())
        {
            item.setImagePath("images/Items/Item.png");
        }

        giveCol(item);
        return item;
    }


    /* MENU ========================================================================================================= */


    private void buildScoutMenu()
    {
        /* GET CHEST DATA: CHEST IMAGE */
        chest();

        /* EDIT MENU */
        scoutMenu.setThumbnail(chestImage);
        if (isPlusTicket)
        {
            scoutMenu.setBannerName("Plus Ticket Scout");
        }
        else
        {
            scoutMenu.setBannerName("Normal Ticket Scout");
        }

        scoutMenu.setTotalScout(userTotalScouts);
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

        if (!generateImage)
        {
            scoutMenu.setItemString(itemString);
        }
    }

    private void chest()
    {
        if (isPlusTicket)
        {
            chestImage = Images.CHEST_BLUE.getUrl();
        }
        else
        {
            chestImage = Images.CHEST_BROWN.getUrl();
        }
    }


    /* USER DATA MODIFIERS ========================================================================================== */


    private void giveCol(Item i)
    {
        final int COL_MAX = 99999999;
        if (i.getValue() >= 0)
        {
            userColBalance += i.getValue();
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
