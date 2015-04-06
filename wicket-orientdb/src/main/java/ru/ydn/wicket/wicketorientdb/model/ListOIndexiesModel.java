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

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Args;

import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;

public class ListOIndexiesModel extends AbstractListModel<OIndex<?>> {

    private IModel<OClass> oClassModel;
    private IModel<Boolean> allIndexiesModel;

    public ListOIndexiesModel(final IModel<OClass> oClassModel, final IModel<Boolean> allIndexiesModel) {
        Args.notNull(oClassModel, "oClassModel");
        this.oClassModel = oClassModel;
        this.allIndexiesModel = allIndexiesModel;
    }

    @Override
    public Collection<OIndex<?>> getData() {
        OClass oClass = oClassModel.getObject();
        if (oClass == null) {
            return null;
        } else if (allIndexiesModel == null || Boolean.TRUE.equals(allIndexiesModel.getObject())) {
            return oClass.getIndexes();
        } else {
            return oClass.getClassIndexes();
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (allIndexiesModel != null) {
            allIndexiesModel.detach();
        }
        oClassModel.detach();
    }

}
