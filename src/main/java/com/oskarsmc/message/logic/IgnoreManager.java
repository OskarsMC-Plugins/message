package com.oskarsmc.message.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages persistent player ignore lists (A ignores B = A never sees messages from B).
 * Player B has no idea they are ignored.
 */
public final class IgnoreManager {
    private static final Type MAP_TYPE = new TypeToken<Map<UUID, Set<UUID>>>() {}.getType();

    private final Path ignoresFile;
    private final Gson gson = new Gson();
    private final Map<UUID, Set<UUID>> ignoredPlayers = new ConcurrentHashMap<>(); // ignorer UUID → set of ignored UUIDs

    public IgnoreManager(Path dataFolder) {
        this.ignoresFile = dataFolder.resolve("ignores.json");
        loadIgnores();
    }

    private void loadIgnores() {
        if (!Files.exists(ignoresFile)) return;
        try {
            Map<UUID, Set<UUID>> loaded = gson.fromJson(Files.readString(ignoresFile), MAP_TYPE);
            if (loaded != null) ignoredPlayers.putAll(loaded);
        } catch (IOException e) {
            // silent fail - file will be recreated on first save
        }
    }

    private void saveIgnores() {
        try {
            Files.writeString(ignoresFile, gson.toJson(ignoredPlayers));
        } catch (IOException ignored) {
        }
    }

    /**
     * Toggle ignore status for the given player.
     * @return true if the target is now ignored
     */
    public boolean toggleIgnore(@NotNull Player ignorer, @NotNull Player target) {
        UUID ignorerId = ignorer.getUniqueId();
        UUID targetId = target.getUniqueId();

        Set<UUID> set = ignoredPlayers.computeIfAbsent(ignorerId, k -> ConcurrentHashMap.newKeySet());

        if (set.remove(targetId)) {
            saveIgnores();
            return false; // no longer ignoring
        } else {
            set.add(targetId);
            saveIgnores();
            return true; // now ignoring
        }
    }

    public void ignore(@NotNull Player ignorer, @NotNull Player target) {
        UUID ignorerId = ignorer.getUniqueId();
        UUID targetId = target.getUniqueId();
        ignoredPlayers.computeIfAbsent(ignorerId, k -> ConcurrentHashMap.newKeySet()).add(targetId);
        saveIgnores();
    }

    public void unignore(@NotNull Player ignorer, @NotNull Player target) {
        UUID ignorerId = ignorer.getUniqueId();
        UUID targetId = target.getUniqueId();
        Set<UUID> set = ignoredPlayers.get(ignorerId);
        if (set != null) {
            set.remove(targetId);
            saveIgnores();
        }
    }

    /**
     * Check if the recipient has ignored the sender (silent check).
     */
    public boolean isIgnored(@NotNull Player recipient, @NotNull CommandSource sender) {
        if (!(sender instanceof Player senderPlayer)) return false;
        Set<UUID> ignored = ignoredPlayers.get(recipient.getUniqueId());
        return ignored != null && ignored.contains(senderPlayer.getUniqueId());
    }

    /**
     * Get all players the given ignorer is ignoring (for /ignore list).
     */
    public @NotNull Set<UUID> getIgnored(@NotNull Player ignorer) {
        Set<UUID> set = ignoredPlayers.get(ignorer.getUniqueId());
        return set != null ? Collections.unmodifiableSet(set) : Collections.emptySet();
    }
}
