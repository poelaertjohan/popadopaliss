package net.minecraft.world.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.phys.Vec3D;

public interface ISteerable {

    boolean a();

    void a(Vec3D vec3d);

    float b();

    default boolean a(EntityInsentient entityinsentient, SaddleStorage saddlestorage, Vec3D vec3d) {
        if (!entityinsentient.isAlive()) {
            return false;
        } else {
            Entity entity = entityinsentient.cB();

            if (entityinsentient.isVehicle() && entityinsentient.fc() && entity instanceof EntityHuman) {
                entityinsentient.setYRot(entity.getYRot());
                entityinsentient.yRotO = entityinsentient.getYRot();
                entityinsentient.setXRot(entity.getXRot() * 0.5F);
                entityinsentient.setYawPitch(entityinsentient.getYRot(), entityinsentient.getXRot());
                entityinsentient.yBodyRot = entityinsentient.getYRot();
                entityinsentient.yHeadRot = entityinsentient.getYRot();
                entityinsentient.maxUpStep = 1.0F;
                entityinsentient.flyingSpeed = entityinsentient.ev() * 0.1F;
                if (saddlestorage.boosting && saddlestorage.boostTime++ > saddlestorage.boostTimeTotal) {
                    saddlestorage.boosting = false;
                }

                if (entityinsentient.cH()) {
                    float f = this.b();

                    if (saddlestorage.boosting) {
                        f += f * 1.15F * MathHelper.sin((float) saddlestorage.boostTime / (float) saddlestorage.boostTimeTotal * 3.1415927F);
                    }

                    entityinsentient.r(f);
                    this.a(new Vec3D(0.0D, 0.0D, 1.0D));
                    entityinsentient.lerpSteps = 0;
                } else {
                    entityinsentient.a((EntityLiving) entityinsentient, false);
                    entityinsentient.setMot(Vec3D.ZERO);
                }

                entityinsentient.as();
                return true;
            } else {
                entityinsentient.maxUpStep = 0.5F;
                entityinsentient.flyingSpeed = 0.02F;
                this.a(vec3d);
                return false;
            }
        }
    }
}
