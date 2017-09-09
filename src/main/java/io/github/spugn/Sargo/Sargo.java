package io.github.spugn.Sargo;

import io.github.spugn.Sargo.Listeners.MessageListener;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Discord.Discord4J.BuildBot;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class Sargo
{
    private static IDiscordClient CLIENT;

    public static void main(String[] args)
    {
        SettingsParser settings = new SettingsParser();

        CLIENT = BuildBot.run(settings.getBotToken());
        EventDispatcher dispatcher = CLIENT.getDispatcher();

        dispatcher.registerListener(new MessageListener(CLIENT));
    }
}
