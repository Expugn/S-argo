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

    /* images/Miscellaneous/ */
    SCOUT_ICON("images/Miscellaneous/Scout_Icon.png"),
    MEMORY_DIAMOND_ICON("images/Miscellaneous/Memory_Diamond_Icon.png"),
    SHOP_ICON("images/Miscellaneous/Shop_Icon.png"),
    PROFILE_ICON("images/Miscellaneous/Profile_Icon.png"),
    HELP_ICON("images/Miscellaneous/Help_Icon.png"),

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
