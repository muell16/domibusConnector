/*
 * 
 */
package org.holodeck.backend.db.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.holodeck.backend.db.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class MessageDAO.
 */
@Service
public class MessageDAO implements IMessageDAO {
	
	/** The log. */
	private Logger log = Logger.getLogger(MessageDAO.class);

	//property constants
	/** The Constant UID. */
	public static final String UID = "uid";
	
	/** The Constant DIRECTORY. */
	public static final String DIRECTORY = "directory";
	
	/** The Constant DOWNLOADED. */
	public static final String DOWNLOADED = "downloaded";
	
	/** The Constant DELETED. */
	public static final String DELETED = "deleted";

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	private EntityManager getEntityManager() {
		return entityManager;
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#save(org.holodeck.backend.db.model.Message)
	 */
	@Transactional
	public void save(Message entity) {
		log.debug("saving Message instance");
		try {
			getEntityManager().persist(entity);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#delete(org.holodeck.backend.db.model.Message)
	 */
	public void delete(Message entity) {
		log.debug("deleting Message instance");
		try {
			entity = getEntityManager().getReference(Message.class, entity.getIdMessage());
			getEntityManager().remove(entity);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#update(org.holodeck.backend.db.model.Message)
	 */
	public Message update(Message entity) {
		log.debug("updating Message instance");
		try {
			Message result = getEntityManager().merge(entity);
			log.debug("update successful");
			return result;
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findById(java.lang.Integer)
	 */
	public Message findById(Integer id) {
		log.debug("finding Message instance with id: " + id);
		try {
			Message instance = getEntityManager().find(Message.class, id);
			return instance;
		} catch (RuntimeException re) {
			log.error("find failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findByProperty(java.lang.String, java.lang.Object, int[])
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
		log.debug("finding Message instance with property: " + propertyName + ", value: " + value);
		try {
			final String queryString = "select model from Message model where model." + propertyName
					+ "= :propertyValue";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findByUid(java.lang.Object, int[])
	 */
	public List<Message> findByUid(Object uid, int... rowStartIdxAndCount) {
		return findByProperty(UID, uid, rowStartIdxAndCount);
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findByDirectory(java.lang.Object, int[])
	 */
	public List<Message> findByDirectory(Object directory, int... rowStartIdxAndCount) {
		return findByProperty(DIRECTORY, directory, rowStartIdxAndCount);
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findByDownloaded(java.lang.Object, int[])
	 */
	public List<Message> findByDownloaded(Object downloaded, int... rowStartIdxAndCount) {
		return findByProperty(DOWNLOADED, downloaded, rowStartIdxAndCount);
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findByDeleted(java.lang.Object, int[])
	 */
	public List<Message> findByDeleted(Object deleted, int... rowStartIdxAndCount) {
		return findByProperty(DELETED, deleted, rowStartIdxAndCount);
	}

	/* (non-Javadoc)
	 * @see org.holodeck.backend.db.dao.IMessageDAO#findAll(int[])
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findAll(final int... rowStartIdxAndCount) {
		log.debug("finding all Message instances");
		try {
			final String queryString = "select model from Message model";
			Query query = getEntityManager().createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * Find not downloaded sorted by date.
	 *
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findNotDownloadedSortedByDate(final int... rowStartIdxAndCount) {
		log.debug("finding NotDownloadedSortedByDate Message instances");
		try {
			final String queryString = "select model" +
							" from Message model" +
							" where model.downloaded = false" +
							" and model.deleted = false" +
							" order by model.date asc";
			Query query = getEntityManager().createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}
				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("findNotDownloadedSortedByDate failed", re);
			throw re;
		}
	}

	/**
	 * Gets the first not downloaded sorted by date.
	 *
	 * @return the first not downloaded sorted by date
	 */
	public Message getFirstNotDownloadedSortedByDate() {
		log.debug("finding FirstNotDownloadedSortedByDate");
		try {
			List<Message> messages = findNotDownloadedSortedByDate(0,1);

			if(messages!=null && messages.size()==1){
				return messages.get(0);
			}
			else{
				return null;
			}
		} catch (RuntimeException re) {
			log.error("getFirstNotDownloadedSortedByDate failed", re);
			throw re;
		}
	}

	/**
	 * Find not deleted.
	 *
	 * @param messagesTimeLiveInDays the messages time live in days
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public List<Message> findNotDeleted(int messagesTimeLiveInDays) {
		log.debug("finding NotDeleted");
		try {
			final String queryString = "select model" +
							" from Message model" +
							" where model.deleted = false" +
							" and model.date < :minDate";
			Query query = getEntityManager().createQuery(queryString);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -messagesTimeLiveInDays);

			query.setParameter("minDate", calendar.getTime());

			return query.getResultList();
		} catch (RuntimeException re) {
			log.error("findNotDeleted failed", re);
			throw re;
		}
	}
}