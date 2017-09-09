package io.github.spugn.Sargo.Objects;

public class Character
{
    private String prefix;
    private String name;
    private String rarity;
    private String imagePath;

    public Character()
    {
        prefix = "";
        name = "null";
        rarity = "1";
        imagePath = "";
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRarity(String rarity)
    {
        this.rarity = rarity;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getName()
    {
        return name;
    }

    public String getRarity()
    {
        return rarity;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    @Override
    public String toString()
    {
        return rarity + "★ [" + prefix + "] " + name;
    }
}
