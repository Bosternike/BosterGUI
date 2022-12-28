package net.boster.gui.constructor;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.button.SimpleButtonItem;
import net.boster.gui.craft.CraftCustomGUI;
import net.boster.gui.multipage.MultiPageButtonAppearance;
import net.boster.gui.multipage.MultiPageGUI;
import net.boster.gui.multipage.MultiPageNextButton;
import net.boster.gui.multipage.MultiPagePreviousButton;
import net.boster.gui.utils.ButtonUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PaginatedGUI extends SimpleConstructor {

    @NotNull protected List<MultiPageNextButton> nextButtons = new ArrayList<>();
    @NotNull protected List<MultiPagePreviousButton> previousButtons = new ArrayList<>();

    @NotNull protected List<Integer> slots;

    public PaginatedGUI(@NotNull ConfigurationSection section, @NotNull MultiPageButtonAppearance def) {
        super(section);
        slots = section.getIntegerList("Slots");

        ConfigurationSection ns = Objects.requireNonNull(section.getConfigurationSection("NextPageButton"));
        ConfigurationSection ps = Objects.requireNonNull(section.getConfigurationSection("PreviousPageButton"));

        SimpleButtonItem ni = new SimpleButtonItem(ns);
        SimpleButtonItem pi = new SimpleButtonItem(ps);

        MultiPageButtonAppearance na = getAppearance(ns.getString("Appear"), def);
        MultiPageButtonAppearance pa = getAppearance(ps.getString("Appear"), def);

        for (Integer slot : ButtonUtils.getSlots(ns)) {
            nextButtons.add(new MultiPageNextButton() {
                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public @NotNull MultiPageButtonAppearance getAppearance() {
                    return na;
                }

                @Override
                public @Nullable ItemStack item(@NotNull Player player) {
                    return ni.prepareItem(player);
                }
            });
        }

        for(Integer slot : ButtonUtils.getSlots(ps)) {
            previousButtons.add(new MultiPagePreviousButton() {
                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public @NotNull MultiPageButtonAppearance getAppearance() {
                    return pa;
                }

                @Override
                public @Nullable ItemStack item(@NotNull Player player) {
                    return pi.prepareItem(player);
                }
            });
        }
    }

    public PaginatedGUI(@NotNull ConfigurationSection section) {
        this(section, MultiPageButtonAppearance.NEEDED);
    }

    public @NotNull MultiPageGUI getGUI(@NotNull Player player) {
        return new MultiPageGUI(player, craftGUI) {
            public @Nullable String prepareTitle() {
                return title != null ? title.replace("%page%", getPageNumber() + "") : null;
            }
        };
    }

    public @NotNull MultiPageGUI getGUI(@NotNull Player player, @NotNull CraftCustomGUI cg) {
        return new MultiPageGUI(player, cg) {
            public @Nullable String prepareTitle() {
                return title != null ? title.replace("%page%", getPageNumber() + "") : null;
            }
        };
    }

    public void apply(@NotNull MultiPageGUI gui) {
        gui.getSlots().addAll(slots);

        gui.getNextButtons().addAll(nextButtons);
        gui.getPreviousButtons().addAll(previousButtons);
    }

    public @NotNull MultiPageButtonAppearance getAppearance(@Nullable String s, @NotNull MultiPageButtonAppearance def) {
        try {
            return MultiPageButtonAppearance.valueOf(s);
        } catch (Exception e) {
            return def;
        }
    }
}
