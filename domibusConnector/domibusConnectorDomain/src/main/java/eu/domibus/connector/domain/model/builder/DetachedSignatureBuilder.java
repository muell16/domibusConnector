/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;
import javax.annotation.Nonnull;

/**
 * Builder for @see eu.domibus.connector.domain.model.DetachedSignatureBuilder
 * 
 * @author {@literal Stephan Spindler <stephan.spindler@extern.brz.gv.at> }
 */
public class DetachedSignatureBuilder {

    private byte detachedSignature[];
	private String detachedSignatureName;
	private DetachedSignatureMimeType mimeType;
    
    public static DetachedSignatureBuilder build() {
        return new DetachedSignatureBuilder();
    }
    
    private DetachedSignatureBuilder() {};
    
    /**
     * the signature @see eu.domibus.connector.domain.model.DetachedSignatureBuilder#detachedSignature
     * @param signature - the signature, must be not null
     * @return the builder
     */
    public DetachedSignatureBuilder setSignature(@Nonnull byte[] signature) {
        this.detachedSignature = signature;
        return this;
    }
    
    /**
     * the name @see eu.domibus.connector.domain.model.DetachedSignatureBuilder#detachedSignatureName
     * @param name - the name
     * @return the builder
     */
    public DetachedSignatureBuilder setName(@Nonnull String name) {
        this.detachedSignatureName = name;
        return this;
    }
    
    /**
     * the mimeType @see eu.domibus.connector.domain.model.DetachedSignatureBuilder#mimeType
     * @param mimeType - mimeType
     * @return  the builder
     */
    public DetachedSignatureBuilder setMimeType(@Nonnull DetachedSignatureMimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }
    
    /**
     * creates the DetachedSignature based 
     * on the provided properties, also checks 
     * if the requirements are fullfilled
     * @throws IllegalArgumentException if an argument is missing or illegal
     * @return the created DetachedSignature
     */
    public DetachedSignature create() {
        if (detachedSignature == null || detachedSignature.length < 1) {
            throw new IllegalArgumentException("");
        }
        if (detachedSignatureName == null) {
            throw new IllegalArgumentException("detachedSignatureName is mandatory!");
        }
        if (mimeType == null) {
            throw new IllegalArgumentException("mimeType is mandatory!");
        }
        return new DetachedSignature(detachedSignature, detachedSignatureName, mimeType);
    }
    
    
}
