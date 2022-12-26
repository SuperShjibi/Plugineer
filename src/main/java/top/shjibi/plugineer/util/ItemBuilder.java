package top.shjibi.plugineer.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 帮你快速构造物品的类
 */
public final class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private ItemBuilder(ItemStack item, ItemMeta meta) {
        itemStack = item;
        itemMeta = meta;
    }

    private ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * 获取一个物品材料为material, 物品数量为amount的ItemBuilder
     *
     * @param material 构造ItemBuilder的物品的材料
     * @param amount   构造ItemBuilder的物品的数量
     * @return 一个ItemBuilder
     */
    public static @NotNull ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    /**
     * 获取一个物品材料为material, 物品数量为1的ItemBuilder
     *
     * @param material 构造ItemBuilder的物品的材料
     * @return 一个ItemBuilder
     */
    public static @NotNull ItemBuilder of(@NotNull Material material) {
        return of(material, 1);
    }

    /**
     * 获取一个物品为itemStack的ItemBuilder
     *
     * @param itemStack 构造ItemBuilder的物品
     * @return 一个ItemBuilder
     */
    public static @NotNull ItemBuilder of(@NotNull ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * 将此ItemBuilder物品的名字设为name
     *
     * @return 名字为name的ItemBuilder
     */
    public @NotNull ItemBuilder withDisplayName(@NotNull String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * 将此ItemBuilder的物品的描述设为lore
     *
     * @return 描述为lore的ItemBuilder
     */
    public @NotNull ItemBuilder withLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * 将此ItemBuilder的物品的描述设为lore
     *
     * @return 描述为lore的ItemBuilder
     */
    public @NotNull ItemBuilder withLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * 给此ItemBuilder的物品添加一个属性修改
     *
     * @param attribute 要修改的属性
     * @param modifier  要修改属性的效果
     * @return 添加了属性修改的ItemBuilder
     */
    public @NotNull ItemBuilder withAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        itemMeta.addAttributeModifier(attribute, modifier);
        return this;
    }

    /**
     * 给此ItemBuilder的物品删除一个属性修改
     *
     * @param attribute 要删除的属性
     * @return 删除了属性修改的ItemBuilder
     */
    public @NotNull ItemBuilder withoutAttributeModifier(@NotNull Attribute attribute) {
        itemMeta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * 给此ItemBuilder的物品删除一个属性修改
     *
     * @param attribute 要删除的属性
     * @param modifier  要删除的属性修改效果
     * @return 删除了属性修改的ItemBuilder
     */
    public @NotNull ItemBuilder withoutAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        itemMeta.removeAttributeModifier(attribute);
        return this;
    }

    /**
     * 给此ItemBuilder的物品添加多个标签
     *
     * @param flags 要添加的所有标签
     * @return 添加了标签的ItemBuilder
     */
    public @NotNull ItemBuilder withItemFlags(@NotNull ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * 给此ItemBuilder的物品删除多个标签
     *
     * @param flags 要删除的所有标签
     * @return 删除了标签的ItemBuilder
     */
    public @NotNull ItemBuilder withoutItemFlags(@NotNull ItemFlag... flags) {
        itemMeta.removeItemFlags(flags);
        return this;
    }

    /**
     * 给此ItemBuilder附魔
     *
     * @param enchantment            需要的附魔
     * @param level                  需要的等级
     * @param ignoreLevelRestriction 是否忽略等级限制
     * @return 一个附了指定魔咒的ItemBuilder
     */
    public @NotNull ItemBuilder withEnchant(@NotNull Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    /**
     * 给此ItemBuilder去除附魔
     *
     * @param enchantment 需要去除的附魔
     * @return 一个去除了指定附魔的ItemBuilder
     */
    public @NotNull ItemBuilder withoutEnchant(@NotNull Enchantment enchantment) {
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * 给此ItemBuilder添加永久数据
     *
     * @param key  数据键
     * @param type 数据种类
     * @param z    数据值
     * @return 一个持有指定数据的ItemBuilder
     */
    public @NotNull <T, Z> ItemBuilder withPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z z) {
        itemMeta.getPersistentDataContainer().set(key, type, z);
        return this;
    }

    /**
     * 给此ItemBuilder删除永久数据
     *
     * @param key 数据键
     * @return 一个删除了指定数据的ItemBuilder
     */
    public @NotNull ItemBuilder withoutPersistentData(NamespacedKey key) {
        itemMeta.getPersistentDataContainer().remove(key);
        return this;
    }

    /**
     * 返回物品的ItemMeta
     *
     * @return 物品的ItemMeta
     */
    public @NotNull ItemMeta getItemMeta() {
        return itemMeta;
    }

    /**
     * 构建最终的物品
     *
     * @return 最终的物品
     */
    public @NotNull ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String toString() {
        return "ItemBuilder {material: " + itemStack.getType() + ", amount: " + itemStack.getAmount() + ", itemMeta: " + itemMeta + "}";
    }
}
