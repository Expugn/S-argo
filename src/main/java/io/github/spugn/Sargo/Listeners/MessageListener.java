package io.github.spugn.Sargo.Listeners;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

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
            SettingsParser settings = new SettingsParser();
            if (!(settings.getIgnoredChannelNames().contains(event.getChannel().getName())))
            {
                new CommandManager(client, event);
            }

        }
        catch (RateLimitException e)
        {
            System.out.println("Rate Limit Exception.");
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
