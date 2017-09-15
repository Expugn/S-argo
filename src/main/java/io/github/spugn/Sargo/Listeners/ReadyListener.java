package io.github.spugn.Sargo.Listeners;

import io.github.spugn.Sargo.GUI.GUI;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

public class ReadyListener
{
    private final IDiscordClient CLIENT;

    public ReadyListener(IDiscordClient discordClient)
    {
        CLIENT = discordClient;
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event)
    {
        SettingsParser settings = new SettingsParser();

        if (!settings.isNoGUI())
        {
            new GUI(CLIENT.getOurUser().getName() + "#" + CLIENT.getOurUser().getDiscriminator());
        }

        if (settings.isUseMention())
        {
            CLIENT.changePlayingText("S'argo | @" + CLIENT.getOurUser().getName() + " help");
        }
        else
        {
            CLIENT.changePlayingText("S'argo | " + settings.getCommandPrefix() + "help");
        }
    }
}


