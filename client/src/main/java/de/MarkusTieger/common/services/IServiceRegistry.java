package de.MarkusTieger.common.services;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.registry.IRegistry;

@NoObfuscation
public interface IServiceRegistry extends IRegistry<IServiceRegistry, IService> {

}
