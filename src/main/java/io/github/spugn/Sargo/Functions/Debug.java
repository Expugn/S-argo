package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import io.github.spugn.sdevkit.Discord.Discord4J.Message;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class Debug
{
    private static IDiscordClient CLIENT;
    private static IChannel CHANNEL;

    public Debug(IDiscordClient client, IChannel channel)
    {
        CLIENT = client;
        CHANNEL = channel;

        run();
    }

    private void run()
    {
        //new CopperCharacter().systemReadImagePaths();
        //new SilverCharacter().systemReadImagePaths();
        //testRates(11);
        //testScoutMenu();
    }

    private void testRates(int amount)
    {
        double d;
        String s = "";
        SettingsParser settings = new SettingsParser();
        Random rng = new Random(System.currentTimeMillis());
        DecimalFormat numberFormat = new DecimalFormat("0.000");

        s += "2STAR: < " + (settings.getTwoRates()) + "\n";
        s += "3STAR: < " + (settings.getTwoRates() + settings.getThreeRates()) + "\n";
        s += "4STAR: < " + (settings.getTwoRates() + settings.getThreeRates() + settings.getFourRates()) + "\n";
        s += "5STAR: < " + (settings.getTwoRates() + settings.getThreeRates() + settings.getFourRates() + settings.getFiveRates()) + "\n\n";

        for (int i = 0 ; i < amount ; i++)
        {
            d = rng.nextDouble();

            if (d < settings.getTwoRates()) /* 2 STAR CHARACTER */
            {
                s += numberFormat.format(d) + " | " + 2 + "\n";
            }
            else if (d < settings.getTwoRates() + settings.getThreeRates()) /* THREE STAR CHARACTER */
            {
                s += numberFormat.format(d) + " | " + 3 + "\n";
            }
            else if (d < settings.getTwoRates() + settings.getThreeRates() + settings.getFourRates()) /* FOUR STAR CHARACTER */
            {
                s += numberFormat.format(d) + " | " + 4 + "\n";
            }
            else /* FIVE STAR CHARACTER */
            {
                s += numberFormat.format(d) + " | " + 5 + "\n";
            }
        }
        s += "==================";
        new Message(CLIENT, CHANNEL, s);
    }

    private void testParse()
    {
        BannerParser read = new BannerParser();
        List<Banner> readConfig = read.readConfig("data/Banners.xml");

        for(Banner banner : readConfig)
        {
            new Message(CLIENT, CHANNEL, banner.toString());
        }

        SettingsParser settings = new SettingsParser();
        System.out.println(settings.getBotToken());
        System.out.println(settings.getCommandPrefix());
        System.out.println(settings.getTwoRates());
        System.out.println(settings.getThreeRates());
        System.out.println(settings.getFourRates());
        System.out.println(settings.getFiveRates());
        System.out.println(settings.isDeleteUserMessage());
        System.out.println(settings.isUseMention());

    }

    private void testScoutMenu()
    {
        ScoutMenu scoutMenu = new ScoutMenu();

        scoutMenu.setArgoText(Text.ARGO_3.get());
        scoutMenu.setThumbnail(Images.ARGO_SMUG.getUrl());
        //scoutMenu.setBannerName(Text.START_DASHV2.get());
        scoutMenu.setPullType(Text.MULTI_PULL.get());

        CHANNEL.sendMessage(scoutMenu.get().build());

        try
        {
            /* GENERATE TEST IMAGE ARRAY */
            //File black_swordsman = new File("images/Characters/Start Dash/Black_Swordsman.png");
            File platinum_placeholder = new File("images/Characters/Placeholders/Platinum.png");
            String imageStrings[] = new String[11];
            for (int i = 0 ; i < 11 ; i++)
            {
                //imageStrings[i] = black_swordsman.toString();
                imageStrings[i] = platinum_placeholder.toString();
            }

            Scout test = new Scout(CLIENT, CHANNEL, 1, "1");

            test.drawImage(imageStrings[0]);
            CHANNEL.sendFile(new File("images/result.png"));

            test.drawImage(imageStrings);
            CHANNEL.sendFile(new File("images/result.png"));

            //test.deleteLatestResult();
        }
        catch (IOException e)
        {
            System.out.println("IOException");
        }
    }
}