package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Images;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

public class Shop
{
    public Shop(IChannel channel)
    {
        EmbedBuilder builder = new EmbedBuilder();

        builder.withAuthorName("Shop");
        builder.withColor(233, 228, 232);
        builder.withThumbnail(Images.SHOP_ICON.getUrl());

        builder.appendField("1) Memory Diamond A [$0.99]", "Get 5 Memory Diamonds", false);
        builder.appendField("2) Memory Diamond B [$4.99]", "Get 25 Memory Diamonds", false);
        builder.appendField("3) Memory Diamond C [$7.99]", "Get 50 Memory Diamonds", false);
        builder.appendField("4) Memory Diamond D [$17.99]", "Get 125 Memory Diamonds", false);
        builder.appendField("5) Memory Diamond E [$33.99]", "Get 250 Memory Diamonds", false);
        builder.appendField("6) Memory Diamond F [$44.99]", "Get 360 Memory Diamonds", false);
        builder.appendField("7) Memory Diamond G [$79.99]", "Get 700 Memory Diamonds", false);

        builder.withFooterText("To buy Memory Diamonds use '" + CommandManager.commandPrefix + "shop [Item ID] [Quantity]'");

        channel.sendMessage(builder.build());
    }

    public Shop(IChannel channel, String discordID, String item, int quantity)
    {
        try
        {
            UserParser user = new UserParser(discordID);
            IUser iUser = channel.getGuild().getUserByID(Long.parseLong(discordID));

            String userName = iUser.getName() + "#" + iUser.getDiscriminator();
            long userMemoryDiamonds = user.getMemoryDiamonds();
            double userMoneySpent = user.getMoneySpent();

            if (item.equals("1") || item.equalsIgnoreCase("A"))
            {
                userMemoryDiamonds += 5 * quantity;
                userMoneySpent += 0.99 * quantity;
            }
            else if (item.equals("2") || item.equalsIgnoreCase("B"))
            {
                userMemoryDiamonds += 25 * quantity;
                userMoneySpent += 4.99 * quantity;
            }
            else if (item.equals("3") || item.equalsIgnoreCase("C"))
            {
                userMemoryDiamonds += 50 * quantity;
                userMoneySpent += 7.99 * quantity;
            }
            else if (item.equals("4") || item.equalsIgnoreCase("D"))
            {
                userMemoryDiamonds += 125 * quantity;
                userMoneySpent += 17.99 * quantity;
            }
            else if (item.equals("5") || item.equalsIgnoreCase("E"))
            {
                userMemoryDiamonds += 250 * quantity;
                userMoneySpent += 33.99 * quantity;
            }
            else if (item.equals("6") || item.equalsIgnoreCase("F"))
            {
                userMemoryDiamonds += 360 * quantity;
                userMoneySpent += 44.99 * quantity;
            }
            else if (item.equals("7") || item.equalsIgnoreCase("G"))
            {
                userMemoryDiamonds += 700 * quantity;
                userMoneySpent += 79.99 * quantity;
            }
            else
            {
                channel.sendMessage(new WarningMessage("UNKNOWN ITEM", "Use '" + CommandManager.commandPrefix + "**shop**' to review available Memory Diamond bundles.").get().build());
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
                builder.withDescription("Your purchase was successful.");
                builder.withFooterText(userName + " | Balance: " + userMemoryDiamonds + " Memory Diamonds");

                user.setMemoryDiamonds((int) userMemoryDiamonds);
                user.setMoneySpent(userMoneySpent);
                user.saveData();
            }
            IMessage message = channel.sendMessage(builder.build());

            Thread.sleep(3000);
            message.delete();
        }
        catch (InterruptedException e)
        {
            channel.sendMessage(new WarningMessage("Whoops.", "Something went wrong with your purchase.").get().build());
        }
    }
}
