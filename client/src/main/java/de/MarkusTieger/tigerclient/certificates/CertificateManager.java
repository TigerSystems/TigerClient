package de.MarkusTieger.tigerclient.certificates;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.certificates.ICertificateManager;
import de.MarkusTieger.common.logger.LoggingCategory;

public class CertificateManager implements ICertificateManager {

	private final List<X509Certificate> certs = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void loadCertificates(File dir) {
		if (!dir.exists())
			return;

		synchronized (certs) {
			certs.clear();
		}
		last_certs = null;

		for (File entry : dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".crt".toLowerCase());
			}
		})) {

			try {
				loadCertificate(entry);
			} catch (CertificateException e) {
				Client.getInstance().getLogger().warn(LoggingCategory.CERTIFICATE,
						"Failed to Load Certificate \"" + entry.getName() + "\"", e);
			} catch (IOException e) {
				Client.getInstance().getLogger().warn(LoggingCategory.CERTIFICATE,
						"Failed to Load Certificate \"" + entry.getName() + "\"", e);
			}

		}

	}

	@Override
	public boolean hasCertificate(Predicate<String[]> test, boolean valid) {
		synchronized (certs) {
			for (X509Certificate cert : certs) {
				if (valid && !isValid(cert))
					continue;
				String subj = cert.getSubjectX500Principal().getName();
				String[] args = subj.contains(";") ? subj.split(";") : new String[] { subj };
				if (test.test(args))
					return true;
			}
		}
		return false;
	}

	private boolean isValid(X509Certificate cert) {
		long current = System.currentTimeMillis();
		if ((cert.getNotBefore().getTime() > current) || (cert.getNotAfter().getTime() < current))
			return false;
		return true;
	}

	@Override
	public void loadCertificate(File entry) throws CertificateException, IOException {
		if (!entry.exists())
			return;

		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is = new FileInputStream(entry);
		InputStream caInput = new BufferedInputStream(is);
		Certificate ca;
		try {
			ca = cf.generateCertificate(caInput);
			synchronized (certs) {
				certs.add((X509Certificate) ca);
			}
			// System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
		} finally {
			caInput.close();
		}

	}

	public de.MarkusTieger.tigerclient.certificates.Certificate convert(X509Certificate cert) {
		return new de.MarkusTieger.tigerclient.certificates.Certificate(cert);
	}

	private List<de.MarkusTieger.tigerclient.certificates.Certificate> last_certs = null;

	@Override
	public List<de.MarkusTieger.tigerclient.certificates.Certificate> getLastLoadedCertificates() {
		if (last_certs == null)
			last_certs = certs.stream().map(this::convert).toList();
		return last_certs;
	}

}
