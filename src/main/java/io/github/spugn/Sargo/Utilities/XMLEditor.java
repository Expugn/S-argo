package io.github.spugn.Sargo.Utilities;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class XMLEditor
{
    public void writeNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException
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

    /**
     * Generates:
     *
     * <CHILD_NAME>
     *     <SUBCHILD_NAME ATTRIBUTE_NAME="String"/>
     *     ...
     * </CHILD_NAME>
     *
     */
    public void writeChild_String(XMLEventWriter eventWriter, final String CHILD_NAME, final String SUBCHILD_NAME, final String ATTRIBUTE_NAME, List<String> stringList) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        StringWriter characterElement;

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* WRITE characterBox START NODE*/
        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", CHILD_NAME);
        eventWriter.add(sElement);

        /* WRITE CHARACTER DATA */
        for (String s : stringList)
        {
            /* CREATE EMPTY ELEMENT */
            characterElement = new StringWriter();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(characterElement);

            /* WRITE SUBCHILD */
            writer.writeEmptyElement(SUBCHILD_NAME);
            writer.writeAttribute(ATTRIBUTE_NAME, s);
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
        EndElement eElement = eventFactory.createEndElement("", "", CHILD_NAME);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    /**
     * Generates:
     *
     * <CHILD_NAME>
     *     <SUBCHILD_NAME ATTRIBUTE_NAME="Double"/>
     *     ...
     * </CHILD_NAME>
     *
     */
    public void writeChild_Double(XMLEventWriter eventWriter, final String CHILD_NAME, final String SUBCHILD_NAME, final String ATTRIBUTE_NAME, List<Double> doubleList) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        StringWriter characterElement;

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* WRITE characterBox START NODE*/
        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", CHILD_NAME);
        eventWriter.add(sElement);

        /* WRITE CHARACTER DATA */
        for (Double d : doubleList)
        {
            /* CREATE EMPTY ELEMENT */
            characterElement = new StringWriter();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(characterElement);

            /* WRITE SUBCHILD */
            writer.writeEmptyElement(SUBCHILD_NAME);
            writer.writeAttribute(ATTRIBUTE_NAME, String.valueOf(d));
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
        EndElement eElement = eventFactory.createEndElement("", "", CHILD_NAME);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    /**
     * Generates:
     *
     * <CHILD_NAME>
     *     <SUBCHILD_NAME ATTRIBUTE_NAME="ITEM_NAME" ATTRIBUTE_NAME_2="ITEM_PRICE" ATTRIBUTE_NAME_3="ITEM_AMOUNT"/>
     *     ...
     * </CHILD_NAME>
     *
     */
    public void writeChild_ItemMap(XMLEventWriter eventWriter, final String CHILD_NAME, final String SUBCHILD_NAME,
                                   final String ATTRIBUTE_NAME, final String ATTRIBUTE_NAME_2, final String ATTRIBUTE_NAME_3,
                                   Map<String, Map<Double, Integer>> itemMap) throws XMLStreamException
    {
        /* INITIALIZE VARIABLES */
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        StringWriter characterElement;

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        /* WRITE characterBox START NODE*/
        eventWriter.add(tab);
        StartElement sElement = eventFactory.createStartElement("", "", CHILD_NAME);
        eventWriter.add(sElement);

        /* WRITE CHARACTER DATA */
        for (Map.Entry<String, Map<Double, Integer>> entry : itemMap.entrySet())
        {
            /* CREATE EMPTY ELEMENT */
            characterElement = new StringWriter();
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(characterElement);

            double price = 0.0;
            int amount = 0;
            for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
            {
                price = entry2.getKey();
                amount = entry2.getValue();
            }

            /* WRITE SUBCHILD */
            writer.writeEmptyElement(SUBCHILD_NAME);
            writer.writeAttribute(ATTRIBUTE_NAME, entry.getKey());
            writer.writeAttribute(ATTRIBUTE_NAME_2, String.valueOf(price));
            writer.writeAttribute(ATTRIBUTE_NAME_3, String.valueOf(amount));
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
        EndElement eElement = eventFactory.createEndElement("", "", CHILD_NAME);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}
