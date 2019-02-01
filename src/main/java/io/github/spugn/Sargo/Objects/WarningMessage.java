package io.github.spugn.Sargo.Objects;

import io.github.spugn.Sargo.Utilities.GitHubImage;
import sx.blah.discord.util.EmbedBuilder;

public class WarningMessage
{
    private EmbedBuilder builder;

    private String title = "";
    private String text = "";
    private String footertext = "";

    public WarningMessage()
    {
        builder = new EmbedBuilder();
    }

    public WarningMessage(String title, String text)
    {
        builder = new EmbedBuilder();

        this.title = title;
        this.text = text;
    }

    public WarningMessage(String title, String text, String footertext)
    {
        builder = new EmbedBuilder();

        this.title = title;
        this.text = text;
        this.footertext = footertext;
    }

    private void build()
    {
        builder.withAuthorName(title);
        builder.withDesc(text);
        if (!footertext.equalsIgnoreCase(""))
        {
            builder.withFooterText(footertext);
            builder.withFooterIcon(new GitHubImage("images/System/Scout_Icon.png").getURL());
        }
        builder.withColor(255, 0, 0);
    }

    public EmbedBuilder get()
    {
        build();
        return builder;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setFootertext(String footertext)
    {
        this.footertext = footertext;
    }
}
