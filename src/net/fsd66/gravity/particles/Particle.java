package net.fsd66.gravity.particles;

import java.awt.*;

public class Particle {
    private static float gravitationalConstant;
    private float x, y, radius, mass;
    private float xVelocity, yVelocity;
    private int color;

    public Particle(float x, float y, float radius, float mass, int rgbColor) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.color = rgbColor;
    }

    public void step(float delta) {
        move((xVelocity * (delta / 1000.0f)), (yVelocity * (delta / 1000.0f)));
    }

    public void render(Graphics2D g) {
        g.setColor(new Color(color));
        g.drawOval((int)(x - radius), (int)(y - radius), (int)(radius), (int)(radius));
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDiameter() {
        return radius * 2;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public int getColor() {
        return color;
    }

    public Color getAWTColor() {
        return new Color(color);
    }

    public void setColor(int rgbColor) {
        this.color = rgbColor;
    }

    public void setColor(Color color) {
        this.color = color.getRGB();
    }

    public void applyGravity(Particle p, float delta) {
        // m_1a = Gm_1m_2/r^2 --> a = Gm_2/r^2 --> vt = Gm_2/r^2 --> v = Gm_2/tr^2
        float rSquared = (((p.getX() - x) * (p.getX() - x)) + ((p.getY() - y) * (p.getY() - y)));
        float v = (Particle.getGravitationalConstant() * p.getMass()) / ((delta/1000.0f) * rSquared);
        float dXV = (float) (v * Math.cos(Math.acos(p.getX() - x / Math.sqrt(rSquared))));
        float dYV = (float) (v * Math.sin(Math.asin(p.getY() - y / Math.sqrt(rSquared))));
        if(Particle.detectCollision(this, p)) {
            dXV = 0.0f;
            dYV = 0.0f;
        }
        xVelocity += dXV;
        yVelocity += dYV;
    }

    public static void setGravitationalConstant(float gravitationalConstant) {
        Particle.gravitationalConstant = gravitationalConstant;
    }

    public static float getGravitationalConstant() {
        return Particle.gravitationalConstant;
    }

    public static boolean detectCollision(Particle p1, Particle p2) {
        return (((p2.getX() - p1.getX()) * (p2.getX() - p1.getX()))
                + ((p2.getY() - p1.getY()) * (p2.getY() - p1.getY())))
                < ((p1.getRadius() + p2.getRadius()) * (p1.getRadius() + p2.getRadius()));
    }

    public static Particle cloneParticle(Particle p) {
        return new Particle(p.x, p.y, p.radius, p.mass, p.color);
    }
}
