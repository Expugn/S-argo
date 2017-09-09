package io.github.spugn.Sargo.Objects;

/**
 * File paths to common files.
 */
public enum Files
{
    /* CHARACTER PLACEHOLDERS*/
    GRAY_PLACEHOLDER("images/Characters/Placeholders/Gray.png"),
    COPPER_PLACEHOLDER("images/Characters/Placeholders/Copper.png"),
    SILVER_PLACEHOLDER("images/Characters/Placeholders/Silver.png"),
    GOLD_PLACEHOLDER("images/Characters/Placeholders/Gold.png"),
    PLATINUM_PLACEHOLDER("images/Characters/Placeholders/Platinum.png"),

    /* SCOUT IMAGES */
    SINGLE_SCOUT_BACKGROUND("images/Scout Backgrounds/Single.png"),
    MULTI_SCOUT_BACKGROUND("images/Scout Backgrounds/Multi.png"),
    SCOUT_RESULT("images/result.png"),

    /* XML FILES */
    BANNER_XML("data/Banners.xml"),

    /* END OF FILES */
    ;

    private String filePath;

    Files(String filePath)
    {
        this.filePath = filePath;
    }

    public String get()
    {
        return filePath;
    }
}
