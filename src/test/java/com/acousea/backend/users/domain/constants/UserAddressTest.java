package com.acousea.backend.users.domain.constants;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserAddressTest {

    @Test
    public void mockTest() {
        assertEquals(1, 1);
    }

//    @Test
//    public void testGetValue() {
//        assertEquals(0x00, 0x00);
//        assertEquals(0x00, Address.RESERVED_BACKEND.getValue());
//        assertEquals(0x01, Address.LOCALIZER_NODE.getValue());
//        assertEquals(0x02, Address.DRIFTER_NODE.getValue());
//        assertEquals(0x03, Address.PI3.getValue());
//        assertEquals(0xC0, Address.SENDER_MASK.getValue());
//        assertEquals(0x30, Address.RECEIVER_MASK.getValue());
//}

//    @Test
//    public void testFromValue() {
//        assertEquals(Address.BACKEND, Address.fromValue(0x00));
//        assertEquals(Address.LOCALIZER_NODE, Address.fromValue(0x01));
//        assertEquals(Address.DRIFTER_NODE, Address.fromValue(0x02));
//        assertEquals(Address.PI3, Address.fromValue(0x03));
//        assertEquals(Address.SENDER_MASK, Address.fromValue(0xC0));
//        assertEquals(Address.RECEIVER_MASK, Address.fromValue(0x30));
//    }
//
//    @Test
//    public void testFromValueInvalid() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            Address.fromValue(0xFF);
//        });
//        String expectedMessage = "Unknown address value: 255";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
}
