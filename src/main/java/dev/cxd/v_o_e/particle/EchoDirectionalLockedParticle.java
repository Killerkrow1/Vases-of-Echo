package dev.cxd.v_o_e.particle;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class EchoDirectionalLockedParticle extends SpriteBillboardParticle {
    private static final Vector3f field_38334 = (new Vector3f(0.5F, 0.5F, 0.5F)).normalize();
    private static final Vector3f field_38335 = new Vector3f(-1.0F, -1.0F, 0.0F);
    private static final float X_ROTATION = 1.0472F;
    private int delay;
    private final SpriteProvider spriteProvider;
    private boolean isActive;

    EchoDirectionalLockedParticle(ClientWorld world, double x, double y, double z, int delay, SpriteProvider spriteProvider) {
        super(world, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
        this.scale = 2.0F;
        this.delay = delay;
        this.gravityStrength = 0.0F;
        this.maxAge = 200;
        this.velocityX = (double)0.0F;
        this.velocityY = (double)0.0F;
        this.velocityZ = (double)0.0F;
        this.spriteProvider = spriteProvider;
        this.setSprite(spriteProvider);
        this.isActive = true;
    }

    public float getSize(float tickDelta) {
        return this.scale;
    }

    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (this.delay <= 0) {
            this.buildGeometryInternal(vertexConsumer, camera, tickDelta, (quaternion) -> quaternion.mul((new Quaternionf()).rotationX(((float)Math.PI / 2F))));
            this.buildGeometryInternal(vertexConsumer, camera, tickDelta, (quaternion) -> quaternion.mul((new Quaternionf()).rotationYXZ(-(float)Math.PI, (-(float)Math.PI / 2F), 0.0F)));
        }
    }

    private void buildGeometryInternal(VertexConsumer vertexConsumer, Camera camera, float tickDelta, Consumer<Quaternionf> rotator) {
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0.0F, field_38334.x(), field_38334.y(), field_38334.z());
        rotator.accept(quaternionf);
        quaternionf.transform(field_38335);
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float i = this.getSize(tickDelta);

        for (int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.rotate(quaternionf);
            vector3f.mul(i);
            vector3f.add(f, g, h);
        }

        int light = this.getBrightness(tickDelta);
        this.vertex(vertexConsumer, vector3fs[0], this.getMaxU(), this.getMaxV(), light);
        this.vertex(vertexConsumer, vector3fs[1], this.getMaxU(), this.getMinV(), light);
        this.vertex(vertexConsumer, vector3fs[2], this.getMinU(), this.getMinV(), light);
        this.vertex(vertexConsumer, vector3fs[3], this.getMinU(), this.getMaxV(), light);
    }

    private void vertex(VertexConsumer vertexConsumer, Vector3f pos, float u, float v, int light) {
        vertexConsumer.vertex((double)pos.x(), (double)pos.y(), (double)pos.z()).texture(u, v).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }

    public int getBrightness(float tint) {
        return 240;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        if (this.delay > 0) {
            --this.delay;
        } else {
            int totalLifetime = 200;
            int fadeDuration = 40;
            int growthDuration = totalLifetime - fadeDuration;
            int elapsedTime = this.age;
            if (elapsedTime <= growthDuration) {
                this.scale = 4.0F + (float)elapsedTime / (float)growthDuration * 4.0F;
            } else if (elapsedTime <= totalLifetime) {
                float fadeProgress = (float)(elapsedTime - growthDuration) / (float)fadeDuration;
                this.alpha = 1.0F - fadeProgress;
                this.scale = 8.0F;
            }

            super.tick();
            if (this.age++ >= this.maxAge) {
                this.markDead();
            }
        }
    }

    public void activate() {
        this.isActive = true;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            EchoDirectionalLockedParticle particle = new EchoDirectionalLockedParticle(world, x, y + 0.5, z, 2, this.spriteProvider);
            particle.setSpriteForAge(this.spriteProvider);
            return particle;
        }
    }
}