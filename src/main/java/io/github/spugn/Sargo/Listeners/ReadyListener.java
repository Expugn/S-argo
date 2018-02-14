package io.github.spugn.Sargo.Listeners;

import io.github.spugn.Sargo.Functions.Update;
import io.github.spugn.Sargo.GUI.GUI;
import io.github.spugn.Sargo.System.SystemData;
import io.github.spugn.Sargo.XMLParsers.ScoutMasterParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @version 1.1
 * @since v1.0
 * @see ReadyEvent
 */
public class ReadyListener
{
    private final IDiscordClient CLIENT;
    private String botName;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyListener.class);

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

        System.out.println();
        LOGGER.info("Starting up S'argo v" + SystemData.getVERSION() + " - SAO:MD Summon Simulator Discord Bot by S'pugn#2612");
        LOGGER.info("Now running bot on: " + CLIENT.getOurUser().getName() + "#" + CLIENT.getOurUser().getDiscriminator());
        new Update();
    }
}


