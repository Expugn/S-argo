package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.Images;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
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

        builder.withFooterText("To buy Memory Diamonds use 'shop [Item ID]'");

        channel.sendMessage(builder.build());
    }

    public Shop(IChannel channel, String discordID, String item)
    {
        UserParser user = new UserParser(discordID);
        IUser iUser = channel.getGuild().getUserByID(Long.parseLong(discordID));

        String userName = iUser.getName() + "#" + iUser.getDiscriminator();
        int userMemoryDiamonds = user.getMemoryDiamonds();
        double userMoneySpent = user.getMoneySpent();

        if (item.equals("1") || item.equalsIgnoreCase("A"))
        {
            userMemoryDiamonds += 5;
            userMoneySpent += 0.99;
        }
        else if (item.equals("2") || item.equalsIgnoreCase("B"))
        {
            userMemoryDiamonds += 25;
            userMoneySpent += 4.99;
        }
        else if (item.equals("3") || item.equalsIgnoreCase("C"))
        {
            userMemoryDiamonds += 50;
            userMoneySpent += 7.99;
        }
        else if (item.equals("4") || item.equalsIgnoreCase("D"))
        {
            userMemoryDiamonds += 125;
            userMoneySpent += 17.99;
        }
        else if (item.equals("5") || item.equalsIgnoreCase("E"))
        {
            userMemoryDiamonds += 250;
            userMoneySpent += 33.99;
        }
        else if (item.equals("6") || item.equalsIgnoreCase("F"))
        {
            userMemoryDiamonds += 360;
            userMoneySpent += 44.99;
        }
        else if (item.equals("7") || item.equalsIgnoreCase("G"))
        {
            userMemoryDiamonds += 700;
            userMoneySpent += 79.99;
        }
        else
        {
            channel.sendMessage(new WarningMessage("UNKNOWN ITEM", "Use 'shop' to review available Memory Diamond bundles.").get().build());
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();

        builder.withColor(209, 204, 210);
        builder.withAuthorName("You're all set.");
        builder.withDescription("Your purchase was successful.");
        builder.withFooterText(userName + " | Balance: " + userMemoryDiamonds + " Memory Diamonds");

        user.setMemoryDiamonds(userMemoryDiamonds);
        user.setMoneySpent(userMoneySpent);
        user.saveData();

        channel.sendMessage(builder.build());
    }
}
