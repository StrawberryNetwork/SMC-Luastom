package LuaCraft.LuaStom.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import LuaCraft.LuaStom.sandbox.position.PointLib;
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

        rawset("SetFill", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue fromTo, LuaValue block) {
                modifier.fill(((PointLib) fromTo.get(1)).getPoint(), ((PointLib) fromTo.get(2)).getPoint(), Block.fromKey(LuaErrorAssert.checkString(block, "SetFill", 3)));
            
                return LuaValue.NIL;
            }
        });
    }
}
