package de.uniReddit.uniReddit;

import java.io.FileInputStream;
import java.io.InputStream;

public class ResourceManager {
    public InputStream getInputStreamFromResource(String name){
        return getClass().getResourceAsStream(name);
    }
}
