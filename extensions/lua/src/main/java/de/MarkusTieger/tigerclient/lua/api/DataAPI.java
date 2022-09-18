package de.MarkusTieger.tigerclient.lua.api;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.tigerclient.utils.module.ScreenPosition;
import net.minecraft.client.Minecraft;

public class DataAPI extends TwoArgFunction {

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {

		LuaValue library = tableOf();

		library.set("createStaticBounds", new createStaticBounds());
		library.set("createDynamicBounds",  new createDynamicBounds());

		library.set("createStaticScreenLocationFromAbsolute", new createStaticScreenLocationFromAbsolute());
		library.set("createStaticScreenLocationFromRelative",  new createStaticScreenLocationFromRelative());
		library.set("newDynamicScreenLocationBuilder",  new newDynamicScreenLocationBuilder());

		library.set("newConfigObject", new newConfigObject());
		library.set("newConfigArray", new newConfigArray());
		
		env.set("data", library);

		return library;
	}
	
	public static class newConfigObject extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			final JsonObject obj = new JsonObject();
			return new LuaObjectData(() -> obj);
		}
		
	}
	
	public static class newConfigArray extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			final JsonArray array = new JsonArray();
			return new LuaArrayData(() -> array);
		}
		
	}
	
	public static class LuaArrayData extends LuaJsonData<JsonArray, Integer> {

		public LuaArrayData(Supplier<JsonArray> sup) {
			super(sup);
			
			
			set("remove", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					return LuaValue.valueOf(LuaArrayData.this.rm(arg.checkint()));
				}
			});
			
			set("has", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					return LuaValue.valueOf(has(arg.checkint()));
				}
			});
			
			set("add", new TwoArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg, LuaValue val) {
					LuaArrayData.this.add(arg.checkint(), property(val));
					return LuaValue.NIL;
				}
			});
			
			set("isString", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isString(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("isBoolean", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isBoolean(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("isNumber", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			
			set("asString", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isString(str)) return LuaValue.NIL;
					return LuaValue.valueOf(asString(str));
				}
			});
			
			set("asBoolean", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isBoolean(str)) return LuaValue.NIL;
					return LuaValue.valueOf(asBoolean(str));
				}
			});
			
			set("isInt", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Integer);
				}
			});
			
			set("isLong", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Long);
				}
			});
			
			set("isDouble", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Double);
				}
			});
			
			
			
			
			
			set("asInt", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Integer d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.intValue());
				}
			});
			
			set("asLong", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Long d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.longValue());
				}
			});
			
			set("asDouble", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Double d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.doubleValue());
				}
			});
			
			
			set("isArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isArray(str)) return LuaValue.NIL;
					
					return asArray(str);
				}
			});
			
			set("isArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isArray(str)) return LuaValue.NIL;
					
					return asArray(str);
				}
			});
			
			set("isObject", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asObject", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					int str = arg.checkint();
					if(!has(str)) return LuaValue.NIL;
					if(!isObject(str)) return LuaValue.NIL;
					
					return asObject(str);
				}
			});
			
		}

		@Override
		public boolean rm(Integer index) {
			if(has(index)) {
				sup.get().remove(index);
				return true;
			}
			return false;
		}

		@Override
		public boolean has(Integer index) {
			return sup.get().size() < index;
		}

		@Override
		public JsonElement getVal(Integer index) {
			return sup.get().get(index);
		}

		@Override
		public void add(Integer index, JsonElement val) {
			sup.get().add(val);
		}
		
	}
	
	public static class LuaObjectData extends LuaJsonData<JsonObject, String> {
		
		public LuaObjectData(Supplier<JsonObject> sup) {
			super(sup);
			
			set("remove", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					return LuaValue.valueOf(rm(arg.checkjstring()));
				}
			});
			
			set("has", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					return LuaValue.valueOf(has(arg.checkjstring()));
				}
			});
			
			set("add", new TwoArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg, LuaValue val) {
					LuaObjectData.this.add(arg.checkjstring(), property(val));
					return LuaValue.NIL;
				}
			});
			
			set("isString", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isString(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("isBoolean", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isBoolean(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("isNumber", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			
			set("asString", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isString(str)) return LuaValue.NIL;
					return LuaValue.valueOf(asString(str));
				}
			});
			
			set("asBoolean", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isBoolean(str)) return LuaValue.NIL;
					return LuaValue.valueOf(asBoolean(str));
				}
			});
			
			set("isInt", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Integer);
				}
			});
			
			set("isLong", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Long);
				}
			});
			
			set("isDouble", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isNumber(str)) return LuaValue.FALSE;
					
					return LuaValue.valueOf(asNumber(str) instanceof Double);
				}
			});
			
			
			
			
			
			set("asInt", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Integer d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.intValue());
				}
			});
			
			set("asLong", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Long d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.longValue());
				}
			});
			
			set("asDouble", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isNumber(str)) return LuaValue.NIL;
					if(!(asNumber(str) instanceof Double d)) return LuaValue.NIL;
					
					return LuaValue.valueOf(d.doubleValue());
				}
			});
			
			
			set("isArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isArray(str)) return LuaValue.NIL;
					
					return asArray(str);
				}
			});
			
			set("isArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asArray", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isArray(str)) return LuaValue.NIL;
					
					return asArray(str);
				}
			});
			
			set("isObject", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.FALSE;
					if(!isArray(str)) return LuaValue.FALSE;
					
					return LuaValue.TRUE;
				}
			});
			
			set("asObject", new OneArgFunction() {
				
				@Override
				public LuaValue call(LuaValue arg) {
					String str = arg.checkjstring();
					if(!has(str)) return LuaValue.NIL;
					if(!isObject(str)) return LuaValue.NIL;
					
					return asObject(str);
				}
			});
		}

		@Override
		public boolean rm(String name) {
			if(has(name)) {
				sup.get().remove(name);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean has(String name) {
			if(name == null) return false;
			return sup.get().has(name);
		}
		

		@Override
		public JsonElement getVal(String index) {
			return sup.get().get(index);
		}

		@Override
		public void add(String index, JsonElement val) {
			sup.get().add(index, val);
		}
		
	}
	
	public static abstract class LuaJsonData<T extends JsonElement, E> extends LuaTable {
		
		protected final Supplier<T> sup;
		
		public LuaJsonData(Supplier<T> sup) {
			this.sup = sup;
		}
		
		public abstract boolean rm(E index);
		
		public abstract boolean has(E index);
		
		public abstract void add(E index, JsonElement val);
		
		public JsonElement property(LuaValue val) {
			if(val.isnumber()) {
				LuaNumber num = val.checknumber();
				
				if(num.isint()) return new JsonPrimitive(Integer.valueOf(num.checkint()));
				else if(num.islong()) return new JsonPrimitive(Long.valueOf(num.checklong()));
				else return new JsonPrimitive(Double.valueOf(num.checkdouble()));
			}
			
			if(val.isboolean()) return new JsonPrimitive(Boolean.valueOf(val.checkboolean()));
			if(val.isstring()) return new JsonPrimitive(val.checkjstring());
			
			if(val instanceof LuaArrayData lad) return lad.sup.get();
			
			throw new LuaError("Unknown type named \"" + val.typename() + "\"");
		}
		
		public abstract JsonElement getVal(E index);
		
		public boolean isString(E index) {
			JsonElement ele = getVal(index);
			return ele.isJsonPrimitive() && ele.getAsJsonPrimitive().isString();
		}

		public boolean isBoolean(E index) {
			JsonElement ele = getVal(index);
			return ele.isJsonPrimitive() && ele.getAsJsonPrimitive().isBoolean();
		}

		public String asString(E index) {
			return getVal(index).getAsString();
		}

		public boolean asBoolean(E index) {
			return getVal(index).getAsBoolean();
		}

		public boolean isNumber(E index) {
			JsonElement ele = getVal(index);
			return ele.isJsonPrimitive() && ele.getAsJsonPrimitive().isNumber();
		}

		public Number asNumber(E index) {
			return getVal(index).getAsNumber();
		}
		
		public boolean isArray(E index) {
			JsonElement ele = getVal(index);
			return ele.isJsonArray();
		}
		
		public LuaArrayData asArray(E index) {
			final JsonArray d = getVal(index).getAsJsonArray();
			return new LuaArrayData(() -> d);
		}
		
		public boolean isObject(E index) {
			JsonElement ele = getVal(index);
			return ele.isJsonObject();
		}
		
		public LuaObjectData asObject(E index) {
			final JsonObject d = getVal(index).getAsJsonObject();
			return new LuaObjectData(() -> d);
		}
		
	}

	public static class createStaticScreenLocationFromAbsolute extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue x, LuaValue y) {
			return new LuaScreenLocationData(ScreenPosition.fromAbsolutePosition(x.checkint(), y.checkint()));
		}

	}

	public static class createStaticScreenLocationFromRelative extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue x, LuaValue y) {
			return new LuaScreenLocationData(ScreenPosition.fromRelativePosition(x.checkdouble(), y.checkdouble()));
		}

	}

	public static class newDynamicScreenLocationBuilder extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			return new DynamicScreenLocationBuilder();
		}

	}

	public static class DynamicScreenLocationBuilder extends LuaTable {

		private LuaFunction getAbsoluteX, getAbsoluteY, getRelativeX, getRelativeY, setAbsolute, setRelative;

		public DynamicScreenLocationBuilder() {

			set("getAbsoluteX", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.getAbsoluteX = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});
			set("getAbsoluteY", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.getAbsoluteY = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});
			set("getRelativeX", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.getRelativeX = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});
			set("getRelativeY", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.getRelativeY = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});
			set("setAbsolute", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.setAbsolute = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});
			set("setRelative", new OneArgFunction() {

				@Override
				public LuaValue call(LuaValue arg) {
					DynamicScreenLocationBuilder.this.setRelative = arg.checkfunction();
					return DynamicScreenLocationBuilder.this;
				}
			});

			set("build", new ZeroArgFunction() {

				@Override
				public LuaValue call() {
					if(getRelativeX == null || getRelativeY == null || getAbsoluteX == null || getAbsoluteY == null || setAbsolute == null || setRelative == null) throw new LuaError("Not All Fields are present.");
					return new LuaScreenLocationData(
							() -> getAbsoluteX.call().checkdouble(),
							() -> getAbsoluteY.call().checkdouble(),
							() -> getRelativeX.call().checkdouble(),
							() -> getRelativeY.call().checkdouble(),
							(x, y) -> setAbsolute.call(LuaValue.valueOf(x), LuaValue.valueOf(y)),
							(x, y) -> setRelative.call(LuaValue.valueOf(x), LuaValue.valueOf(y))
							);
				}
			});

		}

	}

	public static class createStaticBounds extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue width, LuaValue height) {
			return new LuaBoundsData(width.checkint(), height.checkint());
		}

	}

	public static class createDynamicBounds extends TwoArgFunction {

		@Override
		public LuaValue call(LuaValue width, LuaValue height) {
			LuaFunction widthfunc = width.checkfunction();
			LuaFunction heightfunc = height.checkfunction();

			return new LuaBoundsData(() -> widthfunc.call().checkint(), () -> heightfunc.call().checkint());
		}

	}

	public static class LuaBoundsData extends LuaTable {

		private final Supplier<Integer> width, height;

		public LuaBoundsData(Supplier<Integer> width, Supplier<Integer> height) {
			this.width = width;
			this.height = height;

			set("getX", new ZeroArgFunction() {

				@Override
				public LuaValue call() {
					return LuaValue.valueOf(width.get().intValue());
				}
			});
			set("getY", new ZeroArgFunction() {

				@Override
				public LuaValue call() {
					return LuaValue.valueOf(height.get().intValue());
				}
			});
		}

		public LuaBoundsData(int width, int height) {
			this((Supplier<Integer>) () -> width, (Supplier<Integer>) () -> height);
		}

		public int getWidth() {
			return width.get();
		}

		public int getHeight() {
			return height.get();
		}

		@Override
		public String typename() {
			return "bounds";
		}

	}

	public static class LuaScreenLocationData extends LuaTable {

		private final ScreenPosition sp;

		private final Supplier<Double> getAbsoluteX;
		private final Supplier<Double> getAbsoluteY;
		private final Supplier<Double> getRelativeX;
		private final Supplier<Double> getRelativeY;

		private final BiConsumer<Double, Double> setAbsolute;
		private final BiConsumer<Double, Double> setRelative;

		public LuaScreenLocationData(ScreenPosition sp) {
			this(sp, sp::getAbsouluteX, sp::getAbsouluteY, sp::getRelativeX, sp::getRelativeY, sp::setAbsolute,
					sp::setRelative);
		}

		public LuaScreenLocationData(Supplier<Double> getAbsoluteX, Supplier<Double> getAbsoluteY,
				Supplier<Double> getRelativeX, Supplier<Double> getRelativeY, BiConsumer<Double, Double> setAbsolute,
				BiConsumer<Double, Double> setRelative) {
			this(null, getAbsoluteX, getAbsoluteY, getRelativeX, getRelativeY, setAbsolute, setRelative);
		}

		public LuaScreenLocationData(ScreenPosition sp, Supplier<Double> getAbsoluteX, Supplier<Double> getAbsoluteY,
				Supplier<Double> getRelativeX, Supplier<Double> getRelativeY, BiConsumer<Double, Double> setAbsolute,
				BiConsumer<Double, Double> setRelative) {
			this.getAbsoluteX = getAbsoluteX;
			this.getAbsoluteY = getAbsoluteY;
			this.getRelativeX = getRelativeX;
			this.getRelativeY = getRelativeY;

			this.setAbsolute = setAbsolute;
			this.setRelative = setRelative;

			if (sp == null) {
				this.sp = new ScreenPosition(0D, 0D) {

					@Override
					public double getAbsouluteX() {
						return LuaScreenLocationData.this.getAbsoluteX();
					}

					@Override
					public double getAbsouluteY() {
						return LuaScreenLocationData.this.getAbsoluteY();
					}

					@Override
					public double getRelativeX() {
						return LuaScreenLocationData.this.getRelativeX();
					}

					@Override
					public double getRelativeY() {
						return LuaScreenLocationData.this.getRelativeY();
					}

					@Override
					public void setAbsolute(double x, double y) {
						LuaScreenLocationData.this.setAbsolute(x, y);
					}

					@Override
					public void setRelative(double x, double y) {
						LuaScreenLocationData.this.setRelative(x, y);
					}

				};
			} else
				this.sp = sp;
		}

		public double getAbsoluteX() {
			return getAbsoluteX.get();
		}

		public double getAbsoluteY() {
			return getAbsoluteY.get();
		}

		public double getRelativeX() {
			return getRelativeX.get();
		}

		public double getRelativeY() {
			return getRelativeY.get();
		}

		public void setAbsolute(double x, double y) {
			setAbsolute.accept(x, y);
		}

		public void setRelative(double x, double y) {
			setRelative.accept(x, y);
		}

		@Override
		public String typename() {
			return "location";
		}

		public ScreenPosition asScreenPosition() {
			return sp;
		}

		public static class FixedLuaScreenLocationData extends LuaScreenLocationData {

			private static final Window window = Minecraft.getInstance().getWindow();

			public FixedLuaScreenLocationData(int x, int y) {
				super(
						() -> (double) x,
						() -> (double) y,
						() -> (double) (x / window.getGuiScaledWidth()),
						() -> (double) (y / window.getGuiScaledHeight()),
						(v, o) -> {},
						(v, o) -> {}
						);
			}



		}

	}

	public static class LuaRenderData extends LuaTable {

		private final LuaScreenLocationData location;
		private final LuaBoundsData bounds;

		public LuaRenderData(LuaScreenLocationData location, LuaBoundsData bounds) {
			this.location = location;
			this.bounds = bounds;

			set("location", location);
			set("bounds", bounds);
		}

		public LuaScreenLocationData getLocation() {
			return location;
		}

		public LuaBoundsData getBounds() {
			return bounds;
		}

	}

	public static class LuaStack extends LuaTable {

		private final PoseStack handler;

		public LuaStack(PoseStack stack) {
			this.handler = stack;

		}

		public PoseStack getHandler() {
			return handler;
		}

	}

	public static interface LuaConvertable {

		@SuppressWarnings("unchecked")
		default public <T> T castElseThrowLuaError() {
			try {
				return (T) this;
			} catch(ClassCastException ex) {
				throw new LuaError(ex);
			}
		}

		public static <T> T castElseThrowLuaError(LuaValue result) {
			if(!(result instanceof LuaConvertable d)) {
				throw new LuaError("Invalid Result.");
			}

			return d.<T>castElseThrowLuaError();
		}

	}

}
