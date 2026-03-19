package org.agmas.noellesroles.effects;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.agmas.noellesroles.Noellesroles;

import java.util.ListIterator;
import java.util.Objects;

public class SeanceEffect extends StatusEffect {
    public SeanceEffect(){
    super(StatusEffectCategory.BENEFICIAL, 0xe9b8b3);


    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        if (entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entity; //theres some redundancy here BUT dont care
            if (player instanceof ServerPlayerEntity){
                ServerPlayerEntity p = (ServerPlayerEntity) player; //p for ServerPlayerEntity and player for PlayerEntity (They do different things but to the same player)
                p.networkHandler.sendPacket(new SubtitleS2CPacket(Text.of("The voices of the dead reach you for a limited time!")));
                p.networkHandler.sendPacket(new TitleS2CPacket(Text.of(" ")));
                //because overlay, titles, and other bs has to be packets sent through networkHandler. kms

                p.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 240, 0, false, false));
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 240, 30,false,false)); //never forget the duration is in TICKS
                p.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 140, 20,false,false));
                //KEEP THE onApplied AND ITEM EFFECTS SEPARATE AND SORTED. eg. the EFFECT causes glowing, the voices to actually be heard and the text saying so.
                for (ServerPlayerEntity cPlayer : p.getServer().getPlayerManager().getPlayerList()) {

                    GameWorldComponent gwc = (GameWorldComponent) GameWorldComponent.KEY.get(cPlayer.getWorld());

                    if (cPlayer != p) {     //Plays the Sound seance sfx for everyone in the game

                        cPlayer.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_END_PORTAL_SPAWN), SoundCategory.PLAYERS, cPlayer.getX(), cPlayer.getY(), cPlayer.getZ(), 0.5f, 0.6f, 0));
                    }
                    if (cPlayer!= p && !gwc.isRole(cPlayer, Noellesroles.MYSTIC) && GameFunctions.isPlayerSpectatingOrCreative(cPlayer)) {     //Alerts Dead Players of Seance

                        cPlayer.networkHandler.sendPacket(new SubtitleS2CPacket(Text.of("The Mystic can hear the dead!")));
                        cPlayer.networkHandler.sendPacket(new TitleS2CPacket(Text.of(" ")));
                    }

                }



            }

        }

    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
       if (!Objects.equals(entity.getMovement(), new Vec3d(0, 0, 0))){ //if velocity not zero... then make it so.
           entity.setVelocity(0,0,0);
        }



        return super.applyUpdateEffect(entity, amplifier);
    }
}
