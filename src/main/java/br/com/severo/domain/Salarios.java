package br.com.severo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Salarios.
 */
@Entity
@Table(name = "salarios")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Salarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_recebimento")
    private LocalDate dataRecebimento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Salarios id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return this.valor;
    }

    public Salarios valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return this.nome;
    }

    public Salarios nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataRecebimento() {
        return this.dataRecebimento;
    }

    public Salarios dataRecebimento(LocalDate dataRecebimento) {
        this.setDataRecebimento(dataRecebimento);
        return this;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salarios)) {
            return false;
        }
        return id != null && id.equals(((Salarios) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salarios{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", nome='" + getNome() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            "}";
    }
}
