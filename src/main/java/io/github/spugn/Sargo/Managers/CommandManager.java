package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.Functions.BannerInfo;
import io.github.spugn.Sargo.Functions.Debug;
import io.github.spugn.Sargo.Functions.Help;
import io.github.spugn.Sargo.Functions.Scout;
import io.github.spugn.Sargo.Objects.Text;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Command.CommandLine;
import io.github.spugn.sdevkit.Discord.Discord4J.DiscordCommand;
import io.github.spugn.sdevkit.Discord.Discord4J.EmbedMessage;
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
                            }
                        }
                        catch (NumberFormatException f)
                        {
                            /* SECOND CHARACTER IS NOT AN INT */
                            new Message(CLIENT, CHANNEL, Text.SCOUT_NUMBER_FORMAT_EXCEPTION.get(), true, 255, 0, 0);
                        }
                        catch (NullPointerException f)
                        {
                            /* SECOND CHARACTER IS NOT AN INT */
                            new Message(CLIENT, CHANNEL, Text.SCOUT_NUMBER_FORMAT_EXCEPTION.get(), true, 255, 0, 0);
                        }
                    }
                    else
                    {
                        new Message(CLIENT, CHANNEL, Text.SCOUT_NUMBER_FORMAT_EXCEPTION.get(), true, 255, 0, 0);
                    }
                }

            }
            /* SHOP | "@bot shop [number of MDs]" */
            else if (commandLine.getCommand().equalsIgnoreCase("shop"))
            {
                /* TODO - IMPLEMENT CURRENCY */
            }
            /* INFO | "@bot info [mention user]" */
            else if (commandLine.getCommand().equalsIgnoreCase("info"))
            {
                /* TODO - GET AND DISPLAY USER DATA */
            }
            /* RESET | "@bot reset" */
            else if (commandLine.getCommand().equalsIgnoreCase("reset"))
            {
                /* TODO - ERASE PLAYER DATA */
            }
            else if (commandLine.getCommand().equalsIgnoreCase("debug"))
            {
                /* TODO - RESTRICT ACCESS */
                new Debug(CLIENT, CHANNEL, DISCORD_ID);
            }
            else
            {
                new Message(CLIENT, CHANNEL, Text.UNKNOWN_COMMAND.get(), true, 255, 0, 0);
            }
        }
    }

    private void sayWarning()
    {
        if (settings.isErrorInRates())
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_RATE_ERROR.get(), true, 255, 0, 0);
        }
    }
}
