package br.com.carbigdata.event.repository;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Busca uma lista de registros de eventos com base nos parâmetros fornecidos.
     * A consulta é construída dinamicamente com base nos filtros, paginação e ordenação fornecidos no mapa de parâmetros.
     *
     * @param paramsMap mapa contendo os parâmetros de busca, incluindo filtros como "EventDate", paginação com "page" e "size",
     *                  e parâmetros de ordenação "orderBy".
     * @return uma lista de {@link EventTuple} contendo os dados dos eventos encontrados.
     */
    @Override
    public List<EventTuple> findAllByParamsMap(Map<String, String> paramsMap) {
        int page = StringUtil.isNullOrEmpty(paramsMap.get("page")) ? 1 : Integer.parseInt(paramsMap.get("page"));
        int size = StringUtil.isNullOrEmpty(paramsMap.get("size")) ? 10 : Integer.parseInt(paramsMap.get("size"));
        String eventDate = paramsMap.get("EventDate");
        String[] orderByList = new String[0];

        StringBuilder jpql = new StringBuilder("SELECT new br.com.carbigdata.event.repository.EventTuple(c.name, c.cpf, a.cep, a.city, " +
                "a.state, e.eventDate, ep.pathBucket) FROM EventEntity e INNER JOIN ClientEntity c on (e.client.code = c.code)" +
                "INNER JOIN AddressEntity a on (e.address.code = a.code)" +
                "INNER JOIN EventPhotoEntity ep on (e.code = ep.event.code)");

        List<String> whereList = buildWhereList(paramsMap);

        if (!whereList.isEmpty()) {
            jpql.append(" WHERE ");
            for (String where: whereList) {
                jpql.append(where).append(" AND ");
            }
            jpql = new StringBuilder(jpql.substring(0, jpql.length() - 5));
        }

        buildOrderByClause (paramsMap, orderByList, jpql);

        TypedQuery<EventTuple> query = entityManager.createQuery(jpql.toString(), EventTuple.class);
        if (!StringUtil.isNullOrEmpty(eventDate)) {
            query.setParameter("eventDate", LocalDate.parse(eventDate));
        }
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    /**
     * Constrói a cláusula "ORDER BY" para a consulta JPQL com base no parâmetro "orderBy".
     *
     * @param paramsMap parâmetros de filtro.
     * @param orderByList lista de colunas e direções de ordenação.
     * @param jpql onde a cláusula "ORDER BY" será adicionada.
     */
    private static void buildOrderByClause (Map<String, String> paramsMap, String[] orderByList, StringBuilder jpql) {
        if (!StringUtil.isNullOrEmpty(paramsMap.get("orderBy"))) {
            orderByList = paramsMap.get("orderBy").split(",");
            jpql.append(" ORDER BY ");
        }
        boolean isFirst = true;

        for (String order: orderByList) {
            String[] orderSplit = order.split(" ");
            if (orderSplit[0].equals("eventDate")) {
                if (!isFirst) {
                    jpql.append(", ");
                }
                jpql.append("e.eventDate ").append(orderSplit[1]);
                isFirst = false;
            }
            if (orderSplit[0].equals("city")) {
                if (!isFirst) {
                    jpql.append(", ");
                }
                jpql.append("a.city ").append(orderSplit[1]);
                isFirst = false;
            }
        }
    }

    /**
     * Constrói uma lista de condições com base nos parâmetros fornecidos.
     *
     * @param paramsMap mapa contendo os parâmetros de filtro.
     * @return a lista de condições WHERE.
     */
    public List<String> buildWhereList(Map<String, String> paramsMap) {
        List<String> whereList = new ArrayList<>();

        String name = paramsMap.get("name");
        String cpf = paramsMap.get("cpf");
        String eventDate = paramsMap.get("EventDate");
        String city = paramsMap.get("city");

        if (!StringUtil.isNullOrEmpty(name)) {
            whereList.add("c.name = '" + name + "'");
        }
        if (!StringUtil.isNullOrEmpty(cpf)) {
            whereList.add("c.cpf = " + cpf);
        }
        if (!StringUtil.isNullOrEmpty(eventDate)) {
            whereList.add("e.eventDate = :eventDate");
        }
        if (!StringUtil.isNullOrEmpty(city)) {
            whereList.add("a.city = '" + city + "'");
        }

        return whereList;
    }
}
