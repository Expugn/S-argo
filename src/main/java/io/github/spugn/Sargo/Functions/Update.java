package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Weapon;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * UPDATE
 * <p>
 *     This class downloads required images from the GitHubDataRepository
 *     and saves them in their appropriate locations. There are three different
 *     modes: "Reset", "Overwrite", and "Normal". <br>
 *
 *     - "Reset" deletes every file in the Character/Weapon folders and
 *          re-downloads them. This is useful in situations where there may be
 *          image files with spelling errors or changed names. Using "Reset"
 *          will ensure those files will be deleted so those unused files
 *          will no longer take up space in the hard drive.<br>
 *
 *     - "Overwrite" writes over files even if they already exist. This is
 *          useful in situations where a file with the incorrect art is
 *          uploaded and downloaded and needs to be replaced.<br>
 *
 *     - "Normal" is what should be generally used. It will only download
 *          files that are missing.
 * </p>
 *
 * @author S'pugn
 * @version 2.0
 * @since v1.0
 */
public class Update
{
    private String gitHubDataRepository;
    private IChannel CHANNEL;
    private EmbedBuilder builder;
    private IMessage updateMessage;
    private static final Logger LOGGER = LoggerFactory.getLogger(Update.class);
    private List<String> allCharacters;
    private List<String> allWeapons;

    /**
     * Downloads required system images. If it fails, it will
     * shut down the bot.
     */
    public Update()
    {
        gitHubDataRepository = new SettingsParser().getGitHubRepoURL();
        try
        {
            int fileCounter, itemCounter;
            LOGGER.debug("Downloading System Images...");
            fileCounter = downloadSystemImages(false);
            LOGGER.debug(fileCounter + " files downloaded.");
            LOGGER.debug("Downloading Item Images...");
            itemCounter = downloadItemImages(false);
            LOGGER.debug(itemCounter + " files downloaded.");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception, Shutting down system.");
            System.exit(0);
        }
    }

    /**
     * Updates files under the given parameters.
     * Review {@link Update}'s JavaDoc for more information on
     * these update types.
     *
     * @param channel  Channel where the update progress message should be sent.
     * @param reset  If true, delete all files and redownload them.
     * @param overwrite  If true, download files regardless if they already exist.
     */
    public Update(IChannel channel, boolean reset, boolean overwrite)
    {
        CHANNEL = channel;
        builder = new EmbedBuilder();
        gitHubDataRepository = new SettingsParser().getGitHubRepoURL();
        allCharacters = new ArrayList<>();
        allWeapons = new ArrayList<>();

        try
        {
            if (reset)
            {
                int deletedFiles, copperChars, silverChars, copperWeaps, silverWeaps, charImages, weapImages;

                LOGGER.debug("Starting Update...");
                builder.withTitle("Update - Reset Mode");
                builder.withDesc("Updating Banners.xml");
                builder.appendField("Progress", "**Banners.xml**\n" +
                        "Delete All Character/Weapon Images\n" +
                        "Copper Characters\n" +
                        "Silver Characters\n" +
                        "Copper Weapons\n" +
                        "Silver Weapons\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage = CHANNEL.sendMessage(builder.build());
                builder.clearFields();

                LOGGER.debug("Updating Banners.xml File...");
                updateBanners();
                buildCharacterAndWeaponList();

                builder.withDesc("Erasing All Character/Weapon Files");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "**Delete All Character/Weapon Images**\n" +
                        "Copper Characters\n" +
                        "Silver Characters\n" +
                        "Copper Weapons\n" +
                        "Silver Weapons\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Deleting All Character and Weapon Images...");
                deletedFiles = resetImages();
                LOGGER.debug(deletedFiles + " files deleted.");

                builder.withDesc("Downloading Copper Characters");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "**Copper Characters**\n" +
                        "Silver Characters\n" +
                        "Copper Weapons\n" +
                        "Silver Weapons\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading COPPER CHARACTER images - Overwrite Mode: " + overwrite);
                copperChars = downloadCopperCharacters(overwrite);
                LOGGER.debug(copperChars + " files downloaded.");

                builder.withDesc("Downloading Silver Characters");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "**Silver Characters**\n" +
                        "Copper Weapons\n" +
                        "Silver Weapons\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading SILVER CHARACTER images - Overwrite Mode: " + overwrite);
                silverChars = downloadSilverCharacters(overwrite);
                LOGGER.debug(silverChars + " files downloaded.");

                builder.withDesc("Downloading Copper Weapons");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "~~Silver Characters~~ (" + silverChars + " Files)\n" +
                        "**Copper Weapons**\n" +
                        "Silver Weapons\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading COPPER WEAPON images - Overwrite Mode: " + overwrite);
                copperWeaps = downloadCopperWeapons(overwrite);
                LOGGER.debug(copperWeaps + " files downloaded.");

