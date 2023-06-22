package br.com.severo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Despesas.
 */
@Entity
@Table(name = "despesas")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Despesas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "data")
    private LocalDate data;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Despesas id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return this.valor;
    }

    public Despesas valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public Despesas categoria(String categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Despesas data(LocalDate data) {
        this.setData(data);
        return this;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Despesas)) {
            return false;
        }
        return id != null && id.equals(((Despesas) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Despesas{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", categoria='" + getCategoria() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
