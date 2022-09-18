package de.MarkusTieger.common.services;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IService {

	void start();

	void stop();

	void restart();

	ServiceStatus getStatus();

	String getName();

	enum ServiceStatus {

		STOPPED, STARTING, RUNNING, STOPPING, ERROR

	}

}
