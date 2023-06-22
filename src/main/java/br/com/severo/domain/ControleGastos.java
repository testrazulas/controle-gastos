package br.com.severo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A ControleGastos.
 */
@Entity
@Table(name = "controle_gastos")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControleGastos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "mes")
    private LocalDate mes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ControleGastos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMes() {
        return this.mes;
    }

    public ControleGastos mes(LocalDate mes) {
        this.setMes(mes);
        return this;
    }

    public void setMes(LocalDate mes) {
        this.mes = mes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControleGastos)) {
            return false;
        }
        return id != null && id.equals(((ControleGastos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControleGastos{" +
            "id=" + getId() +
            ", mes='" + getMes() + "'" +
            "}";
    }
}
