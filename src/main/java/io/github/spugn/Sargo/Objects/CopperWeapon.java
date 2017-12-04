package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;
import java.util.List;

public class CopperWeapon
{
    private List<Weapon> weaponList;

    public CopperWeapon()
    {
        weaponList = new ArrayList<>();
        Weapon cW;

        List<String> names = new ArrayList<>();

        names.add("Gust Katana");
        names.add("Brave Gemsword");
        names.add("Queen's Knightsword");
        names.add("Flame Knife");
        names.add("Assault Dagger");
        names.add("Wind Dagger");
        names.add("Mithril Spear");
        names.add("Darkness Glaive");
        names.add("Mithril Rapier");
        names.add("Flame Rapier");
        names.add("Q. Knightsword x B. Gemsword");
        names.add("Mithril Mace");
        names.add("War Pick");
        names.add("Aqua Mace");
        names.add("Ethereal Staff");
        names.add("Evil Wand");
        names.add("Flame Wand");
        names.add("Aqua Spread");
        names.add("Dark Bow");
        names.add("Flame Shooter");
        names.add("Stealth Rifle");
        names.add("Double-Stack Magazine");
        names.add("Precision Rifle");

        for (String name : names)
        {
            cW = new Weapon();

            cW.setName(name);
            cW.setRarity(2);
            cW.setImagePath("images/Weapons/" + name.replaceAll(" ", "_") + ".png");

            weaponList.add(cW);
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
