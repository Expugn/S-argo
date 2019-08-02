package io.github.spugn.Sargo.Functions;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Weapon;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.UserParser;

import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

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
    public Reset(Message message)
    {
        Sargo.replyToMessage_Warning(message, "WARNING", "Continuing forward will erase all your data. Are you sure?\nType '" + CommandManager.getCommandPrefix() + "**reset** y' to proceed.");
    }

    public Reset(Message message, String discordID)
    {
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (userFile.exists())
        {
            new UserParser(discordID).resetUser();

            Member member = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(discordID))).block();
            Sargo.replyToMessage_Warning(message, "USER FILE RESET", "**" + member.getUsername() + "#" + member.getDiscriminator() + "**'s file has been reset.");
        }
        else
        {
            Sargo.replyToMessage_Warning(message, "USER FILE NOT FOUND", "Huh. Well that was anti-climatic.");
        }
    }

    /**
     * Removes either the characters, weapons, or all data of a specific banner.
     *
     * @param discordID  Discord ID of the user.
     * @param bannerID  ID of the banner that should be deleted.
     * @param choice  Choice of the type of deletion that should be processed.
     * @param yes  If true, then delete data; if false, then warn the user and display data that will be deleted.
     */
    public Reset(Message message, String discordID, int bannerID, String choice, boolean yes)
    {
        bannerID--;
        File userFile = new File("data/Users/USER_" + discordID + ".xml");

        if (!userFile.exists())
        {
            Sargo.replyToMessage_Warning(message, "USER FILE NOT FOUND", "Huh. Well that was anti-climatic.");
            return;
        }

        /* OPEN BANNERS FILE */
        List<Banner> banners = BannerParser.getBanners();

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
                    // BANNER TYPE IS STEP UP
                    if (bannerType == 1 ||
                            bannerType == 3 ||
                            bannerType == 4 ||
                            bannerType == 7 ||
                            bannerType == 10 ||
                            bannerType == 12 ||
                            bannerType == 13 ||
                            bannerType == 14 ||
                            bannerType == 15 ||
                            bannerType == 16 ||
                            bannerType == 17 ||
                            bannerType == 19 ||
                            bannerType == 21)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 1);
                    }
                    // BANNER TYPE IS RECORD CRYSTAL
                    else if (bannerType == 2)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), 0);
                    }
                    // BANNER TYPE IS RECORD CRYSTAL V2+
                    else if (bannerType == 5 ||
                                bannerType == 8 ||
                                bannerType == 11 ||
                                bannerType == 18 ||
                                bannerType == 20)
                    {
                        user.changeValue(banners.get(bannerID).getBannerName(), -1);
                    }

                    int wepBannerType = banners.get(bannerID).getBannerWepType();
                    // WEP BANNER STEP UP
                    if (wepBannerType == 1 ||
                            wepBannerType == 2 ||
                            wepBannerType == 3 ||
                            wepBannerType == 4)
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

                Member member = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(discordID))).block();

                Sargo.replyToMessage_Warning(message, "USER BANNER DATA RESET", "**" + member.getUsername() + "#" + member.getDiscriminator() + "**'s " + dataType + " for **" + banners.get(bannerID).getBannerName() + "** has been reset.");
            }
            else
            {
                boolean hasData = false;
                int bannerID_final = bannerID;
                String charString_final = charString;
                String weapString_final = weapString;
                int bannerData_final = bannerData;
                int bannerWepData_final = bannerWepData;


                if ((!charString_final.isEmpty() && (choice.equalsIgnoreCase("c") || choice.equalsIgnoreCase("a"))) ||
                        !weapString_final.isEmpty() && (choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("a")) ||
                        choice.equalsIgnoreCase("a") && !(banners.get(bannerID_final).getBannerType() == 0 || banners.get(bannerID_final).getBannerType() == 6))
                {
                    hasData = true;
                }
                boolean hasData_final = hasData;
                Consumer<EmbedCreateSpec> ecsTemplate;
                ecsTemplate = s -> {
                    s.setAuthor("WARNING", "", "");
                    s.setTitle("Continuing forward will erase the following data from your file:");
                    s.setColor(new Color(255, 0, 0));
                    s.setFooter("Type '" + CommandManager.getCommandPrefix() + "reset " + (bannerID_final + 1) + " " + choice + " y' to proceed.", "");
                    s.setThumbnail(new GitHubImage("images/System/Profile_Icon.png").getURL());

                    s.addField("- Banner -", banners.get(bannerID_final).getBannerName(), false);

                    if (!charString_final.isEmpty() && (choice.equalsIgnoreCase("c") || choice.equalsIgnoreCase("a")))
                    {
                        s.addField("- Characters -", charString_final, false);
                    }

                    if (!weapString_final.isEmpty() && (choice.equalsIgnoreCase("w") || choice.equalsIgnoreCase("a")))
                    {
                        s.addField("- Weapons -", weapString_final, false);
                    }

                    if (choice.equalsIgnoreCase("a") && !(banners.get(bannerID_final).getBannerType() == 0 || banners.get(bannerID_final).getBannerType() == 6))
                    {
                        String dataString = "";
                        int bannerType = banners.get(bannerID_final).getBannerType();
                        int bannerWepType = banners.get(bannerID_final).getBannerWepType();

                        // BANNER TYPE IS STEP UP
                        if (bannerType == 1 ||
                                bannerType == 3 ||
                                bannerType == 4 ||
                                bannerType == 7 ||
                                bannerType == 10 ||
                                bannerType == 12 ||
                                bannerType == 13 ||
                                bannerType == 14 ||
                                bannerType == 15 ||
                                bannerType == 16 ||
                                bannerType == 17 ||
                                bannerType == 19 ||
                                bannerType == 21)
                        {
                            dataString = "Step " + bannerData_final + " -> Step 1";
                        }
                        // BANNER TYPE IS RECORD CRYSTAL
                        else if (bannerType == 2 ||
                                bannerType == 5 ||
                                bannerType == 8 ||
                                bannerType == 11 ||
                                bannerType == 18 ||
                                bannerType == 20)
                        {
                            if (bannerData_final < 0)
                            {
                                dataString = "Record Crystals: 0 -> 0";
                            }
                            else
                            {
                                dataString = "Record Crystals: " + bannerData_final + " -> 0";
                            }
                        }

                        // WEAPON BANNER STEP UP
                        if (bannerWepType == 1 ||
                                bannerWepType == 2 ||
                                bannerWepType == 3 ||
                                bannerWepType == 4)
                        {
                            dataString += "\nWeapon Step " + bannerWepData_final + " -> Weapon Step 1";
                        }
                        s.addField("- Data -", dataString, false);
                    }

                    if (!hasData_final)
                    {
                        s.addField("NO DATA FOUND", "No changes will be made if you reset.", false);
                    }

                };

                Sargo.sendEmbed((TextChannel) message.getChannel().block(), ecsTemplate);
            }
        }
        else
        {
            Sargo.replyToMessage_Warning(message, "UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.");
        }
    }
}
