package LuaCraft.LuaStom.sandbox.component;

import org.luaj.vm2.LuaValue;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ComponentUtil {
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component luaValueToComponent(LuaValue arg) {

        if (arg instanceof ComponentLib lib) {
            return lib.getComponent();
        }
        
        if (arg.isuserdata()) {
            Object raw = arg.touserdata();
            if (raw instanceof Component c) return c;
        }

        return Component.text(arg.tojstring());
    }

    public static Component parseLegacy(String text) {
        return LEGACY.deserialize(text);
    }

    public static String toLegacy(Component component) {
        return LEGACY.serialize(component);
    }
}
