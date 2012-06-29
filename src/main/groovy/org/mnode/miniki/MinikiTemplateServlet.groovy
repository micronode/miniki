package org.mnode.miniki

import groovy.servlet.ServletBinding;
import groovy.servlet.TemplateServlet
import groovy.util.logging.Slf4j;


@Slf4j
class MinikiTemplateServlet extends TemplateServlet {

    Long extensionsLastModified
    
    // keeping a map of ConfigObjects as ConfigObject.merge() doesn't seem to work properly..
    ConfigObject extensions = []
    
    @Override
    protected void setVariables(ServletBinding binding) {
        super.setVariables(binding);
//        binding.setVariable('miniki', [version: '1.0', copyrightYear: '2012'])
        
        final defaultScriptContentLoader = { String path ->
            def file = new File(path)
            if(file.exists()){
                return file.text
            }
            //InputStream is = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
            InputStream is = getClass().getResourceAsStream(path)
            if(!is) return ""
            is.text
        }
        
        /*
        // extension dir..
        File extensionDir = [binding.context.getResource('/WEB-INF/extensions').toURI()]
        extensionDir.listFiles().each { extension ->
            if (!extensionsLastModified[extension] || extension.lastModified() > extensionsLastModified[extension]) {
                String extensionScript = extension.text
                log.info "Extension Script: $extensionScript"
                ConfigSlurper extensionLoader = []
                extensions = extensions.merge(extensionLoader.parse(extensionScript))
//                ConfigObject extensionConfig = extensionLoader.parse(extensionScript)
                extensionsLastModified[extension] = extension.lastModified()
//                extensions[extension] = extensionConfig
                StringWriter extensionsOut = []
                extensions.writeTo(extensionsOut)
                log.info "Extensions: $extensionsOut"
            }
        }
        */
        
        String extensionScript
        try {
        File extensionsFile = [binding.context.getResource('/WEB-INF/extensions.groovy').toURI()]
        if (!extensionsLastModified || extensionsFile.lastModified() > extensionsLastModified) {
            extensionScript = extensionsFile.text
        }
        } catch (Exception e) {
            extensionScript = binding.context.getResourceAsStream('/WEB-INF/extensions.groovy')?.text
        }
        if (extensionScript) {
            log.debug "Extension Script: $extensionScript"
            ConfigSlurper extensionLoader = []
//            extensions = extensions.merge(extensionLoader.parse(extensionScript))
            extensions = extensionLoader.parse(extensionScript)
            extensionsLastModified = extensionsFile.lastModified()
//                extensions[extension] = extensionConfig
            StringWriter extensionsOut = []
            extensions.writeTo(extensionsOut)
            log.debug "Extensions: $extensionsOut"

        }
        extensions.entrySet().each {
            log.debug "Binding variable: $it.key, $it.value"
            binding.setVariable(it.key, it.value)
        }
//        binding.setVariable('jcr', binding.context.getAttribute('jcr'))
        // extensions..
//        def extensions = binding.context.getAttribute('jcr').getNode('/mn:extensions')
        
        // todo: parse and load enabled extensions..
    }
}
