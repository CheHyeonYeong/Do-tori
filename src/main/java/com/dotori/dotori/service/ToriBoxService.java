package com.dotori.dotori.service;

import com.dotori.dotori.dto.ToriBoxDTO;

import java.util.List;

public interface ToriBoxService {

    int insert(ToriBoxDTO toriBoxDTO);
    List<ToriBoxDTO> selectAll();
    void delete(int id);

}
