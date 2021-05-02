package fr.geneste.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_from")
    private String from;

    @Column(name = "object")
    private String object;

    @Column(name = "corps")
    private String corps;

    @Column(name = "date")
    private Instant date;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message id(Long id) {
        this.id = id;
        return this;
    }

    public String getFrom() {
        return this.from;
    }

    public Message from(String from) {
        this.from = from;
        return this;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getObject() {
        return this.object;
    }

    public Message object(String object) {
        this.object = object;
        return this;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getCorps() {
        return this.corps;
    }

    public Message corps(String corps) {
        this.corps = corps;
        return this;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public Instant getDate() {
        return this.date;
    }

    public Message date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", object='" + getObject() + "'" +
            ", corps='" + getCorps() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
