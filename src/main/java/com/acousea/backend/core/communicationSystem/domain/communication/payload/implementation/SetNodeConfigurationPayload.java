package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagFactory;
import com.acousea.backend.core.communicationSystem.domain.nodes.NodeDevice;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SetNodeConfigurationPayload implements Payload {
    private List<Tag> tags;

    public SetNodeConfigurationPayload(List<Tag> tags) {
        this.tags = tags;
    }

    public static SetNodeConfigurationPayload fromNodeDevice(@NotNull NodeDevice nodeDevice) {
        List<Tag> tags = TagFactory.createTags(nodeDevice);
        return new SetNodeConfigurationPayload(tags);
    }

    @Override
    public String encode() {
        StringBuilder builder = new StringBuilder();
        tags.forEach(tag -> builder.append(tag.encode()));
        return builder.toString();
    }

    @Override
    public int getFullLength() {
        return tags.stream().mapToInt(Tag::getFullLength).sum();
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[getFullLength()];
        int offset = 0;
        for (Tag tag : tags) {
            byte[] tagBytes = tag.toBytes();
            System.arraycopy(tagBytes, 0, bytes, offset, tagBytes.length);
            offset += tagBytes.length;
        }
        return bytes;
    }

    public static Payload fromBytes(ByteBuffer buffer) {
        List<Tag> tags = new ArrayList<>();
        while (buffer.hasRemaining()) {
            Tag tag = TagFactory.createTag(buffer);
            tags.add(tag);
        }
        return new SetNodeConfigurationPayload(tags);
    }
}
