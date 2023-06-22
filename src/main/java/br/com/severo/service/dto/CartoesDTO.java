package br.com.severo.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.severo.domain.Cartoes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartoesDTO implements Serializable {

    private Long id;

    private String nome;

    private Double valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartoesDTO)) {
            return false;
        }

        CartoesDTO cartoesDTO = (CartoesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartoesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartoesDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
