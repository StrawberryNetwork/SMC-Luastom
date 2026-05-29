package org.example;

import java.util.HashMap;
import java.util.Map;

import org.example.sandbox.events.AsyncPlayerConfiguration;
import org.example.sandbox.events.PickupItem;
import org.example.sandbox.events.PlayerBlockBreak;
import org.luaj.vm2.Globals;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;

public class Main {
    static Map<String, Globals> allGlobals = new HashMap<>();

    public static void main(String[] args) {
        //initialize the server
        MinecraftServer server = MinecraftServer.init();

        //auth
        MinecraftServer.init(new Auth.Online());

        new ScriptGeneration();
        ScriptHandler.loadAllScripts(allGlobals, true);

        new AsyncPlayerConfiguration(allGlobals);
        new PlayerBlockBreak(allGlobals);
        new PickupItem(allGlobals);

        //allow me to join
        server.start("0.0.0.0", 25565);
    }
}
