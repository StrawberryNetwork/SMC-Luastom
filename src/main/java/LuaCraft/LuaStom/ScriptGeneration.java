package LuaCraft.LuaStom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptGeneration {
    public ScriptGeneration() {
        File scriptsFolder = new File("scripts");
        File defaultLuaFile = new File(scriptsFolder, "example.lua");

        String exampleScript = """
                function fooBar()
                    print("test123")
                end
                """;

        scriptsFolder.mkdirs();

        if (scriptsFolder.exists()) {
            try {
                if (!defaultLuaFile.exists()) {
                    defaultLuaFile.createNewFile();
                    Files.writeString(Path.of("scripts/example.lua"), exampleScript);
                }
            } catch (IOException e) {}
        }

        // Once we generate we can safely also set the scripts folder we will be using for reading lua files from
        ScriptHandler.setScriptsFolder(scriptsFolder);
    }
}
