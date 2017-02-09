package com.vogel.common.util.supercsv.cellprocessor.ift;

import com.vogel.common.util.supercsv.util.CSVContext;

public interface CellProcessor {
public abstract Object execute(final Object value, final CSVContext context);
}
