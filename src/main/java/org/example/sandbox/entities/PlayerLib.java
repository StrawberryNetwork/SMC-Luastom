package org.example.sandbox.entities;

import org.example.LuaErrorAssert;
import org.example.sandbox.component.ComponentLib;
import org.example.sandbox.component.ComponentUtil;
import org.example.sandbox.inventory.DefaultInventoryHandlerLib;
import org.example.sandbox.inventory.PlayerInventoryLib;
import org.example.sandbox.position.PositionLib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;

public class PlayerLib extends LivingEntityLib {
    public PlayerLib(Player player) {
        super(player);

        rawset("SetSpawnPoint", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue pos) {
                if (pos instanceof PositionLib) {
                    Pos position = ((PositionLib) pos).getPoint();

                    player.setRespawnPoint(position);
                }

                return PlayerLib.this;
            }
        });

        rawset("SetDisplayName", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue test) {
                player.setDisplayName(Component.text(LuaErrorAssert.checkString(test, "SetDisplayName", 1)));

                return PlayerLib.this;
            }
        });

        rawset("GetInventory", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new PlayerInventoryLib(player.getInventory(), player);
            }
        });

        rawset("SaveInventory", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                DefaultInventoryHandlerLib.defaultSaveInventory(player.getInventory(), player);

                return PlayerLib.this;
            }
        });

        rawset("LoadInventory", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                DefaultInventoryHandlerLib.defaultLoadInventory(player.getInventory(), player);

                return PlayerLib.this;
            }
        });

        rawset("SendMessage", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue message) {
                player.sendMessage(ComponentUtil.luaValueToComponent(message));

                return PlayerLib.this;
            }
        });

        rawset("GetName", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new ComponentLib(player.getName());
            }
        });

        rawset("SetTag", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue name, LuaValue value) {
                String tagName = LuaErrorAssert.checkString(name, "Player.SetTag", 1);

                if (value.isstring()) player.setTag(Tag.String(tagName), value.tojstring());
                if (value.isint()) player.setTag(Tag.Integer(tagName), value.toint());
                if (value.isboolean()) player.setTag(Tag.Boolean(tagName), value.toboolean());
                if (value instanceof ComponentLib) player.setTag(Tag.Component(tagName), ComponentUtil.luaValueToComponent(value));

                return PlayerLib.this;
            }
        });

        rawset("HasTag", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue name) {
                String tagName = LuaErrorAssert.checkString(name, "Player.SetTag", 1);
                boolean hasTag = player.getTag(Tag.String(tagName)) != null || 
                                 player.getTag(Tag.Integer(tagName)) != null ||
                                 player.getTag(Tag.Boolean(tagName)) != null;

                return LuaValue.valueOf(hasTag);
            }
        });

        rawset("GetUUID", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(player.getUuid().toString());
            }
        });
    }
}