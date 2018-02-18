package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reload
{
    private final String BANNERS_FILE_PATH = "data/Banners.xml";
    private final String SETTINGS_FILE_PATH = "data/Settings.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(Reload.class);

    public Reload()
    {
        new BannerParser().reloadBanners(BANNERS_FILE_PATH);
        new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);
        LOGGER.info("Files reloaded!");
    }
}
