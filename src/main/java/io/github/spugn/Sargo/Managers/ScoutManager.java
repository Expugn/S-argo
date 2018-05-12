package io.github.spugn.Sargo.Managers;

import io.github.spugn.Sargo.CharacterScout.*;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;

import java.util.List;

/**
 * SCOUT MANAGER
 * <p>
 *     This class takes a desired banner ID and makes sure the banner is within bounds
 *     and then it reads the banner type of that banner to redirect it to the appropriate
 *     scout type class.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 */
class ScoutManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoutManager.class);
    ScoutManager(IChannel channel, int bannerID, String choice, String discordID)
    {
        List<Banner> banners = BannerParser.getBanners();
        if (!(bannerID - 1 < banners.size() && bannerID - 1 >= 0))
        {
            channel.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.").get().build());
            return;
        }

        LOGGER.debug("Starting CHARACTER Scout...");
        Banner selectedBanner = banners.get(bannerID - 1);
        switch (selectedBanner.getBannerType())
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
                LOGGER.debug("[RECORD CRYSTAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new RecordCrystal(channel, bannerID, choice, discordID);
                break;
            case 3:
                LOGGER.debug("[STEP UP V2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new StepUpv2(channel, bannerID, choice, discordID);
                break;
            case 4:
                LOGGER.debug("[BIRTHDAY STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new BirthdayStepUp(channel, bannerID, choice, discordID);
                break;
            case 5:
                LOGGER.debug("[RECORD CRYSTAL V2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new RecordCrystalv2(channel, bannerID, choice, discordID);
                break;
            case 6:
                LOGGER.debug("[MEMORIAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channel.getName() + "\"");
                new Memorial(channel, bannerID, choice, discordID);
                break;
            case 7:
                LOGGER.debug("[STEP UP V3] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new StepUpv3(channel, bannerID, choice, discordID);
                break;
            case 8:
                LOGGER.debug("[RECORD CRYSTAL V3] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new RecordCrystalv3(channel, bannerID, choice, discordID);
                break;
            case 9:
                LOGGER.debug("[EVENT] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new Event(channel, bannerID, choice, discordID);
                break;
            case 10:
                LOGGER.debug("[SAO GAME 5TH ANNIVERSARY STEP UP] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new SAOGameFifthAnniversaryStepUp(channel, bannerID, choice, discordID);
                break;
            case 11:
                LOGGER.debug("[RECORD CRYSTAL V4] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new RecordCrystalv4(channel, bannerID, choice, discordID);
                break;
            case 12:
                LOGGER.debug("[STEP UP V4] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new StepUpv4(channel, bannerID, choice, discordID);
                break;
            case 13:
                LOGGER.debug("[STEP UP V5] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new StepUpv5(channel, bannerID, choice, discordID);
                break;
            case 14:
                LOGGER.debug("[SAO GAME 5TH ANNIVERSARY STEP UP V2] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new SAOGameFifthAnniversaryStepUpv2(channel, bannerID, choice, discordID);
                break;
            case 15:
                LOGGER.debug("[STEP UP V6] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channel.getName() + "\"");
                new StepUpv6(channel, bannerID, choice, discordID);
                break;
            default:
                channel.sendMessage(new WarningMessage("UNKNOWN BANNER TYPE", "Please correct the issue or update the bot.").get().build());
                break;
        }
        LOGGER.debug("CHARACTER Scout Complete!");
    }
}
