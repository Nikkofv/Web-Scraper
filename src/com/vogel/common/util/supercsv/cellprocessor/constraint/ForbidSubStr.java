/**
 *
 */
package com.vogel.common.util.supercsv.cellprocessor.constraint;

import java.util.List;

import com.vogel.common.util.supercsv.cellprocessor.CellProcessorAdaptor;
import com.vogel.common.util.supercsv.cellprocessor.ift.CellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.StringCellProcessor;
import com.vogel.common.util.supercsv.exception.NullInputException;
import com.vogel.common.util.supercsv.exception.SuperCSVException;
import com.vogel.common.util.supercsv.util.CSVContext;

/**
 * Convert to string and ensure the input string is not present in a set of specified sub strings. Such constraint is
 * handy when reading/writing e.g. filenames and wanting to ensure no filename contains e.g. ":", "/", ...
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class ForbidSubStr extends CellProcessorAdaptor implements StringCellProcessor {

String[] forbiddenSubStrings;

public ForbidSubStr(final List<String> forbiddenSubStrings) {
	this(forbiddenSubStrings.toArray(new String[0]));
}

public ForbidSubStr(final List<String> forbiddenSubStrings, final CellProcessor next) {
	this(forbiddenSubStrings.toArray(new String[0]), next);
}

public ForbidSubStr(final String... forbiddenSubStrings) {
	super();
	this.forbiddenSubStrings = forbiddenSubStrings.clone();
}

public ForbidSubStr(final String forbiddenSubString, final CellProcessor next) {
	this(new String[] { forbiddenSubString }, next);
}

public ForbidSubStr(final String[] forbiddenSubStrings, final CellProcessor next) {
	super(next);
	this.forbiddenSubStrings = forbiddenSubStrings.clone();
}

/**
 * {@inheritDoc}
 * 
 * @throws ClassCastException
 *             is the parameter value cannot be cast to a String
 * @throws SuperCSVException
 *             if the input contains any of the substrings
 * @return the argument value
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final String sval = value.toString(); // cast
	
	// check for forbidden strings
	for( final String forbidden : forbiddenSubStrings ) {
		if( sval.indexOf(forbidden) != -1 ) { throw new SuperCSVException("Entry \"" + value + "\" on line "
			+ context.lineNumber + " column " + context.columnNumber + " contains the forbidden char \"" + forbidden
			+ "\"", context, this); }
	}
	
	return next.execute(value, context);
}
}
