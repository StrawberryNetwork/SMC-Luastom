package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.sandbox.entities.ItemLib;
import org.example.sandbox.events.ServerEvent;
import org.example.sandbox.inventory.ItemStackLib;
import org.example.sandbox.position.BlockVecLib;
import org.example.sandbox.position.PositionLib;
import org.example.sandbox.world.BlockLib;
import org.example.sandbox.world.World;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptHandler {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft ScriptHandler");

    public record FileData(String fileName, String fileContents) {}

    private static File scriptsFolder;

    public static void setScriptsFolder(File folder) {
        scriptsFolder = folder;
    }

    private static FileData readScriptFile(File file) throws IOException {
        String fileContents = null;
        Path filePath;
        String fileName;

        filePath = file.toPath();
        fileName = file.getName();

        fileContents = Files.readString(filePath);

        return new FileData(fileName, fileContents);
    }

    public static void setupScriptGlobals(Globals globals, String fileName) {
        // Restrict these from the user as they can be dangerous and cause unwanted side effects
        globals.set("os", LuaValue.NIL);
        globals.set("io", LuaValue.NIL);
        globals.set("debug", LuaValue.NIL);
        globals.set("_G", LuaValue.NIL);
        globals.set("luajava", LuaValue.NIL);

        // Destroy the loaded extras LuaJ might insert into LuaCraft to prevent malicious code further
        LuaValue loaded = globals.get("package").get("loaded");
        loaded.set("os", LuaValue.NIL);
        loaded.set("io", LuaValue.NIL);
        loaded.set("debug", LuaValue.NIL);
        loaded.set("luajava", LuaValue.NIL);


        globals.set("World", new World());
        globals.set("ServerEvent", new ServerEvent());
        globals.set("Position", PositionLib.positionFactory());
        globals.set("ItemStack", ItemStackLib.creator());
        globals.set("ItemEntity", ItemLib.creator());

        // Force them to stick within the scripts folder for requiring for safety reasons
        LuaValue pkg = globals.get("package");
        String scriptsPath = scriptsFolder.getAbsolutePath() + java.io.File.separator + "?.lua";
        pkg.set("path", LuaValue.valueOf(scriptsPath));
        pkg.set("cpath", LuaValue.NIL);
    }

    public static void loadAllScripts(Map<String, Globals> allGlobals, Boolean firstLoad) {
        List<File> allScriptFiles = collectScripts(scriptsFolder);

        if (allScriptFiles == null) {
            logger.error("Failed to collect all Lua script files, please contact author");
            return;
        } else {
            if (firstLoad) {
                logger.error("Collected all Lua script files... Preparing them to be read");
                logger.warn("Collected all Lua script files... Preparing them to be read");
                logger.info("Collected all Lua script files... Preparing them to be read");
            }
        }

        for (File file : allScriptFiles) {
            Globals globals = JsePlatform.standardGlobals();
            setupScriptGlobals(globals, file.getName());

            FileData data = null;

            try {
                data = readScriptFile(file);
            } catch (IOException e) {}

            if (data == null || data.fileContents() == null) {
                logger.error("Failed to read contents of " + file.getName() + " please contact author");
                continue;
            }

            String rawSource = data.fileContents();

            LuaValue loadedScript = globals.load(rawSource, file.getName());

            if (loadedScript != null) {
                try {
                    loadedScript.call();
                } catch (LuaError e) {
                    throw new LuaError(e.getMessage());
                }
                allGlobals.put(file.getName(), globals);
            }
        }
    }

    private static List<File> collectScripts(File folder) {
        List<File> result = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) return result;

        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(collectScripts(file));
            } else if (!file.getName().startsWith("-") && file.getName().endsWith(".lua")) {
                result.add(file);
            }
        }
        return result;
    }
}
