package io.github.spugn.Sargo.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame
{
    private static final int WIDTH = 550;
    private static final int HEIGHT = 100;
    private JButton exitB;
    private JLabel botLabel;

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

        }
        catch (InstantiationException e)
        {

        }
        catch (IllegalAccessException e)
        {

        }
        catch (UnsupportedLookAndFeelException e)
        {

        }

        setTitle("S'argo v1.3 - SAO:MD Summon Simulator Discord Bot by S'pugn#2612");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pane.add(botLabel);
        pane.add(exitB);
    }
}
