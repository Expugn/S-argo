package io.github.spugn.Sargo.Managers;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import io.github.spugn.Sargo.Functions.*;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.CommandLine;
import io.github.spugn.Sargo.Utilities.DiscordCommand;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import io.github.spugn.Sargo.XMLParsers.LoginSettingsParser;
import io.github.spugn.Sargo.XMLParsers.ShopSettingsParser;

/**
 * COMMAND MANAGER
 * <p>
 *     This class parses a command string and redirects the system
 *     to the proper function to be executed.
 * </p>
 *
 * @author S'pugn
 * @version 1.2
 * @since v1.0
 */
public class CommandManager
{
    private final CommandLine COMMAND_LINE;
    private final Message MESSAGE;
    private final String userDiscordID;

    public CommandManager(Message message)
    {
        this.MESSAGE = message;

        if (message.getAuthor().isPresent())
        {
            userDiscordID = message.getAuthor().get().getId().asString();
        }
        else
        {
            Sargo.replyToMessage_Warning(MESSAGE, "FAILED TO GET USER ID", "This wasn't supposed to happen.");
            userDiscordID = "";
            COMMAND_LINE = null;
            return;
        }

        String botOwnerDiscordID = LoginSettingsParser.getBotOwnerDiscordID();

        DiscordCommand discordCommand = new DiscordCommand();
        discordCommand.setUseMention(false);
        discordCommand.setCommandPrefix(CommandSettingsParser.getCommandPrefix());
        discordCommand.setDeleteUserMessage(CommandSettingsParser.isDeleteUserMessage());

        DiscordCommand discordCommandMention = new DiscordCommand();
        discordCommandMention.setUseMention(true);
        discordCommandMention.setCommandPrefix(CommandSettingsParser.getCommandPrefix());
        discordCommandMention.setDeleteUserMessage(CommandSettingsParser.isDeleteUserMessage());

        Sargo.refreshPresence();

        if (discordCommand.meetsConditions(message) != null)
        {
            COMMAND_LINE = discordCommand.meetsConditions(message);
        }
        else if (discordCommandMention.meetsConditions(message) != null)
        {
            COMMAND_LINE = discordCommandMention.meetsConditions(message);
        }
        else
        {
            COMMAND_LINE = null;
        }

        if (COMMAND_LINE != null)
        {
            if (COMMAND_LINE.getCommand().equalsIgnoreCase("scout"))
            {
                scoutCommand();
            }
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("help"))
            {
                new Help(MESSAGE);
            }
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("info"))
            {
                new Info(MESSAGE);
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
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("update") && userDiscordID.equals(botOwnerDiscordID))
            {
                updateCommand();
            }
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("stop") && userDiscordID.equals(botOwnerDiscordID))
            {
                Sargo.replyToMessage_Warning(MESSAGE, "SHUTTING DOWN", "Goodbye!");
                System.exit(0);
            }
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("reload") && userDiscordID.equals(botOwnerDiscordID))
            {
                reloadCommand();
            }
            else if (COMMAND_LINE.getCommand().equalsIgnoreCase("settings") && userDiscordID.equals(botOwnerDiscordID))
            {
                new SettingsManager(MESSAGE, COMMAND_LINE);
            }
        }
    }

    private void scoutCommand()
    {
        try
        {
            if (COMMAND_LINE.getArgumentCount() >= 2)
            {
                // START "TYPING"
                MESSAGE.getChannel().cast(TextChannel.class).block().type().block();


                if (COMMAND_LINE.getArgument(2).equalsIgnoreCase("ws") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wsi") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wm") ||
                        COMMAND_LINE.getArgument(2).equalsIgnoreCase("wmi"))
                {
                    new WeaponScoutManager(MESSAGE, Integer.parseInt(COMMAND_LINE.getArgument(1)), COMMAND_LINE.getArgument(2), userDiscordID);
                }
                else
                {
                    new ScoutManager(MESSAGE, Integer.parseInt(COMMAND_LINE.getArgument(1)), COMMAND_LINE.getArgument(2), userDiscordID);
                }

            }
            else if (COMMAND_LINE.getArgumentCount() >= 1)
            {
                new BannerInfo(MESSAGE, Integer.parseInt(COMMAND_LINE.getArgument(1)));
            }
            else
            {
                new BannerInfo(MESSAGE, "1");
            }
        }
        catch (NumberFormatException e)
        {
            if (COMMAND_LINE.getArgumentCount() == 1)
            {
                if (COMMAND_LINE.getArgument(1).equalsIgnoreCase("nts") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ntsi") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ntm") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ntmi") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("pts") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ptsi") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ptm") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("ptmi") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("nt2s") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("nt2si") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("nt2m") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("nt2mi") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("pt2s") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("pt2si") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("pt2m") ||
                        COMMAND_LINE.getArgument(1).equalsIgnoreCase("pt2mi"))
                {
                    // START "TYPING"
                    MESSAGE.getChannel().cast(TextChannel.class).block().type().block();

                    new TicketScoutManager(MESSAGE, COMMAND_LINE.getArgument(1), userDiscordID);
                }
                else
                {
                    try
                    {
                        int pageNumber = Integer.parseInt(COMMAND_LINE.getArgument(1).substring(1) + "");
                        char pChar = COMMAND_LINE.getArgument(1).charAt(0);

                        if (pChar == 'p' || pChar == 'P')
                        {
                            new BannerInfo(MESSAGE, String.valueOf(pageNumber));
                        }
                        else
                        {
                            /* FIRST CHARACTER IS NOT 'P' */
                            Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Make sure you're entering a number for the banner ID.");
                        }
                    }
                    catch (NumberFormatException | NullPointerException | StringIndexOutOfBoundsException f)
                    {
                        Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Please review the help menu.");
                    }
                }
            }
            else
            {
                Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Make sure you're entering a number for the banner ID.");
            }
        }
    }

    private void shopCommand()
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
                    else if (quantity > ShopSettingsParser.getMaxShopLimit())
                    {
                        quantity = ShopSettingsParser.getMaxShopLimit();
                    }

                    new Shop(MESSAGE, COMMAND_LINE.getArgument(1), quantity, true);
                }
                catch (NumberFormatException e)
                {
                    new Shop(MESSAGE, COMMAND_LINE.getArgument(1), 1, false);
                }
            }
            else
            {
                new Shop(MESSAGE, COMMAND_LINE.getArgument(1), 1, false);
            }
        }
        else
        {
            new Shop(MESSAGE);
        }
    }

    private void profileCommand()
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
                    new Profile(MESSAGE, userDiscordID, 2, arg2);
                }
                catch (NumberFormatException e)
                {
                    Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Make sure you're entering a number for the banner ID");
                }
            }
            else if (arg1.equalsIgnoreCase("search") || arg1.equalsIgnoreCase("s"))
            {
                new Profile(MESSAGE, userDiscordID, 3, arg2);
            }
            else
            {
                Sargo.replyToMessage_Warning(MESSAGE, "UNKNOWN ARGUMENT", "Please review the help menu.");
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            String arg1 = COMMAND_LINE.getArgument(1);
            if (arg1.equalsIgnoreCase("data") || arg1.equalsIgnoreCase("d"))
            {
                new Profile(MESSAGE, userDiscordID, 1);
            }
            else
            {
                Sargo.replyToMessage_Warning(MESSAGE, "UNKNOWN ARGUMENT", "Please review the help menu.");
            }
        }
        else
        {
            new Profile(MESSAGE, userDiscordID);
        }
    }

    private void userCommand()
    {
        Sargo.replyToMessage_Warning(MESSAGE, "DISCONTINUED COMMAND", "Command is no longer supported.");
        /*
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
                        Sargo.replyToMessage_Warning(MESSAGE, "UNKNOWN USER", "Could not find that user.");
                    }
                }
                catch (NumberFormatException e)
                {
                    Sargo.replyToMessage_Warning(MESSAGE, "UNKNOWN USER", "Could not find that user. Does their name have a space? Try `" + getCommandPrefix() + "user @[name]` instead.");
                }
            }
        }
        else
        {
            Sargo.replyToMessage_Warning(MESSAGE, "NOT ENOUGH ARGUMENTS", "Please review the help menu.");
        }
        */
    }

    private void resetCommand()
    {
        if (COMMAND_LINE.getArgumentCount() >= 3)
        {
            String arg3 = COMMAND_LINE.getArgument(3);
            String arg2 = COMMAND_LINE.getArgument(2);
            int bannerID;
            try
            {
                bannerID = Integer.parseInt(COMMAND_LINE.getArgument(1));
            }
            catch (NumberFormatException e)
            {
                return;
            }
            boolean yes = (arg3.equalsIgnoreCase("y") || arg3.equalsIgnoreCase("yes"));

            if (arg2.equalsIgnoreCase("c") ||
                    arg2.equalsIgnoreCase("w") ||
                    arg2.equalsIgnoreCase("a"))
            {
                new Reset(MESSAGE, userDiscordID, bannerID, arg2, yes);
            }
            else
            {
                Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Use `" + CommandSettingsParser.getCommandPrefix() + "reset [Banner ID] [c/w/a]`.");
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 2)
        {
            String arg2 = COMMAND_LINE.getArgument(2);
            int bannerID;
            try
            {
                bannerID = Integer.parseInt(COMMAND_LINE.getArgument(1));
            }
            catch (NumberFormatException e)
            {
                return;
            }

            if (arg2.equalsIgnoreCase("c") ||
                    arg2.equalsIgnoreCase("w") ||
                    arg2.equalsIgnoreCase("a"))
            {
                new Reset(MESSAGE, userDiscordID, bannerID, arg2, false);
            }
            else
            {
                Sargo.replyToMessage_Warning(MESSAGE, "COMMAND ERROR", "Use `" + CommandSettingsParser.getCommandPrefix() + "reset [Banner ID] [c/w/a]`.");
            }
        }
        else if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            String arg1 = COMMAND_LINE.getArgument(1);
            if (arg1.equalsIgnoreCase("y") || arg1.equalsIgnoreCase("yes"))
            {
                new Reset(MESSAGE, userDiscordID);
            }
        }
        else
        {
            new Reset(MESSAGE);
        }
    }

    private void updateCommand()
    {
        if (COMMAND_LINE.getArgumentCount() >= 1)
        {
            /* RESET */
            if (COMMAND_LINE.getArgument(1).equalsIgnoreCase("r"))
            {
                new Update(MESSAGE, true, true);
            }
            /* OVERWRITE */
            else if (COMMAND_LINE.getArgument(1).equalsIgnoreCase("o"))
            {
                new Update(MESSAGE, false, true);
            }
            else
            {
                new Update(MESSAGE, false, false);
            }
        }
        else
        {
            new Update(MESSAGE, false, false);
        }
    }

    private void reloadCommand()
    {
        Reload.reloadBanners();
        Reload.reloadSettings();
        Sargo.replyToMessage_Warning(MESSAGE, "FILES RELOADED", "The Banners and Settings xml files have been reloaded.");
    }

    public static String getCommandPrefix()
    {
        return CommandSettingsParser.getCommandPrefix() + "";
    }
}
