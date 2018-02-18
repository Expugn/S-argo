package io.github.spugn.Sargo.Exceptions;

public class FailedToReadSettingFileException extends SargoException
{
    private static final String ERROR_TITLE = "FAILED TO READ BANNER FILE";
    private static final int ERROR_CODE = 102;
    private final String ERROR_TEXT;

    public FailedToReadSettingFileException()
    {
        ERROR_TEXT = "An error has occurred when trying to read the Setting file.\n" +
                "Try restarting the bot or correct the issue manually.";
    }

    @Override
    public void displayErrorMessage()
    {
        System.out.println(ERROR_TITLE + " - " + ERROR_TEXT + "\nError Code: " + ERROR_CODE);
    }
}
