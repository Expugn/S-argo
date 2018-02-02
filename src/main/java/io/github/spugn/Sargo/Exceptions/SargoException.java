package io.github.spugn.Sargo.Exceptions;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

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

    protected EmbedObject getEmbedObject(String title, String content, int errorCode)
    {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withTitle(title);
        builder.withDescription(content);
        builder.withFooterText("Error Code: " + errorCode);
        builder.withColor(255, 0, 0);

        return builder.build();
    }

    public abstract void displayErrorMessage();
}
