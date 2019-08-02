package io.github.spugn.Sargo.Functions;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Utilities.GitHubImage;

import java.awt.*;

/**
 * HELP
 * <p>
 *     Displays a help menu full of available commands.
 * </p>
 *
 * @author S'pugn
 * @version 2.0
 * @since v1.0
 */
public class Help
{
    public Help(Message message)
    {
        // HELP MENU TEXT
        final String detailedListLink =
            "[Detailed Command List](https://github.com/Expugn/S-argo#commands)" + "\n";

        final String generalHelp =
            "`help` - Display the command list." + "\n" +
            "`info` - Display bot information." + "\n";

        final String scoutingHelp_general =
            "`scout` - View a list of available banners." + "\n" +
            "`scout p[Page Number]` - View a page in the list of available banners." + "\n" +
            "`scout [Banner ID]` - View a banner's information." + "\n\n" +
            "**Adding an 'i' after your scout type will generate an image result.**";

        final String scoutingHelp_character =
            "`scout [Banner ID] [s/si]` - Perform a single scout." + "\n" +
            "`scout [Banner ID] [m/mi]` - Perform a multi scout." + "\n" +
            "`scout [Banner ID] [rc/rci]` - Perform a record crystal scout.";

        final String scoutingHelp_weapon =
            "`scout [Banner ID] [ws/wsi]` - Perform a weapon single scout." + "\n" +
            "`scout [Banner ID] [wm/wmi]` - Perform a weapon multi scout.";

        final String scoutingHelp_ticket =
            "`scout [nts/ntsi/ntm/ntmi]` - Perform a normal ticket scout." + "\n" +
            "`scout [pts/ptsi/ptm/ptmi]` - Perform a plus ticket scout." + "\n" +
            "`scout [nt2s/nt2si/nt2m/nt2mi]` - Perform a normal ticket v2 scout." + "\n" +
            "`scout [pt2s/pt2si/pt2m/pt2mi]` - Perform a plus ticket v2 scout." + "\n";

        final String shopHelp =
            "`shop` - View the shop." + "\n" +
            "`shop [Item ID] [Quantity]` - \"Buy\" a Memory Diamond bundle(s)." + "\n";

        final String profileHelp =
            "`profile` - View your basic profile." + "\n" +
            "`profile [info/i] [Banner ID]` - View your collected/missing characters in a banner." + "\n" +
            "`profile [data/d]` - View your Step/Record Crystal data." + "\n" +
            "`profile [search/s] [Character Name]` - Search for that character in your collection." + "\n";

        //final String userSearchHelp =
        //    "`user [@name]` - View a user's basic profile." + "\n";

        final String resetHelp =
            "`reset` - Reset your data file." + "\n" +
            "`reset [Banner ID] c` - Reset your character data for a banner." + "\n" +
            "`reset [Banner ID] w` - Reset your weapon data for a banner." + "\n" +
            "`reset [Banner ID] a` - Reset all data for a banner." + "\n\n" +
            "**Resetting all data includes your Step or Record Crystal data as well.**" + "\n";

        // CREATE AND PRINT HELP MENU
        message.getChannel().block().createMessage(
                s -> s.setEmbed(
                        embed -> embed
                            .setColor(new Color(91, 255, 105))
                            .setAuthor("Help Menu", null, new GitHubImage("images/System/Help_Icon.png").getURL())
                            .addField("FOR A MORE DETAILED COMMAND LIST...", detailedListLink, false)
                            .addField("GENERAL", generalHelp, false)
                            .addField("SCOUT - GENERAL", scoutingHelp_general, false)
                            .addField("SCOUT - CHARACTER", scoutingHelp_character, false)
                            .addField("SCOUT - WEAPON", scoutingHelp_weapon, false)
                            .addField("SCOUT - TICKET", scoutingHelp_ticket, false)
                            .addField("SHOP", shopHelp, false)
                            .addField("PROFILE", profileHelp, false)
                            //.addField("USER SEARCH", userSearchHelp, false)
                            .addField("DATA RESET", resetHelp, false)
                )).block();
    }
}
