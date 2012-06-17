import java.net.InetAddress

import org.apache.commons.io.FileUtils;
import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder

miniki = [
    version: '1.0',
    copyrightYear: '2012',
    hostInfo: { InetAddress.localHost.hostAddress },
    memory: [total: {FileUtils.byteCountToDisplaySize(Runtime.runtime.totalMemory())}, free: {FileUtils.byteCountToDisplaySize(Runtime.runtime.freeMemory())}]
]

wiki2html = { content ->
    StringWriter writer = new StringWriter()
    HtmlDocumentBuilder wikiBuilder = new HtmlDocumentBuilder(writer)
    ConfluenceLanguage language = new ConfluenceLanguage()
    MarkupParser parser = new MarkupParser(language, wikiBuilder)
    parser.parse(content as String)
    writer.toString()
}

//binarySize = { path ->
//    def node = jcr.getNode(path as String)
//    FileUtils.byteCountToDisplaySize(node['jcr:content'].binary.size)
//}
