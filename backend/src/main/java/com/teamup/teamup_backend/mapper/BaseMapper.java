package com.teamup.teamup_backend.mapper;

import java.util.List;

public interface BaseMapper<S, T> {

    T toDto(S source);

    S toEntity(T target);

    List<T> toDtoList(List<S> sourceList);

    List<S> toEntityList(List<T> targetList);

}
