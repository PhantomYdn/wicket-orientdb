package ru.ydn.wicket.wicketorientdb.utils.query.filter;

import org.apache.wicket.model.IModel;

/**
 * Embedded contains value filter criteria
 * SELECT FROM Test WHERE embedded.values() CONTAINS :myValue
 * @param <T> type of model for filtering
 */
public class EmbeddedContainsValueCriteria<T> extends AbstractFilterCriteria<T> {

	private static final long serialVersionUID = 1L;

	public EmbeddedContainsValueCriteria(String field, IModel<T> model, IModel<Boolean> join) {
        super(field, model, join);
    }

    @Override
    protected String apply(String field) {
        return " " + field + ".values() CONTAINS :" + getPSVariableName();
    }


    @Override
    public FilterCriteriaType getFilterCriteriaType() {
        return FilterCriteriaType.EMBEDDED_CONTAINS_VALUE;
    }
}
