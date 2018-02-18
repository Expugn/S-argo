package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.Objects.Weapon;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * RESET
 * <p>
 *     This class modifies a user's file to remove data and provide a clean
 *     slate.
 * </p>
 *
 * @author S'pugn
 * @version 1.1
 * @since v1.0
 */
public class Reset
{
    private IChannel CHANNEL;

    /**
     * Warns the user that if they type the command in the warning message their entire
     * user file will be deleted.
     *
     * @param channel  Channel where the message should be sent.
     */
    public Reset(IChannel channel)
    {
        CHANNEL = channel;
        CHANNEL.sendMessage(new WarningMessage("WARNING", "Continuing forward will erase all your data. Are you sure?\nType '" + CommandManager.getCommandPrefix() + "**reset** y' to proceed.").get().build());
    }

    /**
     * Removes everything in the user's file.
     *
     * @param channel  Channel where the message should be sent.
     * @param discordID  Discord ID of the user.
     */
    public Reset(IChannel channel, String discordID)
    {
        CHANNEL = channel;
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (userFile.exists())
        {
            new UserParser(discordID).resetUser();

            IUser discordUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
            CHANNEL.sendMessage(new WarningMessage("USER FILE RESET", "**" + discordUser.getName() + "#" + discordUser.getDiscriminator() + "**'s file has been reset.").get().build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("USER FILE NOT FOUND", "Huh. Well that was anti-climatic.").get().build());
        }
    }

    /**
     * Removes either the characters, weapons, or all data of a specific banner.
     *
     * @param channel  Channel where the data should be displayed.
     * @param discordID  Discord ID of the user.
     * @param bannerID  ID of the banner that should be deleted.
     * @param choice  Choice of the type of deletion that should be processed.
     * @param yes  If true, then delete data; if false, then warn the user and display data that will be deleted.
     */
    public Reset(IChannel channel, String discordID, int bannerID, String choice, boolean yes)
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
        List<Banner> banners = bannersXML.getBanners();

        if (bannerID < banners.size() && bannerID >= 0)
        {
            List<Character> bannerCharacters = banners.get(bannerID).getCharacters();
            List<Weapon> bannerWeapons = banners.get(bannerID).getWeapons();

            UserParser user = new UserParser(discordID);

            String charString = "";
            String weapString = "";
            int bannerData = 0;
            int bannerWepData = 0;

            for (Character c : bannerCharacters)
            {
                for (Character uC : user.getCharacterBox())
                {
                    if (c.getRarity() == uC.getRarity() &&
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
                    if (w.getRarity() == uW.getRarity() &&
                            w.getName().equalsIgnoreCase(uW.getName()))
                    {
                        weapString += w.toString() + "\n";
                    }
                }
            }

            if (banners.get(bannerID).getBannerType() != 0)
            {
                bannerData = user.getBannerData(banners.get(bannerID).getBannerName());
            }

            if (banners.get(bannerID).getBannerWepType() != 0)
            {
                bannerWepData = user.getBannerData(banners.get(bannerID).getBannerName() + " Weapons");
            }

            if (yes)
            {
                if ((choice.equalsIgnoreCase("c") || choice.equalsIgnoreCase("a")))
                {
                    List<Character> userCharacters = user.getCharacterBox();

                    for (Iterator<Character> iter = userCharacters.listIterator(); iter.hasNext() ; )
                    {
                        Character userCharacter = iter.next();
                        for (Character c : bannerCharacters)
                        {
                            if (c.getRarity() == userCharacter.getRarity() &&
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

                    for (Iterator<Weapon> iter = userWeapons.listIterator(); iter.hasNext() ; )
                    {
                        Weapon userWeapon = iter.next();
                        for (Weapon w : bannerWeapons)
                        {
                            if (w.getRarity() == userWeapon.getRarity() &&
                                    w.getName().equalsIgnoreCase(userWeapon.getName()))
                            {
                                iter.remove();
                            }
                        }
                    }

                    user.setWeaponBox(userWeapons);
                }

                if (choice.equalsIgnoreCase("a") && !(banners.get(bannerID).getBannerType() == 0))
                {
                    int bannerType = banners.get(bannerID).getBannerType();
                    if (bannerType == 1 || bannerType == 3 || bannerType == 4 || bannerType == 7)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 1);
                    }
                    else if (bannerType == 2)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 0);
                    }
                    else if (bannerType == 5 || bannerType == 8)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), -1);
                    }

                    int wepBannerType = banners.get(bannerID).getBannerWepType();
                    if (wepBannerType == 1 ||
                            wepBannerType == 2)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName() + " Weapons", 1);
                    }
                }

                user.saveData();

                String dataType;
                if (choice.equalsIgnoreCase("c"))
                {
                    dataType = "character data";
                }
                else if (choice.equalsIgnoreCase("w"))
                {
                    dataType = "weapon data";
                }
                else
                {
                    dataType = "data";
                }

                IUser discordUser = channel.getGuild().getUserByID(Long.parseLong(discordID));
                CHANNEL.sendMessage(new WarningMessage("USER BANNER DATA RESET", "**" + discordUser.getName() + "#" + discordUser.getDiscriminator() + "**'s " + dataType + " for **" + banners.get(bannerID).getBannerName() + "** has been reset.").get().build());
            }
            else
            {
                boolean hasData = false;
                EmbedBuilder builder = new EmbedBuilder();
                builder.withAuthorName("WARNING");
                builder.withTitle("Continuing forward will erase the following data from your file:");
                builder.withFooterText("Type '" + CommandManager.getCommandPrefix() + "reset " + (bannerID + 1) + " " + choice + " y' to proceed.");
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

                if (choice.equalsIgnoreCase("a") && !(banners.get(bannerID).getBannerType() == 0 || banners.get(bannerID).getBannerType() == 6))
                {
                    String dataString = "";
                    int bannerType = banners.get(bannerID).getBannerType();
                    int bannerWepType = banners.get(bannerID).getBannerWepType();

                    if (bannerType == 1 || bannerType == 3 || bannerType == 4 || bannerType == 7)
                    {
                        dataString = "Step " + bannerData + " -> Step 1";
                    }
                    else if (bannerType == 2 ||
                            bannerType == 5 ||
                            bannerType == 8)
                    {
                        if (bannerData < 0)
                        {
                            dataString = "Record Crystals: 0 -> 0";
                        }
                        else
                        {
                            dataString = "Record Crystals: " + bannerData + " -> 0";
                        }
                    }

                    if (bannerWepType == 1 ||
                            bannerWepType == 2)
                    {
                        dataString += "\nWeapon Step " + bannerWepData + " -> Weapon Step 1";
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
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.").get().build());
        }
    }
}
