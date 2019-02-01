package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.Utilities.CommandLine;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import io.github.spugn.Sargo.XMLParsers.LoginSettingsParser;
import io.github.spugn.Sargo.XMLParsers.ScoutSettingsParser;
import io.github.spugn.Sargo.XMLParsers.ShopSettingsParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.File;
import java.util.*;

public class SettingsManager
{
    private IChannel channel;
    private CommandLine commandLine;
    private EmbedBuilder builder;

    private static final String AUTHOR_NAME = "Settings";

    private static final String C_DESCRIPTION = "Choose one of the following categories:";
    private static final String C_TITLE = "SETTING CATEGORIES";
    private static final String C_CONTENT = "`login`\n`command`\n`scout`\n`shop`";
    private final String C_FOOTER_TEXT = "Use '" + CommandManager.getCommandPrefix() + "settings [category]' to view more information.";

    private static final String S_DESCRIPTION = "[Read this for help with editing settings!](https://github.com/Expugn/S-argo/wiki/Settings)";
    private static final String S_WARNING_TITLE = "WARNING";
    private static final String E_CATEGORY_DESCRIPTION = "Invalid Category.";
    private static final String E_SETTING_DESCRIPTION = "Invalid Setting.";

    private static final String LS_TITLE = "LOGIN SETTINGS";
    private static final String CS_TITLE = "COMMAND SETTINGS";
    private static final String ScS_TITLE = "SCOUT SETTINGS";
    private static final String ShS_TITLE = "SHOP SETTINGS";

    public SettingsManager(IChannel c, final CommandLine commandLine)
    {
        channel = c;
        this.commandLine = commandLine;

        builder = new EmbedBuilder();
        builder.withAuthorName(AUTHOR_NAME);
        builder.withColor(85, 0, 128);

        run();
    }

    private void run()
    {
        if (commandLine.getArgumentCount() == 0)
        {
            // display setting categories
            buildCategories();
        }
        else
        {
            if (commandLine.getArgumentCount() == 1)
            {
                String requestedCategory = commandLine.getArgument(1).toLowerCase();

                // view category settings(command/scout/shop/login)
                if (requestedCategory.equals("login"))
                {
                    buildLoginSettings();
                }
                else if (requestedCategory.equals("command"))
                {
                    buildCommandSettings();
                }
                else if (requestedCategory.equals("scout"))
                {
                    buildScoutSettings();
                }
                else if (requestedCategory.equals("shop"))
                {
                    buildShopSettings();
                }
                else
                {
                    builder.withDescription(E_CATEGORY_DESCRIPTION);
                }
            }
            else if (commandLine.getArgumentCount() == 2)
            {
                String requestedCategory = commandLine.getArgument(1).toLowerCase();
                // view specific setting value
                if (requestedCategory.equals("login"))
                {
                    previewLoginSetting();
                }
                else if (requestedCategory.equals("command"))
                {
                    previewCommandSetting();
                }
                else if (requestedCategory.equals("scout"))
                {
                    previewScoutSetting();
                }
                else if (requestedCategory.equals("shop"))
                {
                    previewShopSetting();
                }
                else
                {
                    builder.withDescription(E_CATEGORY_DESCRIPTION);
                }
            }
            else if (commandLine.getArgumentCount() >= 3)
            {
                String requestedCategory = commandLine.getArgument(1).toLowerCase();
                if (requestedCategory.equals("login"))
                {
                    builder.withDescription("Login Settings can only be modified by editing `/data/settings/Login.xml`.");
                }
                else if (requestedCategory.equals("command"))
                {
                    editCommandSetting();
                }
                else if (requestedCategory.equals("scout"))
                {
                    editScoutSetting();
                }
                else if (requestedCategory.equals("shop"))
                {
                    editShopSetting();
                }
                else
                {
                    builder.withDescription(E_CATEGORY_DESCRIPTION);
                }
            }
        }
        sendEmbed();
    }

    //<editor-fold> desc="Command Setting Editors">
    private void editCommandSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();

