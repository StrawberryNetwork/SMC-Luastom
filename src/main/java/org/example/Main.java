package org.example;

import java.util.HashMap;
import java.util.Map;

import org.example.sandbox.world.events.AsyncPlayerConfiguration;
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

        //create the instance/world
        //InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        //InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        //new File("worlds/world").mkdirs();
        //instanceContainer.setChunkLoader(new AnvilLoader("worlds/world"));

        //generate the world
        //instanceContainer.setGenerator(unit -> {
        //    unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
        //});

        //add an event handler to handle spawning!
        //GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //lighting
        //instanceContainer.setChunkSupplier(LightingChunk::new);

        //globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
        //    final Player player = event.getPlayer();
        //    event.setSpawningInstance(instanceContainer);
        //    player.setRespawnPoint(new Pos(0, 42,0));
        //});

        //globalEventHandler.addListener(net.minestom.server.event.player.PlayerBlockBreakEvent.class, event -> {
        //    instanceContainer.saveChunkToStorage(instanceContainer.getChunkAt(event.getBlockPosition()));
        //});
//
        //Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        //    instanceContainer.saveChunksToStorage().join();
        //}));

        //allow me to join
        server.start("0.0.0.0", 25565);
    }
}
