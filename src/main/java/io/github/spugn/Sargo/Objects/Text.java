package io.github.spugn.Sargo.Objects;

/**
 * Text used in the program.
 */
public enum Text
{
    /* ARGO SPEECH */
    ARGO_1("*\"Lookin' for good allies, huh?\nTry the Teleport Plaza.\"*"),
    ARGO_2("*\"So you wanna make friends, eh?\nTry the Teleport Plaza.*\""),
    ARGO_3("*\"たくさん強い仲間が欲しいの力。\n今から転移門広場に行きナ。*\""),
    ARGO_4("*\"オッケー。 力強い仲間カ。\n今から転移門広場に行きナ。*\""),

    /* SCOUT INTERFACE */
    MD_REMAINING("Memory Diamonds Left"),
    SINGLE_PULL("Single Pull"),
    MULTI_PULL("Multi Pull"),

    /* BANNER LIST INTERFACE */
    BANNER_LIST_TITLE("Banner List"),
    BANNER_LIST_BANNER_COUNT(" banners available."),
    BANNER_LIST_BANNER_HEADER("- Banners - "),
    BANNER_LIST_FOOTER("'scout [Banner ID]' for more banner info.  |  'scout p[Page]' to view another page."),

    /* BANNER INFO INTERFACE */
    BANNER_INFO_CHARACTER_COUNT(" characters available."),
    BANNER_INFO_CHARACTER_HEADER("- Characters -"),
    BANNER_INFO_RATES_HEADER("- Pull Rates -"),
    BANNER_INFO_FOOTER_1("Use 'scout "),
    BANNER_INFO_FOOTER_2(" [s/si | m/mi | rc/rci]' to scout."),

    /* END OF TEXT */
    ;
    private final String text;

    Text(String text)
    {
        this.text = text;
    }

    public String get()
    {
        return text;
    }
}
