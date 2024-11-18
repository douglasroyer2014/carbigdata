package br.com.carbigdata.eventphoto.repository;

import br.com.carbigdata.eventphoto.entity.EventPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPhotoRepository extends JpaRepository<EventPhotoEntity, Integer> {
}
