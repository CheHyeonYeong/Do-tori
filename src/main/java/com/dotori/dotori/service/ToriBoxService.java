package com.dotori.dotori.service;

import com.dotori.dotori.dto.ToriBoxDTO;

public interface ToriBoxService {

    void insert(ToriBoxDTO toriBoxDTO) throws Exception;
    void delete(ToriBoxDTO toriBoxDTO) throws Exception;

}
