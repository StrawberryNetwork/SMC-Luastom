package LuaCraft.LuaStom.sandbox.inventory;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;

public class AbstractInventoryLib extends LuaTable {
    private AbstractInventory inventory;

    public AbstractInventoryLib(AbstractInventory inventory) {
        this.inventory = inventory;

        rawset("AddItemStack", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue item) {
                ItemStack itemstack = ((ItemStackLib) item).getItemStack();

                inventory.addItemStack(itemstack);

                return AbstractInventoryLib.this;
            }
        });
    }

    public AbstractInventory getInventory() {
        return inventory;
    }
}