package io.github.spugn.Sargo.Objects;

/**
 * Since Discord4J doesn't allow reading images from source when it comes to EmbedMessages, I'll have to upload them.
 * Images used in this enum can be found under the images folder.
 */
public enum Images
{
    /* images/Argo/ */
    ARGO_SMILE("https://i.imgur.com/LC1jytj.png"),
    ARGO_GRIN("https://i.imgur.com/ut9E78S.png"),
    ARGO_SMUG("https://i.imgur.com/9KL1WGS.png"),
    ARGO_FLOWERS("https://i.imgur.com/yPL7GtS.png"),

    /* images/Items/ */
    MEMORY_DIAMOND("https://i.imgur.com/A4rsy5v.png"),
    HACKING_CRYSTAL("https://i.imgur.com/1hwvwfP.png"),
    GOLD_LARGE("https://i.imgur.com/Z5ynBaX.png"),

    /* images/Miscellaneous/ */
    SCOUT_ICON("https://i.imgur.com/814p6X5.png"),
    MEMORY_DIAMOND_ICON("https://i.imgur.com/KZzkrLd.png"),

    /* images/Weapon Chest/ */
    BROWN_CHEST("https://i.imgur.com/U1ZdX6L.png"),
    BLUE_CHEST("https://i.imgur.com/5QgeJOD.png"),
    RED_CHEST("https://i.imgur.com/1peRhZ8.png"),

    /* Banner Info Thumbnails */
    BANNER_1("https://i.imgur.com/m1Pk1Td.png"),
    BANNER_2("https://i.imgur.com/UvzIVoj.png"),

    /* END OF IMAGES */
    ;

    private final String url;

    Images (String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

}
