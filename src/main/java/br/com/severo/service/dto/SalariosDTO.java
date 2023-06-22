package br.com.severo.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.severo.domain.Salarios} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalariosDTO implements Serializable {

    private Long id;

    private Double valor;

    private String nome;

    private LocalDate dataRecebimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(LocalDate dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalariosDTO)) {
            return false;
        }

        SalariosDTO salariosDTO = (SalariosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salariosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalariosDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", nome='" + getNome() + "'" +
            ", dataRecebimento='" + getDataRecebimento() + "'" +
            "}";
    }
}
