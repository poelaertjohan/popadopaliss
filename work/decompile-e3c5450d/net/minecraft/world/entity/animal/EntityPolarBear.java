package net.minecraft.world.entity.animal;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.MathHelper;
import net.minecraft.util.TimeRange;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntitySize;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.IEntityAngerable;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFollowParent;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalPanic;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalUniversalAngerReset;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;

public class EntityPolarBear extends EntityAnimal implements IEntityAngerable {

    private static final DataWatcherObject<Boolean> DATA_STANDING_ID = DataWatcher.a(EntityPolarBear.class, DataWatcherRegistry.BOOLEAN);
    private static final float STAND_ANIMATION_TICKS = 6.0F;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeRange.a(20, 39);
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;

    public EntityPolarBear(EntityTypes<? extends EntityPolarBear> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityAgeable) EntityTypes.POLAR_BEAR.a((World) worldserver);
    }

    @Override
    public boolean n(ItemStack itemstack) {
        return false;
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityPolarBear.c());
        this.goalSelector.a(1, new EntityPolarBear.d());
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new EntityPolarBear.b());
        this.targetSelector.a(2, new EntityPolarBear.a());
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, 10, true, false, this::a_));
        this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityFox.class, 10, true, true, (Predicate) null));
        this.targetSelector.a(5, new PathfinderGoalUniversalAngerReset<>(this, false));
    }

    public static AttributeProvider.Builder p() {
        return EntityInsentient.w().a(GenericAttributes.MAX_HEALTH, 30.0D).a(GenericAttributes.FOLLOW_RANGE, 20.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D).a(GenericAttributes.ATTACK_DAMAGE, 6.0D);
    }

    public static boolean c(EntityTypes<EntityPolarBear> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        Optional<ResourceKey<BiomeBase>> optional = generatoraccess.j(blockposition);

        return !Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) && !Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN)) ? b(entitytypes, generatoraccess, enummobspawn, blockposition, random) : generatoraccess.getLightLevel(blockposition, 0) > 8 && generatoraccess.getType(blockposition.down()).a(Blocks.ICE);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.a(this.level, nbttagcompound);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        this.c(nbttagcompound);
    }

    @Override
    public void anger() {
        this.setAnger(EntityPolarBear.PERSISTENT_ANGER_TIME.a(this.random));
    }

    @Override
    public void setAnger(int i) {
        this.remainingPersistentAngerTime = i;
    }

    @Override
    public int getAnger() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public UUID getAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isBaby() ? SoundEffects.POLAR_BEAR_AMBIENT_BABY : SoundEffects.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.POLAR_BEAR_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void t() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEffects.POLAR_BEAR_WARNING, 1.0F, this.ep());
            this.warningSoundTicks = 40;
        }

    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.entityData.register(EntityPolarBear.DATA_STANDING_ID, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.updateSize();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.fv()) {
                this.clientSideStandAnimation = MathHelper.a(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = MathHelper.a(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

        if (!this.level.isClientSide) {
            this.a((WorldServer) this.level, true);
        }

    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        if (this.clientSideStandAnimation > 0.0F) {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;

            return super.a(entitypose).a(1.0F, f1);
        } else {
            return super.a(entitypose);
        }
    }

    @Override
    public boolean attackEntity(Entity entity) {
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (float) ((int) this.b(GenericAttributes.ATTACK_DAMAGE)));

        if (flag) {
            this.a((EntityLiving) this, entity);
        }

        return flag;
    }

    public boolean fv() {
        return (Boolean) this.entityData.get(EntityPolarBear.DATA_STANDING_ID);
    }

    public void v(boolean flag) {
        this.entityData.set(EntityPolarBear.DATA_STANDING_ID, flag);
    }

    public float z(float f) {
        return MathHelper.h(f, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    @Override
    protected float eu() {
        return 0.98F;
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(1.0F);
        }

        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    private class c extends PathfinderGoalMeleeAttack {

        public c() {
            super(EntityPolarBear.this, 1.25D, true);
        }

        @Override
        protected void a(EntityLiving entityliving, double d0) {
            double d1 = this.a(entityliving);

            if (d0 <= d1 && this.h()) {
                this.g();
                this.mob.attackEntity(entityliving);
                EntityPolarBear.this.v(false);
            } else if (d0 <= d1 * 2.0D) {
                if (this.h()) {
                    EntityPolarBear.this.v(false);
                    this.g();
                }

                if (this.j() <= 10) {
                    EntityPolarBear.this.v(true);
                    EntityPolarBear.this.t();
                }
            } else {
                this.g();
                EntityPolarBear.this.v(false);
            }

        }

        @Override
        public void d() {
            EntityPolarBear.this.v(false);
            super.d();
        }

        @Override
        protected double a(EntityLiving entityliving) {
            return (double) (4.0F + entityliving.getWidth());
        }
    }

    private class d extends PathfinderGoalPanic {

        public d() {
            super(EntityPolarBear.this, 2.0D);
        }

        @Override
        public boolean a() {
            return !EntityPolarBear.this.isBaby() && !EntityPolarBear.this.isBurning() ? false : super.a();
        }
    }

    private class b extends PathfinderGoalHurtByTarget {

        public b() {
            super(EntityPolarBear.this);
        }

        @Override
        public void c() {
            super.c();
            if (EntityPolarBear.this.isBaby()) {
                this.g();
                this.d();
            }

        }

        @Override
        protected void a(EntityInsentient entityinsentient, EntityLiving entityliving) {
            if (entityinsentient instanceof EntityPolarBear && !entityinsentient.isBaby()) {
                super.a(entityinsentient, entityliving);
            }

        }
    }

    private class a extends PathfinderGoalNearestAttackableTarget<EntityHuman> {

        public a() {
            super(EntityPolarBear.this, EntityHuman.class, 20, true, true, (Predicate) null);
        }

        @Override
        public boolean a() {
            if (EntityPolarBear.this.isBaby()) {
                return false;
            } else {
                if (super.a()) {
                    List<EntityPolarBear> list = EntityPolarBear.this.level.a(EntityPolarBear.class, EntityPolarBear.this.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityPolarBear entitypolarbear = (EntityPolarBear) iterator.next();

                        if (entitypolarbear.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        @Override
        protected double k() {
            return super.k() * 0.5D;
        }
    }
}
