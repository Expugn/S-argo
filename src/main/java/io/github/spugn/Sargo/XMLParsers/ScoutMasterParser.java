package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Utilities.GitHubImage;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * SCOUT MASTER PARSER
 * <p>
 *     This class reads and returns the data found in the ScoutMaster file
 *     as set in the Settings.xml file. If the file cannot be found or there
 *     is a related error, defaults will be used instead.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 */
public class ScoutMasterParser
{
    private String scoutMasterName;
    private boolean useDefaults;

    private String botName;
    private String image1URL;
    private String image2URL;
    private String image3URL;
    private String image4URL;
    private List<String> quotes;

    /**
     * Determines if the file exists and if it does it will continue reading.
     * If not, then the defaults will be used instead.
     */
    public ScoutMasterParser()
    {
        useDefaults = false;
        quotes = new ArrayList<>();
        //scoutMasterName = new SettingsParser().getScoutMaster();
        //scoutMasterName = SettingsParser.getScoutMaster();
        scoutMasterName = ScoutSettingsParser.getScoutMaster();

        if (!(new File("data/mods/" + scoutMasterName + ".xml").exists()))
            useDefaults = true;

        if (!useDefaults)
            readConfig();

        if (useDefaults)
            initDefaults();
    }

    /**
     * Returns the bot name.
     * @return  The bot name.
     */
    public String getBotName()
    {
        return botName;
    }

    /**
     * Returns an image url for the scout thumbnail depending on the rarity level given.
     *
     * @param highestRarity  Rarity of the image that should be provided.
     * @return  Image URL
     */
    public String getImage(int highestRarity)
    {
        double randNum = new Random().nextDouble();
        switch (highestRarity)
        {
            case 2:
                return image1URL;

            case 3:
                if (randNum < 0.5)
                {
                    return image1URL;
                }
                else
                {
                    return image2URL;
                }

            case 4:
                if (randNum < 0.1)
                {
                    return image1URL;
                }
                else if (randNum < 0.35)
                {
                    return image2URL;
                }
                else
                {
                    return image3URL;
                }

            case 5:
                if (randNum < 0.1)
                {
                    return image1URL;
                }
                else if (randNum < 0.2)
                {
                    return image2URL;
                }
                else if (randNum < 0.4)
                {
                    return image3URL;
                }
                else
                {
                    return image4URL;
                }

            default:
                return image1URL;
        }
    }

    /**
     * Returns a random quote from the list in the ScoutMaster file.
     * @return  String of text that should be displayed in the scout result.
     */
    public String getQuote()
    {
        return quotes.get(new Random().nextInt(quotes.size()));
    }

    /**
     * Initializes default data that is used if there is an error when
     * parsing the ScoutMaster file.
     */
    private void initDefaults()
    {
        botName = "S'argo";

        image1URL = new GitHubImage("images/System/Argo_Smile.png").getURL();
        image2URL = new GitHubImage("images/System/Argo_Grin.png").getURL();
        image3URL = new GitHubImage("images/System/Argo_Smug.png").getURL();
        image4URL = new GitHubImage("images/System/Argo_Flowers.png").getURL();

        quotes.add("*\"Lookin' for good allies, huh?\nTry the Teleport Plaza.\"*");
        quotes.add("*\"So you wanna make friends, eh?\nTry the Teleport Plaza.*\"");
        quotes.add("*\"たくさん強い仲間が欲しいの力。\n今から転移門広場に行きナ。*\"");
        quotes.add("*\"オッケー。 力強い仲間カ。\n今から転移門広場に行きナ。*\"");
    }

    /**
     * Reads the ScoutMaster file and saves the data found to variables.
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
            in = new FileInputStream("data/mods/" + scoutMasterName + ".xml");
            eventReader = inputFactory.createXMLEventReader(in);

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    if (event.asStartElement().getName().getLocalPart().equals("BotName"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("name"))
                            {
                                botName = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("Smile"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image1URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("Grin"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image2URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("Smug"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image3URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("Flowers"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image4URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("Quote"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("say"))
                            {
                                quotes.add("*\"" + attribute.getValue() + "\"*");
                            }
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException | XMLStreamException e)
        {
            useDefaults = true;
        }
        finally
        {
            try { if (in != null) {in.close();} }
                catch (IOException e) { /* IGNORED */ }

            try { if (eventReader != null) { eventReader.close(); } }
                catch (XMLStreamException e) { /* IGNORED */ }
        }
    }
}
