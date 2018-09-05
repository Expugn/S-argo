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
        new LoginSettingsParser().reload();
        new CommandSettingsParser().reload();
        new ScoutSettingsParser().reload();
        new ShopSettingsParser().reload();
        LOGGER.debug("Done!");
    }

    public static void silentReloadSettings()
    {
        if (!importSettings())
        {
            boolean dataFolderExists = new File("data").exists();
            if (!dataFolderExists)
            {
                new File("data").mkdir();
            }

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
        }
    }

    public static boolean importSettings()
    {
        boolean settingsFileExists = new File(SETTINGS_FILE_PATH).exists();
        boolean settingsFolderExists = new File("data/settings").exists();

        // CHECK IF SETTINGS FOLDER IS MISSING AND IF SETTINGS.XML EXISTS
        if (!settingsFolderExists && settingsFileExists)
        {
            // SETTINGS FOLDER DOESN'T EXIST BUT SETTINGS FILE EXISTS, IMPORT OLD SETTINGS.XML TO NEW SYSTEM
            LOGGER.info("S'argo v2.8+ core setting files not detected, but found S'argo v2.7- Settings.xml...");
            LOGGER.info("Starting setting import!");

            // CREATE FOLDERS
            LOGGER.info("\tCreating /data/settings/...");
            if (!(new File("data").exists()))
            {
                new File("data").mkdir();
            }

            if (!(new File("data/settings").exists()))
            {
                new File("data/settings").mkdir();
            }

            // LOAD OLD SETTINGS FILE
            LOGGER.info("\tLoading Settings.xml...");
            new SettingsParser().reloadSettings(SETTINGS_FILE_PATH);

            // CREATE/LOAD NEW SETTING FILES
            LOGGER.info("\tCreating/Loading S'argo v2.8 core setting files...");
            new LoginSettingsParser().newFile();
            new LoginSettingsParser().reload();
            new CommandSettingsParser().reload();
            new ScoutSettingsParser().reload();
            new ShopSettingsParser().reload();


            // IMPORT SETTINGS
            LOGGER.info("\tStart settings import!");

            LOGGER.info("\t\t/data/settings/Login.xml");
            LOGGER.info("\t\t\tImporting Bot Token...");
            LoginSettingsParser.setBotToken(SettingsParser.getBotToken());
            LOGGER.info("\t\t\tImporting Bot Owner Discord ID...");
            LoginSettingsParser.setBotOwnerDiscordID(SettingsParser.getBotOwnerDiscordID());
            LOGGER.info("\t\t\tImporting GitHub Data Repository URL...");
            LoginSettingsParser.setGithubDataRepository(SettingsParser.getGitHubRepoURL());
            LOGGER.info("\t\t\tSaving /data/settings/Login.xml and reloading...");
            LoginSettingsParser lsp = new LoginSettingsParser();
            lsp.saveFile();
            lsp.reload();

            LOGGER.info("\t\t/data/settings/Command.xml");
            LOGGER.info("\t\t\tImporting Command Prefix...");
            CommandSettingsParser.setCommandPrefix(SettingsParser.getCommandPrefix());
            LOGGER.info("\t\t\tImporting Delete User Message...");
            CommandSettingsParser.setDeleteUserMessage(SettingsParser.isDeleteUserMessage());
            LOGGER.info("\t\t\tImporting Ignored Channels/Blacklisted Channels...");
            CommandSettingsParser.setBlacklistedChannels(SettingsParser.getIgnoredChannelNames());
            LOGGER.info("\t\t\tSaving /data/settings/Command.xml and reloading...");
            CommandSettingsParser csp = new CommandSettingsParser();
            csp.saveFile();
            csp.reload();

            LOGGER.info("\t\t/data/settings/Scout.xml");
            LOGGER.info("\t\t\tImporting Disable Images...");
            ScoutSettingsParser.setDisableImages(SettingsParser.isDisableImages());
            LOGGER.info("\t\t\tImporting Simple Message...");
            ScoutSettingsParser.setSimpleMessage(SettingsParser.isSimpleMessage());
            LOGGER.info("\t\t\tImporting Scout Master...");
            ScoutSettingsParser.setScoutMaster(SettingsParser.getScoutMaster());
            LOGGER.info("\t\t\tImporting Copper Rate...");
            ScoutSettingsParser.setCopperRate(SettingsParser.getCopperRates());
            LOGGER.info("\t\t\tImporting Silver Rate...");
            ScoutSettingsParser.setSilverRate(SettingsParser.getSilverRates());
            LOGGER.info("\t\t\tImporting Gold Rate...");
            ScoutSettingsParser.setGoldRate(SettingsParser.getGoldRates());
            LOGGER.info("\t\t\tImporting Platinum Rate...");
            ScoutSettingsParser.setPlatinumRate(SettingsParser.getPlatinumRates());
            LOGGER.info("\t\t\tImporting Record Crystal Rates...");
            ScoutSettingsParser.setRecordCrystalRates(SettingsParser.getRecordCrystalRates());
            LOGGER.info("\t\t\tImporting Circulating Record Crystal Rates...");
            ScoutSettingsParser.setCirculatingRecordCrystalRates(SettingsParser.getCirculatingRecordCrystalRates());
            LOGGER.info("\t\t\tSaving /data/settings/Scout.xml and reloading...");
            ScoutSettingsParser scsp = new ScoutSettingsParser();
            scsp.saveFile();
            scsp.reload();

            LOGGER.info("\t\t/data/settings/Shop.xml");
            LOGGER.info("\t\t\tImporting Max Shop Limit...");
            ShopSettingsParser.setMaxShopLimit(SettingsParser.getMaxShopLimit());
            LOGGER.info("\t\t\tImporting Shop Items...");
            ShopSettingsParser.setShopItems(SettingsParser.getShopItems());
            LOGGER.info("\t\t\tSaving /data/settings/Shop.xml and reloading...");
            ShopSettingsParser shsp = new ShopSettingsParser();
            shsp.saveFile();
            shsp.reload();

            LOGGER.info("\t\tDeleting Settings.xml...");
            new File(SETTINGS_FILE_PATH).delete();

            LOGGER.info("\tSettings import complete!");
            return true;
        }
        return false;
    }
}
