package org.agmas.noellesroles.mystic;

import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.agmas.noellesroles.Noellesroles;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MysticPlayerComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<MysticPlayerComponent> KEY = ComponentRegistry.getOrCreate(Identifier.of(Noellesroles.MOD_ID, "mystic"), MysticPlayerComponent.class);
    private final PlayerEntity player;
    public UUID target;
    public boolean won = false;
    public int ballsBought = 0;

    public void reset() {
        this.target = player.getUuid();
        this.sync();
    }

    public MysticPlayerComponent(PlayerEntity player) {
        this.player = player;
        target = player.getUuid();
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void clientTick() {
    }

    public void serverTick() {
        GameWorldComponent gameWorldComponent = (GameWorldComponent) GameWorldComponent.KEY.get(player.getWorld());
        if (!gameWorldComponent.isRole(player, Noellesroles.MYSTIC)) return;
        PlayerEntity player1 = player.getWorld().getPlayerByUuid(target);
        if (player1 == null || !gameWorldComponent.getRole(player1).isInnocent() || (GameFunctions.isPlayerEliminated(player1)) && !won) {
            List<UUID> innocentPlayers = new ArrayList<>();
            gameWorldComponent.getRoles().forEach((uuid2,role1)->{
                PlayerEntity player2 = player.getWorld().getPlayerByUuid(uuid2);
                if (uuid2 == null) return;
                if (role1.isInnocent() && GameFunctions.isPlayerAliveAndSurvival(player2) && !role1.equals(WatheRoles.VIGILANTE) && !role1.equals(Noellesroles.MIMIC)) {
                    innocentPlayers.add(uuid2);
                }
            });
            Collections.shuffle(innocentPlayers);
            if (!innocentPlayers.isEmpty()) {
                target = innocentPlayers.getFirst();
            }
        }
        sync();
    }
    public void setTarget(UUID target) {
        this.target = target;
        this.sync();
    }

    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putUuid("target", this.target);
        tag.putInt("ballsBought", this.ballsBought);
    }

    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.target = tag.contains("target") ? tag.getUuid("target") : player.getUuid();
        this.ballsBought = tag.contains("ballsBought") ? tag.getInt("ballsBought") : 0;
    }
}