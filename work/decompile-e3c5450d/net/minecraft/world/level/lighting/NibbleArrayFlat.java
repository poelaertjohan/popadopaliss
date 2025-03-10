package net.minecraft.world.level.lighting;

import net.minecraft.world.level.chunk.NibbleArray;

public class NibbleArrayFlat extends NibbleArray {

    public static final int SIZE = 128;

    public NibbleArrayFlat() {
        super(128);
    }

    public NibbleArrayFlat(NibbleArray nibblearray, int i) {
        super(128);
        System.arraycopy(nibblearray.asBytes(), i * 128, this.data, 0, 128);
    }

    @Override
    protected int b(int i, int j, int k) {
        return k << 4 | i;
    }

    @Override
    public byte[] asBytes() {
        byte[] abyte = new byte[2048];

        for (int i = 0; i < 16; ++i) {
            System.arraycopy(this.data, 0, abyte, i * 128, 128);
        }

        return abyte;
    }
}
