package org.example.sandbox.inventory;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;

public class AbstractInventoryLib extends LuaTable {
    public AbstractInventoryLib(AbstractInventory inventory) {
        rawset("AddItemStack", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue item) {
                ItemStack itemstack = ((ItemStackLib) item).getItemStack();

                inventory.addItemStack(itemstack);

                return AbstractInventoryLib.this;
            }
        });
    }
}