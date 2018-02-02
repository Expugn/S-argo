package io.github.spugn.Sargo.Exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * NOT ENOUGH RECORD CRYSTALS EXCEPTION
 * <p>
 *      CAUSE:<br>
 *      This exception arises when the user lacks the Record Crystals
 *      needed to perform the record crystal scout they are trying to perform.<br>
 *
 *      SOLUTION:<br>
 *      This exception can be resolved once the user obtains enough
 *      Record Crystals from multi scouts on the banner they want to
 *      perform a record crystal scout on.<br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 */
public class NotEnoughRecordCrystalsException extends SargoException
{
    private static final String ERROR_TITLE = "NOT ENOUGH RECORD CRYSTALS";
    private static final int ERROR_CODE = 201;
    private final String ERROR_TEXT;

    private IChannel channel;

    public NotEnoughRecordCrystalsException()
    {
        this.channel = channel;
        ERROR_TEXT = "You need **10** Record Crystals to perform a record crystal scout.";
    }
    @Override
    public void displayErrorMessage()
    {
        channel.sendMessage(getEmbedObject(ERROR_TITLE, ERROR_TEXT, ERROR_CODE));
    }
}
