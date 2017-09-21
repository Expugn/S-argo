# S'argo
## Information
S'argo is a Discord bot using the Discord4J API wrapper.

This Discord bot's main use is to be a "summon simulator" for the mobile game *Sword Art Online: Memory Defrag*.

## Usage
S'argo was not built to have self-hosting in mind, so support won't be given.

To start the bot: insert your bot's token in data/Settings.xml under the **token** section.  
You can then run the code to start the bot.

There IS a basic GUI, but it only features one button and clicking that button kills the bot.

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
* **Weapon Scouts**  
Same as normal scouts, but with weapons instead of characters.

Unlike the other *Sword Art Online: Memory Defrag* summon simulators, S'argo features user data 
saving so users can see their character collection and get Hacking Crystals for duplicate characters
they receive to fully simulate the in-game experience.

## Commands
- **HELP MENU**  
'**help**' - Get a command list.

- **SCOUTING**  
'**scout**' - View the first page of all available banners.  
'**scout** p[Page Number]' - View a specific page in the banner list.  
'**scout** [Banner ID]' - View banner information.  
'**scout** [Banner ID] [s(ingle)(i) | m(ulti)(i) | rc(record crystal)(i)]' - Scout a banner.  
'**scout** [Banner ID] [ws(ingle)(i) | wm(ulti)(i)]' - Scout a weapon banner.  
**Adding an "i" after s/ws/m/wm/rc will display an image result.**

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

- **BOT OWNERS**  
'**update**' - Download image files from the GitHub Data Repository.  
'**stop**' - Stop the bot.  

## Startup Guide
If you're just starting, grab the S-argo_Startup_Kit.zip in releases.  
1) Open the Settings.xml file located in the data folder.
2) Place your Discord ID and Bot Token in the appropriate location.
3) Modify the other settings as you please.
3) Run the .jar file; a GUI should appear and it should show your Bot's name and discriminator.
4) In any non-ignored channel, type '**update banners**', this should grab the Banners.xml file from the GitHub repository under Settings.xml.
6) Scouting is now available, but if you try to generate an image, gold/platinum character images will be placeholders if you didn't grab the Character_Images.zip file.
7) You can also download character images by running '**update images [Banner ID]**' in any non-ignored channel.
8) Once all the character images for each banner are saved in their appropriate location, the bot is now ready for use.

## Updating Guide
### S'argo (.jar File)
Grab the latest release of S'argo. After stopping your bot, you can delete your old .jar file and replace it 
with the new latest release .jar file. Make sure to check for any Settings.xml changes and update your Settings.xml
file if there are any.

### Banners.xml
When the bot is running, you can use '**update banners**' to grab the latest Banner.xml file from the GitHub Data
Repository linked in Settings.xml.  
If you wish to update manually, fill out the banner details in a similar format to the other banners.

### Character Images
When the bot is running, you can use '**update images [Banner ID]**' to grab the images for that banner. Make sure
you've updated to the latest banner file first before doing so.  
If you wish to update manually, make a new folder with the banner name under images/Characters/ and put your
image files inside.

### Settings.xml
You can grab the latest version of the Settings.xml file by using '**update settings**' when the bot is running.
This will save a file named "new_Settings.xml" in your data folder from the GitHub Data Repository.  
You can modify the settings in there as you please, and then delete your old Settings.xml file and rename the new Settings.xml file to "Settings.xml".

## Related Links
[S'argo Official Data Repository](https://github.com/Expugn/S-argo_Data "Official Data Repository")

## Other Stuff
This is a non-profit fan project with the purpose of practice and entertainment.

*Sword Art Online* is owned by **A-1 Pictures**, **Aniplex USA**, and **Reki Kawahara**.  
All characters and assets belong to their respective owners.  

If you enjoy using this bot, please support the francise by trying out the actual game.  
*Sword Art Online: Memory Defrag* is available in the Apple iOS App Store and Google Play Store.

Project began on September 6, 2017.