package br.com.carbigdata.event.repository;


import java.util.List;
import java.util.Map;

public interface EventRepositoryCustom {

    /**
     * Busca as iformações da ocorrência de acordo com os parâmetros passado.
     *
     * @param paramsMap parâmetros da API.
     * @return uma lista de {@link EventTuple}.
     */
    List<EventTuple> findAllByParamsMap(Map<String, String> paramsMap);
}
