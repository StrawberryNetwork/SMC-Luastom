package org.example.sandbox.entities;

import org.example.sandbox.inventory.PlayerInventoryLib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;

public class LivingEntityLib extends EntityLib {
    public LivingEntityLib(LivingEntity entity) {
        super(entity);

        rawset("GetInventory", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                if (entity instanceof Player player) {
                    return new PlayerInventoryLib(player.getInventory());
                }

                // TODO: Eventually make it so all LivingEntity mobs have at least a basic inventory? Possibly?
                return LivingEntityLib.this;
            }
        });
    }
}