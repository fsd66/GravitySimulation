package net.fsd66.gravity;

import net.fsd66.gravity.particles.Particle;

import javax.swing.*;

public class GravityGUI {
    public static void main(String[] args) {
        final int NUMBER_OF_PARTICLES = 100;
        final int NUMBER_OF_FRAMES = 1000;
        GravityMain gravityMain = new GravityMain();
        Particle.setGravitationalConstant(1);
        gravityMain.init(NUMBER_OF_FRAMES, NUMBER_OF_PARTICLES);

        JFrame frame = new JFrame(gravityMain.getTITLE());
        frame.add(gravityMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gravityMain.start();
    }
}
