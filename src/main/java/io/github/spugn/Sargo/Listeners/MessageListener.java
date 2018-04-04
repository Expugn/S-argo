package io.github.spugn.Sargo.Listeners;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

/**
 * MESSAGE LISTENER
 * <p>
 *     Triggers whenever a message is sent in the guild that the
 *     bot is in.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 * @see MessageReceivedEvent
 */
public class MessageListener
{
    private IDiscordClient client;

    public MessageListener(IDiscordClient discordClient)
    {
        client = discordClient;
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event)
    {
        try
        {
            //SettingsParser settings = new SettingsParser();
            //if (!(settings.getIgnoredChannelNames().contains(event.getChannel().getName())))
            //if (!(SettingsParser.getIgnoredChannelNames().contains(event.getChannel().getName())))
            if (CommandSettingsParser.getMainChannel().isEmpty())
            {
                boolean isInBlacklistedChannel = CommandSettingsParser.getBlacklistedChannels().contains(event.getChannel().getName()) ||
                                            CommandSettingsParser.getBlacklistedChannels().contains(event.getChannel().getStringID());

                if (!isInBlacklistedChannel)
                {
                    new CommandManager(client, event);
                }
            }
            else
            {
                boolean isInMainChannel;

                try
                {
                    isInMainChannel = (event.getChannel().getLongID() == Long.parseLong(CommandSettingsParser.getMainChannel()));
                }
                catch (NumberFormatException e)
                {
                    isInMainChannel = (event.getChannel().getName().equalsIgnoreCase(CommandSettingsParser.getMainChannel()));
                }

                boolean isInWhitelistedChannel = CommandSettingsParser.getWhitelistedChannels().contains(event.getChannel().getName()) ||
                                            CommandSettingsParser.getWhitelistedChannels().contains(event.getChannel().getStringID());

                if (isInMainChannel || isInWhitelistedChannel)
                {
                    new CommandManager(client, event);
                }
            }

        }
        catch (RateLimitException e)
        {
            System.out.println("[MessageListener] - Rate Limit Exception.");
        }
        catch (DiscordException e)
        {
            event.getChannel().sendMessage(new WarningMessage("DISCORD EXCEPTION", "Something went wrong.").get().build());
        }
        catch (MissingPermissionsException e)
        {
            System.out.println("Missing Permissions!");
        }
    }
}
