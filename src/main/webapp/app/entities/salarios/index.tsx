import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Salarios from './salarios';
import SalariosDetail from './salarios-detail';
import SalariosUpdate from './salarios-update';
import SalariosDeleteDialog from './salarios-delete-dialog';

const SalariosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Salarios />} />
    <Route path="new" element={<SalariosUpdate />} />
    <Route path=":id">
      <Route index element={<SalariosDetail />} />
      <Route path="edit" element={<SalariosUpdate />} />
      <Route path="delete" element={<SalariosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SalariosRoutes;
