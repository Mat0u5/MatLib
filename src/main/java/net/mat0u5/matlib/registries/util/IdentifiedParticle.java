package net.mat0u5.matlib.registries.util;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;

public record IdentifiedParticle(ParticleType<?> particleType, Identifier id) { }
