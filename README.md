# S'argo
## Information
S'argo is a Discord bot using the Discord4J API wrapper.

This Discord bot's main use is to be a "summon simulator" for the mobile game *Sword Art Online: Memory Defrag*.

## Usage
S'argo was not built to have self-hosting in mind, so support won't be given.

To start the bot: insert your bot's token in data/Settings.xml under the **token** section.  
You can then run the code to start the bot. There is no GUI or anything, so if you decide  
to make an executable .jar file with this code, you'll have to terminate the java process in  
task manager to stop the bot.

## Features
S'argo features the scouting feature from *Sword Art Online: Memory Defrag*.

This includes:
* **Normal Scouts**  
Static pull rates every time, nothing special.
* **Step Up Scouts**  
Increased 1.5x gold character chance on Step 3, Increased 2.0x gold character chance on Step 5.
* **Record Crystal Scouts**  
Record Crystals given every multi scout that can be used in a guaranteed scout.
* **Step Up v2 Scouts**  
Same thing as Step Up, but features one guaranteed platinum character on Step 5 and 2.0x platinum character rates on Step 6 which repeats.

Unlike the other *Sword Art Online: Memory Defrag* summon simulators, S'argo features user data 
saving so users can see their character collection and get Hacking Crystals for duplicate characters
they receive to fully simulate the in-game experience.

## Commands
S'argo is called by mentioning your bot before the command.  
Replace '@mention' with '@your_bot_name'.  

- **SCOUTING**  
'@mention **scout**' - View the first page of all available banners.  
'@mention **scout** p[Page Number]' - View a specific page in the banner list.  
'@mention **scout** [Banner ID]' - View banner information.  
'@mention **scout** [Banner ID] [s(ingle) | m(ulti) | g(uaranteed)]' - Scout a banner.

- **USER PROFILE**  
'@mention **profile**' - View your information and character collection progress.  
'@mention **profile** data' - View your Step or Record Crystal data.  
'@mention **profile** info [Banner ID]' - View your obtained/missing characters in a banner.  
'@mention **profile** search [Character Name]' - Search through your collection for that character name.

- **USER RESET**  
'@mention **reset**' - Reset your user file.  

## Other Stuff
This is a non-profit fan project with the purpose of practice and entertainment.

*Sword Art Online* is owned by **A-1 Pictures**, **Aniplex USA**, and **Reki Kawahara**.  
All characters and assets belong to their respective owners.  

If you enjoy using this bot, please support the francise by trying out the actual game.  
*Sword Art Online: Memory Defrag* is available in the Apple iOS App Store and Google Play Store.