package io.github.spugn.Sargo.Functions;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import discord4j.rest.http.client.ClientException;
import io.github.spugn.Sargo.System.SystemData;
import io.github.spugn.Sargo.XMLParsers.LoginSettingsParser;

import java.awt.*;
import java.io.File;
import java.util.Objects;

/**
 * INFO
 * <p>
 *     Displays a bunch of info about the bot.
 * </p>
 *
 * @author S'pugn
 * @version 2.0
 * @since v2.6
 */
public class Info
{
    public Info(Message message)
    {
        // DETERMINE BOT OWNER NAME
        String botOwnerName;
        try
        {
            Member botOwner = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(LoginSettingsParser.getBotOwnerDiscordID()))).block();
            botOwnerName = botOwner.getUsername() + "#" + botOwner.getDiscriminator();
        }
        catch (ClientException e)
        {
            botOwnerName = "???";
        }

        // DETERMINE AMOUNT OF FILES IN USER DIRECTORY
        int userFiles;
        try
        {
            File userDirectory = new File("data/Users");
            userFiles = Objects.requireNonNull(userDirectory.list()).length;
        }
        catch (NullPointerException e)
        {
            userFiles = 0;
        }

        // FINALIZE VARIABLES
        final String botOwnerName_final = botOwnerName;
        final int userFiles_final = userFiles;

        // BUILD AND SEND EMBED
        message.getChannel().block().createMessage(
                s -> s.setEmbed(
                        embed -> embed
                            .setColor(new Color(204, 102, 153))
                            .setAuthor("S'argo - SAO:MD Summon Simulator Discord Bot by S'pugn#2612", "", "")
                            .addField("Version", "v" + SystemData.getVERSION(), true)
                            .addField("Bot Owner", botOwnerName_final, true)
                            .addField("Source", "[Github](https://github.com/Expugn/S-argo)", true)
                            .addField("Registered Users", userFiles_final + " User(s)", true)
                            .setImage("https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/S'argo_Banner_Animated.gif")
        )).block();
    }

}
