package org.example.sandbox.position;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;

public class PointLib extends LuaTable {
    private Point point;

    public static LuaValue creator() {
        LuaTable tbl = new LuaTable();

        tbl.set("New", new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs args) {
                if (args.narg() < 4) throw new LuaError("Point.New requires 3 numbers");

                Double x = LuaErrorAssert.checkDouble(args.arg(2), "Point.New", 1);
                Double y = LuaErrorAssert.checkDouble(args.arg(3), "Point.New", 2);
                Double z = LuaErrorAssert.checkDouble(args.arg(4), "Point.New", 3);

                Point newPoint = new Vec(x, y, z);

                return new PointLib(newPoint);
            }
        });

        return tbl;
    }
    
    public PointLib(Point point) {
        this.point = point;

        rawset("Add", new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs args) {
                if (!(args.arg(2) instanceof PointLib) && args.narg() < 4) throw new LuaError("Point.Add requires either a Point or 3 numbers");

                if (args.arg(2) instanceof PointLib otherPoint) {
                    point.add(((PointLib) otherPoint).getPoint());

                    return PointLib.this;
                } else {
                    Double x = LuaErrorAssert.checkDouble(args.arg(2), "Point.Add", 1);
                    Double y = LuaErrorAssert.checkDouble(args.arg(3), "Point.Add", 2);
                    Double z = LuaErrorAssert.checkDouble(args.arg(4), "Point.Add", 3);
                    point.add(x, y, z);

                    return PointLib.this;
                }
            }
        });
    }

    public Point getPoint() {
        return point;
    }
}
