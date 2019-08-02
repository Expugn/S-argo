package io.github.spugn.Sargo.Exceptions;

import discord4j.core.spec.EmbedCreateSpec;

import java.awt.*;
import java.util.function.Consumer;

/**
 * S'ARGO EXCEPTION
 * <p>
 *      This class is a base class for any exceptions/warnings
 *      that may appear during the use of S'argo.<br>
 *
 *      Any class that extends from this class will contain data
 *      for that specific exception as well as the common cause
 *      and solution for that issue in their JavaDoc. <br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 */
public abstract class SargoException extends Exception
{
    public SargoException()
    {
        super();
    }

    protected Consumer<EmbedCreateSpec> getEmbedObject(String title, String content, int errorCode)
    {
        Consumer<EmbedCreateSpec> ecsTemplate;

        ecsTemplate = s -> {
            s.setTitle(title);
            s.setDescription(content);
            s.setColor(new Color(255, 0, 0));
        };
        return ecsTemplate;
    }

    public abstract void displayErrorMessage();
}
