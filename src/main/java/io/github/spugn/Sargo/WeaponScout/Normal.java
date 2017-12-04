package io.github.spugn.Sargo.WeaponScout;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.WarningMessage;
import sx.blah.discord.handle.obj.IChannel;

/**
 * NORMAL WEAPON SCOUT
 * <p>
 *     Normal Scouts are scouts that have static pull rates every time.
 *     Compared to other scout types, there are no special gimmicks.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see WeaponScout
 */
public class Normal extends WeaponScout
{
    public Normal(IChannel channel, int bannerID, String choice, String discordID)
    {
        super(channel, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        /* NORMAL SCOUTS DO NOT NEED TO INITIALIZE ANY BANNER INFORMATION. */
    }

    @Override
    protected void modifyScoutData()
    {
        /* NORMAL SCOUTS DO NOT MODIFY ANY SCOUT DATA. */
    }

    @Override
    protected void updateBannerData()
    {
        /* NORMAL SCOUTS DO NOT HAVE ANY BANNER DATA UPDATES. */
    }

    @Override
    protected void setupScoutMenu()
    {
        if (!SIMPLE_MESSAGE)
        {
            switch (CHOICE.toLowerCase())
            {
                case "ws":
                case "wsi":
                    scoutMenu.withTitle("[Weapon Scout] - Single Pull");
                    break;
                case "wm":
                case "wmi":
                    scoutMenu.withTitle("[Weapon Scout] - Multi Pull");
                    break;
                default:
                    scoutMenu.withTitle("[Weapon Scout] - Unknown");
                    break;
            }
        }
        else
        {
            switch (CHOICE.toLowerCase())
            {
                case "ws":
                case "wsi":
                    simpleMessage += "**[Weapon Scout] - Single Pull**" + "\n";
                    break;
                case "wm":
                case "wmi":
                    simpleMessage += "**[Weapon Scout] - Multi Pull**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Weapon Scout] - Unknown**" + "\n";
                    break;
            }
        }
    }

    @Override
    protected void run()
    {
        switch (CHOICE.toLowerCase())
        {
            case "ws":
            case "wsi":
                if (userMemoryDiamonds < singleScoutPrice)
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + singleScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.").get().build());
                    return;
                }

                if (CHOICE.equalsIgnoreCase("wsi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= singleScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doSinglePull();
                break;
            case "wm":
            case "wmi":
                if (userMemoryDiamonds < multiScoutPrice)
                {
                    CHANNEL.sendMessage(new WarningMessage("NOT ENOUGH MEMORY DIAMONDS", "You need **" + multiScoutPrice + "** Memory Diamonds to scout.\nUse '" + CommandManager.getCommandPrefix() + "**shop**' to get more Memory Diamonds.").get().build());
                    return;
                }

                if (CHOICE.equalsIgnoreCase("wmi") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= multiScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doMultiPull();
                break;
            default:
                CHANNEL.sendMessage(new WarningMessage("UNKNOWN/UNAVAILABLE SCOUT TYPE", "Use '" + CommandManager.getCommandPrefix() + "**scout** " + BANNER_ID + "' and read the footer text for available scout types.").get().build());
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
