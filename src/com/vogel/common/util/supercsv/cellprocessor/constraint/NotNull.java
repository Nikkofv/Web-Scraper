package com.vogel.common.util.supercsv.cellprocessor.constraint;

import com.vogel.common.util.supercsv.cellprocessor.CellProcessorAdaptor;
import com.vogel.common.util.supercsv.cellprocessor.ift.BoolCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.CellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DateCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DoubleCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.LongCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.StringCellProcessor;
import com.vogel.common.util.supercsv.exception.NullInputException;
import com.vogel.common.util.supercsv.exception.SuperCSVException;
import com.vogel.common.util.supercsv.util.CSVContext;

/**
 * This processor checks if the input is 'null', and raises an exception in that case. In all other cases, the next
 * processor in the chain is invoked.
 * <p>
 * You should only use this processor, when a column must be non-null, but you do not need to apply any other processor
 * to the column.
 * <P>
 * If you apply other processors to the column, you can safely omit this processor as all other processors should do a
 * null-check on its input.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class NotNull extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor, DoubleCellProcessor,
	LongCellProcessor, StringCellProcessor {

public NotNull() {
	super();
}

public NotNull(final CellProcessor next) {
	super(next);
}

/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a 'null' value
 * @return the argument value transformed by next processors
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	
	return next.execute(value, context);
}
}
