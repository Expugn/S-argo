package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;
import java.util.List;

public class SilverCharacter
{
    private List<Character> characters;

    public SilverCharacter()
    {
        characters = new ArrayList<>();
        Character sC;

        /* CREATE NAME AND PREFIX ARRAY */
        List<String> names = new ArrayList();
        List<String> prefixes = new ArrayList();

        prefixes.add("Spriggan");
        names.add("Kirito");

        prefixes.add("Solo");
        names.add("Kirito");

        prefixes.add("Katana Master");
        names.add("Klein");

        prefixes.add("Salamander Samurai");
        names.add("Klein");

        prefixes.add("");
        names.add("Sumeragi");

        prefixes.add("Guild Commander");
        names.add("Heathcliff");

        prefixes.add("");
        names.add("Red-Eyed XaXa");

        prefixes.add("");
        names.add("Kuradeel");

        prefixes.add("");
        names.add("Eugene");

        prefixes.add("Uninhibited Fairy");
        names.add("Strea");

        prefixes.add("Sword & Shield");
        names.add("Sachi");

        prefixes.add("Floor-Clearing Demon");
        names.add("Asuna");

        prefixes.add("Battle Smith");
        names.add("Lisbeth");

        prefixes.add("Item Master");
        names.add("Rain");

        prefixes.add("");
        names.add("Lux");

        prefixes.add("Party Mascot");
        names.add("Silica");

        prefixes.add("Information Broker");
        names.add("Argo");

        prefixes.add("Cat Sith Info Broker");
        names.add("Argo");

        prefixes.add("Stalwart Tank");
        names.add("Agil");

        prefixes.add("Mysterious Warrior");
        names.add("Yuuki");

        prefixes.add("Apprentice Blacksmith");
        names.add("Lisbeth");

        prefixes.add("World of Guns");
        names.add("Sinon");

        prefixes.add("Cat-Eared Archer");
        names.add("Sinon");

        prefixes.add("Agile Kitty");
        names.add("Silica");

        prefixes.add("Spriggan Seeker");
        names.add("Philia");

        prefixes.add("");
        names.add("Sakuya");

        prefixes.add("Wind Mage");
        names.add("Leafa");

        prefixes.add("The Young Pooka");
        names.add("Seven");

        prefixes.add("");
        names.add("Alicia Rue");

        prefixes.add("");
        names.add("Siune");

        /* END NAME AND PREFIX INPUT */
        /* ...don't do this at home. it's bad practice. */

        /* ITERATE THROUGH NAME ARRAY, EDIT CHARACTER/FILE PATH, THEN ADD TO CHARACTER ARRAY */
        for (int i = 0 ; i < names.size() ; i++)
        {
            sC = new Character();

            sC.setPrefix(prefixes.get(i));
            sC.setName(names.get(i));
            sC.setRarity("3");
            sC.setImagePath("images/Characters/Silver/" + prefixes.get(i).replaceAll(" ", "_") + "_" + names.get(i).replaceAll(" ", "_") + ".png");

            characters.add(sC);
        }
    }

    public List<Character> getCharacters()
    {
        return characters;
    }

    public Character getCharacter(int index)
    {
        return characters.get(index);
    }

    public int getSize()
    {
        return characters.size();
    }

    public void systemReadImagePaths()
    {
        for (Character character : characters)
        {
            System.out.println(character.getImagePath());
        }
    }
}
