package net.minecraft.world.entity.monster;

import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemProjectileWeapon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;

public abstract class EntityMonster extends EntityCreature implements IMonster {

    protected EntityMonster(EntityTypes<? extends EntityMonster> entitytypes, World world) {
        super(entitytypes, world);
        this.xpReward = 5;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    public void movementTick() {
        this.ei();
        this.fz();
        super.movementTick();
    }

    protected void fz() {
        float f = this.aY();

        if (f > 0.5F) {
            this.noActionTime += 2;
        }

    }

    @Override
    protected boolean Q() {
        return true;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.HOSTILE_SWIM;
    }

    @Override
    protected SoundEffect getSoundSplash() {
        return SoundEffects.HOSTILE_SPLASH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.HOSTILE_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.HOSTILE_DEATH;
    }

    @Override
    protected SoundEffect getSoundFall(int i) {
        return i > 4 ? SoundEffects.HOSTILE_BIG_FALL : SoundEffects.HOSTILE_SMALL_FALL;
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.5F - iworldreader.z(blockposition);
    }

    public static boolean a(WorldAccess worldaccess, BlockPosition blockposition, Random random) {
        if (worldaccess.getBrightness(EnumSkyBlock.SKY, blockposition) > random.nextInt(32)) {
            return false;
        } else {
            int i = worldaccess.getLevel().Y() ? worldaccess.c(blockposition, 10) : worldaccess.getLightLevel(blockposition);

            return i <= random.nextInt(8);
        }
    }

    public static boolean b(EntityTypes<? extends EntityMonster> entitytypes, WorldAccess worldaccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return worldaccess.getDifficulty() != EnumDifficulty.PEACEFUL && a(worldaccess, blockposition, random) && a(entitytypes, (GeneratorAccess) worldaccess, enummobspawn, blockposition, random);
    }

    public static boolean c(EntityTypes<? extends EntityMonster> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return generatoraccess.getDifficulty() != EnumDifficulty.PEACEFUL && a(entitytypes, generatoraccess, enummobspawn, blockposition, random);
    }

    public static AttributeProvider.Builder fA() {
        return EntityInsentient.w().a(GenericAttributes.ATTACK_DAMAGE);
    }

    @Override
    protected boolean isDropExperience() {
        return true;
    }

    @Override
    protected boolean dD() {
        return true;
    }

    public boolean f(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public ItemStack h(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemProjectileWeapon) {
            Predicate<ItemStack> predicate = ((ItemProjectileWeapon) itemstack.getItem()).e();
            ItemStack itemstack1 = ItemProjectileWeapon.a((EntityLiving) this, predicate);

            return itemstack1.isEmpty() ? new ItemStack(Items.ARROW) : itemstack1;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
