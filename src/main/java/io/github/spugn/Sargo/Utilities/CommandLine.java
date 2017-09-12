package io.github.spugn.Sargo.Utilities;

public class CommandLine
{
    private String commandLine;

    /**
     * Simple constructor that takes the commandLine how it is.
     *
     * @param commandLine  A full command line; i.e: "command arg1 arg2 arg3"
     */
    public CommandLine(String commandLine)
    {
        this.commandLine = commandLine;
    }

    /**
     * Constructor for commands that have parameters that aren't related to the command.<p>
     *
     * <b>Example Command Line:</b> "system command arg1"<br>
     * If "system" should be ignored and the desired command line is "command arg1" then<br>
     * a CommandLine object should be constructed as <code>new CommandLine("system command arg1", 1);</code>
     *
     * @param commandLine  A command line with parameters beforehand; i.e: "system command arg1 arg2 arg3"
     * @param startIndex  Value of how many arguments to skip before the command line begins
     */
    public CommandLine(String commandLine, int startIndex)
    {
        String [] command = commandLine.split(" ", startIndex + 1);
        this.commandLine = command[command.length - 1];
    }

    /**
     * Constructor for commands that have a character such as a / or . before the command.<p>
     *
     * <b>Example Command Line:</b> "/command arg1"<br>
     * If '/' should be ignored and the desired command line is "command arg1" then<br>
     * a CommandLine object should be constructed as <code>new CommandLine("/command arg1", '/');</code><p>
     *
     * This can also be used with other characters like '.'; using the previous example it should<br>
     * be constructed as <code>new CommandLine("/command arg1", '.');</code>
     *
     * @param commandLine  A command line with a character before it begins; i.e: "/command arg1 arg2 arg3"
     * @param ignoredChar  The character to ignore; i.e: / or .
     */
    public CommandLine(String commandLine, char ignoredChar)
    {
        this.commandLine = commandLine.substring(commandLine.indexOf(ignoredChar) + 1);
    }

    /**
     * Constructor for when the command and arguments are separated.<p>
     *
     * <b>Example:</b> You have "command" and a String array of [arg, arg2]<br>
     * Piece the two together by constructing the CommandLine object as <code>new CommandLine("command", stringArray);</code>
     *
     * @param command  The command label
     * @param arguments  The arguments following the command label
     */
    public CommandLine(String command, String[] arguments)
    {
        String commandLine = command;

        for (int i = 0 ; i < arguments.length ; i++)
        {
            if (i + 1 <= arguments.length)
            {
                commandLine += " ";
            }
            commandLine += arguments[i];
        }

        this.commandLine = commandLine;
    }

    /**
     * Returns the first argument of the CommandLine.<p>
     *
     * Example:<p>
     * The Command for "do this thing" is "do".
     *
     * @return  The first argument of the CommandLine.
     */
    public String getCommand()
    {
        try
        {
            return getArguments()[0];
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    /**
     * Returns the argument after the command.<p>
     *
     * <b>Example:</b><br>
     * <code>getArgument(2);</code> should return, if the CommandLine "do this thing" is used, "thing".
     *
     * @param value  Argument index
     * @return  String of the CommandLine argument
     */
    public String getArgument(int value)
    {
        try
        {
            return getArguments()[value];
        }
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    /**
     * Returns the command line.
     *
     * @return  Command line
     */
    public String getCommandLine()
    {
        return commandLine;
    }

    /**
     * Returns the amount of arguments.
     *
     * @return  Amount of arguments
     */
    public int getArgumentCount()
    {
        return getArguments().length - 1;
    }

    /**
     * Returns an array with only the arguments.
     *
     * @return  An array with only the arguments
     */
    public String[] getArgumentArray()
    {
        return commandLine.split(" ", 2)[1].split("\\s+");
    }

    /**
     * Check if the command line has the correct number of arguments.
     *
     * @param amount  The value of arguments that you wish to be checked.
     * @return  true or false depending if the correct number of arguments has been reached.
     */
    public boolean checkArgumentCount(int amount)
    {
        if (getArgumentCount() >= amount)
            return true;
        return false;
    }

    @org.jetbrains.annotations.NotNull
    private String[] getArguments()
    {
        return commandLine.split("\\s+");
    }
}
