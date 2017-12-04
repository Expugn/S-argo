# S'argo
## Information
S'argo is a Discord bot using the Discord4J API wrapper.  
This Discord bot's main use is to be a "summon simulator" for the mobile game *Sword Art Online: Memory Defrag*.

## Features
S'argo simulates the scouting system from *Sword Art Online: Memory Defrag*.

This includes:  

[Characters]
* **Normal Scouts**  
Static pull rates every time, nothing special.
* **Step Up Scouts**  
Increased 1.5x gold character chance on Step 3, Increased 2.0x gold character chance on Step 5.
* **Record Crystal Scouts**  
Record Crystals given every multi scout that can be used in a guaranteed scout.
* **Step Up v2 Scouts**  
Same thing as Step Up, but features one guaranteed platinum character on Step 5 and 2.0x platinum character rates on Step 6 which repeats.
* **Birthday Step Up Scouts**  
50% off multi scout on Step 1, Increased 2.0x gold and platinum character chance on Step 3.
* **Record Crystal v2 Scouts**  
Same thing as Record Crystal, but the first scout is 50% off.
* **Memorial Scouts**  
Static pull rates, but multi pulls are disabled and single pulls only cost 1 Memory Diamond.
* **Step Up v3 Scouts**  
50% off multi scout on Step 1, Increased 2.0x platinum character chance on Step 3.

[Weapons]
* **Normal Weapon Scouts**  
Same as normal scouts. Static pull rates every time.
* **Step Up Weapon Scouts**  
100 MD scout on Step 1, Increased 2.0x gold weapon chance on Step 3.

[Miscellaneous]
* **Normal/Plus Ticket Scouts**  
A special type of scout that doesn't use Memory Diamonds.

Unlike the other *Sword Art Online: Memory Defrag* summon simulators, S'argo features user data 
saving so users can see their character collection and get Hacking Crystals for duplicate characters
they receive to fully simulate the in-game experience.

## Demo
Want to try out the bot? Check out the **#scout-simulator** channel in the *Sword Art Online Memory Defrag* Reddit Discord!  
Invite link for the server is down in Related Links.

## Commands
- **HELP MENU**  
'**help**' - Get a command list.

- **SCOUTING**  
'**scout**' - View the first page of all available banners.  
'**scout** p[Page Number]' - View a specific page in the banner list.  
'**scout** [Banner ID]' - View banner information.  
'**scout** [Banner ID] [s(ingle)(i) | m(ulti)(i) | rc(record crystal)(i)]' - Scout a banner.  
'**scout** [Banner ID] [ws(ingle)(i) | wm(ulti)(i)]' - Scout a weapon banner.  
'**scout** [Banner ID] [nts(ingle)(i) | ntm(ulti)(i)]' - Scout using normal tickets.  
'**scout** [Banner ID] [pts(ingle)(i) | ptm(ulti)(i)]' - Scout using plus tickets.  
**Adding an "i" after your scout type will display an image result.**

- **USER PROFILE**  
'**profile**' - View your information and character collection progress.  
'**profile** data' - View your Step or Record Crystal data.  
'**profile** info [Banner ID]' - View your obtained/missing characters in a banner.  
'**profile** search [Character Name]' - Search through your collection for that character name.

- **SHOP**  
'**shop**' - View the available Memory Diamond bundles.  
'**shop** [Item ID] [Quantity]' - "Buy" that Memory Diamond bundle.

- **USER SEARCH**  
'**user** [name/@name]' - Get a user's basic profile.

- **USER RESET**  
'**reset**' - Reset your user file.  
'**reset** [Banner ID] c' - Reset your character data for a banner.  
'**reset** [Banner ID] w' - Reset your weapon data for a banner.  
'**reset** [Banner ID] a' - Reset all data for a banner.  
**Resetting all data includes your Step or Record Crystal data as well.**

- **BOT OWNERS**  
'**update**' - Download any missing character/weapon images from the data repository.  
'**update** r(eset)' - Delete all files in the character/weapon folders and re-download them.  
'**update** o(verwrite)' - Replace all character/weapon files with a new copy.   
'**stop**' - Stop the bot.  

## Related Links
[S'argo Official Wiki](https://github.com/Expugn/S-argo/wiki "Wiki")
[S'argo Official Data Repository](https://github.com/Expugn/S-argo_Data "Official Data Repository")  
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

If you enjoy using this bot, please support the francise by trying out the actual game.  
*Sword Art Online: Memory Defrag* is available in the Apple iOS App Store and Google Play Store.

Project began on September 6, 2017.