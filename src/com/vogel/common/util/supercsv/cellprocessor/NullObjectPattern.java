package com.vogel.common.util.supercsv.cellprocessor;

import com.vogel.common.util.supercsv.cellprocessor.ift.BoolCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DateCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.DoubleCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.CellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.LongCellProcessor;
import com.vogel.common.util.supercsv.cellprocessor.ift.StringCellProcessor;
import com.vogel.common.util.supercsv.util.CSVContext;

/**
 * This is an implementation-specific processor and should NOT be used by anyone other than in the implementation of
 * cell processors. It is the implementation of "the null object pattern".
 * 
 * @author Kasper B. Graversen
 */
public class NullObjectPattern extends CellProcessorAdaptor implements CellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor, BoolCellProcessor {

/** There is no need to create many instances of this class in order to fulfill the null-object pattern. */
public static final NullObjectPattern INSTANCE = new NullObjectPattern();

NullObjectPattern() {
	super();
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	return value;
}
}
