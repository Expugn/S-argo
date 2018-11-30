package io.github.spugn.Sargo.XMLParsers;

import io.github.spugn.Sargo.Exceptions.FailedToReadBannerFileException;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Weapon;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * BANNER PARSER
 * <p>
 *     This class reads and returns the data found in 'data/Banners.xml'
 * </p>
 *
 * @author S'pugn
 * @version 1.2
 * @since v1.0
 */
public class BannerParser
{
    private static List<Banner> bannerFile;
    private static List<Integer> goldBanners;
    private static List<Integer> goldBannersv2;
    private static List<Integer> platinumBanners;
    private static List<Integer> platinumBannersv2;

    // Character Blacklist String: <BANNER ID>;<PREFIX>;<NAME>;<RARITY>
    private static List<String> excludedCharacters;

    /**
     * Returns a List of Banner objects.
     *
     * @return List of Banner objects
     */
    public static List<Banner> getBanners()
    {
        return bannerFile;
    }

    public static List<Integer> getGoldBanners()
    {
        return goldBanners;
    }

    public static List<Integer> getGoldBannersv2()
    {
        return goldBannersv2;
    }

    public static List<Integer> getPlatinumBanners()
{
    return platinumBanners;
}

    public static List<Integer> getPlatinumBannersv2()
    {
        return platinumBannersv2;
    }

    public static List<String> getExcludedCharacters()
    {
        return excludedCharacters;
    }

    /**
     * index 0 = banner id
     * index 1 = character prefix
     * index 2 = character name
     * index 3 = character rarity
     */
    public static String[] parseExcludeCharacterString(String excludedCharacterString)
    {
        // SPLIT ECS
        String[] splitECS = excludedCharacterString.split(";");

        // CONVERT ANY UNDERSCORES TO SPACES FROM CHARACTER NAME/PREFIX
        splitECS[1] = splitECS[1].replaceAll("_", " ");
        splitECS[2] = splitECS[2].replaceAll("_", " ");

        return splitECS;
    }

    /**
     * Reads the banners.xml file and returns a List of Banner objects.
     *
     * @param configFile  Path where the file is located.
     * @return  List of Banner objects.
     */
    @SuppressWarnings("unchecked")
    public void reloadBanners(String configFile)
    {
        try
        {
            bannerFile = tryRead(configFile);
            return;
        }
        catch (FailedToReadBannerFileException e)
        {
            e.displayErrorMessage();
        }
        bannerFile = Collections.emptyList();
    }

    private List<Banner> tryRead(String configFile) throws FailedToReadBannerFileException
    {
        System.out.println("try read");
        List<Banner> banners = new ArrayList();
        goldBanners = new ArrayList();
        goldBannersv2 = new ArrayList();
        platinumBanners = new ArrayList();
        platinumBannersv2 = new ArrayList();
        excludedCharacters = new ArrayList();
        InputStream in;
        XMLEventReader eventReader;

        Banner banner = null;
        ArrayList<Character> characters = null;
        Character character;

        ArrayList<Weapon> weapons = null;
        Weapon weapon;

        /* CREATE XMLInputFactory AND XMLEventReader */
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try { in = new FileInputStream(configFile); }
        catch (FileNotFoundException e) { throw new FailedToReadBannerFileException(); }
        try { eventReader = inputFactory.createXMLEventReader(in); }
        catch (XMLStreamException e)
        {
            try { in.close(); } catch (IOException ex) { /* IGNORED */ }
            throw new FailedToReadBannerFileException();
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
                throw new FailedToReadBannerFileException();
            }

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
                        try { event = eventReader.nextEvent(); }
                        catch (XMLStreamException e)
                        {
                            try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                            try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                            throw new FailedToReadBannerFileException();
                        }
                        banner.setBannerName(event.asCharacters().getData());
                        continue;
                    }
                }

                /* GET AND SAVE BANNER TYPE */
                if (event.asStartElement().getName().getLocalPart().equals("type"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadBannerFileException();
                    }
                    banner.setBannerType(Integer.parseInt(event.asCharacters().getData()));
                    continue;
                }

                /* GET AND SAVE WEAPON BANNER TYPE */
                if (event.asStartElement().getName().getLocalPart().equals("wepType"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadBannerFileException();
                    }
                    banner.setBannerWepType(Integer.parseInt(event.asCharacters().getData()));
                    continue;
                }

                /* GET AND SAVE IF BANNER IS INCLUDED WITH GOLD BANNERS/GOLD BANNERS V2/ETC */
                if (event.asStartElement().getName().getLocalPart().equals("include"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadBannerFileException();
                    }
                    String includeType = event.asCharacters().getData();
                    if (includeType.equals("GoldBanners"))
                    {
                        try { goldBanners.add(banner.getBannerID()); }
                        catch (NullPointerException e) { /* IGNORED */ }
                    }
                    else if (includeType.equals("GoldBannersv2"))
                    {
                        try { goldBannersv2.add(banner.getBannerID()); }
                        catch (NullPointerException e) { /* IGNORED */ }
                    }
                    else if (includeType.equals("PlatinumBanners"))
                    {
                        try { platinumBanners.add(banner.getBannerID()); }
                        catch (NullPointerException e) { /* IGNORED */ }
                    }
                    else if (includeType.equals("PlatinumBannersv2"))
                    {
                        try { platinumBannersv2.add(banner.getBannerID()); }
                        catch (NullPointerException e) { /* IGNORED */ }
                    }
                    continue;
                }

                /* GET AND SAVE ANY EXCLUDED CHARACTERS */
                if (event.asStartElement().getName().getLocalPart().equals("exclude"))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadBannerFileException();
                    }
                    String excludedCharacter = event.asCharacters().getData();

                    // BANNERID_PREFIX_NAME_RARITY
                    try { excludedCharacters.add(banner.getBannerID() + ";" + excludedCharacter); }
                    catch (NullPointerException e) { /* IGNORED */ }

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
                    //System.out.println("added " + banner.getBannerName());
                }
            }
        }

        return banners;
    }
}
