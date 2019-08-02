package io.github.spugn.Sargo.Managers;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.util.Snowflake;
import io.github.spugn.Sargo.CharacterScout.*;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Sargo;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    ScoutManager(Message message, int bannerID, String choice, String discordID)
    {
        List<Banner> banners = BannerParser.getBanners();
        if (!(bannerID - 1 < banners.size() && bannerID - 1 >= 0))
        {
            Sargo.replyToMessage_Warning(message, "UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.");
            return;
        }

        Member user = message.getGuild().block().getMemberById(Snowflake.of(Long.parseLong(discordID))).block();
        String username = user.getUsername() + "#" + user.getDiscriminator();
        LOGGER.debug("Starting CHARACTER Scout for " + (username) + " in " + message.getGuild().block().getName() + "...");

        String channelName = message.getGuild().block().getChannelById(message.getChannelId()).block().getName();
        Banner selectedBanner = banners.get(bannerID - 1);
        switch (selectedBanner.getBannerType())
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
                LOGGER.debug("[RECORD CRYSTAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new RecordCrystal(message, bannerID, choice, discordID);
                break;
            case 3:
                LOGGER.debug("[STEP UP V2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new StepUpv2(message, bannerID, choice, discordID);
                break;
            case 4:
                LOGGER.debug("[BIRTHDAY STEP UP] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new BirthdayStepUp(message, bannerID, choice, discordID);
                break;
            case 5:
                LOGGER.debug("[RECORD CRYSTAL V2] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new RecordCrystalv2(message, bannerID, choice, discordID);
                break;
            case 6:
                LOGGER.debug("[MEMORIAL] " + banners.get(bannerID - 1).getBannerName() + " \"" + choice + "\" in Channel \"" + channelName + "\"");
                new Memorial(message, bannerID, choice, discordID);
                break;
            case 7:
                LOGGER.debug("[STEP UP V3] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv3(message, bannerID, choice, discordID);
                break;
            case 8:
                LOGGER.debug("[RECORD CRYSTAL V3] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new RecordCrystalv3(message, bannerID, choice, discordID);
                break;
            case 9:
                LOGGER.debug("[EVENT] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new Event(message, bannerID, choice, discordID);
                break;
            case 10:
                LOGGER.debug("[SAO GAME 5TH ANNIVERSARY STEP UP] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new SAOGameFifthAnniversaryStepUp(message, bannerID, choice, discordID);
                break;
            case 11:
                LOGGER.debug("[RECORD CRYSTAL V4] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new RecordCrystalv4(message, bannerID, choice, discordID);
                break;
            case 12:
                LOGGER.debug("[STEP UP V4] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv4(message, bannerID, choice, discordID);
                break;
            case 13:
                LOGGER.debug("[STEP UP V5] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv5(message, bannerID, choice, discordID);
                break;
            case 14:
                LOGGER.debug("[SAO GAME 5TH ANNIVERSARY STEP UP V2] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new SAOGameFifthAnniversaryStepUpv2(message, bannerID, choice, discordID);
                break;
            case 15:
                LOGGER.debug("[STEP UP V6] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv6(message, bannerID, choice, discordID);
                break;
            case 16:
                LOGGER.debug("[SAO GAME 5TH ANNIVERSARY STEP UP V3] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new SAOGameFifthAnniversaryStepUpv3(message, bannerID, choice, discordID);
                break;
            case 17:
                LOGGER.debug("[STEP UP V7] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv7(message, bannerID, choice, discordID);
                break;
            case 18:
                LOGGER.debug("[RECORD CRYSTAL V5] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new RecordCrystalv5(message, bannerID, choice, discordID);
                break;
            case 19:
                LOGGER.debug("[STEP UP V8] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv8(message, bannerID, choice, discordID);
                break;
            case 20:
                LOGGER.debug("[RECORD CRYSTAL V6] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new RecordCrystalv6(message, bannerID, choice, discordID);
                break;
            case 21:
                LOGGER.debug("[STEP UP V9] " + banners.get(bannerID - 1).getBannerName() + " " + choice + " in Channel \"" + channelName + "\"");
                new StepUpv9(message, bannerID, choice, discordID);
                break;
            default:
                Sargo.replyToMessage_Warning(message, "UNKNOWN BANNER TYPE", "Please correct the issue or update the bot.");
                break;
        }
        LOGGER.debug("CHARACTER Scout Complete!");
    }
}
