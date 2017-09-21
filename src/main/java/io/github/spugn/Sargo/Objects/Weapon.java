package io.github.spugn.Sargo.Objects;

public class Weapon
{
    private String name;
    private String rarity;
    private String imagePath;
    private int count;

    public Weapon()
    {
        name = "null";
        rarity = "1";
        imagePath = "";
        count = 0;
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

    public void setCount(int count)
    {
        this.count = count;
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

    public int getCount()
    {
        return count;
    }

    @Override
    public String toString()
    {
        return rarity + "★ " + name;
    }

    public String toStringWithCount()
    {
        return rarity + "★ " + name + " (x" + count + ")";
    }
}
