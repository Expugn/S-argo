package io.github.spugn.Sargo.TicketScout;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Objects.Item;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.Utilities.ImageEditor;
import io.github.spugn.Sargo.XMLParsers.ScoutSettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

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
    TextChannel TEXT_CHANNEL;
    List<Item> attributeItemsList;
    List<Item> medallionsAndKeyList;
    boolean SIMPLE_MESSAGE;
    Consumer<EmbedCreateSpec> sMenu;

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
    protected String CHOICE;
    protected UserParser USER;
    protected boolean IMAGE_DISABLED;
    protected Random RNG;
    protected String simpleMessage;
    protected boolean generateImage;
    protected boolean stopScout;

    /* PRIVATE VARIABLES */
    private boolean IS_RARITY_STARS;

    TicketScout(Message message, String choice, String discordID)
    {
        TEXT_CHANNEL = (TextChannel) message.getChannel().block();
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
        USER = new UserParser(DISCORD_ID);

        /* SETTINGS */
        IMAGE_DISABLED = ScoutSettingsParser.isDisableImages();
        SIMPLE_MESSAGE = ScoutSettingsParser.isSimpleMessage();
        IS_RARITY_STARS = ScoutSettingsParser.isRarityStars();

        /* USER */
        userColBalance = USER.getColBalance();
        userTotalScouts = USER.getTotalTicketScout();

        /* VARIABLES */
        RNG = new Random(System.currentTimeMillis());
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
        final int COL_MAX = 999999999;
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
        Member member = TEXT_CHANNEL.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(DISCORD_ID))).block();
        String username = member.getUsername() + "#" + member.getDiscriminator();

        if (!SIMPLE_MESSAGE)
        {
            sMenu = s -> {
                s.setColor(new Color(255, 255, 255));
            };
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
                sMenu = sMenu.andThen(s -> s.addField("- Weapon Result - ", itemString, false));
            sMenu = sMenu.andThen(s -> s.setFooter(username + " | " + userTotalScouts + " Total Ticket Scouts", new GitHubImage("images/System/Memory_Diamond_Icon.png").getURL()));

            if (generateImage && !IMAGE_DISABLED)
                Sargo.sendEmbed(TEXT_CHANNEL, sMenu, new File(tempUserDirectory + "/results.png"));
            else
                Sargo.sendEmbed(TEXT_CHANNEL, sMenu);
        }
        else
        {
            setupScoutMenu();
            if (!generateImage || IMAGE_DISABLED)
            {
                simpleMessage += "**- Weapon Result -**" + "\n";
                simpleMessage += itemString;
            }
            simpleMessage += (username) + " | " + userTotalScouts + " Total Ticket Scouts";

            if (generateImage && !IMAGE_DISABLED)
            {
                Sargo.replyToMessage(TEXT_CHANNEL, simpleMessage, new File(tempUserDirectory + "/results.png"));
            }
            else
            {
                Sargo.replyToMessage(TEXT_CHANNEL, simpleMessage);
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
