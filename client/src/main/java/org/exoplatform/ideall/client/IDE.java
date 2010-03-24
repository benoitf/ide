package org.exoplatform.ideall.client;

import org.exoplatform.gwtframework.commons.initializer.event.ApplicationConfigurationReceivedEvent;
import org.exoplatform.gwtframework.ui.client.smartgwt.dialogs.SmartGWTDialogs;
import org.exoplatform.gwtframework.ui.client.smartgwt.loader.SmartGWTLoader;
import org.exoplatform.ideall.client.application.DevToolForm;
import org.exoplatform.ideall.client.common.CommonActionsComponent;
import org.exoplatform.ideall.client.common.HelpActionsComponent;
import org.exoplatform.ideall.client.gadgets.GadgetActionsComponent;
import org.exoplatform.ideall.client.groovy.GroovyActionsComponent;
import org.exoplatform.ideall.client.model.ApplicationContext;
import org.exoplatform.ideall.client.model.configuration.Configuration;
import org.exoplatform.ideall.client.model.conversation.ConversationServiceImpl;
import org.exoplatform.ideall.client.model.discovery.MockEntryPointSeviceImpl;
import org.exoplatform.ideall.client.model.gadget.GadgetServiceImpl;
import org.exoplatform.ideall.client.model.groovy.GroovyServiceImpl;
import org.exoplatform.ideall.client.model.jcrservice.RepositoryServiceImpl;
import org.exoplatform.ideall.client.model.settings.SettingsServiceImpl;
import org.exoplatform.ideall.client.model.template.TemplateServiceImpl;
import org.exoplatform.ideall.client.model.vfs.webdav.WebDavVirtualFileSystem;
import org.exoplatform.ideall.client.model.wadl.WadlServiceImpl;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:dmitry.ndp@exoplatform.com.ua">Dmytro Nochevnov</a>
 * @version $Id: $
*/
public class IDE
{

   public IDE()
   {
	   
      for (int i = 0; i < 30; i++)
      {
         System.out.println();
      }

      new SmartGWTLoader();

      new SmartGWTDialogs();

      HandlerManager eventBus = new HandlerManager(null);

      new Configuration(eventBus);

      //eventBus.addHandler(ExceptionThrownEvent.TYPE, new ExceptionThrownHandlerImpl());
      ExceptionThrownEventHandlerInitializer.initialize(eventBus);
      eventBus.addHandler(ApplicationConfigurationReceivedEvent.TYPE, Configuration.getInstance());

      /*
       * Initializing services
       */

      new SettingsServiceImpl(eventBus);

      new ConversationServiceImpl(eventBus);

      new RepositoryServiceImpl(eventBus);

      new WebDavVirtualFileSystem(eventBus);

      new GroovyServiceImpl(eventBus);

      new GadgetServiceImpl(eventBus);

      new TemplateServiceImpl(eventBus);
      
      new WadlServiceImpl(eventBus);
      
      new MockEntryPointSeviceImpl(eventBus);

      final ApplicationContext context = new ApplicationContext();

      /*
       * PLUGINS INITIALIZATION
       */

      context.getComponents().add(new CommonActionsComponent());
      context.getComponents().add(new GroovyActionsComponent());
      
      context.getComponents().add(new GadgetActionsComponent());
      
      context.getComponents().add(new HelpActionsComponent());
      
      // new HistoryManager(eventBus, context); // commented to fix the bug with javascript error in IE8 (WBT-321)

      new DevToolForm(eventBus, context);

      Configuration.getInstance().loadConfiguration(eventBus);
   }

}
