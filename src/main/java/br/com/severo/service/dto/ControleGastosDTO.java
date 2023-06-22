package br.com.severo.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.severo.domain.ControleGastos} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControleGastosDTO implements Serializable {

    private Long id;

    private LocalDate mes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getMes() {
        return mes;
    }

    public void setMes(LocalDate mes) {
        this.mes = mes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControleGastosDTO)) {
            return false;
        }

        ControleGastosDTO controleGastosDTO = (ControleGastosDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, controleGastosDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControleGastosDTO{" +
            "id=" + getId() +
            ", mes='" + getMes() + "'" +
            "}";
    }
}
