package net.minecraft.world.phys.shapes;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumAxisCycle;
import net.minecraft.core.EnumDirection;
import net.minecraft.util.MathHelper;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;

public abstract class VoxelShape {

    protected final VoxelShapeDiscrete shape;
    @Nullable
    private VoxelShape[] faces;

    VoxelShape(VoxelShapeDiscrete voxelshapediscrete) {
        this.shape = voxelshapediscrete;
    }

    public double b(EnumDirection.EnumAxis enumdirection_enumaxis) {
        int i = this.shape.a(enumdirection_enumaxis);

        return i >= this.shape.c(enumdirection_enumaxis) ? Double.POSITIVE_INFINITY : this.a(enumdirection_enumaxis, i);
    }

    public double c(EnumDirection.EnumAxis enumdirection_enumaxis) {
        int i = this.shape.b(enumdirection_enumaxis);

        return i <= 0 ? Double.NEGATIVE_INFINITY : this.a(enumdirection_enumaxis, i);
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.isEmpty()) {
            throw (UnsupportedOperationException) SystemUtils.c((Throwable) (new UnsupportedOperationException("No bounds for empty shape.")));
        } else {
            return new AxisAlignedBB(this.b(EnumDirection.EnumAxis.X), this.b(EnumDirection.EnumAxis.Y), this.b(EnumDirection.EnumAxis.Z), this.c(EnumDirection.EnumAxis.X), this.c(EnumDirection.EnumAxis.Y), this.c(EnumDirection.EnumAxis.Z));
        }
    }

    protected double a(EnumDirection.EnumAxis enumdirection_enumaxis, int i) {
        return this.a(enumdirection_enumaxis).getDouble(i);
    }

    protected abstract DoubleList a(EnumDirection.EnumAxis enumdirection_enumaxis);

    public boolean isEmpty() {
        return this.shape.a();
    }

    public VoxelShape a(double d0, double d1, double d2) {
        return (VoxelShape) (this.isEmpty() ? VoxelShapes.a() : new VoxelShapeArray(this.shape, new DoubleListOffset(this.a(EnumDirection.EnumAxis.X), d0), new DoubleListOffset(this.a(EnumDirection.EnumAxis.Y), d1), new DoubleListOffset(this.a(EnumDirection.EnumAxis.Z), d2)));
    }

    public VoxelShape c() {
        VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.a()};

        this.b((d0, d1, d2, d3, d4, d5) -> {
            avoxelshape[0] = VoxelShapes.b(avoxelshape[0], VoxelShapes.create(d0, d1, d2, d3, d4, d5), OperatorBoolean.OR);
        });
        return avoxelshape[0];
    }

    public void a(VoxelShapes.a voxelshapes_a) {
        this.shape.a((i, j, k, l, i1, j1) -> {
            voxelshapes_a.consume(this.a(EnumDirection.EnumAxis.X, i), this.a(EnumDirection.EnumAxis.Y, j), this.a(EnumDirection.EnumAxis.Z, k), this.a(EnumDirection.EnumAxis.X, l), this.a(EnumDirection.EnumAxis.Y, i1), this.a(EnumDirection.EnumAxis.Z, j1));
        }, true);
    }

    public void b(VoxelShapes.a voxelshapes_a) {
        DoubleList doublelist = this.a(EnumDirection.EnumAxis.X);
        DoubleList doublelist1 = this.a(EnumDirection.EnumAxis.Y);
        DoubleList doublelist2 = this.a(EnumDirection.EnumAxis.Z);

        this.shape.b((i, j, k, l, i1, j1) -> {
            voxelshapes_a.consume(doublelist.getDouble(i), doublelist1.getDouble(j), doublelist2.getDouble(k), doublelist.getDouble(l), doublelist1.getDouble(i1), doublelist2.getDouble(j1));
        }, true);
    }

    public List<AxisAlignedBB> d() {
        List<AxisAlignedBB> list = Lists.newArrayList();

        this.b((d0, d1, d2, d3, d4, d5) -> {
            list.add(new AxisAlignedBB(d0, d1, d2, d3, d4, d5));
        });
        return list;
    }

    public double a(EnumDirection.EnumAxis enumdirection_enumaxis, double d0, double d1) {
        EnumDirection.EnumAxis enumdirection_enumaxis1 = EnumAxisCycle.FORWARD.a(enumdirection_enumaxis);
        EnumDirection.EnumAxis enumdirection_enumaxis2 = EnumAxisCycle.BACKWARD.a(enumdirection_enumaxis);
        int i = this.a(enumdirection_enumaxis1, d0);
        int j = this.a(enumdirection_enumaxis2, d1);
        int k = this.shape.a(enumdirection_enumaxis, i, j);

        return k >= this.shape.c(enumdirection_enumaxis) ? Double.POSITIVE_INFINITY : this.a(enumdirection_enumaxis, k);
    }

    public double b(EnumDirection.EnumAxis enumdirection_enumaxis, double d0, double d1) {
        EnumDirection.EnumAxis enumdirection_enumaxis1 = EnumAxisCycle.FORWARD.a(enumdirection_enumaxis);
        EnumDirection.EnumAxis enumdirection_enumaxis2 = EnumAxisCycle.BACKWARD.a(enumdirection_enumaxis);
        int i = this.a(enumdirection_enumaxis1, d0);
        int j = this.a(enumdirection_enumaxis2, d1);
        int k = this.shape.b(enumdirection_enumaxis, i, j);

        return k <= 0 ? Double.NEGATIVE_INFINITY : this.a(enumdirection_enumaxis, k);
    }

    protected int a(EnumDirection.EnumAxis enumdirection_enumaxis, double d0) {
        return MathHelper.a(0, this.shape.c(enumdirection_enumaxis) + 1, (i) -> {
            return d0 < this.a(enumdirection_enumaxis, i);
        }) - 1;
    }

    @Nullable
    public MovingObjectPositionBlock rayTrace(Vec3D vec3d, Vec3D vec3d1, BlockPosition blockposition) {
        if (this.isEmpty()) {
            return null;
        } else {
            Vec3D vec3d2 = vec3d1.d(vec3d);

            if (vec3d2.g() < 1.0E-7D) {
                return null;
            } else {
                Vec3D vec3d3 = vec3d.e(vec3d2.a(0.001D));

                return this.shape.d(this.a(EnumDirection.EnumAxis.X, vec3d3.x - (double) blockposition.getX()), this.a(EnumDirection.EnumAxis.Y, vec3d3.y - (double) blockposition.getY()), this.a(EnumDirection.EnumAxis.Z, vec3d3.z - (double) blockposition.getZ())) ? new MovingObjectPositionBlock(vec3d3, EnumDirection.a(vec3d2.x, vec3d2.y, vec3d2.z).opposite(), blockposition, true) : AxisAlignedBB.a(this.d(), vec3d, vec3d1, blockposition);
            }
        }
    }

    public Optional<Vec3D> a(Vec3D vec3d) {
        if (this.isEmpty()) {
            return Optional.empty();
        } else {
            Vec3D[] avec3d = new Vec3D[1];

            this.b((d0, d1, d2, d3, d4, d5) -> {
                double d6 = MathHelper.a(vec3d.getX(), d0, d3);
                double d7 = MathHelper.a(vec3d.getY(), d1, d4);
                double d8 = MathHelper.a(vec3d.getZ(), d2, d5);

                if (avec3d[0] == null || vec3d.c(d6, d7, d8) < vec3d.distanceSquared(avec3d[0])) {
                    avec3d[0] = new Vec3D(d6, d7, d8);
                }

            });
            return Optional.of(avec3d[0]);
        }
    }

    public VoxelShape a(EnumDirection enumdirection) {
        if (!this.isEmpty() && this != VoxelShapes.b()) {
            VoxelShape voxelshape;

            if (this.faces != null) {
                voxelshape = this.faces[enumdirection.ordinal()];
                if (voxelshape != null) {
                    return voxelshape;
                }
            } else {
                this.faces = new VoxelShape[6];
            }

            voxelshape = this.b(enumdirection);
            this.faces[enumdirection.ordinal()] = voxelshape;
            return voxelshape;
        } else {
            return this;
        }
    }

    private VoxelShape b(EnumDirection enumdirection) {
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
        DoubleList doublelist = this.a(enumdirection_enumaxis);

        if (doublelist.size() == 2 && DoubleMath.fuzzyEquals(doublelist.getDouble(0), 0.0D, 1.0E-7D) && DoubleMath.fuzzyEquals(doublelist.getDouble(1), 1.0D, 1.0E-7D)) {
            return this;
        } else {
            EnumDirection.EnumAxisDirection enumdirection_enumaxisdirection = enumdirection.e();
            int i = this.a(enumdirection_enumaxis, enumdirection_enumaxisdirection == EnumDirection.EnumAxisDirection.POSITIVE ? 0.9999999D : 1.0E-7D);

            return new VoxelShapeSlice(this, enumdirection_enumaxis, i);
        }
    }

    public double a(EnumDirection.EnumAxis enumdirection_enumaxis, AxisAlignedBB axisalignedbb, double d0) {
        return this.a(EnumAxisCycle.a(enumdirection_enumaxis, EnumDirection.EnumAxis.X), axisalignedbb, d0);
    }

    protected double a(EnumAxisCycle enumaxiscycle, AxisAlignedBB axisalignedbb, double d0) {
        if (this.isEmpty()) {
            return d0;
        } else if (Math.abs(d0) < 1.0E-7D) {
            return 0.0D;
        } else {
            EnumAxisCycle enumaxiscycle1 = enumaxiscycle.a();
            EnumDirection.EnumAxis enumdirection_enumaxis = enumaxiscycle1.a(EnumDirection.EnumAxis.X);
            EnumDirection.EnumAxis enumdirection_enumaxis1 = enumaxiscycle1.a(EnumDirection.EnumAxis.Y);
            EnumDirection.EnumAxis enumdirection_enumaxis2 = enumaxiscycle1.a(EnumDirection.EnumAxis.Z);
            double d1 = axisalignedbb.b(enumdirection_enumaxis);
            double d2 = axisalignedbb.a(enumdirection_enumaxis);
            int i = this.a(enumdirection_enumaxis, d2 + 1.0E-7D);
            int j = this.a(enumdirection_enumaxis, d1 - 1.0E-7D);
            int k = Math.max(0, this.a(enumdirection_enumaxis1, axisalignedbb.a(enumdirection_enumaxis1) + 1.0E-7D));
            int l = Math.min(this.shape.c(enumdirection_enumaxis1), this.a(enumdirection_enumaxis1, axisalignedbb.b(enumdirection_enumaxis1) - 1.0E-7D) + 1);
            int i1 = Math.max(0, this.a(enumdirection_enumaxis2, axisalignedbb.a(enumdirection_enumaxis2) + 1.0E-7D));
            int j1 = Math.min(this.shape.c(enumdirection_enumaxis2), this.a(enumdirection_enumaxis2, axisalignedbb.b(enumdirection_enumaxis2) - 1.0E-7D) + 1);
            int k1 = this.shape.c(enumdirection_enumaxis);
            double d3;
            int l1;
            int i2;
            int j2;

            if (d0 > 0.0D) {
                for (l1 = j + 1; l1 < k1; ++l1) {
                    for (i2 = k; i2 < l; ++i2) {
                        for (j2 = i1; j2 < j1; ++j2) {
                            if (this.shape.a(enumaxiscycle1, l1, i2, j2)) {
                                d3 = this.a(enumdirection_enumaxis, l1) - d1;
                                if (d3 >= -1.0E-7D) {
                                    d0 = Math.min(d0, d3);
                                }

                                return d0;
                            }
                        }
                    }
                }
            } else if (d0 < 0.0D) {
                for (l1 = i - 1; l1 >= 0; --l1) {
                    for (i2 = k; i2 < l; ++i2) {
                        for (j2 = i1; j2 < j1; ++j2) {
                            if (this.shape.a(enumaxiscycle1, l1, i2, j2)) {
                                d3 = this.a(enumdirection_enumaxis, l1 + 1) - d2;
                                if (d3 <= 1.0E-7D) {
                                    d0 = Math.max(d0, d3);
                                }

                                return d0;
                            }
                        }
                    }
                }
            }

            return d0;
        }
    }

    public String toString() {
        return this.isEmpty() ? "EMPTY" : "VoxelShape[" + this.getBoundingBox() + "]";
    }
}
