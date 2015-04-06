/**
 * Copyright (C) 2015 Ilia Naryzhny (phantom@ydn.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.ydn.wicket.wicketorientdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class CollectionAdapterModel<T, M extends Collection<T>> extends LoadableDetachableModel<List<T>> {

    private IModel<M> model;

    public CollectionAdapterModel(IModel<M> model) {
        this.model = model;
    }

    @Override
    protected List<T> load() {
        M ret = model.getObject();
        if (ret == null) {
            return null;
        } else if (ret instanceof List) {
            return (List<T>) ret;
        } else {
            return new ArrayList<T>(ret);
        }
    }

    @Override
    public void setObject(List<T> object) {
        setCollection(object);
        super.setObject(object);
    }

    protected void setCollection(List<T> object) {
        if (object == null) {
            model.setObject(null);
        } else {
            M collection = model.getObject();
            if (collection != null) {
                collection.clear();
                collection.addAll(object);
            } else {
                throw new WicketRuntimeException("Creation of collection is not supported. Please override this method of you need support.");
            }
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        model.detach();
    }

}
