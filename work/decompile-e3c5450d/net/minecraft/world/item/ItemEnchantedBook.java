package net.minecraft.world.item;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.IRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.WeightedRandomEnchant;
import net.minecraft.world.level.World;

public class ItemEnchantedBook extends Item {

    public static final String TAG_STORED_ENCHANTMENTS = "StoredEnchantments";

    public ItemEnchantedBook(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public boolean i(ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean a(ItemStack itemstack) {
        return false;
    }

    public static NBTTagList d(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        return nbttagcompound != null ? nbttagcompound.getList("StoredEnchantments", 10) : new NBTTagList();
    }

    @Override
    public void a(ItemStack itemstack, @Nullable World world, List<IChatBaseComponent> list, TooltipFlag tooltipflag) {
        super.a(itemstack, world, list, tooltipflag);
        ItemStack.a(list, d(itemstack));
    }

    public static void a(ItemStack itemstack, WeightedRandomEnchant weightedrandomenchant) {
        NBTTagList nbttaglist = d(itemstack);
        boolean flag = true;
        MinecraftKey minecraftkey = IRegistry.ENCHANTMENT.getKey(weightedrandomenchant.enchantment);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(i);
            MinecraftKey minecraftkey1 = MinecraftKey.a(nbttagcompound.getString("id"));

            if (minecraftkey1 != null && minecraftkey1.equals(minecraftkey)) {
                if (nbttagcompound.getInt("lvl") < weightedrandomenchant.level) {
                    nbttagcompound.setShort("lvl", (short) weightedrandomenchant.level);
                }

                flag = false;
                break;
            }
        }

        if (flag) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setString("id", String.valueOf(minecraftkey));
            nbttagcompound1.setShort("lvl", (short) weightedrandomenchant.level);
            nbttaglist.add(nbttagcompound1);
        }

        itemstack.getOrCreateTag().set("StoredEnchantments", nbttaglist);
    }

    public static ItemStack a(WeightedRandomEnchant weightedrandomenchant) {
        ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);

        a(itemstack, weightedrandomenchant);
        return itemstack;
    }

    @Override
    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        Iterator iterator;
        Enchantment enchantment;

        if (creativemodetab == CreativeModeTab.TAB_SEARCH) {
            iterator = IRegistry.ENCHANTMENT.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (enchantment.category != null) {
                    for (int i = enchantment.getStartLevel(); i <= enchantment.getMaxLevel(); ++i) {
                        nonnulllist.add(a(new WeightedRandomEnchant(enchantment, i)));
                    }
                }
            }
        } else if (creativemodetab.n().length != 0) {
            iterator = IRegistry.ENCHANTMENT.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (creativemodetab.a(enchantment.category)) {
                    nonnulllist.add(a(new WeightedRandomEnchant(enchantment, enchantment.getMaxLevel())));
                }
            }
        }

    }
}
