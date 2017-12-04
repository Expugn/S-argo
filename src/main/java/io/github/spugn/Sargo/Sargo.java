package io.github.spugn.Sargo;

import io.github.spugn.Sargo.Listeners.MessageListener;
import io.github.spugn.Sargo.Listeners.ReadyListener;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import org.jetbrains.annotations.Nullable;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Sargo
{
    private static IDiscordClient CLIENT;

    public static void main(String[] args)
    {
        SettingsParser settings = new SettingsParser();

        CLIENT = buildBot(settings.getBotToken());
        EventDispatcher dispatcher = CLIENT.getDispatcher();

        dispatcher.registerListener(new ReadyListener(CLIENT));
        dispatcher.registerListener(new MessageListener(CLIENT));
    }

    @Nullable
    private static IDiscordClient buildBot(String token)
    {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);

        try
        {
            return clientBuilder.login();
        }
        catch (DiscordException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static IDiscordClient getCLIENT()
    {
        return CLIENT;
    }
}
