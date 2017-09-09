package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BannerParser
{
    /* BANNER OBJECT TAGS */
    static final String BANNER = "Banner";
    static final String ID = "id";
    static final String NAME = "name";
    static final String TYPE = "type";

    /* CHARACTER OBJECT TAGS */
    static final String CHARACTER = "Character";
    static final String PREFIX = "prefix";
    static final String CHARACTER_NAME = "char";
    static final String RARITY = "rarity";

    public List<Banner> readConfig(String configFile)
    {
        List<Banner> banners = new ArrayList();
        try
        {
            Banner banner = null;
            ArrayList<Character> characters = null;
            Character character = null;

            /* CREATE XMLInputFactory AND XMLEventReader */
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    StartElement startElement = event.asStartElement();

                    /* CREATE NEW BANNER AND CHARACTER LIST OBJECT */
                    if (startElement.getName().getLocalPart().equals(BANNER))
                    {
                        banner = new Banner();
                        characters = new ArrayList();

                        /* GET AND SAVE BANNER ID */
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(ID))
                            {
                                banner.setBannerID(attribute.getValue());
                            }
                        }
                    }

                    /* GET AND SAVE BANNER NAME */
                    if (event.isStartElement())
                    {
                        if (event.asStartElement().getName().getLocalPart().equals(NAME))
                        {
                            event = eventReader.nextEvent();
                            banner.setBannerName(event.asCharacters().getData());
                            continue;
                        }
                    }

                    /* GET AND SAVE BANNER TYPE */
                    if (event.asStartElement().getName().getLocalPart().equals(TYPE))
                    {
                        event = eventReader.nextEvent();
                        banner.setBannerType(event.asCharacters().getData());
                        continue;
                    }

                    /* GET AND SAVE CHARACTER */
                    if (event.asStartElement().getName().getLocalPart().equals(CHARACTER))
                    {
                        character = new Character();

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(PREFIX))
                            {
                                character.setPrefix(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals(CHARACTER_NAME))
                            {
                                character.setName(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals(RARITY))
                            {
                                character.setRarity(attribute.getValue());
                            }
                        }

                        /* GENERATE IMAGE FILE PATH*/
                        character.setImagePath("images/Characters/" + banner.getBannerName() + "/" + character.getPrefix().replaceAll(" ", "_") + ".png");

                        /* ADD CHARACTER TO CHARACTER LIST */
                        characters.add(character);
                    }
                }

                /* END OF BANNER ELEMENT. FINALIZE CHARACTER LIST AND ADD OBJECT TO ArrayList */
                if (event.isEndElement())
                {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(BANNER))
                    {
                        banner.setCharacters(characters);
                        banners.add(banner);
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
        return banners;
    }
}
