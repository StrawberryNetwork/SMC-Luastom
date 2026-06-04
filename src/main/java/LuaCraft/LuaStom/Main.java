package LuaCraft.LuaStom;

import java.util.concurrent.ConcurrentHashMap;

import org.luaj.vm2.Globals;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public class Main {
    public static void main(String[] args) {
        //auth
        MinecraftServer server = MinecraftServer.init(new Auth.Online());

        ConcurrentHashMap<String, Globals> allGlobals = new ConcurrentHashMap<>();

        new ScriptGeneration();
        ScriptHandler.loadAllScripts(allGlobals, true);

        MinecraftServer.getCommandManager().register(new LuaCommand(allGlobals, ScriptHandler.getScriptsFolder()));

        //allow me to join
        server.start("0.0.0.0", 25565);
    }
}
