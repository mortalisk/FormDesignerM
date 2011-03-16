package org.openxdata.gwt.designer.client.widget.skiprule;

/**
 * Small validation methods.
 *
 * @apiviz.exclude
 */
public final class Validate {

    private Validate() {
    }

    /**
     * Verifies that object is not null, otherwise throws exception.
     * @param object to be checked
     * @param name to identify object in exception
     * @throws IllegalArgumentException
     */
    public static void notNull(final Object object, final String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }
}
