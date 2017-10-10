package io.github.spugn.Sargo.Objects;

public class Item
{
    private String name;
    private int quantity;
    private int value;
    private String imagePath;

    public Item()
    {
        name = "null";
        quantity = 0;
        value = -1;
        imagePath = "";
    }

    public Item(String name, int quantity, int value)
    {
        this.name = name;
        this.quantity = quantity;
        this.value = value;
        imagePath = "";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getName()
    {
        return name;
    }

    public String getImagePath()
    {
        if (imagePath.isEmpty())
        {
            return "images/Items/" + name.replaceAll(" ", "_") + ".png";
        }
        return imagePath;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return name + " x" + quantity;
    }
}
