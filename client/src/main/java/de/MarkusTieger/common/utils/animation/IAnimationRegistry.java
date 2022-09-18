package de.MarkusTieger.common.utils.animation;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.registry.IMapRegistry;
import de.MarkusTieger.common.utils.ITickable;

@NoObfuscation
public interface IAnimationRegistry
		extends IMapRegistry<IAnimationRegistry, String, IAnimationData>, IModule<Throwable>, ITickable<Throwable> {

}
