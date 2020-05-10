package edu.nyu.pqs.ps3.view;

import javax.swing.*;
import java.awt.*;

public class TestMain {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label1 = new JLabel(new OvalIcon(20,50));
        JLabel label2 = new JLabel(new OvalIcon(50,20));
        JLabel label3 = new JLabel
                ("Round!", new OvalIcon(60,60), SwingConstants.CENTER);
        label3.setHorizontalTextPosition(SwingConstants.CENTER);

        Container c = f.getContentPane();
        c.setLayout(new FlowLayout());
        c.add(label1);
        c.add(label2);
        c.add(label3);
        f.pack();
        f.setVisible(true);
    }

    private static class OvalIcon implements Icon {

        private int width, height;

        public OvalIcon(int w, int h) {
            width = w;
            height = h;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.drawOval(x, y, width-1, height-1);
        }

        public int getIconWidth() { return width; }
        public int getIconHeight() { return height; }
    }

}
