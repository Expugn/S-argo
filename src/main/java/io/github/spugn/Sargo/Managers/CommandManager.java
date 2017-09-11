package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.Functions.*;
import io.github.spugn.Sargo.Objects.Text;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Command.CommandLine;
import io.github.spugn.sdevkit.Discord.Discord4J.DiscordCommand;
import io.github.spugn.sdevkit.Discord.Discord4J.Message;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class CommandManager
{
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;
    private static IMessage MESSAGE;
    private static SettingsParser settings;
    private static String DISCORD_ID;

    public CommandManager(IDiscordClient client, MessageReceivedEvent event)
    {
        CLIENT = client;
        CHANNEL = event.getChannel();
        MESSAGE = event.getMessage();
        settings = new SettingsParser();
        DISCORD_ID = MESSAGE.getAuthor().getStringID();
    }

    public void run()
    {
        /* READ COMMAND */
        DiscordCommand discordCommand = new DiscordCommand(CLIENT);

        discordCommand.setUseMention(settings.isUseMention());
        discordCommand.setCommandPrefix(settings.getCommandPrefix());
        discordCommand.setDeleteUserMessage(settings.isDeleteUserMessage());

        CommandLine commandLine = discordCommand.meetsConditions(MESSAGE);

        if (commandLine != null)
        {
            sayWarning();

            /* HELP | "@bot help" */
            if (commandLine.getCommand().equalsIgnoreCase("help"))
            {
                new Help(CLIENT, CHANNEL);
            }
            /* SCOUT | "@bot scout <banner id> [single/multi] [g/guaranteed]" */
            else if (commandLine.getCommand().equalsIgnoreCase("scout"))
            {
                try
                {
                    if (commandLine.getArgumentCount() >= 2)
                    {
                        new Scout(CLIENT, CHANNEL, Integer.parseInt(commandLine.getArgument(1)), commandLine.getArgument(2), DISCORD_ID);
                    }
                    else if (commandLine.getArgumentCount() >= 1)
                    {
                        new BannerInfo(CLIENT, CHANNEL, Integer.parseInt(commandLine.getArgument(1)));
                    }
                    else
                    {
                        new BannerInfo(CLIENT, CHANNEL, "1");
                    }
                }
                catch (NumberFormatException e)
                {
                    if (commandLine.getArgumentCount() == 1)
                    {
                        try
                        {
                            int pageNumber = Integer.parseInt(commandLine.getArgument(1).charAt(1) + "");
                            char pChar = commandLine.getArgument(1).charAt(0);

                            if (pChar == 'p' || pChar == 'P')
                            {
                                new BannerInfo(CLIENT, CHANNEL, String.valueOf(pageNumber));
                            }
                            else
                            {
                                /* FIRST CHARACTER IS NOT 'P' */
                                new Message(CLIENT, CHANNEL, Text.SCOUT_NUMBER_FORMAT_EXCEPTION.get(), true, 255, 0, 0);
                                CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
                            }
                        }
                        catch (NumberFormatException f)
                        {
                            /* SECOND CHARACTER IS NOT AN INT */
                            CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please provide a page number after 'p'.").get().build());
                        }
                        catch (NullPointerException f)
                        {
                            /* SECOND CHARACTER IS NOT AN INT */
                            CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please provide a page number after 'p'.").get().build());
                        }
                        catch (StringIndexOutOfBoundsException f)
                        {
                            CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Please provide a page number after 'p'.").get().build());
                        }
                    }
                    else
                    {
                        CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
                    }
                }

            }
            /* SHOP | "@bot shop [Item ID]" */
            else if (commandLine.getCommand().equalsIgnoreCase("shop"))
            {
                if (commandLine.getArgumentCount() >= 1)
                {
                    new Shop(CHANNEL, DISCORD_ID, commandLine.getArgument(1));
                }
                else
                {
                    new Shop(CHANNEL);
                }
            }
            /* PROFILE | "@bot profile [info/data/search]" */
            else if (commandLine.getCommand().equalsIgnoreCase("profile"))
            {
                if (commandLine.getArgumentCount() >= 2)
                {
                    if (commandLine.getArgument(1).equalsIgnoreCase("info") || commandLine.getArgument(1).equalsIgnoreCase("i"))
                    {
                        try
                        {
                            /* TEST IF ARGUMENT CAN BE PARSED INTO AN INTEGER */
                            Integer.parseInt(commandLine.getArgument(2));

                            /* OPEN BANNER INFO PROFILE */
                            new Profile(CHANNEL, DISCORD_ID, 2, commandLine.getArgument(2));
                        }
                        catch (NumberFormatException e)
                        {
                            CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Make sure you're entering a number for the banner ID.").get().build());
                        }
                    }
                    else if (commandLine.getArgument(1).equalsIgnoreCase("search") || commandLine.getArgument(1).equalsIgnoreCase("s"))
                    {
                        new Profile(CHANNEL, DISCORD_ID, 3, commandLine.getArgument(2));
                    }
                    else
                    {
                        CHANNEL.sendMessage(new WarningMessage("UNKNOWN ARGUMENT", "Please review the help menu.").get().build());
                    }
                }
                else if (commandLine.getArgumentCount() >= 1)
                {
                    if (commandLine.getArgument(1).equalsIgnoreCase("data") || commandLine.getArgument(1).equalsIgnoreCase("d"))
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
            /* RESET | "@bot reset [y]" */
            else if (commandLine.getCommand().equalsIgnoreCase("reset"))
            {
                if (commandLine.getArgumentCount() >= 1)
                {
                    if (commandLine.getArgument(1).equalsIgnoreCase("y") ||commandLine.getArgument(1).equalsIgnoreCase("yes"))
                    {
                        new Reset(CHANNEL, DISCORD_ID);
                    }
                }
                else
                {
                    new Reset(CHANNEL);
                }
            }
            else
            {
                CHANNEL.sendMessage(new WarningMessage("COMMAND ERROR", "Unknown command. Use 'help' for a list of commands.").get().build());
            }
        }
    }

    private void sayWarning()
    {
        if (settings.isErrorInRates())
        {
            CHANNEL.sendMessage(new WarningMessage("SCOUT RATE ERROR", "Scout rates do not add up to 1.0. Please review your settings file.").get().build());
        }
    }
}
