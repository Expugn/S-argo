package io.github.spugn.Sargo;

import io.github.spugn.Sargo.Functions.Reload;
import io.github.spugn.Sargo.Listeners.MessageListener;
import io.github.spugn.Sargo.Listeners.ReadyListener;
import io.github.spugn.Sargo.XMLParsers.LoginSettingsParser;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

public class Sargo
{
    private static IDiscordClient CLIENT;
    private static final Logger LOGGER = LoggerFactory.getLogger(Sargo.class);

    public static void main(String[] args)
    {
        /*
        File settingsFile = new File("data/Settings.xml");
        if (!(settingsFile.exists()))
        {
            LOGGER.error("\nSettings.xml is not found!\nPlace it in the appropriate location before continuing.");
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                System.exit(0);
            }
            System.exit(0);
        }
        */

        Reload.silentReloadSettings();

        //CLIENT = buildBot(SettingsParser.getBotToken());
        CLIENT = buildBot(LoginSettingsParser.getBotToken());
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
            LOGGER.error("Provided bot token is invalid!\nFix the issue in /data/settings/Login.xml.");
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e2)
            {
                System.exit(0);
            }
            System.exit(0);
            return null;
            //e.printStackTrace();
        }
    }

    public static IDiscordClient getCLIENT()
    {
        return CLIENT;
    }
}
