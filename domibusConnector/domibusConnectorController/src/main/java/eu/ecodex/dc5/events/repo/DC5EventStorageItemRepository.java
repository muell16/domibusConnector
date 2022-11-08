package eu.ecodex.dc5.events.repo;

import eu.ecodex.dc5.events.model.JPAEventStorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DC5EventStorageItemRepository extends JpaRepository<JPAEventStorageItem, Long> {


//    //Transactional allow READ_UNCOMMITTED...?
//    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
//    public List<Long> getAllEventIds();

    @Modifying
    @Query("UPDATE JPAEventStorageItem i SET i.consumed = CURRENT_TIMESTAMP WHERE i.id = :id")
    public void setConsumed(long id);

}
