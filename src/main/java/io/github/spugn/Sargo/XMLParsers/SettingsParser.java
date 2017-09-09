package io.github.spugn.Sargo.XMLParsers;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SettingsParser
{
    /* FILE PATH */
    static final String FILE_PATH = "data/Settings.xml";

    /* BOT SETTING TAGS */
    static final String TOKEN = "Token";
    static final String USE_MENTION = "UseMention";
    static final String COMMAND_PREFIX = "CommandPrefix";
    static final String DELETE_USER_MESSAGE = "DeleteUserMessage";

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
        if (twoRates + threeRates + fourRates + fiveRates != 1)
        {
            errorInRates = true;
        }
    }
}
