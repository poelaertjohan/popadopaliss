package net.minecraft.world.entity.monster;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.MathHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import net.minecraft.world.entity.raid.EntityRaider;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityEvoker extends EntityIllagerWizard {

    private EntitySheep wololoTarget;

    public EntityEvoker(EntityTypes<? extends EntityEvoker> entitytypes, World world) {
        super(entitytypes, world);
        this.xpReward = 10;
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityEvoker.b());
        this.goalSelector.a(2, new PathfinderGoalAvoidTarget<>(this, EntityHuman.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.a(4, new EntityEvoker.c());
        this.goalSelector.a(5, new EntityEvoker.a());
        this.goalSelector.a(6, new EntityEvoker.d());
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a());
        this.targetSelector.a(2, (new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true)).a(300));
        this.targetSelector.a(3, (new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, false)).a(300));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, false));
    }

    public static AttributeProvider.Builder p() {
        return EntityMonster.fA().a(GenericAttributes.MOVEMENT_SPEED, 0.5D).a(GenericAttributes.FOLLOW_RANGE, 12.0D).a(GenericAttributes.MAX_HEALTH, 24.0D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
    }

    @Override
    public SoundEffect t() {
        return SoundEffects.EVOKER_CELEBRATE;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public boolean p(Entity entity) {
        return entity == null ? false : (entity == this ? true : (super.p(entity) ? true : (entity instanceof EntityVex ? this.p(((EntityVex) entity).p()) : (entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == EnumMonsterType.ILLAGER ? this.getScoreboardTeam() == null && entity.getScoreboardTeam() == null : false))));
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.EVOKER_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.EVOKER_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.EVOKER_HURT;
    }

    void a(@Nullable EntitySheep entitysheep) {
        this.wololoTarget = entitysheep;
    }

    @Nullable
    EntitySheep fO() {
        return this.wololoTarget;
    }

    @Override
    protected SoundEffect getSoundCastSpell() {
        return SoundEffects.EVOKER_CAST_SPELL;
    }

    @Override
    public void a(int i, boolean flag) {}

    private class b extends EntityIllagerWizard.b {

        b() {
            super();
        }

        @Override
        public void e() {
            if (EntityEvoker.this.getGoalTarget() != null) {
                EntityEvoker.this.getControllerLook().a(EntityEvoker.this.getGoalTarget(), (float) EntityEvoker.this.eZ(), (float) EntityEvoker.this.eY());
            } else if (EntityEvoker.this.fO() != null) {
                EntityEvoker.this.getControllerLook().a(EntityEvoker.this.fO(), (float) EntityEvoker.this.eZ(), (float) EntityEvoker.this.eY());
            }

        }
    }

    private class c extends EntityIllagerWizard.PathfinderGoalCastSpell {

        private final PathfinderTargetCondition vexCountTargeting = PathfinderTargetCondition.b().a(16.0D).d().e();

        c() {
            super();
        }

        @Override
        public boolean a() {
            if (!super.a()) {
                return false;
            } else {
                int i = EntityEvoker.this.level.a(EntityVex.class, this.vexCountTargeting, (EntityLiving) EntityEvoker.this, EntityEvoker.this.getBoundingBox().g(16.0D)).size();

                return EntityEvoker.this.random.nextInt(8) + 1 > i;
            }
        }

        @Override
        protected int g() {
            return 100;
        }

        @Override
        protected int h() {
            return 340;
        }

        @Override
        protected void j() {
            WorldServer worldserver = (WorldServer) EntityEvoker.this.level;

            for (int i = 0; i < 3; ++i) {
                BlockPosition blockposition = EntityEvoker.this.getChunkCoordinates().c(-2 + EntityEvoker.this.random.nextInt(5), 1, -2 + EntityEvoker.this.random.nextInt(5));
                EntityVex entityvex = (EntityVex) EntityTypes.VEX.a(EntityEvoker.this.level);

                entityvex.setPositionRotation(blockposition, 0.0F, 0.0F);
                entityvex.prepare(worldserver, EntityEvoker.this.level.getDamageScaler(blockposition), EnumMobSpawn.MOB_SUMMONED, (GroupDataEntity) null, (NBTTagCompound) null);
                entityvex.a((EntityInsentient) EntityEvoker.this);
                entityvex.g(blockposition);
                entityvex.a(20 * (30 + EntityEvoker.this.random.nextInt(90)));
                worldserver.addAllEntities(entityvex);
            }

        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.SUMMON_VEX;
        }
    }

    private class a extends EntityIllagerWizard.PathfinderGoalCastSpell {

        a() {
            super();
        }

        @Override
        protected int g() {
            return 40;
        }

        @Override
        protected int h() {
            return 100;
        }

        @Override
        protected void j() {
            EntityLiving entityliving = EntityEvoker.this.getGoalTarget();
            double d0 = Math.min(entityliving.locY(), EntityEvoker.this.locY());
            double d1 = Math.max(entityliving.locY(), EntityEvoker.this.locY()) + 1.0D;
            float f = (float) MathHelper.d(entityliving.locZ() - EntityEvoker.this.locZ(), entityliving.locX() - EntityEvoker.this.locX());
            int i;

            if (EntityEvoker.this.f((Entity) entityliving) < 9.0D) {
                float f1;

                for (i = 0; i < 5; ++i) {
                    f1 = f + (float) i * 3.1415927F * 0.4F;
                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f1) * 1.5D, EntityEvoker.this.locZ() + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (i = 0; i < 8; ++i) {
                    f1 = f + (float) i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f1) * 2.5D, EntityEvoker.this.locZ() + (double) MathHelper.sin(f1) * 2.5D, d0, d1, f1, 3);
                }
            } else {
                for (i = 0; i < 16; ++i) {
                    double d2 = 1.25D * (double) (i + 1);
                    int j = 1 * i;

                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f) * d2, EntityEvoker.this.locZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }

        }

        private void a(double d0, double d1, double d2, double d3, float f, int i) {
            BlockPosition blockposition = new BlockPosition(d0, d3, d1);
            boolean flag = false;
            double d4 = 0.0D;

            do {
                BlockPosition blockposition1 = blockposition.down();
                IBlockData iblockdata = EntityEvoker.this.level.getType(blockposition1);

                if (iblockdata.d(EntityEvoker.this.level, blockposition1, EnumDirection.UP)) {
                    if (!EntityEvoker.this.level.isEmpty(blockposition)) {
                        IBlockData iblockdata1 = EntityEvoker.this.level.getType(blockposition);
                        VoxelShape voxelshape = iblockdata1.getCollisionShape(EntityEvoker.this.level, blockposition);

                        if (!voxelshape.isEmpty()) {
                            d4 = voxelshape.c(EnumDirection.EnumAxis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockposition = blockposition.down();
            } while (blockposition.getY() >= MathHelper.floor(d2) - 1);

            if (flag) {
                EntityEvoker.this.level.addEntity(new EntityEvokerFangs(EntityEvoker.this.level, d0, (double) blockposition.getY() + d4, d1, f, i, EntityEvoker.this));
            }

        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.FANGS;
        }
    }

    public class d extends EntityIllagerWizard.PathfinderGoalCastSpell {

        private final PathfinderTargetCondition wololoTargeting = PathfinderTargetCondition.b().a(16.0D).a((entityliving) -> {
            return ((EntitySheep) entityliving).getColor() == EnumColor.BLUE;
        });

        public d() {
            super();
        }

        @Override
        public boolean a() {
            if (EntityEvoker.this.getGoalTarget() != null) {
                return false;
            } else if (EntityEvoker.this.fF()) {
                return false;
            } else if (EntityEvoker.this.tickCount < this.nextAttackTickCount) {
                return false;
            } else if (!EntityEvoker.this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                return false;
            } else {
                List<EntitySheep> list = EntityEvoker.this.level.a(EntitySheep.class, this.wololoTargeting, (EntityLiving) EntityEvoker.this, EntityEvoker.this.getBoundingBox().grow(16.0D, 4.0D, 16.0D));

                if (list.isEmpty()) {
                    return false;
                } else {
                    EntityEvoker.this.a((EntitySheep) list.get(EntityEvoker.this.random.nextInt(list.size())));
                    return true;
                }
            }
        }

        @Override
        public boolean b() {
            return EntityEvoker.this.fO() != null && this.attackWarmupDelay > 0;
        }

        @Override
        public void d() {
            super.d();
            EntityEvoker.this.a((EntitySheep) null);
        }

        @Override
        protected void j() {
            EntitySheep entitysheep = EntityEvoker.this.fO();

            if (entitysheep != null && entitysheep.isAlive()) {
                entitysheep.setColor(EnumColor.RED);
            }

        }

        @Override
        protected int m() {
            return 40;
        }

        @Override
        protected int g() {
            return 60;
        }

        @Override
        protected int h() {
            return 140;
        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.EVOKER_PREPARE_WOLOLO;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.WOLOLO;
        }
    }
}
