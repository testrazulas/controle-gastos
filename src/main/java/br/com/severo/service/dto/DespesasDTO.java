package br.com.severo.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.severo.domain.Despesas} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DespesasDTO implements Serializable {

    private Long id;

    private Double valor;

    private String categoria;

    private LocalDate data;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DespesasDTO)) {
            return false;
        }

        DespesasDTO despesasDTO = (DespesasDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, despesasDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DespesasDTO{" +
            "id=" + getId() +
            ", valor=" + getValor() +
            ", categoria='" + getCategoria() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
