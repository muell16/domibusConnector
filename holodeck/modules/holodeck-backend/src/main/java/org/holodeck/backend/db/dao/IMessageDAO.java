/*
 * 
 */
package org.holodeck.backend.db.dao;

import java.util.List;

import org.holodeck.backend.db.model.Message;

/**
 * The Interface IMessageDAO.
 */
public interface IMessageDAO {
	
	/**
	 * Save.
	 *
	 * @param entity the entity
	 */
	public void save(Message entity);

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 */
	public void delete(Message entity);

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the message
	 */
	public Message update(Message entity);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the message
	 */
	public Message findById(Integer id);

	/**
	 * Find by property.
	 *
	 * @param propertyName the property name
	 * @param value the value
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findByProperty(String propertyName, Object value, int... rowStartIdxAndCount);

	/**
	 * Find by uid.
	 *
	 * @param uid the uid
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findByUid(Object uid, int... rowStartIdxAndCount);

	/**
	 * Find by directory.
	 *
	 * @param directory the directory
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findByDirectory(Object directory, int... rowStartIdxAndCount);

	/**
	 * Find by downloaded.
	 *
	 * @param downloaded the downloaded
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findByDownloaded(Object downloaded, int... rowStartIdxAndCount);

	/**
	 * Find by deleted.
	 *
	 * @param deleted the deleted
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findByDeleted(Object deleted, int... rowStartIdxAndCount);

	/**
	 * Find all.
	 *
	 * @param rowStartIdxAndCount the row start idx and count
	 * @return the list
	 */
	public List<Message> findAll(int... rowStartIdxAndCount);
}