package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.application.command.DTO.NodeDeviceDTO;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.Payload;
import com.acousea.backend.core.communicationSystem.domain.communication.payload.PayloadFactory;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.Tag;
import com.acousea.backend.core.communicationSystem.domain.communication.tags.TagFactory;
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

    public static SetNodeConfigurationPayload fromNodeDeviceDTO(@NotNull NodeDeviceDTO dto) {
        List<Tag> tags = new ArrayList<>();
        if (dto.getExtModules() == null) {
            return null;
        }
        // TODO: Create factory method to create all tags from DTO
//        PayloadFactory.from(dto);

        // TODO: Add the rest of the tags
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
