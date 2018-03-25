package io.github.spugn.Sargo.Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * IMAGE EDITOR
 * <p>
 *     This class manages all the image editing that S'argo does. It takes
 *     the character/weapon/item images and applies a rarity border to them
 *     and saves it in a given location.
 *     Besides drawing individual character/weapon/item images, this class
 *     also draws the single/multi scout result images as well.
 * </p>
 *
 * @author S'pugn
 * @version 1.1
 * @since v2.0
 */
public class ImageEditor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageEditor.class);

    public void drawCharacterImage(String charImageString, int rarity, boolean exists, boolean useRarityStars, String fileName)
    {
        try
        {
            /* CHARACTER */
            LOGGER.debug("Drawing CHARACTER image: \"" + charImageString + "\"");
            File characterImage = new File(charImageString);
            Image character;
            if (!(characterImage.exists()))
            {
                LOGGER.warn("Character image not found! Using placeholder instead.");
                character = ImageIO.read(new File("images/System/Character_Placeholder.png"));
            }
            else
            {
                character = ImageIO.read(characterImage);
            }
            BufferedImage result = new BufferedImage(character.getWidth(null), character.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(character, 0, 0, null);

            /* RARITY STARS */
            if (useRarityStars)
            {
                int starX = result.getWidth() / 2;
                int starY = 175;
                BufferedImage rStar = ImageIO.read(new File("images/System/Rarity_Star.png"));
                switch (rarity)
                {
                    case 5:
                        g.drawImage(rStar, starX + 51, starY, null);
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 4:
                        g.drawImage(rStar, starX + 34, starY, null);
                        g.drawImage(rStar, starX, starY, null);
                        g.drawImage(rStar, starX - 34, starY, null);
                        g.drawImage(rStar, starX - 68, starY, null);
                        break;
                    case 3:
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        break;
                    case 2:
                        g.drawImage(rStar, starX, starY, null);
                        g.drawImage(rStar, starX - 34, starY, null);
                        break;
                    default:
                        g.drawImage(rStar, starX - 17, starY, null);
                        break;
                }
            }

            /* RARITY BORDER */
            switch (rarity)
            {
                case 5:
                    BufferedImage biPlatinum = ImageIO.read(new File("images/System/Platinum_Border.png"));
                    g.drawImage(biPlatinum, 0, 0, null);
                    break;
                case 4:
                    BufferedImage biGold = ImageIO.read(new File("images/System/Gold_Border.png"));
                    g.drawImage(biGold, 0, 0, null);
                    break;
                case 3:
                    BufferedImage biSilver = ImageIO.read(new File("images/System/Silver_Border.png"));
                    g.drawImage(biSilver, 0, 0, null);
                    break;
                case 2:
                    BufferedImage biCopper = ImageIO.read(new File("images/System/Copper_Border.png"));
                    g.drawImage(biCopper, 0, 0, null);
                    break;
                default:
                    BufferedImage biGray = ImageIO.read(new File("images/System/Gray_Border.png"));
                    g.drawImage(biGray, 0, 0, null);
                    break;
            }

            /* SHADE CHARACTER IMAGE */
            if (exists)
            {
                BufferedImage biShade = ImageIO.read(new File("images/System/Character_Shade.png"));
                g.drawImage(biShade, 0, 0, null);
            }

            /* SAVE */
            ImageIO.write(result, "png", new File(fileName));
            LOGGER.debug("Done! Saved file as \"" + fileName + "\"");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception (drawCharacterImage)");
        }
    }

    public void drawWeaponImage(String weapImageString, int rarity, boolean useRarityStars, String fileName)
    {
        try
        {
            /* WEAPON */
            LOGGER.debug("Drawing WEAPON image: \"" + weapImageString + "\"");
            File weaponImage = new File(weapImageString);
            Image weapon;
            if (!(weaponImage.exists()))
            {
                LOGGER.warn("Weapon image not found! Using placeholder instead.");
                weapon = ImageIO.read(new File("images/System/Weapon_Placeholder.png"));
            }
            else
            {
                weapon = ImageIO.read(weaponImage);
            }
            BufferedImage result = new BufferedImage(weapon.getWidth(null), weapon.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(weapon, 0, 0, null);

            /* RARITY STARS */
            if (useRarityStars)
            {
                int starX = (result.getWidth() / 2);
                int starY = 175;
                BufferedImage rStar = ImageIO.read(new File("images/System/Rarity_Star.png"));
                BufferedImage erStar = ImageIO.read(new File("images/System/Empty_Rarity_Star.png"));
                switch (rarity)
                {
                    case 5:
                        g.drawImage(rStar, starX + 51, starY, null);
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 4:
                        g.drawImage(erStar, starX + 51, starY, null);
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 3:
                        g.drawImage(erStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 2:
                        g.drawImage(erStar, starX + 17, starY, null);
                        g.drawImage(erStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    default:
                        g.drawImage(erStar, starX + 17, starY, null);
                        g.drawImage(erStar, starX - 17, starY, null);
                        g.drawImage(erStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                }
            }

            /* RARITY BORDER */
            switch (rarity)
            {
                case 5:
                    BufferedImage biPlatinum = ImageIO.read(new File("images/System/Platinum_Border.png"));
                    g.drawImage(biPlatinum, 0, 0, null);
                    break;
                case 4:
                    BufferedImage biGold = ImageIO.read(new File("images/System/Gold_Border.png"));
                    g.drawImage(biGold, 0, 0, null);
                    break;
                case 3:
                    BufferedImage biSilver = ImageIO.read(new File("images/System/Silver_Border.png"));
                    g.drawImage(biSilver, 0, 0, null);
                    break;
                case 2:
                    BufferedImage biCopper = ImageIO.read(new File("images/System/Copper_Border.png"));
                    g.drawImage(biCopper, 0, 0, null);
                    break;
                default:
                    BufferedImage biGray = ImageIO.read(new File("images/System/Gray_Border.png"));
                    g.drawImage(biGray, 0, 0, null);
                    break;
            }

            /* SAVE */
            ImageIO.write(result, "png", new File(fileName));
            LOGGER.debug("Done! Saved file as \"" + fileName + "\"");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception (drawWeaponImage)");
        }
    }

    public void drawItemImage(String itemImageString, int rarity, boolean useRarityStars, String fileName)
    {
        try
        {
            /* ITEM */
            LOGGER.debug("Drawing ITEM image: \"" + itemImageString + "\"");
            File itemImage = new File(itemImageString);
            Image item;
            if (!(itemImage.exists()))
            {
                LOGGER.warn("Item image not found! Using placeholder instead.");
                item = ImageIO.read(new File("images/System/Item_Placeholder.png"));
            }
            else
            {
                item = ImageIO.read(itemImage);
            }
            BufferedImage result = new BufferedImage(item.getWidth(null), item.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(item, 0, 0, null);

            /* RARITY STARS */
            if (useRarityStars)
            {
                int starX = (result.getWidth() / 2);
                int starY = 175;
                BufferedImage rStar = ImageIO.read(new File("images/System/Rarity_Star.png"));
                BufferedImage erStar = ImageIO.read(new File("images/System/Empty_Rarity_Star.png"));
                switch (rarity)
                {
                    case 5:
                        g.drawImage(rStar, starX + 51, starY, null);
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 4:
                        //g.drawImage(erStar, starX + 51, starY, null);
                        g.drawImage(rStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 3:
                        g.drawImage(erStar, starX + 17, starY, null);
                        g.drawImage(rStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    case 2:
                        g.drawImage(erStar, starX + 17, starY, null);
                        g.drawImage(erStar, starX - 17, starY, null);
                        g.drawImage(rStar, starX - 51, starY, null);
                        g.drawImage(rStar, starX - 85, starY, null);
                        break;
                    default:
                        break;
                }
            }

            /* RARITY BORDER */
            switch (rarity)
            {
                case 4:
                    BufferedImage biGold = ImageIO.read(new File("images/System/Gold_Border.png"));
                    g.drawImage(biGold, 0, 0, null);
                    break;
                case 3:
                    BufferedImage biSilver = ImageIO.read(new File("images/System/Silver_Border.png"));
                    g.drawImage(biSilver, 0, 0, null);
                    break;
                case 2:
                    BufferedImage biCopper = ImageIO.read(new File("images/System/Copper_Border.png"));
                    g.drawImage(biCopper, 0, 0, null);
                    break;
                default:
                    BufferedImage biWhite = ImageIO.read(new File("images/System/White_Border.png"));
                    g.drawImage(biWhite, 0, 0, null);
                    break;
            }

            /* SAVE */
            ImageIO.write(result, "png", new File(fileName));
            LOGGER.debug("Done! Saved file as \"" + fileName + "\"");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception (drawItemImage)");
        }
    }

    public void drawSingleScout(String imageString, boolean isCharacterScout, String fileName)
    {
        try
        {
            LOGGER.debug("Drawing SINGLE SCOUT image.");
            int x;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background;
            if (isCharacterScout)
            {
                scout_background = ImageIO.read(new File("images/System/Single_Character.png"));
            }
            else
            {
                scout_background = ImageIO.read(new File("images/System/Single_Weapon.png"));
            }
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* IMAGE */
            BufferedImage bi = ImageIO.read(new File(imageString));
            x = (scout_background.getWidth(null) / 2) - (bi.getWidth() / 2);
            g.drawImage(bi, x, y, null);

            ImageIO.write(result, "png", new File(fileName));
            LOGGER.debug("Done! Saved file as \"" + fileName + "\"");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception (drawSingleScout)");
        }
    }

    public void drawMultiScout(String imageStrings[], boolean isCharacterScout, String fileName)
    {
        try
        {
            LOGGER.debug("Drawing MULTI SCOUT image.");
            int x = 0;
            int y = 95;

            /* SCOUT BACKGROUND */
            Image scout_background;
            if (isCharacterScout)
            {
                scout_background = ImageIO.read(new File("images/System/Multi_Character.png"));
            }
            else
            {
                scout_background = ImageIO.read(new File("images/System/Multi_Weapon.png"));
            }
            BufferedImage result = new BufferedImage(scout_background.getWidth(null), scout_background.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(scout_background, 0, 0, null);

            /* IMAGES */
            for (int i = 0 ; i < imageStrings.length ; i++)
            {
                BufferedImage bi = ImageIO.read(new File(imageStrings[i]));
                g.drawImage(bi, x, y, null);
                x += bi.getWidth();

                /* RESET POSITION IF NEAR OUT OF BOUNDS */
                if (x >= result.getWidth())
                {
                    x = 0;
                    y += bi.getHeight();
                }
            }

            ImageIO.write(result, "png", new File(fileName));
            LOGGER.debug("Done! Saved file as \"" + fileName + "\"");
        }
        catch (IOException e)
        {
            LOGGER.error("IO Exception (drawMultiScout)");
        }
    }
}
