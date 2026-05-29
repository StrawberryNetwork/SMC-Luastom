package org.example.sandbox.entities;

import org.example.sandbox.position.PointLib;
import org.example.sandbox.world.InstanceContainerLib;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;

public class EntityLib extends LuaTable {
    public EntityLib(Entity entity) {
        rawset("SetInstance", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue inst, LuaValue pos) {
                if (inst instanceof InstanceContainerLib) {
                    InstanceContainer instance = ((InstanceContainerLib) inst).getContainer();

                    if (pos instanceof PointLib) {
                        entity.setInstance(instance, ((PointLib) pos).getPoint());
                    }
                }

                return EntityLib.this;
            }
        });
    }
}
