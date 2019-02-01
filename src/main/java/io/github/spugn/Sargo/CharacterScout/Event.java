package io.github.spugn.Sargo.CharacterScout;

import io.github.spugn.Sargo.Objects.Character;
import sx.blah.discord.handle.obj.IChannel;

public class Event extends CharacterScout
{
    public Event(IChannel channel, int bannerID, String choice, String discordID)
    {
        super(channel, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        /* EVENT SCOUTS DO NOT NEED TO INITIALIZE ANY BANNER INFORMATION. */
    }

    @Override
    protected void modifyScoutData()
    {
        singleScoutPrice = 0;
    }

    @Override
    protected void updateBannerData()
    {
        /* EVENT SCOUTS DO NOT HAVE ANY BANNER DATA UPDATES. */
    }

    @Override
    protected Character randGoldCharacter()
    {
        /* THIS SCOUT TYPE DOES NOT USE THIS FUNCTIONALITY */
        return null;
    }

    @Override
    protected Character randPlatinumCharacter()
    {
        /* THIS SCOUT TYPE DOES NOT USE THIS FUNCTIONALITY */
        return null;
    }

    @Override
    protected void setupScoutMenu()
    {
        if (!SIMPLE_MESSAGE)
        {
            switch (CHOICE.toLowerCase())
            {
                case "s":
                case "si":
                    scoutMenu.withTitle("[Event] - Single Pull");
                    break;
                default:
                    scoutMenu.withTitle("[Event] - Unknown");
                    break;
            }
        }
        else
        {
            switch (CHOICE.toLowerCase())
            {
                case "s":
                case "si":
                    simpleMessage += "**[Event] - Single Pull**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Event] - Unknown**" + "\n";
                    break;
            }
        }
    }

    @Override
    protected void run()
    {
        switch (CHOICE.toLowerCase())
        {
            case "s":
            case "si":
                if (userMemoryDiamonds < singleScoutPrice)
                {
                    print_NotEnoughMemoryDiamonds_Single_Message();
                    return;
                }

                if (CHOICE.equalsIgnoreCase("si") && !IMAGE_DISABLED)
                {
                    generateImage = true;
                }

                userMemoryDiamonds -= singleScoutPrice;
                USER.setMemoryDiamonds(userMemoryDiamonds);

                doSinglePull();
                break;
            default:
                print_UnknownScoutType_s_Message();
                return;
        }

        if (stopScout)
            return;

        displayAndSave();
    }
}
