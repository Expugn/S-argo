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
    BANNER_LIST_FOOTER("'scout [Banner ID]' for more banner info.  |  'scout p[page]' to view another page."),

    /* BANNER INFO INTERFACE */
    BANNER_INFO_CHARACTER_COUNT(" characters available."),
    BANNER_INFO_CHARACTER_HEADER("- Characters -"),
    BANNER_INFO_RATES_HEADER("- Pull Rates -"),
    BANNER_INFO_FOOTER_1("Use 'scout "),
    BANNER_INFO_FOOTER_2(" [single | multi]' to scout."),

    /* WARNINGS */
    SCOUT_RATE_ERROR("SCOUT RATES DO NOT ADD UP TO 1.0.\nPLEASE CORRECT THIS ERROR IN YOUR **Settings.xml** FILE."),
    SCOUT_IMAGE_GEN_ERROR("IO EXCEPTION\nCANNOT GENERATE SCOUT RESULT IMAGE."),
    SCOUT_MISSING_RESULT_IMAGE_ERROR("UNABLE TO DISPLAY SCOUT RESULT.\nIMAGE NOT FOUND."),
    SCOUT_UNKNOWN_TYPE("**UNKNOWN SCOUT TYPE.**\nUse '*scout [Banner ID] [**single** | **s** | **1** | **multi** | **m** | **11**]*'"),
    SCOUT_UNKNOWN_BANNER("**UNKNOWN BANNER ID.**\nUse '*scout*'"),
    SCOUT_NOT_ENOUGH_ARGUMENTS("**NOT ENOUGH ARGUMENTS**\nUse '*scout [Banner ID] [**single** | **s** | **1** | **multi** | **m** | **11**]*'"),
    SCOUT_NUMBER_FORMAT_EXCEPTION("**COMMAND ERROR**\nMake sure you're entering a number for the Banner ID."),
    UNKNOWN_COMMAND("**UNKNOWN COMMAND.**\nUse '*help*'"),

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
