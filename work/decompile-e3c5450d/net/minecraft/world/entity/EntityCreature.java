package net.minecraft.world.entity;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;

public abstract class EntityCreature extends EntityInsentient {

    protected EntityCreature(EntityTypes<? extends EntityCreature> entitytypes, World world) {
        super(entitytypes, world);
    }

    public float f(BlockPosition blockposition) {
        return this.a(blockposition, (IWorldReader) this.level);
    }

    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.0F;
    }

    @Override
    public boolean a(GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn) {
        return this.a(this.getChunkCoordinates(), (IWorldReader) generatoraccess) >= 0.0F;
    }

    public boolean ft() {
        return !this.getNavigation().m();
    }

    @Override
    protected void fl() {
        super.fl();
        Entity entity = this.getLeashHolder();

        if (entity != null && entity.level == this.level) {
            this.a(entity.getChunkCoordinates(), 5);
            float f = this.e(entity);

            if (this instanceof EntityTameableAnimal && ((EntityTameableAnimal) this).isSitting()) {
                if (f > 10.0F) {
                    this.unleash(true, true);
                }

                return;
            }

            this.y(f);
            if (f > 10.0F) {
                this.unleash(true, true);
                this.goalSelector.a(PathfinderGoal.Type.MOVE);
            } else if (f > 6.0F) {
                double d0 = (entity.locX() - this.locX()) / (double) f;
                double d1 = (entity.locY() - this.locY()) / (double) f;
                double d2 = (entity.locZ() - this.locZ()) / (double) f;

                this.setMot(this.getMot().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
            } else {
                this.goalSelector.b(PathfinderGoal.Type.MOVE);
                float f1 = 2.0F;
                Vec3D vec3d = (new Vec3D(entity.locX() - this.locX(), entity.locY() - this.locY(), entity.locZ() - this.locZ())).d().a((double) Math.max(f - 2.0F, 0.0F));

                this.getNavigation().a(this.locX() + vec3d.x, this.locY() + vec3d.y, this.locZ() + vec3d.z, this.fu());
            }
        }

    }

    protected double fu() {
        return 1.0D;
    }

    protected void y(float f) {}
}
