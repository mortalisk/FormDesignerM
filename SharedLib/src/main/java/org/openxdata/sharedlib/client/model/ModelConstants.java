package org.openxdata.sharedlib.client.model;

import java.io.Serializable;

/**
 * EpihandyConstants shared throughout classes in the containing package.
 *
 * @version ,
 */
public class ModelConstants implements Serializable{
	
	/**
	 * Generated serialization ID
	 */
	private static final long serialVersionUID = -830180521446083067L;

	/** Empty strig representation */
	public static final String EMPTY_STRING = "";
	
	/** Index for no selection */
	public static final int NO_SELECTION = -1;
	
	/** ID not set numeric value */
	public static final int NULL_ID = -1;
	
	/** Conditions perator not set. */
	public static final int CONDITIONS_OPERATOR_NULL = 0;
	
	/** Conditions operator AND */
	public static final int CONDITIONS_OPERATOR_AND = 1;
	
	/** Conditions Operator OR */
	public static final int CONDITIONS_OPERATOR_OR = 2;
	
	/** The Value Function. */
	public static final int FUNCTION_VALUE = 1;
	
	/** The Length function. */
	public static final int FUNCTION_LENGTH = 2;
	
	/** No rule action specified */
	public static final int ACTION_NONE = 0;
	
	/** Rule action to hide questions */
	public static final int ACTION_HIDE = 1 << 1;
	
	/** Rule action to show questions */
	public static final int ACTION_SHOW = 1 << 2;
	
	/** Rule action to disable questions */
	public static final int ACTION_DISABLE = 1 << 3;
	
	/** Rule action to enable questions */
	public static final int ACTION_ENABLE = 1 << 4;
	
	/** Rule action to make a question mandatory */
	public static final int ACTION_MAKE_MANDATORY = 1 << 5;
	
	/** Rule action to make a question optional */
	public static final int ACTION_MAKE_OPTIONAL = 1 << 6;
	
}
