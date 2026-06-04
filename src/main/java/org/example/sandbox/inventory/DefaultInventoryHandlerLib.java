package org.example.sandbox.inventory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;

public class DefaultInventoryHandlerLib {
    public static void defaultSaveInventory(AbstractInventory inventory, Player player) {
        JsonArray items = new JsonArray();
    
    for (int i = 0; i < inventory.getSize(); i++) {
        ItemStack item = inventory.getItemStack(i);
        if (item.isAir()) continue;
        
        JsonObject json = new JsonObject();
        json.addProperty("slot", i);
        try {
            json.addProperty("nbt", MinestomAdventure.tagStringIO().asString(item.toItemNBT()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        items.add(json);
    }

    Path path = Path.of("data/inventories/" + player.getUuid() + ".json");
    
    try {
        Files.createDirectories(path.getParent());
        Files.writeString(path, items.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void defaultLoadInventory(AbstractInventory inventory, Player player) {
        Path path = Path.of("data/inventories/" + player.getUuid() + ".json");

        if (Files.exists(path)) {
            try {
                String json = Files.readString(path);
                JsonArray items = JsonParser.parseString(json).getAsJsonArray();
                for (JsonElement element : items) {
                    JsonObject item = element.getAsJsonObject();
                    String nbt = item.get("nbt").getAsString();
                    int slot = item.get("slot").getAsInt();
                    System.out.println(slot);
                    CompoundBinaryTag NBT = MinestomAdventure.tagStringIO().asCompound(nbt);
                    ItemStack itemStack = ItemStack.fromItemNBT(NBT);
                    
                    inventory.setItemStack(slot, itemStack);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}