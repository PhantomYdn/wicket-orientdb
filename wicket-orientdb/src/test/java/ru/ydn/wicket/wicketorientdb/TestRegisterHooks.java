package ru.ydn.wicket.wicketorientdb;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;
import com.orientechnologies.orient.core.hook.ORecordHook;
import org.junit.ClassRule;
import org.junit.Test;
import ru.ydn.wicket.wicketorientdb.junit.WicketOrientDbTesterScope;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class TestRegisterHooks {

    @ClassRule
    public static WicketOrientDbTesterScope wicket = new WicketOrientDbTesterScope();

    @Test
    public void testRegisterHooks() {
        ODatabaseDocument db = wicket.getTester().getDatabase();
        IOrientDbSettings settings = wicket.getTester().getApplication().getOrientDbSettings();


        List<Class<? extends ORecordHook>> hooks = new LinkedList<>(settings.getORecordHooks());
        hooks.add(RegisterHook.class);
        settings.setORecordHooks(hooks);

        assertRegisteredHook(db, RegisterHook.class);

        db = wicket.getTester().getDatabase();

        assertRegisteredHook(db, RegisterHook.class);
    }


    private void assertRegisteredHook(ODatabaseDocument db, Class<? extends ORecordHook> hookClass) {
        List<Class<? extends ORecordHook>> registeredHooks = db.getHooks().keySet().stream()
                .map(ORecordHook::getClass).collect(Collectors.toList());

        assertTrue("Hook " + hookClass + " is not registered", registeredHooks.contains(hookClass));
    }


    public static class RegisterHook extends ODocumentHookAbstract {
        public RegisterHook(ODatabaseDocument database) {
            super(database);
        }

        @Override
        public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
            return DISTRIBUTED_EXECUTION_MODE.SOURCE_NODE;
        }
    }
}
