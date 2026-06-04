package LuaCraft.LuaStom.sandbox;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import net.minestom.server.item.Material;

public class Enumerations {
    public static LuaTable ItemEnums() {
        LuaTable tbl = new LuaTable();

        for (Material mat : Material.values()) {
            String material = mat.toString().replace("minecraft:", "").toUpperCase();
            tbl.set(material, LuaValue.valueOf(mat.toString()));
        }

        return tbl;
    }
    public static LuaTable LogEnums() {
        LuaTable tbl = new LuaTable();

        tbl.set("INFO", LuaValue.valueOf("Info"));
        tbl.set("WARN", LuaValue.valueOf("Warn"));
        tbl.set("ERROR", LuaValue.valueOf("Error"));

        return tbl;
    }
}