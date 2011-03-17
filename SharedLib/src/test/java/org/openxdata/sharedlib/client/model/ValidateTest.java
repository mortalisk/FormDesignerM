package org.openxdata.sharedlib.client.model;

import org.junit.Test;

public class ValidateTest {

    @Test(expected = IllegalArgumentException.class)
    public void isNullShouldThrowIllegalArgumentExceptionOnNull() {
        Validate.notNull(null, "null");
    }

    @Test
    public void isNullShouldBeNice() {
        Validate.notNull(new Object(), "object");
    }
}
