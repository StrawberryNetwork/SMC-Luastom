package org.example.sandbox.position;

import org.luaj.vm2.LuaTable;

import net.minestom.server.coordinate.Point;

public class PointLib extends LuaTable {
    private Point point;
    
    public PointLib(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}
