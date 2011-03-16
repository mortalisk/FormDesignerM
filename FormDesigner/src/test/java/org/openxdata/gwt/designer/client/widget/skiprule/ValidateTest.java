package org.openxdata.gwt.designer.client.widget.skiprule;

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
