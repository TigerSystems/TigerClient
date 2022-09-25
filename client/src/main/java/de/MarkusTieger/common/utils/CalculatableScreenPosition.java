package de.MarkusTieger.common.utils;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.modules.IModuleRegistry;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

public class CalculatableScreenPosition {

	public static final Supplier<CalculatableScreenPosition> DEFAULT;
	
	static {
		
		DEFAULT = () -> new CalculatableScreenPosition() {
			
			private boolean initialized = false;
			
			private void init() {
				if(initialized) return;
				
				IModuleRegistry m = Client.getInstance().getModuleRegistry();
				if(m == null) return;
				if(m.getPositionLines() == null || m.getPositionLines().size() == 0) {
					return;
				}
				initialized = true;
				
				FixedScreenPosition fsp = new FixedScreenPosition(Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
				
				List<PositionLine> verticalList = m
						.getPositionLines()
						.stream()
						.filter(PositionLine::vertical)
						.toList();
				List<PositionLine> horizontalList = m
						.getPositionLines()
						.stream()
						.filter(PositionLine::horizontal)
						.toList();
				
				Pair<PositionLine, Boolean> vertical = nearest(fsp, verticalList);
				Pair<PositionLine, Boolean> horizontal = nearest(fsp, horizontalList);
				
				this.vLine = index(m.getPositionLines(), vertical.getKey());
				this.hLine = index(m.getPositionLines(), horizontal.getKey());
				
				this.offsetX = 0;
				this.offsetY = 0;
				this.store = StorePosition.MIDDLE;
			}
			
			@Override
			public void storeByFixed(StorePosition store, FixedScreenPosition fsp, int width, int height) {
				init();
				super.storeByFixed(store, fsp, width, height);
			}
			
			@Override
			public FixedScreenPosition calculateFixed(int width, int height) {
				init();
				return super.calculateFixed(width, height);
			}
			
			@Override
			public void changeStorePosition(StorePosition pos, int width, int height) {
				init();
				super.changeStorePosition(pos, width, height);
			}
			
			@Override
			public StorePosition getStorePosition() {
				init();
				return super.getStorePosition();
			}
			
		};
	}
	
	protected int offsetX = 0, offsetY = 0;
	protected int vLine = -1, hLine = -1;
	protected StorePosition store = StorePosition.MIDDLE;
	
	public static CalculatableScreenPosition createDefault(int sto, int offsetX, int offsetY, int vLine, int hLine) {
		CalculatableScreenPosition csp = new CalculatableScreenPosition();
		
		csp.store = StorePosition.values()[sto];
		csp.offsetX = offsetX;
		csp.offsetY = offsetY;
		csp.vLine = vLine;
		csp.hLine = hLine;
		
		return csp;
	}
	
	public void changeStorePosition(StorePosition pos, int width, int height) {
		
		FixedScreenPosition fsp = calculateFixed(width, height);
		store = pos;
		storeByFixed(fsp, width, height);
		
	}
	
	public StorePosition getStorePosition() {
		return store;
	}
	
	public static int index(List<?> l, Object data) {
		int pos = 0;
		for(Object obj : l) {
			if(obj == data) return pos;
			pos++;
		}
		
		return -1;
	}
	
	public static FixedScreenPosition storePoint(StorePosition store, FixedScreenPosition value, int width, int height) {
		
		CalculatableScreenPosition csp = new CalculatableScreenPosition();
		csp.storeByFixed(store, value, width, height);
		csp.store = StorePosition.LEFT_TOP;
		
		return csp.calculateFixed(width, height);
	}
	
	
	public void storeByFixed(FixedScreenPosition fixedScreenPosition, int width,
			int height) {
		this.storeByFixed(store, fixedScreenPosition, width, height);
	}
	
	public void storeByFixed(StorePosition store, FixedScreenPosition fsp, int width, int height) {
		this.store = store;
		
		double w = width * store.getWidth();
		double h = height * store.getHeight();
		
		double x = fsp.getX();
		double y = fsp.getY();
		
		x += w;
		y += h;
		
		List<PositionLine> verticalList = Client.getInstance().getModuleRegistry()
				.getPositionLines()
				.stream()
				.filter(PositionLine::vertical)
				.toList();
		List<PositionLine> horizontalList = Client.getInstance().getModuleRegistry()
				.getPositionLines()
				.stream()
				.filter(PositionLine::horizontal)
				.toList();
		
		PositionLine nearestV = nearest(
				new FixedScreenPosition((int) x, (int) y),
				verticalList
					).getKey();
		
		PositionLine nearestH = nearest(
				new FixedScreenPosition((int) x, (int) y),
				horizontalList
					).getKey();
		
		int vLine = index(Client.getInstance().getModuleRegistry()
				.getPositionLines(), nearestV);
		int hLine = index(Client.getInstance().getModuleRegistry()
				.getPositionLines(), nearestH);
		
		x -= nearestV.calculate();
		y -= nearestH.calculate();
		
		this.vLine = vLine;
		this.hLine = hLine;
		
		this.offsetX = (int) x;
		this.offsetY = (int) y;
	}
	
	public FixedScreenPosition calculateFixed(int width, int height) {
		
		IModuleRegistry module = Client.getInstance().getModuleRegistry();
		
		PositionLine vertical = module.getPositionLines().get(vLine);
		PositionLine horizontal = module.getPositionLines().get(hLine);
		
		double w = width * store.getWidth();
		double h = height * store.getHeight();
		
		double x = offsetX;
		double y = offsetY;
		
		x -= w;
		y -= h;
		
		x += vertical.calculate();
		y += horizontal.calculate();
		
		return new FixedScreenPosition((int) x, (int) y);
	}
	
	public static enum StorePosition {
		
		MIDDLE(0.5D, 0.5D, false, false, false, false),
		LEFT_TOP(0.0D, 0.0D, true, false, true, false),
		LEFT_MIDDLE(0.0D, 0.5D, false, false, true, false),
		LEFT_BUTTOM(0.0D, 1.0D, false, true, true, false),
		MIDDLE_TOP(0.5D, 0.0D, true, false, false, false),
		MIDDLE_BUTTOM(0.5D, 1.0D, false, true, false, false),
		RIGHT_TOP(1.0D, 0.0D, true, false, false, true),
		RIGHT_MIDDLE(1.0D, 0.5D, false, false, false, true),
		RIGHT_BUTTOM(1.0D, 1.0D, false, true, false, true)
		
		;
		
		private final double width, height;
		
		private final boolean top, buttom, left, right;
		
		private StorePosition(double width, double height, boolean top, boolean buttom, boolean left, boolean right) {
			this.width = width;
			this.height = height;
			
			this.top = top;
			this.buttom = buttom;
			this.left = left;
			this.right = right;
		}
		
		public boolean isTop() {
			return top;
		}
		
		public boolean isButtom() {
			return buttom;
		}
		
		public boolean isLeft() {
			return left;
		}
		
		public boolean isRight() {
			return right;
		}
		
		public double getWidth() {
			return width;
		}
		
		public double getHeight() {
			return height;
		}
		
	}

	public static CalculatableScreenPosition fromConfiguration(BiFunction<String, Number, Number> config) {
		int sto = config.apply("store", -1).intValue();
		
		if(sto == -1) return DEFAULT.get();
		
		CalculatableScreenPosition csp = new CalculatableScreenPosition();
		csp.store = StorePosition.values()[(int) sto];
		csp.offsetX = config.apply("offset#x", 0).intValue();
		csp.offsetY = config.apply("offset#y", 0).intValue();
		
		csp.vLine = config.apply("line#vertical", 0).intValue();
		csp.hLine = config.apply("line#horizontal", 0).intValue();
		
		return csp;
	}

	public void storeConfiguration(BiConsumer<String, Number> config) {
		config.accept("store", (int) store.ordinal());
		
		config.accept("line#vertical", (int) vLine);
		config.accept("line#horizontal", (int) hLine);
		
		config.accept("offset#x", (int) offsetX);
		config.accept("offset#y", (int) offsetY);
		
	}

	public static FixedScreenPosition transform(StorePosition current, StorePosition nw, FixedScreenPosition pos,
			int width, int height) {
		
		CalculatableScreenPosition csp = new CalculatableScreenPosition();
		csp.storeByFixed(current, pos, width, height);
		csp.changeStorePosition(nw, width, height);
		
		return csp.calculateFixed(width, height);
	}
	
	public static Pair<PositionLine, Boolean> nearest(FixedScreenPosition pos, List<PositionLine> list) {
		int value = 0;
		PositionLine current = null;

		for (PositionLine l : list) {
			if (current == null) {
				current = l;
				value = calculate(pos, l);
			} else {
				int v = calculate(pos, l);
				if (value > v) {
					value = v;
					current = l;
				}
			}
		}

		return Pair.of(current, value <= 2);
	}

	public static int calculate(FixedScreenPosition pos, PositionLine l) {
		if (l.vertical()) {
			int o = (int) (l.o() * Minecraft.getInstance().getWindow().getGuiScaledWidth());
			int v = pos.getX();
			int value = o - v;
			value = Math.abs(value);
			return value;
		} else {
			int o = (int) (l.o() * Minecraft.getInstance().getWindow().getGuiScaledHeight());
			int v = pos.getY();
			int value = o - v;
			value = Math.abs(value);
			return value;
		}
	}
	
}
