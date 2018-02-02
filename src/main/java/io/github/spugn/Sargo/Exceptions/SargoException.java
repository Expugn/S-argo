package io.github.spugn.Sargo.Exceptions;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

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

    protected abstract void displayErrorMessage();
}
