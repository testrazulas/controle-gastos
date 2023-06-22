import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './despesas.reducer';

export const DespesasDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const despesasEntity = useAppSelector(state => state.despesas.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="despesasDetailsHeading">
          <Translate contentKey="controleGastosApp.despesas.detail.title">Despesas</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{despesasEntity.id}</dd>
          <dt>
            <span id="valor">
              <Translate contentKey="controleGastosApp.despesas.valor">Valor</Translate>
            </span>
          </dt>
          <dd>{despesasEntity.valor}</dd>
          <dt>
            <span id="categoria">
              <Translate contentKey="controleGastosApp.despesas.categoria">Categoria</Translate>
            </span>
          </dt>
          <dd>{despesasEntity.categoria}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="controleGastosApp.despesas.data">Data</Translate>
            </span>
          </dt>
          <dd>{despesasEntity.data ? <TextFormat value={despesasEntity.data} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/despesas" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/despesas/${despesasEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DespesasDetail;
