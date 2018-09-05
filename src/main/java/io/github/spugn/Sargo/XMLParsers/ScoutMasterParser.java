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
    private boolean saoArgoOnly = false;

    private String botName;
    private String image_sao_smile_URL;   //sao-smile
    private String image_sao_grin_URL;   //sao-grin
    private String image_sao_smug_URL;   //sao-smug
    private String image_sao_flowers_URL;   //sao-flowers
    private String image_sao_stars_URL;   //sao-stars

    private String image_alo_smile_URL;   //alo-smile
    private String image_alo_grin_URL;   //alo-grin
    private String image_alo_smug_URL;   //alo-smug
    private String image_alo_flowers_URL;   //alo-flowers
    private String image_alo_stars_URL;   //alo-stars
    private List<String> quotes;

    /**
     * Determines if the file exists and if it does it will continue reading.
     * If not, then the defaults will be used instead.
     */
    public ScoutMasterParser()
    {
        useDefaults = false;
        quotes = new ArrayList<>();
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
    public String getImage(int highestRarity, boolean saoArgoOnly)
    {
        this.saoArgoOnly = saoArgoOnly;
        return getImage(highestRarity);
    }
    public String getImage(int highestRarity)
    {
        double randNum = new Random().nextDouble();
        boolean sao_or_alo = (saoArgoOnly) || (new Random().nextBoolean());    // true = sao, false = alo
        switch (highestRarity)
        {
            case 2:
                return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;

            case 3:
                if (randNum < 0.5)
                {
                    return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;
                }
                else
                {
                    return (sao_or_alo) ? image_sao_grin_URL : image_alo_grin_URL;
                }

            case 4:
                if (randNum < (0.25))
                {
                    return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;
                }
                else if (randNum < (0.25 * 2))
                {
                    return (sao_or_alo) ? image_sao_grin_URL : image_alo_grin_URL;
                }
                else
                {
                    return (sao_or_alo) ? image_sao_smug_URL : image_alo_smug_URL;
                }

            case 5:
                if (randNum < (0.1666))
                {
                    return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;
                }
                else if (randNum < (0.1666 * 2))
                {
                    return (sao_or_alo) ? image_sao_grin_URL : image_alo_grin_URL;
                }
                else if (randNum < (0.1666 * 3))
                {
                    return (sao_or_alo) ? image_sao_smug_URL : image_alo_smug_URL;
                }
                else
                {
                    return (sao_or_alo) ? image_sao_flowers_URL : image_alo_flowers_URL;
                }
            case 6:
                if (randNum < (0.125))
                {
                    return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;
                }
                else if (randNum < (0.125 * 2))
                {
                    return (sao_or_alo) ? image_sao_grin_URL : image_alo_grin_URL;
                }
                else if (randNum < (0.125 * 3))
                {
                    return (sao_or_alo) ? image_sao_smug_URL : image_alo_smug_URL;
                }
                else if (randNum < (0.125 * 4))
                {
                    return (sao_or_alo) ? image_sao_flowers_URL : image_alo_flowers_URL;
                }
                else
                {
                    return (sao_or_alo) ? image_sao_stars_URL : image_alo_stars_URL;
                }
            default:
                return (sao_or_alo) ? image_sao_smile_URL : image_alo_smile_URL;
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

        //image_sao_smile_URL = new GitHubImage("images/System/Argo_Smile.png").getURL();
        //image_sao_grin_URL = new GitHubImage("images/System/Argo_Grin.png").getURL();
        //image_sao_smug_URL = new GitHubImage("images/System/Argo_Smug.png").getURL();
        //image_sao_flowers_URL = new GitHubImage("images/System/Argo_Flowers.png").getURL();

        image_sao_smile_URL = new GitHubImage("images/System/Argo_SAO_Smile.png").getURL();
        image_sao_grin_URL = new GitHubImage("images/System/Argo_SAO_Grin.png").getURL();
        image_sao_smug_URL = new GitHubImage("images/System/Argo_SAO_Smug.png").getURL();
        image_sao_flowers_URL = new GitHubImage("images/System/Argo_SAO_Flowers.png").getURL();
        image_sao_stars_URL = new GitHubImage("images/System/Argo_SAO_Stars.png").getURL();

        image_alo_smile_URL = new GitHubImage("images/System/Argo_ALO_Smile.png").getURL();
        image_alo_grin_URL = new GitHubImage("images/System/Argo_ALO_Grin.png").getURL();
        image_alo_smug_URL = new GitHubImage("images/System/Argo_ALO_Smug.png").getURL();
        image_alo_flowers_URL = new GitHubImage("images/System/Argo_ALO_Flowers.png").getURL();
        image_alo_stars_URL = new GitHubImage("images/System/Argo_ALO_Stars.png").getURL();

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
                    if (event.asStartElement().getName().getLocalPart().equals("SAOSmile"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_sao_smile_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("SAOGrin"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_sao_grin_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("SAOSmug"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_sao_smug_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("SAOFlowers"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_sao_flowers_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("SAOStars"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_sao_stars_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ALOSmile"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_alo_smile_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ALOGrin"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_alo_grin_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ALOSmug"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_alo_smug_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ALOFlowers"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_alo_flowers_URL = attribute.getValue();
                            }
                        }
                        continue;
                    }
                    if (event.asStartElement().getName().getLocalPart().equals("ALOStars"))
                    {
                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("imageURL"))
                            {
                                image_alo_stars_URL = attribute.getValue();
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
