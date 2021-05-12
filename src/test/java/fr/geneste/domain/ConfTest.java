package fr.geneste.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.geneste.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conf.class);
        Conf conf1 = new Conf();
        conf1.setId(1L);
        Conf conf2 = new Conf();
        conf2.setId(conf1.getId());
        assertThat(conf1).isEqualTo(conf2);
        conf2.setId(2L);
        assertThat(conf1).isNotEqualTo(conf2);
        conf1.setId(null);
        assertThat(conf1).isNotEqualTo(conf2);
    }
}
