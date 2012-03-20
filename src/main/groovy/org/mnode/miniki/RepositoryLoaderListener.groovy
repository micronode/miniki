package org.mnode.miniki

import groovy.util.logging.Slf4j;

import javax.jcr.NamespaceException;
import javax.jcr.SimpleCredentials
import javax.naming.InitialContext
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.jackrabbit.core.jndi.RegistryHelper

@Slf4j
class RepositoryLoaderListener implements ServletContextListener {

    def context = new InitialContext()
    
    void contextInitialized(ServletContextEvent e) {
        File homeDir = [System.getProperty('miniki.home', System.getProperty('user.home') + '/.miniki')]
        log.info "Miniki home: [$homeDir.absolutePath]"
//        new File(homeDir, "logs").mkdirs()
        def configFile = new File(homeDir, "config.xml")
        configFile.text = RepositoryLoaderListener.getResourceAsStream("/config-persist.xml").text
        
        File repositoryLocation = [homeDir, "data"]
        
        RegistryHelper.registerRepository(context, 'miniki', configFile.absolutePath,
             repositoryLocation.absolutePath, false)
        
        def context = new InitialContext()
        def repository = context.lookup('miniki')
        def session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
        e.servletContext.setAttribute('jcr', session)
        
        try {
            session.workspace.namespaceRegistry.registerNamespace('mn', 'http://mnode.org/namespace/1.0')
        }
        catch (NamespaceException ne) {
            log.warn ne.message
        }
    }
    
    void contextDestroyed(ServletContextEvent e) {
        RegistryHelper.unregisterRepository(context, 'miniki')
    }
}
