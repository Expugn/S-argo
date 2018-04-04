package io.github.spugn.Sargo.GUI;

import io.github.spugn.Sargo.System.SystemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI
 * <p>
 *     Creates a simple GUI with only one purpose which is to stop
 *     the bot when the button is pressed.
 * </p>
 *
 * @author S'pugn
 * @version 1.0
 * @since v1.0
 */
@Deprecated
public class GUI extends JFrame
{
    private static final int WIDTH = 550;
    private static final int HEIGHT = 100;
    private JButton exitB;
    private JLabel botLabel;

    /**
     * Creates the GUI.
     *
     * @param botName  Name of the bot that is running the program.
     */
    public GUI(String botName)
    {
        exitB = new JButton("Stop S'argo");
        exitB.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        botLabel = new JLabel("        Now running on: " + botName);

        Container pane = getContentPane();
        pane.setLayout(new GridLayout(1, 1));

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("[GUI] - Class Not Found Exception");
        }
        catch (InstantiationException e)
        {
            System.out.println("[GUI] - Instantiation Exception");
        }
        catch (IllegalAccessException e)
        {
            System.out.println("[GUI] - Illegal Access Exception");
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.out.println("[GUI] - Unsupported Look and Feel Exception");
        }

        setTitle("S'argo v" + SystemData.getVERSION() + " - SAO:MD Summon Simulator Discord Bot by S'pugn#2612");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pane.add(botLabel);
        pane.add(exitB);
    }
}
