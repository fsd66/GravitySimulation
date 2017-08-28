package net.fsd66.gravity;

import net.fsd66.gravity.particles.Particle;
import net.fsd66.gravity.particles.ParticleGenerator;
import org.jetbrains.annotations.Contract;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;

public class GravityMain extends JPanel implements ActionListener {
    private final int WIDTH;
    private final int HEIGHT;
    private final float TARGET_FPS;
    private final String TITLE;

    private boolean initialized;

    private long numberOfFrames;
    private int numberOfParticles;
    private LinkedList<Particle[]> simulationCache;

    private Timer timer;

    private long currentFrame;
    private Particle[] currentParticles;
    private ListIterator<Particle[]> frameIterator;

    @Contract(pure = true)
    public final String getTITLE() {
        return TITLE;
    }

    @Contract(pure = true)
    public final int getWIDTH() {
        return WIDTH;
    }

    @Contract(pure = true)
    public final int getHEIGHT() {
        return HEIGHT;
    }

    public GravityMain() {
        this("Gravity Simulator", 800, 600, 60.0f);
    }

    public GravityMain(String title, int width, int height, float targetFPS) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.TITLE = title;
        this.TARGET_FPS = targetFPS;

        timer = new Timer((int)(1000.0f/targetFPS), this);

        initialized = false;
    }

    public void init(long numberOfFrames, int numberOfParticles){
        System.out.println("Initializing simulation with " + numberOfParticles + " particles.");
        this.numberOfParticles = numberOfParticles;
        this.numberOfFrames = numberOfFrames;

        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        generateParticles(numberOfParticles);
        simulate(numberOfFrames);

        initialized = true;
    }

    private void generateParticles(int numberOfParticles) {
        System.out.println("Generating initial set of particles...");
        ParticleGenerator particleGenerator = new ParticleGenerator();
        simulationCache = new LinkedList<Particle[]>();
        Particle[] initialParticles = new Particle[numberOfParticles];
        for(int i = 0; i < initialParticles.length; i++) {
            initialParticles[i] = particleGenerator.generateRandomParticle(0, getWIDTH(), 0, getHEIGHT(), 1f,0,1, 0, 0, 0xFFFFFF);
        }
        simulationCache.add(initialParticles);
        System.out.println(numberOfParticles + " particles successfully generated.");
    }

    private void simulate(long numberOfFrames) {
        for(int i = 0; i < numberOfFrames; i++) {
            //Calculates the particles of the next frame, and adds it to the frame list
            simulationCache.add(applyGravity(simulationCache.getLast()));
            System.out.println("Generated frame #" + simulationCache.size());
        }

        System.out.println("Simulated " + simulationCache.size() + " frames.");
    }

    private Particle[] applyGravity(Particle[] particles) {
        Particle[] frame = new Particle[particles.length];

        // Copy the particles from the last frame to the current one
        for(int i = 0; i < particles.length; i++) {
            frame[i] = Particle.cloneParticle(particles[i]);
        }

        // Apply the effect of gravity from the particles on the last frame to the particles on the current frame
        for(Particle p : frame) {
            for(Particle p2 : particles) {
                if(p == p2) break;
                p.applyGravity(p2, TARGET_FPS / 1000.0f);
            }

            // Apply the movement induced by gravity to each particle on the new frame
            p.step(TARGET_FPS / 1000.0f);
        }

        return frame;
    }

    public synchronized void start() {
        System.out.println("Playing back simulation (" + simulationCache.size() + " frames)...");
        frameIterator = simulationCache.listIterator();
        currentParticles = simulationCache.getFirst();
        currentFrame = 0;
        timer.start();
    }

    public synchronized void stop() {
        timer.stop();
        System.out.println("Playback ended.");
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        render(g2D);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == timer) {
            step();
            repaint();
        }
    }

    public void step() {
        if(currentFrame < 2) {
            currentParticles = frameIterator.next();
            currentFrame++;
        }
        else {
            stop();
        }
    }

    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH, HEIGHT);
        for(Particle p : currentParticles) {
            p.render(g);
        }
        g.setColor(Color.RED);
        g.drawString("Frame #: " + currentFrame, 10, 10);
    }
}
