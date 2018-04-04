package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.XMLParsers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Reload
{
    private final static String BANNERS_FILE_PATH = "data/Banners.xml";
    private final static String SETTINGS_FILE_PATH = "data/Settings.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(Reload.class);

    public Reload()
    {
        new BannerParser().reloadBanners(BANNERS_FILE_PATH);
        //new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
        new LoginSettingsParser().reload();
        new CommandSettingsParser().reload();
        new ScoutSettingsParser().reload();
        new ShopSettingsParser().reload();
    }

    public static void reloadBanners()
    {
        LOGGER.debug("Reloading Banners.xml...");
        new BannerParser().reloadBanners(BANNERS_FILE_PATH);
        LOGGER.debug(BannerParser.getBanners().size() + " banners loaded.");
    }

    public static void reloadSettings()
    {
        LOGGER.debug("Reloading Settings...");
        //new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
        new LoginSettingsParser().reload();
        new CommandSettingsParser().reload();
        new ScoutSettingsParser().reload();
        new ShopSettingsParser().reload();
        LOGGER.debug("Done!");
    }

    public static void silentReloadSettings()
    {
        boolean settingsFolderExists = new File("data/settings").exists();
        if (!settingsFolderExists)
        {
            new File("data/settings").mkdir();
        }

        File loginFile = new File("data/settings/Login.xml");
        if (!(loginFile.exists()))
        {
            new LoginSettingsParser().newFile();
            LOGGER.error("\nLogin.xml is not found!\nCreated a new file in /data/settings/; please fill it out before continuing.");
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                System.exit(0);
            }
            System.exit(0);
        }

        new LoginSettingsParser().reload();
        new CommandSettingsParser().reload();
        new ScoutSettingsParser().reload();
        new ShopSettingsParser().reload();
        //new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
    }
}
