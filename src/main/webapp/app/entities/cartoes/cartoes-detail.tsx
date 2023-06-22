import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cartoes.reducer';

export const CartoesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cartoesEntity = useAppSelector(state => state.cartoes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cartoesDetailsHeading">
          <Translate contentKey="controleGastosApp.cartoes.detail.title">Cartoes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cartoesEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="controleGastosApp.cartoes.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{cartoesEntity.nome}</dd>
          <dt>
            <span id="valor">
              <Translate contentKey="controleGastosApp.cartoes.valor">Valor</Translate>
            </span>
          </dt>
          <dd>{cartoesEntity.valor}</dd>
        </dl>
        <Button tag={Link} to="/cartoes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cartoes/${cartoesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CartoesDetail;
