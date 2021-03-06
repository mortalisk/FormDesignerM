package org.openxdata.sharedlib.client.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * A Question that represents an image input.
 */
public final class ImageQuestion extends AbstractQuestion {

    @Override
    public Set<Operator> validOperators() {
        return EnumSet.of(Operator.IS_NULL, Operator.IS_NOT_NULL);
    }
}
