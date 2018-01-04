/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.domibus.connector.domain.model.builder;

import eu.domibus.connector.domain.model.DetachedSignature;
import eu.domibus.connector.domain.model.DetachedSignatureMimeType;

/**
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
    
    
    public DetachedSignatureBuilder setSignature(byte[] signature) {
        this.detachedSignature = signature;
        return this;
    }
    
    public DetachedSignatureBuilder setName(String name) {
        this.detachedSignatureName = name;
        return this;
    }
    
    public DetachedSignatureBuilder setMimeType(DetachedSignatureMimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }
    
    
    public DetachedSignature create() {
        if (detachedSignature == null || detachedSignature.length < 1) {
            throw new IllegalArgumentException("");
        }
        return new DetachedSignature(detachedSignature, detachedSignatureName, mimeType);
    }
    
    
}
