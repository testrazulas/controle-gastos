import controleGastos from 'app/entities/controle-gastos/controle-gastos.reducer';
import despesas from 'app/entities/despesas/despesas.reducer';
import cartoes from 'app/entities/cartoes/cartoes.reducer';
import salarios from 'app/entities/salarios/salarios.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  controleGastos,
  despesas,
  cartoes,
  salarios,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
