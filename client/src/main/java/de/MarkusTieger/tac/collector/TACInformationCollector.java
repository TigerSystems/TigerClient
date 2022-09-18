package de.MarkusTieger.tac.collector;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.MarkusTieger.common.collector.IInformationCollector;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.SoundCard;

public class TACInformationCollector implements IInformationCollector {

	@Override
	public JsonObject collect() {

		SystemInfo info = new SystemInfo();
		HardwareAbstractionLayer layer = info.getHardware();

		CentralProcessor centralProcessor = layer.getProcessor();
		ComputerSystem computerSystem = layer.getComputerSystem();
		GlobalMemory globalMemory = layer.getMemory();

		JsonObject object = new JsonObject();

		JsonObject cpu = new JsonObject();

		cpu.addProperty("id", centralProcessor.getProcessorIdentifier().getProcessorID());
		cpu.addProperty("name", centralProcessor.getProcessorIdentifier().getName());
		cpu.addProperty("family", centralProcessor.getProcessorIdentifier().getFamily());
		cpu.addProperty("architecture", centralProcessor.getProcessorIdentifier().getMicroarchitecture());
		cpu.addProperty("vendor", centralProcessor.getProcessorIdentifier().getVendor());
		cpu.addProperty("x64", centralProcessor.getProcessorIdentifier().isCpu64bit());
		cpu.addProperty("model", centralProcessor.getProcessorIdentifier().getModel());
		cpu.addProperty("identifier", centralProcessor.getProcessorIdentifier().getIdentifier());

		JsonArray gpus = new JsonArray();

		for (GraphicsCard card : layer.getGraphicsCards()) {

			JsonObject gpu = new JsonObject();

			gpu.addProperty("id", card.getDeviceId());
			gpu.addProperty("vendor", card.getVendor());
			gpu.addProperty("name", card.getName());
			gpu.addProperty("vram", card.getVRam());
			gpu.addProperty("version", card.getVersionInfo());

			gpus.add(gpu);

		}

		JsonObject computer = new JsonObject();

		computer.addProperty("model", computerSystem.getModel());
		computer.addProperty("hardwareId", computerSystem.getHardwareUUID());
		computer.addProperty("manufacturer", computerSystem.getManufacturer());
		computer.addProperty("serialNumber", computerSystem.getSerialNumber());

		JsonObject firmware = new JsonObject();

		firmware.addProperty("name", computerSystem.getFirmware().getName());
		firmware.addProperty("version", computerSystem.getFirmware().getVersion());
		firmware.addProperty("manufacturer", computerSystem.getFirmware().getManufacturer());
		firmware.addProperty("description", computerSystem.getFirmware().getDescription());
		firmware.addProperty("date", computerSystem.getFirmware().getReleaseDate());

		computer.add("firmware", firmware);

		JsonObject baseboard = new JsonObject();

		baseboard.addProperty("manufacturer", computerSystem.getBaseboard().getManufacturer());
		baseboard.addProperty("model", computerSystem.getBaseboard().getModel());
		baseboard.addProperty("version", computerSystem.getBaseboard().getVersion());
		baseboard.addProperty("serialNumber", computerSystem.getBaseboard().getSerialNumber());

		computer.add("baseboard", baseboard);

		JsonObject memory = new JsonObject();

		memory.addProperty("total", globalMemory.getTotal());
		memory.addProperty("page", globalMemory.getPageSize());

		JsonObject virtualMemory = new JsonObject();

		virtualMemory.addProperty("swap_total", globalMemory.getVirtualMemory().getSwapTotal());
		virtualMemory.addProperty("swap_used", globalMemory.getVirtualMemory().getSwapUsed());
		virtualMemory.addProperty("swap_pages_in", globalMemory.getVirtualMemory().getSwapPagesIn());
		virtualMemory.addProperty("swap_pages_out", globalMemory.getVirtualMemory().getSwapPagesOut());
		virtualMemory.addProperty("virtual_max", globalMemory.getVirtualMemory().getVirtualMax());
		virtualMemory.addProperty("virtual_use", globalMemory.getVirtualMemory().getVirtualInUse());

		memory.add("virtual", virtualMemory);

		JsonArray physicalMemories = new JsonArray();

		for (PhysicalMemory physicalGlobalMemory : globalMemory.getPhysicalMemory()) {

			JsonObject phyMemory = new JsonObject();

			phyMemory.addProperty("speed", physicalGlobalMemory.getClockSpeed());
			phyMemory.addProperty("manufacturer", physicalGlobalMemory.getManufacturer());
			phyMemory.addProperty("type", physicalGlobalMemory.getMemoryType());
			phyMemory.addProperty("bank", physicalGlobalMemory.getBankLabel());
			phyMemory.addProperty("capacity", physicalGlobalMemory.getCapacity());

			physicalMemories.add(phyMemory);

		}

		memory.add("physical", physicalMemories);

		JsonArray sounds = new JsonArray();

		for (SoundCard card : layer.getSoundCards()) {

			JsonObject sound = new JsonObject();

			sound.addProperty("name", card.getName());
			sound.addProperty("codec", card.getCodec());
			sound.addProperty("driver-version", card.getDriverVersion());

			sounds.add(sound);

		}

		JsonArray networks = new JsonArray();

		for (NetworkIF nif : layer.getNetworkIFs()) {

			JsonObject network = new JsonObject();

			network.addProperty("name", nif.getName());
			network.addProperty("displayName", nif.getDisplayName());
			network.addProperty("bytesRecv", nif.getBytesRecv());
			network.addProperty("bytesSent", nif.getBytesSent());
			network.addProperty("collisions", nif.getCollisions());
			network.addProperty("alias", nif.getIfAlias());
			network.addProperty("type", nif.getIfType());
			network.addProperty("index", nif.getIndex());
			network.addProperty("inDrops", nif.getInDrops());
			network.addProperty("inErrors", nif.getInErrors());
			network.addProperty("macAddr", nif.getMacaddr());
			network.addProperty("mtu", nif.getMTU());
			network.addProperty("ndisPhysicalMediumType", nif.getNdisPhysicalMediumType());
			network.addProperty("outErrors", nif.getOutErrors());
			network.addProperty("packetsRecv", nif.getPacketsRecv());
			network.addProperty("packetsSent", nif.getPacketsSent());
			network.addProperty("speed", nif.getSpeed());
			network.addProperty("timestamp", nif.getTimeStamp());
			network.addProperty("connectorPresent", nif.isConnectorPresent());
			network.addProperty("knownVMMacAddr", nif.isKnownVmMacAddr());
			network.addProperty("operStatus", nif.getIfOperStatus().name());

			JsonArray prefixLengths = new JsonArray();

			for (short s : nif.getPrefixLengths()) {
				prefixLengths.add(s);
			}

			network.add("prefixLengths", prefixLengths);

			JsonArray subnetMasks = new JsonArray();

			for (short s : nif.getSubnetMasks()) {
				subnetMasks.add(s);
			}

			network.add("subnetMasks", subnetMasks);

			networks.add(network);
		}

		JsonObject sensors = new JsonObject();

		sensors.addProperty("cpuTemerpature", layer.getSensors().getCpuTemperature());
		sensors.addProperty("cpuVoltage", layer.getSensors().getCpuVoltage());

		JsonArray fanSpeeds = new JsonArray();

		for (int i : layer.getSensors().getFanSpeeds()) {
			fanSpeeds.add(i);
		}

		sensors.add("fanSpeeds", fanSpeeds);

		JsonArray displays = new JsonArray();

		for (Display display : layer.getDisplays()) {

			JsonArray array = new JsonArray();

			for (byte b : display.getEdid()) {
				array.add(b);
			}

			displays.add(array);

		}

		JsonArray disks = new JsonArray();

		for (HWDiskStore hwdisk : layer.getDiskStores()) {

			JsonObject disk = new JsonObject();

			disk.addProperty("name", hwdisk.getName());
			disk.addProperty("model", hwdisk.getModel());
			disk.addProperty("timestamp", hwdisk.getTimeStamp());

			disk.addProperty("reads", hwdisk.getReads());
			disk.addProperty("readBytes", hwdisk.getReadBytes());
			disk.addProperty("serial", hwdisk.getSerial());
			disk.addProperty("size", hwdisk.getSize());
			disk.addProperty("writes", hwdisk.getWrites());
			disk.addProperty("writeBytes", hwdisk.getWriteBytes());
			disk.addProperty("transferTime", hwdisk.getTransferTime());
			disk.addProperty("queueLength", hwdisk.getCurrentQueueLength());

			disks.add(disk);

		}

		JsonArray powers = new JsonArray();

		for (PowerSource source : layer.getPowerSources()) {

			JsonObject power = new JsonObject();

			power.addProperty("manufacturer", source.getManufacturer());
			power.addProperty("name", source.getName());
			power.addProperty("serialNumber", source.getSerialNumber());
			power.addProperty("amperage", source.getAmperage());
			power.addProperty("currentCapacity", source.getCurrentCapacity());
			power.addProperty("chemistry", source.getChemistry());
			power.addProperty("cycleCount", source.getCycleCount());
			power.addProperty("designCapacity", source.getDesignCapacity());
			power.addProperty("deviceName", source.getDeviceName());
			power.addProperty("maxCapacity", source.getMaxCapacity());
			power.addProperty("powerUsage", source.getPowerUsageRate());
			power.addProperty("remainingCapacityPercent", source.getRemainingCapacityPercent());
			power.addProperty("temperature", source.getTemperature());
			power.addProperty("timeRemainingEstimated", source.getTimeRemainingEstimated());
			power.addProperty("timeRemainingInstant", source.getTimeRemainingInstant());
			power.addProperty("voltage", source.getVoltage());
			power.addProperty("capacityUnits", source.getCapacityUnits().name());

			powers.add(power);

		}

		/*
		 * JsonArray usbDevices = new JsonArray();
		 * 
		 * for(UsbDevice device : layer.getUsbDevices(true)){
		 * 
		 * JsonObject usb = new JsonObject();
		 * 
		 * usb.addProperty("name", device.getName()); usb.addProperty("serialNumber",
		 * device.getSerialNumber()); usb.addProperty("vendor", device.getVendor());
		 * usb.addProperty("id", device.getUniqueDeviceId());
		 * usb.addProperty("productId", device.getProductId());
		 * usb.addProperty("vendorId", device.getVendorId());
		 * 
		 * JsonArray parents = new JsonArray();
		 * 
		 * for(UsbDevice dev : device.getConnectedDevices()){
		 * 
		 * JsonObject parent = new JsonObject();
		 * 
		 * parent.addProperty("name", dev.getName()); parent.addProperty("serialNumber",
		 * dev.getSerialNumber()); parent.addProperty("vendor", dev.getVendor());
		 * parent.addProperty("id", dev.getUniqueDeviceId());
		 * parent.addProperty("productId", dev.getProductId());
		 * parent.addProperty("vendorId", dev.getVendorId());
		 * 
		 * parents.add(parent);
		 * 
		 * }
		 * 
		 * usb.add("parents", parents);
		 * 
		 * usbDevices.add(usb);
		 * 
		 * }
		 */

		object.add("computer", computer);
		object.add("cpu", cpu);
		object.add("gpu", gpus);
		object.add("memory", memory);
		object.add("powers", powers);
		object.add("sounds", sounds);
		object.add("networks", networks);
		object.add("sensors", sensors);
		object.add("displays", displays);
		object.add("disks", disks);
		// object.add("usbs", usbDevices);

		return object;

	}

}
