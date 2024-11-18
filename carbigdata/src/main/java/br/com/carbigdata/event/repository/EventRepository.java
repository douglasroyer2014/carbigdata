package br.com.carbigdata.event.repository;

import br.com.carbigdata.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer>, EventRepositoryCustom {
}
