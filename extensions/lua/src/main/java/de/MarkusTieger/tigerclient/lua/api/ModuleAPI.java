package de.MarkusTieger.tigerclient.lua.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.modules.IModuleRegistry;
import de.MarkusTieger.common.utils.IConfigable;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.IKeyable;
import de.MarkusTieger.common.utils.IMouseable;
import de.MarkusTieger.common.utils.IMultiDrag;
import de.MarkusTieger.common.utils.IPacketEditor;
import de.MarkusTieger.common.utils.IPacketEditor.PacketSides;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.tigerclient.data.Data;
import de.MarkusTieger.tigerclient.lua.LuaGlobalsGenerator.BooleanSuppliedGlobals;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaBoundsData;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaConvertable;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaRenderData;
import de.MarkusTieger.tigerclient.lua.api.DataAPI.LuaScreenLocationData;
import de.MarkusTieger.tigerclient.lua.converter.LuaConverterRegistry;
import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ModuleAPI extends TwoArgFunction {

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {

		BooleanSupplier supplier = () -> true;

		if (env instanceof BooleanSuppliedGlobals bsg)
			supplier = bsg.getSupplier();

		LuaValue library = tableOf();

		library.set("register", new register(supplier));
		library.set("unregister", new unregister());

		library.set("newModuleBuilder", new newModuleBuilder(supplier));

		env.set("modules", library);

		return library;
	}


	public static class register extends OneArgFunction {

		private final BooleanSupplier supplier;

		public register(BooleanSupplier supplier) {
			this.supplier = supplier;
		}

		@Override
		public LuaValue call(LuaValue arg) {
			if(!supplier.getAsBoolean()) throw new LuaError("Supplier says no");

			LuaModule module = LuaConvertable.<LuaModule>castElseThrowLuaError(arg);

			if(!module.isValid()) throw new LuaError("Module is invalid.");

			IModuleRegistry reg = Client.getInstance().getModuleRegistry();
			reg.register(module);
			reg.update();

			return LuaValue.NIL;
		}

	}

	public static class unregister extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			LuaModule module = LuaConvertable.<LuaModule>castElseThrowLuaError(arg);

			IModuleRegistry reg = Client.getInstance().getModuleRegistry();
			reg.unregister(module);
			reg.update();

			return LuaValue.NIL;
		}

	}



	public static class newModuleBuilder extends ZeroArgFunction {

		private final BooleanSupplier supplier;

		public newModuleBuilder(BooleanSupplier supplier) {
			this.supplier = supplier;
		}

		@Override
		public LuaValue call() {
			return new ModuleBuilder(supplier);
		}

	}

	public static record LuaDraggable(ModuleBuilder builder, Data<String> id, LuaScreenLocationData location, LuaBoundsData bounds, LuaFunction render, LuaFunction renderDummy) implements IDraggable<LuaError> {

		public LuaTable asTable() {
			LuaTable table = LuaValue.tableOf();

			table.set("builder", builder);
			table.set("customId", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					id.setData(arg.checkjstring());
					return table;
				}
			});

			return table;
		}

		@Override
		public int getHeight() throws LuaError {
			return bounds.getHeight();
		}

		@Override
		public int getWidth() throws LuaError {
			return bounds.getWidth();
		}

		@Override
		public ScreenPosition load() throws LuaError {
			return location.asScreenPosition();
		}

		@Override
		public void render(PoseStack stack, ScreenPosition pos) throws LuaError {
			render.call(LuaConverterRegistry.INSTANCE.map(stack), LuaConverterRegistry.INSTANCE.map(pos));
		}

		@Override
		public void renderDummy(PoseStack stack, ScreenPosition pos) throws LuaError {
			renderDummy.call(LuaConverterRegistry.INSTANCE.map(stack), LuaConverterRegistry.INSTANCE.map(pos));
		}

		@Override
		public void save(ScreenPosition screenPosition) throws LuaError {
			location.setAbsolute(screenPosition.getAbsouluteX(), screenPosition.getAbsouluteY());
		}

		@Override
		public String getId() {
			return id.getData();
		}

	}

	public static class LuaConfigable {

		private final BooleanSupplier isConfigable;
		private final Consumer<LuaValue> configure;
		private final Runnable reset;
		private final LuaFunction renderOverlay;

		public LuaConfigable(BooleanSupplier isConfigable, Consumer<LuaValue> configure, Runnable reset, LuaFunction renderOverlay) {
			this.isConfigable = isConfigable;
			this.configure = configure;
			this.reset = reset;
			this.renderOverlay = renderOverlay;
		}

		public void configure(Screen parent) throws LuaError {
			configure.accept(LuaConverterRegistry.INSTANCE.map(parent));
		}

		public boolean isConfigable() throws LuaError {
			return isConfigable.getAsBoolean();
		}

		public void reset() throws LuaError {
			this.reset.run();
		}

		public void renderOverlay(PoseStack stack, int x, int y, int width, int height, boolean hover) throws LuaError {
			if (renderOverlay != null) renderOverlay.call(
					LuaConverterRegistry.INSTANCE.map(stack),
					new LuaRenderData(
							new LuaScreenLocationData.FixedLuaScreenLocationData(x, y),
							new LuaBoundsData(width, height)
					),
					LuaValue.valueOf(hover));
		}

		public static class EmptyLuaConfigable extends LuaConfigable {

			public EmptyLuaConfigable() {
				super(() -> false, (lua) -> {}, () -> {}, null);
			}

		}

	}

	public static record LuaKeyable(LuaFunction onKey) {

		public boolean onKey(int keyCode, int flag) {
			return onKey.call(LuaValue.valueOf(keyCode), LuaValue.valueOf(flag)).checkboolean();
		}

	}

	public static record LuaMousable(LuaFunction left, LuaFunction right, LuaFunction middle) {

		public boolean onLeftClick(int action) throws LuaError {
			return left.call(LuaValue.valueOf(action)).checkboolean();
		}

		public boolean onMiddleClick(int action) throws LuaError {
			return middle.call(LuaValue.valueOf(action)).checkboolean();
		}

		public boolean onRightClick(int action) throws LuaError {
			return right.call(LuaValue.valueOf(action)).checkboolean();
		}
	}

	public static record LuaPacketEditor(LuaFunction accept, LuaFunction edit, PacketSides sides) {

		public boolean accept(Packet<?> p) throws LuaError {
			return accept.call(LuaConverterRegistry.INSTANCE.map(p)).checkboolean();
		}

		public <T extends Packet<?>> T edit(T p) throws LuaError {
			LuaValue lv = LuaConverterRegistry.INSTANCE.map(p);

			LuaValue result = edit.call(lv);

			return LuaConvertable.<T>castElseThrowLuaError(result);
		}

	}

	@SuppressWarnings("unused")
	public static class LuaModule extends LuaTable implements LuaConvertable, IModule<LuaError>, IConfigable<LuaError>, IPacketEditor<LuaError>, IMultiDrag<LuaError>, ITickable<LuaError>, IHighspeedTick<LuaError>, IKeyable<LuaError>, IMouseable<LuaError> {

		private final BooleanSupplier supplier;

		private final String id;
		private final String displayName;
		private final String description;
		private final String icon;
		private final String searchname;

		private final LuaEnableHandler enableHandler;

		private final List<LuaDraggable> drags;

		private final LuaFunction tick;
		private final LuaFunction htick;

		private final LuaConfigable config;
		private final LuaKeyable key;
		private final LuaMousable mouse;
		private final LuaPacketEditor packet;

		private final Component displayName_comp, description_comp, narration_comp;

		private final ResourceLocation icon_location;

		private DynamicTexture binded_icon = null;

		public LuaModule(
				BooleanSupplier supplier,
				String id, String displayName, String description,
				String icon, String searchname, String narration,
				LuaEnableHandler enableHandler,
				List<LuaDraggable> drags,
				LuaFunction tick, LuaFunction htick,
				LuaConfigable config,
				LuaKeyable key, LuaMousable mouse,
				LuaPacketEditor packet) {
			this.supplier = supplier;

			this.id = Objects.requireNonNull(id);
			this.displayName = Objects.requireNonNull(displayName);
			this.description = Objects.requireNonNull(description);
			this.icon = icon;
			this.searchname = Objects.requireNonNull(searchname);

			this.enableHandler = Objects.requireNonNull(enableHandler);

			this.drags = Objects.requireNonNull(drags);
			this.tick = tick;
			this.htick = htick;
			this.config = config;
			this.key = key;
			this.mouse = mouse;
			this.packet = packet;


			this.icon_location = new ResourceLocation("lua", "textures/modules/" + id + "/icon_" + UUID.randomUUID());

			this.displayName_comp = new TextComponent(displayName);
			this.description_comp = new TextComponent(description);
			this.narration_comp = new TextComponent(narration);
		}

		public boolean isValid() {
			for(IModule<?> mod : Client.getInstance().getModuleRegistry().toArray()) {
				if(mod.getId().equalsIgnoreCase(id)) return false;
			}
			return true;
		}

		@Override
	    public Component getInfo() {
	        return isEnabled() ? new TranslatableComponent("modules.enabled") : new TranslatableComponent("modules.disabled");
	    }

		@Override
		public Component getNarration() {
			return this.narration_comp;
		}

		@Override
		public boolean isDisabled() {
			return false;
		}

		@Override
		public String getSearchName() {
			return searchname;
		}

		@Override
		public void renderOverlay0(PoseStack stack, int x, int y, int width, int height, boolean hover) throws LuaError {
			config.renderOverlay(stack, x, y, width, height, hover);
		}

		@Override
		public boolean onLeftClick(int action) throws LuaError {
			if(supplier.getAsBoolean()) return mouse.onLeftClick(action);
			return false;
		}

		@Override
		public boolean onMiddleClick(int action) throws LuaError {
			if(supplier.getAsBoolean()) return mouse.onMiddleClick(action);
			return false;
		}

		@Override
		public boolean onRightClick(int action) throws LuaError {
			if(supplier.getAsBoolean()) return mouse.onRightClick(action);
			return false;
		}

		@Override
		public boolean allowMouseable() {
			return mouse != null && supplier.getAsBoolean();
		}

		@Override
		public boolean onKey(int keyCode, int flag) throws LuaError {
			return key.onKey(keyCode, flag);
		}

		@Override
		public boolean allowKeyable() {
			return key != null && supplier.getAsBoolean();
		}

		@Override
		public void onHighTick() throws LuaError {
			if(supplier.getAsBoolean()) htick.call();
		}

		@Override
		public boolean allowHightick() {
			return htick != null && supplier.getAsBoolean();
		}

		@Override
		public void onTick() throws LuaError {
			if(supplier.getAsBoolean()) tick.call();
		}

		@Override
		public List<IDraggable<LuaError>> getDraggables() throws LuaError {
			return drags.stream().map((d) -> (IDraggable<LuaError>)d).toList();
		}

		@Override
		public boolean accept(Packet<?> p) throws LuaError {
			if(supplier.getAsBoolean()) return packet.accept(p);
			return false;
		}

		@Override
		public PacketSides getSides() {
			return packet.sides();
		}

		@Override
		public <T extends Packet<?>> T edit(T p) throws LuaError {
			if(supplier.getAsBoolean()) return packet.edit(p);
			return p;
		}

		@Override
		public boolean allowPacketEditor() {
			return packet != null && supplier.getAsBoolean();
		}

		@Override
		public boolean canConfigure() throws LuaError {
			if(!supplier.getAsBoolean()) return false;
			return config.isConfigable();
		}

		@Override
		public void configure(Screen parent) throws LuaError {
			if(!supplier.getAsBoolean()) return;
			config.configure(parent);
		}

		@Override
		public void reset() throws LuaError {
			if(!supplier.getAsBoolean()) return;
			config.reset();
		}

		@Override
		public void bindIcon() throws LuaError {
			if(icon != null && binded_icon == null) {
				try {
					NativeImage img = NativeImage.fromBase64(icon);

					binded_icon = new DynamicTexture(img);

					Minecraft.getInstance().getTextureManager().register(this.icon_location, this.binded_icon);
				} catch (IOException e) {
					throw new LuaError(e);
				}
			}
		}

		@Override
		public Component getDescription() {
			return description_comp;
		}

		@Override
		public Component getDisplayName() {
			return displayName_comp;
		}

		@Override
		public ResourceLocation getIcon() {
			return icon_location;
		}

		@Override
		public boolean isEnabled() throws LuaError {
			return enableHandler.isEnabled();
		}

		@Override
		public void setEnabled(boolean bool) throws LuaError {
			enableHandler.setEnabled(bool);
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public boolean allowTickable() {
			return tick != null && supplier.getAsBoolean();
		}

		@Override
		public boolean allowConfigable() {
			return config != null && supplier.getAsBoolean();
		}

	}

	public static class LuaEnableHandler extends LuaTable {

		protected Consumer<Boolean> set;
		protected BooleanSupplier get;

		public LuaEnableHandler(Consumer<Boolean> set, BooleanSupplier get) {
			this.set = Objects.requireNonNull(set);
			this.get = Objects.requireNonNull(get);

			this.applyTable();
		}

		protected void applyTable() {
			set("setEnabled", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					setEnabled(arg.checkboolean());
					return LuaValue.NIL;
				}
			});
			set("isEnabled", new ZeroArgFunction() {

				@Override
				public LuaValue call() {
					return LuaValue.valueOf(isEnabled());
				}
			});
		}

		private LuaEnableHandler() {}

		public void setEnabled(boolean enable) throws LuaError {
			set.accept(Boolean.valueOf(enable));
		}

		public boolean isEnabled() throws LuaError {
			return get.getAsBoolean();
		}

		public static class DefaultLuaEnableHandler extends LuaEnableHandler {

			private boolean value;

			public DefaultLuaEnableHandler(boolean def) {
				this.value = def;
				this.set = (v) -> value = v;
				this.get = () -> value;

				this.applyTable();
			}

		}

	}



	public static class ModuleBuilder extends LuaTable {

		private final BooleanSupplier supplier;

		// Base
		private String id = null;
		private String displayName = null;
		private String description = null;
		private String icon = null;
		private String searchname = null;
		private String narration = null;

		// Draggable
		private List<LuaDraggable> draggables = new ArrayList<>();

		// Tickable
		private LuaFunction tick = null;

		// High-Tickable
		private LuaFunction htick = null;

		// Configable
		private LuaConfigable configable = null;

		// Keyable
		private LuaKeyable key = null;

		// Mouseable
		private LuaMousable mouse = null;

		// PacketEditor
		private LuaPacketEditor packet = null;

		// Custom
		private LuaEnableHandler enableHandler = new LuaEnableHandler.DefaultLuaEnableHandler(false);

		public ModuleBuilder(BooleanSupplier supplier) {
			this.supplier = supplier;

			set("baseInformations", new baseInformations());
			set("makeTickable", new makeTickable());
			set("makeHighTickable", new makeHighTickable());
			set("addDraggable", new addDraggable());
			set("makeKeyable", new makeKeyable());
			set("makeMouseable", new makeMousable());
			set("makePacketEditorable", new makePacketEditorable());
			set("customEnableHandler", new customEnableHandler());
			set("setIcon", new setIcon());
			set("customNarration", new customNarration());
			set("customSearchname", new customSearchname());

			set("build", new buildModule());
		}

		public class buildModule extends ZeroArgFunction {

			@Override
			public LuaValue call() {
				return ModuleBuilder.this.buildModule();
			}

		}

		public LuaModule buildModule() {
			return new LuaModule(supplier,
					id, displayName, description,
					icon, searchname, narration,
					enableHandler,
					draggables,
					tick, htick,
					configable,
					key, mouse,
					packet);
		}

		public class customNarration extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue arg) {
				narration = arg.checkjstring();
				return ModuleBuilder.this;
			}

		}

		public class customSearchname extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue arg) {
				searchname = arg.checkjstring();
				return ModuleBuilder.this;
			}

		}

		public class setIcon extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue arg) {
				icon = arg.checkjstring();
				return ModuleBuilder.this;
			}

		}

		public class customEnableHandler extends LibFunction {

			@Override
			public LuaValue call(LuaValue set, LuaValue get) {
				enableHandler = new LuaEnableHandler((b) -> set.call(LuaValue.valueOf(b)), () -> get.call().checkboolean());
				return ModuleBuilder.this;
			}

			@Override
			public LuaValue call(LuaValue def) {
				enableHandler = new LuaEnableHandler.DefaultLuaEnableHandler(def.checkboolean());
				return ModuleBuilder.this;
			}

		}

		public class baseInformations extends ThreeArgFunction {

			@Override
			public LuaValue call(LuaValue id, LuaValue displayName, LuaValue description) {

				ModuleBuilder.this.id = id.checkjstring();
				ModuleBuilder.this.displayName = displayName.checkjstring();
				ModuleBuilder.this.description = description.checkjstring();

				if(ModuleBuilder.this.searchname == null) ModuleBuilder.this.searchname = ModuleBuilder.this.id;
				if(ModuleBuilder.this.narration == null) ModuleBuilder.this.narration = ModuleBuilder.this.displayName;

				return ModuleBuilder.this;
			}

		}

		public class makeConfigable extends LibFunction {

			@Override
			public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
				LuaFunction isConfigable = arg1.checkfunction();
				LuaFunction configure = arg2.checkfunction();
				LuaFunction reset = arg3.checkfunction();
				LuaFunction renderOverlay = arg4.checkfunction();

				ModuleBuilder.this.configable = new LuaConfigable(
						() -> isConfigable.call().checkboolean(),
						configure::call,
						reset::call,
						renderOverlay);

				return ModuleBuilder.this;
			}



		}

		public class makeMousable extends ThreeArgFunction {

			@Override
			public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
				LuaFunction left = arg1.checkfunction();
				LuaFunction right = arg2.checkfunction();
				LuaFunction middle = arg3.checkfunction();

				ModuleBuilder.this.mouse = new LuaMousable(left, right, middle);

				return ModuleBuilder.this;
			}



		}

		public class makeKeyable extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue arg) {
				ModuleBuilder.this.key = new LuaKeyable(arg.checkfunction());

				return ModuleBuilder.this;
			}



		}

		public class makePacketEditorable extends ThreeArgFunction {

			@Override
			public LuaValue call(LuaValue accept, LuaValue edit, LuaValue sides) {
				PacketSides side = null;
				try {
					side = PacketSides.values()[sides.checkint()];
				} catch(IndexOutOfBoundsException ex) {
					throw new LuaError(ex);
				}
				LuaFunction ac = accept.checkfunction();
				LuaFunction ed = edit.checkfunction();

				ModuleBuilder.this.packet = new LuaPacketEditor(ac, ed, side);

				return ModuleBuilder.this;
			}



		}

		public class makeTickable extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue tick) {
				ModuleBuilder.this.tick = tick.checkfunction();
				return ModuleBuilder.this;
			}

		}

		public class makeHighTickable extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue htick) {
				ModuleBuilder.this.htick = htick.checkfunction();
				return ModuleBuilder.this;
			}

		}

		public class addDraggable extends LibFunction {

			@Override
			public LuaValue call(LuaValue location, LuaValue bounds, LuaValue render, LuaValue renderdummy) {

				if (!(location instanceof LuaScreenLocationData loc) || !(bounds instanceof LuaBoundsData b)) {
					throw new LuaError("bad argument: " + "bounds" + " expected, got " + bounds.typename());
				}

				LuaFunction renderfunc = render.checkfunction();
				LuaFunction renderdummyfunc = renderdummy.checkfunction();

				if(id == null) throw new LuaError("The Module \"id\" is not set.");

				Data<String> d = Data.empty(String.class);
				d.setData(id + "#" + draggables.size());

				LuaDraggable draggable = new LuaDraggable(ModuleBuilder.this, d, loc, b, renderfunc, renderdummyfunc);

				ModuleBuilder.this.draggables.add(draggable);

				return LuaConverterRegistry.INSTANCE.map(draggable);
			}

		}

	}

}
