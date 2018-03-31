package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.TicketScout.Normal;
import io.github.spugn.Sargo.TicketScout.Normalv2;
import io.github.spugn.Sargo.TicketScout.Plus;
import io.github.spugn.Sargo.TicketScout.Plusv2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

/**
 * TICKET SCOUT MANAGER
 * <p>
 *     This class takes the desired choice of ticket type to redirect
 *     it to the appropriate scout type class.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 */
class TicketScoutManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketScoutManager.class);
    TicketScoutManager(IChannel channel, String choice, String discordID)
    {
        choice = choice.toLowerCase();

        LOGGER.debug("Starting TICKET Scout...");
        switch (choice)
        {
            case "nts":
            case "ntsi":
            case "ntm":
            case "ntmi":
                LOGGER.debug("[NORMAL] \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Normal(channel, choice, discordID);
                break;
            case "pts":
            case "ptsi":
            case "ptm":
            case "ptmi":
                LOGGER.debug("[PLUS] \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Plus(channel, choice, discordID);
                break;
            case "nt2s":
            case "nt2si":
            case "nt2m":
            case "nt2mi":
                LOGGER.debug("[NORMAL v2] \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Normalv2(channel, choice, discordID);
                break;
            case "pt2s":
            case "pt2si":
            case "pt2m":
            case "pt2mi":
                LOGGER.debug("[PLUS v2] \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Plusv2(channel, choice, discordID);
                break;
            default:
                break;
        }
        LOGGER.debug("TICKET Scout Complete!");
    }
}
