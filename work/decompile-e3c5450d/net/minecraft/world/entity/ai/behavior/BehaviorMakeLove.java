package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.PacketDebug;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.BehaviorController;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.VillagePlaceType;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.level.pathfinder.PathEntity;

public class BehaviorMakeLove extends Behavior<EntityVillager> {

    private static final int INTERACT_DIST_SQR = 5;
    private static final float SPEED_MODIFIER = 0.5F;
    private long birthTimestamp;

    public BehaviorMakeLove() {
        super(ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
    }

    protected boolean a(WorldServer worldserver, EntityVillager entityvillager) {
        return this.a(entityvillager);
    }

    protected boolean b(WorldServer worldserver, EntityVillager entityvillager, long i) {
        return i <= this.birthTimestamp && this.a(entityvillager);
    }

    protected void a(WorldServer worldserver, EntityVillager entityvillager, long i) {
        EntityAgeable entityageable = (EntityAgeable) entityvillager.getBehaviorController().getMemory(MemoryModuleType.BREED_TARGET).get();

        BehaviorUtil.a(entityvillager, entityageable, 0.5F);
        worldserver.broadcastEntityEffect(entityageable, (byte) 18);
        worldserver.broadcastEntityEffect(entityvillager, (byte) 18);
        int j = 275 + entityvillager.getRandom().nextInt(50);

        this.birthTimestamp = i + (long) j;
    }

    protected void d(WorldServer worldserver, EntityVillager entityvillager, long i) {
        EntityVillager entityvillager1 = (EntityVillager) entityvillager.getBehaviorController().getMemory(MemoryModuleType.BREED_TARGET).get();

        if (entityvillager.f((Entity) entityvillager1) <= 5.0D) {
            BehaviorUtil.a(entityvillager, entityvillager1, 0.5F);
            if (i >= this.birthTimestamp) {
                entityvillager.fO();
                entityvillager1.fO();
                this.a(worldserver, entityvillager, entityvillager1);
            } else if (entityvillager.getRandom().nextInt(35) == 0) {
                worldserver.broadcastEntityEffect(entityvillager1, (byte) 12);
                worldserver.broadcastEntityEffect(entityvillager, (byte) 12);
            }

        }
    }

    private void a(WorldServer worldserver, EntityVillager entityvillager, EntityVillager entityvillager1) {
        Optional<BlockPosition> optional = this.b(worldserver, entityvillager);

        if (!optional.isPresent()) {
            worldserver.broadcastEntityEffect(entityvillager1, (byte) 13);
            worldserver.broadcastEntityEffect(entityvillager, (byte) 13);
        } else {
            Optional<EntityVillager> optional1 = this.b(worldserver, entityvillager, entityvillager1);

            if (optional1.isPresent()) {
                this.a(worldserver, (EntityVillager) optional1.get(), (BlockPosition) optional.get());
            } else {
                worldserver.A().b((BlockPosition) optional.get());
                PacketDebug.c(worldserver, (BlockPosition) optional.get());
            }
        }

    }

    protected void c(WorldServer worldserver, EntityVillager entityvillager, long i) {
        entityvillager.getBehaviorController().removeMemory(MemoryModuleType.BREED_TARGET);
    }

    private boolean a(EntityVillager entityvillager) {
        BehaviorController<EntityVillager> behaviorcontroller = entityvillager.getBehaviorController();
        Optional<EntityAgeable> optional = behaviorcontroller.getMemory(MemoryModuleType.BREED_TARGET).filter((entityageable) -> {
            return entityageable.getEntityType() == EntityTypes.VILLAGER;
        });

        return !optional.isPresent() ? false : BehaviorUtil.a(behaviorcontroller, MemoryModuleType.BREED_TARGET, EntityTypes.VILLAGER) && entityvillager.canBreed() && ((EntityAgeable) optional.get()).canBreed();
    }

    private Optional<BlockPosition> b(WorldServer worldserver, EntityVillager entityvillager) {
        return worldserver.A().a(VillagePlaceType.HOME.c(), (blockposition) -> {
            return this.a(entityvillager, blockposition);
        }, entityvillager.getChunkCoordinates(), 48);
    }

    private boolean a(EntityVillager entityvillager, BlockPosition blockposition) {
        PathEntity pathentity = entityvillager.getNavigation().a(blockposition, VillagePlaceType.HOME.d());

        return pathentity != null && pathentity.j();
    }

    private Optional<EntityVillager> b(WorldServer worldserver, EntityVillager entityvillager, EntityVillager entityvillager1) {
        EntityVillager entityvillager2 = entityvillager.createChild(worldserver, entityvillager1);

        if (entityvillager2 == null) {
            return Optional.empty();
        } else {
            entityvillager.setAgeRaw(6000);
            entityvillager1.setAgeRaw(6000);
            entityvillager2.setAgeRaw(-24000);
            entityvillager2.setPositionRotation(entityvillager.locX(), entityvillager.locY(), entityvillager.locZ(), 0.0F, 0.0F);
            worldserver.addAllEntities(entityvillager2);
            worldserver.broadcastEntityEffect(entityvillager2, (byte) 12);
            return Optional.of(entityvillager2);
        }
    }

    private void a(WorldServer worldserver, EntityVillager entityvillager, BlockPosition blockposition) {
        GlobalPos globalpos = GlobalPos.create(worldserver.getDimensionKey(), blockposition);

        entityvillager.getBehaviorController().setMemory(MemoryModuleType.HOME, (Object) globalpos);
    }
}
