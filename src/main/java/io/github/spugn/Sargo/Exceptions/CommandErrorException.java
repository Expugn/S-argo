package io.github.spugn.Sargo.Exceptions;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.handle.obj.IChannel;

public class CommandErrorException extends SargoException
{
    private static final String ERROR_TITLE = "COMMAND ERROR";
    private static final int ERROR_CODE = 100;
    private final String ERROR_TEXT;

    private IChannel channel;

    public CommandErrorException(IChannel channel)
    {
        this.channel = channel;
        ERROR_TEXT = "Please review the command guide in '" + CommandManager.getCommandPrefix() + "**help**'.";
    }

    @Override
    protected void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
