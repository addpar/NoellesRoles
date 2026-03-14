package org.agmas.noellesroles;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.agmas.noellesroles.effects.SeanceEffect;

public class ModEffects { //Thats right, i wrote another registry entry field for statuses... even though theres only one
    public static final StatusEffect Seance = register(
            new SeanceEffect(),
            "seance_effect"
    );
    public static StatusEffect register(StatusEffect effect, String id){
        Identifier effectId = Identifier.of(Noellesroles.MOD_ID, id);
        StatusEffect registeredEffect = Registry.register(Registries.STATUS_EFFECT, effectId, effect);
        return registeredEffect;


    }
    public static void init(){} //i have NO idea why this allows the effects to get loaded but alright.
    //like you dont even gotta call a method to start the registration. it just do the thing anyways.

}
