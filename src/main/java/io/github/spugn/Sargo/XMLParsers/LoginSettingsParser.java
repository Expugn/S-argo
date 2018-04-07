package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Exceptions.FailedToReadSettingFileException;
import io.github.spugn.Sargo.Utilities.XMLEditor;

import javax.xml.stream.*;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;

public class LoginSettingsParser extends XMLEditor
{
    private final String FILE_PATH = "data/settings/Login.xml";

    private static final String BOT_TOKEN = "Token";
    private static final String BOT_OWNER_ID = "BotOwnerID";
    private static final String GITHUB_DATA_REPOSITORY = "GitHubDataRepository";

    private static final String LOGIN_SETTINGS = "LoginSettings";

    private static final String DEFAULT_TOKEN = "YOUR_BOT_TOKEN_HERE";
    private static final String DEFAULT_BOT_OWNER_ID = "BOT_OWNER_DISCORD_ID_HERE";
    private static final String DEFAULT_GITHUB_DATA_REPOSITORY = "https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/";

    private static String botToken;
    private static String botOwnerDiscordID;
    private static String gitHubRepoURL;

    public static String getBotToken() { return botToken; }
    public static String getBotOwnerDiscordID() { return botOwnerDiscordID; }
    public static String getGitHubRepoURL() { return gitHubRepoURL; }

    public static void setBotToken(String bT) { botToken = bT; }
    public static void setBotOwnerDiscordID(String bODI) { botOwnerDiscordID = bODI; }
    public static void setGithubDataRepository(String ghDR) { gitHubRepoURL = ghDR; }

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
                if (event.asStartElement().getName().getLocalPart().equals(BOT_TOKEN))
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

                if (event.asStartElement().getName().getLocalPart().equals(BOT_OWNER_ID))
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

                if (event.asStartElement().getName().getLocalPart().equals(GITHUB_DATA_REPOSITORY))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    gitHubRepoURL = event.asCharacters().getData();
                }
            }
        }
    }

    public void newFile()
    {
        File loginSettingsFile = new File(FILE_PATH);

        /* FILE DOES NOT EXIST, CREATE A NEW ONE */
        if (!loginSettingsFile.exists())
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

                /* BEGIN WRITING USER CONFIG, WRITE CommandSettings START ELEMENT */
                StartElement configStartElement = eventFactory.createStartElement("", "", LOGIN_SETTINGS);
                eventWriter.add(configStartElement);
                eventWriter.add(end);

                writeNode(eventWriter, BOT_TOKEN, DEFAULT_TOKEN);
                writeNode(eventWriter, BOT_OWNER_ID, DEFAULT_BOT_OWNER_ID);
                writeNode(eventWriter, GITHUB_DATA_REPOSITORY, DEFAULT_GITHUB_DATA_REPOSITORY);

                /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
                eventWriter.add(eventFactory.createEndElement("", "", LOGIN_SETTINGS));
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

            /* WRITE START DOCUMENT ELEMENT */
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            /* BEGIN WRITING USER CONFIG, WRITE CommandSettings START ELEMENT */
            StartElement configStartElement = eventFactory.createStartElement("", "", LOGIN_SETTINGS);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            writeNode(eventWriter, BOT_TOKEN, botToken);
            writeNode(eventWriter, BOT_OWNER_ID, botOwnerDiscordID);
            writeNode(eventWriter, GITHUB_DATA_REPOSITORY, gitHubRepoURL);

            /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", LOGIN_SETTINGS));
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
