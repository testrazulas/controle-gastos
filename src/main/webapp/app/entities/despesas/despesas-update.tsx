import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDespesas } from 'app/shared/model/despesas.model';
import { getEntity, updateEntity, createEntity, reset } from './despesas.reducer';

export const DespesasUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const despesasEntity = useAppSelector(state => state.despesas.entity);
  const loading = useAppSelector(state => state.despesas.loading);
  const updating = useAppSelector(state => state.despesas.updating);
  const updateSuccess = useAppSelector(state => state.despesas.updateSuccess);

  const handleClose = () => {
    navigate('/despesas' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...despesasEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...despesasEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="controleGastosApp.despesas.home.createOrEditLabel" data-cy="DespesasCreateUpdateHeading">
            <Translate contentKey="controleGastosApp.despesas.home.createOrEditLabel">Create or edit a Despesas</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="despesas-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('controleGastosApp.despesas.valor')}
                id="despesas-valor"
                name="valor"
                data-cy="valor"
                type="text"
              />
              <ValidatedField
                label={translate('controleGastosApp.despesas.categoria')}
                id="despesas-categoria"
                name="categoria"
                data-cy="categoria"
                type="text"
              />
              <ValidatedField
                label={translate('controleGastosApp.despesas.data')}
                id="despesas-data"
                name="data"
                data-cy="data"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/despesas" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DespesasUpdate;
