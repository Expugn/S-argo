package io.github.spugn.Sargo.Exceptions;

import io.github.spugn.Sargo.Managers.CommandManager;
import sx.blah.discord.handle.obj.IChannel;

/**
 * NOT ENOUGH MEMORY DIAMONDS EXCEPTION
 * <p>
 *      CAUSE:<br>
 *      This exception arises when the user lacks the Memory Diamonds
 *      needed to perform the scout they are trying to perform.<br>
 *
 *      SOLUTION:<br>
 *      This exception can be resolved once the user obtains enough
 *      Memory Diamonds from the `shop` command to perform the scout. <br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 */
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
    public void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
