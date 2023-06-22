import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ControleGastos from './controle-gastos';
import Despesas from './despesas';
import Cartoes from './cartoes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="controle-gastos/*" element={<ControleGastos />} />
        <Route path="despesas/*" element={<Despesas />} />
        <Route path="cartoes/*" element={<Cartoes />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
