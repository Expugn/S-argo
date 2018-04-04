package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Exceptions.FailedToReadSettingFileException;
import io.github.spugn.Sargo.Utilities.XMLEditor;

import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class ScoutSettingsParser extends XMLEditor
{
    private final String FILE_PATH = "data/settings/Scout.xml";

    private static final String DISABLE_IMAGES = "DisableImages";
    private static final String SIMPLE_MESSAGE = "SimpleMessage";
    private static final String RARITY_STARS = "RarityStars";
    private static final String SCOUT_MASTER = "ScoutMaster";
    private static final String COPPER = "Copper";
    private static final String SILVER = "Silver";
    private static final String GOLD = "Gold";
    private static final String PLATINUM = "Platinum";
    private static final String RECORD_CRYSTAL = "RecordCrystal";
    private static final String CIRCULATING_RECORD_CRYSTAL = "CirculatingRecordCrystal";

    private static final String SCOUT_SETTINGS = "ScoutSettings";
    private static final String RATE = "rate";

    private static final boolean DEFAULT_DISABLE_IMAGES = false;
    private static final boolean DEFAULT_SIMPLE_MESSAGE = false;
    private static final boolean DEFAULT_RARITY_STARS = true;
    private static final double DEFAULT_COPPER_RATE = 0.69;
    private static final double DEFAULT_SILVER_RATE = 0.25;
    private static final double DEFAULT_GOLD_RATE = 0.04;
    private static final double DEFAULT_PLATINUM_RATE = 0.02;
    private static final List<Double> DEFAULT_RECORD_CRYSTAL = Arrays.asList(0.03, 0.37, 0.4, 0.1, 0.035, 0.035, 0.01, 0.01, 0.005, 0.005);
    private static final List<Double> DEFAULT_CIRCULATING_RECORD_CRYSTAL = Arrays.asList(0.36, 0.6, 0.025, 0.01, 0.005);

    private static boolean isDisableImages;
    private static boolean isSimpleMessage;
    private static boolean isRarityStars;
    private static String scoutMaster;
    private static double copperRate;
    private static double silverRate;
    private static double goldRate;
    private static double platinumRate;
    private static List<Double> recordCrystalRates;
    private static List<Double> circulatingRecordCrystalRates;

    public static boolean isDisableImages() { return isDisableImages; }
    public static boolean isSimpleMessage() { return isSimpleMessage; }
    public static boolean isRarityStars() { return isRarityStars; }
    public static String getScoutMaster() { return scoutMaster; }
    public static double getCopperRate() { return copperRate; }
    public static double getSilverRate() { return silverRate; }
    public static double getGoldRate() { return goldRate; }
    public static double getPlatinumRate() { return platinumRate; }
    public static List<Double> getRecordCrystalRates() { return recordCrystalRates; }
    public static List<Double> getCirculatingRecordCrystalRates() { return circulatingRecordCrystalRates; }

    public static void setDisableImages(boolean dI) { isDisableImages = dI; }
    public static void setSimpleMessage(boolean sM) { isSimpleMessage = sM; }
    public static void setRarityStars(boolean rS) { isRarityStars = rS; }
    public static void setScoutMaster(String sM) { scoutMaster = sM; }
    public static void setCopperRate(double cR) { copperRate = cR; }
    public static void setSilverRate(double sR) { silverRate = sR; }
    public static void setGoldRate(double gR) { goldRate = gR; }
    public static void setPlatinumRate(double pR) { platinumRate = pR; }
    public static void setRecordCrystalRates(List<Double> rCR) { recordCrystalRates = rCR; }
    public static void setCirculatingRecordCrystalRates(List<Double> cRCR) { circulatingRecordCrystalRates = cRCR; }

    public void reload()
    {
        try
        {
            newFile();
            read();
        }
        catch (FailedToReadSettingFileException e)
        {
            e.displayErrorMessage();
        }
    }

    private void read() throws FailedToReadSettingFileException
    {
        InputStream in;
        XMLEventReader eventReader;

        /* CREATE XMLInputFactory AND XMLEventReader */
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try { in = new FileInputStream(FILE_PATH); }
        catch (FileNotFoundException e) { throw new FailedToReadSettingFileException(); }
        try { eventReader = inputFactory.createXMLEventReader(in); }
        catch (XMLStreamException e)
        {
            try { in.close(); } catch (IOException ex) { /* IGNORED */ }
            throw new FailedToReadSettingFileException();
        }

        recordCrystalRates = new ArrayList<>();
        circulatingRecordCrystalRates = new ArrayList<>();

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
                if (event.asStartElement().getName().getLocalPart().equals(DISABLE_IMAGES))
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

                if (event.asStartElement().getName().getLocalPart().equals(SIMPLE_MESSAGE))
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

                if (event.asStartElement().getName().getLocalPart().equals(RARITY_STARS))
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

                if (event.asStartElement().getName().getLocalPart().equals(SCOUT_MASTER))
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

                if (event.asStartElement().getName().getLocalPart().equals(COPPER))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    copperRate = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart().equals(SILVER))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    silverRate = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart().equals(GOLD))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    goldRate = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart().equals(PLATINUM))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    platinumRate = Double.parseDouble(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart().equals(RECORD_CRYSTAL))
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

                if (event.asStartElement().getName().getLocalPart().equals(CIRCULATING_RECORD_CRYSTAL))
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
                }
            }
        }
    }

    private void newFile()
    {
        File commandSettingsFile = new File(FILE_PATH);

        /* FILE DOES NOT EXIST, CREATE A NEW ONE */
        if (!commandSettingsFile.exists())
        {
            try
            {
                /* INITIALIZE VARIABLES */
                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
                XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_PATH));
                XMLEventFactory eventFactory = XMLEventFactory.newInstance();

                XMLEvent end = eventFactory.createDTD("\n");
                //XMLEvent tab = eventFactory.createDTD("\t");

                /* WRITE START DOCUMENT ELEMENT */
                StartDocument startDocument = eventFactory.createStartDocument();
                eventWriter.add(startDocument);
                eventWriter.add(end);

                /* BEGIN WRITING USER CONFIG, WRITE CommandSettings START ELEMENT */
                StartElement configStartElement = eventFactory.createStartElement("", "", SCOUT_SETTINGS);
                eventWriter.add(configStartElement);
                eventWriter.add(end);

                writeNode(eventWriter, DISABLE_IMAGES, String.valueOf(DEFAULT_DISABLE_IMAGES));
                writeNode(eventWriter, SIMPLE_MESSAGE, String.valueOf(DEFAULT_SIMPLE_MESSAGE));
                writeNode(eventWriter, RARITY_STARS, String.valueOf(DEFAULT_RARITY_STARS));
                writeNode(eventWriter, SCOUT_MASTER, "");
                writeNode(eventWriter, COPPER, String.valueOf(DEFAULT_COPPER_RATE));
                writeNode(eventWriter, SILVER, String.valueOf(DEFAULT_SILVER_RATE));
                writeNode(eventWriter, GOLD, String.valueOf(DEFAULT_GOLD_RATE));
                writeNode(eventWriter, PLATINUM, String.valueOf(DEFAULT_PLATINUM_RATE));
                writeChild_Double(eventWriter, RECORD_CRYSTAL, RECORD_CRYSTAL, RATE, DEFAULT_RECORD_CRYSTAL);
                writeChild_Double(eventWriter, CIRCULATING_RECORD_CRYSTAL, CIRCULATING_RECORD_CRYSTAL, RATE, DEFAULT_CIRCULATING_RECORD_CRYSTAL);

                /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
                eventWriter.add(eventFactory.createEndElement("", "", SCOUT_SETTINGS));
                eventWriter.add(end);
                eventWriter.add(eventFactory.createEndDocument());
                eventWriter.close();
            }
            catch (FileNotFoundException | XMLStreamException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void saveFile()
    {
        try
        {
            /* INITIALIZE VARIABLES */
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(FILE_PATH));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            XMLEvent end = eventFactory.createDTD("\n");
            //XMLEvent tab = eventFactory.createDTD("\t");

            /* WRITE START DOCUMENT ELEMENT */
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            /* BEGIN WRITING USER CONFIG, WRITE CommandSettings START ELEMENT */
            StartElement configStartElement = eventFactory.createStartElement("", "", SCOUT_SETTINGS);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            writeNode(eventWriter, DISABLE_IMAGES, String.valueOf(isDisableImages));
            writeNode(eventWriter, SIMPLE_MESSAGE, String.valueOf(isSimpleMessage));
            writeNode(eventWriter, RARITY_STARS, String.valueOf(isRarityStars));
            writeNode(eventWriter, SCOUT_MASTER, scoutMaster);
            writeNode(eventWriter, COPPER, String.valueOf(copperRate));
            writeNode(eventWriter, SILVER, String.valueOf(silverRate));
            writeNode(eventWriter, GOLD, String.valueOf(goldRate));
            writeNode(eventWriter, PLATINUM, String.valueOf(platinumRate));
            recordCrystalRates.remove(0);
            writeChild_Double(eventWriter, RECORD_CRYSTAL, RECORD_CRYSTAL, RATE, recordCrystalRates);
            circulatingRecordCrystalRates.remove(0);
            writeChild_Double(eventWriter, CIRCULATING_RECORD_CRYSTAL, CIRCULATING_RECORD_CRYSTAL, RATE, circulatingRecordCrystalRates);

            /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", SCOUT_SETTINGS));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException | XMLStreamException e)
        {
            e.printStackTrace();
        }
    }
}
