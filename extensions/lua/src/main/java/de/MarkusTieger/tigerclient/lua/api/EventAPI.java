package de.MarkusTieger.tigerclient.lua.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.tigerclient.events.lua.LuaEventAdapter;
import de.MarkusTieger.tigerclient.lua.LuaGlobalsGenerator.BooleanSuppliedGlobals;

public class EventAPI extends TwoArgFunction {

	private List<Object> listeners = new ArrayList<>();

	public EventAPI() {}

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {

		BooleanSupplier supplier = () -> true;

		if(env instanceof BooleanSuppliedGlobals bsg) supplier = bsg.getSupplier();

		LuaValue library = LuaValue.tableOf();

		library.set("registerListener", new registerListener(supplier));
		library.set("unregisterListeners",  new unregisterListeners());

		env.set("events", library);

		return library;
	}

	public class unregisterListeners extends ZeroArgFunction {

		@Override
		public LuaValue call() {
			listeners.forEach(Client.getInstance().getEventManager()::unregister);
			return LuaValue.NIL;
		}

	}

	public class registerListener extends OneArgFunction {

		private final BooleanSupplier supplier;

		public registerListener(BooleanSupplier supplier) {
			this.supplier = supplier;
		}

		@Override
		public LuaValue call(LuaValue listener) {
			LuaFunction func = listener.checkfunction();

			Object L = new LuaEventAdapter(supplier, func);
			listeners.add(L);
			Client.getInstance().getEventManager().register(L);

			return LuaValue.NIL;
		}

	}

}
