package com.pep.core.uibase.decode;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 编解码
 */
public final class AESCrypt {

    private final        String          TAG             = "AESCrypt";
    private static final String          AES_MODE        = "AES/CFB/NoPadding";
    private static       AESCrypt        instanse        = null;
    private              SecretKeySpec   secretKeySpec   = null;
    private              IvParameterSpec ivParameterSpec = null;
    private              int             mode            = Cipher.DECRYPT_MODE;
    volatile             Cipher          cipher          = null;

    public AESCrypt() {
    }

//    public synchronized static AESCrypt getInstance() {
//        if (instanse == null) {
//            instanse = new AESCrypt();
//        }
//        return instanse;
//    }

    private SecretKeySpec generateKey() {
        if (secretKeySpec == null) {
            try {
                String key = "rjsz2012+$&#2017";
                secretKeySpec = new SecretKeySpec(key.getBytes("utf-8"), "AES");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return secretKeySpec;
    }


    private IvParameterSpec getIvSpec() {
        if (ivParameterSpec == null) {
            final byte[] ivBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
            ivParameterSpec = new IvParameterSpec(ivBytes);
        }
        return ivParameterSpec;
    }

    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        if (cipher == null) {
            cipher = Cipher.getInstance(AES_MODE);
            mode = Cipher.ENCRYPT_MODE;
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(), getIvSpec());
        }
        if (mode != Cipher.ENCRYPT_MODE) {
            mode = Cipher.ENCRYPT_MODE;
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(), getIvSpec());
        }
        byte[] cipherText = cipher.doFinal(message);
        return cipherText;
    }

    public byte[] decrypt(byte[] decodedCipherText) throws GeneralSecurityException {
        if (cipher == null) {
            cipher = Cipher.getInstance(AES_MODE);
            mode = Cipher.DECRYPT_MODE;
            cipher.init(Cipher.DECRYPT_MODE, generateKey(), getIvSpec());
        }
        if (mode != Cipher.DECRYPT_MODE) {
            mode = Cipher.DECRYPT_MODE;
            cipher.init(Cipher.DECRYPT_MODE, generateKey(), getIvSpec());
        }
        byte[] decryptedBytes = cipher.doFinal(decodedCipherText);
        return decryptedBytes;
    }
}
