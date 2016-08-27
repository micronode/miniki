import org.apache.commons.io.FileUtils
import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder

miniki = [
    version: '1.0',
    copyrightYear: '2012',
    hostInfo: { InetAddress.localHost.hostAddress },
    memory: [total: {FileUtils.byteCountToDisplaySize(Runtime.runtime.totalMemory())}, free: {FileUtils.byteCountToDisplaySize(Runtime.runtime.freeMemory())}],
    
    title: { node ->
        node['mn:title']?.string
    },
    
    path: { node ->
        node.path - /mn:content\// - /mn:content/
    },

    links: [
        view: { node, title = null ->
            def href = miniki.path(node)
            if (href.endsWith('/')) {
                "<a href='/view$href' class='${style.clazz(node)}'>${title ?: miniki.title(node)}</a>"
            }
            else {
                "<a href='/view$href/' class='${style.clazz(node)}'>${title ?: miniki.title(node)}</a>"
            }
        },
        edit: { node, title = null ->
            "<a href='/edit${miniki.path(node)}' class='action-suffix'>${title ?: '[Edit]'}</a>"
        },
        history: { node, title = null ->
            "<a href='/history${miniki.path(node)}' class='action-suffix'>${title ?: '[History]'}</a>"
        }
    ]
]

wiki2html = { content ->
    StringWriter writer = []
    HtmlDocumentBuilder wikiBuilder = [writer]
    wikiBuilder.emitAsDocument = false
    ConfluenceLanguage language = []
    MarkupParser parser = [language, wikiBuilder]
    parser.parse(content as String)
    writer.toString()
}

attachment = [
    binarySize :{ node ->
        FileUtils.byteCountToDisplaySize(node.getNode('jcr:content')['jcr:data'].binary.size)
    }
]


style = [
    clazz: { node ->
        if (node.isNew()) {
            'jcr-new'
        }
        else if (node.isModified()) {
            'jcr-modified'
        }
        else {
            'jcr-default'
        }
    }
]
