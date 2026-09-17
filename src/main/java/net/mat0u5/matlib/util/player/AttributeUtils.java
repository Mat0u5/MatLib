package net.mat0u5.matlib.util.player;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
//? if > 1.20.3 {
import net.minecraft.core.Holder;
//?}

public class AttributeUtils {
	public static AttributeHolder MAX_HEALTH = new AttributeHolder(Attributes.MAX_HEALTH);
	public static AttributeHolder JUMP_STRENGTH = new AttributeHolder(Attributes.JUMP_STRENGTH);
	public static AttributeHolder MOVEMENT_SPEED = new AttributeHolder(Attributes.MOVEMENT_SPEED);

	//? if > 1.20.3 {
	public static AttributeHolder SCALE = new AttributeHolder(Attributes.SCALE);
	public static AttributeHolder SAFE_FALL_DISTANCE = new AttributeHolder(Attributes.SAFE_FALL_DISTANCE);
	public static AttributeHolder STEP_HEIGHT = new AttributeHolder(Attributes.STEP_HEIGHT);
	//?}


	public static class AttributeHolder {
		//? if <= 1.20.3 {
		/*public final Attribute attributeHolder;
		public AttributeHolder(Attribute attributeHolder) {
		*///?} else {
		public final Holder<Attribute> attributeHolder;
		public AttributeHolder(Holder<Attribute> attributeHolder) {
		//?}
			this.attributeHolder = attributeHolder;
		}

		public AttributeEntityInstance of(LivingEntity entity) {
			return new AttributeEntityInstance(entity, this.attributeHolder);
		}
	}

	//? if <= 1.20.3 {
	/*public record AttributeEntityInstance(LivingEntity entity, Attribute attributeHolder) {
	*///?} else {
	public record AttributeEntityInstance(LivingEntity entity, Holder<Attribute> attributeHolder) {
	//?}

		public void reset() {
			//? if <=1.20.3 {
			/*this.set(attributeHolder.getDefaultValue());
			*///?} else {
			this.set(attributeHolder.value().getDefaultValue());
			 //?}
		}

		public void set(double value) {
			if (entity == null || attributeHolder == null) return;
			AttributeInstance instance = entity.getAttribute(attributeHolder);
			if (instance != null) instance.setBaseValue(value);
		}

		public double getDefaultValue() {
			if (attributeHolder == null) return 0;
			//? if <=1.20.3 {
			/*return attributeHolder.getDefaultValue();
			 *///?} else {
			return attributeHolder.value().getDefaultValue();
			//?}
		}

		public double get() {
			if (entity == null || attributeHolder == null) return 0;
			return entity.getAttributeBaseValue(attributeHolder);
		}
	}
}
