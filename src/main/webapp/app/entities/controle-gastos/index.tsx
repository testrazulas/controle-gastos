import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ControleGastos from './controle-gastos';
import ControleGastosDetail from './controle-gastos-detail';
import ControleGastosUpdate from './controle-gastos-update';
import ControleGastosDeleteDialog from './controle-gastos-delete-dialog';

const ControleGastosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ControleGastos />} />
    <Route path="new" element={<ControleGastosUpdate />} />
    <Route path=":id">
      <Route index element={<ControleGastosDetail />} />
      <Route path="edit" element={<ControleGastosUpdate />} />
      <Route path="delete" element={<ControleGastosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ControleGastosRoutes;
