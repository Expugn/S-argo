# Frequently Asked Questions
> I just downloaded the bot, but 'scout' isn't working!

Make sure you have a Banners.xml file. Grab the latest copy with 'update banners'.

> I can't use bot owner commands! (update, stop, etc)

Make sure you've put your Discord ID in the appropriate location in Settings.xml.  
Your Discord ID is not your username and discriminator (i.e.: S'pugn#2612). 
 
To get your Discord ID, go to "User Settings" > "Appearance" and turn on 
"Developer Mode" which is located under "Advanced". Once Developer Mode is enabled,
you can now right click your name/profile picture and click "Copy ID". Paste the ID
under the appropriate location in Settings.xml.

If you don't wish to keep developer mode, feel free to turn it back off.

> The bot doesn't recognize anything but '@botname <command>'!

Set "UseMention" in the Settings.xml file to false.

> I'm just getting text results for scouts! Where are the images?

Make sure to include an 'i' after the scout type you want.  
For example: using 'scout 1 mi' will produce an image for the multi pull
you just did for the *Start Dash* banner.

If that isn't the case, make sure "DisableImages" in the Settings.xml file
is set to false.

# Contributing to S'argo

## Bug Reports
Please file any bugs you find to the issues tracker linked below.  
[S'argo Issues Tracker](https://github.com/Expugn/S-argo/issues "Issues Tracker")  

Please include the following details in your issue report:  
- Bot Version
- Type of issue (Text, Bug, Unhandled Exception, etc)
- Brief summary of what the bug is and how to replicate it.  

I will try to then correct your issue to the best of my ability.

**Please do not use the Issues Tracker if you do any of the following**:
- Use a personal GitHubDataRepository
- Use a non-official/self-modified version of the bot
- Use a custom Banners.xml file
- Use a custom Settings.xml file
- Use custom character images
- Haven't followed the proper update instructions
- Haven't followed the proper setup instructions
- Use an outdated version of the bot

I try to make updating/setup as simple as possible and will provide detailed instructions
on how to do so properly. *Please* respect my effort and read through these provided
instructions carefully to ensure the best experience. If my directions are unclear,
please let me know where and how I could change it to make things clearer.

As for modified bots (those that use non-official images, DataRepository, etc), before
releasing an update, I make sure to test the new features thoroughly. However, this
is assuming that the bot is using valid and correct settings. If you choose to modify
these files, I cannot guarantee that the bot will work properly. Please do not use
the issue tracker if an issue occurs that resulted in your modifications.

## Suggesting New Features
If you wish to suggest a new feature, contact me through Discord!  
You can find me (**S'pugn#2612**) in the *Sword Art Online: Memory Defrag* Reddit Discord.  

**I will not accept friend requests nor will I respond to any messages from those who are
not a part of the Reddit Discord for my safety.**

I am open to all suggestions, but I will only accept suggestions that are actual features in
*Sword Art Online: Memory Defrag*. So if you want to suggest something that is not a part of
the game (i.e: Step Ups/Guaranteed Scouts for Weapons), then it will likely be declined.

## Pull Requests
I'm not accepting pull requests at the moment. Sorry!  
If you really want to contribute to the project, please leave a suggestion instead.

# The Vision for S'argo
S'argo is intended to be an accurate recreation of the scouting feature in *Sword Art Online: Memory Defrag*.
Although there are some things that were not a part of the game (i.e: a banner for free characters/event characters) I
wish for the bot to be accurate to the money you spend, rate changes from Step-Ups, Hacking Crystals you receive from
duplicate characters, etc to ensure the best simulated experience for the game.

I hope users will use this simulator to see how they should continue forward in their real game file.
Memory Diamonds are **VERY** expensive and most users cannot afford to spend $80 USD (as of this writing)
to get 700 Memory Diamonds to scout for 22 characters (500 Memory Diamonds) or 44 weapons (600 Memory Diamonds).
It is very possible for you to not get anything usable from that $80 USD since the rates are fairly low and
anything that isn't a Gold or above is basically garbage.

For those who don't plan on spending money, I hope they will use this simulator to enjoy the "whaling" experience
that a small group of users have with their bottomless wallets. With every global banner available, I hope the
completionists out there will also enjoy trying to collect all the characters for bragging rights to their fellow
discord server-mates. To those who just want to scout with no consequences, I hope you will enjoy using this
summon simulator as well.