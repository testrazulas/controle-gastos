import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISalarios } from 'app/shared/model/salarios.model';
import { getEntity, updateEntity, createEntity, reset } from './salarios.reducer';

export const SalariosUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const salariosEntity = useAppSelector(state => state.salarios.entity);
  const loading = useAppSelector(state => state.salarios.loading);
  const updating = useAppSelector(state => state.salarios.updating);
  const updateSuccess = useAppSelector(state => state.salarios.updateSuccess);

  const handleClose = () => {
    navigate('/salarios' + location.search);
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
      ...salariosEntity,
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
          ...salariosEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="controleGastosApp.salarios.home.createOrEditLabel" data-cy="SalariosCreateUpdateHeading">
            <Translate contentKey="controleGastosApp.salarios.home.createOrEditLabel">Create or edit a Salarios</Translate>
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
                  id="salarios-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('controleGastosApp.salarios.valor')}
                id="salarios-valor"
                name="valor"
                data-cy="valor"
                type="text"
              />
              <ValidatedField
                label={translate('controleGastosApp.salarios.nome')}
                id="salarios-nome"
                name="nome"
                data-cy="nome"
                type="text"
              />
              <ValidatedField
                label={translate('controleGastosApp.salarios.dataRecebimento')}
                id="salarios-dataRecebimento"
                name="dataRecebimento"
                data-cy="dataRecebimento"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/salarios" replace color="info">
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

export default SalariosUpdate;
