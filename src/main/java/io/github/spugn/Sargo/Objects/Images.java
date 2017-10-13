package io.github.spugn.Sargo.Objects;

import io.github.spugn.Sargo.XMLParsers.SettingsParser;

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

    /* images/Ushi/ */
    USHI_DISGUST("images/Ushi/Ushi_Disgust.png"),
    USHI_GRIN("images/Ushi/Ushi_Grin.png"),
    USHI_HAPPY("images/Ushi/Ushi_Happy.png"),
    USHI_GASM("images/Ushi/Ushi_Gasm.png"),

    /* images/Legacy/ */
    FEELS_PIANIST_MAN("images/Legacy/FeelsPianistMan.png"),
    JAVALAVA("images/Legacy/JavaLava.png"),
    PINEHAX("images/Legacy/PineHax.png"),
    OREOWOLFBTW("images/Legacy/OreoWolfBtw.png"),

    /* images/Miscellaneous/ */
    SCOUT_ICON("images/Miscellaneous/Scout_Icon.png"),
    MEMORY_DIAMOND_ICON("images/Miscellaneous/Memory_Diamond_Icon.png"),
    SHOP_ICON("images/Miscellaneous/Shop_Icon.png"),
    PROFILE_ICON("images/Miscellaneous/Profile_Icon.png"),
    HELP_ICON("images/Miscellaneous/Help_Icon.png"),

    /* images/Chest/ */
    CHEST_BROWN("images/Chest/Brown.png"),
    CHEST_BLUE("images/Chest/Blue.png"),
    CHEST_RED("images/Chest/Red.png"),

    /* images/Ticket/*/
    TICKET_NORMAL("images/Ticket/Normal.png"),
    TICKET_PLUS("images/Ticket/Plus.png"),

    /* END OF IMAGES */
    ;

    private final String url;
    private final String GITHUB_IMAGE = new SettingsParser().getGitHubRepoURL();

    Images (String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return (GITHUB_IMAGE + url).replaceAll(" ", "%20");
    }

}
