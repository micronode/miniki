package org.mnode.miniki

import groovy.servlet.ServletBinding;
import groovy.servlet.TemplateServlet
import groovy.util.logging.Slf4j;


@Slf4j
class MinikiTemplateServlet extends TemplateServlet {

    @Override
    protected void setVariables(ServletBinding binding) {
        super.setVariables(binding);
//        binding.setVariable('miniki', [version: '1.0', copyrightYear: '2012'])
        
        final defaultScriptContentLoader = { String path ->
            def file = new File(path)
            if(file.exists()){
                return file.text
            }
            InputStream is = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
            if(!is) return ""
            is.text
        }
        
        ConfigSlurper extensionLoader = []
        ConfigObject extensions = []
//        new File('WEB-INF/extensions').listFiles({file -> file.name.endsWith('.groovy')} as FileFilter).each {
//            log.info "Loading extension: $it.name"
//            String extensionScript = defaultScriptContentLoader('WEB-INF/extensions/miniki.groovy')
            String extensionScript = binding.context.getResource('/WEB-INF/extensions/miniki.groovy').text
            log.info "Extension Script: $extensionScript"
            extensions.merge(extensionLoader.parse(extensionScript))
//        }
        extensions.entrySet().each {
            log.info "Binding variable: $it.key, $it.value"
            binding.setVariable(it.key, it.value)
        }
//        binding.setVariable('jcr', binding.context.getAttribute('jcr'))
        // extensions..
//        def extensions = binding.context.getAttribute('jcr').getNode('/mn:extensions')
        
        // todo: parse and load enabled extensions..
    }
}
