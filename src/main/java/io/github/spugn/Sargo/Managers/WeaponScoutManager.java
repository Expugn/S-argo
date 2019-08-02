package io.github.spugn.Sargo.Managers;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.WeaponScout.*;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * WEAPON SCOUT MANAGER
 * <p>
 *     This class takes a desired banner ID and makes sure the banner is within bounds.
 *     It will then check if there are weapons available before reading the banner type
 *     of that character to redirect it to the appropriate scout type class.
 *     If there are no weapons available in the banner, the process is stopped.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 */
class WeaponScoutManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketScoutManager.class);

    WeaponScoutManager(Message message, int bannerID, String choice, String discordID)
    {
        List<Banner> banners = BannerParser.getBanners();
        if (!(bannerID - 1 < banners.size() && bannerID - 1 >= 0))
        {
            Sargo.replyToMessage_Warning(message, "UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.");
            return;
        }

        Member user = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(discordID))).block();
        String username = user.getUsername() + "#" + user.getDiscriminator();
        LOGGER.debug("Starting WEAPON Scout for " + (username) + " in " + message.getGuild().block().getName() + "...");

        String channelName = message.getGuild().block().getChannelById(message.getChannelId()).block().getName();
        Banner selectedBanner = banners.get(bannerID - 1);

        if (selectedBanner.getWeapons().size() <= 0)
        {
            Sargo.replyToMessage_Warning(message, "NO WEAPONS AVAILABLE", "This banner does not have a weapon scout.");
            return;
        }

        switch (selectedBanner.getBannerWepType())
        {
            case 0:
                LOGGER.debug("[NORMAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Normal(message, bannerID, choice, discordID);
                break;
            case 1:
                LOGGER.debug("[STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new StepUp(message, bannerID, choice, discordID);
                break;
            case 2:
                LOGGER.debug("[GGO STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new GGOStepUp(message, bannerID, choice, discordID);
                break;
            case 3:
                LOGGER.debug("[STEP UP v2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new StepUpv2(message, bannerID, choice, discordID);
                break;
            case 4:
                LOGGER.debug("[STEP UP v3] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new StepUpv3(message, bannerID, choice, discordID);
                break;
            default:
                Sargo.replyToMessage_Warning(message, "UNKNOWN BANNER TYPE", "Please correct the issue or update the bot.");
                break;
        }
        LOGGER.debug("WEAPON Scout Complete!");
    }
}
