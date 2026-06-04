package org.example.sandbox.inventory;

import net.minestom.server.inventory.Inventory;

public class InventoryLib extends AbstractInventoryLib {
    private Inventory inventory;

    public InventoryLib(Inventory inventory) {
        this.inventory = inventory;

        super(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
