package eu.domibus.connector.domain.model.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import eu.domibus.connector.domain.model.LargeFileReference;

import java.io.IOException;

public class LargeFileDeserializer extends StdDeserializer<LargeFileReference> {

    protected LargeFileDeserializer(Class<?> vc) {
        super(vc);
    }

    protected LargeFileDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected LargeFileDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public LargeFileReference deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        LargeFileReference largeFileReference = new LargeFileReference();
        TreeNode treeNode = p.getCodec().readTree(p);
        largeFileReference.setStorageIdReference(((TextNode)treeNode.get(LargeFileReferenceSerializer.STORAGE_ID_REFERENCE_FIELD_NAME)).asText());
        largeFileReference.setStorageProviderName(((TextNode)treeNode.get(LargeFileReferenceSerializer.STORAGE_PROVIDER_FIELD_NAME)).asText());
        largeFileReference.setMimetype(((TextNode)treeNode.get(LargeFileReferenceSerializer.MIME_TYPE_FIELD_NAME)).asText());
        largeFileReference.setName(((TextNode)treeNode.get(LargeFileReferenceSerializer.NAME_FIELD_NAME)).asText());
        largeFileReference.setText(((TextNode)treeNode.get(LargeFileReferenceSerializer.TEXT_FIELD_NAME)).asText());
        largeFileReference.setSize(((IntNode)treeNode.get(LargeFileReferenceSerializer.SIZE_FIELD_NAME)).asLong());
        return largeFileReference;
    }

}
