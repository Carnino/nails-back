package jsges.nails.DTO.articulos;

import jsges.nails.DTO.TipoObjetoDTO;
import jsges.nails.domain.articulos.ArticuloVenta;

public class ArticuloVentaDTO extends TipoObjetoDTO {

    public Integer id;
    public String denominacion;
    public Integer linea;

    public ArticuloVentaDTO( ArticuloVenta model) {
        this.id = model.getId();
        this.denominacion=model.getDenominacion();
        this.linea=model.getLinea().getId();
    }

    public ArticuloVentaDTO( ) {

    }

    public String getDenominacion() {
        return this.denominacion;
    }

    public Integer getLinea() {
        return linea;
    }
    

}
