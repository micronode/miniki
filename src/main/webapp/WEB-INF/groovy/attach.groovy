import java.security.MessageDigest;

import javax.jcr.Value;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload

if (request.method != 'POST') {
    forward("view.html")
}

if (ServletFileUpload.isMultipartContent(request)) {
    // Create a new file upload handler
    ServletFileUpload upload = [new DiskFileItemFactory()]

    def node
    def items = upload.getItemIterator(request)
    while (items.hasNext()) {
        def item = items.next()
        if (item.formField && item.fieldName == 'p') {
            node = context.jcr.getNode(item.openStream().text)
        }
        else {
            def digest = MessageDigest.getInstance('md5')
            def binary = node.session.valueFactory.createBinary(item.openStream())
//            item.openStream().with {
            binary.stream.with {
                byte[] data = new byte[8192]
                int bytesRead
                while ((bytesRead = read(data)) >= 0) {
                    digest.update data, 0, bytesRead
                }
            }
            def checksum = new String(Hex.encodeHex(digest.digest()))
            
            def attachNode = node.session.rootNode << 'mn:attachments'
            def attachPath = checksum.split(/(?<=\G.{2})/)
            attachPath[0..-1].each {
                attachNode = attachNode << it
            }
            if (attachNode[attachPath[-1]]) {
                attachNode = attachNode[attachPath[-1]]
            }
//            if (attachNode.isNew()) {
            else {
                attachNode = attachNode.addNode(attachPath[-1], 'nt:file')
//                println "Creating node: $attachNode.path"
                attachNode.addMixin('mix:referenceable')
                attachNode.session.save {
//                    attachNode['jcr:content'] = attachNode.session.valueFactory.createBinary(item.openStream())
                    def resource = attachNode.addNode('jcr:content', 'nt:resource')
                    resource['jcr:data'] = binary
//                    println "mimetype=$item.contentType"
                    resource['jcr:mimeType'] = item.contentType
                    resource['jcr:lastModified'] = Calendar.instance
                }
            }
            
            if (node) {
                node.session.save {
                    def attachments = node << 'mn:attachments'
                    attachments[item.name] = attachNode
                }
//                    attachments = node.attachments.values as List
//                }
//                else {
//                    attachments = []
//                }
//                attachments << node.session.valueFactory.createValue(attachNode)
//                
//                node.session.save {
//                    node['attachments'] = attachments as Value[]
//                }
            }
        }
    }
    redirect("view${node.path - /mn:content\// - /mn:content/}")
}
