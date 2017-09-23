package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class Reset
{
    private IChannel CHANNEL;
    public Reset(IChannel channel)
    {
        CHANNEL = channel;
        CHANNEL.sendMessage(new WarningMessage("WARNING", "Continuing forward will erase all your data. Are you sure?\nType 'reset y' to proceed.").get().build());
    }

    public Reset (IChannel channel, String discordID)
    {
        CHANNEL = channel;
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (userFile.exists())
        {
            //userFile.delete();
            new UserParser(discordID).resetUser();

            IUser discordUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
            CHANNEL.sendMessage(new WarningMessage("USER FILE RESET", "**" + discordUser.getName() + "#" + discordUser.getDiscriminator() + "**'s file has been reset.").get().build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("USER FILE NOT FOUND", "Huh. Well that was anti-climatic.").get().build());
        }
    }

    public Reset (IChannel channel, String discordID, int bannerID, String choice, boolean yes)
    {
        CHANNEL = channel;
        bannerID--;
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (!userFile.exists())
        {
            CHANNEL.sendMessage(new WarningMessage("USER FILE NOT FOUND", "Huh. Well that was anti-climatic.").get().build());
            return;
        }

        /* OPEN BANNERS FILE */
        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig(Files.BANNER_XML.get());

        if (bannerID < banners.size() && bannerID >= 0)
        {
            List<Character> bannerCharacters = banners.get(bannerID).getCharacters();
            List<Weapon> bannerWeapons = banners.get(bannerID).getWeapons();

            UserParser user = new UserParser(discordID);

            String charString = "";
            String weapString = "";
            int bannerData = 0;

            for (Character c : bannerCharacters)
            {
                for (Character uC : user.getCharacterBox())
                {
                    if (c.getRarity().equalsIgnoreCase(uC.getRarity()) &&
                            c.getPrefix().equalsIgnoreCase(uC.getPrefix()) &&
                            c.getName().equalsIgnoreCase(uC.getName()))
                    {
                        charString += c.toString() + "\n";
                    }
                }
            }

            for (Weapon w : bannerWeapons)
            {
                for (Weapon uW : user.getWeaponBox())
                {
                    if (w.getRarity().equalsIgnoreCase(uW.getRarity()) &&
                            w.getName().equalsIgnoreCase(uW.getName()))
                    {
                        weapString += w.toString() + "\n";
                    }
                }
            }

            if (Integer.parseInt(banners.get(bannerID).getBannerType()) != 0)
            {
                bannerData = user.getBannerData(banners.get(bannerID).getBannerName());
            }

            if (yes)
            {
                if ((choice.equalsIgnoreCase("c") || choice.equalsIgnoreCase("a")))
                {
                    List<Character> userCharacters = user.getCharacterBox();

                    for (Iterator<Character> iter = userCharacters.listIterator() ; iter.hasNext() ; )
                    {
                        Character userCharacter = iter.next();
                        for (Character c : bannerCharacters)
                        {
                            if (c.getRarity().equalsIgnoreCase(userCharacter.getRarity()) &&
                                    c.getPrefix().equalsIgnoreCase(userCharacter.getPrefix()) &&
                                    c.getName().equalsIgnoreCase(userCharacter.getName()))
                            {
                                iter.remove();
                            }
                        }
                    }

                    user.setCharacterBox(userCharacters);
                }

                if ((choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("a")))
                {
                    List<Weapon> userWeapons = user.getWeaponBox();

                    for (Iterator<Weapon> iter = userWeapons.listIterator() ; iter.hasNext() ; )
                    {
                        Weapon userWeapon = iter.next();
                        for (Weapon w : bannerWeapons)
                        {
                            if (w.getRarity().equalsIgnoreCase(userWeapon.getRarity()) &&
                                    w.getName().equalsIgnoreCase(userWeapon.getName()))
                            {
                                iter.remove();
                            }
                        }
                    }

                    user.setWeaponBox(userWeapons);
                }

                if (choice.equalsIgnoreCase("a") && !(banners.get(bannerID).getBannerType().equalsIgnoreCase("0")))
                {
                    int bannerType = Integer.parseInt(banners.get(bannerID).getBannerType());
                    if (bannerType == 1 || bannerType == 3)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 1);
                    }
                    else if (bannerType == 2)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 0);
                    }
                }

                user.saveData();

                IUser discordUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
                CHANNEL.sendMessage(new WarningMessage("USER BANNER DATA RESET", "**" + discordUser.getName() + "#" + discordUser.getDiscriminator() + "**'s data for **" + banners.get(bannerID).getBannerName() + "** has been reset.").get().build());
            }
            else
            {
                boolean hasData = false;
                EmbedBuilder builder = new EmbedBuilder();
                builder.withAuthorName("WARNING");
                builder.withTitle("Continuing forward will erase the following data from your file:");
                builder.withFooterText("Type 'reset " + (bannerID + 1) + " " + choice + " y' to proceed.");
                builder.withColor(255, 0, 0);

                builder.appendField("- Banner -", banners.get(bannerID).getBannerName(), false);

                if (!charString.isEmpty() && (choice.equalsIgnoreCase("c") || choice.equalsIgnoreCase("a")))
                {
                    builder.appendField("- Characters -", charString, false);
                    hasData = true;
                }

                if (!weapString.isEmpty() && (choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("a")))
                {
                    builder.appendField("- Weapons -", weapString, false);
                    hasData = true;
                }

                if (choice.equalsIgnoreCase("a") && !(banners.get(bannerID).getBannerType().equalsIgnoreCase("0")))
                {
                    String dataString = "";
                    int bannerType = Integer.parseInt(banners.get(bannerID).getBannerType());

                    if (bannerType == 1 || bannerType == 3)
                    {
                        dataString = "Step " + bannerData + " -> Step 1";
                    }
                    else if (bannerType == 2)
                    {
                        dataString = "Record Crystals: " + bannerData + " -> 0";
                    }
                    builder.appendField("- Data -", dataString, false);
                    hasData = true;
                }

                if (!hasData)
                {
                    builder.appendField("NO DATA FOUND", "No changes will be made if you reset.", false);
                }
                CHANNEL.sendMessage(builder.build());
            }
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
        }
    }
}
