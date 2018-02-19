package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reload
{
    private final static String BANNERS_FILE_PATH = "data/Banners.xml";
    private final static String SETTINGS_FILE_PATH = "data/Settings.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(Reload.class);

    public Reload()
    {
        new BannerParser().reloadBanners(BANNERS_FILE_PATH);
        new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
    }

    public static void reloadBanners()
    {
        LOGGER.debug("Reloading Banners.xml...");
        new BannerParser().reloadBanners(BANNERS_FILE_PATH);
        LOGGER.debug(BannerParser.getBanners().size() + " banners loaded.");
    }

    public static void reloadSettings()
    {
        LOGGER.debug("Reloading Settings.xml...");
        new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
        LOGGER.debug("Done!");
    }

    public static void silentReloadSettings()
    {
        new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
    }
}
