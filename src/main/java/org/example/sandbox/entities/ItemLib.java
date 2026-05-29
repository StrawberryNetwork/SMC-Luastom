package org.example.sandbox.entities;

import org.example.sandbox.inventory.ItemStackLib;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;

public class ItemLib extends EntityLib {
    public static LuaTable creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue item) {
                ItemStack itemstack = ((ItemStackLib) item).getItemStack();

                ItemEntity entity = new ItemEntity(itemstack);

                return new ItemLib(entity);
            }
        });

        return tbl;
    }

    public ItemLib(ItemEntity item) {
        super(item);

        rawset("GetItemStack", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return new ItemStackLib(item.getItemStack());
            }
        });
    }
}
