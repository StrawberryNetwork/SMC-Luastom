package LuaCraft.LuaStom.sandbox.inventory;


import net.minestom.server.entity.Player;
import net.minestom.server.inventory.PlayerInventory;

public class PlayerInventoryLib extends AbstractInventoryLib {
    public PlayerInventoryLib(PlayerInventory inventory, Player player) {
        super(inventory);
    }
}
