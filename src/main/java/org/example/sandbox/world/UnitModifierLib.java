package org.example.sandbox.world;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.UnitModifier;

public class UnitModifierLib extends LuaTable {
    public UnitModifierLib(UnitModifier modifier) {
        rawset("SetFillHeight", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue minMaxHeight, LuaValue block) {

                modifier.fillHeight(LuaErrorAssert.checkInt(minMaxHeight.get(1), "SetFillHeight", 1), LuaErrorAssert.checkInt(minMaxHeight.get(2), "SetFillHeight", 2), Block.fromKey(LuaErrorAssert.checkString(block, "SetFillHeight", 3)));

                return LuaValue.NIL;
            }
        });
    }
}
