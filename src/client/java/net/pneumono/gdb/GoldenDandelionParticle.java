package net.pneumono.gdb;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

//? if >=1.21.9 {
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
//?}

public class GoldenDandelionParticle extends /*? if >=1.21.9 {*/SingleQuadParticle/*?} else {*//*TextureSheetParticle*//*?}*/ {
    private GoldenDandelionParticle(
            final ClientLevel level,
            final double x,
            final double y,
            final double z,
            final double xa,
            final double ya,
            final double za,
            //? if >=1.21.9
            final TextureAtlasSprite sprite,
            final boolean upwards
    ) {
        super(
                level, x, y, z, xa, ya, za
                //? if >=1.21.9
                , sprite
        );
        this.xd = xa;
        this.zd = za;
        this.yd = ya;
        this.gravity = 0.0F;
        this.yd += upwards ? 0.03 : -0.03;
        this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.5F);
        this.lifetime = 8;
    }

    //? if >=1.21.9 {
    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }
    //?} else {
    /*@Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    *///?}

    public record PauseMobGrowthProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType options, ClientLevel level,
                double x, double y, double z,
                double xAux, double yAux, double zAux
                //? if >=1.21.9
                , final RandomSource random
        ) {
            GoldenDandelionParticle particle = new GoldenDandelionParticle(
                    level, x, y, z, xAux, yAux, zAux,
                    //? if >=1.21.9
                    this.sprite.get(random),
                    false
            );
            //? if <1.21.9
            //particle.pickSprite(this.sprite);
            return particle;
        }
    }

    public record ResetMobGrowthProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(
                SimpleParticleType options, ClientLevel level,
                double x, double y, double z,
                double xAux, double yAux, double zAux
                //? if >=1.21.9
                , final RandomSource random
        ) {
            GoldenDandelionParticle particle = new GoldenDandelionParticle(
                    level, x, y, z, xAux, yAux, zAux,
                    //? if >=1.21.9
                    this.sprite.get(random),
                    true
            );
            //? if <1.21.9
            //particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
