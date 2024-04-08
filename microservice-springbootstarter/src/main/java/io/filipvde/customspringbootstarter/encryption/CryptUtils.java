package io.filipvde.customspringbootstarter.encryption;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;

@Slf4j
public class CryptUtils {

    private static final long CURRENT_CRYPTORVERSION = 2;

    private CryptUtils() {
        // utils
    }

    public static long currentCryptorVersion() {
        return CURRENT_CRYPTORVERSION;
    }

    @Nonnull
    public static String decrypt(@Nonnull final String encVal, final long cryptorVersion, @Nonnull final String pwd) {
        try {
            return makeCryptor(cryptorVersion, pwd).decrypt(encVal);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot decrypt data: " + e, e);
        }
    }

    @Nonnull
    public static String encrypt(@Nonnull final String clearVal, @Nonnull final String pwd) {
        return encrypt(clearVal, currentCryptorVersion(), pwd);
    }

    @Nonnull
    public static String encrypt(@Nonnull final String clearVal, final long cryptorVersion, @Nonnull final String pwd) {
        try {
            return makeCryptor(cryptorVersion, pwd).encrypt(clearVal);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot encrypt data: " + e, e);
        }
    }

    /**
     * Give a Cryptor based on the cryptorVersion and the password.
     * Can be optimized later-on by using a cache,
     * but because part of the key to that needs to be related to the password,
     * the key needs to be some solid form of cryptversion+hased(password).
     */    
    @Nonnull
    private static CryptMagic makeCryptor(final long cryptorVersion, @Nonnull final String pwd) {
        if (cryptorVersion == 1) {
            StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
            standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES"); // optionally, set the algorithm
            standardPBEStringEncryptor.setPassword(pwd);
            return new CryptMagic() {
                @Override
                public String encrypt(String encVal) {
                    return standardPBEStringEncryptor.encrypt(encVal);
                }

                @Override
                public String decrypt(String encVal) {
                    return standardPBEStringEncryptor.decrypt(encVal);
                }
            };
        }
        if (cryptorVersion == 2) {
            // More security: the AES256TextEncryptor util class with a much more secure algorithm: PBEWithHMACSHA512AndAES_256, (you need Java 8 at least to use it):
            AES256TextEncryptor standardPBEStringEncryptor = new AES256TextEncryptor();
            standardPBEStringEncryptor.setPassword(pwd);
            return new CryptMagic() {
                @Override
                public String encrypt(String encVal) {
                    return standardPBEStringEncryptor.encrypt(encVal);
                }

                @Override
                public String decrypt(String encVal) {
                    return standardPBEStringEncryptor.decrypt(encVal);
                }
            };
        }
        throw new IllegalArgumentException("CryptorVersion " + cryptorVersion + "not supported, current (and highest) version is : " + currentCryptorVersion());


    }

    private interface CryptMagic {
        String encrypt(String encVal);

        String decrypt(String encVal);
    }
}
