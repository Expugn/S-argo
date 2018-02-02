package io.github.spugn.Sargo.Exceptions;

/**
 * FAILED TO READ BANNER FILE EXCEPTION
 * <p>
 *      CAUSE:<br>
 *      This exception arises when there is an issue parsing the
 *      Banners.xml file. Generally these problems can arise
 *      if the file is not found, if there is an XML Stream
 *      issue, or if there is a null pointer.<br>
 *
 *      SOLUTION:<br>
 *      This exception can be resolved by making sure the
 *      Banners.xml file actually exists in it's appropriate
 *      location. If the file does indeed exist, then
 *      restarting the bot may resolve the solution.<br>
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.4
 */
public class FailedToReadBannerFileException extends SargoException
{
    private static final String ERROR_TITLE = "FAILED TO READ BANNER FILE";
    private static final int ERROR_CODE = 101;
    private final String ERROR_TEXT;

    public FailedToReadBannerFileException()
    {
        ERROR_TEXT = "An error has occurred when trying to read the Banner file.\n" +
                     "Try restarting the bot or correct the issue manually.";
    }

    @Override
    public void displayErrorMessage()
    {
        System.out.println(ERROR_TITLE + " - " + ERROR_TEXT + "\nError Code: " + ERROR_CODE);
    }
}
