package net.minecraft.world.entity.monster;

import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalBreakDoor;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.util.PathfinderGoalUtil;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.raid.EntityRaider;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;

public class EntityVindicator extends EntityIllagerAbstract {

    private static final String TAG_JOHNNY = "Johnny";
    static final Predicate<EnumDifficulty> DOOR_BREAKING_PREDICATE = (enumdifficulty) -> {
        return enumdifficulty == EnumDifficulty.NORMAL || enumdifficulty == EnumDifficulty.HARD;
    };
    boolean isJohnny;

    public EntityVindicator(EntityTypes<? extends EntityVindicator> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityVindicator.a(this));
        this.goalSelector.a(2, new EntityIllagerAbstract.b(this));
        this.goalSelector.a(3, new EntityRaider.a(this, 10.0F));
        this.goalSelector.a(4, new EntityVindicator.c(this));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a());
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
        this.targetSelector.a(4, new EntityVindicator.b(this));
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
    }

    @Override
    protected void mobTick() {
        if (!this.isNoAI() && PathfinderGoalUtil.a(this)) {
            boolean flag = ((WorldServer) this.level).d(this.getChunkCoordinates());

            ((Navigation) this.getNavigation()).a(flag);
        }

        super.mobTick();
    }

    public static AttributeProvider.Builder p() {
        return EntityMonster.fA().a(GenericAttributes.MOVEMENT_SPEED, 0.3499999940395355D).a(GenericAttributes.FOLLOW_RANGE, 12.0D).a(GenericAttributes.MAX_HEALTH, 24.0D).a(GenericAttributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.isJohnny) {
            nbttagcompound.setBoolean("Johnny", true);
        }

    }

    @Override
    public EntityIllagerAbstract.a n() {
        return this.isAggressive() ? EntityIllagerAbstract.a.ATTACKING : (this.fM() ? EntityIllagerAbstract.a.CELEBRATING : EntityIllagerAbstract.a.CROSSED);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("Johnny", 99)) {
            this.isJohnny = nbttagcompound.getBoolean("Johnny");
        }

    }

    @Override
    public SoundEffect t() {
        return SoundEffects.VINDICATOR_CELEBRATE;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        GroupDataEntity groupdataentity1 = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);

        ((Navigation) this.getNavigation()).a(true);
        this.a(difficultydamagescaler);
        this.b(difficultydamagescaler);
        return groupdataentity1;
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        if (this.fJ() == null) {
            this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }

    }

    @Override
    public boolean p(Entity entity) {
        return super.p(entity) ? true : (entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == EnumMonsterType.ILLAGER ? this.getScoreboardTeam() == null && entity.getScoreboardTeam() == null : false);
    }

    @Override
    public void setCustomName(@Nullable IChatBaseComponent ichatbasecomponent) {
        super.setCustomName(ichatbasecomponent);
        if (!this.isJohnny && ichatbasecomponent != null && ichatbasecomponent.getString().equals("Johnny")) {
            this.isJohnny = true;
        }

    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.VINDICATOR_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.VINDICATOR_HURT;
    }

    @Override
    public void a(int i, boolean flag) {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.fJ();
        byte b0 = 1;

        if (i > raid.a(EnumDifficulty.NORMAL)) {
            b0 = 2;
        }

        boolean flag1 = this.random.nextFloat() <= raid.w();

        if (flag1) {
            Map<Enchantment, Integer> map = Maps.newHashMap();

            map.put(Enchantments.SHARPNESS, Integer.valueOf(b0));
            EnchantmentManager.a((Map) map, itemstack);
        }

        this.setSlot(EnumItemSlot.MAINHAND, itemstack);
    }

    private static class a extends PathfinderGoalBreakDoor {

        public a(EntityInsentient entityinsentient) {
            super(entityinsentient, 6, EntityVindicator.DOOR_BREAKING_PREDICATE);
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean b() {
            EntityVindicator entityvindicator = (EntityVindicator) this.mob;

            return entityvindicator.fK() && super.b();
        }

        @Override
        public boolean a() {
            EntityVindicator entityvindicator = (EntityVindicator) this.mob;

            return entityvindicator.fK() && entityvindicator.random.nextInt(10) == 0 && super.a();
        }

        @Override
        public void c() {
            super.c();
            this.mob.o(0);
        }
    }

    private class c extends PathfinderGoalMeleeAttack {

        public c(EntityVindicator entityvindicator) {
            super(entityvindicator, 1.0D, false);
        }

        @Override
        protected double a(EntityLiving entityliving) {
            if (this.mob.getVehicle() instanceof EntityRavager) {
                float f = this.mob.getVehicle().getWidth() - 0.1F;

                return (double) (f * 2.0F * f * 2.0F + entityliving.getWidth());
            } else {
                return super.a(entityliving);
            }
        }
    }

    private static class b extends PathfinderGoalNearestAttackableTarget<EntityLiving> {

        public b(EntityVindicator entityvindicator) {
            super(entityvindicator, EntityLiving.class, 0, true, true, EntityLiving::eQ);
        }

        @Override
        public boolean a() {
            return ((EntityVindicator) this.mob).isJohnny && super.a();
        }

        @Override
        public void c() {
            super.c();
            this.mob.o(0);
        }
    }
}
