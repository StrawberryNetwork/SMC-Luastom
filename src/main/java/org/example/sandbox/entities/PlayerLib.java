package org.example.sandbox.entities;

import org.example.LuaErrorAssert;
import org.example.sandbox.inventory.PlayerInventoryLib;
import org.example.sandbox.position.PositionLib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class PlayerLib extends LivingEntityLib {
    public PlayerLib(Player player) {
        super(player);

        rawset("SetSpawnPoint", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue pos) {
                if (pos instanceof PositionLib) {
                    Pos position = ((PositionLib) pos).getPoint();

                    player.setRespawnPoint(position);
                }

                return PlayerLib.this;
            }
        });

        rawset("SetDisplayName", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue test) {
                player.setDisplayName(Component.text(LuaErrorAssert.checkString(test, "SetDisplayName", 1)));

                return PlayerLib.this;
            }
        });

        rawset("GetInventory", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return new PlayerInventoryLib(player.getInventory());
            }
        });
    }
}