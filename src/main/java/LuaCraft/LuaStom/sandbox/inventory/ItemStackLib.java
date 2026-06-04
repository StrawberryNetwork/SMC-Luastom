package LuaCraft.LuaStom.sandbox.inventory;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.kyori.adventure.key.Key;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ItemStackLib extends LuaTable {
    private ItemStack item;

    public static LuaTable creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue item, LuaValue amt) {
                Material itemMat = Material.fromKey(Key.key(LuaErrorAssert.checkString(item, "ItemStack.New", 1)));
                int amount = LuaErrorAssert.checkInt(amt, "ItemStack.New", 2);

                return new ItemStackLib(ItemStack.of(itemMat, amount));
            }
        });

        return tbl;
    }

    public ItemStackLib(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItemStack() {
        return item;
    }
}
