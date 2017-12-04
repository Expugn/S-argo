package io.github.spugn.Sargo.Objects;

import java.util.ArrayList;
import java.util.List;

public class CopperCharacter
{
    private List<Character> characters;

    public CopperCharacter()
    {
        characters = new ArrayList<>();
        Character cC = new Character();

        /* CREATE KIRITO FIRST SINCE HE'S THE ONLY ONE WITH A PREFIX */
        cC.setPrefix("Former Beta Tester");
        cC.setName("Kirito");
        cC.setRarity(2);
        cC.setImagePath("images/Characters/Kirito.png");
        characters.add(cC);

        /* CREATE NAME ARRAY */
        List<String> names = new ArrayList();

        names.add("Klein");
        names.add("Diavel");
        names.add("Corvatz");
        names.add("Thinker");
        names.add("Sigurd");
        names.add("Strea");
        names.add("Sachi");
        names.add("Asuna");
        names.add("Yolko");
        names.add("Yulier");
        names.add("Silica");
        names.add("Philia");
        names.add("Argo");
        names.add("Sasha");
        names.add("Agil");
        names.add("Kibaou");
        names.add("Kagemune");
        names.add("Lisbeth");
        names.add("Rosalia");
        names.add("Sinon");
        /* END NAME INPUT */

        /* ITERATE THROUGH NAME ARRAY, EDIT CHARACTER/FILE PATH, THEN ADD TO CHARACTER ARRAY */
        for (String name : names)
        {
            cC = new Character();

            cC.setPrefix("");
            cC.setName(name);
            cC.setRarity(2);
            cC.setImagePath("images/Characters/" + name + ".png");

            characters.add(cC);
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
}
