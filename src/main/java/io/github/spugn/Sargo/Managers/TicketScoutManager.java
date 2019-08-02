package io.github.spugn.Sargo.Managers;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import io.github.spugn.Sargo.TicketScout.Normal;
import io.github.spugn.Sargo.TicketScout.Normalv2;
import io.github.spugn.Sargo.TicketScout.Plus;
import io.github.spugn.Sargo.TicketScout.Plusv2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    TicketScoutManager(Message message, String choice, String discordID)
    {
        choice = choice.toLowerCase();

        Member user = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(discordID))).block();
        String username = user.getUsername() + "#" + user.getDiscriminator();
        LOGGER.debug("Starting TICKET Scout for " + (username) + " in " + message.getGuild().block().getName() +  "...");

        String channelName = message.getGuild().block().getChannelById(message.getChannelId()).block().getName();

        switch (choice)
        {
            case "nts":
            case "ntsi":
            case "ntm":
            case "ntmi":
                LOGGER.debug("[NORMAL] \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Normal(message, choice, discordID);
                break;
            case "pts":
            case "ptsi":
            case "ptm":
            case "ptmi":
                LOGGER.debug("[PLUS] \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Plus(message, choice, discordID);
                break;
            case "nt2s":
            case "nt2si":
            case "nt2m":
            case "nt2mi":
                LOGGER.debug("[NORMAL v2] \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Normalv2(message, choice, discordID);
                break;
            case "pt2s":
            case "pt2si":
            case "pt2m":
            case "pt2mi":
                LOGGER.debug("[PLUS v2] \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Plusv2(message, choice, discordID);
                break;
            default:
                break;
        }
        LOGGER.debug("TICKET Scout Complete!");
    }
}
