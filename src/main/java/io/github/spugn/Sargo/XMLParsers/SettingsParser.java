package io.github.spugn.Sargo.XMLParsers;

import org.apache.commons.io.IOUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * SETTINGS PARSER
 * <p>
 *     This class reads the Settings.xml file and saves the data
 *     to variables.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 */
public class SettingsParser
{
    /* FILE PATH */
    private static final String FILE_PATH = "data/Settings.xml";

    /* BOT SETTINGS */
    private String botToken;
    private boolean isNoGUI;
    private String gitHubRepoURL;
    private String botOwnerDiscordID;

    /* COMMAND SETTINGS */
    private boolean useMention;
    private char commandPrefix;
    private boolean deleteUserMessage;
    private List<String> ignoredChannelNames;

    /* SCOUT SETTINGS */
    private boolean isDisableImages;
    private boolean isSimpleMessage;
    private boolean isRarityStars;
    private String scoutMaster;
    private double copperRates;
    private double silverRates;
    private double goldRates;
    private double platinumRates;
    private List<Double> recordCrystalRates;
    private List<Integer> goldBanners;
    private List<Integer> goldBannersv2;

    /* SHOP SETTINGS */
    private int maxShopLimit;
    private SortedMap<String, SortedMap<Double, Integer>> shopItems;

    public SettingsParser()
    {
        readConfig();
    }

    public String getBotToken() { return botToken; }
    public boolean isNoGUI() { return isNoGUI; }
    public String getGitHubRepoURL() { return gitHubRepoURL; }
    public String getBotOwnerDiscordID() { return botOwnerDiscordID; }

    public boolean isUseMention() { return useMention; }
    public char getCommandPrefix() { return commandPrefix; }
    public boolean isDeleteUserMessage() { return deleteUserMessage; }
    public List<String> getIgnoredChannelNames() { return ignoredChannelNames; }

    public boolean isDisableImages() { return isDisableImages; }
    public boolean isSimpleMessage() { return isSimpleMessage; }
    public boolean isRarityStars() { return  isRarityStars; }
    public String getScoutMaster() { return scoutMaster; }
    public double getCopperRates() { return copperRates; }
    public double getSilverRates() { return silverRates; }
    public double getGoldRates() { return goldRates; }
    public double getPlatinumRates() { return platinumRates; }
    public List<Double> getRecordCrystalRates() { return recordCrystalRates; }
    public List<Integer> getGoldBanners() { return goldBanners; }
    public List<Integer> getGoldBannersv2() { return goldBannersv2; }

    public int getMaxShopLimit() { return maxShopLimit; }
    public SortedMap<String, SortedMap<Double, Integer>> getShopItems() { return shopItems; }

    /**
     * Reads the data in the Settings.xml file and saves the data in variables.
     */
    @SuppressWarnings("unchecked")
    private void readConfig()
    {
        InputStream in = null;
        XMLEventReader eventReader = null;
        try
        {
            /* CREATE XMLInputFactory AND XMLEventReader */
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            in = new FileInputStream(FILE_PATH);
            eventReader = inputFactory.createXMLEventReader(in);

            ignoredChannelNames = new ArrayList<>();
            recordCrystalRates = new ArrayList<>();
            goldBanners = new ArrayList<>();
            goldBannersv2 = new ArrayList<>();
            shopItems = new TreeMap<>();

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    /* BOT SETTINGS */
                    if (event.asStartElement().getName().getLocalPart().equals("Token"))
                    {
                        event = eventReader.nextEvent();
                        botToken = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("NoGUI"))
                    {
                        event = eventReader.nextEvent();
                        isNoGUI = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("GitHubDataRepository"))
                    {
                        event = eventReader.nextEvent();
                        gitHubRepoURL = event.asCharacters().getData();
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("BotOwnerID"))
                    {
                        event = eventReader.nextEvent();
                        botOwnerDiscordID = event.asCharacters().getData();
                        continue;
                    }

                    /* COMMAND SETTINGS */
                    if (event.asStartElement().getName().getLocalPart().equals("UseMention"))
                    {
                        event = eventReader.nextEvent();
                        useMention = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("CommandPrefix"))
                    {
                        event = eventReader.nextEvent();
                        commandPrefix = event.asCharacters().getData().charAt(0);
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("DeleteUserMessage"))
                    {
                        event = eventReader.nextEvent();
                        deleteUserMessage = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("iChannel"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("channelName"))
                            {
                                ignoredChannelNames.add(attribute.getValue());
                            }
                        }
                        continue;
                    }

                    /* SCOUT SETTINGS */
                    if (event.asStartElement().getName().getLocalPart().equals("DisableImages"))
                    {
                        event = eventReader.nextEvent();
                        isDisableImages = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("SimpleMessage"))
                    {
                        event = eventReader.nextEvent();
                        isSimpleMessage = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("RarityStars"))
                    {
                        event = eventReader.nextEvent();
                        isRarityStars = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ScoutMaster"))
                    {
                        event = eventReader.nextEvent();
                        try
                        {
                            scoutMaster = event.asCharacters().getData();
                        }
                        catch (ClassCastException e)
                        {
                            scoutMaster = "";
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("copper"))
                    {
                        event = eventReader.nextEvent();
                        copperRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("silver"))
                    {
                        event = eventReader.nextEvent();
                        silverRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("gold"))
                    {
                        event = eventReader.nextEvent();
                        goldRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("platinum"))
                    {
                        event = eventReader.nextEvent();
                        platinumRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("RecordCrystal"))
                    {
                        double rate = 0.0;

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("rate"))
                            {
                                rate = Double.parseDouble(attribute.getValue());
                            }
                        }
                        recordCrystalRates.add(rate);
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("BannerID"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("id"))
                            {
                                goldBanners.add(Integer.parseInt(attribute.getValue()));
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("BannerID2"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("id"))
                            {
                                goldBannersv2.add(Integer.parseInt(attribute.getValue()));
                            }
                        }
                        continue;
                    }

                    /* SHOP SETTINGS */
                    if (event.asStartElement().getName().getLocalPart().equals("MaxShopLimit"))
                    {
                        event = eventReader.nextEvent();
                        maxShopLimit = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("shopItem"))
                    {
                        SortedMap<Double, Integer> priceAndAmount = new TreeMap<>();
                        String itemName = "";
                        Double price = 0.0;
                        int amount = 0;

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("itemName"))
                            {
                                itemName = attribute.getValue();
                            }
                            if (attribute.getName().toString().equals("price"))
                            {
                                price = Double.parseDouble(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals("amount"))
                            {
                                amount = Integer.parseInt(attribute.getValue());
                            }
                        }
                        priceAndAmount.put(price, amount);
                        shopItems.put(itemName, priceAndAmount);
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[SettingsParser] - File Not Found Exception");
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            System.out.println("[SettingsParser] - XML Stream Exception");
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(in);
            try { if (eventReader != null) { eventReader.close(); } }
                catch (XMLStreamException e) { /* IGNORED */ }
        }
    }
}
