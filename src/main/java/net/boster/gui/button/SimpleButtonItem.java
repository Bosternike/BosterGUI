package net.boster.gui.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boster.gui.BosterGUI;
import net.boster.gui.utils.GUIUtils;
import net.boster.gui.item.ItemManager;
import net.boster.gui.utils.PlaceholdersProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleButtonItem implements PlaceholdersProvider {

    @Nullable public Function<@NotNull Player, @Nullable String> itemName;
    @Nullable public Function<@NotNull Player, @Nullable String> itemSkull;

    @Nullable public Function<@NotNull Player, @Nullable ItemStack> itemStack;

    @Nullable public Function<@NotNull Player, @NotNull List<String>> lore;

    @NotNull private Function<@NotNull Player, @NotNull PlaceholdersProvider> placeholdersProvider = p -> this;

    public SimpleButtonItem(@NotNull ConfigurationSection section) {
        load(section);
    }

    public void load(@NotNull ConfigurationSection section) {
        ItemManager manager = BosterGUI.getItemManager();

        ItemStack item;
        if(!section.getBoolean("blank", false)) {
            String head = section.getString("head");
            String skull = section.getString("skull");

            if(head != null) {
                item = GUIUtils.getCustomSkull(head);
            } else if(skull != null) {
                item = GUIUtils.SKULL.clone();
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                itemSkull = p -> skull;
                manager.setOwner(meta, skull);
                item.setItemMeta(meta);
            } else {
                try {
                    item = manager.createItem(Objects.requireNonNull(section.getString("material")));
                } catch (Exception e) {
                    return;
                }
            }
        } else {
            return;
        }

        itemStack = p -> item;

        ItemMeta meta = item.getItemMeta();
        if(section.get("CustomModelData") != null) {
            manager.setCustomModelData(meta, section.getInt("CustomModelData"));
        }

        int damage = section.getInt("damage", -1);
        if(damage > -1) {
            manager.setDurability(item, meta, damage);
        }

        item.setAmount(section.getInt("Amount", 1));

        loadEffects(meta, section);
        loadEnchants(meta, section);
        loadFlags(meta, section);

        item.setItemMeta(meta);

        String name = GUIUtils.toColor(section.getString("name"));
        itemName = p -> name;

        List<String> list = section.getStringList("lore").stream().map(GUIUtils::toColor).collect(Collectors.toList());
        lore = p -> list;
    }

    private void loadFlags(@NotNull ItemMeta meta, @NotNull ConfigurationSection section) {
        for(String s : section.getStringList("flags")) {
            try {
                meta.addItemFlags(ItemFlag.valueOf(s));
            } catch (Exception ignored) {}
        }
    }

    private void loadEnchants(@NotNull ItemMeta meta, @NotNull ConfigurationSection section) {
        for(String s : section.getStringList("enchants")) {
            try {
                String[] ss = s.split(":");
                meta.addEnchant(Objects.requireNonNull(Enchantment.getByKey(NamespacedKey.minecraft(ss[0]))), Integer.parseInt(ss[1]), true);
            } catch (Exception ignored) {}
        }
    }

    private void loadEffects(@NotNull ItemMeta meta, @NotNull ConfigurationSection section) {
        ConfigurationSection potion = section.getConfigurationSection("potionEffect");
        if(potion != null && meta instanceof PotionMeta) {
            PotionMeta pm = (PotionMeta) meta;
            try {
                pm.addCustomEffect(new PotionEffect(
                        Objects.requireNonNull(PotionEffectType.getByName(potion.getString("type"))),
                        potion.getInt("duration", 0),
                        potion.getInt("amplifier", 0)), true);
            } catch (Exception ignored) {}
        }
    }

    public @Nullable ItemStack prepareItem(@NotNull Player p, @NotNull PlaceholdersProvider placeholders) {
        ItemStack item = itemStack != null ? itemStack.apply(p) : null;
        if(item != null) {
            item = item.clone();

            ItemMeta meta = item.getItemMeta();

            String skull = itemSkull != null ? itemSkull.apply(p) : null;
            if(skull != null && meta instanceof SkullMeta) {
                BosterGUI.getItemManager().setOwner((SkullMeta) meta, placeholders.applyPlaceholders(p, skull));
            }

            String name = itemName != null ? itemName.apply(p) : null;
            if(name != null) {
                meta.setDisplayName(placeholders.applyPlaceholders(p, name));
            }

            List<String> list = lore != null ? lore.apply(p) : null;
            if(list != null) {
                meta.setLore(list.stream().map(s -> placeholders.applyPlaceholders(p, s)).collect(Collectors.toList()));
            }

            item.setItemMeta(meta);

            return item;
        }

        return null;
    }

    public @Nullable ItemStack prepareItem(@NotNull Player p) {
        return prepareItem(p, placeholdersProvider.apply(p));
    }

    @Override
    public @NotNull String applyPlaceholders(@NotNull Player player, @NotNull String s) {
        return s.replace("%player%", player.getName());
    }
}
