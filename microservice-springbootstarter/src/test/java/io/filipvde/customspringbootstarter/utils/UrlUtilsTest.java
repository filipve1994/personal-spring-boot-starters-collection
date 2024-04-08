package io.filipvde.customspringbootstarter.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class UrlUtilsTest {

    @Test
    public void testJoin() {
        assertThat(UrlUtils.joinQueryparms("url", null)).isEqualTo("url");
        assertThat(UrlUtils.joinQueryparms("url", "qp1=een")).isEqualTo("url?qp1=een");
        assertThat(UrlUtils.joinQueryparms("url?qp1=een", "qp2=twee")).isEqualTo("url?qp1=een&qp2=twee");
    }

}
