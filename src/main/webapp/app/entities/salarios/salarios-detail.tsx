import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './salarios.reducer';

export const SalariosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const salariosEntity = useAppSelector(state => state.salarios.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="salariosDetailsHeading">
          <Translate contentKey="controleGastosApp.salarios.detail.title">Salarios</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{salariosEntity.id}</dd>
          <dt>
            <span id="valor">
              <Translate contentKey="controleGastosApp.salarios.valor">Valor</Translate>
            </span>
          </dt>
          <dd>{salariosEntity.valor}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="controleGastosApp.salarios.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{salariosEntity.nome}</dd>
          <dt>
            <span id="dataRecebimento">
              <Translate contentKey="controleGastosApp.salarios.dataRecebimento">Data Recebimento</Translate>
            </span>
          </dt>
          <dd>
            {salariosEntity.dataRecebimento ? (
              <TextFormat value={salariosEntity.dataRecebimento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/salarios" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/salarios/${salariosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SalariosDetail;
