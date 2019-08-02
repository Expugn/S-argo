package io.github.spugn.Sargo.Utilities;

import discord4j.core.DiscordClient;
import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Sargo;

public class DiscordCommand
{
    private DiscordClient CLIENT;
    private String clientID;

    private boolean USE_MENTION = false;
    private boolean DELETE_USER_MESSAGE = false;
    private char COMMAND_PREFIX = '/';

    public DiscordCommand()
    {
        this.clientID = Sargo.getCLIENT().getSelf().block().getId().asString();
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
            if (message.startsWith("<@" + clientID + ">") || message.startsWith("<@!" + clientID + ">"))
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

    public CommandLine meetsConditions(Message message)
    {
        if (USE_MENTION)
        {
            if (message.getContent().orElse("").startsWith("<@" + clientID + ">") || message.getContent().orElse("").startsWith("<@!" + clientID + ">"))
            {
                if (DELETE_USER_MESSAGE)
                {
                    message.delete();
                }
                return new CommandLine(message.getContent().orElse(""), 1);
            }
        }
        else
        {

            if (message.getContent().orElse("").indexOf(COMMAND_PREFIX) == 0)
            {
                if (DELETE_USER_MESSAGE)
                {
                    message.delete();
                }
                return new CommandLine(message.getContent().orElse(""), COMMAND_PREFIX);
            }
        }
        return null;
    }
}
