package de.MarkusTieger.tac.gui.widgets.connect.registry;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import de.MarkusTieger.common.point.IPointRegistry;

public class PointRegistry implements IPointRegistry {

	private final HashMap<String, Point> map = new HashMap<>();

	@Override
	public Map<String, Point> toMap() {
		return map;
	}
}
