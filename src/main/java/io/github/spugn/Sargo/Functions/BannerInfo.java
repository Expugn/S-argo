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
    private final String GITHUB_IMAGE = "https://raw.githubusercontent.com/Expugn/S-argo/master/";

    private int copper;
    private int silver;
    private int gold;
    private int platinum;

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
        /* TODO - IMPLEMENT PAGINATE */

        /* TODO - REPLACE THIS WITH GIVEN PAGE NUMBER */
        //int page = 1;


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

        //paginate(bannerList, page, 10);

        /*
        String s = "";
        for(Banner banner : BANNERS)
        {
            s += "\n" + banner.getBannerID() + ") **" + banner.getBannerName() + "**";
        }

        BannerListMenu menu = new BannerListMenu();
        menu.setBannerCount(BANNERS.size());
        menu.setBannerList(s);

        */
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

            /* FIXME - IMAGE URL DEBUG MESSAGE */
            //System.out.println(new GitHubImage(banner.getCharacters().get(charIndex).getImagePath()).getURL());

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
            ratesList += "[2 ★] " + copper + "%\n";

            menu.setRatesList(ratesList);

            CHANNEL.sendMessage(menu.get().build());
        }
        else
        {
            new Message(CLIENT, CHANNEL, Text.SCOUT_UNKNOWN_BANNER.get(), true, 255, 0, 0);
        }
    }
}
