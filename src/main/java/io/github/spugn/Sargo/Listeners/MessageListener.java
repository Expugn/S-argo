package io.github.spugn.Sargo.Listeners;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.PrivateChannel;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    public static void messageReceived(Message message)
    {
        if (message.getAuthor().isPresent())
        {
            if (!message.getAuthor().get().isBot())
            {
                // DIRECT MESSAGING IS NOT SUPPORTED
                if (message.getChannel().block() instanceof PrivateChannel)
                {
                    User user = message.getAuthor().orElse(null);
                    String username = user.getUsername() + "#" + user.getDiscriminator();
                    String receivedMessage = message.getContent().orElse(null);

                    LOGGER.info("Direct Message received from " + username + " (" + user.getId().asString() + "): \"" + receivedMessage + "\"");

                    try
                    {
                        if (receivedMessage.startsWith(CommandManager.getCommandPrefix()))
                        {
                            Sargo.replyToMessage_Warning(message, "ERROR", "Using commands via **Direct Messaging** is not supported.");
                        }
                    }
                    catch (NullPointerException e)
                    {
                        // IGNORED
                    }
                }
                else
                {
                    String messageChannelName = message.getChannel().cast(TextChannel.class).block().getName();
                    Long messageChannelID = message.getChannelId().asLong();

                    // IF THERE IS NOT A MAIN CHANNEL SET...
                    if (CommandSettingsParser.getMainChannel().isEmpty())
                    {
                        // CHECK IF CHANNEL IS BLACKLISTED (NAME OR CHANNEL ID)
                        boolean isInBlacklistedChannel = CommandSettingsParser.getBlacklistedChannels().contains(messageChannelName) ||
                                CommandSettingsParser.getBlacklistedChannels().contains(messageChannelID);

                        // IF NOT IN BLACKLIST, CONTINUE
                        if (!isInBlacklistedChannel)
                        {
                            new CommandManager(message);
                        }
                    }
                    else
                    {
                        // A MAIN CHANNEL IS SET... CHECK IF THE MESSAGE IS SENT FROM THE MAIN CHANNEL.
                        boolean isInMainChannel;
                        try
                        {
                            isInMainChannel = (messageChannelID == Long.parseLong(CommandSettingsParser.getMainChannel()));
                        }
                        catch (NumberFormatException e)
                        {
                            isInMainChannel = (messageChannelName.equalsIgnoreCase(CommandSettingsParser.getMainChannel()));
                        }

                        // CHECK IF THE MESSAGE IS FROM A WHITELISTED CHANNEL AS WELL.
                        boolean isInWhitelistedChannel = CommandSettingsParser.getWhitelistedChannels().contains(messageChannelName) ||
                                CommandSettingsParser.getWhitelistedChannels().contains(messageChannelID);

                        // IF MESSAGE IS IN MAIN CHANNEL OR A WHITELISTED ONE, CONTINUE...
                        if (isInMainChannel || isInWhitelistedChannel)
                        {
                            new CommandManager(message);
                        }
                    }
                }
            }
        }
    }
}
