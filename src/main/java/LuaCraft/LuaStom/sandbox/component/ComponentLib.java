package LuaCraft.LuaStom.sandbox.component;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import LuaCraft.LuaStom.LuaErrorAssert;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentLib extends LuaTable {
    Component component;
    public ComponentLib(Component component) {
        this.component = component;

        rawset("ToString", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue type) {
                String returnType;

                if (!type.isnil()) {
                    returnType = LuaErrorAssert.checkString(type, "ToString", 1);
                } else {
                    returnType = "default";
                }

                switch(returnType) {
                    case "plain":
                        return LuaValue.valueOf(PlainTextComponentSerializer.plainText().serialize(component));
                    case "legacy":
                        return LuaValue.valueOf(LegacyComponentSerializer.legacyAmpersand().serialize(component));
                    //case "minimessage":
                        //return LuaValue.valueOf(MiniMessage.miniMessage().serialize(component));
                    default:
                        return LuaValue.valueOf(PlainTextComponentSerializer.plainText().serialize(component));
                }
            }
        });
    }

    public Component getComponent() {
        return component;
    }
}
