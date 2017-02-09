package com.vogel.common.util.supercsv.cellprocessor;

import com.vogel.common.util.supercsv.cellprocessor.ift.BoolCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.StringCellProcessor;
import com.vogel.common.util.supercsv.exception.ClassCastInputCSVException;
import com.vogel.common.util.supercsv.exception.NullInputException;
import com.vogel.common.util.supercsv.exception.SuperCSVException;
import com.vogel.common.util.supercsv.util.CSVContext;

/**
 * Converts a boolean into a formatted string. If you want to convert from a String to a boolean, use the
 * {@link ParseBool} processor.
 * <p>
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class FmtBool extends CellProcessorAdaptor implements BoolCellProcessor {

private final String trueValue;
private final String falseValue;

public FmtBool(final String trueValue, final String falseValue) {
	super();
	this.trueValue = trueValue;
	this.falseValue = falseValue;
}

public FmtBool(final String trueValue, final String falseValue, final StringCellProcessor next) {
	super(next);
	this.trueValue = trueValue;
	this.falseValue = falseValue;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) {
		throw new NullInputException("Input cannot be null on line " + context.lineNumber + " column "
			+ context.columnNumber, context, this);
	}
	if( !(value instanceof Boolean) ) {
		throw new ClassCastInputCSVException("the value '" + value + "' is not of type Boolean", context, this);
	}
	String result = ((Boolean) value).booleanValue() ? trueValue : falseValue;
	return next.execute(result, context);
}
}
