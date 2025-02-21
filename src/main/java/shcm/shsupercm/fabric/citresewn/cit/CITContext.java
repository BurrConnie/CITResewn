package shcm.shsupercm.fabric.citresewn.cit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import blue.endless.jankson.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Holds momentary information to be used for CITs' condition matching and type effects.
 */
public class CITContext {
    /**
     * The main item stack to check for the CIT.
     */
    public final ItemStack stack;

    /**
     * The item's containing world(defaults to {@link MinecraftClient#world} if null)
     */
    public final World world;

    /**
     * The item's associated living entity if present. (null if not relevant)
     */
    @Nullable
    public final LivingEntity entity;

    /**
     * Cached enchantment map from {@link #stack}.
     * @see #enchantments()
     */
    private Map<Identifier, Integer> enchantments = null;

    public CITContext(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity) {
        this.stack = stack;
        this.world = world == null ? MinecraftClient.getInstance().world : world;
        this.entity = entity;
    }

    /**
     * @see #enchantments
     * @return a map of this context item's enchantments
     */
    public Map<Identifier, Integer> enchantments() {
        if (this.enchantments == null) {
            this.enchantments = new LinkedHashMap<>();
            for (NbtElement nbtElement : stack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments())
                this.enchantments.put(EnchantmentHelper.getIdFromNbt((NbtCompound) nbtElement), EnchantmentHelper.getLevelFromNbt((NbtCompound) nbtElement));
        }
        return this.enchantments;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CITContext that &&
                Objects.equals(this.stack, that.stack) &&
                Objects.equals(this.world, that.world) &&
                Objects.equals(this.entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, world, entity);
    }
}
