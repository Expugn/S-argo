package io.github.spugn.Sargo.Functions;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.CommandSettingsParser;
import io.github.spugn.Sargo.XMLParsers.ShopSettingsParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * SHOP
 * <p>
 *     Gives the user Memory Diamonds based on the bundle ID and quantity given.
 *     The items that appear in the shop menu can be modified in the Settings.xml
 *     file.
 * </p>
 *
 * @author S'pugn
 * @version 3.0
 * @since v1.0
 */
public class Shop
{
    public Shop(Message message)
    {
        int counter = 1;
        int largestPackID = -1;
        int largestPackAmount = 0;

        Map<String, String> shopItems = new LinkedHashMap<>();
        for (Map.Entry<String, Map<Double, Integer>> entry : ShopSettingsParser.getShopItems().entrySet())
        {
            String itemName = entry.getKey();
            double price = 0.0;
            int amount = 0;

            for (Map.Entry<Double, Integer> entry2 : entry.getValue().entrySet())
            {
                price = entry2.getKey();
                amount = entry2.getValue();

                if (largestPackID == -1 || amount > largestPackAmount)
                {
                    largestPackID = counter;
                    largestPackAmount = amount;
                }
            }

            shopItems.put(counter + ") " + itemName + " [$" + price + "]", "Get " + amount + " Memory Diamonds");

            counter++;
            if (counter >= 51)
            {
                break;
            }
        }

        final Map<String, String> shopItems_final = shopItems;
        final String description = "***Memory Diamonds are FREE.\nYou do not need to pay actual money.***\n\n**Use `" + CommandSettingsParser.getCommandPrefix() + "shop " + largestPackID + " " + ShopSettingsParser.getMaxShopLimit() + "` to get `" + (largestPackAmount * ShopSettingsParser.getMaxShopLimit()) + "` Memory Diamonds.**";
        final String footer = "To get Memory Diamonds use '" + CommandSettingsParser.getCommandPrefix() + "shop [Item ID] [Quantity]'";

        // BUILD AND SEND EMBED MESSAGE
        Consumer<EmbedCreateSpec> ecsTemplate = s -> {
            s.setColor(new Color(233, 228, 232));
            s.setAuthor("Shop", "", "");
            s.setThumbnail(new GitHubImage("images/System/Shop_Icon.png").getURL());
            for (Map.Entry<String, String> entry : shopItems_final.entrySet())
            {
                s.addField(entry.getKey(), entry.getValue(), false);
            }
            s.setDescription(description);
            s.setFooter(footer, "");
        };

        message.getChannel().block().createMessage(
                ms -> ms.setEmbed(ecsTemplate.andThen(es -> {}))).block();
    }

    public Shop(Message message, String item, int quantity, boolean providedQuantity)
    {
        String discordID = "";
        if (message.getAuthor().isPresent())
        {
            discordID = message.getAuthor().get().getId().asString();
        }

        try
        {
            UserParser user = new UserParser(discordID);
            Member user_member = message.getGuild().block().getMemberById(Snowflake.of(discordID)).block();
            String userName = user_member.getUsername() + "#" + user_member.getDiscriminator();

            long userMemoryDiamonds = user.getMemoryDiamonds();
            double userMoneySpent = user.getMoneySpent();

            int counter = 0;
            boolean itemFound = false;

            for (Map.Entry<String, Map<Double, Integer>> entry : ShopSettingsParser.getShopItems().entrySet())
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
                Sargo.replyToMessage_Warning(message, "UNKNOWN ITEM", "Use '" + CommandSettingsParser.getCommandPrefix() + "**shop**' to review available Memory Diamond bundles.");
                return;
            }

            //EmbedBuilder builder = new EmbedBuilder();

            if (userMemoryDiamonds > Integer.MAX_VALUE)
            {
                Sargo.replyToMessage_Warning(message, "TOO MANY MEMORY DIAMONDS", "Yikes, looks like you can't hold that many memory diamonds.");
                return;
            }
            else if (userMoneySpent > Double.MAX_VALUE)
            {
                Sargo.replyToMessage_Warning(message, "TOO MUCH MONEY SPENT", "Yikes, looks like your wallet is bone-dry.");
                return;
            }
            else
            {
                long userMemoryDiamonds_final = userMemoryDiamonds;
                if (providedQuantity)
                {
                    message.getChannel().block().createMessage(
                            s -> s.setEmbed(
                                    embed -> embed
                                            .setColor(new Color(209, 204, 210))
                                            .setAuthor("You're all set.", "", "")
                                            .setDescription("Your purchase was successful.")
                                            .setFooter(userName + " | Balance: " + userMemoryDiamonds_final + " Memory Diamonds", "")
                            )).block();
                }
                else
                {
                    message.getChannel().block().createMessage(
                            s -> s.setEmbed(
                                    embed -> embed
                                            .setColor(new Color(209, 204, 210))
                                            .setAuthor("You're all set.", "", "")
                                            .setDescription("Your purchase was successful.\n\n" +
                                                    "**TIP**: You can use '" + CommandSettingsParser.getCommandPrefix() + "**shop** " + item + " " + ShopSettingsParser.getMaxShopLimit() + "' to get " + ShopSettingsParser.getMaxShopLimit() + " pack(s) of Memory Diamonds.")
                                            .setFooter(userName + " | Balance: " + userMemoryDiamonds_final + " Memory Diamonds", "")
                            )).block();
                }

                user.setMemoryDiamonds((int) userMemoryDiamonds);
                user.setMoneySpent(userMoneySpent);
                user.saveData();
            }

            if (providedQuantity)
            {
                Thread.sleep(3000);
            }
            else
            {
                Thread.sleep(5000);
            }
        }
        catch (InterruptedException e)
        {
            Sargo.replyToMessage_Warning(message, "Whoops.", "Something went wrong with your purchase.");
        }
        catch (NumberFormatException e2)
        {
            Sargo.replyToMessage_Warning(message, "UNKNOWN ITEM", "Use '" + CommandSettingsParser.getCommandPrefix() + "**shop**' to review available Memory Diamond bundles.");
        }
    }
}
