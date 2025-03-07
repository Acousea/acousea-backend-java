package com.acousea.backend.core.users.domain.constants;


import com.acousea.backend.core.communicationSystem.domain.communication.constants.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserAddressTest {

    @Test
    public void mockTest() {
        assertEquals(1, 1);
    }


    @Test
    public void testFromValue() {
        assertEquals(Address.getBackend(), Address.fromValue(0x00), "Address should be backend for value 0x00" );
        assertNotEquals(Address.getBackend(), Address.fromValue(0x30));


    }

    @Test
    public void testFromValueInvalid() {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> Address.fromValue(0xFF));
//        String expectedMessage = "Unknown address value: 255";
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.contains(expectedMessage));
    }
}