        if (requestedSetting.equals("commandprefix"))
        {
            editCommandPrefix();
        }
        else if (requestedSetting.equals("deleteusermessage"))
        {
            editDeleteUserMessage();
        }
        else if (requestedSetting.equals("mainchannel"))
        {
            editMainChannel();
        }
        else if (requestedSetting.equals("blacklistedchannels"))
        {
            editBlacklistedChannels();
        }
        else if (requestedSetting.equals("whitelistedchannels"))
        {
            editWhitelistedChannels();
        }
        else
        {
            builder.withDescription(E_SETTING_DESCRIPTION);
        }
    }

    private void commandSaveAndReload()
    {
        CommandSettingsParser csp = new CommandSettingsParser();
        csp.saveFile();
        csp.reload();
    }

    private void editCommandPrefix()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                CommandSettingsParser.setCommandPrefix(newData.charAt(0));

                commandSaveAndReload();

                builder.withDescription("`CommandPrefix` set to: `" + CommandSettingsParser.getCommandPrefix() + "`.");
            }
            else
            {
                builder.withDescription("Command Error:\n`settings command commandprefix set [value]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings command commandprefix set [value]`");
        }
    }

    private void editDeleteUserMessage()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                if (newData.equalsIgnoreCase("true"))
                {
                    CommandSettingsParser.setDeleteUserMessage(true);
                }
                else if (newData.equalsIgnoreCase("false"))
                {
                    CommandSettingsParser.setDeleteUserMessage(false);
                }
                else
                {
                    builder.withDescription("Command Error:\n`settings command deleteusermessage set [true/false]`");
                }

                if (newData.equalsIgnoreCase("true") || newData.equalsIgnoreCase("false"))
                {
                    commandSaveAndReload();
                    builder.withDescription("`DeleteUserMessage` set to: `" + CommandSettingsParser.isDeleteUserMessage() + "`.");
                }

            }
            else
            {
                builder.withDescription("Command Error:\n`settings command deleteusermessage set [true/false]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings command deleteusermessage set [true/false]`");
        }
    }

    private void editMainChannel()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                if (newData.startsWith("<#") && newData.endsWith(">"))
                {
                    newData = newData.substring(2, newData.length() - 1);
                }

                CommandSettingsParser.setMainChannel(newData);
                commandSaveAndReload();
                try
                {
                    builder.withDescription("`MainChannel` set to: `" + Long.parseLong(CommandSettingsParser.getMainChannel()) + "`.");
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("`MainChannel` set to: `#" + CommandSettingsParser.getMainChannel() + "`.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings command mainchannel set [#channel/channel-name/channel-id]`");
            }
        }
        else if (requestedType.equals("remove"))
        {
            if (CommandSettingsParser.getMainChannel().isEmpty())
            {
                builder.withDescription("`MainChannel` is already empty.");
            }
            else
            {
                CommandSettingsParser.setMainChannel("");
                commandSaveAndReload();
                builder.withDescription("`MainChannel` removed.");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings command mainchannel set [#channel/channel-name/channel-id]`\n`settings command mainchannel remove`");
        }
    }

    private void editBlacklistedChannels()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("add"))
        {
            if (!newData.isEmpty())
            {
                if (newData.startsWith("<#") && newData.endsWith(">"))
                {
                    newData = newData.substring(2, newData.length() - 1);
                }

                List<String> blacklist = CommandSettingsParser.getBlacklistedChannels();
                if (blacklist.contains(newData))
                {
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` already exists in `BlacklistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` already exists in `BlacklistedChannels`.");
                    }
                }
                else
                {
                    blacklist.add(newData);
                    CommandSettingsParser.setBlacklistedChannels(blacklist);
                    commandSaveAndReload();

                    try
                    {
                        builder.withDescription("`BlacklistedChannels` added: `" + Long.parseLong(newData) + "`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("`BlacklistedChannels` added: `#" + newData + "`.");
                    }
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings command blacklistedchannels add [#channel/channel-name/channel-id]`");
            }
        }
        else if (requestedType.equals("remove"))
        {
            if (!newData.isEmpty())
            {
                if (newData.startsWith("<#") && newData.endsWith(">"))
                {
                    newData = newData.substring(2, newData.length() - 1);
                }

                List<String> blacklist = CommandSettingsParser.getBlacklistedChannels();
                if (blacklist.remove(newData))
                {
                    CommandSettingsParser.setBlacklistedChannels(blacklist);
                    commandSaveAndReload();
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` removed from `BlacklistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` removed from `BlacklistedChannels`.");
                    }
                }
                else
                {
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` not found in `BlacklistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` not found in `BlacklistedChannels`.");
                    }
                }
            }
            else
            {
                CommandSettingsParser.setBlacklistedChannels(Collections.emptyList());
                commandSaveAndReload();
                builder.withDescription("All data in `BlacklistedChannels` has been cleared.");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n" +
                    "`settings command blacklistedchannels add [#channel/channel-name/channel-id]`\n" +
                    "`settings command blacklistedchannels remove [#channel/channel-name/channel-id]`\n" +
                    "`settings command blacklistedchannels remove`");
        }
    }

    private void editWhitelistedChannels()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("add"))
        {
            if (!newData.isEmpty())
            {
                if (newData.startsWith("<#") && newData.endsWith(">"))
                {
                    newData = newData.substring(2, newData.length() - 1);
                }

                List<String> whitelist = CommandSettingsParser.getWhitelistedChannels();
                if (whitelist.contains(newData))
                {
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` already exists in `WhitelistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` already exists in `WhitelistedChannels`.");
                    }
                }
                else
                {
                    whitelist.add(newData);
                    CommandSettingsParser.setWhitelistedChannels(whitelist);
                    commandSaveAndReload();

                    try
                    {
                        builder.withDescription("`WhitelistedChannels` added: `" + Long.parseLong(newData) + "`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("`WhitelistedChannels` added: `#" + newData + "`.");
                    }
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings command whitelistedchannels add [#channel/channel-name/channel-id]`");
            }
        }
        else if (requestedType.equals("remove"))
        {
            if (!newData.isEmpty())
            {
                if (newData.startsWith("<#") && newData.endsWith(">"))
                {
                    newData = newData.substring(2, newData.length() - 1);
                }

                List<String> whitelist = CommandSettingsParser.getWhitelistedChannels();
                if (whitelist.remove(newData))
                {
                    CommandSettingsParser.setWhitelistedChannels(whitelist);
                    commandSaveAndReload();
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` removed from `WhitelistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` removed from `WhitelistedChannels`.");
                    }
                }
                else
                {
                    try
                    {
                        builder.withDescription("Channel `" + Long.parseLong(newData) + "` not found in `WhitelistedChannels`.");
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("Channel `#" + newData + "` not found in `WhitelistedChannels`.");
                    }
                }
            }
            else
            {
                CommandSettingsParser.setWhitelistedChannels(Collections.emptyList());
                commandSaveAndReload();
                builder.withDescription("All data in `WhitelistedChannels` has been cleared.");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n" +
                    "`settings command whitelistedchannels add [#channel/channel-name/channel-id]`\n" +
                    "`settings command whitelistedchannels remove [#channel/channel-name/channel-id]`\n" +
                    "`settings command whitelistedchannels remove`");
        }
    }
    //</editor-fold>

    //<editor-fold> desc="Scout Setting Editors">
    private void editScoutSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();

        if (requestedSetting.equals("disableimages"))
        {
            editDisableImages();
        }
        else if (requestedSetting.equals("simplemessage"))
        {
            editSimpleMessage();
        }
        else if (requestedSetting.equals("raritystars"))
        {
            editRarityStars();
        }
        else if (requestedSetting.equals("scoutmaster"))
        {
            editScoutMaster();
        }
        else if (requestedSetting.equals("rates"))
        {
            editRates();
        }
        else if (requestedSetting.equals("recordcrystal"))
        {
            editRecordCrystal();
        }
        else if (requestedSetting.equals("circulatingrecordcrystal"))
        {
            editCirculatingRecordCrystal();
        }
        else
        {
            builder.withDescription(E_SETTING_DESCRIPTION);
        }
    }

    private void scoutSaveAndReload()
    {
        ScoutSettingsParser ssp = new ScoutSettingsParser();
        ssp.saveFile();
        ssp.reload();
    }

    private void editDisableImages()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                if (newData.equalsIgnoreCase("true"))
                {
                    ScoutSettingsParser.setDisableImages(true);
                }
                else if (newData.equalsIgnoreCase("false"))
                {
                    ScoutSettingsParser.setDisableImages(false);
                }
                else
                {
                    builder.withDescription("Command Error:\n`settings scout disableimages set [true/false]`");
                }

                if (newData.equalsIgnoreCase("true") || newData.equalsIgnoreCase("false"))
                {
                    scoutSaveAndReload();
                    builder.withDescription("`DisableImages` set to: `" + ScoutSettingsParser.isDisableImages() + "`.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout disableimages set [true/false]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings scout disableimages set [true/false]`");
        }
    }

    private void editSimpleMessage()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                if (newData.equalsIgnoreCase("true"))
                {
                    ScoutSettingsParser.setSimpleMessage(true);
                }
                else if (newData.equalsIgnoreCase("false"))
                {
                    ScoutSettingsParser.setSimpleMessage(false);
                }
                else
                {
                    builder.withDescription("Command Error:\n`settings scout simplemessage set [true/false]`");
                }

                if (newData.equalsIgnoreCase("true") || newData.equalsIgnoreCase("false"))
                {
                    scoutSaveAndReload();
                    builder.withDescription("`SimpleMessage` set to: `" + ScoutSettingsParser.isSimpleMessage() + "`.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout simplemessage set [true/false]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings scout simplemessage set [true/false]`");
        }
    }

    private void editRarityStars()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                if (newData.equalsIgnoreCase("true"))
                {
                    ScoutSettingsParser.setRarityStars(true);
                }
                else if (newData.equalsIgnoreCase("false"))
                {
                    ScoutSettingsParser.setRarityStars(false);
                }
                else
                {
                    builder.withDescription("Command Error:\n`settings scout raritystars set [true/false]`");
                }

                if (newData.equalsIgnoreCase("true") || newData.equalsIgnoreCase("false"))
                {
                    scoutSaveAndReload();
                    builder.withDescription("`RarityStars` set to: `" + ScoutSettingsParser.isRarityStars() + "`.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout raritystars set [true/false]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings scout raritystars set [true/false]`");
        }
    }

    private void editScoutMaster()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                ScoutSettingsParser.setScoutMaster(newData);
                scoutSaveAndReload();
                builder.withDescription("`ScoutMaster` set to: `" + ScoutSettingsParser.getScoutMaster() + ".xml`.");
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout scoutmaster set [file-name]`");
            }
        }
        else if (requestedType.equals("remove"))
        {
            if (ScoutSettingsParser.getScoutMaster().isEmpty())
            {
                builder.withDescription("`ScoutMaster` is already empty.");
            }
            else
            {
                ScoutSettingsParser.setScoutMaster("");
                scoutSaveAndReload();
                builder.withDescription("`ScoutMaster` removed.");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings scout scoutmaster set [file-name]`\n`settings scout scoutmaster remove`");
        }
    }

    private void editRates()
    {
        // settings scout rates copper 0.31
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("copper"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        ScoutSettingsParser.setCopperRate(newData_double);
                        scoutSaveAndReload();
                        builder.withDescription("`Copper` set to: `" + ScoutSettingsParser.getCopperRate() + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout rates copper [percentage-in-decimal]`");
            }
        }
        else if (requestedType.equals("silver"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        ScoutSettingsParser.setSilverRate(newData_double);
                        scoutSaveAndReload();
                        builder.withDescription("`Silver` set to: `" + ScoutSettingsParser.getSilverRate() + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout rates silver [percentage-in-decimal]`");
            }
        }
        else if (requestedType.equals("gold"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        ScoutSettingsParser.setGoldRate(newData_double);
                        scoutSaveAndReload();
                        builder.withDescription("`Gold` set to: `" + ScoutSettingsParser.getGoldRate() + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout rates gold [percentage-in-decimal]`");
            }
        }
        else if (requestedType.equals("platinum"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        ScoutSettingsParser.setPlatinumRate(newData_double);
                        scoutSaveAndReload();
                        builder.withDescription("`Platinum` set to: `" + ScoutSettingsParser.getPlatinumRate() + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout rates platinum [percentage-in-decimal]`");
            }
        }
        else if (requestedType.equals("platinum6"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        ScoutSettingsParser.setPlatinum6Rate(newData_double);
                        scoutSaveAndReload();
                        builder.withDescription("`Platinum6` set to: `" + ScoutSettingsParser.getPlatinum6Rate() + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout rates platinum6 [percentage-in-decimal]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings scout rates copper [percentage-in-decimal]`\n" +
                    "`settings scout rates silver [percentage-in-decimal]`\n" +
                    "`settings scout rates gold [percentage-in-decimal]`\n" +
                    "`settings scout rates platinum [percentage-in-decimal]`\n" +
                    "`settings scout rates platinum6 [percentage-in-decimal]`");
        }
    }

    private void editRecordCrystal()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        int requestedType_int = -1;
        try
        {
            requestedType_int = Integer.parseInt(requestedType);
        }
        catch (NumberFormatException e)
        {
            //builder.withDescription("RecordCrystal must be within `1` - `10`.");
        }

        if (requestedType_int >= 1 && requestedType_int <= 10)
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        List<Double> recordCrystal = ScoutSettingsParser.getRecordCrystalRates();
                        recordCrystal.set(requestedType_int, newData_double);
                        ScoutSettingsParser.setRecordCrystalRates(recordCrystal);
                        scoutSaveAndReload();
                        builder.withDescription("`RC " + requestedType_int + "` set to: `" + ScoutSettingsParser.getRecordCrystalRates().get(requestedType_int) + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout recordcrystal [1-10] [percentage-in-decimal]`");
            }
        }
        else
        {
            builder.withDescription("Record Crystal must be within `1` - `10`.");
        }
    }

    private void editCirculatingRecordCrystal()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        int requestedType_int = -1;
        try
        {
            requestedType_int = Integer.parseInt(requestedType);
        }
        catch (NumberFormatException e)
        {
            //builder.withDescription("Circulating Record Crystal must be within `1` - `5`.");
        }

        if (requestedType_int >= 1 && requestedType_int <= 5)
        {
            if (!newData.isEmpty())
            {
                try
                {
                    double newData_double = Double.parseDouble(newData);
                    if (newData_double >= 0 && newData_double <= 1.0)
                    {
                        List<Double> cRecordCrystal = ScoutSettingsParser.getCirculatingRecordCrystalRates();
                        cRecordCrystal.set(requestedType_int, newData_double);
                        ScoutSettingsParser.setCirculatingRecordCrystalRates(cRecordCrystal);
                        scoutSaveAndReload();
                        builder.withDescription("`CRC " + requestedType_int + "` set to: `" + ScoutSettingsParser.getCirculatingRecordCrystalRates().get(requestedType_int) + "`.");
                    }
                    else
                    {
                        builder.withDescription("Provided rate within `0` - `1.0`.");
                    }
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Provided rate is not a number.");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings scout circulatingrecordcrystal [1-5] [percentage-in-decimal]`");
            }
        }
        else
        {
            builder.withDescription("Circulating Record Crystal must be within `1` - `5`.");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Shop Setting Editors">
    private void editShopSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();

        if (requestedSetting.equals("maxshoplimit"))
        {
            editMaxShopLimit();
        }
        else if (requestedSetting.equals("shopitems"))
        {
            editShopItems();
        }
        else
        {
            builder.withDescription(E_SETTING_DESCRIPTION);
        }
    }

    private void shopSaveAndReload()
    {
        ShopSettingsParser ssp = new ShopSettingsParser();
        ssp.saveFile();
        ssp.reload();
    }

    private void editMaxShopLimit()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String newData = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");

        if (requestedType.equals("set"))
        {
            if (!newData.isEmpty())
            {
                try
                {
                    int newData_int = Integer.parseInt(newData);
                    if (newData_int < 0)
                    {
                        throw new NumberFormatException();
                    }

                    ShopSettingsParser.setMaxShopLimit(newData_int);
                    shopSaveAndReload();
                    builder.withDescription("`MaxShopLimit` set to: `" + ShopSettingsParser.getMaxShopLimit() + "`.");
                }
                catch (NumberFormatException e)
                {
                    builder.withDescription("Value must be greater than `0`");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings shop maxshoplimit set [value]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings shop maxshoplimit set [value]`");
        }
    }

    private void editShopItems()
    {
        String requestedType = commandLine.getArgument(3).toLowerCase();
        String itemName = (commandLine.getArgumentCount() >= 4 ? commandLine.getArgument(4) : "");
        String itemPrice = (commandLine.getArgumentCount() >= 5 ? commandLine.getArgument(5) : "");
        String itemAmount = (commandLine.getArgumentCount() >= 6 ? commandLine.getArgument(6) : "");

        if (requestedType.equals("add"))
        {
            if (!itemName.isEmpty())
            {
                if (!itemPrice.isEmpty())
                {
                    double itemPrice_double;
                    try
                    {
                        itemPrice_double = Double.parseDouble(itemPrice);

                        if (itemPrice_double < 0)
                        {
                            builder.withDescription("`item-price` must be greater than `0`.");
                            return;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        builder.withDescription("`item-price` must be a number.");
                        return;
                    }

                    if (!itemAmount.isEmpty())
                    {
                        int itemAmount_int;
                        try
                        {
                            itemAmount_int = Integer.parseInt(itemAmount);

                            if (itemAmount_int < 0)
                            {
                                builder.withDescription("`item-amount` must be greater than `0`.");
                                return;
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            builder.withDescription("`item-amount` must be a number.");
                            return;
                        }

                        // construct item.
                        boolean itemExists = false;
                        Map<String, Map<Double, Integer>> shopItems = ShopSettingsParser.getShopItems();
                        String itemName_modified = itemName.replaceAll("_", " ");
                        Map<Double, Integer> item = new TreeMap<>();
                        item.put(itemPrice_double, itemAmount_int);
                        if (shopItems.containsKey(itemName_modified))
                        {
                            itemExists = true;
                        }
                        shopItems.put(itemName_modified, item);

                        ShopSettingsParser.setShopItems(shopItems);
                        shopSaveAndReload();
                        if (itemExists)
                        {
                            builder.withDescription("`" + itemName_modified + "` listing edited to: [$`" + itemPrice_double + "`] : `" + itemAmount_int + "` MD");
                        }
                        else
                        {
                            builder.withDescription("\"`" + itemName_modified + "` [$`" + itemPrice_double + "`] : `" + itemAmount_int + "` MD\" added to `ShopItems`.");
                        }
                    }
                    else
                    {
                        builder.withDescription("Command Error:\n`settings shop shopitems add [item-name] [item-price] [item-amount]`\n" +
                                "`settings shop shopitems add [item-name_with_underscores_for_spaces] [item-price] [item-amount]`\n");
                    }
                }
                else
                {
                    builder.withDescription("Command Error:\n`settings shop shopitems add [item-name] [item-price] [item-amount]`\n" +
                            "`settings shop shopitems add [item-name_with_underscores_for_spaces] [item-price] [item-amount]`\n");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings shop shopitems add [item-name] [item-price] [item-amount]`\n" +
                        "`settings shop shopitems add [item-name_with_underscores_for_spaces] [item-price] [item-amount]`\n");
            }
        }
        else if (requestedType.equals("remove"))
        {
            if (!itemName.isEmpty())
            {
                String itemName_modified = itemName.replaceAll("_", " ");
                Map<String, Map<Double, Integer>> shopItems = ShopSettingsParser.getShopItems();
                if (shopItems.remove(itemName_modified) != null)
                {
                    ShopSettingsParser.setShopItems(shopItems);
                    shopSaveAndReload();
                    builder.withDescription("`" + itemName_modified + "` removed from `ShopItems`");
                }
                else
                {
                    builder.withDescription("`" + itemName_modified + "` could not be found in `ShopItems`");
                }
            }
            else
            {
                builder.withDescription("Command Error:\n`settings shop shopitems remove [item-name]`\n" +
                        "`settings shop shopitems remove [item-name_with_underscores_for_spaces]`");
            }
        }
        else
        {
            builder.withDescription("Command Error:\n`settings shop shopitems add [item-name] [item-price] [item-amount]`\n" +
                    "`settings shop shopitems add [item-name_with_underscores_for_spaces] [item-price] [item-amount]`\n" +
                    "`settings shop shopitems remove [item-name]`\n" +
                    "`settings shop shopitems remove [item-name_with_underscores_for_spaces]`");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Setting Previewers">
    private void previewLoginSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();
        String LS_DESCRIPTION;
        if (requestedSetting.equals("token"))
        {
            LS_DESCRIPTION = "`Token` is private information. To view the token, open `/data/settings/Login.xml`";
        }
        else if (requestedSetting.equals("botownerid"))
        {
            LS_DESCRIPTION = "`BotOwnerID`\n\t- " + LoginSettingsParser.getBotOwnerDiscordID() + "\n\t- `" + botOwnerIDToString() + "`";
        }
        else if (requestedSetting.equals("githubdatarepository"))
        {
            LS_DESCRIPTION = "`GitHubDataRepository`\n\t- `" + LoginSettingsParser.getGitHubRepoURL() + "`\n\t- " + "[Link](" + LoginSettingsParser.getGitHubRepoURL().replaceAll("https://raw.githubusercontent.com/", "https://github.com/").replaceAll("/master/", "") + ")";
        }
        else
        {
            LS_DESCRIPTION = E_SETTING_DESCRIPTION;
        }

        builder.withDescription(LS_DESCRIPTION);
    }

    private void previewCommandSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();
        String CS_DESCRIPTION;
        if (requestedSetting.equals("commandprefix"))
        {
            CS_DESCRIPTION = "`CommandPrefix`\n\t- `" + CommandSettingsParser.getCommandPrefix() + "`";
        }
        else if (requestedSetting.equals("deleteusermessage"))
        {
            CS_DESCRIPTION = "`DeleteUserMessage`\n\t- `" + (CommandSettingsParser.isDeleteUserMessage() ? "Command messages that users send will be deleted." : "Command messages that user send will persist.") + "`";
        }
        else if (requestedSetting.equals("mainchannel"))
        {
            boolean mainChannelExists = !CommandSettingsParser.getMainChannel().isEmpty();
            CS_DESCRIPTION = "`MainChannel`\n\t- " + (mainChannelExists ? readChannelID(CommandSettingsParser.getMainChannel()) + "\n\t- Using `WHITELIST` mode." : "`MainChannel` has not been set.\n\t- Using `BLACKLIST` mode.");
        }
        else if (requestedSetting.equals("blacklistedchannels"))
        {
            boolean mainChannelExists = !CommandSettingsParser.getMainChannel().isEmpty();
            CS_DESCRIPTION = "`BlacklistedChannels`\n";
            List<String> blacklistedChannels = CommandSettingsParser.getBlacklistedChannels();
            if (blacklistedChannels.isEmpty())
            {
                CS_DESCRIPTION += "\t- `none`\n";
            }
            else
            {
                int counter = 0;
                for (String c : blacklistedChannels)
                {
                    CS_DESCRIPTION += "\t- " + readChannelID(c) + "\n";
                    counter++;

                    if (counter >= 10)
                    {
                        CS_DESCRIPTION += "\t`and " + (blacklistedChannels.size() - 10) + " more...`\n";
                        break;
                    }
                }
            }

            CS_DESCRIPTION += (mainChannelExists ? "`BlacklistedChannels` are **NOT** in use!" : "`BlacklistedChannels` are in use.");
        }
        else if (requestedSetting.equals("whitelistedchannels"))
        {
            boolean mainChannelExists = !CommandSettingsParser.getMainChannel().isEmpty();
            CS_DESCRIPTION = "`WhitelistedChannels`\n";
            List<String> whitelistedChannels = CommandSettingsParser.getWhitelistedChannels();
            if (whitelistedChannels.isEmpty())
            {
                CS_DESCRIPTION += "\t- `none`\n";
            }
            else
            {
                int counter = 0;
                for (String c : whitelistedChannels)
                {
                    CS_DESCRIPTION += "\t- " + readChannelID(c) + "\n";
                    counter++;

                    if (counter >= 10)
                    {
                        CS_DESCRIPTION += "\t`and " + (whitelistedChannels.size() - 10) + " more...`\n";
                        break;
                    }
                }
            }

            CS_DESCRIPTION += (mainChannelExists ? "`WhitelistedChannels` are in use." : "`WhitelistedChannels` are **NOT** in use!");
        }
        else
        {
            CS_DESCRIPTION = E_SETTING_DESCRIPTION;
        }

        builder.withDescription(CS_DESCRIPTION);
    }

    private void previewScoutSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();
        String ScS_DESCRIPTION;
        if (requestedSetting.equals("disableimages"))
        {
            ScS_DESCRIPTION = "`DisableImages`\n\t- " + (ScoutSettingsParser.isDisableImages() ? "Image results for scouts are disabled." : "Users can generate image results.");
        }
        else if (requestedSetting.equals("simplemessage"))
        {
            ScS_DESCRIPTION = "`SimpleMessage`\n\t- " + (ScoutSettingsParser.isSimpleMessage() ? "Scout results will be displayed in a simple text format." : "Scout results will be displayed using EmbedMessages.");
        }
        else if (requestedSetting.equals("raritystars"))
        {
            ScS_DESCRIPTION = "`RarityStars`\n\t- " + (ScoutSettingsParser.isRarityStars() ? "Gold stars will be added to show the rarity of characters/weapons." : "Gold stars will not be added to show the rarity of characters/weapons.");
        }
        else if (requestedSetting.equals("scoutmaster"))
        {
            ScS_DESCRIPTION = "`ScoutMaster`\n";
            String scoutMasterName = ScoutSettingsParser.getScoutMaster();
            if (!scoutMasterName.isEmpty())
            {
                String scoutMaster = ScoutSettingsParser.getScoutMaster() + ".xml";
                File scoutMasterFile = new File("data/mods/" + scoutMasterName + ".xml");
                if (!scoutMasterFile.exists())
                {
                    ScS_DESCRIPTION = "\t- Using the default `Argo` ScoutMaster (" + scoutMaster + " does not exist!).";
                }
                else
                {
                    ScS_DESCRIPTION = "\t- Using `" + scoutMaster + "`.";
                }

            }
            else
            {
                ScS_DESCRIPTION = "\t- Using the default `Argo` ScoutMaster.";
            }
        }
        else if (requestedSetting.equals("rates"))
        {
            ScS_DESCRIPTION =   "`Rates`\n" +
                                "\t- `Copper (2★)` : " + ScoutSettingsParser.getCopperRate() + "\n" +
                                "\t- `Silver (3★)` : " + ScoutSettingsParser.getSilverRate() + "\n" +
                                "\t- `Gold (4★)` : " + ScoutSettingsParser.getGoldRate() + "\n" +
                                "\t- `Platinum (5★)` : " + ScoutSettingsParser.getPlatinumRate() + "\n" +
                                "\t- `Platinum6 (6★)` : " + ScoutSettingsParser.getPlatinum6Rate() + "\n";
        }
        else if (requestedSetting.equals("recordcrystal"))
        {
            ScS_DESCRIPTION = "`RecordCrystal`\n";
            int rcCounter = 0;
            List<Double> recordCrystalRates = ScoutSettingsParser.getRecordCrystalRates();
            for (double d : recordCrystalRates)
            {
                if (rcCounter >= 1)
                {
                    ScS_DESCRIPTION += "\t- `" + rcCounter + " RC` : " + d + "\n";
                }
                rcCounter++;
                if (rcCounter > 10)
                {
                    break;
                }
            }
        }
        else if (requestedSetting.equals("circulatingrecordcrystal"))
        {
            ScS_DESCRIPTION = "`CirculatingRecordCrystal`\n";
            int crcCounter = 0;
            List<Double> circulatingRecordCrystalRates = ScoutSettingsParser.getCirculatingRecordCrystalRates();
            for (double d : circulatingRecordCrystalRates)
            {
                if (crcCounter >= 1)
                {
                    ScS_DESCRIPTION += "\t- `" + crcCounter + " RC` : " + d + "\n";
                }
                crcCounter++;
                if (crcCounter > 5)
                {
                    break;
                }
            }
        }
        else
        {
            ScS_DESCRIPTION = E_SETTING_DESCRIPTION;
        }

        builder.withDescription(ScS_DESCRIPTION);
    }

    private void previewShopSetting()
    {
        String requestedSetting = commandLine.getArgument(2).toLowerCase();
        String ShS_DESCRIPTION;
        if (requestedSetting.equals("maxshoplimit"))
        {
            ShS_DESCRIPTION = "`MaxShopLimit`\n\t- `" + ShopSettingsParser.getMaxShopLimit() + "`";
        }
        else if (requestedSetting.equals("shopitems"))
        {
            ShS_DESCRIPTION = "`ShopItems`\n";
            int counter = 0;
            Set<Map.Entry<String, Map<Double, Integer>>> entrySet = ShopSettingsParser.getShopItems().entrySet();
            for (Map.Entry<String, Map<Double, Integer>> entry : entrySet)
            {
                String itemName = entry.getKey();
                double price = 0.0;
                int amount = 0;

                for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
                {
                    price = entry2.getKey();
                    amount = entry2.getValue();
                }

                ShS_DESCRIPTION += "\t- `" + itemName + "` [$`" + price + "`] : `" + amount + "` Memory Diamonds\n";

                counter++;
                if (counter >= 10)
                {
                    ShS_DESCRIPTION += "\t`and " + (entrySet.size() - 10) + " more...`";
                    break;
                }
            }
        }
        else
        {
            ShS_DESCRIPTION = E_SETTING_DESCRIPTION;
        }

        builder.withDescription(ShS_DESCRIPTION);
    }
    //</editor-fold>

    //<editor-fold desc="Category Builders">
    private void buildCategories()
    {
        builder.withDescription(C_DESCRIPTION);
        builder.appendField(C_TITLE, C_CONTENT, false);
        builder.withFooterText(C_FOOTER_TEXT);
    }

    private void buildLoginSettings()
    {
        String LS_CONTENT = "`Token` - " + "`PRIVATE`" + "\n" +
                            "`BotOwnerID` - " + LoginSettingsParser.getBotOwnerDiscordID() + " (`"+ botOwnerIDToString() + "`)" + "\n" +
                            "`GitHubDataRepository` - [Link](" + LoginSettingsParser.getGitHubRepoURL().replaceAll("https://raw.githubusercontent.com/", "https://github.com/").replaceAll("/master/", "") + ")";

        builder.withDescription(S_DESCRIPTION);
        builder.appendField(LS_TITLE, LS_CONTENT, false);
    }

    private void buildCommandSettings()
    {
        String CS_CONTENT;
        if (CommandSettingsParser.getMainChannel().isEmpty())
        {
            CS_CONTENT =    "`CommandPrefix` - " + CommandSettingsParser.getCommandPrefix() + "\n" +
                            "`DeleteUserMessage` - " + CommandSettingsParser.isDeleteUserMessage() + "\n" +
                            "`MainChannel` - `none`" + "\n" +
                            "`BlacklistedChannels:`\n";
            List<String> blacklistedChannels = CommandSettingsParser.getBlacklistedChannels();
            if (blacklistedChannels.isEmpty())
            {
                CS_CONTENT += "\t- `none`";
            }
            else
            {
                int counter = 0;
                for (String c : blacklistedChannels)
                {
                    CS_CONTENT += "\t- " + readChannelID(c) + "\n";
                    counter++;

                    if (counter >= 10)
                    {
                        CS_CONTENT += "\t`and " + (blacklistedChannels.size() - 10) + " more...`";
                        break;
                    }
                }
            }
        }
        else
        {
            String mainChannel;
            mainChannel = readChannelID(CommandSettingsParser.getMainChannel());

            CS_CONTENT =    "`CommandPrefix` - " + CommandSettingsParser.getCommandPrefix() + "\n" +
                            "`DeleteUserMessage` - " + CommandSettingsParser.isDeleteUserMessage() + "\n" +
                            "`MainChannel` - " + mainChannel + "\n" +
                            "`WhitelistedChannels:`\n";
            List<String> whitelistedChannels = CommandSettingsParser.getWhitelistedChannels();
            if (whitelistedChannels.isEmpty())
            {
                CS_CONTENT += "\t- `none`";
            }
            int counter = 0;
            for (String c : whitelistedChannels)
            {
                CS_CONTENT += "\t- " + readChannelID(c) + "\n";
                counter++;

                if (counter >= 10)
                {
                    CS_CONTENT += "\t`and " + (whitelistedChannels.size() - 10) + " more...`";
                    break;
                }
            }
        }

        builder.withDescription(S_DESCRIPTION);
        builder.appendField(CS_TITLE, CS_CONTENT, false);
    }

    private void buildScoutSettings()
    {
        String scoutMasterName = ScoutSettingsParser.getScoutMaster();
        String scoutMaster;
        if (!scoutMasterName.isEmpty())
        {
            scoutMaster = ScoutSettingsParser.getScoutMaster() + ".xml";
        }
        else
        {
            scoutMaster = "`none`";
        }

        double copperRate = ScoutSettingsParser.getCopperRate();
        double silverRate = ScoutSettingsParser.getSilverRate();
        double goldRate = ScoutSettingsParser.getGoldRate();
        double platinumRate = ScoutSettingsParser.getPlatinumRate();
        double platinum6Rate = ScoutSettingsParser.getPlatinum6Rate();

        String ScS_CONTENT =    "`DisableImages` - " + ScoutSettingsParser.isDisableImages() + "\n" +
                                "`SimpleMessage` - " + ScoutSettingsParser.isSimpleMessage() + "\n" +
                                "`RarityStars` - " + ScoutSettingsParser.isRarityStars() + "\n" +
                                "`ScoutMaster` - " + scoutMaster + "\n" +
                                "`Rates:` \n" +
                                "\t`Copper` - " + copperRate + "\n" +
                                "\t`Silver` - " + silverRate + "\n" +
                                "\t`Gold` - " + goldRate + "\n" +
                                "\t`Platinum` - " + platinumRate + "\n" +
                                "\t`Platinum6` - " + platinum6Rate + "\n" +
                                "`RecordCrystal:` \n";

        int rcCounter = 0;
        List<Double> recordCrystalRates = ScoutSettingsParser.getRecordCrystalRates();
        for (double d : recordCrystalRates)
        {
            if (rcCounter >= 1)
            {
                ScS_CONTENT += "\t`" + rcCounter + " RC` - " + d + "\n";
            }
            rcCounter++;
            if (rcCounter > 10)
            {
                break;
            }
        }

        ScS_CONTENT += "`CirculatingRecordCrystal:` \n";

        int crcCounter = 0;
        List<Double> circulatingRecordCrystalRates = ScoutSettingsParser.getCirculatingRecordCrystalRates();
        for (double d : circulatingRecordCrystalRates)
        {
            if (crcCounter >= 1)
            {
                ScS_CONTENT += "\t`" + crcCounter + " CRC` - " + d + "\n";
            }
            crcCounter++;
            if (crcCounter > 5)
            {
                break;
            }
        }




        String ScS_WARNINGS = "";
        // CHECK SCOUT MASTER
        if (!scoutMasterName.isEmpty())
        {
            File scoutMasterFile = new File("data/mods/" + scoutMasterName + ".xml");
            if (!scoutMasterFile.exists())
            {
                ScS_WARNINGS += ":warning: `" + scoutMaster + "` does not exist in the `data/mods/` directory.\n";
            }
        }

        // CHECK RATES
        double totalRates = copperRate + silverRate + goldRate + platinumRate + platinum6Rate;
        if (totalRates != 1.0)
        {
            ScS_WARNINGS += ":warning: `Rates` (`" + totalRates + "`) does not add up to `1.0`.\n";
        }

        // CHECK RECORD CRYSTAL RATES
        double totalRCRates = 0.0;
        rcCounter = 0;
        for (double d : recordCrystalRates)
        {
            if (rcCounter >= 1)
            {
                totalRCRates += d;
            }
            rcCounter++;
            if (rcCounter > 10)
            {
                break;
            }
        }
        if (totalRCRates != 1.0)
        {
            ScS_WARNINGS += ":warning: `RecordCrystal` (`" + totalRCRates + "`) does not add up to `1.0`.\n";
        }

        // CHECK CIRCULATING RECORD CRYSTAL RATES
        double totalCRCRates = 0.0;
        crcCounter = 0;
        for (double d : circulatingRecordCrystalRates)
        {
            if (crcCounter >= 1)
            {
                totalCRCRates += d;
            }
            crcCounter++;
            if (crcCounter > 5)
            {
                break;
            }
        }
        if (totalCRCRates != 1.0)
        {
            ScS_WARNINGS += ":warning: `CirculatingRecordCrystal` (`" + totalCRCRates + "`) does not add up to `1.0`.\n";
        }


        builder.withDescription(S_DESCRIPTION);
        builder.appendField(ScS_TITLE, ScS_CONTENT, false);
        if (!ScS_WARNINGS.isEmpty())
            builder.appendField(S_WARNING_TITLE, ScS_WARNINGS, false);
    }

    private void buildShopSettings()
    {
        String ShS_CONTENT =    "`MaxShopLimit` - " + ShopSettingsParser.getMaxShopLimit() + "\n" +
                                "`ShopItems:` \n";

        int counter = 0;
        Set<Map.Entry<String, Map<Double, Integer>>> entrySet = ShopSettingsParser.getShopItems().entrySet();
        for (Map.Entry<String, Map<Double, Integer>> entry : entrySet)
        {
            String itemName = entry.getKey();
            double price = 0.0;
            int amount = 0;

            for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
            {
                price = entry2.getKey();
                amount = entry2.getValue();
            }

            ShS_CONTENT += "\t- `" + itemName + "` [$`" + price + "`] : `" + amount + "` Memory Diamonds\n";

            counter++;
            if (counter >= 10)
            {
                ShS_CONTENT += "\t`and " + (entrySet.size() - 10) + " more...`";
                break;
            }
        }

        builder.withDescription(S_DESCRIPTION);
        builder.appendField(ShS_TITLE, ShS_CONTENT, false);
    }
    // </editor-fold>

    // <editor-fold desc="Utilities">
    private void sendEmbed()
    {
        channel.sendMessage(builder.build());
    }

    private String botOwnerIDToString()
    {
        try
        {
            IUser botOwner = channel.getGuild().getUserByID(Long.parseLong(LoginSettingsParser.getBotOwnerDiscordID()));
            return botOwner.getName() + "#" + botOwner.getDiscriminator();
        }
        catch (NullPointerException e)
        {
            return "???";
        }
    }

    private String readChannelID(String channelID)
    {
        try
        {
            long channelLong = Long.parseLong(channelID);
            try
            {
                // CHANNEL (LONG) EXISTS IN SERVER
                channel.getGuild().getChannelByID(channelLong).getName();
                return "<#" + channelLong + ">";
            }
            catch (NullPointerException e1)
            {
                // CHANNEL (LONG) DOES NOT EXIST IN SERVER
                return "`" + channelLong + "`";
            }
        }
        catch (NumberFormatException e2)
        {
            // CHANNEL (NAME) PROVIDED
            return "`#" + channelID + "`";
        }
    }
    // </editor-fold>
}
