package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Exceptions.FailedToReadSettingFileException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * @version 1.1
 * @since v1.0
 */
public class SettingsParser
{
    /* BOT SETTINGS */
    private static String botToken;
    private static boolean isNoGUI;
    private static String gitHubRepoURL;
    private static String botOwnerDiscordID;

    /* COMMAND SETTINGS */
    private static boolean useMention;
    private static char commandPrefix;
    private static boolean deleteUserMessage;
    private static List<String> ignoredChannelNames;

    /* SCOUT SETTINGS */
    private static boolean isDisableImages;
    private static boolean isSimpleMessage;
    private static boolean isRarityStars;
    private static String scoutMaster;
    private static double copperRates;
    private static double silverRates;
    private static double goldRates;
    private static double platinumRates;
    private static List<Double> recordCrystalRates;
    private static List<Double> circulatingRecordCrystalRates;
    private static List<Integer> goldBanners;
    private static List<Integer> goldBannersv2;

    /* SHOP SETTINGS */
    private static int maxShopLimit;
    private static SortedMap<String, SortedMap<Double, Integer>> shopItems;

    public static String getBotToken() { return botToken; }
    public static boolean isNoGUI() { return isNoGUI; }
    public static String getGitHubRepoURL() { return gitHubRepoURL; }
    public static String getBotOwnerDiscordID() { return botOwnerDiscordID; }

    public static boolean isUseMention() { return useMention; }
    public static char getCommandPrefix() { return commandPrefix; }
    public static boolean isDeleteUserMessage() { return deleteUserMessage; }
    public static List<String> getIgnoredChannelNames() { return ignoredChannelNames; }

    public static boolean isDisableImages() { return isDisableImages; }
    public static boolean isSimpleMessage() { return isSimpleMessage; }
    public static boolean isRarityStars() { return  isRarityStars; }
    public static String getScoutMaster() { return scoutMaster; }
    public static double getCopperRates() { return copperRates; }
    public static double getSilverRates() { return silverRates; }
    public static double getGoldRates() { return goldRates; }
    public static double getPlatinumRates() { return platinumRates; }
    public static List<Double> getRecordCrystalRates() { return recordCrystalRates; }
    public static List<Double> getCirculatingRecordCrystalRates() { return circulatingRecordCrystalRates; }
    public static List<Integer> getGoldBanners() { return BannerParser.getGoldBanners(); }
    public static List<Integer> getGoldBannersv2() { return BannerParser.getGoldBannersv2(); }

    public static int getMaxShopLimit() { return maxShopLimit; }
    public static SortedMap<String, SortedMap<Double, Integer>> getShopItems() { return shopItems; }

    /**
     * Reads the data in the Settings.xml file and saves the data in variables.
     */
    public void reloadSettings(String configFile)
    {
        try
        {
            tryRead(configFile);
        }
        catch (FailedToReadSettingFileException e)
        {
            e.displayErrorMessage();
        }
    }

    private void tryRead(String configFile) throws FailedToReadSettingFileException
    {
        InputStream in;
        XMLEventReader eventReader;

        /* CREATE XMLInputFactory AND XMLEventReader */
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try { in = new FileInputStream(configFile); }
        catch (FileNotFoundException e) { throw new FailedToReadSettingFileException(); }
        try { eventReader = inputFactory.createXMLEventReader(in); }
        catch (XMLStreamException e)
        {
            try { in.close(); } catch (IOException ex) { /* IGNORED */ }
            throw new FailedToReadSettingFileException();
        }


        ignoredChannelNames = new ArrayList<>();
        recordCrystalRates = new ArrayList<>();
        circulatingRecordCrystalRates = new ArrayList<>();
        goldBanners = new ArrayList<>();
        goldBannersv2 = new ArrayList<>();
        shopItems = new TreeMap<>();

        /* READ XML FILE */
        while (eventReader.hasNext())
        {
            XMLEvent event;
            try { event = eventReader.nextEvent(); }
            catch (XMLStreamException e)
            {
                try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                throw new FailedToReadSettingFileException();
            }


            if (event.isStartElement())
            {
                /* BOT SETTINGS */
                if (event.asStartElement().getName().getLocalPart().equals("Token"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    botToken = event.asCharacters().getData();
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("NoGUI"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    isNoGUI = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("GitHubDataRepository"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    gitHubRepoURL = event.asCharacters().getData();
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("BotOwnerID"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    botOwnerDiscordID = event.asCharacters().getData();
                    continue;
                }

                /* COMMAND SETTINGS */
                if (event.asStartElement().getName().getLocalPart().equals("UseMention"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    useMention = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("CommandPrefix"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    commandPrefix = event.asCharacters().getData().charAt(0);
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("DeleteUserMessage"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
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
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    isDisableImages = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("SimpleMessage"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    isSimpleMessage = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("RarityStars"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    isRarityStars = Boolean.parseBoolean(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("ScoutMaster"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
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
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    copperRates = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("silver"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    silverRates = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("gold"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    goldRates = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }
                if (event.asStartElement().getName().getLocalPart().equals("platinum"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
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
                if (event.asStartElement().getName().getLocalPart().equals("CirculatingRecordCrystal"))
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
                    circulatingRecordCrystalRates.add(rate);
                    continue;
                }
                /*
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
                */

                /* SHOP SETTINGS */
                if (event.asStartElement().getName().getLocalPart().equals("MaxShopLimit"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
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
}
