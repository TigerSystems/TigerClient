package de.MarkusTieger.common.certificates;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.function.Predicate;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.tigerclient.certificates.Certificate;

@NoObfuscation
public interface ICertificateManager {
	void loadCertificates(File dir);

	boolean hasCertificate(Predicate<String[]> test, boolean valid);

	void loadCertificate(File entry) throws CertificateException, IOException;

	List<Certificate> getLastLoadedCertificates();
}
