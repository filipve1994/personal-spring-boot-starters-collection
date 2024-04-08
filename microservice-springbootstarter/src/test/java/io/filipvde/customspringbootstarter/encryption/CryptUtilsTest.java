package io.filipvde.customspringbootstarter.encryption;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CryptUtilsTest {

    private static final String TEST_STR = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    private static final String TEST_PWD = "oidfoihdiovdoivphdvidqsvhuqdspvpqdvpids";

    @Test
    public void shouldEncryptAndDecryptWithVersion_1() {
        String encrypted = CryptUtils.encrypt(TEST_STR, 1, TEST_PWD);
        Assertions.assertThat(CryptUtils.decrypt(encrypted, 1, TEST_PWD)).isEqualTo(TEST_STR);
        //multiple encryptions of the same should yield different results (due to random salt)
        Assertions.assertThat(encrypted).isNotEqualTo(CryptUtils.encrypt(TEST_STR, 1, TEST_PWD));
    }

    @Test
    public void shouldEncryptAndDecryptWithVersion_2() {
        String encrypted = CryptUtils.encrypt(TEST_STR, 2, TEST_PWD);
        Assertions.assertThat(CryptUtils.decrypt(encrypted, 2, TEST_PWD)).isEqualTo(TEST_STR);
        //multiple encryptions of the same should yield different results (due to random salt)
        Assertions.assertThat(encrypted).isNotEqualTo(CryptUtils.encrypt(TEST_STR, 2, TEST_PWD));
    }

    @Test
    public void shouldEncryptAndDecryptWith_DefaultVersion() {
        String encrypted = CryptUtils.encrypt(TEST_STR, TEST_PWD);
        Assertions.assertThat(CryptUtils.decrypt(encrypted, CryptUtils.currentCryptorVersion(), TEST_PWD)).isEqualTo(TEST_STR);
        //multiple encryptions of the same should yield different results (due to random salt)
        Assertions.assertThat(encrypted).isNotEqualTo(CryptUtils.encrypt(TEST_STR, CryptUtils.currentCryptorVersion(), TEST_PWD));
    }

}