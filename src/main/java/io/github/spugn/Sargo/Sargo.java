package io.github.spugn.Sargo;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Functions.Reload;
import io.github.spugn.Sargo.Functions.Update;
import io.github.spugn.Sargo.Listeners.MessageListener;
import io.github.spugn.Sargo.System.SystemData;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import io.github.spugn.Sargo.XMLParsers.LoginSettingsParser;
import io.github.spugn.Sargo.XMLParsers.ScoutMasterParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.util.function.Consumer;

public class Sargo
{
    private static DiscordClient CLIENT;
    private static final Logger LOGGER = LoggerFactory.getLogger(Sargo.class);

    public static void main(String[] args)
    {
        Reload.silentReloadSettings();

        CLIENT = new DiscordClientBuilder(LoginSettingsParser.getBotToken()).build();

        CLIENT.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(ready -> {
                    LOGGER.info("Starting up S'argo v" + SystemData.getVERSION() + " - SAO:MD Summon Simulator Discord Bot by S'pugn#2612");
                    LOGGER.info("Logged in and running as: " + ready.getSelf().getUsername() + "#" + ready.getSelf().getDiscriminator());

                    // UPDATE PRESENCE
                    refreshPresence();

                    // DOWNLOAD SYSTEM IMAGES IF NEEDED AND RELOAD BANNERS
                    new Update();
                    Reload.reloadBanners();
                });

        CLIENT.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> MessageListener.messageReceived(event.getMessage()));

        CLIENT.login().block();
    }

    public static DiscordClient getCLIENT()
    {
        return CLIENT;
    }

    public static void refreshPresence()
    {
        if (CLIENT.getSelfId().isPresent())
        {
            String playingText;
            String commandPrefix = CommandSettingsParser.getCommandPrefix() + "";
            String clientID = CLIENT.getSelfId().get().asString();
            String botName = new ScoutMasterParser().getBotName();

            playingText = botName +
                    (clientID.equalsIgnoreCase("356981380338679810") ||
                            clientID.equalsIgnoreCase("309620271621341185") ? " devBot | " : " | ") +
                    commandPrefix + "help";

            CLIENT.updatePresence(Presence.online(Activity.playing(playingText))).subscribe();
        }
    }

    public static void replyToMessage(TextChannel textChannel, String reply)
    {
        textChannel.createMessage(reply).block();
    }

    public static void replyToMessage(TextChannel textChannel, String reply, File image)
    {
        try
        {
            InputStream is = new FileInputStream(image);
            textChannel.createMessage( s -> s.setContent(reply).addFile(image.getName(), is) ).block();
        }
        catch (FileNotFoundException e)
        {
            textChannel.createMessage(reply).block();
        }

    }

    public static Message replyToMessage_Warning(Message sentMessage, String title, String text)
    {
        return sentMessage.getChannel().block().createMessage(
                    s -> s.setEmbed(embed -> embed.setColor(Color.RED).setTitle(title).setDescription(text))).block();
    }

    public static void replyToMessage_Warning(Message sentMessage, String title, String text, String bannerName)
    {
        sentMessage.getChannel().block().createMessage(
                s -> s.setEmbed(embed -> embed.setColor(Color.RED).setTitle(title).setDescription(text).setFooter(bannerName, new GitHubImage("images/System/Scout_Icon.png").getURL()))).block();
    }

    public static Message replyToMessage_Warning(TextChannel textChannel, String title, String text)
    {
        return textChannel.createMessage(
                s -> s.setEmbed(embed -> embed.setColor(Color.RED).setTitle(title).setDescription(text))).block();
    }

    public static void replyToMessage_Warning(TextChannel textChannel, String title, String text, String bannerName)
    {
        textChannel.createMessage(
                s -> s.setEmbed(embed -> embed.setColor(Color.RED).setTitle(title).setDescription(text).setFooter(bannerName, new GitHubImage("images/System/Scout_Icon.png").getURL()))).block();
    }

    public static Message sendEmbed(TextChannel tc, Consumer<EmbedCreateSpec> ecsTemplate)
    {
        return tc.createMessage(ms -> ms.setEmbed(ecsTemplate.andThen(es -> {}))).block();
    }

    public static Message sendEmbed(TextChannel tc, Consumer<EmbedCreateSpec> ecsTemplate, File image)
    {
        try
        {
            InputStream is = new FileInputStream(image);
            Message sentMessage = tc.createMessage(s -> s.setEmbed(ecsTemplate.andThen(es -> es.setImage("attachment://" + image.getName()))).addFile(image.getName(), is)).block();
            is.close();
            return sentMessage;
        }
        catch (FileNotFoundException e)
        {
            return tc.createMessage(ms -> ms.setEmbed(ecsTemplate.andThen(es -> es.addField("MISSING SCOUT RESULT", "Scout result image is missing.", true)))).block();
        }
        catch (IOException e)
        {
            // IGNORED
        }
        return null;
    }

    public static Message editEmbedMessage(Message embedMessage, Consumer<EmbedCreateSpec> ecsTemplate)
    {
        return embedMessage.edit(spec -> spec.setEmbed(ecsTemplate)).block();
    }
}


