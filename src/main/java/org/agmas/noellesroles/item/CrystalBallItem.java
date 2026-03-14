package org.agmas.noellesroles.item;

import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.agmas.noellesroles.Noellesroles;
import org.agmas.noellesroles.effects.SeanceEffect;
import org.jetbrains.annotations.NotNull;
import org.agmas.noellesroles.ModEffects;

import static org.agmas.noellesroles.ModEffects.Seance;

public class CrystalBallItem extends Item {
    public CrystalBallItem(Item.Settings settings) { // why doesnt this work but FakeKnifeItem does? I figured it out
        super(settings);

    }

    public TypedActionResult<ItemStack> use(World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand); //Get object to return it... thats it

        user.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 0.7F, 0.4F); //low pitch for the funny

        user.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(Seance), 200, 0,false,false)); //i hate the registry i hate the registryi hate the registryi hate the registryi hate the registryi hate the registry

        itemStack.decrementUnlessCreative(1,user); //surely decrementing the item count before running 'consume' wont be an issue right? :)

        if (user.isCreative()){ //this reads like english, i think you understand this atleast
            return TypedActionResult.success(itemStack,true);
        } else {
            return TypedActionResult.consume(itemStack); //this is supposed to remove item on use, but doesnt? gonna test this

        }
    }


}
