package io.github.spugn.Sargo.Functions;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

public class Help
{
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;

    public Help(IDiscordClient client, IChannel channel)
    {
        CLIENT = client;
        CHANNEL = channel;
    }

    private void run()
    {

    }
}
