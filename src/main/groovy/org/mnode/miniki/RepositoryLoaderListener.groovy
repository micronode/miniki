package org.mnode.miniki

import javax.jcr.SimpleCredentials
import javax.naming.InitialContext
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.jackrabbit.core.jndi.RegistryHelper

class RepositoryLoaderListener implements ServletContextListener {

    def context = new InitialContext()
    
    void contextInitialized(ServletContextEvent e) {
        new File(System.getProperty("user.home"), ".miniki/logs").mkdirs()
        def configFile = new File(System.getProperty("user.home"), ".miniki/config.xml")
        configFile.text = RepositoryLoaderListener.getResourceAsStream("/config-persist.xml").text
        
        File repositoryLocation = [System.getProperty("user.home"), ".miniki/data"]
        
        RegistryHelper.registerRepository(context, 'miniki', configFile.absolutePath,
             repositoryLocation.absolutePath, false)
        
        def context = new InitialContext()
        def repository = context.lookup('miniki')
        def session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
        e.servletContext.setAttribute('jcr', session)
    }
    
    void contextDestroyed(ServletContextEvent e) {
        RegistryHelper.unregisterRepository(context, 'miniki')
    }
}
