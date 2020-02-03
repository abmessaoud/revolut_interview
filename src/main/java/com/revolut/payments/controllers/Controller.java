package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.InvocationTargetException;

public abstract class Controller {
    public ResponseDTO handle(final RequestDTO requestDTO)  {
        try {
            return (ResponseDTO) this.getClass().getDeclaredMethod(requestDTO.getMethod(), RequestDTO.class).invoke(this, requestDTO);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    protected ResponseDTO get(final RequestDTO request) {
        if (StringUtils.isNotBlank(request.getUrlParam())) {
            return getById(request);
        }
        return getMany(request);
    }

    protected ResponseDTO post(final RequestDTO request) {
        if (StringUtils.isNotBlank(request.getUrlParam())) {
            return null;
        }
        return create(request);
    }

    protected ResponseDTO put(final RequestDTO request) {
        if (StringUtils.isBlank(request.getUrlParam()) || !request.hasQueryParam("balance")) {
            return null;
        }
        return update(request);
    }

    protected ResponseDTO delete(final RequestDTO request) {
        if (StringUtils.isBlank(request.getUrlParam())) {
            return null;
        }
        return deactivate(request);
    }

    protected abstract ResponseDTO create(final RequestDTO requestDTO);
    protected abstract ResponseDTO update(final RequestDTO requestDTO);
    protected abstract ResponseDTO getMany(final RequestDTO requestDTO);
    protected abstract ResponseDTO getById(final RequestDTO requestDTO);
    protected abstract ResponseDTO deactivate(final RequestDTO requestDTO);
}
