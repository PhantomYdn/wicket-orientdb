package ru.ydn.wicket.wicketorientdb.web;

import com.orientechnologies.orient.core.metadata.security.ODatabaseSecurityResources;

import ru.ydn.wicket.wicketorientdb.security.OrientPermission;
import ru.ydn.wicket.wicketorientdb.security.RequiredOrientResource;

@RequiredOrientResource(value=ODatabaseSecurityResources.CLASS+".ORole", permissions=OrientPermission.UPDATE)
public class StaticSecuredPage extends OrientDbTestPage
{
	private static final long serialVersionUID = 1L;

}
