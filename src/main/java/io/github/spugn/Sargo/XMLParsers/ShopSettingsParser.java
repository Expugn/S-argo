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

public class ShopSettingsParser extends XMLEditor
{
    private final String FILE_PATH = "data/settings/Shop.xml";

    private static final String MAX_SHOP_LIMIT = "MaxShopLimit";
    private static final String SHOP_ITEM = "ShopItem";

    private static final String SHOP_SETTINGS = "ShopSettings";
    private static final String SHOP_ITEMS = "ShopItems";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String AMOUNT = "amount";

    private static final int DEFAULT_MAX_SHOP_LIMIT = 20;
    private static final Map<String, Map<Double, Integer>> DEFAULT_SHOP_ITEMS = createMap();
    private static Map<String, Map<Double, Integer>> createMap()
    {
        Map<String, Map<Double, Integer>> itemList = new TreeMap<>();

        Map<Double, Integer> item = new TreeMap<>();
        item.put(0.99, 5);
        itemList.put("Memory Diamond A", item);

        Map<Double, Integer> item2 = new TreeMap<>();
        item2.put(4.99, 25);
        itemList.put("Memory Diamond B", item2);

        Map<Double, Integer> item3 = new TreeMap<>();
        item3.put(7.99, 50);
        itemList.put("Memory Diamond C", item3);

        Map<Double, Integer> item4 = new TreeMap<>();
        item4.put(17.99, 125);
        itemList.put("Memory Diamond D", item4);

        Map<Double, Integer> item5 = new TreeMap<>();
        item5.put(33.99, 250);
        itemList.put("Memory Diamond E", item5);

        Map<Double, Integer> item6 = new TreeMap<>();
        item6.put(44.99, 360);
        itemList.put("Memory Diamond F", item6);

        Map<Double, Integer> item7 = new TreeMap<>();
        item7.put(79.99, 700);
        itemList.put("Memory Diamond G", item7);

        return itemList;
    }

    private static int maxShopLimit;
    private static Map<String, Map<Double, Integer>> shopItems;

    public static int getMaxShopLimit() { return maxShopLimit; }
    public static Map<String, Map<Double, Integer>> getShopItems() { return shopItems; }

    public static void setMaxShopLimit(int mSL) { maxShopLimit = mSL; }
    public static void setShopItems(Map<String, Map<Double, Integer>> sI) { shopItems = sI; }

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

        shopItems = new TreeMap<>();

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
                if (event.asStartElement().getName().getLocalPart().equals(MAX_SHOP_LIMIT))
                {
                    try { event = eventReader.nextEvent(); }
                    catch (XMLStreamException e)
                    {
                        try { in.close(); } catch (IOException ex) { /* IGNORED */ }
                        try { eventReader.close(); } catch (XMLStreamException ex) { /* IGNORED */ }
                        throw new FailedToReadSettingFileException();
                    }
                    maxShopLimit = Integer.parseInt(event.asCharacters().getData());
                    continue;
                }

                if (event.asStartElement().getName().getLocalPart().equals(SHOP_ITEM))
                {
                    HashMap<Double, Integer> priceAndAmount = new HashMap<>();
                    String itemName = "";
                    Double price = 0.0;
                    int amount = 0;

                    Iterator<Attribute> attributes = event.asStartElement().getAttributes();
                    while (attributes.hasNext())
                    {
                        Attribute attribute = attributes.next();
                        if (attribute.getName().toString().equals(NAME))
                        {
                            itemName = attribute.getValue();
                        }
                        if (attribute.getName().toString().equals(PRICE))
                        {
                            price = Double.parseDouble(attribute.getValue());
                        }
                        if (attribute.getName().toString().equals(AMOUNT))
                        {
                            amount = Integer.parseInt(attribute.getValue());
                        }
                    }
                    priceAndAmount.put(price, amount);
                    shopItems.put(itemName, priceAndAmount);
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
                StartElement configStartElement = eventFactory.createStartElement("", "", SHOP_SETTINGS);
                eventWriter.add(configStartElement);
                eventWriter.add(end);

                writeNode(eventWriter, MAX_SHOP_LIMIT, String.valueOf(DEFAULT_MAX_SHOP_LIMIT));
                writeChild_ItemMap(eventWriter, SHOP_ITEMS, SHOP_ITEM, NAME, PRICE, AMOUNT, DEFAULT_SHOP_ITEMS);

                /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
                eventWriter.add(eventFactory.createEndElement("", "", SHOP_SETTINGS));
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
            StartElement configStartElement = eventFactory.createStartElement("", "", SHOP_SETTINGS);
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            writeNode(eventWriter, MAX_SHOP_LIMIT, String.valueOf(maxShopLimit));
            writeChild_ItemMap(eventWriter, SHOP_ITEMS, SHOP_ITEM, NAME, PRICE, AMOUNT, shopItems);

            /* WRITE CommandSettings END ELEMENT AND CLOSE WRITER */
            eventWriter.add(eventFactory.createEndElement("", "", SHOP_SETTINGS));
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
