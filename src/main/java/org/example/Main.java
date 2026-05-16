package org.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;

import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;

public class Main {
    static Map<String, Globals> allGlobals = new HashMap<>();

    public static void main(String[] args) {
        new ScriptGeneration();
        ScriptHandler.loadAllScripts(allGlobals, true);
        //initialize the server
        MinecraftServer server = MinecraftServer.init();

        //auth
        MinecraftServer.init(new Auth.Online());

        //create the instance/world
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        new File("worlds/world").mkdirs();
        instanceContainer.setChunkLoader(new AnvilLoader("worlds/world"));

        //generate the world
        instanceContainer.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
        });

        //add an event handler to handle spawning!
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

        //lighting
        instanceContainer.setChunkSupplier(LightingChunk::new);

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42,0));
        });

        globalEventHandler.addListener(net.minestom.server.event.player.PlayerBlockBreakEvent.class, event -> {
            instanceContainer.saveChunkToStorage(instanceContainer.getChunkAt(event.getBlockPosition()));
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            instanceContainer.saveChunksToStorage().join();
        }));

        //allow me to join
        server.start("0.0.0.0", 25565);
    }
}
