package io.github.spugn.Sargo.Objects;

/**
 * Since Discord4J doesn't allow reading images from source when it comes to EmbedMessages, I'll have to upload them.
 * Images used in this enum can be found under the images folder.
 */
public enum Images
{
    /* images/Argo/ */
    ARGO_SMILE("images/Argo/Argo_Smile.png"),
    ARGO_GRIN("images/Argo/Argo_Grin.png"),
    ARGO_SMUG("images/Argo/Argo_Smug.png"),
    ARGO_FLOWERS("images/Argo/Argo_Flowers.png"),

    /* images/Items/ */
    MEMORY_DIAMOND("images/Items/Memory_Diamond.png"),
    HACKING_CRYSTAL("https://i.imgur.com/1hwvwfP.png"),
    GOLD_LARGE("https://i.imgur.com/Z5ynBaX.png"),

    /* images/Miscellaneous/ */
    SCOUT_ICON("images/Miscellaneous/Scout_Icon.png"),
    MEMORY_DIAMOND_ICON("https://i.imgur.com/KZzkrLd.png"),

    /* images/Weapon Chest/ */
    BROWN_CHEST("https://i.imgur.com/U1ZdX6L.png"),
    BLUE_CHEST("https://i.imgur.com/5QgeJOD.png"),
    RED_CHEST("https://i.imgur.com/1peRhZ8.png"),

    /* END OF IMAGES */
    ;

    private final String url;
    private final String GITHUB_IMAGE = "https://raw.githubusercontent.com/Expugn/S-argo/master/";

    Images (String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return (GITHUB_IMAGE + url).replaceAll(" ", "%20");
    }

}
