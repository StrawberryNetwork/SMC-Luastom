package LuaCraft.LuaStom.sandbox.entities;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import LuaCraft.LuaStom.sandbox.position.PointLib;
import LuaCraft.LuaStom.sandbox.position.PositionLib;
import LuaCraft.LuaStom.sandbox.world.InstanceContainerLib;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;

public class EntityLib extends LuaTable {
    private Entity entity;

    public EntityLib(Entity entity) {
        this.entity = entity;

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

        rawset("GetPosition", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return new PointLib(entity.getPosition());
            }
        });

        rawset("GetYaw", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(entity.getPosition().yaw());
            }
        });

        rawset("GetFacing", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(entity.getPosition().facing().toString());
            }
        });

        rawset("Teleport", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue position) {
                if (position instanceof PositionLib) {
                    Pos newPoint = ((PositionLib) position).getPoint();

                    entity.teleport(newPoint);

                    return EntityLib.this;
                } else {
                    LuaErrorAssert.LuaStomError("Entity:Teleport requires a position, received unknown value");
                    return LuaValue.NIL;
                }
            }
        });
    }

    public Entity getEntity() {
        return entity;
    }
}
