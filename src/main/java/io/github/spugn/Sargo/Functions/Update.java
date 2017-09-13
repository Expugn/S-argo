package io.github.spugn.Sargo.Functions;

import io.github.spugn.Sargo.Objects.Banner;
import io.github.spugn.Sargo.Objects.Character;
import io.github.spugn.Sargo.Objects.Files;
import io.github.spugn.Sargo.Objects.WarningMessage;
import io.github.spugn.Sargo.XMLParsers.BannerParser;
import io.github.spugn.Sargo.XMLParsers.SettingsParser;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
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
        else
        {
            channel.sendMessage(new WarningMessage("INCORRECT UPDATE TYPE", "Update Types: **banners**, **images**").get().build());
        }
    }

    public Update(IChannel channel, String dataName, String bannerID_String)
    {
        CHANNEL = channel;
        gitHubDataRepository = new SettingsParser().getGitHubRepoURL();

        if (dataName.equalsIgnoreCase("images"))
        {
            updateImages((Integer.parseInt(bannerID_String) - 1));
        }
        else
        {
            channel.sendMessage(new WarningMessage("INCORRECT UPDATE TYPE", "Update Types: **banners**, **images**").get().build());
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
            CHANNEL.sendMessage(new WarningMessage("Whoops.", "MalformedURLException").get().build());
        }
        catch (IOException e)
        {
            CHANNEL.sendMessage(new WarningMessage("Whoops.", "IOException").get().build());
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
            String characterList = "";

            boolean imageFolderExists = new File("images").exists();
            boolean characterFolderExists = new File("images/Characters").exists();
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

                    characterList += "[" + c.toString() + "](" + url + ")\n";
                }
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

            EmbedBuilder builder = new EmbedBuilder();
            builder.withAuthorName("Updated the following images:");
            builder.appendField(selectedBanner.getBannerName(), characterList, false);

            CHANNEL.sendMessage(builder.build());
        }
        else
        {
            CHANNEL.sendMessage(new WarningMessage("UNKNOWN BANNER ID", "Use 'scout' for a list of banners.").get().build());
        }
    }
}
