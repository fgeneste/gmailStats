package fr.geneste.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stat.
 */
@Entity
@Table(name = "stat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Stat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_from")
    private String from;

    @Column(name = "number")
    private Integer number;

    @Column(name = "lastupdated")
    private Instant lastupdated;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stat id(Long id) {
        this.id = id;
        return this;
    }

    public String getFrom() {
        return this.from;
    }

    public Stat from(String from) {
        this.from = from;
        return this;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getNumber() {
        return this.number;
    }

    public Stat number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Instant getLastupdated() {
        return this.lastupdated;
    }

    public Stat lastupdated(Instant lastupdated) {
        this.lastupdated = lastupdated;
        return this;
    }

    public void setLastupdated(Instant lastupdated) {
        this.lastupdated = lastupdated;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stat)) {
            return false;
        }
        return id != null && id.equals(((Stat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stat{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", number=" + getNumber() +
            ", lastupdated='" + getLastupdated() + "'" +
            "}";
    }
}
