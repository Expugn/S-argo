package io.github.spugn.Sargo.Exceptions;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.handle.obj.IChannel;

/**
 * COMMAND ERROR EXCEPTION
 * <p>
 *      CAUSE:<br>
 *      This exception arises when the user inputs a command in an
 *      invalid format (i.e: forgetting an argument, inputting an invalid
 *      argument, etc).<br>
 *
 *      SOLUTION:<br>
 *      This exception can be resolved once the user inputs a correctly
 *      formatted command. They can review how to do so with the `help`
 *      command.<br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 */
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
    public void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
