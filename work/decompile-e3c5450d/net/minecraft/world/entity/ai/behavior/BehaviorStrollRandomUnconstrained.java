package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3D;

public class BehaviorStrollRandomUnconstrained extends Behavior<EntityCreature> {

    private static final int MAX_XZ_DIST = 10;
    private static final int MAX_Y_DIST = 7;
    private final float speedModifier;
    protected final int maxHorizontalDistance;
    protected final int maxVerticalDistance;

    public BehaviorStrollRandomUnconstrained(float f) {
        this(f, 10, 7);
    }

    public BehaviorStrollRandomUnconstrained(float f, int i, int j) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = f;
        this.maxHorizontalDistance = i;
        this.maxVerticalDistance = j;
    }

    protected void a(WorldServer worldserver, EntityCreature entitycreature, long i) {
        Optional<Vec3D> optional = Optional.ofNullable(this.a(entitycreature));

        entitycreature.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, optional.map((vec3d) -> {
            return new MemoryTarget(vec3d, this.speedModifier, 0);
        }));
    }

    @Nullable
    protected Vec3D a(EntityCreature entitycreature) {
        return LandRandomPos.a(entitycreature, this.maxHorizontalDistance, this.maxVerticalDistance);
    }
}
