package fr.geneste.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.geneste.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachement.class);
        Attachement attachement1 = new Attachement();
        attachement1.setId(1L);
        Attachement attachement2 = new Attachement();
        attachement2.setId(attachement1.getId());
        assertThat(attachement1).isEqualTo(attachement2);
        attachement2.setId(2L);
        assertThat(attachement1).isNotEqualTo(attachement2);
        attachement1.setId(null);
        assertThat(attachement1).isNotEqualTo(attachement2);
    }
}
