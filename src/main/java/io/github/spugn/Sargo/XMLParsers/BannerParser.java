package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Weapon;
import org.apache.commons.io.IOUtils;

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

/**
 * BANNER PARSER
 * <p>
 *     This class reads and returns the data found in 'data/Banners.xml'
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 */
public class BannerParser
{
    /**
     * Reads the banners.xml file and returns a List of Banner objects.
     *
     * @param configFile  Path where the file is located.
     * @return  List of Banner objects.
     */
    @SuppressWarnings("unchecked")
    public List<Banner> readConfig(String configFile)
    {
        List<Banner> banners = new ArrayList();
        InputStream in = null;
        XMLEventReader eventReader = null;
        try
        {
            Banner banner = null;
            ArrayList<Character> characters = null;
            Character character;

            ArrayList<Weapon> weapons = null;
            Weapon weapon;

            /* CREATE XMLInputFactory AND XMLEventReader */
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            in = new FileInputStream(configFile);
            eventReader = inputFactory.createXMLEventReader(in);

            /* READ XML FILE */
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement())
                {
                    StartElement startElement = event.asStartElement();

                    /* CREATE NEW BANNER AND CHARACTER LIST OBJECT */
                    if (startElement.getName().getLocalPart().equals("Banner"))
                    {
                        banner = new Banner();
                        characters = new ArrayList();
                        weapons = new ArrayList<>();

                        /* GET AND SAVE BANNER ID */
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("id"))
                            {
                                banner.setBannerID(Integer.parseInt(attribute.getValue()));
                            }
                        }
                    }

                    /* GET AND SAVE BANNER NAME */
                    if (event.isStartElement())
                    {
                        if (event.asStartElement().getName().getLocalPart().equals("name"))
                        {
                            event = eventReader.nextEvent();
                            banner.setBannerName(event.asCharacters().getData());
                            continue;
                        }
                    }

                    /* GET AND SAVE BANNER TYPE */
                    if (event.asStartElement().getName().getLocalPart().equals("type"))
                    {
                        event = eventReader.nextEvent();
                        banner.setBannerType(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }

                    /* GET AND SAVE WEAPON BANNER TYPE */
                    if (event.asStartElement().getName().getLocalPart().equals("wepType"))
                    {
                        event = eventReader.nextEvent();
                        banner.setBannerWepType(Integer.parseInt(event.asCharacters().getData()));
                        continue;
                    }

                    /* GET AND SAVE CHARACTER */
                    if (event.asStartElement().getName().getLocalPart().equals("Character"))
                    {
                        character = new Character();

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("prefix"))
                            {
                                character.setPrefix(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals("char"))
                            {
                                character.setName(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals("rarity"))
                            {
                                character.setRarity(Integer.parseInt(attribute.getValue()));
                            }
                        }

                        /* GENERATE IMAGE FILE PATH*/
                        String characterImage = character.getPrefix() + " " + character.getName();
                        character.setImagePath("images/Characters/" + characterImage.replaceAll(" ", "_") + ".png");

                        /* ADD CHARACTER TO CHARACTER LIST */
                        characters.add(character);
                    }

                    /* GET AND SAVE WEAPON */
                    if (event.asStartElement().getName().getLocalPart().equals("Weapon"))
                    {
                        weapon = new Weapon();

                        Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                        while (attributes.hasNext())
                        {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("name"))
                            {
                                weapon.setName(attribute.getValue());
                            }
                            if (attribute.getName().toString().equals("rarity"))
                            {
                                weapon.setRarity(Integer.parseInt(attribute.getValue()));
                            }
                        }

                        /* GENERATE IMAGE FILE PATH*/
                        weapon.setImagePath("images/Weapons/" + weapon.getName().replaceAll(" ", "_") + ".png");

                        /* ADD WEAPON TO WEAPON LIST */
                        weapons.add(weapon);
                    }
                }

                /* END OF BANNER ELEMENT. FINALIZE CHARACTER LIST AND ADD OBJECT TO ArrayList */
                if (event.isEndElement())
                {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals("Banner"))
                    {
                        banner.setCharacters(characters);
                        banner.setWeapons(weapons);
                        banners.add(banner);
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("[BannerParser] - File Not Found Exception");
            e.printStackTrace();
        }
        catch (XMLStreamException e)
        {
            System.out.println("[BannerParser] - XML Stream Exception");
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            System.out.println("[BannerParser] - Null Pointer Exception");
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(in);
            try { if (eventReader != null) { eventReader.close(); } }
                catch (XMLStreamException e) { /* IGNORED */ }
        }
        return banners;
    }
}
