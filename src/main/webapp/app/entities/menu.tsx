import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/controle-gastos">
        <Translate contentKey="global.menu.entities.controleGastos" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/despesas">
        <Translate contentKey="global.menu.entities.despesas" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cartoes">
        <Translate contentKey="global.menu.entities.cartoes" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/salarios">
        <Translate contentKey="global.menu.entities.salarios" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
