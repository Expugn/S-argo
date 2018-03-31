package io.github.spugn.Sargo.TicketScout;

import io.github.spugn.Sargo.Objects.Item;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import sx.blah.discord.handle.obj.IChannel;

public class Normalv2 extends TicketScout
{
    public Normalv2(IChannel channel, String choice, String discordID)
    {
        super(channel, choice, discordID);
        run();
    }

    @Override
    protected void initItems()
    {
        attributeItemsList.add(new Item("HP Shard", 50, 2500));
        attributeItemsList.add(new Item("MP Shard", 50, 2500));
        attributeItemsList.add(new Item("Attack Shard", 50, 2500));
        attributeItemsList.add(new Item("Defense Shard", 50, 2500));
        attributeItemsList.add(new Item("Critical Shard", 50, 2500));
        attributeItemsList.add(new Item("Skill Shard", 50, 2500));
        attributeItemsList.add(new Item("HP Crystal", 50, 12500));
        attributeItemsList.add(new Item("MP Crystal", 50, 12500));
        attributeItemsList.add(new Item("Attack Crystal", 50, 12500));
        attributeItemsList.add(new Item("Defense Crystal", 50, 12500));
        attributeItemsList.add(new Item("Critical Crystal", 50, 12500));
        attributeItemsList.add(new Item("Skill Crystal", 50, 12500));
        attributeItemsList.add(new Item("Holy HP Crystal", 10, 10000));
        attributeItemsList.add(new Item("Holy MP Crystal", 10, 10000));
        attributeItemsList.add(new Item("Holy Attack Crystal", 10, 10000));
        attributeItemsList.add(new Item("Holy Defense Crystal", 10, 10000));
        attributeItemsList.add(new Item("Holy Critical Crystal", 10, 10000));
        attributeItemsList.add(new Item("Holy Skill Crystal", 10, 10000));
        attributeItemsList.add(new Item("EXP X-Potion", 3, 3000));
        attributeItemsList.add(new Item("EXP Hi-Potion", 5, 1250));
        attributeItemsList.add(new Item("EXP Potion", 10, 500));

        medallionsAndKeyList.add(new Item("Lv. 80 Decryption Key", 1, 0));
        medallionsAndKeyList.add(new Item("Void Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Fire Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Water Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Wind Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Holy Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Dark Medallion", 1, 2500));
        medallionsAndKeyList.add(new Item("Earth Medallion", 1, 2500));
    }

    @Override
    protected int scout()
    {
        double d;
        d = RNG.nextDouble() * 100;

        double exchangeSword = 5;
        double attributeItems = 59.367;
        double medallionsAndKey = 16;
        double stardust = 12;
        double rainbowEssence = 1;
        double upgradeCrystal = 2.8;
        double memoryFragment = 1;

        /* EXCHANGE SWORD R2 */
        if (d < exchangeSword)
        {
            return 1;
        }
        /* SHARD / CRYSTAL / HOLY CRYSTAL / EXP POTION */
        else if (d < exchangeSword + attributeItems)
        {
            return 2;
        }
        /* MEDALLIONS AND LV. 80 KEY */
        else if (d < exchangeSword + attributeItems + medallionsAndKey)
        {
            return 3;
        }
        /* STARDUST */
        else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust)
        {
            return 4;
        }
        /* RAINBOW ESSENCE */
        else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust + rainbowEssence)
        {
            return 5;
        }
        /* UPGRADE CRYSTAL */
        else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust + rainbowEssence + upgradeCrystal)
        {
            return 6;
        }
        /* MEMORY FRAGMENT */
        else if (d < exchangeSword + attributeItems + medallionsAndKey + stardust + rainbowEssence + upgradeCrystal + memoryFragment)
        {
            return 7;
        }
        /* COL */
        else
        {
            return 8;
        }
    }

    @Override
    protected Item getItem(int value)
    {
        Item item;
        /* EXCHANGE SWORD R2 */
        if (value == 1)
        {
            item = new Item("Exchange Sword R2", 1, -1);
        }
        /* SHARD / CRYSTAL / HOLY CRYSTAL / EXP POTION */
        else if (value == 2)
        {
            item = attributeItemsList.get(RNG.nextInt(attributeItemsList.size()));
        }
        /* MEDALLIONS AND LV. 80 KEY */
        else if (value == 3)
        {
            item = medallionsAndKeyList.get(RNG.nextInt(medallionsAndKeyList.size()));
        }
        /* STARDUST */
        else if (value == 4)
        {
            item = new Item("Stardust (Medium)", 10, 500);
        }
        /* RAINBOW ESSENCE */
        else if (value == 5)
        {
            item = new Item("Rainbow Essence", 1, -1);
        }
        /* UPGRADE CRYSTAL */
        else if (value == 6)
        {
            item = new Item("Upgrade Crystal", 1, -1);
        }
        /* MEMORY FRAGMENT */
        else if (value == 7)
        {
            item = new Item("Memory Fragment", 1, -1);
        }
        /* COL */
        else
        {
            item = new Item("Col", 50000, 50000);
        }

        giveCol(item);
        return item;
    }

    @Override
    protected void setupScoutMenu()
    {
        if (!SIMPLE_MESSAGE)
        {
            scoutMenu.withAuthorName("Normal Ticket v2 Scout");
            scoutMenu.withThumbnail(new GitHubImage("images/System/Normal_Ticket_2.png").getURL());
            switch (CHOICE.toLowerCase())
            {
                case "nts":
                case "ntsi":
                    scoutMenu.withTitle("[Ticket Scout] - Single Pull");
                    break;
                case "ntm":
                case "ntmi":
                    scoutMenu.withTitle("[Ticket Scout] - Multi Pull");
                    break;
                default:
                    scoutMenu.withTitle("[Ticket Scout] - Unknown");
                    break;
            }
        }
        else
        {
            simpleMessage += "**Normal Ticket v2 Scout**" + "\n";
            switch (CHOICE.toLowerCase())
            {
                case "nts":
                case "ntsi":
                    simpleMessage += "**[Ticket Scout] - Single Pull**" + "\n";
                    break;
                case "ntm":
                case "ntmi":
                    simpleMessage += "**[Ticket Scout] - Multi Pull**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Ticket Scout] - Unknown**" + "\n";
                    break;
            }
        }
    }

    @Override
    protected void run()
    {
        switch (CHOICE.toLowerCase())
        {
            case "nt2s":
            case "nt2si":
                if (CHOICE.equalsIgnoreCase("nt2si") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                doSinglePull();
                break;
            case "nt2m":
            case "nt2mi":
                if (CHOICE.equalsIgnoreCase("nt2mi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                doMultiPull();
                break;
            default:
                System.out.println("default");
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
