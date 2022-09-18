package de.MarkusTieger.tigerclient.loader;

import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.cert.Certificate;

public class ClientVerification {

    private String cert;

    public ClientVerification(String cert){
        this.cert = cert;
    }

    public boolean verify(Class<?> clazz){

        CodeSource target = clazz.getProtectionDomain().getCodeSource();

        if(target == null) {
            return false;
        }

        boolean equal = false;

        Certificate[] certs = target.getCertificates();

        if(certs == null) return false;

        for(Certificate cert : certs) {
            try {
                String data = bytesToHex(
                        MessageDigest.getInstance("SHA-256").digest(
                                cert.getEncoded())).toLowerCase();

                if(data.equalsIgnoreCase(this.cert)) equal = true;
            } catch(Exception e){
                e.printStackTrace();
            }
        }



        return equal;
    }
    
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
