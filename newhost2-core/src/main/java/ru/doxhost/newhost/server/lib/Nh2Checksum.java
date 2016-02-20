package ru.doxhost.newhost.server.lib;

import java.io.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Eugene Kirin
 */
public class Nh2Checksum {

    /**
     * Generates md5 checksum for given {@code Class}
     * @param forClass Class
     * @return md5 checksum
     * @throws NoSuchAlgorithmException
     */
    public static String md5Sum(Class forClass) throws NoSuchAlgorithmException, IOException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        String path = forClass.getName().replace('.', '/');
        String fileName = path + ".class";

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        InputStream stream = forClass.getClassLoader().getResourceAsStream(fileName);

        int datum = stream.read();

        while( datum != -1) {
            buffer.write(datum);
            datum = stream.read();
        }

        messageDigest.update(buffer.toByteArray());

        byte[] digestBytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder("");

        for (byte digestByte : digestBytes) {
            sb.append(Integer.toString((digestByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}