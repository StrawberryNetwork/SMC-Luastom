package LuaCraft.LuaStom.sandbox.entities;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.sandbox.inventory.ItemStackLib;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;

public class ItemLib extends EntityLib {
    public static LuaTable creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue item) {
                ItemStack itemstack = ((ItemStackLib) item).getItemStack();

                ItemEntity entity = new ItemEntity(itemstack);

                return new ItemLib(entity);
            }
        });

        return tbl;
    }

    public ItemLib(ItemEntity item) {
        super(item);

        rawset("GetItemStack", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new ItemStackLib(item.getItemStack());
            }
        });
    }
}
