package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Exceptions.FailedToReadSettingFileException;
import io.github.spugn.Sargo.Utilities.XMLEditor;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandSettingsParser extends XMLEditor
{
    private final String FILE_PATH = "data/settings/Command.xml";

    private static final String COMMAND_PREFIX = "CommandPrefix";
    private static final String DELETE_USER_MESSAGE = "DeleteUserMessage";
    private static final String MAIN_CHANNEL = "MainChannel";
    private static final String BLACKLISTED_CHANNEL = "bChannel";
    private static final String WHITELISTED_CHANNEL = "wChannel";

    private static final String COMMAND_SETTINGS = "CommandSettings";
    private static final String BLACKLISTED_CHANNELS = "BlacklistedChannels";
    private static final String WHITELISTED_CHANNELS = "WhitelistedChannels";
    private static final String NAME = "name";

    private static final char DEFAULT_COMMAND_PREFIX = '$';
    private static final boolean DEFAULT_DELETE_USER_MESSAGE = false;
    private static final String DEFAULT_MAIN_CHANNEL = "";

    private static char commandPrefix;
    private static boolean deleteUserMessage;
    private static String mainChannel;
    private static List<String> blacklistedChannels;
    private static List<String> whitelistedChannels;

    public static char getCommandPrefix() { return commandPrefix; }
    public static boolean isDeleteUserMessage() { return deleteUserMessage; }
    public static String getMainChannel() { return mainChannel; }
    public static List<String> getBlacklistedChannels() { return blacklistedChannels; }
    public static List<String> getWhitelistedChannels() { return whitelistedChannels; }

    public static void setCommandPrefix(char cP) { commandPrefix = cP; }
    public static void setDeleteUserMessage(boolean dUM) { deleteUserMessage = dUM; }
    public static void setMainChannel(String mC) { mainChannel = mC; }
    public static void setBlacklistedChannels(List<String> bC) { blacklistedChannels = bC; }
    public static void setWhitelistedChannels(List<String> wC) { whitelistedChannels = wC; }

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

        blacklistedChannels = new ArrayList<>();
        whitelistedChannels = new ArrayList<>();

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
                if (event.asStartElement().getName().getLocalPart().equals(COMMAND_PREFIX))
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



                if (event.asStartElement().getName().getLocalPart().equals(DELETE_USER_MESSAGE))
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



                if (event.asStartElement().getName().getLocalPart().equals(MAIN_CHANNEL))
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
                        mainChannel = event.asCharacters().getData();
                    }
                    catch (ClassCastException e)
                    {
                        mainChannel = "";
                    }
                    continue;
                }



                if (event.asStartElement().getName().getLocalPart().equals("bChannel"))
                {
                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                    while (attributes.hasNext())
                    {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("name"))
                        {
                            blacklistedChannels.add(attribute.getValue());
                        }
                    }
                    continue;
                }



                if (event.asStartElement().getName().getLocalPart().equals("wChannel"))
                {
                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                    while (attributes.hasNext())
                    {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals("name"))
                        {
                            whitelistedChannels.add(attribute.getValue());
                        }
                    }
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
                StartElement configStartElement = eventFactory.createStartElement("", "", COMMAND_SETTINGS);
                eventWriter.add(configStartElement);
                eventWriter.add(end);

                writeNode(eventWriter, COMMAND_PREFIX, String.valueOf(DEFAULT_COMMAND_PREFIX));
                writeNode(eventWriter, DELETE_USER_MESSAGE, String.valueOf(DEFAULT_DELETE_USER_MESSAGE));
                writeNode(eventWriter, MAIN_CHANNEL, DEFAULT_MAIN_CHANNEL);
                writeChild_String(eventWriter, BLACKLISTED_CHANNELS, BLACKLISTED_CHANNEL, NAME, Collections.emptyList());
                writeChild_String(eventWriter, WHITELISTED_CHANNELS, WHITELISTED_CHANNEL, NAME, Collections.emptyList());

                /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
                eventWriter.add(eventFactory.createEndElement("", "", COMMAND_SETTINGS));
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
            StartElement configStartElement = eventFactory.createStartElement("", "", COMMAND_SETTINGS);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            writeNode(eventWriter, COMMAND_PREFIX, String.valueOf(commandPrefix));
            writeNode(eventWriter, DELETE_USER_MESSAGE, String.valueOf(deleteUserMessage));
            writeNode(eventWriter, MAIN_CHANNEL, mainChannel);
            writeChild_String(eventWriter, BLACKLISTED_CHANNELS, BLACKLISTED_CHANNEL, NAME, blacklistedChannels);
            writeChild_String(eventWriter, WHITELISTED_CHANNELS, WHITELISTED_CHANNEL, NAME, whitelistedChannels);

            /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", COMMAND_SETTINGS));
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
