package net.fsd66.gravity.particles;

import java.util.Random;

public class ParticleGenerator {
    private Random random;
    private long seed;

    public ParticleGenerator() {
        this(-1); // -1 sets RNG to randomized seed
    }

    public ParticleGenerator(long seed) {
        setSeed(seed);
    }

    public Particle generateParticle(float x, float y, float radius, float mass, int color) {
        return new Particle(x, y, radius, mass, color);
    }

    public Particle generateRandomParticle(float xOffset, float xRange, float yOffset, float yRange, float radiusOffset, float radiusRange, float massOffset, float massRange, int colorOffset, int colorRange) {
        return generateParticle((random.nextFloat() * xRange) + xOffset, (random.nextFloat() * yRange) + yOffset, (random.nextFloat() * radiusRange) + radiusOffset, (random.nextFloat() * massRange) + massOffset, random.nextInt(colorRange) + colorOffset);
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        if(seed == -1) {
            random = new Random();
        }
        else {
            random = new Random(this.seed);
        }
    }
}
