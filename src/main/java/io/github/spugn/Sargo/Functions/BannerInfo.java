package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Managers.CommandManager;
import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.handle.obj.IChannel;

import java.text.DecimalFormat;
import java.util.*;

public class BannerInfo
{
    private static IChannel CHANNEL;
    private static List<Banner> BANNERS;

    private double copper;
    private double silver;
    private double gold;
    private double platinum;

    private List<Double> recordCrystal;

    private int bannerID;

    private int page;

    public BannerInfo(IChannel channel, String page)
    {
        CHANNEL = channel;

        /* READ Banners.xml */
        BannerParser bannersXML = new BannerParser();
        BANNERS = bannersXML.readConfig(Files.BANNER_XML.get());

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
        BannerParser bannersXML = new BannerParser();
        BANNERS = bannersXML.readConfig(Files.BANNER_XML.get());

        SettingsParser settings = new SettingsParser();

        copper = (int) (settings.getTwoRates() * 100);
        silver = (int) (settings.getThreeRates() * 100);
        gold = (int) (settings.getFourRates() * 100);
        platinum = (int) (settings.getFiveRates() * 100);
        recordCrystal = settings.getRecordCrystalRates();

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
            bannerList.put(Integer.parseInt(b.getBannerID()), b.getBannerName());
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

            /* SORT GOLD AND PLATINUM CHARACTERS AND STORE THEM IN THEIR OWN ARRAYS */
            for (Character character : banner.getCharacters())
            {
                if (Integer.parseInt(character.getRarity()) == 4)
                {
                    goldCharacters.add(character);
                }
                else if (Integer.parseInt(character.getRarity()) == 5)
                {
                    platinumCharacters.add(character);
                }
            }

            /* NO PLATINUM CHARACTER, ADJUST RATES */
            if (platinumCharacters.size() <= 0)
            {
                copper += platinum;
                platinum = 0;
            }

            String ratesList = "";
            if (platinum != 0)
                ratesList += "[5 ★] " + platinum + "%\n";
            ratesList += "[4 ★] " + gold + "%\n";
            ratesList += "[3 ★] " + silver + "%\n";
            ratesList += "[2 ★] " + copper + "%";

            menu.setRatesList(ratesList);

            /* BANNER IS STEP UP */
            if (banner.getBannerType().equals("1"))
            {
                double tC = copper - ((gold * 1.5) - gold);
                double tS = silver;
                double tG = gold * 1.5;
                double tP = platinum;

                String stepThreeRates = "";
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

                String stepFiveRates = "";
                if (tP != 0)
                    stepFiveRates += "[5 ★] " + tP + "%\n";
                stepFiveRates += "[4 ★] " + tG + "%\n";
                stepFiveRates += "[3 ★] " + tS + "%\n";
                stepFiveRates += "[2 ★] " + tC + "%\n";
                stepFiveRates += "**(4 ★ Scout Rates 2.0x)**";
                menu.setStepFiveRatesList(stepFiveRates);
            }
            /* BANNER IS STEP UP V2 */
            else if (banner.getBannerType().equals("3"))
            {
                double tC = copper - ((platinum * 1.5) - platinum);
                double tS = silver;
                double tG = gold;
                double tP = platinum * 1.5;

                String stepThreeRates = "";
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

                String stepFiveRates = "";
                if (tP != 0)
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

                String stepSixRates = "";
                if (tP != 0)
                    stepSixRates += "[5 ★] " + tP + "%\n";
                stepSixRates += "[4 ★] " + tG + "%\n";
                stepSixRates += "[3 ★] " + tS + "%\n";
                stepSixRates += "[2 ★] " + tC + "%\n";
                stepSixRates += "**(5 ★ Scout Rates 2.0x)**";
                menu.setStepSixRatesList(stepSixRates);
            }
            /* BANNER IS BIRTHDAY STEP UP */
            else if (banner.getBannerType().equals("4"))
            {
                double tC = copper - (((platinum * 2.0) - platinum) + ((gold * 2.0) - gold));
                double tS = silver;
                double tG = gold * 2.0;
                double tP = platinum * 2.0;

                String stepThreeRates = "";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(4+ ★ Scout Rates 2.0x)**";
                menu.setStepThreeRatesList(stepThreeRates);
            }
            /* BANNER IS RECORD CRYSTAL */
            else if (banner.getBannerType().equals("2") || banner.getBannerType().equals("5"))
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
            }
            /* BANNER IS STEP UP V3 */
            else if (banner.getBannerType().equals("7"))
            {
                double tC = copper - ((platinum * 2.0) - platinum);
                double tS = silver;
                double tG = gold;
                double tP = platinum * 2.0;

                String stepThreeRates = "";
                if (tP != 0)
                    stepThreeRates += "[5 ★] " + tP + "%\n";
                stepThreeRates += "[4 ★] " + tG + "%\n";
                stepThreeRates += "[3 ★] " + tS + "%\n";
                stepThreeRates += "[2 ★] " + tC + "%\n";
                stepThreeRates += "**(5 ★ Scout Rates 2.0x)**";
                menu.setStepThreeRatesList(stepThreeRates);
            }

            CHANNEL.sendMessage(menu.get().build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use '" + CommandManager.commandPrefix + "**scout**' for a list of banners.").get().build());
        }
    }
}
