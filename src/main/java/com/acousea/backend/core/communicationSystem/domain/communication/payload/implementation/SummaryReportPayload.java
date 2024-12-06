package com.acousea.backend.core.communicationSystem.domain.communication.payload.implementation;

import com.acousea.backend.core.communicationSystem.domain.communication.CommunicationPacket;
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
public class SummaryReportPayload implements Payload {
    private List<Tag> tags;

    public SummaryReportPayload(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public short getBytesSize() {
        int size = tags.stream().mapToInt(Tag::getFullLength).sum();
        if (size > CommunicationPacket.MaxSizes.MAX_PAYLOAD_SIZE)  {
            throw new IllegalArgumentException(SummaryReportPayload.class.getSimpleName() + "Payload size is too big");
        }
        return (short) size;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getBytesSize());
        tags.forEach(tag -> buffer.put(tag.toBytes()));
        return buffer.array();
    }

    public static Payload fromBytes(ByteBuffer buffer) {
        List<Tag> tags = new ArrayList<>();
        while (buffer.hasRemaining()) {
            Tag tag = TagFactory.createTag(buffer);
            tags.add(tag);
        }
        return new SummaryReportPayload(tags);
    }
}
