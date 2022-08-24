package cc.polyfrost.polyaccessibility.keyboard;

import cc.polyfrost.polyaccessibility.mixin.AccessorSlot;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import net.minecraft.screen.slot.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.EnchantmentScreenHandler;

public class SlotsGroup {
    public String name;
    public List<Slot> slots;
    public HashMap<Slot, String> namesMap;

    public SlotsGroup(String name, List<Slot> slots) {
        this.namesMap = new HashMap<Slot, String>();
        this.name = name;

        if (slots == null) {
            this.slots = new ArrayList<Slot>();
        } else {
            this.slots = slots;
        }
    }

    public void setSlotName(Slot slot, String name) {
        this.namesMap.put(slot, name);
    }

    public String getSlotName(Slot slot) {
        String output = this.namesMap.get(slot);
        return output != null ? output : "";
    }

    public Slot getFirstSlot() {
        return this.slots.get(0);
    }

    public Slot getLastSlot() {
        return this.slots.get(this.slots.size() - 1);
    }

    public boolean hasSlotAbove(Slot slot) {
        return slot.y != this.getFirstSlot().y;
    }

    public boolean hasSlotBelow(Slot slot) {
        return slot.y != this.getLastSlot().y;
    }

    public boolean hasSlotLeft(Slot slot) {
        return slot.x != this.getFirstSlot().x;
    }

    public boolean hasSlotRight(Slot slot) {
        return slot.x != this.getLastSlot().x;
    }

    private static List<Slot> getSlotNeighbours(Slot slot, List<Slot> allSlots) {
        List<Slot> neighbours = new ArrayList<Slot>();

        int deltaY = 0;
        while (true) {
            boolean rowHasSlots = false;
            List<Slot> sameY = new ArrayList<Slot>();
            for (Slot s : allSlots) {
                if (s.y == slot.y + deltaY) {
                    sameY.add(s);
                    rowHasSlots = true;
                }
            }
            int deltaX = 0;
            for (Slot s : sameY) {
                if (s.x == slot.x + deltaX && s.y == slot.y + deltaY) {
                    deltaX += 18;
                    neighbours.add(s);
                }
            }

            if (!rowHasSlots || deltaX == 0) {
                break;
            }
            deltaY += 18;
        }
        return neighbours;
    }

    public static List<SlotsGroup> generateGroupsFromSlots(List<Slot> slots) {
        slots = new ArrayList<Slot>(slots);

        System.out.println("\n\n\n\n\n====================");
        for (Slot s : slots) {
            System.out.println(String.valueOf(s.id) + ": x" + String.valueOf(s.x) + " y" + String.valueOf(s.y) + " ["
                    + String.valueOf(((AccessorSlot) s).getInventoryIndex()) + "] " + String.valueOf(s.inventory) + " ("
                    + String.valueOf(s) + ")");
        }
        System.out.println("====================\n\n\n\n");

        List<SlotsGroup> foundGroups = new ArrayList<SlotsGroup>();

        SlotsGroup hotbar = new SlotsGroup("Hotbar", null);
        for (Slot s : slots) {
            int index = ((AccessorSlot) s).getInventoryIndex();
            if (s.inventory instanceof PlayerInventory && index >= 0 && index <= 8) {
                hotbar.slots.add(s);
            }
        }
        slots.removeAll(hotbar.slots);

        SlotsGroup playerInventory = new SlotsGroup("Inventory", null);
        for (Slot s : slots) {
            int index = ((AccessorSlot) s).getInventoryIndex();
            if (s.inventory instanceof PlayerInventory && index >= 9 && index <= 35) {
                playerInventory.slots.add(s);
            }
        }
        slots.removeAll(playerInventory.slots);

        SlotsGroup playerArmor = new SlotsGroup("Armor", null);
        for (Slot s : slots) {
            int index = ((AccessorSlot) s).getInventoryIndex();
            if (s.inventory instanceof PlayerInventory && index >= 36 && index <= 39) {
                playerArmor.slots.add(s);
            }
        }
        slots.removeAll(playerArmor.slots);

        SlotsGroup offHand = new SlotsGroup("Off hand", null);
        for (Slot s : slots) {
            int index = ((AccessorSlot) s).getInventoryIndex();
            if (s.inventory instanceof PlayerInventory && index == 40) {
                offHand.slots.add(s);
                break;
            }
        }
        slots.removeAll(offHand.slots);

        while (slots.size() > 0) {
            SlotsGroup group = new SlotsGroup(getInventoryName(slots.get(0)), getSlotNeighbours(slots.get(0), slots));
            slots.removeAll(group.slots);
            group.nameSlots();
            foundGroups.add(group);
        }

        if (playerArmor.slots.size() > 0) {
            foundGroups.add(playerArmor);
        }
        if (offHand.slots.size() > 0) {
            foundGroups.add(offHand);
        }
        if (playerInventory.slots.size() > 0) {
            foundGroups.add(playerInventory);
        }

        foundGroups.add(hotbar);
        return foundGroups;
    }

    private void nameSlots() {
        if (this.slots.get(0).inventory instanceof CraftingInventory) {
            int size = (int) Math.round(Math.sqrt(this.slots.size()));
            List<String> names = new ArrayList<String>();
            for (int row = 1; row <= size; row++) {
                for (int column = 1; column <= size; column++) {
                    names.add(String.valueOf(row) + "x" + String.valueOf(column));
                }
            }
            for (int i = 0; i < this.slots.size(); i++) {
                Slot slot = this.slots.get(i);
                String name = names.get(i);
                this.setSlotName(slot, name);
            }
        }
    }

    public static String getInventoryName(Slot slot) {
        if (slot.inventory instanceof CraftingResultInventory) {
            return "Crafting output";
        } else if (slot.inventory instanceof CraftingInventory) {
            return "Crafting input";
        }

        if (slot instanceof FurnaceFuelSlot) {
            return "Fuel input";
        } else if (slot instanceof FurnaceOutputSlot) {
            return "Furnace output";
        }
        return "Group";
    }
}
