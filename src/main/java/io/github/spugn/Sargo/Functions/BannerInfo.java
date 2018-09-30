package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Utilities.GitHubImage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.ScoutSettingsParser;
import sx.blah.discord.handle.obj.IChannel;

import java.text.DecimalFormat;
import java.util.*;

/**
 * BANNER INFO
 * <p>
 *     Manages the information in the Embed Messages when
 *     listing available banners or information about a
 *     specific banner.
 * </p>
 *
 * @author S'pugn
 * @version 2.0
 * @since v1.0
 * @see BannerListMenu
 * @see BannerInfoMenu
 */
public class BannerInfo
{
    private static IChannel CHANNEL;
    private static List<Banner> BANNERS;

    private double copper;
    private double silver;
    private double gold;
    private double platinum;
    private double platinum6;

    private List<Double> recordCrystal;
    private List<Double> circulatingRecordCrystal;

    private int bannerID;

    private int page;

    public BannerInfo(IChannel channel, String page)
    {
        CHANNEL = channel;

        /* READ Banners.xml */
        BANNERS = BannerParser.getBanners();

        this.page = Integer.parseInt(page);
        if (this.page < 1)
        {
            this.page = 1;
        }

        listBanners();
    }

    public BannerInfo(IChannel channel, int bannerID)
    {
        CHANNEL = channel;
        this.bannerID = bannerID - 1;

        /* READ Banners.xml */
        BANNERS = BannerParser.getBanners();

        copper = (int) (ScoutSettingsParser.getCopperRate() * 100);
        silver = (int) (ScoutSettingsParser.getSilverRate() * 100);
        gold = (int) (ScoutSettingsParser.getGoldRate() * 100);
        platinum = (int) (ScoutSettingsParser.getPlatinumRate() * 100);
        platinum6 = (int) (ScoutSettingsParser.getPlatinum6Rate() * 100);
        recordCrystal = ScoutSettingsParser.getRecordCrystalRates();
        circulatingRecordCrystal = ScoutSettingsParser.getCirculatingRecordCrystalRates();

        getBannerInfo();
    }

    private void listBanners()
    {
        BannerListMenu menu = new BannerListMenu();
        menu.setBannerCount(BANNERS.size());
        int pageLength = 10;

        SortedMap<Integer, String> bannerList = new TreeMap<>();

        for (Banner b : BANNERS)
        {
            bannerList.put(b.getBannerID(), b.getBannerName());
        }

        String message = "";
        if (page > (((bannerList.size() % pageLength) == 0) ? bannerList.size() / pageLength : (bannerList.size() / pageLength) + 1))
            page = (((bannerList.size() % pageLength) == 0) ? bannerList.size() / pageLength : (bannerList.size() / pageLength) + 1);

        menu.setCurrentPage(page);
        menu.setHighestPage(((bannerList.size() % pageLength) == 0) ? bannerList.size() / pageLength : (bannerList.size() / pageLength) + 1);
        menu.setBannerCount(bannerList.size());

        int i = 0, k = 0;
        page--;

        for(final Map.Entry<Integer, String> e : bannerList.entrySet())
        {
            k++;
            if ((((page * pageLength) + i + 1) == k) && (k != (( page * pageLength) + pageLength + 1)))
            {
                i++;
                message += "**" + e.getKey() + "**) *" + e.getValue() + "*\n";
            }
        }

        menu.setBannerList(message);
        CHANNEL.sendMessage(menu.get().build());
    }

