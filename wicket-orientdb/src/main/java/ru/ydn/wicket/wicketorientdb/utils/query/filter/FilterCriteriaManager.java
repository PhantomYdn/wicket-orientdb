package ru.ydn.wicket.wicketorientdb.utils.query.filter;

import com.google.common.collect.Maps;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Default implementation of {@link IFilterCriteriaManager}
 */
public class FilterCriteriaManager implements IFilterCriteriaManager {

    private final IModel<OProperty> propertyModel;
    private final Map<FilterCriteriaType, IFilterCriteria> filterCriterias;
    private boolean and;

    public FilterCriteriaManager(IModel<OProperty> propertyModel) {
        Args.notNull(propertyModel, "propertyModel");
        Args.notNull(propertyModel.getObject(), "propertyModel.getObject()");
        this.propertyModel = propertyModel;
        filterCriterias = Maps.newHashMap();
        and = true;
    }

    @Override
    public String apply() {
        if (filterCriterias.isEmpty())
            return null;

        StringBuilder sb = new StringBuilder();
        String logicalOperator = and ? " AND " : " OR ";
        int counter = 0;
        for (FilterCriteriaType key : filterCriterias.keySet()) {
            IFilterCriteria filterCriteria = filterCriterias.get(key);
            if (filterCriteria == null)
                continue;

            if (!filterCriteria.isEmpty()) {
                if (counter > 0)
                    sb.append(logicalOperator);
                sb.append(filterCriteria.apply());
                counter++;
            }
        }
        return sb.toString();
    }

    @Override
    public void setAnd(boolean and) {
        this.and = and;
    }

    @Override
    public IFilterCriteria createEqualsFilterCriteria(IModel<?> model, IModel<Boolean> join) {
        OProperty property = propertyModel.getObject();
        return new EqualsFilterCriteria(property.getName(), model, join);
    }

    @Override
    public IFilterCriteria createCollectionFilterCriteria(IModel<Collection<IModel<?>>> models, IModel<Boolean> join) {
        Args.notNull(models.getObject(), "models");
        OProperty property = propertyModel.getObject();
        return new CollectionFilterCriteria(property.getName(), models, join);
    }

    @Override
    public IFilterCriteria createRangeFilterCriteria(IModel<Collection<IModel<?>>> models, IModel<Boolean> join) {
        Args.notNull(models.getObject(), "models");
        Args.isTrue(models.getObject().size() == 2, "models.getObject().size() == 2");
        OProperty property = propertyModel.getObject();
        return new RangeFilterCriteria(property.getName(), models, join);
    }


    @Override
    public IFilterCriteria createContainsStringFilterCriteria(IModel<String> model, IModel<Boolean> join) {
        OProperty property = propertyModel.getObject();
        return new ContainsTextFilterCriteria(property.getName(), model, join);
    }

    @Override
    public IFilterCriteria createLinkCollectionFilterCriteria(IModel<Collection<ODocument>> model, boolean list, IModel<Boolean> join) {
        OProperty property = propertyModel.getObject();
        return new CollectionLinkFilterCriteria(property.getName(), model, list, join);
    }

    @Override
    public void addFilterCriteria(IFilterCriteria filterCriteria) {
        filterCriterias.put(filterCriteria.getFilterCriteriaType(), filterCriteria);
    }

    @Override
    public IFilterCriteria getFilterCriteria(FilterCriteriaType type) {
        return filterCriterias.get(type);
    }

    @Override
    public Map<FilterCriteriaType, IFilterCriteria> getFilterCriterias() {
        return Collections.unmodifiableMap(filterCriterias);
    }

    @Override
    public void clearFilterCriterias() {
        filterCriterias.clear();
    }

    @Override
    public boolean isFilterApply() {
        for (IFilterCriteria criteria : filterCriterias.values()) {
            if (criteria != null) {
                if (!criteria.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
