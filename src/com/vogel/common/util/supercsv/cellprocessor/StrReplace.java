package com.vogel.common.util.supercsv.cellprocessor;

import com.vogel.common.util.supercsv.cellprocessor.ift.BoolCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DateCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DoubleCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.LongCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.StringCellProcessor;
import com.vogel.common.util.supercsv.exception.NullInputException;
import com.vogel.common.util.supercsv.exception.SuperCSVException;
import com.vogel.common.util.supercsv.util.CSVContext;

/**
 * String replacer.
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 */
public class StrReplace extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
private String searchText, replaceText;

/**
 * String relpace
 * 
 * @param searchText
 *            text to search for
 * @param replaceText
 *            tetx to replace with
 */
public StrReplace(final String searchText, final String replaceText) {
	super();
	handleArguments(searchText, replaceText);
}

public StrReplace(final String searchText, final String replaceText, final StringCellProcessor next) {
	super(next);
	handleArguments(searchText, replaceText);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) {
		throw new NullInputException("Input cannot be null", context, this);
	}
	String result = value.toString().replaceAll(searchText, replaceText);
	return next.execute(result, context);
}

private void handleArguments(final String searchText, final String replaceText) throws IllegalArgumentException {
	if( searchText == null ) {
		throw new NullInputException("searchtext cannot be null", this);
	}
	if( replaceText == null ) {
		throw new NullInputException("replacettext cannot be null", this);
	}
	if( searchText.equals("") ) {
		throw new SuperCSVException("argument searchText cannot be \"\" as this has no effect", this);
	}
	this.searchText = searchText;
	this.replaceText = replaceText;
}
}
