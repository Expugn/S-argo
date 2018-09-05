package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.WeaponScout.GGOStepUp;
import io.github.spugn.Sargo.WeaponScout.Normal;
import io.github.spugn.Sargo.WeaponScout.StepUp;
import io.github.spugn.Sargo.WeaponScout.StepUpv2;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

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

    WeaponScoutManager(IChannel channel, int bannerID, String choice, String discordID)
    {
        //List<Banner> banners = new BannerParser().getBanners();
        List<Banner> banners = BannerParser.getBanners();
        if (!(bannerID - 1 < banners.size() && bannerID - 1 >= 0))
        {
            channel.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.").get().build());
            return;
        }
        LOGGER.debug("Starting WEAPON Scout for " + (channel.getGuild().getUserByID(Long.parseLong(discordID)).getName() + "#" + channel.getGuild().getUserByID(Long.parseLong(discordID)).getDiscriminator()) + "...");
        Banner selectedBanner = banners.get(bannerID - 1);

        if (selectedBanner.getWeapons().size() <= 0)
        {
            channel.sendMessage(new WarningMessage("NO WEAPONS AVAILABLE", "This banner does not have a weapon scout.").get().build());
            channel.setTypingStatus(false);
            return;
        }

        switch (selectedBanner.getBannerWepType())
        {
            case 0:
                LOGGER.debug("[NORMAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Normal(channel, bannerID, choice, discordID);
                break;
            case 1:
                LOGGER.debug("[STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new StepUp(channel, bannerID, choice, discordID);
                break;
            case 2:
                LOGGER.debug("[GGO STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new GGOStepUp(channel, bannerID, choice, discordID);
                break;
            case 3:
                LOGGER.debug("[STEP UP v2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new StepUpv2(channel, bannerID, choice, discordID);
                break;
            default:
                channel.sendMessage(new WarningMessage("UNKNOWN BANNER TYPE", "Please correct the issue or update the bot.").get().build());
                break;
        }
        LOGGER.debug("WEAPON Scout Complete!");
    }
}
