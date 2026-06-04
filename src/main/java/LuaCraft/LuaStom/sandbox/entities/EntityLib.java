package LuaCraft.LuaStom.sandbox.entities;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import LuaCraft.LuaStom.sandbox.position.PointLib;
import LuaCraft.LuaStom.sandbox.world.InstanceContainerLib;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;

public class EntityLib extends LuaTable {
    public EntityLib(Entity entity) {
        rawset("SetInstance", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue inst, LuaValue pos) {
                if (inst instanceof InstanceContainerLib) {
                    InstanceContainer instance = ((InstanceContainerLib) inst).getContainer();

                    if (pos instanceof PointLib) {
                        entity.setInstance(instance, ((PointLib) pos).getPoint());
                    }
                }

                return EntityLib.this;
            }
        });

        rawset("GetInstance", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                if (entity.getInstance() instanceof InstanceContainer container) {
                    return new InstanceContainerLib(container);
                } else {
                    return LuaValue.NIL;
                }
            }
        });
    }
}
