package ru.ydn.wicket.wicketorientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.security.OSecurityManager;

import ru.ydn.wicket.wicketorientdb.components.IHookPosition;


/**
*	Support of changing system users passwords in web interface
*/
public class OUserCatchPasswordHook extends ODocumentHookAbstract implements IHookPosition {

	
	public OUserCatchPasswordHook(ODatabaseDocument database){
		super(database);
		setIncludeClasses("OUser");
	}
	
	@Override
	public RESULT onRecordBeforeUpdate(final ODocument iDocument) {
		String name = iDocument.field("name");
		String password = iDocument.field("password");
		
		if (password.startsWith(OSecurityManager.HASH_ALGORITHM_PREFIX) 
				|| password.startsWith(OSecurityManager.PBKDF2_ALGORITHM_PREFIX)
				|| password.startsWith(OSecurityManager.PBKDF2_SHA256_ALGORITHM_PREFIX)){
			return RESULT.RECORD_NOT_CHANGED;
		}
		
		IOrientDbSettings settings = OrientDbWebApplication.get().getOrientDbSettings();
		if (	settings.getAdminUserName()!=null && 
				settings.getAdminUserName().equals(name)){
			settings.setAdminPassword(password);
		}
		if (settings.getGuestUserName()!=null && 
				settings.getGuestUserName().equals(name)){
			settings.setGuestPassword(password);
		}
		if(OrientDbWebSession.exists()) {
			OrientDbWebSession session = OrientDbWebSession.get();
			if (session.getUsername()!=null && session.getUsername().equals(name)){
				session.setUser(name, password);
			}
		}
		return RESULT.RECORD_NOT_CHANGED;
	}
	
	@Override
	public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
		return DISTRIBUTED_EXECUTION_MODE.TARGET_NODE;
	}

	@Override
	public HOOK_POSITION getPosition() {
		return HOOK_POSITION.FIRST;
	}
}
