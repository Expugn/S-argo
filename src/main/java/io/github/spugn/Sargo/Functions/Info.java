package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.System.SystemData;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

/**
 * INFO
 * <p>
 *     Displays a bunch of info about the bot.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.6
 */
public class Info
{
    /**
     * Displays the help menu.
     * @param channel  The channel where the help menu should be displayed.
     */
    public Info(IChannel channel)
    {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withAuthorName("S'argo - SAO:MD Summon Simulator Discord Bot by S'pugn#2612");
        builder.appendField("Version", "v" + SystemData.getVERSION(), true);

        try
        {
            IUser botOwner = channel.getGuild().getUserByID(Long.parseLong(SettingsParser.getBotOwnerDiscordID()));
            String botOwnerName = botOwner.getName() + "#" + botOwner.getDiscriminator();
            builder.appendField("Bot Owner", botOwnerName, true);
        }
        catch (NullPointerException e)
        {
            builder.appendField("Bot Owner", "???", true);
        }

        builder.appendField("Source", "[Github](https://github.com/Expugn/S-argo)", false);
        builder.withImage("https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/S'argo_Banner.png");
        builder.withColor(204, 102, 153);

        channel.sendMessage(builder.build());
    }

}
