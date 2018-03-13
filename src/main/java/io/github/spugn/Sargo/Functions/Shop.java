package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Map;
import java.util.SortedMap;

/**
 * SHOP
 * <p>
 *     Gives the user Memory Diamonds based on the bundle ID and quantity given.
 *     The items that appear in the shop menu can be modified in the Settings.xml
 *     file.
 * </p>
 *
 * @author S'pugn
 * @version 2.1
 * @since v1.0
 */
public class Shop
{
    /**
     * Displays the list of options available.
     *
     * @param channel  Channel where the message should be sent.
     */
    public Shop(IChannel channel)
    {
        SettingsParser settings = new SettingsParser();
        EmbedBuilder builder = new EmbedBuilder();
        int counter = 1;

        builder.withAuthorName("Shop");
        builder.withDesc("***No actual currency is required to get Memory Diamonds.***");
        builder.withColor(233, 228, 232);
        builder.withThumbnail(new GitHubImage("images/System/Shop_Icon.png").getURL());

        for (Map.Entry<String, SortedMap<Double, Integer>> entry : settings.getShopItems().entrySet())
        {
            String itemName = entry.getKey();
            double price = 0.0;
            int amount = 0;

            for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
            {
                price = entry2.getKey();
                amount = entry2.getValue();
            }

            builder.appendField(counter + ") " + itemName + " [$" + price + "]", "Get " + amount + " Memory Diamonds", false);

            counter++;
            if (counter >= 51)
            {
                break;
            }
        }

        builder.withFooterText("To get Memory Diamonds use '" + CommandManager.getCommandPrefix() + "shop [Item ID] [Quantity]'");

        channel.sendMessage(builder.build());
    }

    /**
     * Performs the shop "transaction".
     *
     * @param channel  Channel where the message should be sent.
     * @param discordID  Discord ID of the user.
     * @param item  Item that the user is requesting to purchase.
     * @param quantity  Amount of bundles the user wants to purchase.
     */
    public Shop(IChannel channel, String discordID, String item, int quantity, boolean providedQuantity)
    {
        try
        {
            //SettingsParser settings = new SettingsParser();
            UserParser user = new UserParser(discordID);
            IUser iUser = channel.getGuild().getUserByID(Long.parseLong(discordID));

            String userName = iUser.getName() + "#" + iUser.getDiscriminator();
            long userMemoryDiamonds = user.getMemoryDiamonds();
            double userMoneySpent = user.getMoneySpent();

            int counter = 0;
            boolean itemFound = false;

            //for (Map.Entry<String, SortedMap<Double, Integer>> entry : settings.getShopItems().entrySet())
            for (Map.Entry<String, SortedMap<Double, Integer>> entry : SettingsParser.getShopItems().entrySet())
            {
                counter++;
                if (counter == Integer.parseInt(item))
                {
                    double price = 0.0;
                    int amount = 0;

                    for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
                    {
                        price = entry2.getKey();
                        amount = entry2.getValue();
                    }

                    userMemoryDiamonds += amount * quantity;
                    userMoneySpent += price * quantity;
                    itemFound = true;
                    break;
                }

                if (counter >= 51)
                {
                    break;
                }
            }

            if (!itemFound)
            {
                channel.sendMessage(new WarningMessage("UNKNOWN ITEM", "Use '" + CommandManager.getCommandPrefix() + "**shop**' to review available Memory Diamond bundles.").get().build());
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();

            if (userMemoryDiamonds > Integer.MAX_VALUE)
            {
                builder.withColor(255, 0, 0);
                builder.withAuthorName("TOO MANY MEMORY DIAMONDS");
                builder.withDescription("Yikes, looks like you can't hold that many memory diamonds.");
            }
            else if (userMoneySpent > Double.MAX_VALUE)
            {
                builder.withColor(255, 0, 0);
                builder.withAuthorName("TOO MUCH MONEY SPENT");
                builder.withDescription("Yikes, looks like your wallet is bone-dry.");
            }
            else
            {
                builder.withColor(209, 204, 210);
                builder.withAuthorName("You're all set.");
                if (providedQuantity)
                {
                    builder.withDescription("Your purchase was successful.");
                }
                else
                {
                    builder.withDescription("Your purchase was successful.\n\n" +
                            "**TIP**: You can use '" + CommandManager.getCommandPrefix() + "**shop** " + item + " " + SettingsParser.getMaxShopLimit() + "' to get more than one pack of Memory Diamonds.");
                }
                builder.withFooterText(userName + " | Balance: " + userMemoryDiamonds + " Memory Diamonds");

                user.setMemoryDiamonds((int) userMemoryDiamonds);
                user.setMoneySpent(userMoneySpent);
                user.saveData();
            }
            IMessage message = channel.sendMessage(builder.build());

            if (providedQuantity)
            {
                Thread.sleep(3000);
            }
            else
            {
                Thread.sleep(5000);
            }
            message.delete();
        }
        catch (InterruptedException e)
        {
            channel.sendMessage(new WarningMessage("Whoops.", "Something went wrong with your purchase.").get().build());
        }
    }
}
