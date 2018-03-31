package io.github.spugn.Sargo.TicketScout;

import io.github.spugn.Sargo.Objects.Item;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
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
 * TICKET SCOUT
 * <p>
 *     This class contains all the data and functions needed to perform
 *     a ticket scout sans all the scout exclusive stuff like item names
 *     or potential items. Scout type exclusive stuff can be found in
 *     their appropriate classes (Example: {@link Normal} or {@link Plus}).
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see Normal
 * @see Plus
 */
abstract class TicketScout
{
    /* PACKAGE-PRIVATE VARIABLES */
    List<Item> attributeItemsList;
    List<Item> medallionsAndKeyList;
    boolean SIMPLE_MESSAGE;

    /* PRIVATE VARIABLES */
    private String DISCORD_ID;
    private String imageString;
    private String imageStrings[];
    private List<Item> items;
    private File tempUserDirectory;
    private int userColBalance;
    private int userTotalScouts;
    private String itemString;

    /* PROTECTED VARIABLES */
    protected IChannel CHANNEL;
    protected String CHOICE;
    protected UserParser USER;
    protected boolean IMAGE_DISABLED;
    protected Random RNG;
    protected EmbedBuilder scoutMenu;
    protected String simpleMessage;
    protected boolean generateImage;
    protected boolean stopScout;

    /* PRIVATE VARIABLES */
    private boolean IS_RARITY_STARS;

    TicketScout(IChannel channel, String choice, String discordID)
    {
        CHANNEL = channel;
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
        //SettingsParser SETTINGS = new SettingsParser();
        USER = new UserParser(DISCORD_ID);

        /* SETTINGS */
        //IMAGE_DISABLED = SETTINGS.isDisableImages();
        //SIMPLE_MESSAGE = SETTINGS.isSimpleMessage();
        //IS_RARITY_STARS = SETTINGS.isRarityStars();
        IMAGE_DISABLED = SettingsParser.isDisableImages();
        SIMPLE_MESSAGE = SettingsParser.isSimpleMessage();
        IS_RARITY_STARS = SettingsParser.isRarityStars();

        /* USER */
        userColBalance = USER.getColBalance();
        userTotalScouts = USER.getTotalTicketScout();

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
        scoutMenu = new EmbedBuilder();
        simpleMessage = "";
        imageStrings = new String[11];
        items = new ArrayList<>();
        itemString = "";
        tempUserDirectory = new File("images/temp_" + DISCORD_ID);
        attributeItemsList = new ArrayList<>();
        medallionsAndKeyList = new ArrayList<>();

        /* ITEMS */
        initItems();
    }

    /**
     * Generates one item and the scout result image.
     */
    protected void doSinglePull()
    {
        if (generateImage && !IMAGE_DISABLED)
        {
            tempUserDirectory.mkdir();
        }

        userTotalScouts += 1;
        USER.setTotalTicketScout(userTotalScouts);

        items.add(getItem(scout()));

        generateImageString();
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawSingleScout(imageString, false, tempUserDirectory + "/results.png");
    }

