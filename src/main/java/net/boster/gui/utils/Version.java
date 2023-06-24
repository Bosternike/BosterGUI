package net.boster.gui.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public enum Version {

    OLD_VERSION(0, 0),
    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_19_R4,
    v1_20_R1,
    NOT_FOUND(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private static Version currentVersion;

    private final int majorVersion;
    private final int minorVersion;
    private int patchVersion = -1;

    Version() {
        String[] version = name().replaceFirst("v", "").split("_");

        majorVersion = Integer.parseInt(version[0]);
        minorVersion = Integer.parseInt(version[1]);

        if(version.length >= 3) {
            patchVersion = Integer.parseInt(version[2]);
        }
    }

    public boolean isUpToDate(int major, int minor, int patch) {
        return major >= majorVersion && minor >= minorVersion && patch >= patchVersion;
    }

    public boolean isUpToDate(@NotNull Version version) {
        return isUpToDate(version.majorVersion, version.minorVersion, version.patchVersion);
    }

    public boolean isCurrentUpToDate() {
        return isUpToDate(getCurrentVersion());
    }

    public static Version getCurrentVersion() {
        if(currentVersion == null) {
            String ver = Bukkit.getServer().getClass().getPackage().getName();
            String version = ver.substring(ver.lastIndexOf('.') + 1);
            try {
                int cv = Integer.parseInt(version.split("_")[1]);
                if(cv < 8) {
                    return (currentVersion = OLD_VERSION);
                }
            } catch (Exception e) {
                return (currentVersion = OLD_VERSION);
            }

            try {
                currentVersion = valueOf(version);
            } catch (IllegalArgumentException e) {
                currentVersion = NOT_FOUND;
            }
        }
        return currentVersion;
    }

    @NotNull
    public static String getBukkitVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
