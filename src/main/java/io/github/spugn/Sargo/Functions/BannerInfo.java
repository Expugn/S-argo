package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Discord.Discord4J.Message;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

import java.util.*;

public class BannerInfo
{
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;
    private static List<Banner> BANNERS;

    private double copper;
    private double silver;
    private double gold;
    private double platinum;

    private int bannerID;

    private int page;

    public BannerInfo(IDiscordClient client, IChannel channel, String page)
    {
        CLIENT = client;
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

    public BannerInfo(IDiscordClient client, IChannel channel, int bannerID)
    {
        CLIENT = client;
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
        if (bannerID < BANNERS.size())
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
                stepThreeRates += "[2 ★] " + tC + "%";
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
                stepFiveRates += "[2 ★] " + tC + "%";
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
                stepThreeRates += "[2 ★] " + tC + "%";
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
                stepFiveRates += "(For One Character)";
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
                stepSixRates += "[2 ★] " + tC + "%";
                menu.setStepSixRatesList(stepSixRates);
            }

            CHANNEL.sendMessage(menu.get().build());
        }
        else
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_UNKNOWN_BANNER.get(), true, 255, 0, 0);
        }
    }
}
