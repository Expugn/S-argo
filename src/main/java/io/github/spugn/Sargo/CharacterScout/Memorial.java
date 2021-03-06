package io.github.spugn.Sargo.CharacterScout;

import discord4j.core.object.entity.Message;
import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;

import java.util.List;

/**
 * MEMORIAL CHARACTER SCOUT
 * <p>
 *     Memorial Scouts are similar to {@link Normal} scouts where there are
 *     no added benefits no matter how many scouts you do, however,
 *     it has a special gimmick of only allowing single scouts priced
 *     at 1 Memory Diamond per scout.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v2.0
 * @see Normal
 * @see CharacterScout
 */
public class Memorial extends CharacterScout
{

    public Memorial(Message message, int bannerID, String choice, String discordID)
    {
        super(message, bannerID, choice, discordID);
        run();
    }

    @Override
    protected void initBannerInfo()
    {
        /* MEMORIAL SCOUTS DO NOT NEED TO INITIALIZE ANY BANNER INFORMATION. */
    }

    @Override
    protected void modifyScoutData()
    {
        // REMOVE 1.5% FROM PLATINUM (IT'S ORIGINAL VALUE)
        COPPER = COPPER + (PLATINUM - (PLATINUM / 1.5));
        PLATINUM = PLATINUM / 1.5;

        singleScoutPrice = 1;
    }

    @Override
    protected void updateBannerData()
    {
        /* MEMORIAL SCOUTS DO NOT HAVE ANY BANNER DATA UPDATES. */
    }

    @Override
    protected Character randGoldCharacter()
    {
        int randIndex = GOLD_BANNERS_V2.get(RNG.nextInt(GOLD_BANNERS_V2.size()));
        Banner randBanner = BANNERS.get(randIndex - 1);
        List<Character> randCharacters = randBanner.getCharacters();
        return randCharacters.get(RNG.nextInt(randCharacters.size()));
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
                    sMenu = sMenu.andThen(s -> s.setTitle("[Memorial] - Single Pull"));
                    break;
                default:
                    sMenu = sMenu.andThen(s -> s.setTitle("[Memorial] - Unknown"));
                    break;
            }
        }
        else
        {
            switch (CHOICE.toLowerCase())
            {
                case "s":
                case "si":
                    simpleMessage += "**[Memorial] - Single Pull**" + "\n";
                    break;
                default:
                    simpleMessage += "**[Memorial] - Unknown**" + "\n";
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
