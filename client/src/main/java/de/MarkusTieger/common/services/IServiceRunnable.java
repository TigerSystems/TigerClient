package de.MarkusTieger.common.services;

import de.MarkusTieger.annotations.NoObfuscation;

@NoObfuscation
public interface IServiceRunnable {

	void start(final IService service) throws Exception;

	void stop(final IService service) throws Exception;

}
