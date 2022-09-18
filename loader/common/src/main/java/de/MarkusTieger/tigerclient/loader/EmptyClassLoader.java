package de.MarkusTieger.tigerclient.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class EmptyClassLoader extends URLClassLoader {

    public EmptyClassLoader(){
        super(new URL[0]);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        throw new ClassNotFoundException("Empty Loader");
    }
}
