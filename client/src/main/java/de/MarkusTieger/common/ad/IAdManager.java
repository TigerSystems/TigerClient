package de.MarkusTieger.common.ad;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.services.IServiceRunnable;
import de.MarkusTieger.tigerclient.utils.animation.AnimationData;

@NoObfuscation
public interface IAdManager extends IServiceRunnable {

	AnimationData getAd();

}
