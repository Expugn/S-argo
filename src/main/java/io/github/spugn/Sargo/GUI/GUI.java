package io.github.spugn.Sargo.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame
{
    private static final int WIDTH = 500;
    private static final int HEIGHT = 100;
    private JButton exitB;

    public GUI()
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

        setTitle("S'argo - SAO:MD Summon Simulator Discord Bot");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pane.add(exitB);
    }
}