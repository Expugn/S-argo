package io.github.spugn.Sargo.Utilities;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class DiscordCommand
{
    private static IDiscordClient CLIENT;
    private static IUser USER;

    private static boolean USE_MENTION = false;
    private static boolean DELETE_USER_MESSAGE = false;
    private static char COMMAND_PREFIX = '/';

    public DiscordCommand(IDiscordClient botClient)
    {
        this.CLIENT = botClient;
        this.USER = CLIENT.getOurUser();
    }

    public void setUseMention(boolean b)
    {
        USE_MENTION = b;
    }

    public boolean getUseMention()
    {
        return USE_MENTION;
    }

    public void setCommandPrefix(char c)
    {
        COMMAND_PREFIX = c;
    }

    public char getCommandPrefix()
    {
        return COMMAND_PREFIX;
    }

    public void setDeleteUserMessage(boolean b)
    {
        DELETE_USER_MESSAGE = b;
    }

    public boolean getDeleteUserMessage()
    {
        return DELETE_USER_MESSAGE;
    }

    @Deprecated
    public CommandLine meetsConditions(String message)
    {
        if (USE_MENTION)
        {
            if (message.startsWith("<@" + USER.getLongID() + ">") || message.startsWith("<@!" + USER.getLongID() + ">"))
            {
                return new CommandLine(message, 1);
            }
        }
        else
        {
            if (message.indexOf(COMMAND_PREFIX) == 0)
            {
                return new CommandLine(message, COMMAND_PREFIX);
            }
        }
        return null;
    }

    public CommandLine meetsConditions(IMessage message)
    {
        if (USE_MENTION)
        {
            if (message.getContent().startsWith("<@" + USER.getLongID() + ">") || message.getContent().startsWith("<@!" + USER.getLongID() + ">"))
            {
                if (DELETE_USER_MESSAGE)
                {
                    message.delete();
                }
                return new CommandLine(message.getContent(), 1);
            }
        }
        else
        {

            if (message.getContent().indexOf(COMMAND_PREFIX) == 0)
            {
                if (DELETE_USER_MESSAGE)
                {
                    message.delete();
                }
                return new CommandLine(message.getContent(), COMMAND_PREFIX);
            }
        }
        return null;
    }
}
