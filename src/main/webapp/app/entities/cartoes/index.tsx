import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cartoes from './cartoes';
import CartoesDetail from './cartoes-detail';
import CartoesUpdate from './cartoes-update';
import CartoesDeleteDialog from './cartoes-delete-dialog';

const CartoesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cartoes />} />
    <Route path="new" element={<CartoesUpdate />} />
    <Route path=":id">
      <Route index element={<CartoesDetail />} />
      <Route path="edit" element={<CartoesUpdate />} />
      <Route path="delete" element={<CartoesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CartoesRoutes;
