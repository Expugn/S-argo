package io.github.spugn.Sargo.Objects;

import sx.blah.discord.util.EmbedBuilder;

public class WarningMessage
{
    private EmbedBuilder builder;

    private String title = "";
    private String text = "";

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

    private void build()
    {
        builder.withAuthorName(title);
        builder.withDesc(text);
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
}
