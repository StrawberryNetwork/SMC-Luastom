package org.example.sandbox.inventory;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class ItemStackLib extends LuaTable {
    private ItemStack item;

    public static LuaTable creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue item, LuaValue amt) {
                Material itemMat = Material.fromKey(LuaErrorAssert.checkString(item, "ItemStack.New", 1));
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
