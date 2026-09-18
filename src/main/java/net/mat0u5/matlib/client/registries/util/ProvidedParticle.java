package net.mat0u5.matlib.client.registries.util;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ProvidedParticle<T extends ParticleOptions> (ParticleType<T> particleType, ParticleProvider<T> particleProvider) {}
