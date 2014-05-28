package thermalexpansion.gui.container.machine;

import cofh.gui.slot.SlotEnergy;
import cofh.gui.slot.SlotOutput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import thermalexpansion.block.machine.TileSawmill;
import thermalexpansion.gui.container.ContainerTEBase;
import thermalexpansion.util.crafting.SawmillManager;

public class ContainerSawmill extends ContainerTEBase {

	TileSawmill myTile;

	public ContainerSawmill(InventoryPlayer inventory, TileEntity entity) {

		super(entity);

		myTile = (TileSawmill) entity;
		addSlotToContainer(new Slot(myTile, 0, 56, 26));
		addSlotToContainer(new SlotOutput(myTile, 1, 116, 26));
		addSlotToContainer(new SlotOutput(myTile, 2, 134, 26));
		addSlotToContainer(new SlotOutput(myTile, 3, 116, 53));
		addSlotToContainer(new SlotEnergy(myTile, 4, 8, 53));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int i) {

		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(i);

		int invTile = myTile.inventory.length;
		int invPlayer = invTile + 27;
		int invFull = invTile + 36;

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();

			if (i == 1 || i == 2 || i == 3) {
				if (!mergeItemStack(stackInSlot, invTile, invFull, true)) {
					return null;
				}
			} else if (i != 0) {
				if (SawmillManager.recipeExists(stackInSlot)) {
					if (!mergeItemStack(stackInSlot, 0, 1, false)) {
						return null;
					}
				} else if (i >= invTile && i < invPlayer) {
					if (!mergeItemStack(stackInSlot, invPlayer, invFull, false)) {
						return null;
					}
				} else if (i >= invPlayer && i < invFull && !mergeItemStack(stackInSlot, invTile, invPlayer, false)) {
					return null;
				}
			} else if (!mergeItemStack(stackInSlot, invTile, invFull, false)) {
				return null;
			}
			if (stackInSlot.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (stackInSlot.stackSize == stack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

}