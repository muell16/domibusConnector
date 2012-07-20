package eu.ecodex.connector.mapping;

import eu.ecodex.connector.mapping.exception.ECodexConnectorContentMapperException;

/**
 * Interface with methods to map national format XML to eCodex format and vice
 * versa. Inheritance must be configured.
 * 
 * @author riederb
 * 
 */
public interface ECodexConnectorContentMapper {

    /**
     * Method to map international eCodex XML to national format. Must be
     * overridden when ContentMapper is used by configuration.
     * 
     * @param internationalContent
     *            - eCodex XML.
     * @return nationalContent as byte array.
     * @throws ECodexConnectorContentMapperException
     */
    public byte[] mapInternationalToNational(byte[] internationalContent) throws ECodexConnectorContentMapperException;

    /**
     * Method to map national XML to international eCodex format. Must be
     * overridden when ContentMapper is used by configuration.
     * 
     * @param nationalContent
     * @return eCodex XML as byte array.
     * @throws ECodexConnectorContentMapperException
     */
    public byte[] mapNationalToInternational(byte[] nationalContent) throws ECodexConnectorContentMapperException;

}
