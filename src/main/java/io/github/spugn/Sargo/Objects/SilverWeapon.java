package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;
import java.util.List;

public class SilverWeapon
{
    private List<Weapon> weaponList;

    public SilverWeapon()
    {
        weaponList = new ArrayList<>();
        Weapon sW;

        List<String> names = new ArrayList<>();

        names.add("Moonlit Sword");
        names.add("Ice Blade");
        names.add("Carmine Katana");
        names.add("Artisan's Longsword");
        names.add("Misericorde");
        names.add("Moonlight Kukri");
        names.add("Midnight Kris");
        names.add("Petra Twins");
        names.add("Gale Halbert");
        names.add("Chivalrous Rapier");
        names.add("Earth Dragon Stinger");
        names.add("Brave Rapier");
        names.add("Dual Artisan's Swords");
        names.add("Bloody Club");
        names.add("Barbarian Mace");
        names.add("Gale Zaghnal");
        names.add("Tidal Rod");
        names.add("Crystal Wand");
        names.add("Emerald Rod");
        names.add("Chivalrous Bow");
        names.add("Petra Bow");
        names.add("Falcon Shooter");
        names.add("Pale Shooter");
        names.add("Behemoth Sniper");
        names.add("Long-Range Barrett");

        for (String name : names)
        {
            sW = new Weapon();
            sW.setName(name);
            sW.setRarity("3");
            sW.setImagePath("images/Weapons/Silver/" + name.replaceAll(" ", "_") + ".png");

            weaponList.add(sW);
        }
    }

    public List<Weapon> getWeaponList()
    {
        return weaponList;
    }

    public Weapon getWeapon(int index)
    {
        return weaponList.get(index);
    }

    public int getSize()
    {
        return weaponList.size();
    }
}
