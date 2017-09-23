package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.Functions.*;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.Utilities.CommandLine;
import io.github.spugn.Sargo.Utilities.DiscordCommand;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandManager
{
    private final IChannel CHANNEL;
    private final IMessage MESSAGE;
    private final String DISCORD_ID;
    private final String BOT_OWNER_DISCORD_ID;
    private final SettingsParser SETTINGS;
    private final DiscordCommand DISCORD_COMMAND;
    private final CommandLine COMMAND_LINE;

    public CommandManager(IDiscordClient client, MessageReceivedEvent event)
    {
        CHANNEL = event.getChannel();
        MESSAGE = event.getMessage();
        DISCORD_ID = MESSAGE.getAuthor().getStringID();

        SETTINGS = new SettingsParser();

        BOT_OWNER_DISCORD_ID = SETTINGS.getBotOwnerDiscordID();

        DISCORD_COMMAND = new DiscordCommand(client);
        DISCORD_COMMAND.setUseMention(SETTINGS.isUseMention());
        DISCORD_COMMAND.setCommandPrefix(SETTINGS.getCommandPrefix());
        DISCORD_COMMAND.setDeleteUserMessage(SETTINGS.isDeleteUserMessage());

        if (DISCORD_COMMAND.getUseMention())
        {
            client.changePlayingText("S'argo | @" + client.getOurUser().getName() + " help");
        }
        else
        {
            client.changePlayingText("S'argo | " + DISCORD_COMMAND.getCommandPrefix() + "help");
        }

        COMMAND_LINE = DISCORD_COMMAND.meetsConditions(MESSAGE);

        try
        {
            if (COMMAND_LINE != null)
            {
                /* WARN USERS ABOUT ERROR IN SETTINGS */
                sayWarning();

                if (COMMAND_LINE.getCommand().equalsIgnoreCase("help"))
                {
                    new Help(CHANNEL);
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("scout"))
                {
                    scoutCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("shop"))
                {
                    shopCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("profile"))
                {
                    profileCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("user"))
                {
                    userCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("reset"))
                {
                    resetCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("update") && DISCORD_ID.equals(BOT_OWNER_DISCORD_ID))
                {
                    updateCommand();
                }
                else if (COMMAND_LINE.getCommand().equalsIgnoreCase("stop") && DISCORD_ID.equals(BOT_OWNER_DISCORD_ID))
                {
                    CHANNEL.sendMessage(new WarningMessage("SHUTTING DOWN", "Goodbye!").get().build());
                    System.exit(0);
                }
                else
                {
                    CHANNEL.sendMessage(new WarningMessage("UNKNOWN COMMAND", "Use 'help' for a list of commands.").get().build());
                }
            }
        }
        catch (RateLimitException e)
        {
            System.out.println("Rate Limit Exception.");
        }
        catch (DiscordException e)
        {
            event.getChannel().sendMessage(new WarningMessage("DISCORD EXCEPTION", "Something went wrong.").get().build());
        }
        catch (MissingPermissionsException e)
        {
            System.out.println("Not Enough Permissions.");
        }
    }

    private void scoutCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        try
        {
            if (COMMAND_LINE.getArgumentCount() >= 2)
            {
                if (COMMAND_LINE.getArgument(2).equalsIgnoreCase("ws") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wsi") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wm") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wmi"))
                {
                    new WeaponScout(CHANNEL, Integer.parseInt(COMMAND_LINE.getArgument(1)), COMMAND_LINE.getArgument(2), DISCORD_ID);
                }
                else
                {
                    new Scout(CHANNEL, Integer.parseInt(COMMAND_LINE.getArgument(1)), COMMAND_LINE.getArgument(2), DISCORD_ID);
                }

            }
            else if (COMMAND_LINE.getArgumentCount() >= 1)
            {
                new BannerInfo(CHANNEL, Integer.parseInt(COMMAND_LINE.getArgument(1)));
            }
            else
            {
                new BannerInfo(CHANNEL, "1");
            }
        }
        catch (NumberFormatException e)
        {
            if (COMMAND_LINE.getArgumentCount() == 1)
            {
                try
                {
                    int pageNumber = Integer.parseInt(COMMAND_LINE.getArgument(1).charAt(1) + "");
                    char pChar = COMMAND_LINE.getArgument(1).charAt(0);

                    if (pChar == 'p' || pChar == 'P')
                    {
                        new BannerInfo(CHANNEL, String.valueOf(pageNumber));
                    }
                    else
                    {
                        /* FIRST CHARACTER IS NOT 'P' */
                        CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
                    }
                }
                catch (NumberFormatException f)
                {
                    CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please review the help menu.").get().build());
                }
                catch (NullPointerException f)
                {
                    CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please review the help menu.").get().build());
                }
                catch (StringIndexOutOfBoundsException f)
                {
                    CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please review the help menu.").get().build());
                }
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
            }
        }
    }

    private void shopCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            if (COMMAND_LINE.getArgumentCount() >= 2)
            {
                try
                {
                    int quantity = Integer.parseInt(COMMAND_LINE.getArgument(2));

                    if (quantity < 0)
                    {
                        quantity = 1;
                    }
                    else if (quantity > 10)
                    {
                        quantity = 10;
                    }

                    new Shop(CHANNEL, DISCORD_ID, COMMAND_LINE.getArgument(1), quantity);
                }
                catch (NumberFormatException e)
                {
                    new Shop(CHANNEL, DISCORD_ID, COMMAND_LINE.getArgument(1), 1);
                }
            }
            else
            {
                new Shop(CHANNEL, DISCORD_ID, COMMAND_LINE.getArgument(1), 1);
            }

        }
        else
        {
            new Shop(CHANNEL);
        }
    }

    private void profileCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        if (COMMAND_LINE.getArgumentCount() >= 2)
        {
            String arg1 = COMMAND_LINE.getArgument(1);
            String arg2 = COMMAND_LINE.getArgument(2);

            if (arg1.equalsIgnoreCase("info") || arg1.equalsIgnoreCase("i"))
            {
                try
                {
                    /* TEST IF ARGUMENT CAN BE PARSED INTO AN INTEGER */
                    Integer.parseInt(arg2);

                    /* OPEN BANNER INFO PROFILE */
                    new Profile(CHANNEL, DISCORD_ID, 2, arg2);
                }
                catch (NumberFormatException e)
                {
                    CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
                }
            }
            else if (arg1.equalsIgnoreCase("search") || arg1.equalsIgnoreCase("s"))
            {
                new Profile(CHANNEL, DISCORD_ID, 3, arg2);
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN ARGUMENT", "Please review the help menu.").get().build());
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            String arg1 = COMMAND_LINE.getArgument(1);
            if (arg1.equalsIgnoreCase("data") || arg1.equalsIgnoreCase("d"))
            {
                new Profile(CHANNEL, DISCORD_ID, 1);
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN ARGUMENT", "Please review the help menu.").get().build());
            }
        }
        else
        {
            new Profile(CHANNEL, DISCORD_ID);
        }
    }

    private void userCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            IGuild guild = CHANNEL.getGuild();
            String userDiscordName = COMMAND_LINE.getArgument(1);

            if (!(guild.getUsersByName(userDiscordName).isEmpty()))
            {
                IUser foundUser = guild.getUsersByName(userDiscordName).get(0);
                new Profile(CHANNEL, foundUser.getStringID());
            }
            else
            {
                try
                {
                    String userDiscordID = COMMAND_LINE.getArgument(1);
                    userDiscordID = userDiscordID.replaceAll("<", "");
                    userDiscordID = userDiscordID.replaceAll("@", "");
                    userDiscordID = userDiscordID.replaceAll("!", "");
                    userDiscordID = userDiscordID.replaceAll(">", "");

                    if (guild.getUserByID(Long.parseLong(userDiscordID)) != null)
                    {
                        IUser foundUser = guild.getUserByID(Long.parseLong(userDiscordID));
                        new Profile(CHANNEL, foundUser.getStringID());
                    }
                    else
                    {
                        CHANNEL.sendMessage(new WarningMessage("UNKNOWN USER", "Could not find that user.").get().build());
                    }
                }
                catch (NumberFormatException e)
                {
                    CHANNEL.sendMessage(new WarningMessage("UNKNOWN USER", "Could not find that user. Does their name have a space? Try 'user @name' instead.").get().build());
                }
            }
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH ARGUMENTS", "Please review the help menu.").get().build());
        }
    }

    private void resetCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        if (COMMAND_LINE.getArgumentCount() >= 3)
        {
            String arg3 = COMMAND_LINE.getArgument(3);
            String arg2 = COMMAND_LINE.getArgument(2);
            int bannerID = Integer.parseInt(COMMAND_LINE.getArgument(1));
            boolean yes = (arg3.equalsIgnoreCase("y") || arg3.equalsIgnoreCase("yes"));

            if (arg2.equalsIgnoreCase("c") ||
                    arg2.equalsIgnoreCase("w") ||
                    arg2.equalsIgnoreCase("a"))
            {
                new Reset(CHANNEL, DISCORD_ID, bannerID, arg2, yes);
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Use 'reset [BannerID] [c/w/a]'").get().build());
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 2)
        {
            String arg2 = COMMAND_LINE.getArgument(2);
            int bannerID = Integer.parseInt(COMMAND_LINE.getArgument(1));

            if (arg2.equalsIgnoreCase("c") ||
                    arg2.equalsIgnoreCase("w") ||
                    arg2.equalsIgnoreCase("a"))
            {
                new Reset(CHANNEL, DISCORD_ID, bannerID, arg2, false);
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Use 'reset [BannerID] [c/w/a]'").get().build());
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            String arg1 = COMMAND_LINE.getArgument(1);
            if (arg1.equalsIgnoreCase("y") || arg1.equalsIgnoreCase("yes"))
            {
                new Reset(CHANNEL, DISCORD_ID);
            }
        }
        else
        {
            new Reset(CHANNEL);
        }
    }

    private void updateCommand() throws RateLimitException, DiscordException, MissingPermissionsException
    {
        if (COMMAND_LINE.getArgumentCount() >= 2)
        {
            new Update(CHANNEL, COMMAND_LINE.getArgument(1), COMMAND_LINE.getArgument(2));
        }
        else if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            new Update(CHANNEL, COMMAND_LINE.getArgument(1));
        }
    }

    private void sayWarning()
    {
        if (SETTINGS.isErrorInRates())
        {
            CHANNEL.sendMessage(new WarningMessage("SCOUT RATE ERROR", "Scout rates do not add up to 1.0. Please review your settings file.").get().build());
        }
    }
}
