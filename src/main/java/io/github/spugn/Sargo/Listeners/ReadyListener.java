package io.github.spugn.Sargo.Listeners;

import io.github.spugn.Sargo.Functions.Update;
import io.github.spugn.Sargo.GUI.GUI;
import io.github.spugn.Sargo.XMLParsers.ScoutMasterParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * READY LISTENER
 * <p>
 *     Triggers when the bot initially logs in.
 *     At this state, the bot is ready to do things such
 *     as send messages or change it's playing text.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 * @see ReadyEvent
 */
public class ReadyListener
{
    private final IDiscordClient CLIENT;
    private String botName;

    public ReadyListener(IDiscordClient discordClient)
    {
        CLIENT = discordClient;
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event)
    {
        SettingsParser settings = new SettingsParser();
        botName = new ScoutMasterParser().getBotName();

        if (!settings.isNoGUI())
        {
            new GUI(CLIENT.getOurUser().getName() + "#" + CLIENT.getOurUser().getDiscriminator());
        }

        String playingText;
        String commandPrefix;
        if (settings.isUseMention())
        {
            commandPrefix = "@" + CLIENT.getOurUser().getName() + " ";
        }
        else
        {
            commandPrefix = settings.getCommandPrefix() + "";
        }

        if (CLIENT.getOurUser().getStringID().equalsIgnoreCase("356981380338679810") ||
                CLIENT.getOurUser().getStringID().equalsIgnoreCase("309620271621341185"))
        {
            playingText = botName + " devBot | " + commandPrefix + "help";
        }
        else
        {
            playingText = botName + " | " + commandPrefix + "help";
        }
        CLIENT.changePlayingText(playingText);

        new Update();
    }
}


