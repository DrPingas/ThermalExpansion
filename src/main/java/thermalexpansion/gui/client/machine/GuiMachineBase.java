package thermalexpansion.gui.client.machine;

import cofh.gui.GuiBaseAdv;
import cofh.gui.container.IAugmentableContainer;
import cofh.gui.element.TabAugment;
import cofh.gui.element.TabBase;
import cofh.gui.element.TabConfiguration;
import cofh.gui.element.TabEnergy;
import cofh.gui.element.TabInfo;
import cofh.gui.element.TabRedstone;
import cofh.gui.element.TabSecurity;
import cofh.gui.element.TabTutorial;
import cofh.util.StringHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import thermalexpansion.block.machine.BlockMachine;
import thermalexpansion.block.machine.TileMachineBase;

public abstract class GuiMachineBase extends GuiBaseAdv {

	protected TileMachineBase myTile;
	protected String playerName;

	public String myInfo = "";
	public String myTutorial = StringHelper.tutorialTabAugment();

	protected TabBase redstoneTab;
	protected TabBase configTab;

	public GuiMachineBase(Container container, TileEntity tile, EntityPlayer player, ResourceLocation texture) {

		super(container, texture);

		myTile = (TileMachineBase) tile;
		name = myTile.getInventoryName();
		playerName = player.getCommandSenderName();

		String machineName = BlockMachine.NAMES[myTile.getType()];

		myInfo = StringHelper.localize("tab.thermalexpansion.machine." + machineName + "." + 0);
		for (int i = 1; i < 3; i++) {
			myInfo += "\n\n" + StringHelper.localize("tab.thermalexpansion.machine." + machineName + "." + i);
		}
		if (myTile.enableSecurity() && myTile.isSecured()) {
			myTutorial += "\n\n" + StringHelper.tutorialTabSecurity();
		}
		if (myTile.augmentRedstoneControl) {
			myTutorial += "\n\n" + StringHelper.tutorialTabRedstone();
		}
		if (myTile.augmentReconfigSides) {
			myTutorial += "\n\n" + StringHelper.tutorialTabConfiguration();
		}
		if (myTile.getMaxEnergyStored(ForgeDirection.UNKNOWN) > 0) {
			myTutorial += "\n\n" + StringHelper.tutorialTabFluxRequired();
		}
	}

	@Override
	public void initGui() {

		super.initGui();

		addTab(new TabAugment(this, (IAugmentableContainer) inventorySlots));
		if (myTile.enableSecurity() && myTile.isSecured()) {
			addTab(new TabSecurity(this, myTile, playerName));
		}
		redstoneTab = addTab(new TabRedstone(this, myTile));
		configTab = addTab(new TabConfiguration(this, myTile));

		if (myTile.getMaxEnergyStored(ForgeDirection.UNKNOWN) > 0) {
			addTab(new TabEnergy(this, myTile, false));
		}
		addTab(new TabInfo(this, myInfo));
		addTab(new TabTutorial(this, myTutorial));
	}

	@Override
	public void updateScreen() {

		super.updateScreen();

		if (!myTile.canAccess()) {
			this.mc.thePlayer.closeScreen();
		}
	}

	@Override
	protected void updateElementInformation() {

		super.updateElementInformation();

		redstoneTab.setVisible(myTile.augmentRedstoneControl);
		configTab.setVisible(myTile.augmentReconfigSides);
	}

}