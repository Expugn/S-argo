package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;

public class Banner
{
    private String bannerID;
    private String bannerName;
    private String bannerType;
    private ArrayList<Character> characters;
    private ArrayList<Weapon> weapons;

    public String getBannerID()
    {
        return bannerID;
    }

    public String getBannerName()
    {
        return bannerName;
    }

    public String getBannerType()
    {
        return bannerType;
    }

    public ArrayList<Character> getCharacters()
    {
        return characters;
    }

    public ArrayList<Weapon> getWeapons()
    {
        return weapons;
    }

    public void setBannerID(String bannerID)
    {
        this.bannerID = bannerID;
    }

    public void setBannerName(String bannerName)
    {
        this.bannerName = bannerName;
    }

    public void setBannerType(String bannerType)
    {
        this.bannerType = bannerType;
    }

    public void setCharacters(ArrayList<Character> characters)
    {
        this.characters = characters;
    }

    public void setWeapons(ArrayList<Weapon> weapons)
    {
        this.weapons = weapons;
    }

    public String bannerTypeToString()
    {
        int type = Integer.parseInt(bannerType);

        switch(type)
        {
            case 0:
                return "Normal";
            case 1:
                return "Step Up";
            case 2:
                return "Record Crystal";
            case 3:
                return "Step Up v2";
            default:
                return "Unknown";
        }
    }

    @Override
    public String toString()
    {
        return bannerID + ") " + bannerName;
    }
}
