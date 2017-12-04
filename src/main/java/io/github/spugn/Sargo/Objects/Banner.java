package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;

public class Banner
{
    private int bannerID;
    private String bannerName;
    private int bannerType;
    private int bannerWepType;
    private ArrayList<Character> characters;
    private ArrayList<Weapon> weapons;

    public int getBannerID()
    {
        return bannerID;
    }

    public String getBannerName()
    {
        return bannerName;
    }

    public int getBannerType()
    {
        return bannerType;
    }

    public int getBannerWepType()
    {
        return bannerWepType;
    }

    public ArrayList<Character> getCharacters()
    {
        return characters;
    }

    public ArrayList<Weapon> getWeapons()
    {
        return weapons;
    }

    public void setBannerID(int bannerID)
    {
        this.bannerID = bannerID;
    }

    public void setBannerName(String bannerName)
    {
        this.bannerName = bannerName;
    }

    public void setBannerType(int bannerType)
    {
        this.bannerType = bannerType;
    }

    public void setBannerWepType(int bannerWepType)
    {
        this.bannerWepType = bannerWepType;
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
        switch(bannerType)
        {
            case 0:
                return "Normal";
            case 1:
                return "Step Up";
            case 2:
                return "Record Crystal";
            case 3:
                return "Step Up v2";
            case 4:
                return "Birthday Step Up";
            case 5:
                return "Record Crystal v2";
            case 6:
                return "Memorial Scout";
            case 7:
                return "Step Up v3";
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
