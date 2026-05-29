package org.example.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.minestom.server.instance.generator.GenerationUnit;

public class GenerationUnitLib extends LuaTable {
    public GenerationUnitLib(GenerationUnit unit) {
        rawset("GetModifier", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return new UnitModifierLib(unit.modifier());
            }
        });
    }
}