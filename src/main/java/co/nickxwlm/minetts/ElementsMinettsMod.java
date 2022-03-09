/*
 *    MCreator note:
 *
 *    This file is autogenerated to connect all MCreator mod elements together.
 *
 */
package co.nickxwlm.minetts;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;

import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Potion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

import java.util.function.Supplier;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

import co.nickxwlm.minetts.gui.GuiSamTrade;
import co.nickxwlm.minetts.gui.GuiOS;
import co.nickxwlm.minetts.gui.GuiEasterEgg;
import co.nickxwlm.minetts.gui.GuiDiskDriveslot;

public class ElementsMinettsMod implements IFuelHandler, IWorldGenerator {
	public final List<ModElement> elements = new ArrayList<>();
	public final List<Supplier<Block>> blocks = new ArrayList<>();
	public final List<Supplier<Item>> items = new ArrayList<>();
	public final List<Supplier<Biome>> biomes = new ArrayList<>();
	public final List<Supplier<EntityEntry>> entities = new ArrayList<>();
	public final List<Supplier<Potion>> potions = new ArrayList<>();
	public static Map<ResourceLocation, net.minecraft.util.SoundEvent> sounds = new HashMap<>();
	public ElementsMinettsMod() {
		sounds.put(new ResourceLocation("minetts", "nickolay-idle1"),
				new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "nickolay-idle1")));
		sounds.put(new ResourceLocation("minetts", "nickolay-damage1"),
				new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "nickolay-damage1")));
		sounds.put(new ResourceLocation("minetts", "nickolay-death1"),
				new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "nickolay-death1")));
		sounds.put(new ResourceLocation("minetts", "sam-damage1"), new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "sam-damage1")));
		sounds.put(new ResourceLocation("minetts", "sam-idle1"), new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "sam-idle1")));
		sounds.put(new ResourceLocation("minetts", "sam-death1"), new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "sam-death1")));
		sounds.put(new ResourceLocation("minetts", "irina-damage1"),
				new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "irina-damage1")));
		sounds.put(new ResourceLocation("minetts", "irina-death1"),
				new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "irina-death1")));
		sounds.put(new ResourceLocation("minetts", "irina-idle1"), new net.minecraft.util.SoundEvent(new ResourceLocation("minetts", "irina-idle1")));
	}

	public void preInit(FMLPreInitializationEvent event) {
		try {
			for (ASMDataTable.ASMData asmData : event.getAsmData().getAll(ModElement.Tag.class.getName())) {
				Class<?> clazz = Class.forName(asmData.getClassName());
				if (clazz.getSuperclass() == ElementsMinettsMod.ModElement.class)
					elements.add((ElementsMinettsMod.ModElement) clazz.getConstructor(this.getClass()).newInstance(this));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(elements);
		elements.forEach(ElementsMinettsMod.ModElement::initElements);
		this.addNetworkMessage(MinettsModVariables.WorldSavedDataSyncMessageHandler.class, MinettsModVariables.WorldSavedDataSyncMessage.class,
				Side.SERVER, Side.CLIENT);
	}

	public void registerSounds(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
		for (Map.Entry<ResourceLocation, net.minecraft.util.SoundEvent> sound : sounds.entrySet())
			event.getRegistry().register(sound.getValue().setRegistryName(sound.getKey()));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator cg, IChunkProvider cp) {
		elements.forEach(element -> element.generateWorld(random, chunkX * 16, chunkZ * 16, world, world.provider.getDimension(), cg, cp));
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		for (ModElement element : elements) {
			int ret = element.addFuel(fuel);
			if (ret != 0)
				return ret;
		}
		return 0;
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			WorldSavedData mapdata = MinettsModVariables.MapVariables.get(event.player.world);
			WorldSavedData worlddata = MinettsModVariables.WorldVariables.get(event.player.world);
			if (mapdata != null)
				MinettsMod.PACKET_HANDLER.sendTo(new MinettsModVariables.WorldSavedDataSyncMessage(0, mapdata), (EntityPlayerMP) event.player);
			if (worlddata != null)
				MinettsMod.PACKET_HANDLER.sendTo(new MinettsModVariables.WorldSavedDataSyncMessage(1, worlddata), (EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void onPlayerChangedDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.player.world.isRemote) {
			WorldSavedData worlddata = MinettsModVariables.WorldVariables.get(event.player.world);
			if (worlddata != null)
				MinettsMod.PACKET_HANDLER.sendTo(new MinettsModVariables.WorldSavedDataSyncMessage(1, worlddata), (EntityPlayerMP) event.player);
		}
	}
	private int messageID = 0;
	public <T extends IMessage, V extends IMessage> void addNetworkMessage(Class<? extends IMessageHandler<T, V>> handler, Class<T> messageClass,
			Side... sides) {
		for (Side side : sides)
			MinettsMod.PACKET_HANDLER.registerMessage(handler, messageClass, messageID, side);
		messageID++;
	}
	public static class GuiHandler implements IGuiHandler {
		@Override
		public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			if (id == GuiEasterEgg.GUIID)
				return new GuiEasterEgg.GuiContainerMod(world, x, y, z, player);
			if (id == GuiDiskDriveslot.GUIID)
				return new GuiDiskDriveslot.GuiContainerMod(world, x, y, z, player);
			if (id == GuiSamTrade.GUIID)
				return new GuiSamTrade.GuiContainerMod(world, x, y, z, player);
			if (id == GuiOS.GUIID)
				return new GuiOS.GuiContainerMod(world, x, y, z, player);
			return null;
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			if (id == GuiEasterEgg.GUIID)
				return new GuiEasterEgg.GuiWindow(world, x, y, z, player);
			if (id == GuiDiskDriveslot.GUIID)
				return new GuiDiskDriveslot.GuiWindow(world, x, y, z, player);
			if (id == GuiSamTrade.GUIID)
				return new GuiSamTrade.GuiWindow(world, x, y, z, player);
			if (id == GuiOS.GUIID)
				return new GuiOS.GuiWindow(world, x, y, z, player);
			return null;
		}
	}
	public List<ModElement> getElements() {
		return elements;
	}

	public List<Supplier<Block>> getBlocks() {
		return blocks;
	}

	public List<Supplier<Item>> getItems() {
		return items;
	}

	public List<Supplier<Biome>> getBiomes() {
		return biomes;
	}

	public List<Supplier<EntityEntry>> getEntities() {
		return entities;
	}

	public List<Supplier<Potion>> getPotions() {
		return potions;
	}
	public static class ModElement implements Comparable<ModElement> {
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Tag {
		}
		protected final ElementsMinettsMod elements;
		protected final int sortid;
		public ModElement(ElementsMinettsMod elements, int sortid) {
			this.elements = elements;
			this.sortid = sortid;
		}

		public void initElements() {
		}

		public void init(FMLInitializationEvent event) {
		}

		public void preInit(FMLPreInitializationEvent event) {
		}

		public void generateWorld(Random random, int posX, int posZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
		}

		public void serverLoad(FMLServerStartingEvent event) {
		}

		public void registerModels(ModelRegistryEvent event) {
		}

		public int addFuel(ItemStack fuel) {
			return 0;
		}

		@Override
		public int compareTo(ModElement other) {
			return this.sortid - other.sortid;
		}
	}
}
