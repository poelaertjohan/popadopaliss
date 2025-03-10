package net.minecraft.core.dispenser;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.IPosition;
import net.minecraft.core.ISourceBlock;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagsBlock;
import net.minecraft.tags.TagsFluid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.ISaddleable;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.animal.horse.EntityHorseChestedAbstract;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.entity.projectile.EntitySmallFireball;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.projectile.EntitySpectralArrow;
import net.minecraft.world.entity.projectile.EntityThrownExpBottle;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.entity.projectile.IProjectile;
import net.minecraft.world.entity.vehicle.EntityBoat;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemArmor;
import net.minecraft.world.item.ItemBoneMeal;
import net.minecraft.world.item.ItemMonsterEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtil;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.IMaterial;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockBeehive;
import net.minecraft.world.level.block.BlockCampfire;
import net.minecraft.world.level.block.BlockDispenser;
import net.minecraft.world.level.block.BlockFireAbstract;
import net.minecraft.world.level.block.BlockPumpkinCarved;
import net.minecraft.world.level.block.BlockRespawnAnchor;
import net.minecraft.world.level.block.BlockShulkerBox;
import net.minecraft.world.level.block.BlockSkull;
import net.minecraft.world.level.block.BlockTNT;
import net.minecraft.world.level.block.BlockWitherSkull;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.IFluidSource;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityBeehive;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface IDispenseBehavior {

    Logger LOGGER = LogManager.getLogger();
    IDispenseBehavior NOOP = (isourceblock, itemstack) -> {
        return itemstack;
    };

    ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack);

    static void c() {
        BlockDispenser.a((IMaterial) Items.ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.pickup = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.TIPPED_ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.a(itemstack);
                entitytippedarrow.pickup = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SPECTRAL_ARROW, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entityspectralarrow.pickup = EntityArrow.PickupStatus.ALLOWED;
                return entityspectralarrow;
            }
        }));
        BlockDispenser.a((IMaterial) Items.EGG, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((Object) (new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entityegg) -> {
                    entityegg.setItem(itemstack);
                });
            }
        }));
        BlockDispenser.a((IMaterial) Items.SNOWBALL, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((Object) (new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitysnowball) -> {
                    entitysnowball.setItem(itemstack);
                });
            }
        }));
        BlockDispenser.a((IMaterial) Items.EXPERIENCE_BOTTLE, (IDispenseBehavior) (new DispenseBehaviorProjectile() {
            @Override
            protected IProjectile a(World world, IPosition iposition, ItemStack itemstack) {
                return (IProjectile) SystemUtils.a((Object) (new EntityThrownExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitythrownexpbottle) -> {
                    entitythrownexpbottle.setItem(itemstack);
                });
            }

            @Override
            protected float a() {
                return super.a() * 0.5F;
            }

            @Override
            protected float getPower() {
                return super.getPower() * 1.25F;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SPLASH_POTION, new IDispenseBehavior() {
            @Override
            public ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack) {
                return (new DispenseBehaviorProjectile() {
                    @Override
                    protected IProjectile a(World world, IPosition iposition, ItemStack itemstack1) {
                        return (IProjectile) SystemUtils.a((Object) (new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitypotion) -> {
                            entitypotion.setItem(itemstack1);
                        });
                    }

                    @Override
                    protected float a() {
                        return super.a() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        BlockDispenser.a((IMaterial) Items.LINGERING_POTION, new IDispenseBehavior() {
            @Override
            public ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack) {
                return (new DispenseBehaviorProjectile() {
                    @Override
                    protected IProjectile a(World world, IPosition iposition, ItemStack itemstack1) {
                        return (IProjectile) SystemUtils.a((Object) (new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ())), (entitypotion) -> {
                            entitypotion.setItem(itemstack1);
                        });
                    }

                    @Override
                    protected float a() {
                        return super.a() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        DispenseBehaviorItem dispensebehavioritem = new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                EntityTypes entitytypes = ((ItemMonsterEgg) itemstack.getItem()).a(itemstack.getTag());

                try {
                    entitytypes.spawnCreature(isourceblock.getWorld(), itemstack, (EntityHuman) null, isourceblock.getBlockPosition().shift(enumdirection), EnumMobSpawn.DISPENSER, enumdirection != EnumDirection.UP, false);
                } catch (Exception exception) {
                    null.LOGGER.error("Error while dispensing spawn egg from dispenser at {}", isourceblock.getBlockPosition(), exception);
                    return ItemStack.EMPTY;
                }

                itemstack.subtract(1);
                isourceblock.getWorld().a(GameEvent.ENTITY_PLACE, isourceblock.getBlockPosition());
                return itemstack;
            }
        };
        Iterator iterator = ItemMonsterEgg.i().iterator();

        while (iterator.hasNext()) {
            ItemMonsterEgg itemmonsteregg = (ItemMonsterEgg) iterator.next();

            BlockDispenser.a((IMaterial) itemmonsteregg, (IDispenseBehavior) dispensebehavioritem);
        }

        BlockDispenser.a((IMaterial) Items.ARMOR_STAND, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                WorldServer worldserver = isourceblock.getWorld();
                EntityArmorStand entityarmorstand = new EntityArmorStand(worldserver, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D);

                EntityTypes.a((World) worldserver, (EntityHuman) null, (Entity) entityarmorstand, itemstack.getTag());
                entityarmorstand.setYRot(enumdirection.o());
                worldserver.addEntity(entityarmorstand);
                itemstack.subtract(1);
                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.SADDLE, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityLiving> list = isourceblock.getWorld().a(EntityLiving.class, new AxisAlignedBB(blockposition), (entityliving) -> {
                    if (!(entityliving instanceof ISaddleable)) {
                        return false;
                    } else {
                        ISaddleable isaddleable = (ISaddleable) entityliving;

                        return !isaddleable.hasSaddle() && isaddleable.canSaddle();
                    }
                });

                if (!list.isEmpty()) {
                    ((ISaddleable) list.get(0)).saddle(SoundCategory.BLOCKS);
                    itemstack.subtract(1);
                    this.a(true);
                    return itemstack;
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        DispenseBehaviorMaybe dispensebehaviormaybe = new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityHorseAbstract> list = isourceblock.getWorld().a(EntityHorseAbstract.class, new AxisAlignedBB(blockposition), (entityhorseabstract) -> {
                    return entityhorseabstract.isAlive() && entityhorseabstract.gb();
                });
                Iterator iterator1 = list.iterator();

                EntityHorseAbstract entityhorseabstract;

                do {
                    if (!iterator1.hasNext()) {
                        return super.a(isourceblock, itemstack);
                    }

                    entityhorseabstract = (EntityHorseAbstract) iterator1.next();
                } while (!entityhorseabstract.m(itemstack) || entityhorseabstract.gc() || !entityhorseabstract.isTamed());

                entityhorseabstract.k(401).a(itemstack.cloneAndSubtract(1));
                this.a(true);
                return itemstack;
            }
        };

        BlockDispenser.a((IMaterial) Items.LEATHER_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.IRON_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.GOLDEN_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.DIAMOND_HORSE_ARMOR, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.WHITE_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.ORANGE_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.CYAN_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.BLUE_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.BROWN_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.BLACK_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.GRAY_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.GREEN_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.LIGHT_BLUE_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.LIGHT_GRAY_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.LIME_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.MAGENTA_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.PINK_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.PURPLE_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.RED_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.YELLOW_CARPET, (IDispenseBehavior) dispensebehaviormaybe);
        BlockDispenser.a((IMaterial) Items.CHEST, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                List<EntityHorseChestedAbstract> list = isourceblock.getWorld().a(EntityHorseChestedAbstract.class, new AxisAlignedBB(blockposition), (entityhorsechestedabstract) -> {
                    return entityhorsechestedabstract.isAlive() && !entityhorsechestedabstract.isCarryingChest();
                });
                Iterator iterator1 = list.iterator();

                EntityHorseChestedAbstract entityhorsechestedabstract;

                do {
                    if (!iterator1.hasNext()) {
                        return super.a(isourceblock, itemstack);
                    }

                    entityhorsechestedabstract = (EntityHorseChestedAbstract) iterator1.next();
                } while (!entityhorsechestedabstract.isTamed() || !entityhorsechestedabstract.k(499).a(itemstack));

                itemstack.subtract(1);
                this.a(true);
                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.FIREWORK_ROCKET, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                EntityFireworks entityfireworks = new EntityFireworks(isourceblock.getWorld(), itemstack, isourceblock.getX(), isourceblock.getY(), isourceblock.getX(), true);

                IDispenseBehavior.a(isourceblock, entityfireworks, enumdirection);
                entityfireworks.shoot((double) enumdirection.getAdjacentX(), (double) enumdirection.getAdjacentY(), (double) enumdirection.getAdjacentZ(), 0.5F, 1.0F);
                isourceblock.getWorld().addEntity(entityfireworks);
                itemstack.subtract(1);
                return itemstack;
            }

            @Override
            protected void a(ISourceBlock isourceblock) {
                isourceblock.getWorld().triggerEffect(1004, isourceblock.getBlockPosition(), 0);
            }
        }));
        BlockDispenser.a((IMaterial) Items.FIRE_CHARGE, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.a(isourceblock);
                double d0 = iposition.getX() + (double) ((float) enumdirection.getAdjacentX() * 0.3F);
                double d1 = iposition.getY() + (double) ((float) enumdirection.getAdjacentY() * 0.3F);
                double d2 = iposition.getZ() + (double) ((float) enumdirection.getAdjacentZ() * 0.3F);
                WorldServer worldserver = isourceblock.getWorld();
                Random random = worldserver.random;
                double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentX();
                double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentY();
                double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getAdjacentZ();
                EntitySmallFireball entitysmallfireball = new EntitySmallFireball(worldserver, d0, d1, d2, d3, d4, d5);

                worldserver.addEntity((Entity) SystemUtils.a((Object) entitysmallfireball, (entitysmallfireball1) -> {
                    entitysmallfireball1.setItem(itemstack);
                }));
                itemstack.subtract(1);
                return itemstack;
            }

            @Override
            protected void a(ISourceBlock isourceblock) {
                isourceblock.getWorld().triggerEffect(1018, isourceblock.getBlockPosition(), 0);
            }
        }));
        BlockDispenser.a((IMaterial) Items.OAK_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.OAK)));
        BlockDispenser.a((IMaterial) Items.SPRUCE_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.SPRUCE)));
        BlockDispenser.a((IMaterial) Items.BIRCH_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.BIRCH)));
        BlockDispenser.a((IMaterial) Items.JUNGLE_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.JUNGLE)));
        BlockDispenser.a((IMaterial) Items.DARK_OAK_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.DARK_OAK)));
        BlockDispenser.a((IMaterial) Items.ACACIA_BOAT, (IDispenseBehavior) (new DispenseBehaviorBoat(EntityBoat.EnumBoatType.ACACIA)));
        DispenseBehaviorItem dispensebehavioritem1 = new DispenseBehaviorItem() {
            private final DispenseBehaviorItem defaultDispenseItemBehavior = new DispenseBehaviorItem();

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) itemstack.getItem();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                WorldServer worldserver = isourceblock.getWorld();

                if (dispensiblecontaineritem.a((EntityHuman) null, worldserver, blockposition, (MovingObjectPositionBlock) null)) {
                    dispensiblecontaineritem.a((EntityHuman) null, worldserver, itemstack, blockposition);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(isourceblock, itemstack);
                }
            }
        };

        BlockDispenser.a((IMaterial) Items.LAVA_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.WATER_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.POWDER_SNOW_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.SALMON_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.COD_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.PUFFERFISH_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.TROPICAL_FISH_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.AXOLOTL_BUCKET, (IDispenseBehavior) dispensebehavioritem1);
        BlockDispenser.a((IMaterial) Items.BUCKET, (IDispenseBehavior) (new DispenseBehaviorItem() {
            private final DispenseBehaviorItem defaultDispenseItemBehavior = new DispenseBehaviorItem();

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                IBlockData iblockdata = worldserver.getType(blockposition);
                Block block = iblockdata.getBlock();

                if (block instanceof IFluidSource) {
                    ItemStack itemstack1 = ((IFluidSource) block).removeFluid(worldserver, blockposition, iblockdata);

                    if (itemstack1.isEmpty()) {
                        return super.a(isourceblock, itemstack);
                    } else {
                        worldserver.a((Entity) null, GameEvent.FLUID_PICKUP, blockposition);
                        Item item = itemstack1.getItem();

                        itemstack.subtract(1);
                        if (itemstack.isEmpty()) {
                            return new ItemStack(item);
                        } else {
                            if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
                                this.defaultDispenseItemBehavior.dispense(isourceblock, new ItemStack(item));
                            }

                            return itemstack;
                        }
                    }
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.FLINT_AND_STEEL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();

                this.a(true);
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                IBlockData iblockdata = worldserver.getType(blockposition);

                if (BlockFireAbstract.a((World) worldserver, blockposition, enumdirection)) {
                    worldserver.setTypeUpdate(blockposition, BlockFireAbstract.a((IBlockAccess) worldserver, blockposition));
                    worldserver.a((Entity) null, GameEvent.BLOCK_PLACE, blockposition);
                } else if (!BlockCampfire.h(iblockdata) && !CandleBlock.g(iblockdata) && !CandleCakeBlock.g(iblockdata)) {
                    if (iblockdata.getBlock() instanceof BlockTNT) {
                        BlockTNT.a((World) worldserver, blockposition);
                        worldserver.a(blockposition, false);
                    } else {
                        this.a(false);
                    }
                } else {
                    worldserver.setTypeUpdate(blockposition, (IBlockData) iblockdata.set(BlockProperties.LIT, true));
                    worldserver.a((Entity) null, GameEvent.BLOCK_CHANGE, blockposition);
                }

                if (this.a() && itemstack.isDamaged(1, worldserver.random, (EntityPlayer) null)) {
                    itemstack.setCount(0);
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Items.BONE_MEAL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(true);
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));

                if (!ItemBoneMeal.a(itemstack, (World) worldserver, blockposition) && !ItemBoneMeal.a(itemstack, (World) worldserver, blockposition, (EnumDirection) null)) {
                    this.a(false);
                } else if (!worldserver.isClientSide) {
                    worldserver.triggerEffect(1505, blockposition, 0);
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.TNT, (IDispenseBehavior) (new DispenseBehaviorItem() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(worldserver, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, (EntityLiving) null);

                worldserver.addEntity(entitytntprimed);
                worldserver.playSound((EntityHuman) null, entitytntprimed.locX(), entitytntprimed.locY(), entitytntprimed.locZ(), SoundEffects.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldserver.a((Entity) null, GameEvent.ENTITY_PLACE, blockposition);
                itemstack.subtract(1);
                return itemstack;
            }
        }));
        DispenseBehaviorMaybe dispensebehaviormaybe1 = new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(ItemArmor.a(isourceblock, itemstack));
                return itemstack;
            }
        };

        BlockDispenser.a((IMaterial) Items.CREEPER_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.ZOMBIE_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.DRAGON_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.SKELETON_SKULL, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.PLAYER_HEAD, (IDispenseBehavior) dispensebehaviormaybe1);
        BlockDispenser.a((IMaterial) Items.WITHER_SKELETON_SKULL, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);

                if (worldserver.isEmpty(blockposition) && BlockWitherSkull.b((World) worldserver, blockposition, itemstack)) {
                    worldserver.setTypeAndData(blockposition, (IBlockData) Blocks.WITHER_SKELETON_SKULL.getBlockData().set(BlockSkull.ROTATION, enumdirection.n() == EnumDirection.EnumAxis.Y ? 0 : enumdirection.opposite().get2DRotationValue() * 4), 3);
                    worldserver.a((Entity) null, GameEvent.BLOCK_PLACE, blockposition);
                    TileEntity tileentity = worldserver.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntitySkull) {
                        BlockWitherSkull.a((World) worldserver, blockposition, (TileEntitySkull) tileentity);
                    }

                    itemstack.subtract(1);
                    this.a(true);
                } else {
                    this.a(ItemArmor.a(isourceblock, itemstack));
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.CARVED_PUMPKIN, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                BlockPumpkinCarved blockpumpkincarved = (BlockPumpkinCarved) Blocks.CARVED_PUMPKIN;

                if (worldserver.isEmpty(blockposition) && blockpumpkincarved.a((IWorldReader) worldserver, blockposition)) {
                    if (!worldserver.isClientSide) {
                        worldserver.setTypeAndData(blockposition, blockpumpkincarved.getBlockData(), 3);
                        worldserver.a((Entity) null, GameEvent.BLOCK_PLACE, blockposition);
                    }

                    itemstack.subtract(1);
                    this.a(true);
                } else {
                    this.a(ItemArmor.a(isourceblock, itemstack));
                }

                return itemstack;
            }
        }));
        BlockDispenser.a((IMaterial) Blocks.SHULKER_BOX.getItem(), (IDispenseBehavior) (new DispenseBehaviorShulkerBox()));
        EnumColor[] aenumcolor = EnumColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumColor enumcolor = aenumcolor[j];

            BlockDispenser.a((IMaterial) BlockShulkerBox.a(enumcolor).getItem(), (IDispenseBehavior) (new DispenseBehaviorShulkerBox()));
        }

        BlockDispenser.a((IMaterial) Items.GLASS_BOTTLE.getItem(), (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            private final DispenseBehaviorItem defaultDispenseItemBehavior = new DispenseBehaviorItem();

            private ItemStack a(ISourceBlock isourceblock, ItemStack itemstack, ItemStack itemstack1) {
                itemstack.subtract(1);
                if (itemstack.isEmpty()) {
                    isourceblock.getWorld().a((Entity) null, GameEvent.FLUID_PICKUP, isourceblock.getBlockPosition());
                    return itemstack1.cloneItemStack();
                } else {
                    if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(itemstack1.cloneItemStack()) < 0) {
                        this.defaultDispenseItemBehavior.dispense(isourceblock, itemstack1.cloneItemStack());
                    }

                    return itemstack;
                }
            }

            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                this.a(false);
                WorldServer worldserver = isourceblock.getWorld();
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                IBlockData iblockdata = worldserver.getType(blockposition);

                if (iblockdata.a((Tag) TagsBlock.BEEHIVES, (blockbase_blockdata) -> {
                    return blockbase_blockdata.b(BlockBeehive.HONEY_LEVEL);
                }) && (Integer) iblockdata.get(BlockBeehive.HONEY_LEVEL) >= 5) {
                    ((BlockBeehive) iblockdata.getBlock()).a((World) worldserver, iblockdata, blockposition, (EntityHuman) null, TileEntityBeehive.ReleaseStatus.BEE_RELEASED);
                    this.a(true);
                    return this.a(isourceblock, itemstack, new ItemStack(Items.HONEY_BOTTLE));
                } else if (worldserver.getFluid(blockposition).a((Tag) TagsFluid.WATER)) {
                    this.a(true);
                    return this.a(isourceblock, itemstack, PotionUtil.a(new ItemStack(Items.POTION), Potions.WATER));
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.GLOWSTONE, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
                BlockPosition blockposition = isourceblock.getBlockPosition().shift(enumdirection);
                WorldServer worldserver = isourceblock.getWorld();
                IBlockData iblockdata = worldserver.getType(blockposition);

                this.a(true);
                if (iblockdata.a(Blocks.RESPAWN_ANCHOR)) {
                    if ((Integer) iblockdata.get(BlockRespawnAnchor.CHARGE) != 4) {
                        BlockRespawnAnchor.a((World) worldserver, blockposition, iblockdata);
                        itemstack.subtract(1);
                    } else {
                        this.a(false);
                    }

                    return itemstack;
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
        BlockDispenser.a((IMaterial) Items.SHEARS.getItem(), (IDispenseBehavior) (new DispenseBehaviorShears()));
        BlockDispenser.a((IMaterial) Items.HONEYCOMB, (IDispenseBehavior) (new DispenseBehaviorMaybe() {
            @Override
            public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
                BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
                WorldServer worldserver = isourceblock.getWorld();
                IBlockData iblockdata = worldserver.getType(blockposition);
                Optional<IBlockData> optional = HoneycombItem.b(iblockdata);

                if (optional.isPresent()) {
                    worldserver.setTypeUpdate(blockposition, (IBlockData) optional.get());
                    worldserver.triggerEffect(3003, blockposition, 0);
                    itemstack.subtract(1);
                    this.a(true);
                    return itemstack;
                } else {
                    return super.a(isourceblock, itemstack);
                }
            }
        }));
    }

    static void a(ISourceBlock isourceblock, Entity entity, EnumDirection enumdirection) {
        entity.setPosition(isourceblock.getX() + (double) enumdirection.getAdjacentX() * (0.5000099999997474D - (double) entity.getWidth() / 2.0D), isourceblock.getY() + (double) enumdirection.getAdjacentY() * (0.5000099999997474D - (double) entity.getHeight() / 2.0D) - (double) entity.getHeight() / 2.0D, isourceblock.getZ() + (double) enumdirection.getAdjacentZ() * (0.5000099999997474D - (double) entity.getWidth() / 2.0D));
    }
}
