package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.WarningMessage;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

import java.io.File;

public class Reset
{
    private static IChannel CHANNEL;
    public Reset(IChannel channel)
    {
        CHANNEL = channel;
        CHANNEL.sendMessage(new WarningMessage("WARNING", "Continuing forward will erase all your data. Are you sure?\nType 'reset y' to proceed.").get().build());
    }

    public Reset (IChannel channel, String discordID)
    {
        CHANNEL = channel;
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (userFile.exists())
        {
            userFile.delete();

            IUser discordUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
            CHANNEL.sendMessage(new WarningMessage("USER FILE DELETED", "**" + discordUser.getName() + "#" + discordUser.getDiscriminator() + "**'s file has been deleted.").get().build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("USER FILE NOT FOUND", "Huh. Well that was anti-climatic.").get().build());
        }

    }
}
