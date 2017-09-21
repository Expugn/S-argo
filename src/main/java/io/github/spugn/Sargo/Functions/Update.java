package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.*;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class Update
{
    private String gitHubDataRepository;
    private IChannel CHANNEL;

    public Update(IChannel channel, String dataName)
    {
        CHANNEL = channel;
        gitHubDataRepository = new SettingsParser().getGitHubRepoURL();

        if (dataName.equalsIgnoreCase("banners"))
        {
            updateBanners();
        }
        else if (dataName.equalsIgnoreCase("settings"))
        {
            updateSettings();
        }
        else
        {
            channel.sendMessage(new WarningMessage("INCORRECT UPDATE TYPE", "Update Types: **banners**, **settings**, **images system**, **images [Banner ID]**").get().build());
        }
    }

    public Update(IChannel channel, String dataName, String bannerID_String)
    {
        CHANNEL = channel;
        gitHubDataRepository = new SettingsParser().getGitHubRepoURL();

        if (dataName.equalsIgnoreCase("images"))
        {
            try
            {
                updateImages((Integer.parseInt(bannerID_String) - 1));
            }
            catch (NumberFormatException e)
            {
                if (bannerID_String.equalsIgnoreCase("system"))
                {
                    updateSystem();
                }
            }
        }
        else
        {
            channel.sendMessage(new WarningMessage("INCORRECT UPDATE TYPE", "Update Types: **banners**, **settings**, **images system**, **images [Banner ID]**").get().build());
        }
    }

    private void updateBanners()
    {
        boolean dataFolderExists = new File("data").exists();
        try
        {
            if (!dataFolderExists)
            {
                new File("data").mkdir();
            }

            CHANNEL.sendMessage(new WarningMessage("UPDATING BANNERS", "Fetching [Banners.xml](" + gitHubDataRepository + "data/Banners.xml)").get().build());
            URL website = new URL(gitHubDataRepository + "data/Banners.xml");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("data/Banners.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch (MalformedURLException e)
        {
            CHANNEL.sendMessage(new WarningMessage("[MalformedURLException] Whoops. Something went wrong.", "Are you sure the Banners.xml you're trying to load are in the repository?").get().build());
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("[IOException] Whoops. Something went wrong.", "Are you sure the directory you're trying to save files to exists?").get().build());
        }
    }

    private void updateSettings()
    {
        boolean dataFolderExists = new File("data").exists();
        try
        {
            if (!dataFolderExists)
            {
                new File("data").mkdir();
            }

            CHANNEL.sendMessage(new WarningMessage("UPDATING SETTINGS", "Fetching [new_Settings.xml](" + gitHubDataRepository + "data/new_Settings.xml)").get().build());
            URL website = new URL(gitHubDataRepository + "data/new_Settings.xml");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("data/new_Settings.xml");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        catch (MalformedURLException e)
        {
            CHANNEL.sendMessage(new WarningMessage("[MalformedURLException] Whoops. Something went wrong.", "Are you sure the new_Settings.xml you're trying to load are in the repository?").get().build());
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("[IOException] Whoops. Something went wrong.", "Are you sure the directory you're trying to save files to exists?").get().build());
        }
    }

    private void updateImages(int bannerID)
    {
        BannerParser bannersXML = new BannerParser();
        List<Banner> banners = bannersXML.readConfig(Files.BANNER_XML.get());

        if (bannerID < banners.size() && bannerID >= 0)
        {
            Banner selectedBanner = banners.get(bannerID);
            List<Character> bannerCharacters = selectedBanner.getCharacters();
            List<Weapon> bannerWeapons = selectedBanner.getWeapons();
            EmbedBuilder builder = new EmbedBuilder();
            EmbedBuilder builder2 = new EmbedBuilder();

            boolean imageFolderExists = new File("images").exists();
            boolean characterFolderExists = new File("images/Characters").exists();
            boolean weaponFolderExists = new File("images/Weapons").exists();
            boolean bannerFolderExists;

            try
            {
                for (Character c : bannerCharacters)
                {
                    bannerFolderExists = new File("images/Characters/" + selectedBanner.getBannerName()).exists();

                    if (!imageFolderExists)
                    {
                        new File("images").mkdir();
                    }

                    if (!characterFolderExists)
                    {
                        new File ("images/Characters").mkdir();
                    }

                    if (!bannerFolderExists)
                    {
                        new File("images/Characters/" + selectedBanner.getBannerName()).mkdir();
                    }

                    String url = gitHubDataRepository + c.getImagePath();
                    url = url.replaceAll(" ", "%20");
                    url = url.replaceAll("★", "%E2%98%85");

                    URL website = new URL(url);

                    BufferedImage image = ImageIO.read(website);
                    File file = new File(c.getImagePath());
                    ImageIO.write(image, "png", file);

                    builder.appendField(c.toString(), "[Image Link](" + url + ")", false);
                }

                for (Weapon w : bannerWeapons)
                {
                    bannerFolderExists = new File("images/Weapons/" + selectedBanner.getBannerName()).exists();

                    if (!imageFolderExists)
                    {
                        new File("images").mkdir();
                    }

                    if (!weaponFolderExists)
                    {
                        new File ("images/Weapons").mkdir();
                    }

                    if (!bannerFolderExists)
                    {
                        new File("images/Weapons/" + selectedBanner.getBannerName()).mkdir();
                    }

                    String url = gitHubDataRepository + w.getImagePath();
                    url = url.replaceAll(" ", "%20");
                    url = url.replaceAll("★", "%E2%98%85");

                    URL website = new URL(url);

                    BufferedImage image = ImageIO.read(website);
                    File file = new File(w.getImagePath());
                    ImageIO.write(image, "png", file);

                    builder2.appendField(w.toString(), "[Image Link](" + url + ")", false);
                }
            }
            catch (MalformedURLException e)
            {
                CHANNEL.sendMessage(new WarningMessage("[MalformedURLException] Whoops. Something went wrong.", "Are you sure the images you're trying to load are in the repository?").get().build());
                return;
            }
            catch (IOException e)
            {
                CHANNEL.sendMessage(new WarningMessage("[IOException] Whoops. Something went wrong.", "Are you sure the directory you're trying to save files to exists?").get().build());
                return;
            }

            builder.withAuthorName("Updated the following images:");
            builder2.withAuthorName("Updated the following images:");
            CHANNEL.sendMessage(builder.build());
            CHANNEL.sendMessage(builder2.build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
        }
    }

    private void updateSystem()
    {
        boolean imageFolderExists = new File("images").exists();
        boolean scoutBGFolderExists = new File("images/Scout Backgrounds").exists();
        boolean miscFolderExists = new File("images/Miscellaneous").exists();

        if (!imageFolderExists)
        {
            new File("images").mkdir();
        }

        if (!scoutBGFolderExists)
        {
            new File("images/Scout Backgrounds").mkdir();
        }

        if (!miscFolderExists)
        {
            new File("images/Miscellaneous").mkdir();
        }

        try
        {
            URL website;
            File file;
            BufferedImage image;

            String fp_OCS = "images/Miscellaneous/Owned_Character_Shade.png";
            String fp_SB_S = "images/Scout Backgrounds/Single.png";
            String fp_SB_M = "images/Scout Backgrounds/Multi.png";
            String fp_SB_SW = "images/Scout Backgrounds/Single_Weapon.png";
            String fp_SB_MW = "images/Scout Backgrounds/Multi_Weapon.png";

            /* Owned_Character_Shade.png */
            website = new URL(gitHubDataRepository + fp_OCS);
            image = ImageIO.read(website);
            file = new File(fp_OCS);
            ImageIO.write(image, "png", file);

            /* Single.png */
            website = new URL(gitHubDataRepository + fp_SB_S.replaceAll(" ", "%20"));
            image = ImageIO.read(website);
            file = new File(fp_SB_S);
            ImageIO.write(image, "png", file);

            /* Multi.png */
            website = new URL(gitHubDataRepository + fp_SB_M.replaceAll(" ", "%20"));
            image = ImageIO.read(website);
            file = new File(fp_SB_M);
            ImageIO.write(image, "png", file);

            /* Single_Weapon.png */
            website = new URL(gitHubDataRepository + fp_SB_SW.replaceAll(" ", "%20"));
            image = ImageIO.read(website);
            file = new File(fp_SB_SW);
            ImageIO.write(image, "png", file);

            /* Multi_Weapon.png */
            website = new URL(gitHubDataRepository + fp_SB_MW.replaceAll(" ", "%20"));
            image = ImageIO.read(website);
            file = new File(fp_SB_MW);
            ImageIO.write(image, "png", file);

            String filesList = "";
            filesList += "[Owned_Character_Shade.png](" + gitHubDataRepository + fp_OCS + ")\n";
            filesList += "[Single.png](" + (gitHubDataRepository + fp_SB_S).replaceAll(" ", "%20") + ")\n";
            filesList += "[Multi.png](" + (gitHubDataRepository + fp_SB_M).replaceAll(" ", "%20") + ")\n";
            filesList += "[Single_Weapon.png](" + (gitHubDataRepository + fp_SB_SW).replaceAll(" ", "%20") + ")\n";
            filesList += "[Multi_Weapon.png](" + (gitHubDataRepository + fp_SB_MW).replaceAll(" ", "%20") + ")\n";

            EmbedBuilder builder = new EmbedBuilder();
            builder.withAuthorName("Updated the following files:");
            builder.appendField("Files", filesList, false);

            CHANNEL.sendMessage(builder.build());
        }
        catch (MalformedURLException e)
        {
            CHANNEL.sendMessage(new WarningMessage("Whoops.", "MalformedURLException").get().build());
            return;
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("Whoops.", "IOException").get().build());
            return;
        }
    }
}