    private void getBannerInfo()
    {
        if (bannerID < BANNERS.size() && bannerID >= 0)
        {
            Banner banner = BANNERS.get(bannerID);
            BannerInfoMenu menu = new BannerInfoMenu();
            Random rng = new Random();
            int charIndex = rng.nextInt(banner.getCharacters().size());

            menu.setBannerType(banner.bannerTypeToString());
            menu.setBannerWepType(banner.getBannerWepType());
            menu.setBannerName(banner.getBannerName());
            menu.setCharacterAmount(banner.getCharacters().size());
            menu.setBannerID(bannerID);
            menu.setImageURL(new GitHubImage(banner.getCharacters().get(charIndex).getImagePath()).getURL());

            /* CREATE CHARACTER LIST */
            String charList = "";
            for (Character c : banner.getCharacters())
            {
                charList += "\n" + c.toString();
            }
            menu.setCharacterList(charList);

            /* IF THERE ARE WEAPONS, CREATE WEAPON LIST */
            if (banner.getWeapons().size() > 0)
            {
                String weapList = "";
                for (Weapon w : banner.getWeapons())
                {
                    weapList += "\n" + w.toString();
                }
                menu.setWeaponAmount(banner.getWeapons().size());
                menu.setWeaponList(weapList);
            }

            /* CREATE RATE LIST */
            List<Character> goldCharacters = new ArrayList<>();
            List<Character> platinumCharacters = new ArrayList<>();
            List<Character> platinum6Characters = new ArrayList<>();

            /* SORT GOLD/PLATINUM/PLATINUM6 CHARACTERS AND STORE THEM IN THEIR OWN ARRAYS */
            for (Character character : banner.getCharacters())
            {
                if (character.getRarity() == 4)
                {
                    goldCharacters.add(character);
                }
                else if (character.getRarity() == 5)
                {
                    platinumCharacters.add(character);
                }
                else if (character.getRarity() == 6)
                {
                    platinum6Characters.add(character);
                }
            }

            /* NO PLATINUM/PLATINUM6 CHARACTER, ADJUST RATES */
            if (platinumCharacters.size() <= 0 && platinum6Characters.size() <= 0)
            {
                copper += platinum;
                platinum = 0;

                copper += platinum6;
                platinum6 = 0;
            }
            /* PLATINUM CHARACTERS EXIST BUT NOT PLATINUM6, ADJUST RATES */
            else if (platinumCharacters.size() > 0 && platinum6Characters.size() <= 0)
            {
                copper += platinum6;
                platinum6 = 0;
            }

            /* IF EVENT SCOUT, SET EVERYTHING BUT GOLD TO 0 */
            if (banner.getBannerType() == 9)
            {
                copper = 0.0;
                silver = 0.0;

                if (goldCharacters.size() > 0)
                {
                    gold = 100.0;
                }
                if (platinumCharacters.size() > 0)
                {
                    platinum = 100.0;
                }
                if (platinum6Characters.size() > 0)
                {
                    platinum6 = 100.0;
                }
            }

            /* IF RECORD CRYSTAL V4 AND ABOVE, INCREASE PLATINUM RATES BY 1.5 */
            /*
            if (banner.getBannerType() == 11 ||
                    banner.getBannerType() == 12 ||
                    banner.getBannerType() == 13 ||
                    banner.getBannerType() == 14 ||
                    banner.getBannerType() == 15 ||
                    banner.getBannerType() == 16)
            {
                copper = copper - ((platinum * 1.5) - platinum);
                platinum = platinum * 1.5;
            }
            */

            // IF OLDER THAN RECORD CRYSTAL V4 (EXCEPT EVENT), DECREASE PLATINUM RATES BY 1.5
            if (banner.getBannerType() == 10 ||
                    banner.getBannerType() == 8 ||
                    banner.getBannerType() == 7 ||
                    banner.getBannerType() == 6 ||
                    banner.getBannerType() == 5 ||
                    banner.getBannerType() == 4 ||
                    banner.getBannerType() == 3 ||
                    banner.getBannerType() == 2 ||
                    banner.getBannerType() == 1 ||
                    banner.getBannerType() == 0)
            {
                copper = copper + (platinum - (platinum / 1.5));
                platinum = platinum / 1.5;
            }

            String ratesList = "";
            if (platinum6 != 0)
                ratesList += "[6 ★] " + platinum6 + "%\n";
            if (platinum != 0)
                ratesList += "[5 ★] " + platinum + "%\n";
            ratesList += "[4 ★] " + gold + "%\n";
            ratesList += "[3 ★] " + silver + "%\n";
            ratesList += "[2 ★] " + copper + "%";

            menu.setRatesList(ratesList);

            /* BANNER IS STEP UP */
            if (banner.getBannerType() == 1)
            {
                double tC = copper - ((gold * 1.5) - gold);
                double tS = silver;
                double tG = gold * 1.5;
                double tP = platinum;
                double tP6 = platinum6;

                String stepThreeRates = "";
                if (tP6 != 0)
                    stepThreeRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(4 ★ Scout Rates 1.5x)**";
                menu.setStepThreeRatesList(stepThreeRates);

                tC = copper - ((gold * 2.0) - gold);
                tS = silver;
                tG = gold * 2.0;
                tP = platinum;
                tP6 = platinum6;

                String stepFiveRates = "";
                if (tP6 != 0)
                    stepFiveRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepFiveRates += "[5 ★] " + tP + "%\n";
                stepFiveRates += "[4 ★] " + tG + "%\n";
                stepFiveRates += "[3 ★] " + tS + "%\n";
                stepFiveRates += "[2 ★] " + tC + "%\n";
                stepFiveRates += "**(4 ★ Scout Rates 2.0x)**";
                menu.setStepFiveRatesList(stepFiveRates);
            }
            /* BANNER IS STEP UP V2 */
            else if (banner.getBannerType() == 3 ||
                    banner.getBannerType() == 10 ||
                    banner.getBannerType() == 12 ||
                    banner.getBannerType() == 13 ||
                    banner.getBannerType() == 14 ||
                    banner.getBannerType() == 16)
            {
                double tC = copper - ((platinum * 1.5) - platinum);
                double tS = silver;
                double tG = gold;
                double tP = platinum * 1.5;
                double tP6 = platinum6;

                if (banner.getBannerType() == 16)
                {
                    String stepOneRates = "";
                    if (tP6 != 0)
                        stepOneRates += "[6 ★] 0.0%\n";
                    stepOneRates += "[5 ★] 3.0%\n";
                    stepOneRates += "[4 ★] 97.0%\n";
                    stepOneRates += "[3 ★] 0.0%\n";
                    stepOneRates += "[2 ★] 0.0%\n";
                    stepOneRates += "**(For One Character)**";
                    menu.setStepOneRatesList(stepOneRates);
                }

                String stepThreeRates = "";
                if (tP6 != 0)
                    stepThreeRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(5 ★ Scout Rates 1.5x)**";
                menu.setStepThreeRatesList(stepThreeRates);

                tC = 0.0;
                tS = 0.0;
                tG = 0.0;
                tP = 100.0;
                tP6 = 0.0;

                String stepFiveRates = "";
                if (tP6 != 0)
                    stepFiveRates += "[6 ★] " + tP6 + "%\n";
                stepFiveRates += "[5 ★] " + tP + "%\n";
                stepFiveRates += "[4 ★] " + tG + "%\n";
                stepFiveRates += "[3 ★] " + tS + "%\n";
                stepFiveRates += "[2 ★] " + tC + "%\n";
                stepFiveRates += "**(For One Character)**";
                menu.setStepFiveRatesList(stepFiveRates);

                tC = copper - ((platinum * 2.0) - platinum);
                tS = silver;
                tG = gold;
                tP = platinum * 2.0;
                tP6 = platinum6;

                String stepSixRates = "";
                if (tP6 != 0)
                    stepSixRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepSixRates += "[5 ★] " + tP + "%\n";
                stepSixRates += "[4 ★] " + tG + "%\n";
                stepSixRates += "[3 ★] " + tS + "%\n";
                stepSixRates += "[2 ★] " + tC + "%\n";
                stepSixRates += "**(5 ★ Scout Rates 2.0x)**";
                menu.setStepSixRatesList(stepSixRates);
            }
            /* BANNER IS STEP UP V7 */
            else if (banner.getBannerType() == 17)
            {
                double tC = copper - ((platinum6 * 1.5) - platinum6);
                double tS = silver;
                double tG = gold;
                double tP = platinum;
                double tP6 = platinum6 * 1.5;

                String stepThreeRates = "";
                if (tP6 != 0)
                    stepThreeRates += "[6 ★] " + tP6 + "%\n";
                stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(6 ★ Scout Rates 1.5x)**";
                menu.setStepThreeRatesList(stepThreeRates);

                tC = 0.0;
                tS = 0.0;
                tG = 0.0;
                tP = 0.0;
                tP6 = 100.0;

                String stepFiveRates = "";
                stepFiveRates += "[6 ★] " + tP6 + "%\n";
                stepFiveRates += "[5 ★] " + tP + "%\n";
                stepFiveRates += "[4 ★] " + tG + "%\n";
                stepFiveRates += "[3 ★] " + tS + "%\n";
                stepFiveRates += "[2 ★] " + tC + "%\n";
                stepFiveRates += "**(For One Character)**";
                menu.setStepFiveRatesList(stepFiveRates);

                tC = copper - ((platinum6 * 2.0) - platinum6);
                tS = silver;
                tG = gold;
                tP = platinum;
                tP6 = platinum6 * 2.0;

                String stepSixRates = "";
                if (tP6 != 0)
                    stepSixRates += "[6 ★] " + tP6 + "%\n";
                stepSixRates += "[5 ★] " + tP + "%\n";
                stepSixRates += "[4 ★] " + tG + "%\n";
                stepSixRates += "[3 ★] " + tS + "%\n";
                stepSixRates += "[2 ★] " + tC + "%\n";
                stepSixRates += "**(6 ★ Scout Rates 2.0x)**";
                menu.setStepSixRatesList(stepSixRates);
            }
            /* BANNER IS BIRTHDAY STEP UP */
            else if (banner.getBannerType() == 4)
            {
                double tC = copper - (((platinum * 2.0) - platinum) + ((gold * 2.0) - gold));
                double tS = silver;
                double tG = gold * 2.0;
                double tP = platinum * 2.0;
                double tP6 = platinum6;

                String stepThreeRates = "";
                if (tP6 != 0)
                    stepThreeRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(4+ ★ Scout Rates 2.0x)**";
                menu.setStepThreeRatesList(stepThreeRates);
            }
            /* BANNER IS RECORD CRYSTAL */
            else if (banner.getBannerType() == 2 ||
                    banner.getBannerType() == 5 ||
                    banner.getBannerType() == 8 ||
                    banner.getBannerType() == 11 ||
                    banner.getBannerType() == 18)
            {
                int counter = 0;
                String recordCrystalRates = "";
                DecimalFormat df = new DecimalFormat("0.0");

                for (double d : recordCrystal)
                {
                    if (d != 0)
                    {
                        recordCrystalRates += "[" + ++counter + " RC] " + df.format((d * 100)) + "%\n";
                    }
                }

                menu.setRecordCrystalRatesList(recordCrystalRates);

                if (banner.getBannerType() == 8 ||
                        banner.getBannerType() == 11)
                {
                    int counter2 = 0;
                    String circluatingRecordCrystalRates = "";
                    if (banner.getBannerType() == 8 ||
                            banner.getBannerType() == 11)
                    {
                        for (double d : circulatingRecordCrystal)
                        {
                            if (d != 0)
                            {
                                circluatingRecordCrystalRates += "[" + ++counter2 + " RC] " + df.format((d * 100)) + "%\n";
                            }
                        }
                    }
                    else
                    {
                        for (double d : recordCrystal)
                        {
                            if (d != 0)
                            {
                                circluatingRecordCrystalRates += "[" + ++counter2 + " RC] " + df.format((d * 100)) + "%\n";
                            }
                        }
                    }
                    menu.setCirculatingRecordCrystalRatesList(circluatingRecordCrystalRates);
                }
            }
            /* BANNER IS STEP UP V3 */
            else if (banner.getBannerType() == 7 ||
                     banner.getBannerType() == 15)
            {
                double tC = copper - ((platinum * 2.0) - platinum);
                double tS = silver;
                double tG = gold;
                double tP = platinum * 2.0;
                double tP6 = platinum6;

                String stepThreeRates = "";
                if (tP6 != 0)
                    stepThreeRates += "[6 ★] " + tP6 + "%\n";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(5 ★ Scout Rates 2.0x)**";
                menu.setStepThreeRatesList(stepThreeRates);
            }



            /* WEAPON BANNER IS STEP UP */
            if (banner.getBannerWepType() == 1)
            {
                double tC = (copper + platinum) - ((gold * 2.0) - gold);
                double tS = silver;
                double tG = gold * 2.0;

                String stepThreeRates = "";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(4 ★ Weapon Rates 2.0x)**";
                menu.setStepThreeWeaponRatesList(stepThreeRates);
            }
            /* WEAPON BANNER IS GGO STEP UP OR STEP UP V2 */
            else if (banner.getBannerWepType() == 2 ||
                    banner.getBannerWepType() == 3)
            {
                double tC = (copper + platinum) - ((gold * 1.5) - gold);
                double tS = silver;
                double tG = gold * 1.5;

                String stepThreeRates = "";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(4 ★ Weapon Rates 1.5x)**";
                menu.setStepThreeWeaponRatesList(stepThreeRates);

                tC = 0.0;
                tS = 0.0;
                tG = 100.0;

                String stepFiveRates = "";
                stepFiveRates += "[4 ★] " + tG + "%\n";
                stepFiveRates += "[3 ★] " + tS + "%\n";
                stepFiveRates += "[2 ★] " + tC + "%\n";
                stepFiveRates += "**(For One Weapon)**";
                menu.setStepFiveWeaponRatesList(stepFiveRates);

                tC = (copper + platinum) - ((gold * 2.0) - gold);
                tS = silver;
                tG = gold * 2.0;

                String stepSixRates = "";
                stepSixRates += "[4 ★] " + tG + "%\n";
                stepSixRates += "[3 ★] " + tS + "%\n";
                stepSixRates += "[2 ★] " + tC + "%\n";
                stepSixRates += "**(4 ★ Weapon Rates 2.0x)**";
                menu.setStepSixWeaponRatesList(stepSixRates);
            }

            CHANNEL.sendMessage(menu.get().build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.getCommandPrefix() + "**scout**' for a list of banners.").get().build());
        }
    }
}
