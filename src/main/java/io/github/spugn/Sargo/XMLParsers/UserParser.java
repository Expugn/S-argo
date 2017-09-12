package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Files;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.*;

public class UserParser
{
    private String FILE_PATH;
    private String USER_DIR_FILE_PATH;

    /* DATA TAGS */
    static final String MEMORY_DIAMONDS = "memoryDiamonds";
    static final String MONEY_SPENT = "moneySpent";
    static final String HACKING_CRYSTALS = "hackingCrystals";
    static final String BANNER = "Banner";
    static final String CHARACTER = "Character";

    static final String CHARACTER_BOX = "characterBox";
    static final String BANNER_DATA = "bannerData";

    static final String USER = "user";

    static final String CHARACTER_PREFIX = "prefix";
    static final String CHARACTER_NAME = "name";
    static final String CHARACTER_RARITY = "rarity";

    static final String B_NAME = "name";
    static final String B_DATA = "data";

    static final String DEFAULT_MEMORY_DIAMONDS = "0";
    static final String DEFAULT_MONEY_SPENT = "0";
    static final String DEFAULT_HACKING_CRYSTALS = "0";
    static final String DEFAULT_STEP = "1";
    static final String DEFAULT_RECORD_CRYSTAL = "0";

    /* USER DATA */
    private int memoryDiamonds;
    private double moneySpent;
    private int hackingCrystals;
    private SortedMap<String, Integer> bannerInfo;
    private List<Character> characterBox;
    private int cC;
    private int sC;
    private int gC;
    private int pC;

    public UserParser(String discordID)
    {
        bannerInfo = new TreeMap<>();
        characterBox = new ArrayList<>();

        FILE_PATH = "data/Users/USER_" + discordID + ".xml";
        USER_DIR_FILE_PATH = "data/Users";

        cC = 0;
        sC = 0;
        gC = 0;
        pC = 0;

        makeDirectory();
        createNewUser();
        readConfig();
    }

    public int getMemoryDiamonds()
    {
        return memoryDiamonds;
    }

    public double getMoneySpent()
    {
        return moneySpent;
    }

    public int getHackingCrystals()
    {
        return hackingCrystals;
    }

    public int getBannerData(String bannerName)
    {
        for(final Map.Entry<String, Integer> e : bannerInfo.entrySet())
        {
            if (e.getKey().equals(bannerName))
            {
                return e.getValue();
            }
        }

        /* REQUESTED BANNER NOT FOUND */
        return -1;
    }

    public List<Character> getCharacterBox()
    {
        return characterBox;
    }

    public int getCopperCount()
    {
        return cC;
    }

    public int getSilverCount()
    {
        return sC;
    }

    public int getGoldCount()
    {
        return gC;
    }

    public int getPlatinumCount()
    {
        return pC;
    }

    public void setMemoryDiamonds(int memoryDiamonds)
    {
        this.memoryDiamonds = memoryDiamonds;
    }

    public void setMoneySpent(double moneySpent)
    {
        this.moneySpent = moneySpent;
    }

    public void setHackingCrystals(int hackingCrystals)
    {
        this.hackingCrystals = hackingCrystals;
    }

    public void changeValue(String bannerName, int newValue)
    {
        bannerInfo.put(bannerName, newValue);
    }

    public void addCharacter(Character character)
    {
        characterBox.add(character);
    }

    private void makeDirectory()
    {
        File userDir = new File(USER_DIR_FILE_PATH);

        if (!userDir.exists())
        {
            userDir.mkdir();
        }
    }

