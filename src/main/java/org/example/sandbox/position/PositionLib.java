package org.example.sandbox.position;

import org.example.LuaErrorAssert;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import net.minestom.server.coordinate.Pos;

public class PositionLib extends PointLib {
    public static ThreeArgFunction positionFactory() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
                double X = LuaErrorAssert.checkDouble(x, "Position", 1);
                double Y = LuaErrorAssert.checkDouble(y, "Position", 2);
                double Z = LuaErrorAssert.checkDouble(z, "Position", 3);

                Pos position = new Pos(X, Y, Z);

                return new PositionLib(position);
            }
        };
    }

    private final Pos position;

    public PositionLib(Pos position) {
        super(position);
        this.position = position;
    }

    @Override
    public Pos getPoint() {
        return this.position;
    }
}
