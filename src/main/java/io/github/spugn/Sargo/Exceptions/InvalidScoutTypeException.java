package io.github.spugn.Sargo.Exceptions;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.handle.obj.IChannel;

public class InvalidScoutTypeException extends SargoException
{
    private static final String ERROR_TITLE = "UNKNOWN/UNAVAILABLE SCOUT TYPE";
    private static final int ERROR_CODE = 201;
    private final String ERROR_TEXT;

    private IChannel channel;

    public InvalidScoutTypeException(IChannel channel, int bannerID)
    {
        this.channel = channel;
        ERROR_TEXT = "Use '" + CommandManager.getCommandPrefix() + "**scout** " + bannerID + "' and read the footer text for available scout types.";
    }

    @Override
    public void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
