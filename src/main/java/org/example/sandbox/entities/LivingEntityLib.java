package org.example.sandbox.entities;

import org.example.sandbox.inventory.PlayerInventoryLib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;

public class LivingEntityLib extends EntityLib {
    public LivingEntityLib(LivingEntity entity) {
        super(entity);

        rawset("GetInventory", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                if (entity instanceof Player player) {
                    return new PlayerInventoryLib(player.getInventory(), player);
                }

                // TODO: Possibly this entire one isn't needed if we can just get a player from the entity???
                return LivingEntityLib.this;
            }
        });

        rawset("IsPlayer", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(entity instanceof Player);
            }
        });

        rawset("AsPlayer", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                if (entity instanceof Player player) return new PlayerLib(player);

                return LuaValue.NIL;
            }
        });
    }
}