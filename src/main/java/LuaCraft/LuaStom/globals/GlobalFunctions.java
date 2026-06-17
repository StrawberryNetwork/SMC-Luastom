package LuaCraft.LuaStom.globals;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import LuaCraft.LuaStom.sandbox.entities.PlayerLib;

public class GlobalFunctions {
    public static OneArgFunction findMetaTable() {
        OneArgFunction func = new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue metaTable) {
                String type = LuaErrorAssert.checkString(metaTable, "FindMetaTable", 1).toLowerCase();

                switch (type) {
                    case "player": return PlayerLib.PLAYER_METATABLE;
                    default: return LuaValue.NIL;
                }
                
            }
        };

        return func;
    }
}