    private void readConfig()
    {
        try
        {
            /* CREATE XMLInputFactory AND XMLEventReader */
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(FILE_PATH);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    /* GET AND SAVE MEMORY DIAMOND COUNT */
                    if (event.asStartElement().getName().getLocalPart().equals(MEMORY_DIAMONDS))
                    {
                        event = eventReader.nextEvent();
                        memoryDiamonds = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE MONEY SPENT */
                    if (event.asStartElement().getName().getLocalPart().equals(MONEY_SPENT))
                    {
                        event = eventReader.nextEvent();
                        moneySpent = Double.parseDouble(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE HACKING CRYSTAL COUNT */
                    if (event.asStartElement().getName().getLocalPart().equals(HACKING_CRYSTALS))
                    {
                        event = eventReader.nextEvent();
                        hackingCrystals = Integer.parseInt(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE BANNER INFO */
                    if (event.asStartElement().getName().getLocalPart().equals(BANNER))
                    {
                        String bannerName = "";
                        int bannerData = 0;

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(B_NAME))
                            {
                                bannerName = attribute.getValue();
                            }
                            if (attribute.getName().toString().equals(B_DATA))
                            {
                                bannerData = Integer.parseInt(attribute.getValue());
                            }
                        }
                        bannerInfo.put(bannerName, bannerData);
                    }

                    /* GET AND SAVE CHARACTER BOX */
                    if (event.asStartElement().getName().getLocalPart().equals(CHARACTER))
                    {
                        Character character = new Character();

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(CHARACTER_PREFIX))
                            {
                                character.setPrefix(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals(CHARACTER_NAME))
                            {
                                character.setName(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals(CHARACTER_RARITY))
                            {
                                character.setRarity(attribute.getValue());
                            }
                        }

                        /* GENERATE IMAGE FILE PATH */
                        character.setImagePath(Files.GRAY_PLACEHOLDER.get());

                        /* ADD CHARACTER TO CHARACTER LIST */
                        characterBox.add(character);
                    }
                }
            }
            /* GO THROUGH CHARACTER BOX AND COUNT CHARACTERS */
            if (!characterBox.isEmpty())
            {
                for (Character c : characterBox)
                {
                    if (Integer.parseInt(c.getRarity()) == 5)
                    {
                        pC++;
                    }
                    else if (Integer.parseInt(c.getRarity()) == 4)
                    {
                        gC++;
                    }
                    else if (Integer.parseInt(c.getRarity()) == 3)
                    {
                        sC++;
                    }
                    else if (Integer.parseInt(c.getRarity()) == 2)
                    {
                        cC++;
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
    }

    private void createNewUser()
    {
        File userFile = new File(FILE_PATH);

        /* USER FILE DOES NOT EXIST, CREATE A NEW ONE */
        if (!userFile.exists())
        {
            try
            {
                /* INITIALIZE VARIABLES */
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_PATH));
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();

                XMLEvent end = eventFactory.createDTD("\n");
                XMLEvent tab = eventFactory.createDTD("\t");

                /* WRITE START DOCUMENT ELEMENT */
                StartDocument startDocument = eventFactory.createStartDocument();
                eventWriter.add(startDocument);
                eventWriter.add(end);

                /* BEGIN WRITING USER CONFIG, WRITE user START ELEMENT */
                StartElement configStartElement = eventFactory.createStartElement("", "", USER);
                eventWriter.add(configStartElement);
                eventWriter.add(end);

                /* WRITE DEFAULT DATA FOR MEMORY DIAMONDS, MONEY SPENT, AND HACKING CRYSTALS */
                writeNode(eventWriter, MEMORY_DIAMONDS, DEFAULT_MEMORY_DIAMONDS);
                writeNode(eventWriter, MONEY_SPENT, DEFAULT_MONEY_SPENT);
                writeNode(eventWriter, HACKING_CRYSTALS, DEFAULT_HACKING_CRYSTALS);

                /* WRITE bannerData ELEMENT AND FILL WITH BANNER DATA */
                writeDefaultBannerData(eventWriter);

                /* WRITE characterBox START NODE*/
                eventWriter.add(tab);
                StartElement sElement = eventFactory.createStartElement("", "", CHARACTER_BOX);
                eventWriter.add(sElement);
                eventWriter.add(end);

                /* CLOSE CHARACTER BOX ELEMENT */
                eventWriter.add(tab);
                EndElement eElement = eventFactory.createEndElement("", "", CHARACTER_BOX);
                eventWriter.add(eElement);
                eventWriter.add(end);


                /* WRITE user END ELEMENT AND CLOSE WRITER */
                eventWriter.add(eventFactory.createEndElement("", "", USER));
                eventWriter.add(end);
                eventWriter.add(eventFactory.createEndDocument());
                eventWriter.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (XMLStreamException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void writeDefaultBannerData(XMLEventWriter eventWriter) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig(Files.BANNER_XML.get());

        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", BANNER_DATA);
        eventWriter.add(sElement);

        for (Banner b : banners)
        {
            /* BANNER IS NOT NORMAL */
            if (!b.getBannerType().equals("0"))
            {
                /* WRITE ELEMENT NAME, BANNER NAME, AND BANNER DATA */
                StringWriter bannerElement = new StringWriter();
                XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bannerElement);

                writer.writeEmptyElement(BANNER);
                writer.writeAttribute(B_NAME, b.getBannerName());

                /* IS STEP UP UP OR STEP UP V2 */
                if (b.getBannerType().equals("1") || b.getBannerType().equals("3"))
                {
                    writer.writeAttribute(B_DATA, DEFAULT_STEP);
                }
                /* IS RECORD CRYSTAL */
                else if (b.getBannerType().equals("2"))
                {
                    writer.writeAttribute(B_DATA, DEFAULT_RECORD_CRYSTAL);
                }

                writer.writeEndDocument();
                writer.flush();
                writer.close();

                /* START A NEW LINE AND TAB TWICE */
                eventWriter.add(end);
                eventWriter.add(tab);
                eventWriter.add(tab);

                /* WRITE BANNER DATA ELEMENT TO FILE */
                eventWriter.add(eventFactory.createDTD(bannerElement.toString()));
            }
        }

        eventWriter.add(end);
        eventWriter.add(tab);

        EndElement eElement = eventFactory.createEndElement("", "", BANNER_DATA);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    public void saveData()
    {
        try
        {
            /* INITIALIZE VARIABLES */
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_PATH));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");

            /* WRITE START DOCUMENT ELEMENT */
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            /* BEGIN WRITING USER CONFIG, WRITE user START ELEMENT */
            StartElement configStartElement = eventFactory.createStartElement("", "", USER);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            /* WRITE DATA FOR MEMORY DIAMONDS, MONEY SPENT, AND HACKING CRYSTALS */
            writeNode(eventWriter, MEMORY_DIAMONDS, String.valueOf(memoryDiamonds));
            writeNode(eventWriter, MONEY_SPENT, String.valueOf(moneySpent));
            writeNode(eventWriter, HACKING_CRYSTALS, String.valueOf(hackingCrystals));

            /* WRITE bannerData ELEMENT AND FILL WITH BANNER DATA */
            writeBannerData(eventWriter);

            /* WRITE characterBox ELEMENT AND FILL WITH CURRENT CHARACTERS */
            writeCharacterBox(eventWriter);


            /* WRITE user END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", USER));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }
    }

    private void writeNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* WRITE START NODE */
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);

        /* WRITE CONTENT */
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        /* CLOSE ELEMENT */
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    private void writeCharacterBox(XMLEventWriter eventWriter) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        StringWriter characterElement;

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* BUBBLE SORT CHARACTER ARRAY */
        Character tempCharacter;
        for (int a = 0 ; a < characterBox.size() + 1 ; a++)
        {
            for (int b = 1 ; b < characterBox.size() ; b++)
            {
                if (Integer.parseInt(characterBox.get(b-1).getRarity()) <= Integer.parseInt(characterBox.get(b).getRarity()))
                {
                    tempCharacter = characterBox.get(b-1);
                    characterBox.set(b-1, characterBox.get(b));
                    characterBox.set(b, tempCharacter);
                }
            }
        }

        /* WRITE characterBox START NODE*/
        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", CHARACTER_BOX);
        eventWriter.add(sElement);

        /* WRITE CHARACTER DATA */
        for (Character character : characterBox)
        {
            /* CREATE EMPTY ELEMENT */
            characterElement = new StringWriter();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(characterElement);

            /* WRITE ELEMENT NAME, CHARACTER PREFIX, CHARACTER NAME, AND RARITY */
            writer.writeEmptyElement(CHARACTER);
            writer.writeAttribute(CHARACTER_PREFIX, character.getPrefix());
            writer.writeAttribute(CHARACTER_NAME, character.getName());
            writer.writeAttribute(CHARACTER_RARITY, character.getRarity());
            writer.writeEndDocument();
            writer.flush();
            writer.close();

            /* START NEW LINE AND TAB TWICE */
            eventWriter.add(end);
            eventWriter.add(tab);
            eventWriter.add(tab);

            /* WRITE CHARACTER ELEMENT TO FILE */
            eventWriter.add(eventFactory.createDTD(characterElement.toString()));
        }

        /* NEW LINE AND ADD TAB */
        eventWriter.add(end);
        eventWriter.add(tab);

        /* CLOSE CHARACTER BOX ELEMENT */
        EndElement eElement = eventFactory.createEndElement("", "", CHARACTER_BOX);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    private void writeBannerData(XMLEventWriter eventWriter) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        StringWriter bannerElement;

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* WRITE bannerData START NODE*/
        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", BANNER_DATA);
        eventWriter.add(sElement);

        /* WRITE BANNER DATA DATA */
        for(final Map.Entry<String, Integer> e : bannerInfo.entrySet())
        {
            /* CREATE EMPTY ELEMENT */
            bannerElement = new StringWriter();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(bannerElement);

            /* WRITE ELEMENT NAME, BANNER NAME, AND BANNER DATA */
            writer.writeEmptyElement(BANNER);
            writer.writeAttribute(B_NAME, e.getKey());
            writer.writeAttribute(B_DATA, String.valueOf(e.getValue()));
            writer.writeEndDocument();
            writer.flush();
            writer.close();

            /* START NEW LINE AND TAB TWICE */
            eventWriter.add(end);
            eventWriter.add(tab);
            eventWriter.add(tab);

            /* WRITE BANNER DATA ELEMENT TO FILE */
            eventWriter.add(eventFactory.createDTD(bannerElement.toString()));
        }

        /* NEW LINE AND ADD TAB */
        eventWriter.add(end);
        eventWriter.add(tab);

        /* CLOSE BANNER DATA ELEMENT */
        EndElement eElement = eventFactory.createEndElement("", "", BANNER_DATA);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    public void resetUser()
    {
        try
        {
            /* INITIALIZE VARIABLES */
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_PATH));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");
            XMLEvent tab = eventFactory.createDTD("\t");

            /* WRITE START DOCUMENT ELEMENT */
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            /* BEGIN WRITING USER CONFIG, WRITE user START ELEMENT */
            StartElement configStartElement = eventFactory.createStartElement("", "", USER);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            /* WRITE DEFAULT DATA FOR MEMORY DIAMONDS, MONEY SPENT, AND HACKING CRYSTALS */
            writeNode(eventWriter, MEMORY_DIAMONDS, DEFAULT_MEMORY_DIAMONDS);
            writeNode(eventWriter, MONEY_SPENT, DEFAULT_MONEY_SPENT);
            writeNode(eventWriter, HACKING_CRYSTALS, DEFAULT_HACKING_CRYSTALS);

            /* WRITE bannerData ELEMENT AND FILL WITH BANNER DATA */
            writeDefaultBannerData(eventWriter);

            /* WRITE characterBox START NODE*/
            eventWriter.add(tab);
            StartElement sElement = eventFactory.createStartElement("", "", CHARACTER_BOX);
            eventWriter.add(sElement);
            eventWriter.add(end);

            /* CLOSE CHARACTER BOX ELEMENT */
            eventWriter.add(tab);
            EndElement eElement = eventFactory.createEndElement("", "", CHARACTER_BOX);
            eventWriter.add(eElement);
            eventWriter.add(end);


            /* WRITE user END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", USER));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            e.printStackTrace();
        }
    }
}
