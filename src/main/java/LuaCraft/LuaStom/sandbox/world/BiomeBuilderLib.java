package LuaCraft.LuaStom.sandbox.world;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.minestom.server.world.biome.Biome;

public class BiomeBuilderLib extends LuaTable {
    Biome.Builder builder;

    public static LuaValue creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new BiomeBuilderLib(Biome.builder());
            }
        });

        return tbl;
    }

    public BiomeBuilderLib(Biome.Builder builder) {
        this.builder = builder;

        rawset("SetTempature", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue temp) {
                builder.temperature(LuaErrorAssert.checkFloat(temp, "BiomeBuilder:SetTempature", 1));

                return BiomeBuilderLib.this;
            }
        });

        rawset("SetDownFall", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue downfall) {
                builder.downfall(LuaErrorAssert.checkFloat(downfall, "BiomeBuilder:SetDownFall", 1));

                return BiomeBuilderLib.this;
            }
        });

        rawset("SetPercipitation", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue percipitation) {
                builder.precipitation(LuaErrorAssert.checkBoolean(percipitation, "BiomeBuilder:SetPercipitation", 1));

                return BiomeBuilderLib.this;
            }
        });

        rawset("Build", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new BiomeLib(builder.build());
            }
        });
    }

    public Biome.Builder getBuilder() {
        return builder;
    }
}
