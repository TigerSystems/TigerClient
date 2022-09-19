package de.MarkusTieger.tigerclient.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class ClientClassLoader extends URLClassLoader {

    private final ClientVerification verify;
    private final ClassLoader parent;

    public ClientClassLoader(ClientVerification verify, URL[] urls) {
        super(urls, new EmptyClassLoader());
        this.verify = verify;
        this.parent = ClientClassLoader.class.getClassLoader();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            Class<?> clazz = super.loadClass(name);
            if(!verify.verify(clazz)) throw new ClassNotFoundException("Signature verification failed!");
            return clazz;
        } catch(ClassNotFoundException ex){
            return parent.loadClass(name);
        }
    }
    
    @Override
    public URL getResource(String name) {
    	return super.getResource(name);
    }

    @Override
    public void addURL(URL url) {
    	super.addURL(url);
    }
}
