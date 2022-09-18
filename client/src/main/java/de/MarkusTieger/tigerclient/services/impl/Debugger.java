package de.MarkusTieger.tigerclient.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.services.IService;
import de.MarkusTieger.common.services.IServiceRegistry;
import de.MarkusTieger.common.services.IServiceRunnable;

public class Debugger implements IServiceRunnable {

	private ServerSocket server = null;
	private Thread th = null;
	private int state = 0;
	private static final Gson GSON = new GsonBuilder().create();

	@Override
	public void start(final IService service) throws Exception {
		server = new ServerSocket();

		state = 1;
		th = new Thread(() -> {
			try {
				server = new ServerSocket(25763);
				while (state == 1) {
					tick(service);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, "Server-Task");

		th.start();

	}

	private void tick(final IService service) {
		try {
			if (server == null && state == 1) {
				state = 2;
				service.stop();
				return;
			}
			Socket client = server.accept();
			new Thread(() -> handle(client)).start();
		} catch (IOException e) {
			Client.getInstance().getLogger().error(LoggingCategory.SERVICES, e);
			service.stop();
		}
	}

	private void handle(Socket client) {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter writer = new PrintWriter(client.getOutputStream());
			String str = null;
			while ((str = reader.readLine()) != null) {
				try {
					Client.getInstance().getLogger().debug(LoggingCategory.SERVICES, "DATA: " + str);
					JsonObject object = GSON.fromJson(str, JsonObject.class);

					JsonArray requests = object.getAsJsonArray("requests");
					ArrayList<JsonObject> array = new ArrayList<>();
					for (int i = 0; i < requests.size(); i++) {
						handle(client, writer, requests.get(i).getAsJsonObject(), array);
					}

					if (array.size() == 0)
						continue;

					object = new JsonObject();
					requests = new JsonArray();

					array.forEach(requests::add);

					object.add("requests", requests);

					writer.write(GSON.toJson(object));
					writer.write("\n");
					writer.flush();

				} catch (Exception e) {
					Client.getInstance().getLogger().warn(LoggingCategory.SERVICES, "Failed to handle Request: " + str,
							e);
				}
			}
		} catch (Exception e) {
			if (!client.isClosed()) {
				try {
					client.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			Client.getInstance().getLogger().warn(LoggingCategory.SERVICES, "Connection Lost.", e);
		}
	}

	private void handle(Socket client, PrintWriter writer, JsonObject object, ArrayList<JsonObject> requests) {

		String request = object.get("request").getAsString();

		/*
		 * if(request.equalsIgnoreCase("hardware-request")){ String method =
		 * object.get("method").getAsString(); if(method.equalsIgnoreCase("tac")){
		 * JsonObject data =
		 * Client.getInstance().getTACManager().getInformationCollector().collect();
		 * 
		 * JsonObject response = new JsonObject(); response.addProperty("request",
		 * "handle-response"); response.addProperty("type", "hardware");
		 * response.add("data", data);
		 * 
		 * requests.add(response); } }
		 */

		if (request.equalsIgnoreCase("process-request")) {
			JsonObject data = new JsonObject();

			ProcessHandle handle = ProcessHandle.current();

			ProcessHandle.Info info = handle.info();

			Optional<String> user = info.user();

			if (user.isPresent()) {
				data.addProperty("user", user.get());
			}

			JsonObject memory = new JsonObject();

			memory.addProperty("total", Runtime.getRuntime().totalMemory());
			memory.addProperty("max", Runtime.getRuntime().maxMemory());
			memory.addProperty("free", Runtime.getRuntime().freeMemory());

			data.add("memory", memory);
			data.addProperty("processors", Runtime.getRuntime().availableProcessors());

			JsonObject response = new JsonObject();
			response.addProperty("request", "handle-response");
			response.addProperty("type", "process");
			response.add("data", data);

			requests.add(response);
		}

		if (request.equalsIgnoreCase("service-request")) {
			String method = object.get("method").getAsString();
			IServiceRegistry registry = Client.getInstance().getServiceRegistry();
			if (method.equalsIgnoreCase("list")) {

				JsonArray services = new JsonArray();

				for (IService service : registry.toArray()) {
					JsonObject obj = new JsonObject();
					obj.addProperty("name", service.getName());
					obj.addProperty("status", service.getStatus().name());
					services.add(obj);
				}

				JsonObject response = new JsonObject();
				response.addProperty("request", "handle-response");
				response.addProperty("type", "service-list");
				response.add("data", services);

				requests.add(response);

			} else {
				String name = object.get("name").getAsString();
				IService target = null;
				for (IService service : registry.toArray()) {
					if (service.getName().equalsIgnoreCase(name)) {
						target = service;
						break;
					}
				}

				if (target == null)
					return;

				if (method.equalsIgnoreCase("get")) {

					JsonObject obj = new JsonObject();
					obj.addProperty("name", target.getName());
					obj.addProperty("status", target.getStatus().name());

					JsonObject response = new JsonObject();
					response.addProperty("request", "handle-response");
					response.addProperty("type", "service-get");
					response.add("data", obj);

					requests.add(response);

				}

				if (method.equalsIgnoreCase("start")) {
					target.start();
				}

				if (method.equalsIgnoreCase("restart")) {
					target.restart();
				}

				if (method.equalsIgnoreCase("stop")) {
					target.stop();
				}

			}
		}

		if (request.equalsIgnoreCase("recovery")) {
			Client.getInstance().getCompatiblityExecutor().enableRecovery();
		}

		if (request.equalsIgnoreCase("exit")) {
			System.exit(0);
		}

	}

	@Override
	public void stop(final IService service) throws Exception {
		state = 0;
		if (server == null)
			return;
		server.close();
		server = null;
	}
}
