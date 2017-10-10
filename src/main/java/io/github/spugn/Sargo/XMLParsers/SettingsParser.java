package io.github.spugn.Sargo.XMLParsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class SettingsParser
{
    /* FILE PATH */
    static final String FILE_PATH = "data/Settings.xml";

    /* BOT SETTING TAGS */
    static final String TOKEN = "Token";
    static final String USE_MENTION = "UseMention";
    static final String COMMAND_PREFIX = "CommandPrefix";
    static final String DELETE_USER_MESSAGE = "DeleteUserMessage";

    static final String BOT_OWNER_ID = "BotOwnerID";
    static final String GITHUB_DATA_REPOSITORY = "GitHubDataRepository";
    static final String SECRET_WORD = "SecretWord";
    static final String DISABLE_IMAGES = "DisableImages";
    static final String MAX_SHOP_LIMIT = "MaxShopLimit";

    static final String IGNORED_CHANNEL = "iChannel";
    static final String CHANNEL_NAME = "channelName";
    static final String NO_GUI = "NoGUI";

    static final String BANNER_ID = "BannerID";
    static final String BANNER_ID2 = "BannerID2";
    static final String ID = "id";

    static final String RECORD_CRYSTAL = "RecordCrystal";
    static final String AMOUNT = "amount";
    static final String RATE = "rate";

    /* BASE RATE TAGS */
    static final String TWO_STAR_RATES = "two";
    static final String THREE_STAR_RATES = "three";
    static final String FOUR_STAR_RATES = "four";
    static final String FIVE_STAR_RATES = "five";

    private String botToken;
    private boolean useMention;
    private char commandPrefix;
    private boolean deleteUserMessage;
    private double twoRates;
    private double threeRates;
    private double fourRates;
    private double fiveRates;
    private boolean errorInRates;
    private List<Integer> goldBanners;
    private List<Integer> goldBannersv2;
    private List<Double> recordCrystalRates;
    private List<String> ignoredChannelNames;
    private String botOwnerDiscordID;
    private String gitHubRepoURL;
    private String secretWord;
    private boolean isDisableImages;
    private boolean isNoGUI;
    private int maxShopLimit;

    public SettingsParser()
    {
        readConfig();
    }

    public String getBotToken()
    {
        return botToken;
    }

    public boolean isUseMention()
    {
        return useMention;
    }

    public char getCommandPrefix()
    {
        return commandPrefix;
    }

    public boolean isDeleteUserMessage()
    {
        return deleteUserMessage;
    }

    public int getMaxShopLimit()
    {
        return maxShopLimit;
    }

    public double getTwoRates()
    {
        return twoRates;
    }

    public double getThreeRates()
    {
        return threeRates;
    }

    public double getFourRates()
    {
        return fourRates;
    }

    public double getFiveRates()
    {
        return fiveRates;
    }

    public boolean isErrorInRates()
    {
        return errorInRates;
    }

    public List<Double> getRecordCrystalRates()
    {
        return recordCrystalRates;
    }

    public List<Integer> getGoldBanners()
    {
        return goldBanners;
    }

    public List<Integer> getGoldBannersv2()
    {
        return goldBannersv2;
    }

    public List<String> getIgnoredChannelNames()
    {
        return ignoredChannelNames;
    }

    public String getGitHubRepoURL()
    {
        return gitHubRepoURL;
    }

    public String getSecretWord()
    {
        return secretWord;
    }

    public String getBotOwnerDiscordID()
    {
        return botOwnerDiscordID;
    }

    public boolean isDisableImages()
    {
        return isDisableImages;
    }

    public boolean isNoGUI()
    {
        return isNoGUI;
    }

    private void readConfig()
    {
        try
        {
            /* CREATE XMLInputFactory AND XMLEventReader */
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(FILE_PATH);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            goldBanners = new ArrayList<>();
            goldBannersv2 = new ArrayList<>();
            recordCrystalRates = new ArrayList<>();
            ignoredChannelNames = new ArrayList<>();

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    /* GET AND SAVE TOKEN */
                    if (event.isStartElement())
                    {
                        if (event.asStartElement().getName().getLocalPart().equals(TOKEN))
                        {
                            event = eventReader.nextEvent();
                            botToken = event.asCharacters().getData();
                            continue;
                        }
                    }

                    /* GET AND SAVE MENTION SETTING */
                    if (event.asStartElement().getName().getLocalPart().equals(USE_MENTION))
                    {
                        event = eventReader.nextEvent();
                        useMention = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE COMMAND PREFIX */
                    if (event.asStartElement().getName().getLocalPart().equals(COMMAND_PREFIX))
                    {
                        event = eventReader.nextEvent();
                        commandPrefix = event.asCharacters().getData().charAt(0);
                        continue;
                    }

                    /* GET AND SAVE DELETE USER MESSAGE SETTING */
                    if (event.asStartElement().getName().getLocalPart().equals(DELETE_USER_MESSAGE))
                    {
                        event = eventReader.nextEvent();
                        deleteUserMessage = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE BOT OWNER ID */
                    if (event.asStartElement().getName().getLocalPart().equals(BOT_OWNER_ID))
                    {
                        event = eventReader.nextEvent();
                        botOwnerDiscordID = event.asCharacters().getData();
                        continue;
                    }

                    /* GET AND SAVE GITHUB DATA REPOSITORY */
                    if (event.asStartElement().getName().getLocalPart().equals(GITHUB_DATA_REPOSITORY))
                    {
                        event = eventReader.nextEvent();
                        gitHubRepoURL = event.asCharacters().getData();
                        continue;
                    }

                    /* GET AND SAVE SECRET WORD */
                    if (event.asStartElement().getName().getLocalPart().equals(SECRET_WORD))
                    {
                        event = eventReader.nextEvent();
                        secretWord = event.asCharacters().getData();
                        continue;
                    }

                    /* GET AND SAVE DISABLE IMAGES */
                    if (event.asStartElement().getName().getLocalPart().equals(DISABLE_IMAGES))
                    {
                        event = eventReader.nextEvent();
                        isDisableImages = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE NO GUI */
                    if (event.asStartElement().getName().getLocalPart().equals(NO_GUI))
                    {
                        event = eventReader.nextEvent();
                        isNoGUI = Boolean.parseBoolean(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE MAX SHOP LIMIT */
                    if (event.asStartElement().getName().getLocalPart().equals(MAX_SHOP_LIMIT))
                    {
                        event = eventReader.nextEvent();
                        maxShopLimit = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE TWO STAR RATES */
                    if (event.asStartElement().getName().getLocalPart().equals(TWO_STAR_RATES))
                    {
                        event = eventReader.nextEvent();
                        twoRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE THREE STAR RATES */
                    if (event.asStartElement().getName().getLocalPart().equals(THREE_STAR_RATES))
                    {
                        event = eventReader.nextEvent();
                        threeRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE FOUR STAR RATES */
                    if (event.asStartElement().getName().getLocalPart().equals(FOUR_STAR_RATES))
                    {
                        event = eventReader.nextEvent();
                        fourRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE FIVE STAR RATES */
                    if (event.asStartElement().getName().getLocalPart().equals(FIVE_STAR_RATES))
                    {
                        event = eventReader.nextEvent();
                        fiveRates = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE GOLD BANNERS */
                    if (event.asStartElement().getName().getLocalPart().equals(BANNER_ID))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(ID))
                            {
                                goldBanners.add(Integer.parseInt(attribute.getValue()));
                            }
                        }
                    }

                    /* GET AND SAVE GOLD BANNERS V2 */
                    if (event.asStartElement().getName().getLocalPart().equals(BANNER_ID2))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(ID))
                            {
                                goldBannersv2.add(Integer.parseInt(attribute.getValue()));
                            }
                        }
                    }

                    /* GET AND SAVE IGNORED CHANNEL NAME*/
                    if (event.asStartElement().getName().getLocalPart().equals(IGNORED_CHANNEL))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(CHANNEL_NAME))
                            {
                                ignoredChannelNames.add(attribute.getValue());
                            }
                        }
                    }

                    /* GET AND SAVE DELETE USER MESSAGE SETTING */
                    if (event.asStartElement().getName().getLocalPart().equals(RECORD_CRYSTAL))
                    {
                        double rate = 0.0;

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(RATE))
                            {
                                rate = Double.parseDouble(attribute.getValue());
                            }
                        }
                        recordCrystalRates.add(rate);
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }

        checkRates();
    }

    private void checkRates()
    {
        /* CHECK SCOUT RATES */
        if (twoRates + threeRates + fourRates + fiveRates != 1)
        {
            errorInRates = true;
            return;
        }

        /* CHECK RECORD CRYSTAL RATES */
        double rcRates = 0.0;
        for (double d : recordCrystalRates)
        {
            rcRates += d;
        }
        if (rcRates != 1)
        {
            errorInRates = true;
            return;
        }
    }
}