                builder.withDesc("Downloading Silver Weapons");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "~~Silver Characters~~ (" + silverChars + " Files)\n" +
                        "~~Copper Weapons~~ (" + copperWeaps + " Files)\n" +
                        "**Silver Weapons**\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading SILVER WEAPON images - Overwrite Mode: " + overwrite);
                silverWeaps = downloadSilverWeapons(overwrite);
                LOGGER.debug(silverWeaps + " files downloaded.");

                builder.withDesc("Updating Character Images");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "~~Silver Characters~~ (" + silverChars + " Files)\n" +
                        "~~Copper Weapons~~ (" + copperWeaps + " Files)\n" +
                        "~~Silver Weapons~~ (" + silverWeaps + " Files)\n" +
                        "**Character Images**\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading CHARACTER images - Overwrite Mode: " + overwrite);
                charImages = downloadCharacterImages(overwrite);
                LOGGER.debug(charImages + " files downloaded.");

                builder.withDesc("Updating Weapon Images");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "~~Silver Characters~~ (" + silverChars + " Files)\n" +
                        "~~Copper Weapons~~ (" + copperWeaps + " Files)\n" +
                        "~~Silver Weapons~~ (" + silverWeaps + " Files)\n" +
                        "~~Character Images~~ (" + charImages + " Files)\n" +
                        "**Weapon Images**\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();

                LOGGER.debug("Downloading WEAPON images - Overwrite Mode: " + overwrite);
                weapImages = downloadWeaponImages(overwrite);
                LOGGER.debug(weapImages + " files downloaded.");

                builder.withDesc("Update Complete! :tada:");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Delete All Character/Weapon Images~~ (" + deletedFiles + " Files)\n" +
                        "~~Copper Characters~~ (" + copperChars + " Files)\n" +
                        "~~Silver Characters~~ (" + silverChars + " Files)\n" +
                        "~~Copper Weapons~~ (" + copperWeaps + " Files)\n" +
                        "~~Silver Weapons~~ (" + silverWeaps + " Files)\n" +
                        "~~Character Images~~ (" + charImages + " Files)\n" +
                        "~~Weapon Images~~ (" + weapImages + " Files)\n", false);
                updateMessage.edit(builder.build());
            }
            else
            {
                int charImages, weapImages;
                LOGGER.debug("Starting Update...");
                if (overwrite)
                    builder.withTitle("Update - Overwrite Mode");
                else
                    builder.withTitle("Update");
                builder.withDesc("Updating Banners.xml");
                builder.appendField("Progress", "**Banners.xml**\n" +
                        "Character Images\n" +
                        "Weapon Images\n", false);
                updateMessage = CHANNEL.sendMessage(builder.build());
                builder.clearFields();
                if (!overwrite)
                    Thread.sleep(2000);

                LOGGER.debug("Updating Banners.xml File...");
                updateBanners();
                buildCharacterAndWeaponList();

                builder.withDesc("Updating Character Images");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "**Character Images**\n" +
                        "Weapon Images\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();
                if (!overwrite)
                    Thread.sleep(2000);

                LOGGER.debug("Downloading CHARACTER images - Overwrite Mode: " + overwrite);
                charImages = downloadCharacterImages(overwrite);
                LOGGER.debug(charImages + " files downloaded.");

                builder.withDesc("Updating Weapon Images");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Character Images~~ (" + charImages + " Files)\n" +
                        "**Weapon Images**\n", false);
                updateMessage.edit(builder.build());
                builder.clearFields();
                if (!overwrite)
                    Thread.sleep(2000);

                LOGGER.debug("Downloading WEAPON images - Overwrite Mode: " + overwrite);
                weapImages = downloadWeaponImages(overwrite);
                LOGGER.debug(weapImages + " files downloaded.");

                builder.withDesc("Update Complete! :tada:");
                builder.appendField("Progress", "~~Banners.xml~~\n" +
                        "~~Character Images~~ (" + charImages + " Files)\n" +
                        "~~Weapon Images~~ (" + weapImages + " Files)\n", false);
                updateMessage.edit(builder.build());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Update Failed!");

            builder.withDesc("Update Failed!");
            builder.withColor(255, 0, 0);
            updateMessage.edit(builder.build());
        }
        LOGGER.debug("Update Process Complete!");
    }

    /**
     * Downloads the Banners.xml file from the GitHubDataRepository.
     *
     * @throws IOException
     */
    private void updateBanners() throws IOException
    {
        boolean dataFolderExists = new File("data").exists();
        try
        {
            if (!dataFolderExists)
            {
                new File("data").mkdir();
            }

            URL website = new URL(gitHubDataRepository + "data/Banners.xml");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("data/Banners.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }
    }

    /**
     * Reads the Banners.xml file and creates a list of all characters
     * and weapons.
     */
    private void buildCharacterAndWeaponList()
    {
        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig("data/Banners.xml");

        for (Banner b : banners)
        {
            for (Character c : b.getCharacters())
            {
                if (!(allCharacters.contains(c.getPrefix() + " " + c.getName())))
                {
                    allCharacters.add(c.getPrefix() + " " + c.getName());
                }
            }

            for (Weapon w : b.getWeapons())
            {
                if (!(allWeapons.contains(w.getName())))
                {
                    allWeapons.add(w.getName());
                }
            }
        }
    }

    /**
     * Deletes all images in the Characters and Weapons folder
     *
     * @return  Number of images deleted.
     */
    private int resetImages()
    {
        File characterFolder = new File("images/Characters");
        File weaponFolder = new File("images/Weapons");
        int fileCounter = 0;

        if (characterFolder.exists())
        {
            String[] entries = characterFolder.list();
            for (String s : entries)
            {
                File currentFile = new File(characterFolder.getPath(), s);
                currentFile.delete();
                fileCounter++;
            }
        }

        if (weaponFolder.exists())
        {
            String[] entries = weaponFolder.list();
            for (String s : entries)
            {
                File currentFile = new File(weaponFolder.getPath(), s);
                currentFile.delete();
                fileCounter++;
            }
        }

        return fileCounter;
    }

    /**
     * Downloads character images from the GitHubDataRepository using
     * the constructed list in {@link Update#buildCharacterAndWeaponList()}.
     *
     * @param overwrite  If true, images will be downloaded regardless if they already exist.
     * @return  Number of files downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadCharacterImages(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean characterFolderExists = new File("images/Characters").exists();
        int fileCounter = 0;

        try
        {
            for (String cS : allCharacters)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!characterFolderExists)
                {
                    new File("images/Characters").mkdir();
                }

                if (overwrite || !(new File("images/Characters/" + cS.replaceAll(" ", "_") + ".png").exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Characters/" + cS.replaceAll(" ", "_") + ".png";
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Characters/" + cS.replaceAll(" ", "_") + ".png");
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Downloads weapon images from the GitHubDataRepository using
     * the constructed list in {@link Update#buildCharacterAndWeaponList()}.
     *
     * @param overwrite  If true, images will be downloaded regardless if they already exist.
     * @return  Number of images downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadWeaponImages(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean weaponFolderExists = new File("images/Weapons").exists();
        int fileCounter = 0;

        try
        {
            for (String wS : allWeapons)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!weaponFolderExists)
                {
                    new File("images/Weapons").mkdir();
                }

                if (overwrite || !(new File("images/Weapons/" + wS.replaceAll(" ", "_") + ".png").exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Weapons/" + wS.replaceAll(" ", "_") + ".png";
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Weapons/" + wS.replaceAll(" ", "_") + ".png");
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Downloads all required System images that are used.
     *
     * @param overwrite  If true, download images regardless if they already exist.
     * @return  Number of images downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadSystemImages(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean systemFolderExists = new File("images/System").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Character_Placeholder.png");
        fileNames.add("Character_Shade.png");
        fileNames.add("Copper_Border.png");
        fileNames.add("Empty_Rarity_Star.png");
        fileNames.add("Gold_Border.png");
        fileNames.add("Gray_Border.png");
        fileNames.add("Item_Placeholder.png");
        fileNames.add("Multi_Character.png");
        fileNames.add("Multi_Weapon.png");
        fileNames.add("Platinum_Border.png");
        fileNames.add("Rarity_Star.png");
        fileNames.add("Silver_Border.png");
        fileNames.add("Single_Character.png");
        fileNames.add("Single_Weapon.png");
        fileNames.add("Weapon_Placeholder.png");
        fileNames.add("White_Border.png");

        try
        {
            for (String iS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!systemFolderExists)
                {
                    new File("images/System").mkdir();
                }

                if (overwrite || !(new File("images/System/" + iS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/System/" + iS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/System/" + iS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Downloads all copper character images.
     *
     * @param overwrite  If true, download the image regardless if it already exists.
     * @return  Number of images downloaded
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadCopperCharacters(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean characterFolderExists = new File("images/Characters").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Kirito.png");
        fileNames.add("Klein.png");
        fileNames.add("Diavel.png");
        fileNames.add("Corvatz.png");
        fileNames.add("Thinker.png");
        fileNames.add("Sigurd.png");
        fileNames.add("Strea.png");
        fileNames.add("Sachi.png");
        fileNames.add("Asuna.png");
        fileNames.add("Yolko.png");
        fileNames.add("Yulier.png");
        fileNames.add("Silica.png");
        fileNames.add("Philia.png");
        fileNames.add("Argo.png");
        fileNames.add("Sasha.png");
        fileNames.add("Agil.png");
        fileNames.add("Kibaou.png");
        fileNames.add("Kagemune.png");
        fileNames.add("Lisbeth.png");
        fileNames.add("Rosalia.png");
        fileNames.add("Sinon.png");

        try
        {
            for (String cS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!characterFolderExists)
                {
                    new File("images/Characters").mkdir();
                }

                if (overwrite || !(new File("images/Characters/" + cS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Characters/" + cS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Characters/" + cS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Downloads all silver character images.
     *
     * @param overwrite  If true, download images regardless if they already exist.
     * @return  Number of images downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadSilverCharacters(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean characterFolderExists = new File("images/Characters").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Spriggan_Kirito.png");
        fileNames.add("Solo_Kirito.png");
        fileNames.add("Katana_Master_Klein.png");
        fileNames.add("Salamander_Samurai_Klein.png");
        fileNames.add("_Sumeragi.png");
        fileNames.add("Guild_Commander_Heathcliff.png");
        fileNames.add("_Red-Eyed_XaXa.png");
        fileNames.add("_Kuradeel.png");
        fileNames.add("_Eugene.png");
        fileNames.add("Uninhibited_Fairy_Strea.png");
        fileNames.add("Sword_&_Shield_Sachi.png");
        fileNames.add("Floor-Clearing_Demon_Asuna.png");
        fileNames.add("Battle_Smith_Lisbeth.png");
        fileNames.add("Item_Master_Rain.png");
        fileNames.add("_Lux.png");
        fileNames.add("Party_Mascot_Silica.png");
        fileNames.add("Information_Broker_Argo.png");
        fileNames.add("Cat_Sith_Info_Broker_Argo.png");
        fileNames.add("Stalwart_Tank_Agil.png");
        fileNames.add("Mysterious_Warrior_Yuuki.png");
        fileNames.add("Apprentice_Blacksmith_Lisbeth.png");
        fileNames.add("World_of_Guns_Sinon.png");
        fileNames.add("Cat-Eared_Archer_Sinon.png");
        fileNames.add("Agile_Kitty_Silica.png");
        fileNames.add("Spriggan_Seeker_Philia.png");
        fileNames.add("_Sakuya.png");
        fileNames.add("Wind_Mage_Leafa.png");
        fileNames.add("The_Young_Pooka_Seven.png");
        fileNames.add("_Alicia_Rue.png");
        fileNames.add("_Siune.png");

        try
        {
            for (String cS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!characterFolderExists)
                {
                    new File("images/Characters").mkdir();
                }

                if (overwrite || !(new File("images/Characters/" + cS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Characters/" + cS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Characters/" + cS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Download all copper weapon images.
     *
     * @param overwrite  If true, download images regardless if they already exist.
     * @return  Number of images downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadCopperWeapons(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean weaponFolderExists = new File("images/Weapons").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Gust_Katana.png");
        fileNames.add("Brave_Gemsword.png");
        fileNames.add("Queen's_Knightsword.png");
        fileNames.add("Flame_Knife.png");
        fileNames.add("Assault_Dagger.png");
        fileNames.add("Wind_Dagger.png");
        fileNames.add("Mithril_Spear.png");
        fileNames.add("Darkness_Glaive.png");
        fileNames.add("Mithril_Rapier.png");
        fileNames.add("Flame_Rapier.png");
        fileNames.add("Q._Knightsword_x_B._Gemsword.png");
        fileNames.add("Mithril_Mace.png");
        fileNames.add("War_Pick.png");
        fileNames.add("Aqua_Mace.png");
        fileNames.add("Ethereal_Staff.png");
        fileNames.add("Evil_Wand.png");
        fileNames.add("Flame_Wand.png");
        fileNames.add("Aqua_Spread.png");
        fileNames.add("Dark_Bow.png");
        fileNames.add("Flame_Shooter.png");
        fileNames.add("Stealth_Rifle.png");
        fileNames.add("Double-Stack_Magazine.png");
        fileNames.add("Precision_Rifle.png");

        try
        {
            for (String wS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!weaponFolderExists)
                {
                    new File("images/Weapons").mkdir();
                }

                if (overwrite || !(new File("images/Weapons/" + wS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Weapons/" + wS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Weapons/" + wS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    /**
     * Download all silver weapon images.
     *
     * @param overwrite  If true, download images regardless if they already exist.
     * @return  Number of images downloaded.
     * @throws IOException  If file is not found in repository OR if file cannot be saved.
     */
    private int downloadSilverWeapons(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean weaponFolderExists = new File("images/Weapons").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Moonlit_Sword.png");
        fileNames.add("Ice_Blade.png");
        fileNames.add("Carmine_Katana.png");
        fileNames.add("Artisan's_Longsword.png");
        fileNames.add("Misericorde.png");
        fileNames.add("Midnight_Kris.png");
        fileNames.add("Petra_Twins.png");
        fileNames.add("Gale_Halbert.png");
        fileNames.add("Chivalrous_Rapier.png");
        fileNames.add("Earth_Dragon_Stinger.png");
        fileNames.add("Brave_Rapier.png");
        fileNames.add("Dual_Artisan's_Swords.png");
        fileNames.add("Bloody_Club.png");
        fileNames.add("Barbarian_Mace.png");
        fileNames.add("Gale_Zaghnal.png");
        fileNames.add("Tidal_Rod.png");
        fileNames.add("Crystal_Wand.png");
        fileNames.add("Emerald_Rod.png");
        fileNames.add("Chivalrous_Bow.png");
        fileNames.add("Petra_Bow.png");
        fileNames.add("Falcon_Shooter.png");
        fileNames.add("Pale_Shooter.png");
        fileNames.add("Behemoth_Sniper.png");
        fileNames.add("Long-Range_Barrett.png");
        fileNames.add("Moonlight_Kukri.png");

        try
        {
            for (String wS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!weaponFolderExists)
                {
                    new File("images/Weapons").mkdir();
                }

                if (overwrite || !(new File("images/Weapons/" + wS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Weapons/" + wS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Weapons/" + wS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }

    private int downloadItemImages(boolean overwrite) throws IOException
    {
        boolean imageFolderExists = new File("images").exists();
        boolean itemFolderExists = new File("images/Items").exists();
        int fileCounter = 0;

        List<String> fileNames = new ArrayList<>();
        fileNames.add("Attack_Crystal.png");
        fileNames.add("Attack_Shard.png");
        fileNames.add("Col.png");
        fileNames.add("Critical_Crystal.png");
        fileNames.add("Critical_Shard.png");
        fileNames.add("Dark_Medallion.png");
        fileNames.add("Defense_Crystal.png");
        fileNames.add("Defense_Shard.png");
        fileNames.add("Earth_Medallion.png");
        fileNames.add("Exchange_Sword.png");
        fileNames.add("EXP_Hi-Potion.png");
        fileNames.add("EXP_Potion.png");
        fileNames.add("EXP_X-Potion.png");
        fileNames.add("Fire_Medallion.png");
        fileNames.add("Holy_Attack_Crystal.png");
        fileNames.add("Holy_Critical_Crystal.png");
        fileNames.add("Holy_Defense_Crystal.png");
        fileNames.add("Holy_HP_Crystal.png");
        fileNames.add("Holy_Medallion.png");
        fileNames.add("Holy_MP_Crystal.png");
        fileNames.add("Holy_Skill_Crystal.png");
        fileNames.add("HP_Crystal.png");
        fileNames.add("HP_Shard.png");
        fileNames.add("Lv._80_Decryption_Key.png");
        fileNames.add("MP_Crystal.png");
        fileNames.add("MP_Shard.png");
        fileNames.add("Rainbow_Essence.png");
        fileNames.add("Skill_Crystal.png");
        fileNames.add("Skill_Shard.png");
        fileNames.add("Stardust_(Medium).png");
        fileNames.add("Void_Medallion.png");
        fileNames.add("Water_Medallion.png");
        fileNames.add("Wind_Medallion.png");

        try
        {
            for (String iS : fileNames)
            {
                /* CREATE DIRECTORIES IF MISSING */
                if (!imageFolderExists)
                {
                    new File("images").mkdir();
                }
                if (!itemFolderExists)
                {
                    new File("images/Items").mkdir();
                }

                if (overwrite || !(new File("images/Items/" + iS).exists()))
                {
                    /* GET IMAGE URL */
                    String url = gitHubDataRepository + "images/Items/" + iS;
                    URL website = new URL(url);

                    LOGGER.debug("Downloading \"" + url + "\"");
                    /* DOWNLOAD IMAGE */
                    BufferedImage image = ImageIO.read(website);
                    File file = new File("images/Items/" + iS);
                    ImageIO.write(image, "png", file);
                    fileCounter++;
                }
            }
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException();
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        return fileCounter;
    }
}
