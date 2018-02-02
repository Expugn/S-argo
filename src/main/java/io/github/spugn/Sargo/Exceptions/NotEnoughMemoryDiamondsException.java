package io.github.spugn.Sargo.Exceptions;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.handle.obj.IChannel;

public class NotEnoughMemoryDiamondsException extends SargoException
{
    private static final String ERROR_TITLE = "NOT ENOUGH MEMORY DIAMONDS";
    private static final int ERROR_CODE = 200;
    private final String ERROR_TEXT;

    private IChannel channel;

    public NotEnoughMemoryDiamondsException(IChannel channel, int scoutPrice)
    {
        this.channel = channel;
        ERROR_TEXT = "You need **" + scoutPrice + "** Memory Diamonds to scout.\n" +
                     "Use '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.";
    }

    @Override
    protected void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
