![S'argo Logo](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/S'argo_Banner_Animated.gif)
_[The .GIF version of the banner doesn't do it justice. Higher quality version here.](https://giant.gfycat.com/GoldenInnocentAustralianfreshwatercrocodile.webm)_

# S'argo
[![Downloads](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/Downloads_Button.png)](https://github.com/Expugn/S-argo/releases)
[![Wiki](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/Wiki_Button.png)](https://github.com/Expugn/S-argo/wiki)
[![Issues Tracker](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/Issues_Tracker_Button.png)](https://github.com/Expugn/S-argo/issues)
[![Data Repository](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/Data_Repository_Button.png)](https://github.com/Expugn/S-argo_Data_v2)
[![/r/MemoryDefrag Discord](https://raw.githubusercontent.com/Expugn/S-argo_Data_v2/master/wiki/readme/Discord_Button.png)](http://discord.gg/MemoryDefrag)

> **Table of Contents**  
[1. Information](https://github.com/Expugn/S-argo#information "Information")  
[2. Features](https://github.com/Expugn/S-argo#features "Features")  
[3. Demo](https://github.com/Expugn/S-argo#demo "Demo")  
[4. Commands](https://github.com/Expugn/S-argo#commands "Commands")  
[5. Related Links](https://github.com/Expugn/S-argo#related-links "Related Links")  
[6. Special Thanks](https://github.com/Expugn/S-argo#special-thanks "Special Thanks")  
[7. Other Stuff](https://github.com/Expugn/S-argo#other-stuff "Other Stuff")    

## Information
S'argo is a Discord bot made using the [Discord4J](https://github.com/austinv11/Discord4J "Discord4J on GitHub") API wrapper.  
This Discord bot's main use is to be a "summon simulator" for the mobile game *Sword Art Online: Memory Defrag*.

## Features
S'argo simulates the scouting system from *Sword Art Online: Memory Defrag*.

This includes:  

| Character Scout Type | Description |  
| :---: | :--- |  
| Normal | Static pull rates every time. Nothing special.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul></details> |  
| Step Up | Performing more multi pulls modifies the character rates and pricing.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`200` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`200` Memory Diamonds</ul>**★4 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★4 Rate** <ul>`+2.0x`</ul>**Resets back to `Step 1`**</details> |  
| Record Crystal | Record Crystals are given every multi scout which can be used in a record crystal scout.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul></details> |  
| Step Up v2 | Modified version of `Step Up` that features a repeating `Step 6`.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`200` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`200` Memory Diamonds</ul>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Birthday Step Up | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**★4 Rate** <ul>`+2.0x`</ul>**★5 Rate** <ul>`+2.0x`</ul>**Resets back to `Step 1`.**</details> |  
| Record Crystal v2 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>First Scout</h2>**Multi** <ul>`125` Memory Diamonds</ul></details> |  
| Memorial | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`1` Memory Diamond</ul>**Multi** <ul>`Disabled`</ul></details> |  
| Step Up v3 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**★5 Rate** <ul>`+2.0x`</ul></details> |  
| Record Crystal v3 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>First Scout</h2>**Multi** <ul>`125` Memory Diamonds</ul></details> |  
| Event | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`0` Memory Diamond</ul>**Multi** <ul>`Disabled`</ul></details> |  
| SAO Game 5th Anniversary Step Up | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`55` Memory Diamonds</ul><h2>Step 3</h2>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Record Crystal v4 | Introduced the increased `+1.5` rates for ★5 characters (`2%` -> `3%`). This change carries through future banners.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>First Scout</h2>**Multi** <ul>`125` Memory Diamonds</ul></details> |  
| Step Up v4 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`125` Memory Diamonds</ul>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |
| Step Up v5 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`200` Memory Diamonds</ul>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |
| SAO Game 5th Anniversary Step Up v2 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`55` Memory Diamonds</ul><h2>Step 3</h2>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Step Up v6 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**★5 Rate** <ul>`+2.0x`</ul></details> |
| SAO Game 5th Anniversary Step Up v3 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`55` Memory Diamonds</ul>**★4+ Rate** <ul>`100%` (for one character)</ul><h2>Step 3</h2>**★5 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★5 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★5 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details |  
| Step Up v7 | Introduces ★6 characters.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`200` Memory Diamonds</ul>**★6 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★6 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★6 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Record Crystal v5 | The first use of Record Crystals with ★6 characters. Circulating Record Crystals are unobtainable here. <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>First Scout</h2>**Multi** <ul>`125` Memory Diamonds</ul></details> |  
| Step Up v8 | Same as `Step Up v7`, but includes the rest of the ★5 characters into the general pool.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`125` Memory Diamonds</ul><h2>Step 3</h2>**Multi** <ul>`200` Memory Diamonds</ul>**★6 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★6 Rate** <ul>`100%` (for one character)</ul><h2>Step 6</h2>**★6 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Record Crystal v6 | The first use of Circulating Record Crystals with ★6 characters. <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`25` Memory Diamonds</ul>**Multi** <ul>`250` Memory Diamonds</ul><h2>First Scout</h2>**Multi** <ul>`125` Memory Diamonds</ul></details> |  

| Weapon Scout Type | Description |  
| :---: | :--- |  
| Normal | Static pull rates every time. Nothing special.<details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`15` Memory Diamonds</ul>**Multi** <ul>`150` Memory Diamonds</ul></details> |  
| Step Up | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`15` Memory Diamonds</ul>**Multi** <ul>`150` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`100` Memory Diamonds</ul><h2>Step 3</h2>**★4 Rate** <ul>`+2.0x`</ul></details> |
| GGO Step Up | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`15` Memory Diamonds</ul>**Multi** <ul>`150` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`100` Memory Diamonds</ul>**Guarantees `1` ★2 Automatic Rifle weapon.**<h2>Step 3</h2>**★4 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★4 Rate** <ul>`100%` (for one weapon)</ul><h2>Step 6</h2>**★4 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  
| Step Up v2 | <details> <summary>**Rates/Pricing**</summary> <h2>Base Pricing</h2>**Single** <ul>`15` Memory Diamonds</ul>**Multi** <ul>`150` Memory Diamonds</ul><h2>Step 1</h2>**Multi** <ul>`100` Memory Diamonds</ul><h2>Step 3</h2>**★4 Rate** <ul>`+1.5x`</ul><h2>Step 5</h2>**★4 Rate** <ul>`100%` (for one weapon)</ul><h2>Step 6</h2>**★4 Rate** <ul>`+2.0x`</ul>**`Step 6` repeats.**</details> |  

| Miscellaneous Scout Type | Description |  
| :---: | :--- |  
| Normal Ticket | Scout that typically has misc. items, rainbow essence, and copper exchange swords. |  
| Plus Ticket | Scout that typically has medallions, rainbow essence, and silver+ exchange swords. |
| Normal v2 Ticket | Scout that typically has misc. items, rainbow essence, upgrade crystals, memory fragments, and copper exchange swords. |  
| Plus v2 Ticket | Scout that typically has medallions, rainbow essence, upgrade crystals, memory fragments and silver+ exchange swords. |

Unlike the other *Sword Art Online: Memory Defrag* summon simulators, S'argo features user data 
saving so users can see their character collection and get Hacking Crystals for duplicate characters
they receive to fully simulate the in-game experience.

## Demo
Want to try out the bot? Check out the **#scout-simulator** channel in the *Sword Art Online Memory Defrag* Reddit Discord!  
Invite link for the server is down in Related Links.

## Commands
Example commands are assuming you have your command prefix set as the default (`$`):

###### GENERAL

| Command | Description | Example |
| :---: | :--- | :---: |
| `help` | Get the command list. | `$help` |
| `info` | Get some bot information. | `$info` |

###### SCOUTING
```
ABBREVIATION MEANINGS:
s = "single"
m = "multi"
rc = "record crystal"
ws = "weapon single"
wm = "weapon multi"
nts = "normal ticket single"
ntm = "normal ticket multi"
pts = "plus ticket single"
ptm = "plus ticket multi"
i = "image"
```
**Adding an `i` after your scout type will display an image result.**

| Command | Description | Example |
| :---: | :--- | :---: |
| `scout` | View the first page of all available banners. | `$scout` |
| `scout p[Page Number]` | View a specific page in the banner list. | `$scout p2` |
| `scout [Banner ID]` | View a specific banner's information. | `$scout 1` |
| `scout [Banner ID] [s(i) / m(i) / rc(i)]` | Scout a banner. | `$scout 1 mi` |
| `scout [Banner ID] [ws(i) / wm(i)]` | Scout a weapon banner. | `$scout 1 ws` |
| `scout [nts(i) / ntm(i)]` | Scout using normal tickets. | `$scout ntsi` |
| `scout [pts(i) / ptm(i)]` | Scout using plus tickets. | `$scout ptm` |  
| `scout [nt2s(i) / nt2m(i)]` | Scout using normal v2 tickets. | `$scout nt2si` |  
| `scout [pt2s(i) / pt2m(i)]` | Scout using plus v2 tickets. | `$scout pt2m` |  

###### USER PROFILE

| Command | Description | Example |
| :---: | :--- | :---: |
| `profile` | View your information and character collection progress. | `$profile` |
| `profile data` | View your Step Up or Record Crystal data | `$profile data` |
| `profile info [Banner ID]` | View your obtained/missing characters or weapons in a banner. | `$profile info 1` |
| `profile search [Character Name]` | Search through your collection for that character. | `$profile search Kirito` |

###### SHOP

| Command | Description | Example |
| :---: | :--- | :---: |
| `shop` | View the available Memory Diamond bundles. | `$shop` |
| `shop [Item ID] [Quantity]` | "Buy" a Memory Diamond bundle. | `$shop 7 10` |

###### USER SEARCH

| Command | Description | Example |
| :---: | :--- | :---: |
| `user [@name]` | Get a user's basic profile | `$user @S'pugn` |

###### USER RESET

| Command | Description | Example |
| :---: | :--- | :---: |
| `reset` | Reset your entire user file. | `$reset` |
| `reset [Banner ID] c` | Reset your character data for a banner. | `$reset 1 c` |
| `reset [Banner ID] w` | Reset your weapon data for a banner. | `$reset 1 w` |
| `reset [Banner ID] a` | Reset all data (characters/weapons/step/record crystal) for a banner. | `$reset 1 a` |

###### BOT OWNERS

| Command | Description | Example |
| :---: | :--- | :---: |
| `update` | Download any missing character/weapon images from the data repository. | `$update` |
| `update r` | Delete all files in the character/weapon folders and re-download them. | `$update r` |
| `update o` | Replace all character/weapon files with a new copy. | `$update o` |
| `reload` | Reload the `Banners.xml` and setting files to use any new changes that were made. | `$reload` |
| `settings` | Used to modify the setting files. | `$settings` |
| `stop` | Shutdown the bot. | `$stop` |

## Related Links
[S'argo Official Wiki](https://github.com/Expugn/S-argo/wiki "Wiki")  
[S'argo Official Data Repository](https://github.com/Expugn/S-argo_Data_v2 "Official Data Repository")  
[*Sword Art Online: Memory Defrag* Subreddit](https://www.reddit.com/r/MemoryDefrag/ "Fan Subreddit")  
[Subreddit Discord Invite Link](http://discord.gg/MemoryDefrag "Discord Invite Link")  
[4★ Character Database](https://www.reddit.com/r/MemoryDefrag/comments/5yyr4j/sao_md_4_database/ "4★ Character Database")

## Special Thanks
> **Pep#7848 | /u/pedrobpimenta**  
  For liking my bot enough to ask to add it to the Reddit Discord, lol.  
  Also added the Wind, Halloween 2016, and Earth banners/images that were exclusive to the Japan server.
  
> **Azuto#0016 | /u/Azuto**  
  For his work in the Character Database.  
  The database was really handy for grabbing banner names, character prefixes, and weapon names.  
  The character database is linked in Related Links.
  
> **Legacy ∞**  
  For beta testing S'argo before it's official release :HAhaa:. Thanks guys!
  
> **Discord Dataminers (Salieri#6041 and Ntogg#2809)**  
  For releasing game assets of new content (images/prefixes/etc) before they're announced so banners/images
  can be updated quicker.

## Other Stuff
This is a non-profit fan project with the purpose of practice and entertainment.

*Sword Art Online* is owned by **A-1 Pictures**, **Aniplex USA**, and **Reki Kawahara**.  
All characters and assets belong to their respective owners.  

If you enjoy using this bot, please support the franchise by trying out the actual game.  
*Sword Art Online: Memory Defrag* is available in the Apple iOS App Store and Google Play Store.

Project began on September 6, 2017.