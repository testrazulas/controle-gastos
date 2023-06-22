import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Despesas from './despesas';
import DespesasDetail from './despesas-detail';
import DespesasUpdate from './despesas-update';
import DespesasDeleteDialog from './despesas-delete-dialog';

const DespesasRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Despesas />} />
    <Route path="new" element={<DespesasUpdate />} />
    <Route path=":id">
      <Route index element={<DespesasDetail />} />
      <Route path="edit" element={<DespesasUpdate />} />
      <Route path="delete" element={<DespesasDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DespesasRoutes;
