package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.level.World;

public class EntityHorseDonkey extends EntityHorseChestedAbstract {

    public EntityHorseDonkey(EntityTypes<? extends EntityHorseDonkey> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        super.getSoundAmbient();
        return SoundEffects.DONKEY_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundAngry() {
        super.getSoundAngry();
        return SoundEffects.DONKEY_ANGRY;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        super.getSoundDeath();
        return SoundEffects.DONKEY_DEATH;
    }

    @Nullable
    @Override
    protected SoundEffect fP() {
        return SoundEffects.DONKEY_EAT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        super.getSoundHurt(damagesource);
        return SoundEffects.DONKEY_HURT;
    }

    @Override
    public boolean mate(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (!(entityanimal instanceof EntityHorseDonkey) && !(entityanimal instanceof EntityHorse) ? false : this.fX() && ((EntityHorseAbstract) entityanimal).fX());
    }

    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntityTypes<? extends EntityHorseAbstract> entitytypes = entityageable instanceof EntityHorse ? EntityTypes.MULE : EntityTypes.DONKEY;
        EntityHorseAbstract entityhorseabstract = (EntityHorseAbstract) entitytypes.a((World) worldserver);

        this.a(entityageable, entityhorseabstract);
        return entityhorseabstract;
    }
}