    /**
     * Generates eleven items and the scout result image.
     */
    protected void doMultiPull()
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
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawMultiScout(imageStrings, false, tempUserDirectory + "/results.png");
    }

    /**
     * Generates the item images
     */
    private void generateImageString()
    {
        int rarity = 0;
        if (items.get(0).getValue() < 0)
        {
            if (items.get(0).getName().equals("Exchange Sword R2"))
            {
                USER.setR2ExchangeSwords(USER.getR2ExchangeSwords() + items.get(0).getQuantity());
                rarity = 2;
            }
            if (items.get(0).getName().equals("Exchange Sword R3"))
            {
                USER.setR3ExchangeSwords(USER.getR3ExchangeSwords() + items.get(0).getQuantity());
                rarity = 3;
            }
            if (items.get(0).getName().equals("Exchange Sword R4"))
            {
                USER.setR4ExchangeSwords(USER.getR4ExchangeSwords() + items.get(0).getQuantity());
                rarity = 4;
            }

            if (items.get(0).getName().equals("Rainbow Essence"))
                USER.setRainbowEssence(USER.getRainbowEssence() + items.get(0).getQuantity());

            if (items.get(0).getName().equals("Upgrade Crystal"))
                USER.setUpgradeCrystal(USER.getUpgradeCrystal() + items.get(0).getQuantity());

            if (items.get(0).getName().equals("Memory Fragment"))
                USER.setMemoryFragment(USER.getMemoryFragment() + items.get(0).getQuantity());

            System.out.println("quantity: " + items.get(0).getQuantity());
        }
        itemString += items.get(0).toString() + "\n";
        if (generateImage && !IMAGE_DISABLED)
            new ImageEditor().drawItemImage(items.get(0).getImagePath(), rarity, IS_RARITY_STARS, tempUserDirectory + "/temp_" + 0 + ".png");
        imageString = tempUserDirectory + "/temp_" + 0 + ".png";
    }

    /**
     * Same function as {@link #generateImageString()} but for eleven items.
     * @see #generateImageString()
     */
    private void generateImageStrings()
    {
        int rarity;
        for (int i = 0 ; i < 11 ; i++)
        {
            rarity = 0;
            if (items.get(i).getValue() < 0)
            {
                if (items.get(i).getName().equals("Exchange Sword R2"))
                {
                    items.get(i).setImagePath("images/Items/Exchange_Sword.png");
                    USER.setR2ExchangeSwords(USER.getR2ExchangeSwords() + items.get(i).getQuantity());
                    rarity = 2;
                }
                if (items.get(i).getName().equals("Exchange Sword R3"))
                {
                    items.get(i).setImagePath("images/Items/Exchange_Sword.png");
                    USER.setR3ExchangeSwords(USER.getR3ExchangeSwords() + items.get(i).getQuantity());
                    rarity = 3;
                }
                if (items.get(i).getName().equals("Exchange Sword R4"))
                {
                    items.get(i).setImagePath("images/Items/Exchange_Sword.png");
                    USER.setR4ExchangeSwords(USER.getR4ExchangeSwords() + items.get(i).getQuantity());
                    rarity = 4;
                }
                if (items.get(i).getName().equals("Rainbow Essence"))
                    USER.setRainbowEssence(USER.getRainbowEssence() + items.get(i).getQuantity());
                if (items.get(i).getName().equals("Upgrade Crystal"))
                    USER.setUpgradeCrystal(USER.getUpgradeCrystal() + items.get(i).getQuantity());
                if (items.get(i).getName().equals("Memory Fragment"))
                    USER.setMemoryFragment(USER.getMemoryFragment() + items.get(i).getQuantity());
            }
            itemString += items.get(i).toString() + "\n";
            if (generateImage && !IMAGE_DISABLED)
                new ImageEditor().drawItemImage(items.get(i).getImagePath(), rarity, IS_RARITY_STARS, tempUserDirectory + "/temp_" + i + ".png");
            imageStrings[i] = tempUserDirectory + "/temp_" + i + ".png";
        }
    }

    /**
     * Gives the user Col depending on the item rarity given.
     *
     * @param i  The {@link Item} to have it's rarity checked.
     */
    void giveCol(Item i)
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
     * Displays the scout results to the channel where the command
     * was requested and save the results to the user file afterwards.
     * If the results fail to display for any reason then the user file
     * is not saved.
     */
    void displayAndSave()
    {
        if (!SIMPLE_MESSAGE)
        {
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
                scoutMenu.appendField("- Weapon Result -", itemString, false);
            scoutMenu.withAuthorIcon(new GitHubImage("images/System/Scout_Icon.png").getURL());
            scoutMenu.withColor(255, 255, 255);
            scoutMenu.withFooterIcon(new GitHubImage("images/System/Memory_Diamond_Icon.png").getURL());
            scoutMenu.withFooterText((CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator()) + " | " + userTotalScouts + " Total Ticket Scouts");

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
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
            {
                simpleMessage += "**- Weapon Result -**" + "\n";
                simpleMessage += itemString;
            }
            simpleMessage += (CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getName() + "#" + CHANNEL.getGuild().getUserByID(Long.parseLong(DISCORD_ID)).getDiscriminator()) + " | " + userTotalScouts + " Total Ticket Scouts";

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

        USER.upgradeExchangeSwords();
        USER.saveData();
        deleteTempDirectory();
    }

    /**
     * Initializes the lists of items needed.
     *
     * @see Normal#initItems()
     * @see Plus#initItems()
     */
    protected abstract void initItems();

    /**
     * Determines the type of item granted using RNG.
     *
     * @return  The item type to be given to the user.
     * @see Normal#scout()
     * @see Plus#scout()
     */
    protected abstract int scout();

    /**
     * Returns the item type based on the integer given.
     *
     * @param value  Item type integer
     * @return  The item generated from the integer given.
     * @see Normal#getItem(int)
     * @see Plus#getItem(int)
     */
    protected abstract Item getItem(int value);

    /**
     * Sets up the title portion of the scout result UI
     * The title has information such as the scout type
     * and total ticket scouts the user has done.
     *
     * @see Normal#setupScoutMenu()
     * @see Plus#setupScoutMenu()
     */
    protected abstract void setupScoutMenu();

    /**
     * Determines how the flow of scouts will go.
     *
     * @see Normal#run()
     * @see Plus#run()
     */
    protected abstract void run();
}
